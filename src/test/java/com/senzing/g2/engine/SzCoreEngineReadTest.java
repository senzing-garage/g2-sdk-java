package com.senzing.g2.engine;

import java.util.List;
import java.util.Set;
import java.util.LinkedList;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.shadow.com.univocity.parsers.annotations.Parsed;
import org.junit.jupiter.params.shadow.com.univocity.parsers.common.processor.CustomMatcher;

import com.senzing.g2.engine.SzRecord.SzAddressByParts;
import com.senzing.g2.engine.SzRecord.SzDataSourceCode;
import com.senzing.g2.engine.SzRecord.SzDateOfBirth;
import com.senzing.g2.engine.SzRecord.SzFullAddress;
import com.senzing.g2.engine.SzRecord.SzFullName;
import com.senzing.g2.engine.SzRecord.SzNameByParts;
import com.senzing.g2.engine.SzRecord.SzPhoneNumber;
import com.senzing.g2.engine.SzRecord.SzRecordId;
import com.senzing.g2.engine.SzRecord.SzRecordKey;

import static org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import static org.junit.jupiter.api.TestInstance.Lifecycle;

import javax.json.Json;
import javax.json.JsonObjectBuilder;
import javax.json.JsonObject;
import javax.json.JsonArray;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;
import static com.senzing.util.JsonUtilities.*;
import static com.senzing.g2.engine.NativeEngine.*;
import static org.junit.jupiter.params.provider.Arguments.*;
import static com.senzing.g2.engine.SzFlag.*;
import static com.senzing.g2.engine.Utilities.*;
import static com.senzing.g2.engine.SzRecord.*;

/**
 * Unit tests for {@link SzCoreDiagnostic}.
 */
@TestInstance(Lifecycle.PER_CLASS)
@Execution(ExecutionMode.SAME_THREAD)
public class SzCoreEngineReadTest extends AbstractTest {
    private static final String PASSENGERS          = "PASSENGERS";
    private static final String EMPLOYEES           = "EMPLOYEES";
    private static final String VIPS                = "VIPS";
    private static final String MARRIAGES           = "MARRIAGES";
    private static final String UNKNOWN_DATA_SOURCE = "UNKNOWN";
  
    private static final SzRecordKey ABC123
        = SzRecordKey.of(PASSENGERS,"ABC123");
    private static final SzRecordKey DEF456
        = SzRecordKey.of(PASSENGERS, "DEF456");
    private static final SzRecordKey GHI789
        = SzRecordKey.of(PASSENGERS, "GHI789");
    private static final SzRecordKey JKL012
        = SzRecordKey.of(PASSENGERS, "JKL012");
    private static final SzRecordKey MNO345
        = SzRecordKey.of(EMPLOYEES, "MNO345");
    private static final SzRecordKey PQR678
        = SzRecordKey.of(EMPLOYEES, "PQR678");
    private static final SzRecordKey STU901
        = SzRecordKey.of(VIPS, "STU901");
    private static final SzRecordKey XYZ234
        = SzRecordKey.of(VIPS, "XYZ234");
    private static final SzRecordKey ZYX321
        = SzRecordKey.of(EMPLOYEES, "ZYX321");
    private static final SzRecordKey CBA654
        = SzRecordKey.of(EMPLOYEES, "CBA654");
  
    private static final SzRecordKey BCD123
        = SzRecordKey.of(MARRIAGES, "BCD123");
    private static final SzRecordKey CDE456
        = SzRecordKey.of(MARRIAGES, "CDE456");
    private static final SzRecordKey EFG789
        = SzRecordKey.of(MARRIAGES, "EFG789");
    private static final SzRecordKey FGH012
        = SzRecordKey.of(MARRIAGES, "FGH012");
  
    private static final List<Set<SzFlag>> READ_FLAG_SETS;
    static {
        List<Set<SzFlag>> list = new LinkedList<>();
        list.add(null);
        list.add(EnumSet.noneOf(SzFlag.class));
        list.add(EnumSet.of(SZ_NO_FLAGS));
        list.add(SZ_ENTITY_DEFAULT_FLAGS);
        list.add(SZ_ENTITY_BRIEF_DEFAULT_FLAGS);
        list.add(Collections.unmodifiableSet(EnumSet.of(
            SZ_ENTITY_INCLUDE_ENTITY_NAME,
            SZ_ENTITY_INCLUDE_RECORD_SUMMARY,
            SZ_ENTITY_INCLUDE_RECORD_DATA,
            SZ_ENTITY_INCLUDE_RECORD_MATCHING_INFO)));
        list.add(Collections.unmodifiableSet(EnumSet.of(SZ_ENTITY_INCLUDE_ENTITY_NAME)));
        READ_FLAG_SETS = Collections.unmodifiableList(list);
    }

