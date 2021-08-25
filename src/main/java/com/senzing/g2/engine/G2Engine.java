/**********************************************************************************
Copyright Senzing, Inc. 2017-2021
The source code for this program is not published or otherwise divested
of its trade secrets, irrespective of what has been deposited with the U.S.
Copyright Office.
**********************************************************************************/

package com.senzing.g2.engine;

public interface G2Engine extends G2Fallible 
{
  /**
   * The bitwise flag for export functionality to indicate that
   * we should include "resolved" relationships
   *
   */
  static final int G2_EXPORT_INCLUDE_RESOLVED = ( 1 << 0 );
  /**
   * The bitwise flag for export functionality to indicate that
   * we should include "possibly same" relationships
   *
   */
  static final int G2_EXPORT_INCLUDE_POSSIBLY_SAME = ( 1 << 1 );
  /**
   * The bitwise flag for export functionality to indicate that
   * we should include "possibly related" relationships
   *
   */
  static final int G2_EXPORT_INCLUDE_POSSIBLY_RELATED = ( 1 << 2 );
  /**
   * The bitwise flag for export functionality to indicate that
   * we should include "name only" relationships
   *
   */
  static final int G2_EXPORT_INCLUDE_NAME_ONLY = ( 1 << 3 );
  /**
   * The bitwise flag for export functionality to indicate that
   * we should include "disclosed" relationships
   *
   */
  static final int G2_EXPORT_INCLUDE_DISCLOSED = ( 1 << 4 );
  /**
   * The bitwise flag for export functionality to indicate that
   * we should include singleton entities
   *
   */
  static final int G2_EXPORT_INCLUDE_SINGLETONS = ( 1 << 5 );
  /**
   * The bitwise flag for export functionality to indicate that
   * we should include all entities
   *
   */
  static final int G2_EXPORT_INCLUDE_ALL_ENTITIES = (G2_EXPORT_INCLUDE_RESOLVED | G2_EXPORT_INCLUDE_SINGLETONS);
  /**
   * The bitwise flag for export functionality to indicate that
   * we should include all relationships
   *
   */
  static final int G2_EXPORT_INCLUDE_ALL_RELATIONSHIPS = (G2_EXPORT_INCLUDE_POSSIBLY_SAME | G2_EXPORT_INCLUDE_POSSIBLY_RELATED | G2_EXPORT_INCLUDE_NAME_ONLY | G2_EXPORT_INCLUDE_DISCLOSED);

  
  
