/**********************************************************************************
Copyright Senzing, Inc. 2017, 2019
The source code for this program is not published or otherwise divested
of its trade secrets, irrespective of what has been deposited with the U.S.
Copyright Office.
**********************************************************************************/

package com.senzing.g2.engine;

public class G2JNI implements G2Engine {
  static {
    System.loadLibrary("G2");
  }

  /**
   * {@inheritDoc}
   */
  public native int initV2(String moduleName, String iniParams, boolean verboseLogging);

  /**
   * {@inheritDoc}
   */
  public native int initWithConfigIDV2(String moduleName, String iniParams, long initConfigID, boolean verboseLogging);

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
  public native int primeEngine();

  
  /**
   * {@inheritDoc}
   */
  public native int purgeRepository();


  /**
   * {@inheritDoc}
   */
  public native String stats();

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

  /**
   * {@inheritDoc}
   */
  public native int exportConfig(StringBuffer response);

  /**
   * {@inheritDoc}
   */
  public native int exportConfig(StringBuffer response, Result<Long> configID);

  /**
   * {@inheritDoc}
   */
  public native int getActiveConfigID(Result<Long> configID);

  /**
   * {@inheritDoc}
   */
  public native int getRepositoryLastModifiedTime(Result<Long> lastModifiedTime);

  /**
   * {@inheritDoc}
   */
  public native int addRecord(String dataSourceCode, String recordID, String jsonData, String loadID);


  /**
   * {@inheritDoc}
   */
  public native int replaceRecord(String dataSourceCode, String recordID, String jsonData, String loadID);
  
  /**
   * {@inheritDoc}
   */
  public native int replaceRecordWithInfo(String dataSourceCode, String recordID, String jsonData, String loadID, int flags, StringBuffer response);


  /**
   * {@inheritDoc}
   */
  public native int addRecordWithReturnedRecordID(String dataSourceCode, StringBuffer recordID, String jsonData, String loadID);

  /**
   * {@inheritDoc}
   */
  public native int addRecordWithInfo(String dataSourceCode, String recordID, String jsonData, String loadID, int flags, StringBuffer response);

  public native int addRecordWithInfoWithReturnedRecordID(String dataSourceCode, String jsonData, String loadID, int flags, StringBuffer recordID, StringBuffer response);
  
  /**
   * {@inheritDoc}
   */
  public native int deleteRecord(String dataSourceCode, String recordID, String loadID);
  
  /**
   * {@inheritDoc}
   */
  public native int deleteRecordWithInfo(String dataSourceCode, String recordID, String loadID, int flags, StringBuffer response);

  /**
   * {@inheritDoc}
   */
  public native int reevaluateRecord(String dataSourceCode, String recordID, int flags);
  /**
   * {@inheritDoc}
   */
  public native int reevaluateEntity(long entityID, int flags);
  /**
   * {@inheritDoc}
   */
  public native int reevaluateRecordWithInfo(String dataSourceCode, String recordID, int flags, StringBuffer response);
  /**
   * {@inheritDoc}
   */
  public native int reevaluateEntityWithInfo(long entityID, int flags, StringBuffer response);


  /**
   * {@inheritDoc}
   */
  public native int searchByAttributes(String jsonData, StringBuffer response);
  public native int searchByAttributesV2(String jsonData, int flags, StringBuffer response);


  /**
   * {@inheritDoc}
   */
  public native int getEntityByEntityID(long entityID, StringBuffer response);
  public native int getEntityByEntityIDV2(long entityID, int flags, StringBuffer response);


  /**
   * {@inheritDoc}
   */
  public native int getEntityByRecordID(String dataSourceCode, String recordID, StringBuffer response);
  public native int getEntityByRecordIDV2(String dataSourceCode, String recordID, int flags, StringBuffer response);


  public native int findPathByEntityID(long entityID1, long entityID2, int maxDegree, StringBuffer response);
  public native int findPathByEntityIDV2(long entityID1, long entityID2, int maxDegree, int flags, StringBuffer response);
  public native int findPathByRecordID(String dataSourceCode1, String recordID1, String dataSourceCode2, String recordID2, int maxDegree, StringBuffer response);
  public native int findPathByRecordIDV2(String dataSourceCode1, String recordID1, String dataSourceCode2, String recordID2, int maxDegree, int flags, StringBuffer response);

