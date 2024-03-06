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
public class G2ConfigJNI implements G2Config
{
  static {
    System.loadLibrary("G2");
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public native int init(String   moduleName,
                         String   iniParams,
                         boolean verboseLogging);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int destroy();

  /**
   * {@inheritDoc}
   */
  @Override
  public native int create(Result<Long> configHandle);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int load(String jsonConfig, Result<Long> configHandle);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int save(long configHandle, StringBuffer response );

  /**
   * {@inheritDoc}
   */
  @Override
  public native int close(long configHandle);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int listDataSources(long configHandle, StringBuffer response);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int addDataSource(long          configHandle,
                                  String        inputJson,
                                  StringBuffer  response);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int deleteDataSource(long configHandle, String inputJson);

  /**
   * {@inheritDoc}
   */
  @Override
  public native String getLastException();

  /**
   * {@inheritDoc}
   */
  @Override
  public native int getLastExceptionCode();

  /**
   * {@inheritDoc}
   */
  @Override
  public native void clearLastException();
}

