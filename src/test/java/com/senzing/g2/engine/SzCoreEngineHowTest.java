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
@TestMethodOrder(OrderAnnotation.class)
public class SzCoreEngineHowTest extends AbstractTest {
    private static final String CUSTOMERS_DATA_SOURCE   = "CUSTOMERS";
    private static final String WATCHLIST_DATA_SOURCE   = "WATCHLIST";
    private static final String EMPLOYEES_DATA_SOURCE   = "EMPLOYEES";
    private static final String PASSENGERS_DATA_SOURCE  = "PASSENGERS";
    private static final String VIPS_DATA_SOURCE        = "VIPS";
    private static final String UNKNOWN_DATA_SOURCE     = "UNKNOWN";
    
    private static final SzRecord RECORD_JOE_SCHMOE
        = new SzRecord(
            SzFullName.of("Joe Schmoe"),
            SzPhoneNumber.of("725-555-1313"),
            SzFullAddress.of("101 Main Street, Las Vegas, NV 89101"));
    
    private static final SzRecord RECORD_JANE_SMITH
        = new SzRecord(
            SzFullName.of("Jane Smith"),
            SzPhoneNumber.of("725-555-1414"),
            SzFullAddress.of("440 N Rancho Blvd, Las Vegas, NV 89101"));

    private static final SzRecord RECORD_JOHN_DOE
        = new SzRecord(
            SzFullName.of("John Doe"),
            SzPhoneNumber.of("725-555-1717"),
            SzFullAddress.of("777 W Sahara Blvd, Las Vegas, NV 89107"));

    private static final SzRecord RECORD_JAMES_MORIARTY
        = new SzRecord(
            SzNameByParts.of("James", "Moriarty"),
            SzPhoneNumber.of("44-163-555-1313"),
            SzFullAddress.of("16A Upper Montagu St, London, W1H 2PB, England"));

    private static final SzRecord RECORD_SHERLOCK_HOLMES
        = new SzRecord(
            SzFullName.of("Sherlock Holmes"),
            SzPhoneNumber.of("44-163-555-1212"),
            SzFullAddress.of("221b Baker Street, London, NW1 6XE, England"));

    private static final SzRecord RECORD_JOHN_WATSON
        = new SzRecord(
            SzFullName.of("Dr. John H. Watson"),
            SzPhoneNumber.of("44-163-555-1414"),
            SzFullAddress.of("221b Baker Street, London, NW1 6XE, England"));
    
    private static final SzRecord RECORD_JOANN_SMITTH
        = new SzRecord(
            SzFullName.of("Joann Smith"),
            SzPhoneNumber.of("725-888-3939"),
            SzFullAddress.of("101 Fifth Ave, Las Vegas, NV 89118"),
            SzDateOfBirth.of("15-MAY-1983"));
        
    private static final SzRecord RECORD_BILL_WRIGHT
        = new SzRecord(
            SzNameByParts.of("Bill", "Wright", "AKA"),
            SzNameByParts.of("William", "Wright", "PRIMARY"),
            SzPhoneNumber.of("725-444-2121"),
            SzAddressByParts.of("101 Main StreetFifth Ave", "Las Vegas", "NV", "89118"),
            SzDateOfBirth.of("15-MAY-1983"));

    private static final SzRecord RECORD_CRAIG_SMITH
        = new SzRecord(
            SzNameByParts.of("Craig", "Smith"),
            SzPhoneNumber.of("725-888-3940"),
            SzFullAddress.of("101 Fifth Ave, Las Vegas, NV 89118"),
            SzDateOfBirth.of("12-JUN-1981"));
                
    private static final SzRecord RECORD_KIM_LONG
        = new SzRecord(
            SzFullName.of("Kim Long"),
            SzPhoneNumber.of("725-135-1913"),
            SzFullAddress.of("451 Dover St., Las Vegas, NV 89108"),
            SzDateOfBirth.of("24-OCT-1976"));

