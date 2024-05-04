package com.senzing.g2.engine;

import java.util.Map;
import java.util.Set;
import java.util.LinkedHashMap;
import java.util.Collections;
import java.util.AbstractMap;

/**
 * Defines the base exception for Senzing errors.  This adds a property
 * for the numeric Senzing error code which can optionally be set.
 */
public class SzException extends Exception {
    /**
     * Set this environment variable to the value <code>"true"</code> (case 
     * insensitive) to <b>override</b> the default behavior of the {@link 
     * #redact(Object)} function of <b>attempting</b> to redact sensitive data
     * from the generated exception messages.
     * <p>
     * <b>WARNING:</b> The Senzing SDK will use the {@link #redact(Object)} function
     * to <b>attempt</b> to redact sensitive data from exception messages, but this is
     * <b>NOT</b> a guarantee that all sensitive data will be redacted.  The developer
     * is still responsible to scrub and redact sensitive data from messages to prevent
     * exposing sensitive data to unauthorized viewers (e.g.: in error dialogs of user
     * interfaces or in system logs).
     */
    public static final String SZ_DO_NOT_REDACT_ENV_VARIABLE = "SZ_DO_NOT_REDACT";

    /**
     * Constant indicating if we are redacting sensitive data.
     */
    private static final boolean REDACTING 
        = Boolean.valueOf(System.getenv(SZ_DO_NOT_REDACT_ENV_VARIABLE));

    /**
     * The underlying senzing error code.
     */
    private Integer errorCode = null;
    
    /**
     * The method signature that produced the exception.
     */
    private String methodSignature = null;

    /**
     * The {@link Map} of {@link String} parameter name keys to {@link Object}
     * parameter name values.
     */
    private Map<String,Object> methodParameters = null;

    /**
     * Default constructor.
     */
    public SzException() {
        super();
        this.errorCode = null;
    }

    /**
     * Constructs with a message explaing the reason for the exception.
     * 
     * @param message The message explaining the reason for the exception.
     */
    public SzException(String message) {
        super(message);
        this.errorCode = null;
    }