  /**
   * The bitwise flag for including possibly-same relations for entities
   */
  static final int G2_ENTITY_INCLUDE_POSSIBLY_SAME_RELATIONS = ( 1 << 6 );
  /**
   * The bitwise flag for including possibly-related relations for entities
   */
  static final int G2_ENTITY_INCLUDE_POSSIBLY_RELATED_RELATIONS = ( 1 << 7 );
  /**
   * The bitwise flag for including name-only relations for entities
   */
  static final int G2_ENTITY_INCLUDE_NAME_ONLY_RELATIONS = ( 1 << 8 );
  /**
   * The bitwise flag for including disclosed relations for entities
   */
  static final int G2_ENTITY_INCLUDE_DISCLOSED_RELATIONS = ( 1 << 9 );
  /**
   * The bitwise flag for including all relations for entities
   */
  static final int G2_ENTITY_INCLUDE_ALL_RELATIONS = (G2_ENTITY_INCLUDE_POSSIBLY_SAME_RELATIONS | G2_ENTITY_INCLUDE_POSSIBLY_RELATED_RELATIONS | G2_ENTITY_INCLUDE_NAME_ONLY_RELATIONS | G2_ENTITY_INCLUDE_DISCLOSED_RELATIONS);

  
  /**
   * The bitwise flag for including all features for entities
   */
  static final int G2_ENTITY_INCLUDE_ALL_FEATURES = ( 1 << 10 );
  /**
   * The bitwise flag for including representative features for entities
   */
  static final int G2_ENTITY_INCLUDE_REPRESENTATIVE_FEATURES = ( 1 << 11 );

  
  /**
   * The bitwise flag for including the name of the entity
   */
  static final int G2_ENTITY_INCLUDE_ENTITY_NAME = ( 1 << 12 );
  /**
   * The bitwise flag for including the record summary of the entity
   */
  static final int G2_ENTITY_INCLUDE_RECORD_SUMMARY = ( 1 << 13 );
  /**
   * The bitwise flag for including the basic record data for the entity
   */
  static final int G2_ENTITY_INCLUDE_RECORD_DATA = ( 1 << 14 );
  /**
   * The bitwise flag for including the record matching info for the entity
   */
  static final int G2_ENTITY_INCLUDE_RECORD_MATCHING_INFO = ( 1 << 15 );
  /**
   * The bitwise flag for including the record json data for the entity
   */
  static final int G2_ENTITY_INCLUDE_RECORD_JSON_DATA = ( 1 << 16 );
  /**
   * The bitwise flag for including the record formattted data for the entity
   */
  static final int G2_ENTITY_INCLUDE_RECORD_FORMATTED_DATA = ( 1 << 17 );
  /**
   * The bitwise flag for the features identifiers for the records
   */
  static final int G2_ENTITY_INCLUDE_RECORD_FEATURE_IDS = ( 1 << 18 );
  /**
   * The bitwise flag for including the name of the related entities
   */
  static final int G2_ENTITY_INCLUDE_RELATED_ENTITY_NAME = ( 1 << 19 );
  /**
   * The bitwise flag for including the record matching info of the related entities
   */
  static final int G2_ENTITY_INCLUDE_RELATED_MATCHING_INFO = ( 1 << 20 );
  /**
   * The bitwise flag for including the record summary of the related entities
   */
  static final int G2_ENTITY_INCLUDE_RELATED_RECORD_SUMMARY = ( 1 << 21 );
  /**
   * The bitwise flag for including the basic record data of the related entities
   */
  static final int G2_ENTITY_INCLUDE_RELATED_RECORD_DATA = ( 1 << 22 );


  /**
   * The bitwise flag for including internal features in entity output
   */
  static final int G2_ENTITY_OPTION_INCLUDE_INTERNAL_FEATURES = ( 1 << 23 );
  /**
   * The bitwise flag for including feature statistics in entity output.
   */
  static final int G2_ENTITY_OPTION_INCLUDE_FEATURE_STATS = ( 1 << 24 );


  /**
   * The bitwise flag for find-path functionality to indicate that
   * excluded entities are still allowed, but not preferred
   */
  static final int G2_FIND_PATH_PREFER_EXCLUDE = ( 1 << 25 );


  /**
   * The bitwise flag for including feature scores from search results
   */
  static final int G2_SEARCH_INCLUDE_FEATURE_SCORES = ( 1 << 26 );
  /**
   * The bitwise flag for including statistics from search results
   */
  static final int G2_SEARCH_INCLUDE_STATS = ( 1 << 27 );


  /**
   * The bitwise flag for search functionality to indicate that
   * we should include "resolved" match level results
   *
   */
  static final int G2_SEARCH_INCLUDE_RESOLVED = (G2_EXPORT_INCLUDE_RESOLVED);
  /**
   * The bitwise flag for search functionality to indicate that
   * we should include "possibly same" match level results
   *
   */
  static final int G2_SEARCH_INCLUDE_POSSIBLY_SAME = (G2_EXPORT_INCLUDE_POSSIBLY_SAME);
  /**
   * The bitwise flag for search functionality to indicate that
   * we should include "possibly related" match level results
   *
   */
  static final int G2_SEARCH_INCLUDE_POSSIBLY_RELATED = (G2_EXPORT_INCLUDE_POSSIBLY_RELATED);
  /**
   * The bitwise flag for search functionality to indicate that
   * we should include "name only" match level results
   *
   */
  static final int G2_SEARCH_INCLUDE_NAME_ONLY = (G2_EXPORT_INCLUDE_NAME_ONLY);
  /**
   * The bitwise flag for search functionality to indicate that
   * we should include all match level results
   *
   */
  static final int G2_SEARCH_INCLUDE_ALL_ENTITIES = (G2_SEARCH_INCLUDE_RESOLVED | G2_SEARCH_INCLUDE_POSSIBLY_SAME | G2_SEARCH_INCLUDE_POSSIBLY_RELATED | G2_SEARCH_INCLUDE_NAME_ONLY);
  
  
  