    private static final SzRecord RECORD_KATHY_OSBOURNE
        = new SzRecord(
            SzFullName.of("Kathy Osbourne"),
            SzPhoneNumber.of("725-111-2222"),
            SzFullAddress.of("707 Seventh Ave, Las Vegas, NV 89143"),
            SzDateOfBirth.of("24-OCT-1976"));
    
    private static final List<SzRecord> WRITE_RECORDS
        = List.of(RECORD_BILL_WRIGHT,
                  RECORD_CRAIG_SMITH,
                  RECORD_JAMES_MORIARTY,
                  RECORD_JANE_SMITH,
                  RECORD_JOANN_SMITTH,
                  RECORD_JOE_SCHMOE,
                  RECORD_JOHN_DOE,
                  RECORD_JOHN_WATSON,
                  RECORD_KATHY_OSBOURNE,
                  RECORD_KIM_LONG,
                  RECORD_SHERLOCK_HOLMES);

    private static final List<SzRecordKey> WRITE_RECORD_KEYS
        = List.of(
            SzRecordKey.of(CUSTOMERS_DATA_SOURCE, "ABC123"),
            SzRecordKey.of(WATCHLIST_DATA_SOURCE, "DEF456"),
            SzRecordKey.of(UNKNOWN_DATA_SOURCE, "GHI789"),
            SzRecordKey.of(CUSTOMERS_DATA_SOURCE, "JKL012"),
            SzRecordKey.of(WATCHLIST_DATA_SOURCE, "MNO345"),
            SzRecordKey.of(CUSTOMERS_DATA_SOURCE, "PQR678"),
            SzRecordKey.of(WATCHLIST_DATA_SOURCE, "STU901"),
            SzRecordKey.of(CUSTOMERS_DATA_SOURCE, "VWX234"),
            SzRecordKey.of(WATCHLIST_DATA_SOURCE, "YZA567"),
            SzRecordKey.of(CUSTOMERS_DATA_SOURCE, "BCD890"),
            SzRecordKey.of(WATCHLIST_DATA_SOURCE, "EFG123")
        );

    private static final List<Set<SzFlag>> WRITE_FLAG_SETS;
    static {
        List<Set<SzFlag>> list = new ArrayList<>(4);
        list.add(null);
        list.add(EnumSet.noneOf(SzFlag.class));
        list.add(EnumSet.of(SZ_NO_FLAGS));
        list.add(SZ_WITH_INFO_FLAGS);
        WRITE_FLAG_SETS = Collections.unmodifiableList(list);
    }

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

    private static final SzRecordKey PASSENGER_ABC123
        = SzRecordKey.of(PASSENGERS_DATA_SOURCE, "ABC123");
    
    private static final SzRecordKey PASSENGER_DEF456
        = SzRecordKey.of(PASSENGERS_DATA_SOURCE, "DEF456");

    private static final SzRecordKey PASSENGER_GHI789 
        = SzRecordKey.of(PASSENGERS_DATA_SOURCE, "GHI789");

    private static final SzRecordKey PASSENGER_JKL012
        = SzRecordKey.of(PASSENGERS_DATA_SOURCE, "JKL012");

    private static final SzRecordKey EMPLOYEE_MNO345
        = SzRecordKey.of(EMPLOYEES_DATA_SOURCE, "MNO345");

    private static final SzRecordKey EMPLOYEE_PQR678
        = SzRecordKey.of(EMPLOYEES_DATA_SOURCE, "PQR678");

    private static final SzRecordKey EMPLOYEE_ABC567
        = SzRecordKey.of(EMPLOYEES_DATA_SOURCE, "ABC567");
        
    private static final SzRecordKey EMPLOYEE_DEF890
        = SzRecordKey.of(EMPLOYEES_DATA_SOURCE, "DEF890");

    private static final SzRecordKey VIP_STU901
        = SzRecordKey.of(VIPS_DATA_SOURCE, "STU901");

