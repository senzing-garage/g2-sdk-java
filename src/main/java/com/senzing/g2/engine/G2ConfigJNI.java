/**********************************************************************************
 © Copyright Senzing, Inc. 2021
 The source code for this program is not published or otherwise divested
 of its trade secrets, irrespective of what has been deposited with the U.S.
 Copyright Office.
**********************************************************************************/

package com.senzing.g2.engine;

/**
 * Implements the {@link G2Config} interface to call the native implementations
 * of the functions.
 */
class G2ConfigJNI implements G2Fallible {
  static {
    System.loadLibrary("G2");
  }

  /**
   * {@inheritDoc}
   */
  native int init(String  instanceName,
                  String  settings,
                  boolean verboseLogging);

  /**
   * {@inheritDoc}
   */
  native int destroy();

  /**
   * {@inheritDoc}
   */
  native int create(Result<Long> configHandle);

  /**
   * {@inheritDoc}
   */
  native int load(String jsonConfig, Result<Long> configHandle);

  /**
   * {@inheritDoc}
   */
  native int save(long configHandle, StringBuffer response );

  /**
   * {@inheritDoc}
   */
  native int close(long configHandle);

  /**
   * {@inheritDoc}
   */
  native int listDataSources(long configHandle, StringBuffer response);

  /**
   * {@inheritDoc}
   */
  native int addDataSource(long          configHandle,
                           String        inputJson,
                           StringBuffer  response);

  /**
   * {@inheritDoc}
   */
  native int deleteDataSource(long configHandle, String inputJson);

  /**
   * {@inheritDoc}
   */
  public native String getLastException();

  /**
   * {@inheritDoc}
   */
  public native int getLastExceptionCode();

  /**
   * {@inheritDoc}
   */
  public native void clearLastException();
}

