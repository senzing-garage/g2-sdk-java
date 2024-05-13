package com.senzing.g2.engine;

import static com.senzing.g2.engine.NativeEngine.*;
import static com.senzing.g2.engine.SzFlag.*;
import static com.senzing.g2.engine.SzRecord.*;
import static com.senzing.g2.engine.Utilities.*;
import static com.senzing.util.JsonUtilities.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import static org.junit.jupiter.api.TestInstance.Lifecycle;
import static org.junit.jupiter.params.provider.Arguments.*;

import com.senzing.g2.engine.SzRecord.SzDataSourceCode;
import com.senzing.g2.engine.SzRecord.SzFullAddress;
import com.senzing.g2.engine.SzRecord.SzFullName;
import com.senzing.g2.engine.SzRecord.SzNameByParts;
import com.senzing.g2.engine.SzRecord.SzPhoneNumber;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import javax.json.JsonObject;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.jupiter.params.provider.Arguments;

/** Unit tests for {@link SzCoreDiagnostic}. */
@TestInstance(Lifecycle.PER_CLASS)
@Execution(ExecutionMode.SAME_THREAD)
@TestMethodOrder(OrderAnnotation.class)
@Disabled
public class SzCoreEngineTest extends AbstractTest {
  private static final String CUSTOMERS_DATA_SOURCE = "CUSTOMERS";
  private static final String EMPLOYEES_DATA_SOURCE = "EMPLOYEES";

  private static final SzDataSourceCode CUSTOMERS = SzDataSourceCode.of(CUSTOMERS_DATA_SOURCE);

  private static final SzDataSourceCode EMPLOYEES = SzDataSourceCode.of(EMPLOYEES_DATA_SOURCE);

  private static final SzRecord RECORD_JOE_SCHMOE =
      new SzRecord(
          SzFullName.of("Joe Schmoe"),
          SzPhoneNumber.of("702-555-1313"),
          SzFullAddress.of("101 Main Street, Las Vegas, NV 89101"));

  private static final SzRecord RECORD_JANE_SMITH =
      new SzRecord(
          SzFullName.of("Jane Smith"),
          SzPhoneNumber.of("702-555-1414"),
          SzFullAddress.of("440 N Rancho Blvd, Las Vegas, NV 89101"));

  private static final SzRecord RECORD_JOHN_DOE =
      new SzRecord(
          SzFullName.of("John Doe"),
          SzPhoneNumber.of("702-555-1717"),
          SzFullAddress.of("777 W Sahara Blvd, Las Vegas, NV 89107"));

  private static final SzRecord RECORD_JAMES_MORIARTY =
      new SzRecord(
          SzNameByParts.of("James", "Moriarty"),
          SzPhoneNumber.of("44-163-555-1313"),
          SzFullAddress.of("16A Upper Montagu St, London, W1H 2PB, England"));

  private static final SzRecord RECORD_SHERLOCK_HOLMES =
      new SzRecord(
          SzFullName.of("Sherlock Holmes"),
          SzPhoneNumber.of("44-163-555-1212"),
          SzFullAddress.of("221b Baker Street, London, NW1 6XE, England"));

  private static final SzRecord RECORD_JOHN_WATSON =
      new SzRecord(
          SzFullName.of("Dr. John H. Watson"),
          SzPhoneNumber.of("44-163-555-1414"),
          SzFullAddress.of("221b Baker Street, London, NW1 6XE, England"));

  private static final SzRecord RECORD_JOANN_SMITTH =
      new SzRecord(
          SzFullName.of("Joann Smith"),
          SzPhoneNumber.of("702-888-3939"),
          SzFullAddress.of("101 Fifth Ave, Las Vegas, NV 89118"),
          SzDateOfBirth.of("15-MAY-1983"));

  private static final SzRecord RECORD_BILL_WRIGHT =
      new SzRecord(
          SzNameByParts.of("Bill", "Wright", "AKA"),
          SzNameByParts.of("William", "Wright", "PRIMARY"),
          SzPhoneNumber.of("702-444-2121"),
          SzAddressByParts.of("101 Main StreetFifth Ave", "Las Vegas", "NV", "89118"),
          SzDateOfBirth.of("15-MAY-1983"));