    private static final SzRecordKey VIP_XYZ234
        = SzRecordKey.of(VIPS_DATA_SOURCE, "XYZ234");

    private static final SzRecordKey VIP_GHI123
        = SzRecordKey.of(VIPS_DATA_SOURCE, "GHI123");

    private static final SzRecordKey VIP_JKL456
        = SzRecordKey.of(VIPS_DATA_SOURCE, "JKL456");
    

    private static final List<SzRecordKey> READ_RECORD_KEYS
        = List.of(PASSENGER_ABC123,
                  PASSENGER_DEF456,
                  PASSENGER_GHI789,
                  PASSENGER_JKL012,
                  EMPLOYEE_MNO345,
                  EMPLOYEE_PQR678,
                  EMPLOYEE_ABC567,
                  EMPLOYEE_DEF890,
                  VIP_STU901,
                  VIP_XYZ234,
                  VIP_GHI123,
                  VIP_JKL456);

    private SzCoreEnvironment env = null;

    @BeforeAll
    public void initializeEnvironment() {
        this.beginTests();
        this.initializeTestEnvironment();
    }

  /**
   * Overridden to configure some data sources.
   */
  protected void prepareRepository() {
    File repoDirectory = this.getRepositoryDirectory();

    Set<String> dataSources = new LinkedHashSet<>();
    dataSources.add("CUSTOMERS");
    dataSources.add("WATCHLIST");
    dataSources.add("PASSENGERS");
    dataSources.add("EMPLOYEES");
    dataSources.add("VIPS");

    File passengerFile = this.preparePassengerFile();
    File employeeFile = this.prepareEmployeeFile();
    File vipFile = this.prepareVipFile();

    employeeFile.deleteOnExit();
    passengerFile.deleteOnExit();
    vipFile.deleteOnExit();

    RepositoryManager.configSources(repoDirectory,
                                    dataSources,
                                    true);

    RepositoryManager.loadFile(repoDirectory,
                               passengerFile,
                               PASSENGERS_DATA_SOURCE,
                               true);

    RepositoryManager.loadFile(repoDirectory,
                               employeeFile,
                               EMPLOYEES_DATA_SOURCE,
                               true);

    RepositoryManager.loadFile(repoDirectory,
                               vipFile,
                               VIPS_DATA_SOURCE,
                               true);
  }

  private File preparePassengerFile() {
    String[] headers = {
        "RECORD_ID", "NAME_FIRST", "NAME_LAST", "MOBILE_PHONE_NUMBER",
        "HOME_PHONE_NUMBER", "ADDR_FULL", "DATE_OF_BIRTH"};

    String[][] passengers = {
        {PASSENGER_ABC123.recordId(), "Joseph", "Schmidt", "818-555-1212", "818-777-2424",
            "101 Main Street, Los Angeles, CA 90011", "12-JAN-1981"},
        {PASSENGER_DEF456.recordId(), "Joann", "Smith", "818-555-1212", "818-888-3939",
            "101 Fifth Ave, Los Angeles, CA 90018", "15-MAR-1982"},
        {PASSENGER_GHI789.recordId(), "John", "Parker", "818-555-1313", "818-999-2121",
            "101 Fifth Ave, Los Angeles, CA 90018", "17-DEC-1977"},
        {PASSENGER_JKL012.recordId(), "Jane", "Donaldson", "818-555-1313", "818-222-3131",
            "400 River Street, Pasadena, CA 90034", "23-MAY-1973"}
    };
    return this.prepareCSVFile("test-passengers-", headers, passengers);
  }

