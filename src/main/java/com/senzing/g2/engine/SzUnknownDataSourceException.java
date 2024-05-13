package com.senzing.g2.engine;

import java.util.Map;

/**
 * Extends {@link SzBadInputException} to define an exceptional condition where a specified data
 * source code is not confifgured in the current active configuration and therefore the data source
 * could not be found.
 */
public class SzUnknownDataSourceException extends SzBadInputException {
  /** Default constructor. */
  public SzUnknownDataSourceException() {
    super();
  }

  /**
   * Constructs with a message explaing the reason for the exception.
   *
   * @param message The message explaining the reason for the exception.
   */
  public SzUnknownDataSourceException(String message) {
    super(message);
  }

  /**
   * Constructs with a message explaing the reason for the exception.
   *
   * @param errorCode The underlying senzing error code.
   * @param message The message explaining the reason for the exception.
   */
  public SzUnknownDataSourceException(int errorCode, String message) {
    super(errorCode, message);
  }

  /**
   * Constructs with the {@link Throwable} that is the underlying cause for the exception.
   *
   * @param cause The message The message explaining the reason for the exception.
   */
  public SzUnknownDataSourceException(Throwable cause) {
    super(cause);
  }

  /**
   * Constructs with a message explaing the reason for the exception and the {@link Throwable} that
   * is the underlying cause for the exception.
   *
   * @param message The message explaining the reason for the exception.
   * @param cause The message The message explaining the reason for the exception.
   */
  public SzUnknownDataSourceException(String message, Throwable cause) {
    super(message, cause);
  }

  /**
   * Constructs with the Senzing error code, the message explaing the reason for the exception and
   * the {@link Throwable} that is the underlying cause for the exception.
   *
   * @param errorCode The underlying senzing error code.
   * @param message The message explaining the reason for the exception.
   * @param cause The message The message explaining the reason for the exception.
   */
  public SzUnknownDataSourceException(int errorCode, String message, Throwable cause) {
    super(errorCode, message, cause);
  }

  /**
   * Constructs with the message explaing the reason for the exception, the method signature of the
   * method that failed and the {@link Map} describing the parmaeters with which that method was
   * called.
   *
   * <p><b>NOTE:</b> The specified {@link Map} describing the parameters should preferrably be in
   * order of how the parameters appear in the method signature. Further, parameter values should be
   * {@linkplain #redact(Object) redacted} to protected sensitive data from being exposed in error
   * dialogs or log files.
   *
   * @param message The message explaining the reason for the exception.
   * @param methodSignature The name of the method being called (usually prefixed by its simple
   *     class name and including its parameter types).
   * @param methodParameters The {@link Map} describing the method parameters, preferrably an
   *     ordered map according to the parameter positions in the method signature.
   */
  public SzUnknownDataSourceException(
      String message, String methodSignature, Map<String, Object> methodParameters) {
    super(message, methodSignature, methodParameters);
  }

  /**
   * Constructs with the Senzing error code, the message explaing the reason for the exception, the
   * method signature of the method that failed and the {@link Map} describing the parmaeters with
   * which that method was called.
   *
   * <p><b>NOTE:</b> The specified {@link Map} describing the parameters should preferrably be in
   * order of how the parameters appear in the method signature. Further, parameter values should be
   * {@linkplain #redact(Object) redacted} to protected sensitive data from being exposed in error
   * dialogs or log files.
   *
   * @param errorCode The underlying senzing error code.
   * @param message The message explaining the reason for the exception.
   * @param methodSignature The name of the method being called (usually prefixed by its simple
   *     class name and including its parameter types).
   * @param methodParameters The {@link Map} describing the method parameters, preferrably an
   *     ordered map according to the parameter positions in the method signature.
   */
  public SzUnknownDataSourceException(
      int errorCode, String message, String methodSignature, Map<String, Object> methodParameters) {
    super(errorCode, message, methodSignature, methodParameters);
  }

  /**
   * Constructs with the message explaing the reason for the exception, the method signature of the
   * method that failed, the {@link Map} describing the parmaeters with which that method was called
   * and the {@link Throwable} that is the underlying cause for the exception.
   *
   * <p><b>NOTE:</b> The specified {@link Map} describing the parameters should preferrably be in
   * order of how the parameters appear in the method signature. Further, parameter values should be
   * {@linkplain #redact(Object) redacted} to protected sensitive data from being exposed in error
   * dialogs or log files.
   *
   * @param message The message explaining the reason for the exception.
   * @param methodSignature The name of the method being called (usually prefixed by its simple
   *     class name and including its parameter types).
   * @param methodParameters The {@link Map} describing the method parameters, preferrably an
   *     ordered map according to the parameter positions in the method signature.
   * @param cause The message The message explaining the reason for the exception.
   */
  public SzUnknownDataSourceException(
      String message,
      String methodSignature,
      Map<String, Object> methodParameters,
      Throwable cause) {
    super(message, methodSignature, methodParameters, cause);
  }

  /**
   * Constructs with the Senzing error code, the message explaing the reason for the exception, the
   * method signature of the method that failed, the {@link Map} describing the parmaeters with
   * which that method was called and the {@link Throwable} that is the underlying cause for the
   * exception.
   *
   * <p><b>NOTE:</b> The specified {@link Map} describing the parameters should preferrably be in
   * order of how the parameters appear in the method signature. Further, parameter values should be
   * {@linkplain #redact(Object) redacted} to protected sensitive data from being exposed in error
   * dialogs or log files.
   *
   * @param errorCode The underlying senzing error code.
   * @param message The message explaining the reason for the exception.
   * @param methodSignature The name of the method being called (usually prefixed by its simple
   *     class name and including its parameter types).
   * @param methodParameters The {@link Map} describing the method parameters, preferrably an
   *     ordered map according to the parameter positions in the method signature.
   * @param cause The message The message explaining the reason for the exception.
   */
  public SzUnknownDataSourceException(
      int errorCode,
      String message,
      String methodSignature,
      Map<String, Object> methodParameters,
      Throwable cause) {
    super(errorCode, message, methodSignature, methodParameters, cause);
  }
}