  /**
   * The default recommended bitwise flag values for getting records
   */
  static final int G2_RECORD_DEFAULT_FLAGS = (G2_ENTITY_INCLUDE_RECORD_JSON_DATA);
  /**
   * The default recommended bitwise flag values for getting entities
   */
  static final int G2_ENTITY_DEFAULT_FLAGS = (G2_ENTITY_INCLUDE_ALL_RELATIONS | G2_ENTITY_INCLUDE_REPRESENTATIVE_FEATURES | G2_ENTITY_INCLUDE_ENTITY_NAME | G2_ENTITY_INCLUDE_RECORD_SUMMARY | G2_ENTITY_INCLUDE_RECORD_DATA | G2_ENTITY_INCLUDE_RECORD_MATCHING_INFO | G2_ENTITY_INCLUDE_RELATED_ENTITY_NAME | G2_ENTITY_INCLUDE_RELATED_RECORD_SUMMARY | G2_ENTITY_INCLUDE_RELATED_MATCHING_INFO);
  /**
   * The default recommended bitwise flag values for getting entities
   */
  static final int G2_ENTITY_BRIEF_DEFAULT_FLAGS = (G2_ENTITY_INCLUDE_RECORD_MATCHING_INFO | G2_ENTITY_INCLUDE_ALL_RELATIONS | G2_ENTITY_INCLUDE_RELATED_MATCHING_INFO);
  /**
   * The default recommended bitwise flag values for exporting entities
   */
  static final int G2_EXPORT_DEFAULT_FLAGS = (G2_EXPORT_INCLUDE_ALL_ENTITIES | G2_EXPORT_INCLUDE_ALL_RELATIONSHIPS | G2_ENTITY_INCLUDE_ALL_RELATIONS | G2_ENTITY_INCLUDE_REPRESENTATIVE_FEATURES | G2_ENTITY_INCLUDE_ENTITY_NAME | G2_ENTITY_INCLUDE_RECORD_DATA | G2_ENTITY_INCLUDE_RECORD_MATCHING_INFO | G2_ENTITY_INCLUDE_RELATED_MATCHING_INFO);
  /**
   * The default recommended bitwise flag values for finding entity paths
   */
  static final int G2_FIND_PATH_DEFAULT_FLAGS = (G2_ENTITY_INCLUDE_ALL_RELATIONS | G2_ENTITY_INCLUDE_ENTITY_NAME | G2_ENTITY_INCLUDE_RECORD_SUMMARY | G2_ENTITY_INCLUDE_RELATED_MATCHING_INFO);
  /**
   * The default recommended bitwise flag values for why-analysis on entities
   */
  static final int G2_WHY_ENTITY_DEFAULT_FLAGS = (G2_ENTITY_DEFAULT_FLAGS | G2_ENTITY_INCLUDE_RECORD_FEATURE_IDS | G2_ENTITY_OPTION_INCLUDE_INTERNAL_FEATURES | G2_ENTITY_OPTION_INCLUDE_FEATURE_STATS);