    private static final List<Boolean> VIA_KEY_LIST
        = List.of(true,false,true);
    
    private static final Map<SzRecordKey, Long> LOADED_RECORD_MAP
        = Collections.synchronizedMap(new LinkedHashMap<>());

    private static final Map<Long, Set<SzRecordKey>> LOADED_ENTITY_MAP
        = Collections.synchronizedMap(new LinkedHashMap<>());
    
    private static final List<SzRecordKey> RECORD_KEYS
        = List.of(ABC123,
                  DEF456,
                  GHI789,
                  JKL012,
                  MNO345,
                  PQR678,
                  STU901,
                  XYZ234,
                  ZYX321,
                  CBA654,
                  BCD123,
                  CDE456,
                  EFG789,
                  FGH012);
    
    private SzCoreEnvironment env = null;

    @BeforeAll
    public void initializeEnvironment() {
        this.beginTests();
        this.initializeTestEnvironment();
        String settings = this.getRepoSettings();
        
        String instanceName = this.getClass().getSimpleName();
        
        // now we just need the entity ID's for the loaded records to use later
        NativeEngine nativeEngine = new G2JNI();
        try {
            int returnCode = nativeEngine.init(instanceName, settings, false);
            if (returnCode != 0) {
                throw new RuntimeException(nativeEngine.getLastException());
            }

            // get the loaded records and entity ID's
            StringBuffer sb = new StringBuffer();
            for (SzRecordKey key : RECORD_KEYS) {
                // clear the buffer
                sb.delete(0, sb.length());
                returnCode = nativeEngine.getEntityByRecordID(
                    key.dataSourceCode(), key.recordId(), sb);
                if (returnCode != 0) {
                    throw new RuntimeException(nativeEngine.getLastException());
                }
                // parse the JSON 
                JsonObject  jsonObj     = parseJsonObject(sb.toString());
                JsonObject  entity      = getJsonObject(jsonObj, "RESOLVED_ENTITY");
                Long        entityId    = getLong(entity, "ENTITY_ID");
                LOADED_RECORD_MAP.put(key, entityId);
                Set<SzRecordKey> recordKeySet = LOADED_ENTITY_MAP.get(entityId);
                if (recordKeySet == null) {
                    recordKeySet = new LinkedHashSet<>();
                    LOADED_ENTITY_MAP.put(entityId, recordKeySet);
                }
                recordKeySet.add(key);
            };

        } finally {
            nativeEngine.destroy();
        }

        this.env = SzCoreEnvironment.newBuilder()
                                      .instanceName(instanceName)
                                      .settings(settings)
                                      .verboseLogging(false)
                                      .build();
    }

    public static Long getEntityId(SzRecordKey recordKey) {
        return getEntityId(recordKey);
    }

    /**
     * Overridden to configure some data sources.
     */
    protected void prepareRepository() {
        File repoDirectory = this.getRepositoryDirectory();

        Set<String> dataSources = new LinkedHashSet<>();
        dataSources.add("PASSENGERS");
        dataSources.add("EMPLOYEES");
        dataSources.add("VIPS");
        dataSources.add("MARRIAGES");

        File passengerFile = this.preparePassengerFile();
        File employeeFile = this.prepareEmployeeFile();
        File vipFile = this.prepareVipFile();
        File marriagesFile = this.prepareMarriagesFile();

        employeeFile.deleteOnExit();
        passengerFile.deleteOnExit();
        vipFile.deleteOnExit();
        marriagesFile.deleteOnExit();

        RepositoryManager.configSources(repoDirectory,
                                        dataSources,
                                        true);

        RepositoryManager.loadFile(repoDirectory,
                                    passengerFile,
                                    PASSENGERS,
                                    true);

        RepositoryManager.loadFile(repoDirectory,
                                    employeeFile,
                                    EMPLOYEES,
                                    true);

        RepositoryManager.loadFile(repoDirectory,
                                    vipFile,
                                    VIPS,
                                    true);

        RepositoryManager.loadFile(repoDirectory,
                                    marriagesFile,
                                    MARRIAGES,
                                    true);
    }

    private static String relationshipKey(SzRecordKey recordKey1,
                                          SzRecordKey recordKey2) 
    {
        String rec1 = recordKey1.toString();
        String rec2 = recordKey2.toString();
        if (rec1.compareTo(rec2) <= 0) {
            return rec1 + "|" + rec2;
        } else {
            return rec2 + "|" + rec1;
        }
    }