  private SzCoreEnvironment env = null;

  @BeforeAll
  public void initializeEnvironment() {
    this.beginTests();
    this.initializeTestEnvironment();
    String settings = this.getRepoSettings();

    String instanceName = this.getClass().getSimpleName();

    NativeConfig nativeConfig = new G2ConfigJNI();
    NativeConfigMgr nativeConfigMgr = new G2ConfigMgrJNI();

    long configHandle = 0L;
    try {
      // initialize the native config
      int returnCode = nativeConfig.init(instanceName, settings, false);
      if (returnCode != 0) {
        throw new RuntimeException(nativeConfig.getLastException());
      }

      // initialize the native config manager
      returnCode = nativeConfigMgr.init(instanceName, settings, false);
      if (returnCode != 0) {
        throw new RuntimeException(nativeConfigMgr.getLastException());
      }

      // create the default config
      Result<Long> result = new Result<>();
      returnCode = nativeConfig.create(result);
      if (returnCode != 0) {
        throw new RuntimeException(nativeConfig.getLastException());
      }

      // get the config handle
      configHandle = result.getValue();

      // export the default config JSON
      StringBuffer sb = new StringBuffer();
      returnCode = nativeConfig.save(configHandle, sb);
      if (returnCode != 0) {
        throw new RuntimeException(nativeConfig.getLastException());
      }

      // add the CUSTOMERS data source
      sb.delete(0, sb.length());
      returnCode =
          nativeConfig.addDataSource(
              configHandle, "{\"DSRC_CODE\": \"" + CUSTOMERS_DATA_SOURCE + "\"}", sb);
      if (returnCode != 0) {
        throw new RuntimeException(nativeConfig.getLastException());
      }

      // add the EMPLOYEES data source
      sb.delete(0, sb.length());
      returnCode =
          nativeConfig.addDataSource(
              configHandle, "{\"DSRC_CODE\": \"" + EMPLOYEES_DATA_SOURCE + "\"}", sb);
      if (returnCode != 0) {
        throw new RuntimeException(nativeConfig.getLastException());
      }

      // export the modified config JSON
      sb.delete(0, sb.length());
      returnCode = nativeConfig.save(configHandle, sb);
      if (returnCode != 0) {
        throw new RuntimeException(nativeConfig.getLastException());
      }

      // set the modified config
      String modifiedConfig = sb.toString();

      // close the config handle
      returnCode = nativeConfig.close(configHandle);
      configHandle = 0L;
      if (returnCode != 0) {
        throw new RuntimeException(nativeConfig.getLastException());
      }

      // add the config
      returnCode = nativeConfigMgr.addConfig(modifiedConfig, "Test", result);
      if (returnCode != 0) {
        throw new RuntimeException(nativeConfigMgr.getLastException());
      }

      // get the config ID
      long configId = result.getValue();

      // set the default config
      returnCode = nativeConfigMgr.setDefaultConfigID(configId);
      if (returnCode != 0) {
        throw new RuntimeException(nativeConfigMgr.getLastException());
      }

    } finally {
      if (configHandle != 0L) nativeConfig.close(configHandle);
      configHandle = 0L;
      nativeConfig.destroy();
      nativeConfigMgr.destroy();
    }

    this.env =
        SzCoreEnvironment.newBuilder()
            .instanceName(instanceName)
            .settings(settings)
            .verboseLogging(false)
            .build();
  }

  @AfterAll
  public void teardownEnvironment() {
    try {
      if (this.env != null) {
        this.env.destroy();
        this.env = null;
      }
      this.teardownTestEnvironment();
    } finally {
      this.endTests();
    }
  }

  private List<Arguments> getAddRecordArguments() {
    List<Arguments> result = new LinkedList<>();
    return result;
  }