  /**
   * The default recommended bitwise flag values for searching by attributes, returning all matching entities
   */
  static final int G2_SEARCH_BY_ATTRIBUTES_ALL = (G2_SEARCH_INCLUDE_ALL_ENTITIES | G2_ENTITY_INCLUDE_REPRESENTATIVE_FEATURES | G2_ENTITY_INCLUDE_ENTITY_NAME | G2_ENTITY_INCLUDE_RECORD_SUMMARY | G2_SEARCH_INCLUDE_FEATURE_SCORES);
  /**
   * The default recommended bitwise flag values for searching by attributes, returning only strongly matching entities
   */
  static final int G2_SEARCH_BY_ATTRIBUTES_STRONG = (G2_SEARCH_INCLUDE_RESOLVED | G2_SEARCH_INCLUDE_POSSIBLY_SAME | G2_ENTITY_INCLUDE_REPRESENTATIVE_FEATURES | G2_ENTITY_INCLUDE_ENTITY_NAME | G2_ENTITY_INCLUDE_RECORD_SUMMARY | G2_SEARCH_INCLUDE_FEATURE_SCORES);
  /**
   * The default recommended bitwise flag values for searching by attributes, returning minimal data with all matches
   */
  static final int G2_SEARCH_BY_ATTRIBUTES_MINIMAL_ALL = (G2_SEARCH_INCLUDE_ALL_ENTITIES);
  /**
   * The default recommended bitwise flag values for searching by attributes, returning the minimal data, and returning only the strongest matches
   */
  static final int G2_SEARCH_BY_ATTRIBUTES_MINIMAL_STRONG = (G2_SEARCH_INCLUDE_RESOLVED | G2_SEARCH_INCLUDE_POSSIBLY_SAME);
  /**
   * The default recommended bitwise flag values for searching by attributes
   */
  static final int G2_SEARCH_BY_ATTRIBUTES_DEFAULT_FLAGS = (G2_SEARCH_BY_ATTRIBUTES_ALL);
  
  
  
  
  /**
   * Initializes the G2 engine
   * This should only be called once per process.  Currently re-initializing the G2 engin
   * after a destroy requires unloaded the class loader used to load this class.
   *
   * @param moduleName A short name given to this instance of the engine
   * @param iniParams A JSON string containing configuration paramters
   * @param verboseLogging Enable diagnostic logging which will print a massive amount of information to stdout
   *
   * @return 0 on success
   */
  int initV2(String moduleName, String iniParams, boolean verboseLogging);
  int initWithConfigIDV2(String moduleName, String iniParams, long initConfigID, boolean verboseLogging);
  int reinitV2(long initConfigID);

  
  /**
   * Uninitializes the G2 engine.
   *
   * @return 0 on success, -1 if the engine is not initialized
   */
  int destroy();


  /**
   * May optionally be called to pre-initialize some of the heavier weight
   * internal resources of the G2 engine.
   */
  int primeEngine();


  /**
   * Purges all data in the configured repository
   * WARNING: There is no undoing from this.  Make sure your repository is regularly backed up.
   *
   * @return 0 on sucess
   */
  int purgeRepository();


  /**
   * Returns the current internal engine workload statistics for the process.
   * The counters are reset after each call.
   *
   * @return JSON document of workload statistics
   */
  String stats();

  
  /**
   * Exports the JSON configuration that is currently loaded into the engine.
   *
   * @param response The StringBuffer to retrieve the JSON configuration document
   *
   * @return 0 on success
   */
  int exportConfig(StringBuffer response);
  int exportConfig(StringBuffer response, Result<Long> configID);

  
  /**
   * Returns an identifier for the loaded G2 engine configuration
   *
   * @param configID The identifier value for the config
   *
   * @return 0 on success
   */
  int getActiveConfigID(Result<Long> configID);

  
  /**
   * Returns a long integer representing number of seconds since 
   * January 1, 1970 12:00am GMT (epoch time).  This indicates the last
   * time the data repository was modified.
   *
   * @param lastModifiedTime The last modified time of the data repository
   *
   * @return 0 on success
   */
  int getRepositoryLastModifiedTime(Result<Long> lastModifiedTime);

  
  /**
   * Loads the JSON record
   *
   * @param dataSourceCode The data source for the observation.
   * @param recordID The ID for the record
   * @param jsonData A JSON document containing the attribute information
   *        for the observation.
   * @param loadID The observation load ID for the record, can be null and will default to dataSourceCode
   *
   * @return Returns 0 on success
   */
  int addRecord(String dataSourceCode, String recordID, String jsonData, String loadID);