    private File preparePassengerFile() {
        String[] headers = {
            "RECORD_ID", "NAME_FIRST", "NAME_LAST", "PHONE_NUMBER", "ADDR_FULL",
            "DATE_OF_BIRTH"};

        String[][] passengers = {
            {ABC123.recordId(), "Joe", "Schmoe", "702-555-1212",
                "101 Main Street, Las Vegas, NV 89101", "1981-01-12"},
            {DEF456.recordId(), "Joanne", "Smith", "212-555-1212",
                "101 Fifth Ave, Las Vegas, NV 10018", "1983-05-15"},
            {GHI789.recordId(), "John", "Doe", "818-555-1313",
                "100 Main Street, Los Angeles, CA 90012", "1978-10-17"},
            {JKL012.recordId(), "Jane", "Doe", "818-555-1212",
                "100 Main Street, Los Angeles, CA 90012", "1979-02-05"}
        };
        return this.prepareCSVFile("test-passengers-", headers, passengers);
    }

    private File prepareEmployeeFile() {
        String[] headers = {
            "RECORD_ID", "NAME_FIRST", "NAME_LAST", "PHONE_NUMBER", "ADDR_FULL",
            "DATE_OF_BIRTH","MOTHERS_MAIDEN_NAME", "SSN_NUMBER"};

        String[][] employees = {
            {MNO345.recordId(), "Joseph", "Schmoe", "702-555-1212",
                "101 Main Street, Las Vegas, NV 89101", "1981-01-12", "WILSON",
                "145-45-9866"},
            {PQR678.recordId(), "Jo Anne", "Smith", "212-555-1212",
                "101 Fifth Ave, Las Vegas, NV 10018", "1983-05-15", "JACOBS",
                "213-98-9374"},
            {ZYX321.recordId(), "Mark", "Hightower", "563-927-2833",
                "1882 Meadows Lane, Las Vegas, NV 89125", "1981-06-22", "JENKINS",
                "873-22-4213"},
            {CBA654.recordId(), "Mark", "Hightower", "781-332-2824",
                "2121 Roscoe Blvd, Los Angeles, CA 90232", "1980-09-09", "BROOKS",
                "827-27-4829"}
        };

        return this.prepareJsonArrayFile("test-employees-", headers, employees);
  }

    private File prepareVipFile() {
        String[] headers = {
            "RECORD_ID", "NAME_FIRST", "NAME_LAST", "PHONE_NUMBER", "ADDR_FULL",
            "DATE_OF_BIRTH","MOTHERS_MAIDEN_NAME"};

        String[][] vips = {
            {STU901.recordId(), "John", "Doe", "818-555-1313",
                "100 Main Street, Los Angeles, CA 90012", "1978-10-17", "GREEN"},
            {XYZ234.recordId(), "Jane", "Doe", "818-555-1212",
                "100 Main Street, Los Angeles, CA 90012", "1979-02-05", "GRAHAM"}
        };

        return this.prepareJsonFile("test-vips-", headers, vips);
    }