  // @ParameterizedTest
  // @MethodSource("getAddRecordArguments")
  void testAddRecord(
      SzRecordKey recordKey,
      SzRecord recordDefinition,
      Set<SzFlag> flags,
      boolean viaKey,
      Class<?> expectedExceptionType) {
    this.performTest(
        () -> {
          try {
            SzEngine engine = this.env.getEngine();

            String result = null;
            if (viaKey) {
              result = engine.addRecord(recordKey, recordDefinition.toString(), flags);

            } else {
              result =
                  engine.addRecord(
                      recordKey.dataSourceCode(),
                      recordKey.recordId(),
                      recordDefinition.toString(),
                      flags);
            }

            if (expectedExceptionType != null) {
              fail(
                  "Unexpectedly succeeded in adding record: viaKey=[ "
                      + viaKey
                      + " ], recordKey=[ "
                      + recordKey
                      + " ], definition=[ "
                      + recordDefinition
                      + " ], expectedException=[ "
                      + expectedExceptionType
                      + " ]");
            }

            // check if we are expecting info
            if (flags != null && flags.contains(SZ_WITH_INFO)) {
              // parse the result as JSON and check that it parses
              JsonObject jsonObject = parseJsonObject(result);

              assertTrue(
                  jsonObject.containsKey("DATA_SOURCE"), "Info message lacking DATA_SOURCE key");
              assertTrue(jsonObject.containsKey("RECORD_ID"), "Info message lacking RECORD_ID key");
              assertTrue(
                  jsonObject.containsKey("AFFECTED_ENTITIES"),
                  "Info message lacking AFFECTED_ENTITIES key");
            } else {
              assertEquals(
                  SzCoreEngine.NO_INFO,
                  result,
                  "No INFO requested, but non-empty response received");
            }

          } catch (Exception e) {
            if (expectedExceptionType == null) {
              fail("Unexpectedly failed adding a record", e);

            } else if (!expectedExceptionType.isInstance(e)) {
              assertInstanceOf(
                  expectedExceptionType, e, "addRecord() failed with an unexpected exception type");
            }
          }
        });
  }

  @Test
  void testCloseExport() {}

  @Test
  void testCountRedoRecords() {}

  @Test
  void testDeleteRecord() {}

  @Test
  void testDeleteRecord2() {}

  @Test
  void testDestroy() {}

  @Test
  void testEncodeDataSources() {}

  @Test
  void testEncodeEntityIds() {}

  @Test
  void testEncodeRecordKeys() {}

  @Test
  void testExportCsvEntityReport() {}

  @Test
  void testExportJsonEntityReport() {}

  @Test
  void testFetchNext() {}

  @Test
  void testFindNetworkByEntityId() {}

  @Test
  void testFindNetworkByRecordId() {}

  @Test
  void testFindPathByEntityId() {}

  @Test
  void testFindPathByRecordId() {}

  @Test
  void testFindPathByRecordId2() {}

  @Test
  void testGetEntity() {}

  @Test
  void testGetEntity2() {}

  @Test
  void testGetEntity3() {}

  @Test
  void testGetRecord() {}

  @Test
  void testGetRecord2() {}

  @Test
  void testGetRedoRecord() {}

  @Test
  void testGetStats() {}

  @Test
  void testGetVirtualEntity() {}

  @Test
  void testHowEntity() {}

  @Test
  void testIsDestroyed() {}

  @Test
  void testPrimeEngine() {}

  @Test
  void testProcessRedoRecord() {}

  @Test
  void testReevaluateEntity() {}

  @Test
  void testReevaluateRecord() {}

  @Test
  void testReevaluateRecord2() {}

  @Test
  void testSearchByAttributes() {}

  @Test
  void testSearchByAttributes2() {}

  @Test
  void testWhyEntities() {}

  @Test
  void testWhyRecordInEntity() {}

  @Test
  void testWhyRecordInEntity2() {}

  @Test
  void testWhyRecords() {}

  @Test
  void testWhyRecords2() {}
}
