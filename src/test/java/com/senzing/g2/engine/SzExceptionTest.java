package com.senzing.g2.engine;

import java.util.List;
import java.util.LinkedList;
import java.util.Map;
import java.util.Iterator;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import static com.senzing.g2.engine.SzException.*;
import static org.junit.jupiter.api.TestInstance.Lifecycle;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.fail;

@TestInstance(Lifecycle.PER_CLASS)
@Execution(ExecutionMode.SAME_THREAD)
@TestMethodOrder(OrderAnnotation.class)
public class SzExceptionTest {
    @ParameterizedTest
    @ValueSource(ints = { 10, 20, 30, 40 })
    @Order(10)
    void testGetErrorCode(int errorCode) {
        SzException e = new SzException(errorCode, "Test");
        assertEquals(errorCode, e.getErrorCode(), "Error code not as expected");
    }

    @ParameterizedTest
    @ValueSource(strings = { "Foo.bar()", "Phoo.baz(int,String)", "Bar.phoo(String)"})
    @Order(20)
    void testGetMethodSignature(String signature) {
        SzException e = new SzException("Test message", signature, paramsOf());
        assertEquals(signature, e.getMethodSignature(),
             "Method signature not as expected");
    }

    private static void checkParamsMap(int                  expectedSize,
                                       Map<String, Object>  paramsMap) 
    {
        assertEquals(expectedSize, paramsMap.size(), 
                     "Params map size not as expected");
        
        try {
            paramsMap.put("abc", "xyz");
            fail("Unexpectedly able to modify the params map.");
        } catch (UnsupportedOperationException e) {
            // expected
        } catch (Exception e) {
            fail("Failed modify on params map with unexpected exception.", e);
        }

        // check containment of expected parameters
        for (int index = 0; index < expectedSize; index++) {
            Object expectedValue = ((index % 2) == 0) ? index : "Value:" + index;
            String expectedName  = "param" + index;
            assertTrue(paramsMap.containsKey(expectedName), 
                "Parameter name missing in map: " + expectedName);
            Object value = paramsMap.get(expectedName);
            assertEquals(expectedValue, value,
                         "Parameter value not as expected for parameter: " + expectedName);
        }

        // check the parameters
        Iterator<Map.Entry<String,Object>> iter = paramsMap.entrySet().iterator();
        for (int index = 0; iter.hasNext(); index++) {
            Map.Entry<String,Object> entry = iter.next();
            String paramName    = entry.getKey();
            Object paramValue   = entry.getValue();

            Object expectedValue = ((index % 2) == 0) ? index : "Value:" + index;
            String expectedName  = "param" + index;

            assertEquals(expectedName, paramName, 
                         "Param name in iteration order not as expected: " + paramsMap);
            assertEquals(expectedValue, paramValue, 
                         "Param value in iteration order not as expected: " + paramsMap);

        }        
    }

    @Test
    @Order(30)
    void testParamsOf0() {
        checkParamsMap(0, paramsOf());
    }

    @Test
    @Order(40)
    void testParamsOf1() {
        checkParamsMap(1, paramsOf("param0", 0));
    }

    @Test
    @Order(50)
    void testParamsOf2() {
        checkParamsMap(2, 
                       paramsOf("param0", 0, 
                                "param1", "Value:1"));
    }

    @Test
    @Order(60)
    void testParamsOf3() {
        checkParamsMap(3, 
                       paramsOf("param0", 0, 
                                "param1", "Value:1",
                                "param2", 2));
    }

    @Test
    @Order(70)
    void testParamsOf4() {
        checkParamsMap(4, 
                       paramsOf("param0", 0, 
                                "param1", "Value:1",
                                "param2", 2,
                                "param3", "Value:3"));
    }

    @Test
    @Order(80)
    void testParamsOf5() {
        checkParamsMap(5, 
                       paramsOf("param0", 0, 
                                "param1", "Value:1",
                                "param2", 2,
                                "param3", "Value:3",
                                "param4", 4));
    }

    @Test
    @Order(90)
    void testParamsOf6() {
        checkParamsMap(6, 
                       paramsOf("param0", 0, 
                               "param1", "Value:1",
                               "param2", 2,
                               "param3", "Value:3",
                               "param4", 4,
                               "param5", "Value:5"));

    }