  public native int findPathExcludingByEntityID(long entityID1, long entityID2, int maxDegree, String excludedEntities, int flags, StringBuffer response);
  public native int findPathExcludingByRecordID(String dataSourceCode1, String recordID1, String dataSourceCode2, String recordID2, int maxDegree, String excludedEntities, int flags, StringBuffer response);

  public native int findPathIncludingSourceByEntityID(long entityID1, long entityID2, int maxDegree, String excludedEntities, String requiredDsrcs, int flags, StringBuffer response);
  public native int findPathIncludingSourceByRecordID(String dataSourceCode1, String recordID1, String dataSourceCode2, String recordID2, int maxDegree, String excludedEntities, String requiredDsrcs, int flags, StringBuffer response);

  public native int findNetworkByEntityID(String entityList, int maxDegree, int buildOutDegree, int maxEntities, StringBuffer response);
  public native int findNetworkByEntityIDV2(String entityList, int maxDegree, int buildOutDegree, int maxEntities, int flags, StringBuffer response);
  public native int findNetworkByRecordID(String recordList, int maxDegree, int buildOutDegree, int maxEntities, StringBuffer response);
  public native int findNetworkByRecordIDV2(String recordList, int maxDegree, int buildOutDegree, int maxEntities, int flags, StringBuffer response);

  public native int whyEntityByRecordID(String dataSourceCode, String recordID, StringBuffer response);
  public native int whyEntityByRecordIDV2(String dataSourceCode, String recordID, int flags, StringBuffer response);
  public native int whyEntityByEntityID(long entityID, StringBuffer response);
  public native int whyEntityByEntityIDV2(long entityID, int flags, StringBuffer response);

  public native int whyRecords(String dataSourceCode1, String recordID1, String dataSourceCode2, String recordID2, StringBuffer response);
  public native int whyRecordsV2(String dataSourceCode1, String recordID1, String dataSourceCode2, String recordID2, int flags, StringBuffer response);

  public native int whyEntities(long entityID1, long entityID2, StringBuffer response);
  public native int whyEntitiesV2(long entityID1, long entityID2, int flags, StringBuffer response);

  /**
   * {@inheritDoc}
   */
  public native int getRecord(String dataSourceCode, String recordID, StringBuffer response);
  public native int getRecordV2(String dataSourceCode, String recordID, int flags, StringBuffer response);


  /**
   * {@inheritDoc}
   */
  public native long exportJSONEntityReport(int flags);
  public native int exportJSONEntityReportV3(int flags, Result<Long> exportHandle);


  /**
   * {@inheritDoc}
   */
  public native long exportCSVEntityReportV2(String csvColumnList, int flags);
  public native int exportCSVEntityReportV3(String csvColumnList, int flags, Result<Long> exportHandle);


  /**
   * {@inheritDoc}
   */
  public native String fetchNext(long exportHandle);
  public native int fetchNextV3(long exportHandle, StringBuffer response);


  /**
   * {@inheritDoc}
   */
  public native void closeExport(long exportHandle);
  public native int closeExportV3(long exportHandle);

  /**
   * {@inheritDoc}
   */
  public native int processRedoRecord(StringBuffer record);
  
  /**
   * {@inheritDoc}
   */
  public native int processRedoRecordWithInfo(int flags, StringBuffer record, StringBuffer response);

  /**
   * {@inheritDoc}
   */
  public native int getRedoRecord(StringBuffer record);

  /**
   * {@inheritDoc}
   */
  public native long countRedoRecords();


  /**
   * {@inheritDoc}
   */
  public native int process(String record);

  /**
   * {@inheritDoc}
   */
  public native int process(String record, StringBuffer response);
  
  /**
   * {@inheritDoc}
   */
  public native int processWithInfo(String record, int flags, StringBuffer response);

}

