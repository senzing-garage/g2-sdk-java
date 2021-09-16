package com.senzing.g2.engine.internal;
import com.senzing.g2.engine.Result;

/**
 * Implements the {@link G2Audit} interface to call the native function
 *
 * @deprecated
 */
@SuppressWarnings({"deprecation", "removal"})
@Deprecated(since = "2.9.0", forRemoval = true)
public class G2AuditJNI implements G2Audit
{
  static {
    System.loadLibrary("G2");
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public native int initV2(String   moduleName,
                           String   iniParams,
                           boolean  verboseLogging);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int initWithConfigIDV2(String   moduleName,
                                       String   iniParams,
                                       long     initConfigID,
                                       boolean  verboseLogging);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int reinitV2(long initConfigID);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int destroy();

  /**
   * {@inheritDoc}
   */
  @Override
  public native long openSession();

  /**
   * {@inheritDoc}
   */
  @Override
  public native void cancelSession(long session);

  /**
   * {@inheritDoc}
   */
  @Override
  public native void closeSession(long session);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int getSummaryData(long session, StringBuffer response);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int getSummaryDataDirect(StringBuffer response);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int getUsedMatchKeys(long         session,
                                     String       fromDataSource,
                                     String       toDataSource,
                                     int          type,
                                     StringBuffer response);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int getUsedPrinciples(long          session,
                                      String        fromDataSource,
                                      String        toDataSource,
                                      int           type,
                                      StringBuffer  response);

  /**
   * {@inheritDoc}
   */
  @Override
  public native long getAuditReport(long    session,
                                    String  fromDataSource,
                                    String  toDataSource,
                                    int     matchLevel);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int getAuditReportV2(long         session,
                                     String       fromDataSource,
                                     String       toDataSource,
                                     int          matchLevel,
                                     Result<Long> reportHandle);

  /**
   * {@inheritDoc}
   */
  @Override
  public native String fetchNext(long reportHandle);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int fetchNextV2(long reportHandle, StringBuffer response);

  /**
   * {@inheritDoc}
   */
  @Override
  public native void closeReport(long reportHandle);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int closeReportV2(long reportHandle);

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