    /**
     * Constructs with a message explaing the reason for the exception.
     * 
     * @param errorCode The underlying senzing error code.
     * 
     * @param message The message explaining the reason for the exception.
     */
    public SzException(int errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    /**
     * Constructs with the {@link Throwable} that is the underlying cause
     * for the exception.
     * 
     * @param cause The message The message explaining the reason for the exception.
     */
    public SzException(Throwable cause) {
        super(cause);
        this.errorCode = null;
    }

    /**
     * Constructs with a message explaing the reason for the exception
     * and the {@link Throwable} that is the underlying cause for the 
     * exception.
     * 
     * @param message The message explaining the reason for the exception.
     *
     * @param cause The message The message explaining the reason for the exception.
     */
    public SzException(String message, Throwable cause) {
        super(message, cause);
        this.errorCode = null;
    }

    /**
     * Constructs with the Senzing error code, the message explaing
     * the reason for the exception and the {@link Throwable} that
     * is the underlying cause for the exception.
     * 
     * @param errorCode The underlying senzing error code.
     *
     * @param message The message explaining the reason for the exception.
     *
     * @param cause The message The message explaining the reason for the exception.
     */
    public SzException(int errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    /**
     * Constructs with the message explaing the reason for the exception, 
     * the method signature of the method that failed and the {@link Map}
     * describing the parmaeters with which that method was called.
     * <p>
     * <b>NOTE:</b> The specified {@link Map} describing the parameters
     * should preferrably be in order of how the parameters appear in
     * the method signature.  Further, parameter values should be 
     * {@linkplain #redact(Object) redacted} to protected sensitive data
     * from being exposed in error dialogs or log files.
     * 
     * @param message The message explaining the reason for the exception.
     *
     * @param methodSignature The name of the method being called (usually
     *                        prefixed by its simple class name and including
     *                        its parameter types).
     * 
     * @param methodParameters The {@link Map} describing the method parameters,
     *                         preferrably an ordered map according to the 
     *                         parameter positions in the method signature.
     */
    public SzException(String               message,
                       String               methodSignature,
                       Map<String,Object>   methodParameters) 
    {
        super(message);
        this.methodSignature    = methodSignature;
        this.methodParameters   = (methodParameters instanceof ParameterMap)
            ? methodParameters
            : new ParameterMap(new LinkedHashMap<>(methodParameters));
    }

    /**
     * Constructs with the Senzing error code, the message explaing the
     * reason for the exception, the method signature of the method that
     * failed and the {@link Map} describing the parmaeters with which 
     * that method was called.
     * <p>
     * <b>NOTE:</b> The specified {@link Map} describing the parameters
     * should preferrably be in order of how the parameters appear in
     * the method signature.  Further, parameter values should be 
     * {@linkplain #redact(Object) redacted} to protected sensitive data
     * from being exposed in error dialogs or log files.
     * 
     * @param errorCode The underlying senzing error code.
     *
     * @param message The message explaining the reason for the exception.
     *
     * @param methodSignature The name of the method being called (usually
     *                        prefixed by its simple class name and including
     *                        its parameter types).
     * 
     * @param methodParameters The {@link Map} describing the method parameters,
     *                         preferrably an ordered map according to the 
     *                         parameter positions in the method signature.
     */
    public SzException(int                  errorCode,
                       String               message,
                       String               methodSignature,
                       Map<String,Object>   methodParameters) 
    {
        super(message);
        this.errorCode          = errorCode;
        this.methodSignature    = methodSignature;
        this.methodParameters   = (methodParameters instanceof ParameterMap)
            ? methodParameters
            : new ParameterMap(new LinkedHashMap<>(methodParameters));
    }


    /**
     * Constructs with the message explaing the reason for the exception, 
     * the method signature of the method that failed, the {@link Map}
     * describing the parmaeters with which that method was called and the
     * {@link Throwable} that is the underlying cause for the exception.
     * <p>
     * <b>NOTE:</b> The specified {@link Map} describing the parameters
     * should preferrably be in order of how the parameters appear in
     * the method signature.  Further, parameter values should be 
     * {@linkplain #redact(Object) redacted} to protected sensitive data
     * from being exposed in error dialogs or log files.
     * 
     * @param message The message explaining the reason for the exception.
     *
     * @param methodSignature The name of the method being called (usually
     *                        prefixed by its simple class name and including
     *                        its parameter types).
     * 
     * @param methodParameters The {@link Map} describing the method parameters,
     *                         preferrably an ordered map according to the 
     *                         parameter positions in the method signature.
     * 
     * @param cause The message The message explaining the reason for the exception.
     */
    public SzException(String               message,
                       String               methodSignature,
                       Map<String,Object>   methodParameters, 
                       Throwable            cause) 
    {
        super(message, cause);
        this.methodSignature    = methodSignature;
        this.methodParameters   = (methodParameters instanceof ParameterMap)
            ? methodParameters
            : new ParameterMap(new LinkedHashMap<>(methodParameters));
    }

    /**
     * Constructs with the Senzing error code, the message explaing the
     * reason for the exception, the method signature of the method that
     * failed, the {@link Map} describing the parmaeters with which that
     * method was called and the {@link Throwable} that is the underlying
     * cause for the exception.
     * <p>
     * <b>NOTE:</b> The specified {@link Map} describing the parameters
     * should preferrably be in order of how the parameters appear in
     * the method signature.  Further, parameter values should be 
     * {@linkplain #redact(Object) redacted} to protected sensitive data
     * from being exposed in error dialogs or log files.
     * 
     * @param errorCode The underlying senzing error code.
     *
     * @param message The message explaining the reason for the exception.
     *
     * @param methodSignature The name of the method being called (usually
     *                        prefixed by its simple class name and including
     *                        its parameter types).
     * 
     * @param methodParameters The {@link Map} describing the method parameters,
     *                         preferrably an ordered map according to the 
     *                         parameter positions in the method signature.
     * 
     * @param cause The message The message explaining the reason for the exception.
     */
    public SzException(int                  errorCode,
                       String               message,
                       String               methodSignature,
                       Map<String,Object>   methodParameters, 
                       Throwable            cause) 
    {
        super(message, cause);
        this.errorCode          = errorCode;
        this.methodSignature    = methodSignature;
        this.methodParameters   = (methodParameters instanceof ParameterMap)
            ? methodParameters
            : new ParameterMap(new LinkedHashMap<>(methodParameters));
    }

    /**
     * Gets the underlying Senzing error code associated with the
     * exception.  This returns <code>null</code> if no error code was 
     * associated with the exception.
     * 
     * @return The underlying Senzing error code associated with the
     *         exception, or <code>null</code> if none was associated.
     */
    public Integer getErrorCode() {
        return this.errorCode;
    }

    /**
     * Gets the description of the method signature of the method that
     * failed causing this exception to be generated.  This method returns
     * <code>null</code> if the method signature was not specified when 
     * this instance was constructed.
     * 
     * @return The description of the method signature of the method that
     *         failed causing this exception to be generated, or
     *         <code>null</code> if the method signature was not specified
     *         when this instance was constructed.
     */
    public String getMethodSignature() {
        return this.methodSignature;
    }

    /**
     * Gets the <b>unmodifiable</b> {@link Map} describing the parameters 
     * used to call the method that failed causing this exception to be
     * generated.  This method returns <code>null</code> if the method
     * parameters were not specified when this instance was constructed.
     * 
     * @return Gets the <b>unmodifiable</b> {@link Map} describing the
     *         parameters used to call the method that failed causing
     *         this exception to be generated, or <code>null</code> if
     *         the method parameters were not specified when this
     *         instance was constructed.
     */
    public Map<String,Object> getMethodParameters() {
        return this.methodParameters;
    }

    /**
     * Handles optionally redacting the specified data to attempt to prevent
     * inclusion of sensitive data in a message that might be exposed.
     * 
     * @param sensitiveData The potentially sensitive data to include in the
     *                      exception message.
     * 
     * @return The redacted data or the specified object converted to a 
     *         {@link String}.
     */
    public static String redact(Object sensitiveData) {
        String text = String.valueOf(sensitiveData);
        return (REDACTING) ? "*|REDACTED|*" : text;
    }

    /**
     * Utility method for adding parameter name/value pairs to the specified
     * {@link LinkedHashMap} while properly handling <code>null</code> keys
     * and duplicate keys with suffixes.
     * 
     * @param name The parameter name key.
     * @param value The parameter value.
     */
    private static void put(LinkedHashMap<String,Object>    map, 
                            String                          name, 
                            Object                          value) 
    {
        String key = String.valueOf(name);
        String suffix = "";
        for (int index = 1; map.containsKey(key + suffix); index++) {
            suffix = "-" + index;
        }
        map.put(key + suffix, value);
    }

    /**
     * Creates an unmodifiable <b>ordered</b> {@link LinkedHashMap} representing
     * the specified parameters for helping with construction of
     * {@link SzException} instances.  If the value contains sensitive data then
     * {@link #redact(Object)} should be called with the result from that
     * function being passed instead.
     * <p>
     * <b>NOTE:</b> If the parameter name is <code>null</code> then it is represented 
     * as <code>"null"</code>.  This is done to avoid throwing an exception while
     * trying to construct an {@link SzException}.
     * 
     * @param name The name of the parameter.
     * @param value The value (or redacted value) for the parameter.
     * 
     * @return A new unmodifiable <b>ordered</b> {@link Map} containing the
     *         specified parameter name and parameter value.
     */
    public static Map<String, Object> paramsOf(String name, Object value)
        throws NullPointerException
    {
        LinkedHashMap<String,Object> map = new LinkedHashMap<>();
        put(map, name, value);
        return new ParameterMap(map);
    }

    /**
     * Creates an unmodifiable <b>ordered</b> {@link LinkedHashMap} representing
     * the specified parameters in the order they are specified.  This is for helping
     * with construction of {@link SzException} instances.  If a value contains
     * sensitive data then {@link #redact(Object)} should be called with the result
     * from that function being passed instead.
     * <p>
     * <b>NOTE:</b> Any <code>null</code> parameter names are represented as
     * <code>"null"</code>.  Any duplicate keys are handled by suffixing them to
     * make them unique.  This is done to avoid throwing an exception while trying
     * to construct an {@link SzException}.
     * 
     * @param name1 The name of the first parameter.
     * @param value1 The value (or redacted value) for the first parameter.
     * @param name2 The name of the second parameter.
     * @param value2 The value (or redacted value) for the second parameter.
     * 
     * @return A new unmodifiable <b>ordered</b> {@link Map} containing the
     *         specified parameter names matched to their respective parameter
     *         values.
     */
    public static Map<String, Object> paramsOf(String name1, Object value1,
                                               String name2, Object value2)
    {
        LinkedHashMap<String,Object> map = new LinkedHashMap<>();
        put(map, name1, value1);
        put(map, name2, value2);
        return new ParameterMap(map);
    }

    /**
     * Creates an unmodifiable <b>ordered</b> {@link LinkedHashMap} representing
     * the specified parameters in the order they are specified.  This is for helping
     * with construction of {@link SzException} instances.  If a value contains
     * sensitive data then {@link #redact(Object)} should be called with the result
     * from that function being passed instead.
     * <p>
     * <b>NOTE:</b> Any <code>null</code> parameter names are represented as
     * <code>"null"</code>.  Any duplicate keys are handled by suffixing them to
     * make them unique.  This is done to avoid throwing an exception while trying
     * to construct an {@link SzException}.
     * 
     * @param name1 The name of the first parameter.
     * @param value1 The value (or redacted value) for the first parameter.
     * @param name2 The name of the second parameter.
     * @param value2 The value (or redacted value) for the second parameter.
     * @param name3 The name of the third parameter.
     * @param value3 The value (or redacted value) for the third parameter.
     * 
     * @return A new unmodifiable <b>ordered</b> {@link Map} containing the
     *         specified parameter names matched to their respective parameter
     *         values.
     */
    public static Map<String, Object> paramsOf(String name1, Object value1,
                                               String name2, Object value2,
                                               String name3, Object value3)
    {
        LinkedHashMap<String,Object> map = new LinkedHashMap<>();
        put(map, name1, value1);
        put(map, name2, value2);
        put(map, name3, value3);
        return new ParameterMap(map);
    }

    /**
     * Creates an unmodifiable <b>ordered</b> {@link LinkedHashMap} representing
     * the specified parameters in the order they are specified.  This is for helping
     * with construction of {@link SzException} instances.  If a value contains
     * sensitive data then {@link #redact(Object)} should be called with the result
     * from that function being passed instead.
     * <p>
     * <b>NOTE:</b> Any <code>null</code> parameter names are represented as
     * <code>"null"</code>.  Any duplicate keys are handled by suffixing them to
     * make them unique.  This is done to avoid throwing an exception while trying
     * to construct an {@link SzException}.
     * 
     * @param name1 The name of the first parameter.
     * @param value1 The value (or redacted value) for the first parameter.
     * @param name2 The name of the second parameter.
     * @param value2 The value (or redacted value) for the second parameter.
     * @param name3 The name of the third parameter.
     * @param value3 The value (or redacted value) for the third parameter.
     * @param name4 The name of the fourth parameter.
     * @param value4 The value (or redacted value) for the fourth parameter.
     * 
     * @return A new unmodifiable <b>ordered</b> {@link Map} containing the
     *         specified parameter names matched to their respective parameter
     *         values.
     */
    public static Map<String, Object> paramsOf(String name1, Object value1,
                                               String name2, Object value2,
                                               String name3, Object value3,
                                               String name4, Object value4)
    {
        LinkedHashMap<String,Object> map = new LinkedHashMap<>();
        put(map, name1, value1);
        put(map, name2, value2);
        put(map, name3, value3);
        put(map, name4, value4);
        return new ParameterMap(map);
    }

    /**
     * Creates an unmodifiable <b>ordered</b> {@link LinkedHashMap} representing
     * the specified parameters in the order they are specified.  This is for helping
     * with construction of {@link SzException} instances.  If a value contains
     * sensitive data then {@link #redact(Object)} should be called with the result
     * from that function being passed instead.
     * <p>
     * <b>NOTE:</b> Any <code>null</code> parameter names are represented as
     * <code>"null"</code>.  Any duplicate keys are handled by suffixing them to
     * make them unique.  This is done to avoid throwing an exception while trying
     * to construct an {@link SzException}.
     * 
     * @param name1 The name of the first parameter.
     * @param value1 The value (or redacted value) for the first parameter.
     * @param name2 The name of the second parameter.
     * @param value2 The value (or redacted value) for the second parameter.
     * @param name3 The name of the third parameter.
     * @param value3 The value (or redacted value) for the third parameter.
     * @param name4 The name of the fourth parameter.
     * @param value4 The value (or redacted value) for the fourth parameter.
     * @param name5 The name of the fifth parameter.
     * @param value5 The value (or redacted value) for the fifth parameter.
     * 
     * @return A new unmodifiable <b>ordered</b> {@link Map} containing the
     *         specified parameter names matched to their respective parameter
     *         values.
     */
    public static Map<String, Object> paramsOf(String name1, Object value1,
                                               String name2, Object value2,
                                               String name3, Object value3,
                                               String name4, Object value4,
                                               String name5, Object value5)
    {
        LinkedHashMap<String,Object> map = new LinkedHashMap<>();
        put(map, name1, value1);
        put(map, name2, value2);
        put(map, name3, value3);
        put(map, name4, value4);
        put(map, name5, value5);
        return new ParameterMap(map);
    }

    /**
     * Creates an unmodifiable <b>ordered</b> {@link LinkedHashMap} representing
     * the specified parameters in the order they are specified.  This is for helping
     * with construction of {@link SzException} instances.  If a value contains
     * sensitive data then {@link #redact(Object)} should be called with the result
     * from that function being passed instead.
     * <p>
     * <b>NOTE:</b> Any <code>null</code> parameter names are represented as
     * <code>"null"</code>.  Any duplicate keys are handled by suffixing them to
     * make them unique.  This is done to avoid throwing an exception while trying
     * to construct an {@link SzException}.
     * 
     * @param name1 The name of the first parameter.
     * @param value1 The value (or redacted value) for the first parameter.
     * @param name2 The name of the second parameter.
     * @param value2 The value (or redacted value) for the second parameter.
     * @param name3 The name of the third parameter.
     * @param value3 The value (or redacted value) for the third parameter.
     * @param name4 The name of the fourth parameter.
     * @param value4 The value (or redacted value) for the fourth parameter.
     * @param name5 The name of the fifth parameter.
     * @param value5 The value (or redacted value) for the fifth parameter.
     * @param name6 The name of the sixth parameter.
     * @param value6 The value (or redacted value) for the sixth parameter.
     * 
     * @return A new unmodifiable <b>ordered</b> {@link Map} containing the
     *         specified parameter names matched to their respective parameter
     *         values.
     */
    public static Map<String, Object> paramsOf(String name1, Object value1,
                                               String name2, Object value2,
                                               String name3, Object value3,
                                               String name4, Object value4,
                                               String name5, Object value5,
                                               String name6, Object value6)
    {
        LinkedHashMap<String,Object> map = new LinkedHashMap<>();
        put(map, name1, value1);
        put(map, name2, value2);
        put(map, name3, value3);
        put(map, name4, value4);
        put(map, name5, value5);
        put(map, name6, value6);
        return new ParameterMap(map);
    }

    /**
     * Creates an unmodifiable <b>ordered</b> {@link LinkedHashMap} representing
     * the specified parameters in the order they are specified.  This is for helping
     * with construction of {@link SzException} instances.  If a value contains
     * sensitive data then {@link #redact(Object)} should be called with the result
     * from that function being passed instead.
     * <p>
     * <b>NOTE:</b> Any <code>null</code> parameter names are represented as
     * <code>"null"</code>.  Any duplicate keys are handled by suffixing them to
     * make them unique.  This is done to avoid throwing an exception while trying
     * to construct an {@link SzException}.
     * 
     * @param name1 The name of the first parameter.
     * @param value1 The value (or redacted value) for the first parameter.
     * @param name2 The name of the second parameter.
     * @param value2 The value (or redacted value) for the second parameter.
     * @param name3 The name of the third parameter.
     * @param value3 The value (or redacted value) for the third parameter.
     * @param name4 The name of the fourth parameter.
     * @param value4 The value (or redacted value) for the fourth parameter.
     * @param name5 The name of the fifth parameter.
     * @param value5 The value (or redacted value) for the fifth parameter.
     * @param name6 The name of the sixth parameter.
     * @param value6 The value (or redacted value) for the sixth parameter.
     * @param name7 The name of the seventh parameter.
     * @param value7 The value (or redacted value) for the seventh parameter.
     * 
     * @return A new unmodifiable <b>ordered</b> {@link Map} containing the
     *         specified parameter names matched to their respective parameter
     *         values.
     */
    public static Map<String, Object> paramsOf(String name1, Object value1,
                                               String name2, Object value2,
                                               String name3, Object value3,
                                               String name4, Object value4,
                                               String name5, Object value5,
                                               String name6, Object value6,
                                               String name7, Object value7)
    {
        LinkedHashMap<String,Object> map = new LinkedHashMap<>();
        put(map, name1, value1);
        put(map, name2, value2);
        put(map, name3, value3);
        put(map, name4, value4);
        put(map, name5, value5);
        put(map, name6, value6);
        put(map, name7, value7);
        return new ParameterMap(map);
    }

    /**
     * Creates an unmodifiable <b>ordered</b> {@link LinkedHashMap} representing
     * the specified parameters in the order they are specified.  This is for helping
     * with construction of {@link SzException} instances.  If a value contains
     * sensitive data then {@link #redact(Object)} should be called with the result
     * from that function being passed instead.
     * <p>
     * <b>NOTE:</b> Any <code>null</code> parameter names are represented as
     * <code>"null"</code>.  Any duplicate keys are handled by suffixing them to
     * make them unique.  This is done to avoid throwing an exception while trying
     * to construct an {@link SzException}.
     * 
     * @param name1 The name of the first parameter.
     * @param value1 The value (or redacted value) for the first parameter.
     * @param name2 The name of the second parameter.
     * @param value2 The value (or redacted value) for the second parameter.
     * @param name3 The name of the third parameter.
     * @param value3 The value (or redacted value) for the third parameter.
     * @param name4 The name of the fourth parameter.
     * @param value4 The value (or redacted value) for the fourth parameter.
     * @param name5 The name of the fifth parameter.
     * @param value5 The value (or redacted value) for the fifth parameter.
     * @param name6 The name of the sixth parameter.
     * @param value6 The value (or redacted value) for the sixth parameter.
     * @param name7 The name of the seventh parameter.
     * @param value7 The value (or redacted value) for the seventh parameter.
     * @param name8 The name of the eighth parameter.
     * @param value8 The value (or redacted value) for the eighth parameter.
     * 
     * @return A new unmodifiable <b>ordered</b> {@link Map} containing the
     *         specified parameter names matched to their respective parameter
     *         values.
     */
    public static Map<String, Object> paramsOf(String name1, Object value1,
                                               String name2, Object value2,
                                               String name3, Object value3,
                                               String name4, Object value4,
                                               String name5, Object value5,
                                               String name6, Object value6,
                                               String name7, Object value7,
                                               String name8, Object value8)
    {
        LinkedHashMap<String,Object> map = new LinkedHashMap<>();
        put(map, name1, value1);
        put(map, name2, value2);
        put(map, name3, value3);
        put(map, name4, value4);
        put(map, name5, value5);
        put(map, name6, value6);
        put(map, name7, value7);
        put(map, name8, value8);
        return new ParameterMap(map);
    }

    /**
     * Creates an unmodifiable <b>ordered</b> {@link LinkedHashMap} representing
     * the specified parameters in the order they are specified.  This is for helping
     * with construction of {@link SzException} instances.  If a value contains
     * sensitive data then {@link #redact(Object)} should be called with the result
     * from that function being passed instead.
     * <p>
     * <b>NOTE:</b> Any <code>null</code> parameter names are represented as
     * <code>"null"</code>.  Any duplicate keys are handled by suffixing them to
     * make them unique.  This is done to avoid throwing an exception while trying
     * to construct an {@link SzException}.
     * 
     * @param name1 The name of the first parameter.
     * @param value1 The value (or redacted value) for the first parameter.
     * @param name2 The name of the second parameter.
     * @param value2 The value (or redacted value) for the second parameter.
     * @param name3 The name of the third parameter.
     * @param value3 The value (or redacted value) for the third parameter.
     * @param name4 The name of the fourth parameter.
     * @param value4 The value (or redacted value) for the fourth parameter.
     * @param name5 The name of the fifth parameter.
     * @param value5 The value (or redacted value) for the fifth parameter.
     * @param name6 The name of the sixth parameter.
     * @param value6 The value (or redacted value) for the sixth parameter.
     * @param name7 The name of the seventh parameter.
     * @param value7 The value (or redacted value) for the seventh parameter.
     * @param name8 The name of the eighth parameter.
     * @param value8 The value (or redacted value) for the eighth parameter.
     * @param name9 The name of the eighth parameter.
     * @param value9 The value (or redacted value) for the eighth parameter.
     * 
     * @return A new unmodifiable <b>ordered</b> {@link Map} containing the
     *         specified parameter names matched to their respective parameter
     *         values.
     */
    public static Map<String, Object> paramsOf(String name1, Object value1,
                                               String name2, Object value2,
                                               String name3, Object value3,
                                               String name4, Object value4,
                                               String name5, Object value5,
                                               String name6, Object value6,
                                               String name7, Object value7,
                                               String name8, Object value8,
                                               String name9, Object value9)
    {
        LinkedHashMap<String,Object> map = new LinkedHashMap<>();
        put(map, name1, value1);
        put(map, name2, value2);
        put(map, name3, value3);
        put(map, name4, value4);
        put(map, name5, value5);
        put(map, name6, value6);
        put(map, name7, value7);
        put(map, name8, value8);
        put(map, name9, value9);
        return new ParameterMap(map);
    }

    /**
     * Creates an unmodifiable <b>ordered</b> {@link LinkedHashMap} representing
     * the specified parameters in the order they are specified.  This is for helping
     * with construction of {@link SzException} instances.  If a value contains
     * sensitive data then {@link #redact(Object)} should be called with the result
     * from that function being passed instead.
     * <p>
     * <b>NOTE:</b> Any <code>null</code> parameter names are represented as
     * <code>"null"</code>.  Any duplicate keys are handled by suffixing them to
     * make them unique.  This is done to avoid throwing an exception while trying
     * to construct an {@link SzException}.
     * 
     * @param name1 The name of the first parameter.
     * @param value1 The value (or redacted value) for the first parameter.
     * @param name2 The name of the second parameter.
     * @param value2 The value (or redacted value) for the second parameter.
     * @param name3 The name of the third parameter.
     * @param value3 The value (or redacted value) for the third parameter.
     * @param name4 The name of the fourth parameter.
     * @param value4 The value (or redacted value) for the fourth parameter.
     * @param name5 The name of the fifth parameter.
     * @param value5 The value (or redacted value) for the fifth parameter.
     * @param name6 The name of the sixth parameter.
     * @param value6 The value (or redacted value) for the sixth parameter.
     * @param name7 The name of the seventh parameter.
     * @param value7 The value (or redacted value) for the seventh parameter.
     * @param name8 The name of the eighth parameter.
     * @param value8 The value (or redacted value) for the eighth parameter.
     * @param name9 The name of the eighth parameter.
     * @param value9 The value (or redacted value) for the eighth parameter.
     * @param name10 The name of the eighth parameter.
     * @param value10 The value (or redacted value) for the eighth parameter.
     * 
     * @return A new unmodifiable <b>ordered</b> {@link Map} containing the
     *         specified parameter names matched to their respective parameter
     *         values.
     */
    public static Map<String, Object> paramsOf(String name1, Object value1,
                                               String name2, Object value2,
                                               String name3, Object value3,
                                               String name4, Object value4,
                                               String name5, Object value5,
                                               String name6, Object value6,
                                               String name7, Object value7,
                                               String name8, Object value8,
                                               String name9, Object value9,
                                               String name10, Object value10)
    {
        LinkedHashMap<String,Object> map = new LinkedHashMap<>();
        put(map, name1, value1);
        put(map, name2, value2);
        put(map, name3, value3);
        put(map, name4, value4);
        put(map, name5, value5);
        put(map, name6, value6);
        put(map, name7, value7);
        put(map, name8, value8);
        put(map, name9, value9);
        put(map, name10, value10);
        return new ParameterMap(map);
    }

    /**
     * Provides a private unmodifiable {@link Map} implementation that can
     * be typed-check to know if the incoming {@link Map} of parameters 
     * needs to be copied or if it can be used as-is.
     */
    private static class ParameterMap extends AbstractMap<String,Object> {
        /**
         * The <b>unmodifiable</b> backing {@link Map} for this instance.
         */
        private Map<String,Object> backingMap = null;

        /**
         * Constructs with the specified {@link LinkedHashMap} of parameter
         * name/value pairs.
         */
        private ParameterMap(LinkedHashMap<String,Object> map) {
            this.backingMap = Collections.unmodifiableMap(map);
        }

        @Override
        public Set<Entry<String, Object>> entrySet() {
            return this.backingMap.entrySet();
        }
    }
}