  private File prepareEmployeeFile() {
    String[] headers = {
        "RECORD_ID", "NAME_FIRST", "NAME_LAST", "MOBILE_PHONE_NUMBER",
        "HOME_PHONE_NUMBER", "ADDR_FULL", "DATE_OF_BIRTH"};

    String[][] employees = {
        {EMPLOYEE_MNO345.recordId(), "Bill", "Bandley", "818-444-2121", "818-123-4567",
            "101 Main Street, Los Angeles, CA 90011", "22-AUG-1981"},
        {EMPLOYEE_PQR678.recordId(), "Craig", "Smith", "818-555-1212", "818-888-3939",
            "451 Dover Street, Los Angeles, CA 90018", "17-OCT-1983"},
        {EMPLOYEE_ABC567.recordId(), "Kim", "Long", "818-246-8024", "818-135-7913",
            "451 Dover Street, Los Angeles, CA 90018", "24-NOV-1975"},
        {EMPLOYEE_DEF890.recordId(), "Katrina", "Osmond", "818-444-2121", "818-111-2222",
            "707 Seventh Ave, Los Angeles, CA 90043", "27-JUN-1980"}
    };

    return this.prepareJsonArrayFile("test-employees-", headers, employees);
  }

  private File prepareVipFile() {
    String[] headers = {
        "RECORD_ID", "NAME_FIRST", "NAME_LAST", "MOBILE_PHONE_NUMBER",
        "HOME_PHONE_NUMBER", "ADDR_FULL", "DATE_OF_BIRTH"};

    String[][] vips = {
        {VIP_STU901.recordId(), "Martha", "Wayne", "818-891-9292", "818-987-1234",
            "888 Sepulveda Blvd, Los Angeles, CA 90034", "27-NOV-1973"},
        {VIP_XYZ234.recordId(), "Jane", "Johnson", "818-333-7171", "818-123-9876",
            "400 River Street, Pasadena, CA 90034", "6-SEP-1975"},
        {VIP_GHI123.recordId(), "Martha", "Kent", "818-333-5757", "818-123-9876",
            "888 Sepulveda Blvd, Los Angeles, CA 90034", "17-AUG-1978"},
        {VIP_JKL456.recordId(), "Kelly", "Rogers", "818-333-7171", "818-789-6543",
            "707 Seventh Ave, Los Angeles, CA 90043", "15-JAN-1979"}
    };

    return this.prepareJsonFile("test-vips-", headers, vips);
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

    public List<Arguments> getAddRecordArguments() {
        List<Arguments>     result      = new LinkedList<>();
        int count = Math.min(WRITE_RECORD_KEYS.size(), WRITE_RECORDS.size());
        Iterator<SzRecordKey>   keyIter     = WRITE_RECORD_KEYS.iterator();
        Iterator<SzRecord>      recordIter  = WRITE_RECORDS.iterator();

        Iterator<Set<SzFlag>> flagSetIter = circularIterator(WRITE_FLAG_SETS);
        Iterator<Boolean> viaKeyIter = circularIterator(VIA_KEY_LIST);

        int mismatch = 0;
        for (int index = 0; index < count; index++) {
            SzRecordKey key             = keyIter.next();
            SzRecord    record          = recordIter.next();
            Class<?>    exceptionType   = null;
            Set<SzFlag> flagSet         = flagSetIter.next();
            boolean     viaKey          = viaKeyIter.next();

            switch (mismatch) {
                case 1:
                {
                    String dataSource 
                        = (CUSTOMERS_DATA_SOURCE.equals(key.dataSourceCode()))
                        ? WATCHLIST_DATA_SOURCE
                        : CUSTOMERS_DATA_SOURCE;

                    SzRecordKey wrongKey 
                        = SzRecordKey.of(dataSource, key.recordId());

                    record = new SzRecord(wrongKey, record);

                    exceptionType = SzBadInputException.class;
                    mismatch++;
                }
                break;
                case 2:
                {
                    SzRecordKey wrongKey 
                        = SzRecordKey.of(key.dataSourceCode(), "WRONG_ID");

                    record = new SzRecord(wrongKey, record);

                    exceptionType = SzBadInputException.class;

                    mismatch++;
                }
                break;
                default:
                {
                    record = new SzRecord(key, record);
                    if (key.dataSourceCode().equals(UNKNOWN_DATA_SOURCE)) {
                        mismatch++;
                        exceptionType = SzUnknownDataSourceException.class;
                    }        
                }
            }
            
            result.add(Arguments.of(
                key, record, flagSet, viaKey, exceptionType));
        }

        return result;
    }

    @ParameterizedTest
    @MethodSource("getAddRecordArguments")
    @Order(10)
    void testAddRecord(SzRecordKey  recordKey, 
                       SzRecord     record,
                       Set<SzFlag>  flags,
                       boolean      viaKey,
                       Class<?>     expectedExceptionType)
    {
        this.performTest(() -> {
            try {
                SzEngine engine = this.env.getEngine();

                String result = null;
                if (viaKey) {
                    result = engine.addRecord(
                        recordKey,
                        record.toString(),
                        flags);

                } else {
                    result = engine.addRecord(
                        recordKey.dataSourceCode(),
                        recordKey.recordId(),
                        record.toString(),
                        flags);
                }

                if (expectedExceptionType != null) {
                    fail("Unexpectedly succeeded in adding record: viaKey=[ "
                         + viaKey + " ], recordKey=[ " + recordKey 
                         + " ], flags=[ " + SzFlag.toString(flags)
                         + " ], definition=[ " + record 
                         + " ], expectedException=[ " + expectedExceptionType
                         + " ]");
                }

                LOADED_RECORD_MAP.put(recordKey, null);
                
                // check if we are expecting info
                if (flags != null && flags.contains(SZ_WITH_INFO)) {
                    // parse the result as JSON and check that it parses
                    JsonObject jsonObject = parseJsonObject(result);

                    assertTrue(jsonObject.containsKey("DATA_SOURCE"),
                               "Info message lacking DATA_SOURCE key");
                    assertTrue(jsonObject.containsKey("RECORD_ID"),
                                "Info message lacking RECORD_ID key");
                    assertTrue(jsonObject.containsKey("AFFECTED_ENTITIES"),
                                "Info message lacking AFFECTED_ENTITIES key");
                } else {
                    assertEquals(SzCoreEngine.NO_INFO, result,
                                "No INFO requested, but non-empty response received");
                }

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
                    fail("Unexpectedly failed adding a record: "
                         + description, e);

                } else if (expectedExceptionType != e.getClass()) {
                    assertInstanceOf(
                        expectedExceptionType, e, 
                        "addRecord() failed with an unexpected exception type: "
                        + description);
                }
            }
        });
    }



