/**********************************************************************************
Copyright Senzing, Inc. 2017, 2019
The source code for this program is not published or otherwise divested
of its trade secrets, irrespective of what has been deposited with the U.S.
Copyright Office.
**********************************************************************************/

package com.senzing.g2.engine;

/**
 * Implements the {@link G2Engine} interface to call the native implementations
 * of each function.
 */
public class G2JNI implements G2Engine {
  static {
    System.loadLibrary("G2");
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public native int initV2(String moduleName, String iniParams, boolean verboseLogging);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int initWithConfigIDV2(String moduleName, String iniParams, long initConfigID, boolean verboseLogging);

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
  public native int primeEngine();
  
  /**
   * {@inheritDoc}
   */
  @Override
  public native int purgeRepository();

  /**
   * {@inheritDoc}
   */
  @Override
  public native String stats();

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

  /**
   * {@inheritDoc}
   */
  @Override
  public native int exportConfig(StringBuffer response);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int exportConfig(StringBuffer response, Result<Long> configID);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int getActiveConfigID(Result<Long> configID);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int getRepositoryLastModifiedTime(Result<Long> lastModifiedTime);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int addRecord(String dataSourceCode, String recordID, String jsonData, String loadID);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int replaceRecord(String dataSourceCode, String recordID, String jsonData, String loadID);
  
  /**
   * {@inheritDoc}
   */
  @Override
  public native int replaceRecordWithInfo(String        dataSourceCode,
                                          String        recordID,
                                          String        jsonData,
                                          String        loadID,
                                          long          flags,
                                          StringBuffer  response);


  /**
   * {@inheritDoc}
   */
  @Override
  public native int addRecordWithReturnedRecordID(String        dataSourceCode,
                                                  StringBuffer  recordID,
                                                  String        jsonData,
                                                  String        loadID);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int addRecordWithInfo(String        dataSourceCode,
                                      String        recordID,
                                      String        jsonData,
                                      String        loadID,
                                      long          flags,
                                      StringBuffer  response);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int addRecordWithInfoWithReturnedRecordID(
      String        dataSourceCode,
      String        jsonData,
      String        loadID,
      long          flags,
      StringBuffer  recordID,
      StringBuffer  response);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int deleteRecord(String dataSourceCode,
                                 String recordID,
                                 String loadID);
  
  /**
   * {@inheritDoc}
   */
  @Override
  public native int deleteRecordWithInfo(String       dataSourceCode,
                                         String       recordID,
                                         String       loadID,
                                         long         flags,
                                         StringBuffer response);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int reevaluateRecord(String dataSourceCode,
                                     String recordID,
                                     long   flags);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int reevaluateEntity(long entityID, long flags);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int reevaluateRecordWithInfo(String       dataSourceCode,
                                             String       recordID,
                                             long         flags,
                                             StringBuffer response);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int reevaluateEntityWithInfo(long         entityID,
                                             long         flags,
                                             StringBuffer response);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int searchByAttributes(String jsonData, StringBuffer response);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int searchByAttributesV2(String       jsonData,
                                         long         flags,
                                         StringBuffer response);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int getEntityByEntityID(long entityID, StringBuffer response);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int getEntityByEntityIDV2(long          entityID,
                                          long          flags,
                                          StringBuffer  response);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int getEntityByRecordID(String        dataSourceCode,
                                        String        recordID,
                                        StringBuffer  response);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int getEntityByRecordIDV2(String        dataSourceCode,
                                          String        recordID,
                                          long          flags,
                                          StringBuffer  response);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int findPathByEntityID(long         entityID1,
                                       long         entityID2,
                                       int          maxDegree,
                                       StringBuffer response);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int findPathByEntityIDV2(long         entityID1,
                                         long         entityID2,
                                         int          maxDegree,
                                         long         flags,
                                         StringBuffer response);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int findPathByRecordID(String       dataSourceCode1,
                                       String       recordID1,
                                       String       dataSourceCode2,
                                       String       recordID2,
                                       int          maxDegree,
                                       StringBuffer response);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int findPathByRecordIDV2(String       dataSourceCode1,
                                         String       recordID1,
                                         String       dataSourceCode2,
                                         String       recordID2,
                                         int          maxDegree,
                                         long         flags,
                                         StringBuffer response);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int findPathExcludingByEntityID(long          entityID1,
                                                long          entityID2,
                                                int           maxDegree,
                                                String        excludedEntities,
                                                long          flags,
                                                StringBuffer  response);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int findPathExcludingByRecordID(String        dataSourceCode1,
                                                String        recordID1,
                                                String        dataSourceCode2,
                                                String        recordID2,
                                                int           maxDegree,
                                                String        excludedEntities,
                                                long          flags,
                                                StringBuffer  response);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int findPathIncludingSourceByEntityID(
      long          entityID1,
      long          entityID2,
      int           maxDegree,
      String        excludedEntities,
      String        requiredDsrcs,
      long          flags,
      StringBuffer  response);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int findPathIncludingSourceByRecordID(
      String        dataSourceCode1,
      String        recordID1,
      String        dataSourceCode2,
      String        recordID2,
      int           maxDegree,
      String        excludedEntities,
      String        requiredDsrcs,
      long          flags,
      StringBuffer  response);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int findNetworkByEntityID(String        entityList,
                                          int           maxDegree,
                                          int           buildOutDegree,
                                          int           maxEntities,
                                          StringBuffer  response);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int findNetworkByEntityIDV2(String        entityList,
                                            int           maxDegree,
                                            int           buildOutDegree,
                                            int           maxEntities,
                                            long          flags,
                                            StringBuffer  response);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int findNetworkByRecordID(String        recordList,
                                          int           maxDegree,
                                          int           buildOutDegree,
                                          int           maxEntities,
                                          StringBuffer  response);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int findNetworkByRecordIDV2(String        recordList,
                                            int           maxDegree,
                                            int           buildOutDegree,
                                            int           maxEntities,
                                            long          flags,
                                            StringBuffer  response);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int whyEntityByRecordID(String        dataSourceCode,
                                        String        recordID,
                                        StringBuffer  response);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int whyEntityByRecordIDV2(String        dataSourceCode,
                                          String        recordID,
                                          long          flags,
                                          StringBuffer  response);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int whyEntityByEntityID(long entityID, StringBuffer response);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int whyEntityByEntityIDV2(long          entityID,
                                          long          flags,
                                          StringBuffer  response);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int whyRecords(String       dataSourceCode1,
                               String       recordID1,
                               String       dataSourceCode2,
                               String       recordID2,
                               StringBuffer response);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int whyRecordsV2(String       dataSourceCode1,
                                 String       recordID1,
                                 String       dataSourceCode2,
                                 String       recordID2,
                                 long         flags,
                                 StringBuffer response);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int whyEntities(long          entityID1,
                                long          entityID2,
                                StringBuffer  response);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int whyEntitiesV2(long          entityID1,
                                  long          entityID2,
                                  long          flags,
                                  StringBuffer  response);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int getRecord(String        dataSourceCode,
                              String        recordID,
                              StringBuffer  response);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int getRecordV2(String        dataSourceCode,
                                String        recordID,
                                long          flags,
                                StringBuffer  response);

  /**
   * {@inheritDoc}
   */
  @Override
  public native long exportJSONEntityReport(long flags);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int exportJSONEntityReportV3(long         flags,
                                             Result<Long> exportHandle);

  /**
   * {@inheritDoc}
   */
  @Override
  public native long exportCSVEntityReportV2(String csvColumnList, long flags);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int exportCSVEntityReportV3(String        csvColumnList,
                                            long          flags,
                                            Result<Long>  exportHandle);

  /**
   * {@inheritDoc}
   */
  @Override
  public native String fetchNext(long exportHandle);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int fetchNextV3(long exportHandle, StringBuffer response);

  /**
   * {@inheritDoc}
   */
  @Override
  public native void closeExport(long exportHandle);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int closeExportV3(long exportHandle);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int processRedoRecord(StringBuffer record);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int processRedoRecordWithInfo(long          flags,
                                              StringBuffer  record,
                                              StringBuffer  response);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int getRedoRecord(StringBuffer record);

  /**
   * {@inheritDoc}
   */
  @Override
  public native long countRedoRecords();

  /**
   * {@inheritDoc}
   */
  @Override
  public native int process(String record);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int process(String record, StringBuffer response);

  /**
   * {@inheritDoc}
   */
  @Override
  public native int processWithInfo(String        record,
                                    long          flags,
                                    StringBuffer  response);
}

