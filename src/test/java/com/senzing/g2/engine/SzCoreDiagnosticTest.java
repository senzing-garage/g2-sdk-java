package com.senzing.g2.engine;

import java.util.List;
import java.util.LinkedList;
import java.util.Map;
import java.util.LinkedHashMap;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import static org.junit.jupiter.api.TestInstance.Lifecycle;

import javax.json.Json;
import javax.json.JsonObjectBuilder;
import javax.json.JsonObject;
import javax.json.JsonArray;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertSame;
import static com.senzing.util.JsonUtilities.*;
import static com.senzing.g2.engine.NativeEngine.*;
import static org.junit.jupiter.params.provider.Arguments.*;

/**
 * Unit tests for {@link SzCoreDiagnostic}.
 */
 @TestInstance(Lifecycle.PER_CLASS)
 @Execution(ExecutionMode.SAME_THREAD)
 @TestMethodOrder(OrderAnnotation.class)
 public class SzCoreDiagnosticTest extends AbstractTest {
    private static final String TEST_DATA_SOURCE = "TEST";
    private static final String TEST_RECORD_ID = "ABC123";

    private static final long FLAGS 
        = G2_ENTITY_INCLUDE_ALL_FEATURES
        | G2_ENTITY_INCLUDE_ENTITY_NAME
        | G2_ENTITY_INCLUDE_RECORD_SUMMARY
        | G2_ENTITY_INCLUDE_RECORD_TYPES
        | G2_ENTITY_INCLUDE_RECORD_DATA
        | G2_ENTITY_INCLUDE_RECORD_JSON_DATA
        | G2_ENTITY_INCLUDE_RECORD_MATCHING_INFO
        | G2_ENTITY_INCLUDE_RECORD_UNMAPPED_DATA
        | G2_ENTITY_INCLUDE_RECORD_FEATURE_IDS
        | G2_ENTITY_OPTION_INCLUDE_INTERNAL_FEATURES
        | G2_ENTITY_OPTION_INCLUDE_FEATURE_ELEMENTS
        | G2_ENTITY_OPTION_INCLUDE_MATCH_KEY_DETAILS
        | G2_INCLUDE_FEATURE_SCORES;

    private SzCoreEnvironment session = null;

    private Map<Long, String> featureMaps = new LinkedHashMap<>();

    @BeforeAll
    public void initializeEnvironment() {
        this.initializeTestEnvironment();
        String settings = this.getRepoSettings();
        
        String instanceName = this.getClass().getSimpleName();

        NativeEngine nativeEngine = new G2JNI();
        try {
            // initialize the native engine
            int returnCode = nativeEngine.init(instanceName, settings, false);
            if (returnCode != 0) {
                throw new RuntimeException(nativeEngine.getLastException());
            }

            JsonObjectBuilder job = Json.createObjectBuilder();
            job.add("DATA_SOURCE", "TEST");
            job.add("RECORD_ID", "ABC123");
            job.add("NAME_FULL", "Joe Schmoe");
            job.add("EMAIL_ADDRESS", "joeschmoe@nowhere.com");
            job.add("PHONE_NUMBER", "702-555-1212");
            JsonObject jsonObj = job.build();
            String recordDefinition = jsonObj.toString();

            // add a record
            returnCode = nativeEngine.addRecord(
                TEST_DATA_SOURCE, TEST_RECORD_ID, recordDefinition);

            if (returnCode != 0) {
                throw new RuntimeException(nativeEngine.getLastException());
            }
                
             // get the entity 
             StringBuffer sb = new StringBuffer();
             returnCode = nativeEngine.getEntityByRecordID(
                TEST_DATA_SOURCE, TEST_RECORD_ID, FLAGS, sb);

            // parse the entity and get the feature ID's
            JsonObject entity = parseJsonObject(sb.toString());
            entity = entity.getJsonObject("RESOLVED_ENTITY");
            JsonObject features = entity.getJsonObject("FEATURES");
            for (String featureName : features.keySet()) {
                JsonArray featureArr = features.getJsonArray(featureName);
                for (JsonObject feature : featureArr.getValuesAs(JsonObject.class)) {
                    this.featureMaps.put(getLong(feature, "LIB_FEAT_ID"),
                    toJsonText(feature));
                }
            }

        } finally {
            nativeEngine.destroy();
        }

        this.session = SzCoreEnvironment.newBuilder()
                                      .instanceName(instanceName)
                                      .settings(settings)
                                      .verboseLogging(false)
                                      .build();
    }
    
    @AfterAll
    public void teardownEnvironment() {
        if (this.session != null) {
            this.session.destroy();
            this.session = null;
        }
        this.teardownTestEnvironment();
    }

    @Test
    @Order(20)
    void testCheckDatabasePerformance() {
        this.performTest(() -> {
            try {
                SzDiagnostic diagnostic = this.session.getDiagnostic();

                String result = diagnostic.checkDatabasePerformance(5);
                
                // parse the result as JSON and check that it parses
                parseJsonObject(result);

            } catch (Exception e) {
                fail("Failed testCheckDatabasePerformance test with exception", e);
            }
        });
    }

    protected List<Arguments> getFeatureIdArguments() {
        List<Arguments> argumentsList = new LinkedList<>();
        this.featureMaps.forEach((featureId, feature) -> {
            argumentsList.add(arguments(featureId, feature));
        });
        return argumentsList;
    }

    @ParameterizedTest
    @MethodSource("getFeatureIdArguments")
    @Order(30)
    void testGetFeature(long featureId, String expected) {
        this.performTest(() -> {
            try {
                SzDiagnostic diagnostic = this.session.getDiagnostic();

                String actual = diagnostic.getFeature(featureId);

                JsonObject actualObj    = parseJsonObject(actual);
                JsonObject expectedObj  = parseJsonObject(expected);
                
                assertEquals(getLong(expectedObj, "LIB_FEAT_ID"),
                             getLong(actualObj, "LIB_FEAT_ID"),
                             "Feature ID does not match what is expected");
                
            } catch (Exception e) {
                fail("Failed testPurgeRepository test with exception", e);
            }
        });
    }

    @Test
    @Order(100)
    void testPurgeRepository() {
        this.performTest(() -> {
            try {
                SzDiagnostic diagnostic = this.session.getDiagnostic();

                diagnostic.purgeRepository();

            } catch (Exception e) {
                fail("Failed testPurgeRepository test with exception", e);
            }
        });

    }
}