  /**
   * Replace the JSON record that has already been loaded
   *
   * @param dataSourceCode The data source for the observation.
   * @param recordID The ID for the record
   * @param jsonData A JSON document containing the attribute information
   *        for the observation.
   * @param loadID The observation load ID for the record, can be null and will default to dataSourceCode
   *
   * @return Returns 0 on success
   */
  int replaceRecord(String dataSourceCode, String recordID, String jsonData, String loadID);
  
  
  /**
   * Replace the JSON record that has already been loaded and returns
   * a list of modified resolved entities
   *
   * @param dataSourceCode The data source for the observation.
   * @param recordID The ID for the record
   * @param jsonData A JSON document containing the attribute information
   *        for the observation.
   * @param loadID The observation load ID for the record, can be null and will default to dataSourceCode
   * @param response A memory buffer for returning the response document.
   *        If an error occurred, an error response is stored here.
   *
   * @return Returns 0 on success
   */
  int replaceRecordWithInfo(String dataSourceCode, String recordID, String jsonData, String loadID, int flags, StringBuffer response);


  /**
   * Loads the JSON record.  This works similarly to the G2_addRecord function, 
   * except that instead or requiring the recordID as an input parameter, the 
   * recordID is generated internally and returned through the parameter list.
   *
   * @param dataSourceCode The data source for the observation.
   * @param recordID A buffer that returns the ID for the record
   * @param jsonData A JSON document containing the attribute information
   *        for the observation.
   * @param loadID The observation load ID for the record, can be null and will default to dataSourceCode
   *
   * @return Returns 0 on success
   */
  int addRecordWithReturnedRecordID(String dataSourceCode, StringBuffer recordID, String jsonData, String loadID);
  
  int addRecordWithInfoWithReturnedRecordID(String dataSourceCode, String jsonData, String loadID, int flags, StringBuffer recordID, StringBuffer response);

  
  /**
   * This method is used to add entity data into the system.  This works
   * similarly to the G2_addRecord function, except that instead or requiring
   * the recordID as an input parameter, the recordID is generated internally
   * and returned through the parameter list. A list of modified resolved 
   * entities is also returned
   *
   * @param dataSourceCode The data source for the observation.
   * @param recordID The ID for the record. If specified, then it will be used, if empty string, then
   *        G2 will generate an ID
   * @param jsonData A JSON document containing the attribute information for the observation.
   * @param loadID The observation load ID for the record, can be NULL and will default to dataSourceCode
   * @param flags Reserved for future use
   * @param response A memory buffer for returning the response document.
   *        If an error occurred, an error response is stored here.
   * @return Returns 0 for success
   */
   int addRecordWithInfo(String dataSourceCode, String recordID, String jsonData, String loadID, int flags, StringBuffer response);

  
  /**
   * Delete the record that has already been loaded
   *
   * @param dataSourceCode The data source for the observation.
   * @param recordID The ID for the record
   * @param loadID The observation load ID for the record, can be null and will default to dataSourceCode
   *
   * @return Returns 0 on success
   */
  int deleteRecord(String dataSourceCode, String recordID, String loadID);
  
  
  /**
   * Delete the record that has already been loaded. Returns a list 
   * of modified resolved entities
   *
   * @param dataSourceCode The data source for the observation.
   * @param recordID The ID for the record
   * @param loadID The observation load ID for the record, can be null and will default to dataSourceCode
   * @param response A memory buffer for returning the response document.
   *        If an error occurred, an error response is stored here.
   *
   * @return Returns 0 on success
   */
  int deleteRecordWithInfo(String dataSourceCode, String recordID, String loadID, int flags, StringBuffer response);