    private List<Arguments> getGetEntityByRecordParameters() {
        List<Arguments>         result      = new LinkedList<>();

        for (SzRecordKey key : WRITE_RECORD_KEYS) {
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
    @Order(20)
    void testGetEntityByRecordId(SzRecordKey  recordKey,
                                 Set<SzFlag>  flags,
                                 boolean      viaKey,
                                 Class<?>     expectedExceptionType)
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

    public List<Arguments> getDeleteRecordArguments() {
        List<Arguments>     result      = new LinkedList<>();

        int count = WRITE_RECORD_KEYS.size();
        Iterator<SzRecordKey>   keyIter     = WRITE_RECORD_KEYS.iterator();
        Iterator<Set<SzFlag>>   flagSetIter = circularIterator(WRITE_FLAG_SETS);
        Iterator<Boolean>       viaKeyIter  = circularIterator(VIA_KEY_LIST);

        for (int index = 0; index < count; index++) {
            SzRecordKey key             = keyIter.next();
            Class<?>    exceptionType   = null;
            Set<SzFlag> flagSet         = flagSetIter.next();
            boolean     viaKey          = viaKeyIter.next();

            if (key.dataSourceCode().equals(UNKNOWN_DATA_SOURCE)) {
                exceptionType = SzUnknownDataSourceException.class;
            }        
            
            result.add(Arguments.of(
                key, flagSet, viaKey, exceptionType));
        }

        return result;
    }

    @ParameterizedTest
    @MethodSource("getDeleteRecordArguments")
    @Order(100)
    void testDeleteRecord(SzRecordKey  recordKey, 
                          Set<SzFlag>  flags,
                          boolean      viaKey,
                          Class<?>     expectedExceptionType)
    {
        this.performTest(() -> {
            try {
                SzEngine engine = this.env.getEngine();

                String result = null;
                if (viaKey) {
                    result = engine.deleteRecord(recordKey, flags);

                } else {
                    result = engine.deleteRecord(
                        recordKey.dataSourceCode(),
                        recordKey.recordId(),
                        flags);
                }

                if (expectedExceptionType != null) {
                    fail("Unexpectedly succeeded in deleting record: viaKey=[ "
                         + viaKey + " ], recordKey=[ " + recordKey 
                         + " ], flags=[ " + SzFlag.toString(flags)
                         + " ], expectedException=[ " + expectedExceptionType
                         + " ]");
                }
                
                // check if we are expecting info
                if (flags != null && flags.contains(SZ_WITH_INFO)) {
                    // parse the result as JSON and check that it parses
                    JsonObject jsonObject = parseJsonObject(result);

                    assertTrue(jsonObject.containsKey("DATA_SOURCE"),
                               "Info message lacking DATA_SOURCE key");
                    assertTrue(jsonObject.containsKey("RECORD_ID"),
                                "Info message lacking RECORD_ID key");
                    assertTrue(jsonObject.containsKey("AFFECTED_ENTITIES"),
                                "Info message lacking AFFECTED_ENTITIES key");
                } else {
                    assertEquals(SzCoreEngine.NO_INFO, result,
                                "No INFO requested, but non-empty response received");
                }

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
                    fail("Unexpectedly failed deleting a record: "
                         + description, e);

                } else if (expectedExceptionType != e.getClass()) {
                    assertInstanceOf(
                        expectedExceptionType, e, 
                        "deleteRecord() failed with an unexpected exception type: "
                        + description);
                }
            }
        });
    }

    @Test
    void testCloseExport() {

    }

    @Test
    void testCountRedoRecords() {

    }

    @Test
    void testDestroy() {

    }

    @Test
    void testEncodeDataSources() {

    }

    @Test
    void testEncodeEntityIds() {

    }

    @Test
    void testEncodeRecordKeys() {

    }

    @Test
    void testExportCsvEntityReport() {

    }

    @Test
    void testExportJsonEntityReport() {

    }

    @Test
    void testFetchNext() {

    }

    @Test
    void testFindNetworkByEntityId() {

    }

    @Test
    void testFindNetworkByRecordId() {

    }

    @Test
    void testFindPathByEntityId() {

    }

    @Test
    void testFindPathByRecordId() {

    }

    @Test
    void testFindPathByRecordId2() {

    }

    @Test
    void testGetEntity3() {

    }

    @Test
    void testGetRecord() {

    }

    @Test
    void testGetRecord2() {

    }

    @Test
    void testGetRedoRecord() {

    }

    @Test
    void testGetStats() {

    }

    @Test
    void testGetVirtualEntity() {

    }

    @Test
    void testHowEntity() {

    }

    @Test
    void testIsDestroyed() {

    }

    @Test
    void testPrimeEngine() {

    }

    @Test
    void testProcessRedoRecord() {

    }

    @Test
    void testReevaluateEntity() {

    }

    @Test
    void testReevaluateRecord() {

    }

    @Test
    void testReevaluateRecord2() {

    }

    @Test
    void testSearchByAttributes() {

    }

    @Test
    void testSearchByAttributes2() {

    }

    @Test
    void testWhyEntities() {

    }

    @Test
    void testWhyRecordInEntity() {

    }

    @Test
    void testWhyRecordInEntity2() {

    }

    @Test
    void testWhyRecords() {

    }

    @Test
    void testWhyRecords2() {

    }
}