    @Test
    @Order(100)
    void testParamsOf7() {
        checkParamsMap(7, 
                       paramsOf("param0", 0, 
                               "param1", "Value:1",
                               "param2", 2,
                               "param3", "Value:3",
                               "param4", 4,
                               "param5", "Value:5",
                               "param6", 6));
    }

    @Test
    @Order(110)
    void testParamsOf8() {
        checkParamsMap(8, 
                       paramsOf("param0", 0, 
                               "param1", "Value:1",
                               "param2", 2,
                               "param3", "Value:3",
                               "param4", 4,
                               "param5", "Value:5",
                               "param6", 6,
                               "param7", "Value:7"));
    }

    @Test
    @Order(120)
    void testParamsOf9() {
        checkParamsMap(9, 
                       paramsOf("param0", 0, 
                               "param1", "Value:1",
                               "param2", 2,
                               "param3", "Value:3",
                               "param4", 4,
                               "param5", "Value:5",
                               "param6", 6,
                               "param7", "Value:7",
                               "param8", 8));
    }

    @Test
    @Order(130)
    void testParamsOf10() {
        checkParamsMap(10, 
                       paramsOf("param0", 0, 
                               "param1", "Value:1",
                               "param2", 2,
                               "param3", "Value:3",
                               "param4", 4,
                               "param5", "Value:5",
                               "param6", 6,
                               "param7", "Value:7",
                               "param8", 8,
                               "param9", "Value:9"));
    }


    private List<Map<String,Object>> getMethodParameterMaps() {
        List<Map<String,Object>> result = new LinkedList<>();
        result.add(paramsOf("foo", 5, "bar", true));
        result.add(paramsOf("phoo", 200));
        result.add(null);
        result.add(paramsOf());
        result.add(Map.of());
        return result;
    }

    @ParameterizedTest
    @MethodSource("getMethodParameterMaps")
    @Order(140)
    void testGetMethodParameters(Map<String, Object> paramsMap) {
        SzException e = new SzException(
            "Test message", "Foo.bar(int)", paramsMap);
        assertEquals(paramsMap, e.getMethodParameters(), 
             "Method parameters not as expected");
    }

    @Test 
    @Order(150)
    void testIsRedactingDefault() {
        String value = System.getenv(SZ_DO_NOT_REDACT_ENV_VARIABLE);
        boolean expected = true;
        if (value != null) {
            expected = (!Boolean.valueOf(value.trim().toLowerCase()));
        }
        assertEquals(expected, SzException.isRedacting(),
                     "Default redaction setting not as expected.  " 
                    + SZ_DO_NOT_REDACT_ENV_VARIABLE + "=[ " + value + " ]");
    }

    @Test 
    @Order(160)
    void testEnableRedaction() {
        enableRedaction();
        assertEquals(true, isRedacting(),
                     "Enabling redaction did not have the expected effect");
    }

    private List<Object> getRedactingObjects() {
        List<Object> result = new LinkedList<>();
        result.add("Hello");
        result.add(Map.of("foo", "bar", "phoo", "baz"));
        result.add(null);
        result.add("");
        result.add(12);
        result.add(true);
        return result;
    }

    @ParameterizedTest
    @Order(170)
    @MethodSource("getRedactingObjects")
    void testRedact(Object object) {
        String notRedacted  = String.valueOf(object);
        String redacted     = redact(object);
        assertNotEquals(notRedacted, redacted,
                        "Object was unexpectedly NOT redacted");
        assertEquals(REDACTION_REPLACEMENT, redacted,
                     "Object was not properly redacted");
    }

    @Test 
    @Order(180)
    void testDisableRedaction() {
        disableRedaction();
        assertEquals(false, isRedacting(),
                     "Disabling redaction did not have the expected effect");
    }

    @ParameterizedTest
    @Order(190)
    @MethodSource("getRedactingObjects")
    void testNoRedact(Object object) {
        String expected = String.valueOf(object);
        String actual   = redact(object);
        assertNotEquals(REDACTION_REPLACEMENT, actual,
                        "Object unexpectedly redacted");
        assertEquals(expected, actual, "Object not preserved");
    }
}
