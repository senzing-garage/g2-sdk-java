package com.senzing.g2.engine.internal;
import com.senzing.g2.engine.Result;

/**
 * Implements the {@link G2Audit} interface to call the native function
 *
 * @deprecated
 */
public class G2AuditJNI implements G2Audit 
{
  static {
    System.loadLibrary("G2");
  }

  /**
   * {@inheritDoc}
   */
  public native int initV2(String   moduleName,
                           String   iniParams,
                           boolean  verboseLogging);

  /**
   * {@inheritDoc}
   */
  public native int initWithConfigIDV2(String   moduleName,
                                       String   iniParams,
                                       long     initConfigID,
                                       boolean  verboseLogging);

  /**
   * {@inheritDoc}
   */
  public native int reinitV2(long initConfigID);

  /**
   * {@inheritDoc}
   */
  public native int destroy();

  /**
   * {@inheritDoc}
   */
  public native long openSession();

  /**
   * {@inheritDoc}
   */
  public native void cancelSession(long session);

  /**
   * {@inheritDoc}
   */
  public native void closeSession(long session);

  /**
   * {@inheritDoc}
   */
  public native int getSummaryData(long session, StringBuffer response);

  /**
   * {@inheritDoc}
   */
  public native int getSummaryDataDirect(StringBuffer response);

  /**
   * {@inheritDoc}
   */
  public native int getUsedMatchKeys(long         session,
                                     String       fromDataSource,
                                     String       toDataSource,
                                     int          type,
                                     StringBuffer response);

  /**
   * {@inheritDoc}
   */
  public native int getUsedPrinciples(long          session,
                                      String        fromDataSource,
                                      String        toDataSource,
                                      int           type,
                                      StringBuffer  response);

  /**
   * {@inheritDoc}
   */
  public native long getAuditReport(long    session,
                                    String  fromDataSource,
                                    String  toDataSource,
                                    int     matchLevel);

  /**
   * {@inheritDoc}
   */
  public native int getAuditReportV2(long         session,
                                     String       fromDataSource,
                                     String       toDataSource,
                                     int          matchLevel,
                                     Result<Long> reportHandle);

  /**
   * {@inheritDoc}
   */
  public native String fetchNext(long reportHandle);

  /**
   * {@inheritDoc}
   */
  public native int fetchNextV2(long reportHandle, StringBuffer response);

  /**
   * {@inheritDoc}
   */
  public native void closeReport(long reportHandle);

  /**
   * {@inheritDoc}
   */
  public native int closeReportV2(long reportHandle);

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