  /**
   * Reevaluate a record that has already been loaded
   *
   * @param dataSourceCode The data source for the observation.
   * @param recordID The ID for the record
   * @param flags Control flags
   *
   * @return Returns 0 on success
   */
  int reevaluateRecord(String dataSourceCode, String recordID, int flags);
  
  
  /**
   * Reevaluate a record that has already been loaded. Returns a list
   * of resolved entities.
   *
   * @param dataSourceCode The data source for the observation.
   * @param recordID The ID for the record
   * @param flags Control flags
   * @param response A memory buffer for returning the response document.
   *        If an error occurred, an error response is stored here.
   *
   * @return Returns 0 on success
   */
  int reevaluateRecordWithInfo(String dataSourceCode, String recordID, int flags, StringBuffer response);

  
  /**
   * Reevaluate a resolved entity
   *
   * @param entityID The ID of the resolved entity to reevaluate
   * @param flags Control flags
   *
   * @return Returns 0 on success
   */
  int reevaluateEntity(long entityID, int flags);
  
  
  /**
   * Reevaluate a resolved entity and return a list of resolved entities
   *
   * @param entityID The ID of the resolved entity to reevaluate
   * @param flags Control flags
   * @param response A memory buffer for returning the response document.
   *        If an error occurred, an error response is stored here.
   *
   * @return Returns 0 on success
   */
  int reevaluateEntityWithInfo(long entityID, int flags, StringBuffer response);

  
  /**
   * This method searches for entities that contain attribute information
   * that are relevant to a set of input search attributes.
   *
   * @param jsonData A JSON document containing the attribute information
   *        to search for
   * @param response A memory buffer for returning the response document.
   *        If an error occurred, an error response is stored here.
   *
   * @return Returns 0 for success
   */
  int searchByAttributes(String jsonData, StringBuffer response);
  int searchByAttributesV2(String jsonData, int flags, StringBuffer response);


  /**
   * This method is used to retrieve information about a specific resolved
   * entity.  The information is returned as a JSON document.
   *
   * An entityID may be named ENTITY_ID, RESOLVED_ID, or RELATED_ID in
   * the JSON or CSV function output.
   *
   * @param entityID The resolved entity to retrieve information for
   * @param response A memory buffer for returning the response document.
   *        If an error occurred, an error response is stored here.
   *
   * @return Returns 0 for success. Returns -1 if the response status indicates
   *         failure or the G2 module is not initialized. Returns -2 if
   *         an exception was thrown and caught.
   */
  int getEntityByEntityID(long entityID, StringBuffer response);
  int getEntityByEntityIDV2(long entityID, int flags, StringBuffer response);


  /**
   * This method is used to retrieve information about the resolved entity
   * containing a particular observation record.
   *
   * @param dataSourceCode The data source of the observation to search for
   * @param recordID The record ID of the observation to search for
   * @param response A memory buffer for returning the response document.
   *        If an error occurred, an error response is stored here.
   *
   * @return Returns 0 for success
   */
  int getEntityByRecordID(String dataSourceCode, String recordID, StringBuffer response);
  int getEntityByRecordIDV2(String dataSourceCode, String recordID, int flags, StringBuffer response);

  
  /**
   * This method is used to find a relationship path between entities.
   */
  int findPathByEntityID(long entityID1, long entityID2, int maxDegree, StringBuffer response);
  int findPathByEntityIDV2(long entityID1, long entityID2, int maxDegree, int flags, StringBuffer response);
  int findPathByRecordID(String dataSourceCode1, String recordID1, String dataSourceCode2, String recordID2, int maxDegree, StringBuffer response);
  int findPathByRecordIDV2(String dataSourceCode1, String recordID1, String dataSourceCode2, String recordID2, int maxDegree, int flags, StringBuffer response);


