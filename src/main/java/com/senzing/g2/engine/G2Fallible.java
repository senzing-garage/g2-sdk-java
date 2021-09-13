package com.senzing.g2.engine;

/**
 * Provides a base interface for Senzing native SDK's can have failures occur.
 */
public interface G2Fallible {
  /**
   * Returns a string about the last error the system received.
   * This is most commonly called after an API function returns an error code (non-zero or NULL)
   *
   * @return An error message
   */
  String getLastException();

  /**
   * Returns the exception code of the last error the system received.
   * This is most commonly called after an API function returns an error code (non-zero or NULL)
   *
   * @return An error code
   */
  int getLastExceptionCode();

  /**
   * Clears the information about the last error the system received.
   */
  void clearLastException();
}