    private File prepareMarriagesFile() {
        String[] headers = {
            "RECORD_ID", "NAME_FULL", "AKA_NAME_FULL", "PHONE_NUMBER", "ADDR_FULL",
            "MARRIAGE_DATE", "DATE_OF_BIRTH", "GENDER", "RELATIONSHIP_TYPE",
            "RELATIONSHIP_ROLE", "RELATIONSHIP_KEY" };

        String[][] spouses = {
            {BCD123.recordId(), "Bruce Wayne", "Batman", "201-765-3451",
                "101 Wayne Manor Rd; Gotham City, NJ 07017", "2008-06-05",
                "1971-09-08", "M", "SPOUSE", "HUSBAND",
                relationshipKey(BCD123, CDE456)},
            {CDE456.recordId(), "Selina Kyle", "Catwoman", "201-875-2314",
                "101 Wayne Manor Rd; Gotham City, NJ 07017", "2008-06-05",
                "1981-12-05", "F", "SPOUSE", "WIFE",
                relationshipKey(BCD123, CDE456)},
            {EFG789.recordId(), "Barry Allen", "The Flash", "330-982-2133",
                "1201 Main Street; Star City, OH 44308", "2014-11-07",
                "1986-03-04", "M", "SPOUSE", "HUSBAND",
                relationshipKey(EFG789, FGH012)},
            {FGH012.recordId(), "Iris West-Allen", "", "330-675-1231",
                "1201 Main Street; Star City, OH 44308", "2014-11-07",
                "1986-05-14", "F", "SPOUSE", "WIFE",
                relationshipKey(EFG789, FGH012)}
        };

        return this.prepareJsonFile("test-marriages-", headers, spouses);
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

    private List<Arguments> getGetEntityParameters() {
        List<Arguments>         result      = new LinkedList<>();

        for (SzRecordKey key : RECORD_KEYS) {
            // check if this record was loaded
            boolean loaded = LOADED_RECORD_MAP.containsKey(key);

            for (Boolean viaKey : VIA_KEY_LIST) {
                for (Set<SzFlag> flagSet : READ_FLAG_SETS) {

                    boolean unknownDataSource = UNKNOWN_DATA_SOURCE.equals(
                        key.dataSourceCode());

                    Class<?> expectedException = null;
                    if (unknownDataSource) {
                        expectedException = SzUnknownDataSourceException.class;
                    } else if (!loaded) {
                        expectedException = SzNotFoundException.class;
                    }
                    result.add(Arguments.of(key, flagSet, viaKey, expectedException));

                    // no need to try different flags if not loaded
                    if (!loaded) break;
                }
            }
        }
        return result;
    }

    @ParameterizedTest
    @MethodSource("getGetEntityByRecordParameters")
    void testGetEntityByRecordId(SzRecordKey    recordKey,
                                 long           entityId,
                                 Set<SzFlag>    flags,
                                 boolean        viaKey,
                                 Class<?>       recordExceptionType,
                                 Class<?>       entityExceptionType)
    {
        this.performTest(() -> {
            try {
                SzEngine engine = this.env.getEngine();

                String result = null;
                if (viaKey) {
                    result = engine.getEntity(recordKey, flags);

                } else {
                    result = engine.getEntity(
                        recordKey.dataSourceCode(),
                        recordKey.recordId(),
                        flags);
                }

                if (expectedExceptionType != null) {
                    fail("Unexpectedly succeeded in getting an entity: viaKey=[ "
                         + viaKey + " ], recordKey=[ " + recordKey 
                         + " ], flags=[ " + SzFlag.toString(flags)
                         + " ], expectedException=[ " + expectedExceptionType
                         + " ]");
                }
                
                // parse the result
                JsonObject  jsonObject = null;
                try {
                    jsonObject = parseJsonObject(result);
                } catch (Exception e) {
                    fail("Failed to parse entity result JSON for record (" + recordKey + ")"
                         + result, e);
                }

                // check if we have an expected entity ID
                Long expectedEntityId = LOADED_RECORD_MAP.get(recordKey);

                // get the entity
                JsonObject  entity = getJsonObject(jsonObject, "RESOLVED_ENTITY");

                assertNotNull(entity, "No RESOLVED_ENTITY property in entity JSON for record (" 
                              + recordKey + "): " + result);

                // get the entity ID
                Long entityId = getLong(entity, "ENTITY_ID");

                assertNotNull(entity, "No ENTITY_ID property in entity JSON for record (" + recordKey
                              + "): " + result);


                if (expectedEntityId != null) {
                    assertEquals(expectedEntityId, entityId, 
                        "Unecpeted entity ID for record: " + recordKey);
                }

                // update the loaded record map with an entity ID
                LOADED_RECORD_MAP.put(recordKey, entityId);
                
            } catch (Exception e) {
                String description = "";
                if (e instanceof SzException) {
                    SzException sze = (SzException) e;
                    description = "errorCode=[ " + sze.getErrorCode()
                        + " ], methodSignature=[ " + sze.getMethodSignature()
                        + " ], parameters=[ " + sze.getMethodParameters()
                        + " ], exception=[ " + e.toString() + " ]";
                } else {
                    description = e.toString();
                }

                if (expectedExceptionType == null) {
                    fail("Unexpectedly failed getting entity by record: "
                         + description, e);

                } else if (expectedExceptionType != e.getClass()) {
                    assertInstanceOf(
                        expectedExceptionType, e, 
                        "get-entity-by-record failed with an unexpected exception type: "
                        + description);
                }
            }
        });
    }

    //@Test
    void testGetEntityByEntityId(long         entityId,
                                 Set<SzFlag>  flags,
                                 boolean      viaKey,
                                 Class<?>     expectedExceptionType)
    {

    }

    @Test
    void testGetRecord() {

    }

    @Test
    void testSearchByAttributes() {

    }

}