  /**
   * This method is used to find a relationship path between entities, that excludes
   * a particular set of entities.
   */
  int findPathExcludingByEntityID(long entityID1, long entityID2, int maxDegree, String excludedEntities, int flags, StringBuffer response);
  int findPathExcludingByRecordID(String dataSourceCode1, String recordID1, String dataSourceCode2, String recordID2, int maxDegree, String excludedEntities, int flags, StringBuffer response);

  
  /**
   * This method is used to find a relationship path between entities, that excludes
   * a particular set of entities, and also requires at least one data source from a
   * set to be part of the path.
   */
  int findPathIncludingSourceByEntityID(long entityID1, long entityID2, int maxDegree, String excludedEntities, String requiredDsrcs, int flags, StringBuffer response);
  int findPathIncludingSourceByRecordID(String dataSourceCode1, String recordID1, String dataSourceCode2, String recordID2, int maxDegree, String excludedEntities, String requiredDsrcs, int flags, StringBuffer response);

  
  /**
   * This method is used to find a network of entity relationships, surrounding the paths between
   * a set of entities.
   */
  int findNetworkByEntityID(String entityList, int maxDegree, int buildOutDegree, int maxEntities, StringBuffer response);
  int findNetworkByEntityIDV2(String entityList, int maxDegree, int buildOutDegree, int maxEntities, int flags, StringBuffer response);
  int findNetworkByRecordID(String recordList, int maxDegree, int buildOutDegree, int maxEntities, StringBuffer response);
  int findNetworkByRecordIDV2(String recordList, int maxDegree, int buildOutDegree, int maxEntities, int flags, StringBuffer response);

  
  /**
   * This method determines why records are included in the resolved entity they belong to.
   */
  int whyEntityByRecordID(String dataSourceCode, String recordID, StringBuffer response);
  int whyEntityByRecordIDV2(String dataSourceCode, String recordID, int flags, StringBuffer response);
  int whyEntityByEntityID(long entityID, StringBuffer response);
  int whyEntityByEntityIDV2(long entityID, int flags, StringBuffer response);

  
  /**
   * This method determines how records are related to each other.
   */
  int whyRecords(String dataSourceCode1, String recordID1, String dataSourceCode2, String recordID2, StringBuffer response);
  int whyRecordsV2(String dataSourceCode1, String recordID1, String dataSourceCode2, String recordID2, int flags, StringBuffer response);
  
  
  /**
   * This method determines how entities are related to each other.
   */
  int whyEntities(long entityID1, long entityID2, StringBuffer response);
  int whyEntitiesV2(long entityID1, long entityID2, int flags, StringBuffer response);

  
  /**
   * This method is used to retrieve the stored record.
   *
   * @param dataSourceCode The data source of the observation to search for
   * @param recordID The record ID of the observation to search for
   * @param response A memory buffer for returning the response document.
   *        If an error occurred, an error response is stored here.
   *
   * @return Returns 0 for success
   */
  int getRecord(String dataSourceCode, String recordID, StringBuffer response);
  int getRecordV2(String dataSourceCode, String recordID, int flags, StringBuffer response);


  /**
   * This is used to export entity data from known entities.  This function
   * returns an export-handle that can be read from to get the export data 
   * in JSON format.  The export-handle should be read using the "G2_fetchNext"
   * function, and closed when work is complete. Each output row contains the 
   * exported entity data for a single resolved entity.
   *
   * @param flags A bit mask specifying control flags, such as 
   *        "G2_EXPORT_INCLUDE_SINGLETONS".  The default and recommended
   *        value is "G2_EXPORT_DEFAULT_FLAGS".
   *
   * @return Returns an export handle that the entity data can be read from.
   */
  long exportJSONEntityReport(int flags);
  int exportJSONEntityReportV3(int flags, Result<Long> exportHandle);


  /**
   * This is used to export entity data from known entities.  This function
   * returns an export-handle that can be read from to get the export data 
   * in CSV format.  The export-handle should be read using the "G2_fetchNext"
   * function, and closed when work is complete. The first output row returned 
   * by the export-handle contains the JSON column headers as a string.  Each 
   * following row contains the exported entity data.
   *
   * @param flags A bit mask specifying other control flags, such as 
   *        "G2_EXPORT_INCLUDE_SINGLETONS".  The default and recommended
   *        value is "G2_EXPORT_DEFAULT_FLAGS".
   *
   * @return Returns an export handle that the entity data can be read from.
   */
  long exportCSVEntityReportV2(String csvColumnList, int flags);
  int exportCSVEntityReportV3(String csvColumnList, int flags, Result<Long> exportHandle);

  
  /**
   * @brief
   * This function is used to read entity data from an export handle,
   * one data row at a time.
   *
   * @param exportHandle The export handle to retrieve data from
   *
   * @return Returns a pointer to the next block of the report data buffer.
   */
  String fetchNext(long exportHandle);
  int fetchNextV3(long exportHandle, StringBuffer response);

  
  /**
   * This function closes an export handle, to clean up system resources.
   */
  void closeExport(long exportHandle);
  int closeExportV3(long exportHandle);

  
  /**
   * @param jsonData A returned JSON document of the redo record processed
   *
   * @return Returns 0 on success, jsonData is empty if there was no record to process
   */
  int processRedoRecord(StringBuffer record);
  
  
  /**
   * @param jsonData A returned JSON document of the redo record processed
   * @param response A memory buffer for returning the response document.
   *        If an error occurred, an error response is stored here.
   *
   * @return Returns 0 on success, jsonData is empty if there was no record to process
   */
  int processRedoRecordWithInfo(int flags, StringBuffer record, StringBuffer response);

  
  /**
   * @param jsonData A returned JSON document of the redo record to send to the process() function
   *
   * @return Returns 0 on success, jsonData is empty if there is no record to process
   */
  int getRedoRecord(StringBuffer record);

  
  /**
   * @return Returns the number of redo records waiting to be processed
   */
  long countRedoRecords();


  /**
   * This method will send a record for processing in g2. It is a synchronous
   * call, i.e. it will wait until g2 actually processes the record.  This
   * is similar to {@link #process(String,StringBuffer)} method, but this
   * variant does <b>not</b> return a response.  <b>NOTE:</b> there are
   * performance benefits of calling this variant of process that does not
   * require a response.
   *
   * @param record An input record to be processed.
   * @return Returns 0 for success
   */
  int process(String record);
  
  
  /**
   * This method will send a record for processing in g2. It is a synchronous
   * call, i.e. it will wait until g2 actually processes the record.  This
   * is similar to {@link #process(String,StringBuffer)} method, but this
   * variant does <b>not</b> return a response.  <b>NOTE:</b> there are
   * performance benefits of calling this variant of process that does not
   * require a response. Also returns a list of modified resolved entities
   *
   * @param record An input record to be processed.
   * @param response A memory buffer for returning the response document.
   *        If an error occurred, an error response is stored here.
   * @return Returns 0 for success
   */
  int processWithInfo(String record, int flags, StringBuffer response);

  
  /**
   * This method will send a record for processing in g2. It is a synchronous
   * call, i.e. it will wait until g2 actually processes the record, and then
   * return a response message.  This is similar to {@link #process(String)}
   * method, but this variant returns a response.  <b>NOTE:</b> there are
   * performance benefits of calling the variant of process that does <b>not</b>
   * require a response.
   *
   * @param record An input record to be processed.
   * @param response If there is a response to the message it will be returned here.
   * @return Returns 0 for success
   */
  int process(String record, StringBuffer response);
}

