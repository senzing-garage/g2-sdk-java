/**********************************************************************************
Copyright Senzing, Inc. 2017-2021
The source code for this program is not published or otherwise divested
of its trade secrets, irrespective of what has been deposited with the U.S.
Copyright Office.
**********************************************************************************/

package com.senzing.g2.engine;

/**
 * Defines the Java interface to the G2 engine functions.  The G2 engine
 * functions primarily provide means of working with identity data records,
 * entities and their relationships.
 */
public interface G2Engine extends G2Fallible
{
  /**
   * The bitwise flag for export functionality to indicate that
   * we should include "resolved" relationships
   *
   */
  long G2_EXPORT_INCLUDE_RESOLVED = ( 1L << 0 );

  /**
   * The bitwise flag for export functionality to indicate that
   * we should include "possibly same" relationships
   *
   */
  long G2_EXPORT_INCLUDE_POSSIBLY_SAME = ( 1L << 1 );

  /**
   * The bitwise flag for export functionality to indicate that
   * we should include "possibly related" relationships
   *
   */
  long G2_EXPORT_INCLUDE_POSSIBLY_RELATED = ( 1L << 2 );

  /**
   * The bitwise flag for export functionality to indicate that
   * we should include "name only" relationships
   *
   */
  long G2_EXPORT_INCLUDE_NAME_ONLY = ( 1L << 3 );

  /**
   * The bitwise flag for export functionality to indicate that
   * we should include "disclosed" relationships
   *
   */
  long G2_EXPORT_INCLUDE_DISCLOSED = ( 1L << 4 );

  /**
   * The bitwise flag for export functionality to indicate that
   * we should include singleton entities
   *
   */
  long G2_EXPORT_INCLUDE_SINGLETONS = ( 1L << 5 );

  /**
   * The bitwise flag for export functionality to indicate that
   * we should include all entities
   *
   */
  long G2_EXPORT_INCLUDE_ALL_ENTITIES
      = (G2_EXPORT_INCLUDE_RESOLVED | G2_EXPORT_INCLUDE_SINGLETONS);

  /**
   * The bitwise flag for export functionality to indicate that
   * we should include all relationships
   *
   */
  long G2_EXPORT_INCLUDE_ALL_RELATIONSHIPS
      = (G2_EXPORT_INCLUDE_POSSIBLY_SAME | G2_EXPORT_INCLUDE_POSSIBLY_RELATED
         | G2_EXPORT_INCLUDE_NAME_ONLY | G2_EXPORT_INCLUDE_DISCLOSED);

  /**
   * The bitwise flag for including possibly-same relations for entities
   */
  long G2_ENTITY_INCLUDE_POSSIBLY_SAME_RELATIONS = ( 1L << 6 );

  /**
   * The bitwise flag for including possibly-related relations for entities
   */
  long G2_ENTITY_INCLUDE_POSSIBLY_RELATED_RELATIONS = ( 1L << 7 );

  /**
   * The bitwise flag for including name-only relations for entities
   */
  long G2_ENTITY_INCLUDE_NAME_ONLY_RELATIONS = ( 1L << 8 );

  /**
   * The bitwise flag for including disclosed relations for entities
   */
  long G2_ENTITY_INCLUDE_DISCLOSED_RELATIONS = ( 1L << 9 );

  /**
   * The bitwise flag for including all relations for entities
   */
  long G2_ENTITY_INCLUDE_ALL_RELATIONS
      = (G2_ENTITY_INCLUDE_POSSIBLY_SAME_RELATIONS
         | G2_ENTITY_INCLUDE_POSSIBLY_RELATED_RELATIONS
         | G2_ENTITY_INCLUDE_NAME_ONLY_RELATIONS
         | G2_ENTITY_INCLUDE_DISCLOSED_RELATIONS);

  /**
   * The bitwise flag for including all features for entities
   */
  long G2_ENTITY_INCLUDE_ALL_FEATURES = ( 1L << 10 );

  /**
   * The bitwise flag for including representative features for entities
   */
  long G2_ENTITY_INCLUDE_REPRESENTATIVE_FEATURES = ( 1L << 11 );

  /**
   * The bitwise flag for including the name of the entity
   */
  long G2_ENTITY_INCLUDE_ENTITY_NAME = ( 1L << 12 );

  /**
   * The bitwise flag for including the record summary of the entity
   */
  long G2_ENTITY_INCLUDE_RECORD_SUMMARY = ( 1L << 13 );

  /**
   * The bitwise flag for including the basic record data for the entity
   */
  long G2_ENTITY_INCLUDE_RECORD_DATA = ( 1L << 14 );

  /**
   * The bitwise flag for including the record matching info for the entity
   */
  long G2_ENTITY_INCLUDE_RECORD_MATCHING_INFO = ( 1L << 15 );

  /**
   * The bitwise flag for including the record json data for the entity
   */
  long G2_ENTITY_INCLUDE_RECORD_JSON_DATA = ( 1L << 16 );

  /**
   * The bitwise flag for including the record formatted data for the entity
   */
  long G2_ENTITY_INCLUDE_RECORD_FORMATTED_DATA = ( 1L << 17 );

  /**
   * The bitwise flag for the features identifiers for the records
   */
  long G2_ENTITY_INCLUDE_RECORD_FEATURE_IDS = ( 1L << 18 );

  /**
   * The bitwise flag for including the name of the related entities
   */
  long G2_ENTITY_INCLUDE_RELATED_ENTITY_NAME = ( 1L << 19 );

  /**
   * The bitwise flag for including the record matching info of the related
   * entities
   */
  long G2_ENTITY_INCLUDE_RELATED_MATCHING_INFO = ( 1L << 20 );

  /**
   * The bitwise flag for including the record summary of the related entities
   */
  long G2_ENTITY_INCLUDE_RELATED_RECORD_SUMMARY = ( 1L << 21 );

  /**
   * The bitwise flag for including the basic record data of the related
   * entities.
   */
  long G2_ENTITY_INCLUDE_RELATED_RECORD_DATA = ( 1L << 22 );

  /**
   * The bitwise flag for including internal features in entity output
   */
  long G2_ENTITY_OPTION_INCLUDE_INTERNAL_FEATURES = ( 1L << 23 );

  /**
   * The bitwise flag for including feature statistics in entity output.
   */
  long G2_ENTITY_OPTION_INCLUDE_FEATURE_STATS = ( 1L << 24 );

  /**
   * The bitwise flag for find-path functionality to indicate that
   * excluded entities are still allowed, but not preferred
   */
  long G2_FIND_PATH_PREFER_EXCLUDE = ( 1L << 25 );

  /**
   * The bitwise flag for including feature scores.
   */
  long G2_INCLUDE_FEATURE_SCORES = ( 1L << 26 );

  /**
   * The bitwise flag for including statistics from search results
   */
  long G2_SEARCH_INCLUDE_STATS = ( 1L << 27 );

  /**
   * The bitwise flag for including feature scores from search results.
   */
  long G2_SEARCH_INCLUDE_FEATURE_SCORES = G2_INCLUDE_FEATURE_SCORES;

  /**
   * The bitwise flag for search functionality to indicate that
   * we should include "resolved" match level results
   *
   */
  long G2_SEARCH_INCLUDE_RESOLVED = (G2_EXPORT_INCLUDE_RESOLVED);

  /**
   * The bitwise flag for search functionality to indicate that
   * we should include "possibly same" match level results
   */
  long G2_SEARCH_INCLUDE_POSSIBLY_SAME
      = (G2_EXPORT_INCLUDE_POSSIBLY_SAME);

  /**
   * The bitwise flag for search functionality to indicate that
   * we should include "possibly related" match level results
   *
   */
  long G2_SEARCH_INCLUDE_POSSIBLY_RELATED
      = (G2_EXPORT_INCLUDE_POSSIBLY_RELATED);

  /**
   * The bitwise flag for search functionality to indicate that
   * we should include "name only" match level results
   *
   */
  long G2_SEARCH_INCLUDE_NAME_ONLY = (G2_EXPORT_INCLUDE_NAME_ONLY);

  /**
   * The bitwise flag for search functionality to indicate that
   * we should include all match level results
   *
   */
  long G2_SEARCH_INCLUDE_ALL_ENTITIES
      = (G2_SEARCH_INCLUDE_RESOLVED | G2_SEARCH_INCLUDE_POSSIBLY_SAME
         | G2_SEARCH_INCLUDE_POSSIBLY_RELATED | G2_SEARCH_INCLUDE_NAME_ONLY);

  /**
   * The default recommended bitwise flag values for getting records
   */
  long G2_RECORD_DEFAULT_FLAGS = (G2_ENTITY_INCLUDE_RECORD_JSON_DATA);

  /**
   * The default recommended bitwise flag values for getting entities
   */
  long G2_ENTITY_DEFAULT_FLAGS
      = (G2_ENTITY_INCLUDE_ALL_RELATIONS
         | G2_ENTITY_INCLUDE_REPRESENTATIVE_FEATURES
         | G2_ENTITY_INCLUDE_ENTITY_NAME
         | G2_ENTITY_INCLUDE_RECORD_SUMMARY
         | G2_ENTITY_INCLUDE_RECORD_DATA
         | G2_ENTITY_INCLUDE_RECORD_MATCHING_INFO
         | G2_ENTITY_INCLUDE_RELATED_ENTITY_NAME
         | G2_ENTITY_INCLUDE_RELATED_RECORD_SUMMARY
         | G2_ENTITY_INCLUDE_RELATED_MATCHING_INFO);

  /**
   * The default recommended bitwise flag values for getting entities
   */
  long G2_ENTITY_BRIEF_DEFAULT_FLAGS
      = (G2_ENTITY_INCLUDE_RECORD_MATCHING_INFO
         | G2_ENTITY_INCLUDE_ALL_RELATIONS
         | G2_ENTITY_INCLUDE_RELATED_MATCHING_INFO);

  /**
   * The default recommended bitwise flag values for exporting entities
   */
  long G2_EXPORT_DEFAULT_FLAGS
      = (G2_EXPORT_INCLUDE_ALL_ENTITIES
         | G2_EXPORT_INCLUDE_ALL_RELATIONSHIPS
         | G2_ENTITY_INCLUDE_ALL_RELATIONS
         | G2_ENTITY_INCLUDE_REPRESENTATIVE_FEATURES
         | G2_ENTITY_INCLUDE_ENTITY_NAME
         | G2_ENTITY_INCLUDE_RECORD_DATA
         | G2_ENTITY_INCLUDE_RECORD_MATCHING_INFO
         | G2_ENTITY_INCLUDE_RELATED_MATCHING_INFO);

  /**
   * The default recommended bitwise flag values for finding entity paths
   */
  long G2_FIND_PATH_DEFAULT_FLAGS
      = (G2_ENTITY_INCLUDE_ALL_RELATIONS
         | G2_ENTITY_INCLUDE_ENTITY_NAME
         | G2_ENTITY_INCLUDE_RECORD_SUMMARY
         | G2_ENTITY_INCLUDE_RELATED_MATCHING_INFO);

  /**
   * The default recommended bitwise flag values for why-analysis on entities
   */
  long G2_WHY_ENTITY_DEFAULT_FLAGS
      = (G2_ENTITY_DEFAULT_FLAGS
         | G2_ENTITY_INCLUDE_RECORD_FEATURE_IDS
         | G2_ENTITY_OPTION_INCLUDE_INTERNAL_FEATURES
         | G2_ENTITY_OPTION_INCLUDE_FEATURE_STATS
         | G2_INCLUDE_FEATURE_SCORES);

  /**
   * The default recommended bitwise flag values for how-analysis on entities
   */
  long G2_HOW_ENTITY_DEFAULT_FLAGS
      = (G2_ENTITY_DEFAULT_FLAGS
         | G2_ENTITY_INCLUDE_RECORD_FEATURE_IDS
         | G2_ENTITY_OPTION_INCLUDE_INTERNAL_FEATURES
         | G2_ENTITY_OPTION_INCLUDE_FEATURE_STATS
         | G2_INCLUDE_FEATURE_SCORES);

  /**
   * The default recommended bitwise flag values for searching by attributes,
   * returning all matching entities
   */
  long G2_SEARCH_BY_ATTRIBUTES_ALL
      = (G2_SEARCH_INCLUDE_ALL_ENTITIES
         | G2_ENTITY_INCLUDE_REPRESENTATIVE_FEATURES
         | G2_ENTITY_INCLUDE_ENTITY_NAME
         | G2_ENTITY_INCLUDE_RECORD_SUMMARY
         | G2_SEARCH_INCLUDE_FEATURE_SCORES);

  /**
   * The default recommended bitwise flag values for searching by attributes,
   * returning only strongly matching entities
   */
  long G2_SEARCH_BY_ATTRIBUTES_STRONG
      = (G2_SEARCH_INCLUDE_RESOLVED
         | G2_SEARCH_INCLUDE_POSSIBLY_SAME
         | G2_ENTITY_INCLUDE_REPRESENTATIVE_FEATURES
         | G2_ENTITY_INCLUDE_ENTITY_NAME
         | G2_ENTITY_INCLUDE_RECORD_SUMMARY
         | G2_SEARCH_INCLUDE_FEATURE_SCORES);

  /**
   * The default recommended bitwise flag values for searching by attributes,
   * returning minimal data with all matches
   */
  long G2_SEARCH_BY_ATTRIBUTES_MINIMAL_ALL = (G2_SEARCH_INCLUDE_ALL_ENTITIES);

  /**
   * The default recommended bitwise flag values for searching by attributes,
   * returning the minimal data, and returning only the strongest matches
   */
  long G2_SEARCH_BY_ATTRIBUTES_MINIMAL_STRONG
      = (G2_SEARCH_INCLUDE_RESOLVED | G2_SEARCH_INCLUDE_POSSIBLY_SAME);

  /**
   * The default recommended bitwise flag values for searching by attributes
   */
  long G2_SEARCH_BY_ATTRIBUTES_DEFAULT_FLAGS = (G2_SEARCH_BY_ATTRIBUTES_ALL);

  /**
   * Initializes the G2 Engine API with the specified module name,
   * init parameters and flag indicating verbose logging.  If the
   * <code>G2CONFIGFILE</code> init parameter is absent then the default
   * configuration from the repository is used.
   *
   * @param moduleName A short name given to this instance of the engine object
   * @param iniParams A JSON string containing configuration parameters
   * @param verboseLogging Enable diagnostic logging which will print a massive
   *                       amount of information to stdout
   *
   * @return Zero (0) on success, non-zero on failure.
   */
  int init(String moduleName, String iniParams, boolean verboseLogging);

  /**
   * Initializes the G2 Engine object with the module name, initialization
   * parameters, verbose logging flag and a specific configuration ID
   * identifying the configuration to use.
   *
   * @param moduleName The module name with which to initialize.
   * @param iniParams The JSON initialization parameters.
   * @param initConfigID The specific configuration ID to initialize with.
   * @param verboseLogging Whether or not to initialize with verbose logging.
   *
   * @return Zero (0) on success, non-zero on failure.
   */
  int initWithConfigID(String   moduleName,
                       String   iniParams,
                       long     initConfigID,
                       boolean  verboseLogging);

  /**
   * Reinitializes with the specified configuration ID.
   *
   * @param initConfigID The configuration ID with which to reinitialize.
   *
   * @return Zero (0) on success, non-zero on failure.
   */
  int reinit(long initConfigID);

  /**
   * Uninitializes the G2 engine.
   *
   * @return Zero (0) on success, negative-one (-1) if the engine is not
   *         initialized.
   */
  int destroy();

  /**
   * May optionally be called to pre-initialize some of the heavier weight
   * internal resources of the G2 engine.
   *
   * @return Zero (0) on success and non-zero on failure.
   */
  int primeEngine();

  /**
   * Purges all data in the configured repository
   * WARNING: There is no undoing from this.  Make sure your repository is
   * regularly backed up.
   *
   * @return Zero (0) on success and non-zero on failure.
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
   * @param response The {@link StringBuffer} to retrieve the JSON
   *                 configuration document.
   *
   * @return Zero (0) on success and non-zero on failure.
   */
  int exportConfig(StringBuffer response);

  /**
   * Exports the JSON configuration that is currently loaded into the engine.
   *
   * @param response The {@link StringBuffer} to retrieve the JSON
   *                 configuration document.
   * @param configID The {@link Result} object to store the configuration ID.
   *
   * @return Zero (0) on success and non-zero on failure.
   */
  int exportConfig(StringBuffer response, Result<Long> configID);

  /**
   * Returns an identifier for the loaded G2 engine configuration
   *
   * @param configID The identifier value for the config
   *
   * @return Zero (0) on success and non-zero on failure.
   */
  int getActiveConfigID(Result<Long> configID);

  /**
   * Returns a long integer representing number of seconds since
   * January 1, 1970 12:00am GMT (epoch time).  This indicates the last
   * time the data repository was modified.
   *
   * @param lastModifiedTime The last modified time of the data repository
   *
   * @return Zero (0) on success and non-zero on failure.
   */
  int getRepositoryLastModifiedTime(Result<Long> lastModifiedTime);

  /**
   * Loads the JSON record with the specified data source code and record ID.
   * The specified JSON data may contain the <code>DATA_SOURCE</code> and
   * <code>RECORD_ID</code> elements, but, if so, they must match the specified
   * parameters.
   *
   * @param dataSourceCode The data source for the observation.
   * @param recordID The ID for the record
   * @param jsonData A JSON document containing the attribute information
   *        for the observation.
   * @param loadID The observation load ID for the record, can be null and will
   *               default to dataSourceCode
   *
   * @return Zero (0) on success and non-zero on failure.
   */
  int addRecord(String  dataSourceCode,
                String  recordID,
                String  jsonData,
                String  loadID);

  /**
   * Replace the JSON record that has already been loaded
   *
   * @param dataSourceCode The data source for the observation.
   * @param recordID The ID for the record
   * @param jsonData A JSON document containing the attribute information
   *        for the observation.
   * @param loadID The observation load ID for the record, can be null and will
   *               default to dataSourceCode.
   *
   * @return Zero (0) on success and non-zero on failure.
   */
  int replaceRecord(String  dataSourceCode,
                    String  recordID,
                    String  jsonData,
                    String  loadID);

  /**
   * Replace the JSON record that has already been loaded and returns
   * a list of modified resolved entities
   *
   * @param dataSourceCode The data source for the observation.
   * @param recordID The ID for the record
   * @param jsonData A JSON document containing the attribute information
   *        for the observation.
   * @param loadID The observation load ID for the record, can be null and will
   *               default to dataSourceCode
   * @param flags The flags to control how the operation is performed and
   *              specifically the content of the response JSON document.
   * @param response A {@link StringBuffer} for returning the response document.
   *                 If an error occurred, an error response is stored here.
   *
   * @return Zero (0) on success and non-zero on failure.
   */
  int replaceRecordWithInfo(String        dataSourceCode,
                            String        recordID,
                            String        jsonData,
                            String        loadID,
                            long          flags,
                            StringBuffer  response);

  /**
   * Loads the JSON record.  This works similarly to the G2_addRecord function,
   * except that instead or requiring the recordID as an input parameter, the
   * recordID is generated internally and returned through the parameter list.
   *
   * @param dataSourceCode The data source for the observation.
   * @param recordID A buffer that returns the ID for the record
   * @param jsonData A JSON document containing the attribute information
   *        for the observation.
   * @param loadID The observation load ID for the record, can be null and will
   *               default to dataSourceCode
   *
   * @return Zero (0) on success and non-zero on failure.
   */
  int addRecordWithReturnedRecordID(String        dataSourceCode,
                                    StringBuffer  recordID,
                                    String        jsonData,
                                    String        loadID);

  /**
   *
   * @param dataSourceCode The data source for the observation.
   * @param recordID A buffer that returns the ID for the record
   * @param jsonData A JSON document containing the attribute information
   *        for the observation.
   * @param loadID The observation load ID for the record, can be null and will
   *               default to dataSourceCode.
   * @param flags The flags to control how the operation is performed and
   *              specifically the content of the response JSON document.
   * @param response The {@link StringBuffer} to write the response JSON
   *                 document to.
   * @return Zero (0) on success and non-zero on failure.
   */
  int addRecordWithInfoWithReturnedRecordID(String        dataSourceCode,
                                            String        jsonData,
                                            String        loadID,
                                            long          flags,
                                            StringBuffer  recordID,
                                            StringBuffer  response);

  /**
   * This method is used to add entity data into the system.  This works
   * similarly to the G2_addRecord function, except that instead or requiring
   * the recordID as an input parameter, the recordID is generated internally
   * and returned through the parameter list. A list of modified resolved
   * entities is also returned
   *
   * @param dataSourceCode The data source for the observation.
   * @param recordID The ID for the record. If specified, then it will be used,
   *                 if empty string, then G2 will generate an ID
   * @param jsonData A JSON document containing the attribute information for
   *                 the observation.
   * @param loadID The observation load ID for the record, can be NULL and will
   *               default to dataSourceCode.
   * @param flags The flags to control how the operation is performed and
   *              specifically the content of the response JSON document.
   * @param response A {@link StringBuffer} for returning the response document.
   *                 If an error occurred, an error response is stored here.
   * @return Zero (0) on success and non-zero on failure.
   */
   int addRecordWithInfo(String       dataSourceCode,
                         String       recordID,
                         String       jsonData,
                         String       loadID,
                         long         flags,
                         StringBuffer response);

  /**
   * Delete the record that has already been loaded
   *
   * @param dataSourceCode The data source for the observation.
   * @param recordID The ID for the record
   * @param loadID The observation load ID for the record, can be null and will
   *               default to <code>dataSourceCode</code>.
   *
   * @return Zero (0) on success and non-zero on failure.
   */
  int deleteRecord(String dataSourceCode, String recordID, String loadID);


  /**
   * Delete the record that has already been loaded. Returns a list
   * of modified resolved entities
   *
   * @param dataSourceCode The data source for the observation.
   * @param recordID The ID for the record
   * @param loadID The observation load ID for the record, can be null and will
   *               default to <code>dataSourceCode</code>.
   * @param flags The flags to control how the operation is performed and
   *              specifically the content of the response JSON document.
   * @param response A {@link StringBuffer} for returning the response document.
   *                 If an error occurred, an error response is stored here.
   *
   * @return Zero (0) on success and non-zero on failure.
   */
  int deleteRecordWithInfo(String       dataSourceCode,
                           String       recordID,
                           String       loadID,
                           long         flags,
                           StringBuffer response);

  /**
   * Reevaluate a record that has already been loaded
   *
   * @param dataSourceCode The data source for the observation.
   * @param recordID The ID for the record
   * @param flags The flags to control how the operation is performed.
   *
   * @return Zero (0) on success and non-zero on failure.
   */
  int reevaluateRecord(String dataSourceCode, String recordID, long flags);


  /**
   * Reevaluate a record that has already been loaded. Returns a list
   * of resolved entities.
   *
   * @param dataSourceCode The data source for the observation.
   * @param recordID The ID for the record
   * @param flags The flags to control how the operation is performed and
   *              specifically the content of the response JSON document.
   * @param response A {@link StringBuffer} for returning the response document.
   *                 If an error occurred, an error response is stored here.
   *
   * @return Zero (0) on success and non-zero on failure.
   */
  int reevaluateRecordWithInfo(String       dataSourceCode,
                               String       recordID,
                               long         flags,
                               StringBuffer response);

  /**
   * Reevaluate a resolved entity identified by the specified entity ID.
   *
   * @param entityID The ID of the resolved entity to reevaluate
   * @param flags The flags to control how the operation is performed.
   *
   * @return Zero (0) on success and non-zero on failure.
   */
  int reevaluateEntity(long entityID, long flags);

  /**
   * Reevaluate a resolved entity and return a list of resolved entities
   *
   * @param entityID The ID of the resolved entity to reevaluate
   * @param flags The flags to control how the operation is performed and
   *              specifically the content of the response JSON document.
   * @param response A {@link StringBuffer} for returning the response document.
   *                 If an error occurred, an error response is stored here.
   *
   * @return Zero (0) on success and non-zero on failure.
   */
  int reevaluateEntityWithInfo(long         entityID,
                               long         flags,
                               StringBuffer response);

  /**
   * This method searches for entities that contain attribute information
   * that are relevant to a set of input search attributes.
   *
   * @param jsonData A JSON document containing the attribute information
   *        to search for
   * @param response A {@link StringBuffer} for returning the response document.
   *                 If an error occurred, an error response is stored here.
   *
   * @return Zero (0) on success and non-zero on failure.
   */
  int searchByAttributes(String jsonData, StringBuffer response);

  /**
   * This method searches for entities that contain attribute information
   * that are relevant to a set of input search attributes.
   *
   * @param jsonData A JSON document containing the attribute information
   *        to search for
   * @param flags The flags to control how the operation is performed and
   *              specifically the content of the response JSON document.
   * @param response A {@link StringBuffer} for returning the response document.
   *                 If an error occurred, an error response is stored here.
   *
   * @return Zero (0) on success and non-zero on failure.
   */
  int searchByAttributes(String jsonData, long flags, StringBuffer response);

  /**
   * This method searches for entities that contain attribute information
   * that are relevant to a set of input search attributes.
   *
   * @param jsonData A JSON document containing the attribute information
   *        to search for
   * @param searchProfile A search-profile identifier
   * @param flags The flags to control how the operation is performed and
   *              specifically the content of the response JSON document.
   * @param response A {@link StringBuffer} for returning the response document.
   *                 If an error occurred, an error response is stored here.
   *
   * @return Zero (0) on success and non-zero on failure.
   */
  int searchByAttributes(String jsonData, String searchProfile, long flags, StringBuffer response);

  /**
   * This method is used to retrieve information about a specific resolved
   * entity.  The information is returned as a JSON document.
   *
   * An entityID may be named ENTITY_ID, RESOLVED_ID, or RELATED_ID in
   * the JSON or CSV function output.
   *
   * @param entityID The resolved entity to retrieve information for
   * @param response A {@link StringBuffer} for returning the response document.
   *                 If an error occurred, an error response is stored here.
   *
   * @return Returns zero (0) for success. Returns negative-one (-1) if the
   *         response status indicates failure or the G2 module is not
   *         initialized. Returns negative-two (-2) if an exception was thrown
   *         and caught.
   */
  int getEntityByEntityID(long entityID, StringBuffer response);

  /**
   * This method is used to retrieve information about a specific resolved
   * entity.  The information is returned as a JSON document.
   *
   * An entityID may be named ENTITY_ID, RESOLVED_ID, or RELATED_ID in
   * the JSON or CSV function output.
   *
   * @param entityID The resolved entity to retrieve information for
   * @param flags The flags to control how the operation is performed and
   *              specifically the content of the response JSON document.
   * @param response A {@link StringBuffer} for returning the response document.
   *                 If an error occurred, an error response is stored here.
   *
   * @return Returns zero (0) for success. Returns negative-one (-1) if the
   *         response status indicates failure or the G2 module is not
   *         initialized. Returns negative-two (-2) if an exception was thrown
   *         and caught.
   */
  int getEntityByEntityID(long          entityID,
                          long          flags,
                          StringBuffer  response);


  /**
   * This method is used to retrieve information about the resolved entity
   * containing a particular observation record.
   *
   * @param dataSourceCode The data source of the observation to search for
   * @param recordID The record ID of the observation to search for
   * @param response A {@link StringBuffer} for returning the response document.
   *                 If an error occurred, an error response is stored here.
   *
   * @return Zero (0) on success and non-zero on failure.
   */
  int getEntityByRecordID(String        dataSourceCode,
                          String        recordID,
                          StringBuffer  response);

  /**
   * This method is used to retrieve information about the resolved entity
   * containing a particular observation record.
   *
   * @param dataSourceCode The data source of the observation to search for
   * @param recordID The record ID of the observation to search for
   * @param flags The flags to control how the operation is performed and
   *              specifically the content of the response JSON document.
   * @param response A {@link StringBuffer} for returning the response document.
   *                 If an error occurred, an error response is stored here.
   *
   * @return Zero (0) on success and non-zero on failure.
   */
  int getEntityByRecordID(String        dataSourceCode,
                          String        recordID,
                          long          flags,
                          StringBuffer  response);

  /**
   * This method is used to find interesting entities close to a specific resolved
   * entity.  The information is returned as a JSON document.
   *
   * @param entityID The resolved entity to search around
   * @param flags The flags to control how the operation is performed.
   * @param response A {@link StringBuffer} for returning the response document.
   *                 If an error occurred, an error response is stored here.
   *
   * @return Zero (0) on success and non-zero on failure.
   */
  int findInterestingEntitiesByEntityID(long          entityID,
                                        long          flags,
                                        StringBuffer  response);

  /**
   * This method is used to find interesting entities close to a specific resolved
   * entity containing a particular observation record.
   *
   * @param dataSourceCode The data source of the observation to search around
   * @param recordID The record ID of the observation to search around
   * @param flags The flags to control how the operation is performed.
   * @param response A {@link StringBuffer} for returning the response document.
   *                 If an error occurred, an error response is stored here.
   *
   * @return Zero (0) on success and non-zero on failure.
   */
  int findInterestingEntitiesByRecordID(String        dataSourceCode,
                                        String        recordID,
                                        long          flags,
                                        StringBuffer  response);

  /**
   * This method is used to find a relationship path between entities that
   * are identified by entity ID.
   *
   * @param entityID1 The entity ID of the first entity.
   * @param entityID2 The entity ID of the second entity.
   * @param maxDegrees The maximum number of degrees for the path search.
   * @param response The {@link StringBuffer} to write the JSON response
   *                 document to.
   * @return Zero (0) on success and non-zero on failure.
   */
  int findPathByEntityID(long         entityID1,
                         long         entityID2,
                         int          maxDegrees,
                         StringBuffer response);

  /**
   * This method is used to find a relationship path between entities that
   * are identified by entity ID.
   *
   * @param entityID1 The entity ID of the first entity.
   * @param entityID2 The entity ID of the second entity.
   * @param maxDegrees The maximum number of degrees for the path search.
   * @param flags The flags to control how the operation is performed and
   *              specifically the content of the response JSON document.
   * @param response The {@link StringBuffer} to write the JSON response
   *                 document to.
   * @return Zero (0) on success and non-zero on failure.
   */
  int findPathByEntityID(long         entityID1,
                         long         entityID2,
                         int          maxDegrees,
                         long         flags,
                         StringBuffer response);

  /**
   * This method is used to find a relationship path between entities that
   * are identified by the data source code and record ID of records in each
   * of the entities.
   *
   * @param dataSourceCode1 The data source code of the first record.
   * @param recordID1 The record ID of the first record.
   * @param dataSourceCode2 The data source code of the second record.
   * @param recordID2 The record ID of the second record.
   * @param maxDegrees The maximum number of degrees for the path search.
   * @param response The {@link StringBuffer} to write the JSON response
   *                 document to.
   * @return Zero (0) on success and non-zero on failure.
   */
  int findPathByRecordID(String       dataSourceCode1,
                         String       recordID1,
                         String       dataSourceCode2,
                         String       recordID2,
                         int          maxDegrees,
                         StringBuffer response);

  /**
   * This method is used to find a relationship path between entities that
   * are identified by the data source code and record ID of records in each
   * of the entities.
   *
   * @param dataSourceCode1 The data source code of the first record.
   * @param recordID1 The record ID of the first record.
   * @param dataSourceCode2 The data source code of the second record.
   * @param recordID2 The record ID of the second record.
   * @param maxDegrees The maximum number of degrees for the path search.
   * @param flags The flags to control how the operation is performed and
   *              specifically the content of the response JSON document.
   * @param response The {@link StringBuffer} to write the JSON response
   *                 document to.
   * @return Zero (0) on success and non-zero on failure.
   */
  int findPathByRecordID(String       dataSourceCode1,
                         String       recordID1,
                         String       dataSourceCode2,
                         String       recordID2,
                         int          maxDegrees,
                         long         flags,
                         StringBuffer response);


  /**
   * <p>
   * This method is used to find a relationship path between two entities
   * identified by their entity ID's that excludes one or more entities, also
   * identified by their entity ID's.
   *
   * <p>
   * The excluded entities are identified by their entity ID's in a JSON
   * document with the following format:
   * <pre>
   *   {
   *     "ENTITIES": [
   *        { "ENTITY_ID": &lt;entity_id1&gt; },
   *        { "ENTITY_ID": &lt;entity_id2&gt; },
   *        . . .
   *        { "ENTITY_ID": &lt;entity_idN&gt; }
   *     ]
   *   }
   * </pre>
   *
   * @param entityID1 The entity ID of the first entity.
   * @param entityID2 The entity ID of the second entity.
   * @param maxDegrees The maximum number of degrees for the path search.
   * @param excludedEntities The JSON document identifying the excluded entities
   *                         via their entity ID's.
   * @param response The {@link StringBuffer} to write the JSON response
   *                 document to.
   * @return Zero (0) on success and non-zero on failure.
   */
  int findPathExcludingByEntityID(long          entityID1,
                                  long          entityID2,
                                  int           maxDegrees,
                                  String        excludedEntities,
                                  StringBuffer  response);

  /**
   * <p>
   * This method is used to find a relationship path between two entities
   * identified by their entity ID's that excludes one or more entities, also
   * identified by their entity ID's.
   *
   * <p>
   * The excluded entities are identified by their entity ID's in a JSON
   * document with the following format:
   * <pre>
   *   {
   *     "ENTITIES": [
   *        { "ENTITY_ID": &lt;entity_id1&gt; },
   *        { "ENTITY_ID": &lt;entity_id2&gt; },
   *        . . .
   *        { "ENTITY_ID": &lt;entity_idN&gt; }
   *     ]
   *   }
   * </pre>
   *
   * @param entityID1 The entity ID of the first entity.
   * @param entityID2 The entity ID of the second entity.
   * @param maxDegrees The maximum number of degrees for the path search.
   * @param excludedEntities The JSON document identifying the excluded entities
   *                         via their entity ID's.
   * @param flags The flags to control how the operation is performed and
   *              specifically the content of the response JSON document.
   * @param response The {@link StringBuffer} to write the JSON response
   *                 document to.
   * @return Zero (0) on success and non-zero on failure.
   */
  int findPathExcludingByEntityID(long          entityID1,
                                  long          entityID2,
                                  int           maxDegrees,
                                  String        excludedEntities,
                                  long          flags,
                                  StringBuffer  response);

  /**
   * <p>
   * This method is used to find a relationship path between two entities
   * identified by the data source codes and record IDs of their composite
   * records where that path excludes one or more entities, also
   * identified by the data source codes and record IDs of their composite
   * records.
   *
   * <p>
   * The excluded entities are identified by the data source codes and record
   * ID's of their composite records in a JSON document with the following
   * format:
   * <pre>
   *   {
   *     "ENTITIES": [
   *        {
   *          "DATA_SOURCE": "&lt;data_source1&gt;",
   *          "RECORD_ID":  "&lt;record_id1&gt;"
   *        },
   *        {
   *          "DATA_SOURCE": "&lt;data_source2&gt;",
   *          "RECORD_ID":  "&lt;record_id2&gt;"
   *        },
   *        . . .
   *        {
   *          "DATA_SOURCE": "&lt;data_sourceN&gt;",
   *          "RECORD_ID":  "&lt;record_idN&gt;"
   *        }
   *     ]
   *   }
   * </pre>
   *
   * @param dataSourceCode1 The data source code of the first record.
   * @param recordID1 The record ID of the first record.
   * @param dataSourceCode2 The data source code of the second record.
   * @param recordID2 The record ID of the second record.
   * @param maxDegrees The maximum number of degrees for the path search.
   * @param excludedEntities The JSON document identifying the excluded entities
   *                         via their entity ID's.
   * @param response The {@link StringBuffer} to write the JSON response
   *                 document to.
   * @return Zero (0) on success and non-zero on failure.
   */
  int findPathExcludingByRecordID(String        dataSourceCode1,
                                  String        recordID1,
                                  String        dataSourceCode2,
                                  String        recordID2,
                                  int           maxDegrees,
                                  String        excludedEntities,
                                  StringBuffer  response);

  /**
   * <p>
   * This method is used to find a relationship path between two entities
   * identified by the data source codes and record IDs of their composite
   * records where that path excludes one or more entities, also
   * identified by the data source codes and record IDs of their composite
   * records.
   *
   * <p>
   * The excluded entities are identified by the data source codes and record
   * ID's of their composite records in a JSON document with the following
   * format:
   * <pre>
   *   {
   *     "ENTITIES": [
   *        {
   *          "DATA_SOURCE": "&lt;data_source1&gt;",
   *          "RECORD_ID":  "&lt;record_id1&gt;"
   *        },
   *        {
   *          "DATA_SOURCE": "&lt;data_source2&gt;",
   *          "RECORD_ID":  "&lt;record_id2&gt;"
   *        },
   *        . . .
   *        {
   *          "DATA_SOURCE": "&lt;data_sourceN&gt;",
   *          "RECORD_ID":  "&lt;record_idN&gt;"
   *        }
   *     ]
   *   }
   * </pre>
   *
   * @param dataSourceCode1 The data source code of the first record.
   * @param recordID1 The record ID of the first record.
   * @param dataSourceCode2 The data source code of the second record.
   * @param recordID2 The record ID of the second record.
   * @param maxDegrees The maximum number of degrees for the path search.
   * @param excludedEntities The JSON document identifying the excluded entities
   *                         via their entity ID's.
   * @param flags The flags to control how the operation is performed and
   *              specifically the content of the response JSON document.
   * @param response The {@link StringBuffer} to write the JSON response
   *                 document to.
   * @return Zero (0) on success and non-zero on failure.
   */
  int findPathExcludingByRecordID(String        dataSourceCode1,
                                  String        recordID1,
                                  String        dataSourceCode2,
                                  String        recordID2,
                                  int           maxDegrees,
                                  String        excludedEntities,
                                  long          flags,
                                  StringBuffer  response);

  /**
   * <p>
   * This method is used to find a relationship path between two entities
   * identified by their entity ID's.  The path will exclude the one or more
   * entities, also identified by the specified entity ID's and will require
   * that the path contains <b>at least one</b> of the data sources identified
   * by the one or more specified data sources codes.
   *
   * <p>
   * The excluded entities are identified by their entity ID's in a JSON
   * document with the following format:
   * <pre>
   *   {
   *     "ENTITIES": [
   *        { "ENTITY_ID": &lt;entity_id1&gt; },
   *        { "ENTITY_ID": &lt;entity_id2&gt; },
   *        . . .
   *        { "ENTITY_ID": &lt;entity_idN&gt; }
   *     ]
   *   }
   * </pre>
   *
   * <p>
   * The required set of data sources are identified by their data source codes
   * in a JSON document with the following format:
   * <pre>
   *    { "DATA_SOURCES": [
   *        "&lt;data_source_code1&gt;",
   *        "&lt;data_source_code2&gt;",
   *        . . .
   *        "&lt;data_source_codeN&gt;"
   *      ]
   *    }
   * </pre>
   *
   * @param entityID1 The entity ID of the first entity.
   * @param entityID2 The entity ID of the second entity.
   * @param maxDegrees The maximum number of degrees for the path search.
   * @param excludedEntities The JSON document identifying the excluded entities
   *                         via their entity ID's.
   * @param requiredSources The JSON document identifying the data sources that
   *                        must be included on the path.
   * @param response The {@link StringBuffer} to write the JSON response
   *                 document to.
   * @return Zero (0) on success and non-zero on failure.
   */
  int findPathIncludingSourceByEntityID(long          entityID1,
                                        long          entityID2,
                                        int           maxDegrees,
                                        String        excludedEntities,
                                        String        requiredSources,
                                        StringBuffer  response);

  /**
   * <p>
   * This method is used to find a relationship path between two entities
   * identified by their entity ID's.  The path will exclude the one or more
   * entities, also identified by the specified entity ID's and will require
   * that the path contains <b>at least one</b> of the data sources identified
   * by the one or more specified data sources codes.
   *
   * <p>
   * The excluded entities are identified by their entity ID's in a JSON
   * document with the following format:
   * <pre>
   *   {
   *     "ENTITIES": [
   *        { "ENTITY_ID": &lt;entity_id1&gt; },
   *        { "ENTITY_ID": &lt;entity_id2&gt; },
   *        . . .
   *        { "ENTITY_ID": &lt;entity_idN&gt; }
   *     ]
   *   }
   * </pre>
   *
   * <p>
   * The required set of data sources are identified by their data source codes
   * in a JSON document with the following format:
   * <pre>
   *    { "DATA_SOURCES": [
   *        "&lt;data_source_code1&gt;",
   *        "&lt;data_source_code2&gt;",
   *        . . .
   *        "&lt;data_source_codeN&gt;"
   *      ]
   *    }
   * </pre>
   *
   * @param entityID1 The entity ID of the first entity.
   * @param entityID2 The entity ID of the second entity.
   * @param maxDegrees The maximum number of degrees for the path search.
   * @param excludedEntities The JSON document identifying the excluded entities
   *                         via their entity ID's.
   * @param requiredSources The JSON document identifying the data sources that
   *                        must be included on the path.
   * @param flags The flags to control how the operation is performed and
   *              specifically the content of the response JSON document.
   * @param response The {@link StringBuffer} to write the JSON response
   *                 document to.
   * @return Zero (0) on success and non-zero on failure.
   */
  int findPathIncludingSourceByEntityID(long          entityID1,
                                        long          entityID2,
                                        int           maxDegrees,
                                        String        excludedEntities,
                                        String        requiredSources,
                                        long          flags,
                                        StringBuffer  response);

  /**
   * <p>
   * This method is used to find a relationship path between two entities
   * identified by the data source codes and record IDs of their composite
   * records.  THe path will exclude the one or more entities also identified
   * by the specified data source code and record ID pairs that identify the
   * composite records of the excluded entities and further will require the
   * path contains <b>at least one</b> of the data sources identified by the
   * one or more specified data sources codes.
   *
   * <p>
   * The excluded entities are identified by the data source codes and record
   * ID's of their composite records in a JSON document with the following
   * format:
   * <pre>
   *   {
   *     "ENTITIES": [
   *        {
   *          "DATA_SOURCE": "&lt;data_source1&gt;",
   *          "RECORD_ID":  "&lt;record_id1&gt;"
   *        },
   *        {
   *          "DATA_SOURCE": "&lt;data_source2&gt;",
   *          "RECORD_ID":  "&lt;record_id2&gt;"
   *        },
   *        . . .
   *        {
   *          "DATA_SOURCE": "&lt;data_sourceN&gt;",
   *          "RECORD_ID":  "&lt;record_idN&gt;"
   *        }
   *     ]
   *   }
   * </pre>
   *
   * <p>
   * The required set of data sources are identified by their data source codes
   * in a JSON document with the following format:
   * <pre>
   *    { "DATA_SOURCES": [
   *        "&lt;data_source_code1&gt;",
   *        "&lt;data_source_code2&gt;",
   *        . . .
   *        "&lt;data_source_codeN&gt;"
   *      ]
   *    }
   * </pre>
   *
   * @param dataSourceCode1 The data source code of the first record.
   * @param recordID1 The record ID of the first record.
   * @param dataSourceCode2 The data source code of the second record.
   * @param recordID2 The record ID of the second record.
   * @param maxDegrees The maximum number of degrees for the path search.
   * @param excludedEntities The JSON document identifying the excluded entities
   *                         via their entity ID's.
   * @param requiredSources The JSON document identifying the data sources that
   *                        must be included on the path.
   * @param response The {@link StringBuffer} to write the JSON response
   *                 document to.
   * @return Zero (0) on success and non-zero on failure.
   */
  int findPathIncludingSourceByRecordID(String        dataSourceCode1,
                                        String        recordID1,
                                        String        dataSourceCode2,
                                        String        recordID2,
                                        int           maxDegrees,
                                        String        excludedEntities,
                                        String        requiredSources,
                                        StringBuffer  response);

  /**
   * <p>
   * This method is used to find a relationship path between two entities
   * identified by the data source codes and record IDs of their composite
   * records.  THe path will exclude the one or more entities also identified
   * by the specified data source code and record ID pairs that identify the
   * composite records of the excluded entities and further will require the
   * path contains <b>at least one</b> of the data sources identified by the
   * one or more specified data sources codes.
   *
   * <p>
   * The excluded entities are identified by the data source codes and record
   * ID's of their composite records in a JSON document with the following
   * format:
   * <pre>
   *   {
   *     "ENTITIES": [
   *        {
   *          "DATA_SOURCE": "&lt;data_source1&gt;",
   *          "RECORD_ID":  "&lt;record_id1&gt;"
   *        },
   *        {
   *          "DATA_SOURCE": "&lt;data_source2&gt;",
   *          "RECORD_ID":  "&lt;record_id2&gt;"
   *        },
   *        . . .
   *        {
   *          "DATA_SOURCE": "&lt;data_sourceN&gt;",
   *          "RECORD_ID":  "&lt;record_idN&gt;"
   *        }
   *     ]
   *   }
   * </pre>
   *
   * <p>
   * The required set of data sources are identified by their data source codes
   * in a JSON document with the following format:
   * <pre>
   *    { "DATA_SOURCES": [
   *        "&lt;data_source_code1&gt;",
   *        "&lt;data_source_code2&gt;",
   *        . . .
   *        "&lt;data_source_codeN&gt;"
   *      ]
   *    }
   * </pre>
   *
   * @param dataSourceCode1 The data source code of the first record.
   * @param recordID1 The record ID of the first record.
   * @param dataSourceCode2 The data source code of the second record.
   * @param recordID2 The record ID of the second record.
   * @param maxDegrees The maximum number of degrees for the path search.
   * @param excludedEntities The JSON document identifying the excluded entities
   *                         via their entity ID's.
   * @param requiredSources The JSON document identifying the data sources that
   *                        must be included on the path.
   * @param flags The flags to control how the operation is performed and
   *              specifically the content of the response JSON document.
   * @param response The {@link StringBuffer} to write the JSON response
   *                 document to.
   * @return Zero (0) on success and non-zero on failure.
   */
  int findPathIncludingSourceByRecordID(String        dataSourceCode1,
                                        String        recordID1,
                                        String        dataSourceCode2,
                                        String        recordID2,
                                        int           maxDegrees,
                                        String        excludedEntities,
                                        String        requiredSources,
                                        long          flags,
                                        StringBuffer  response);

  /**
   * <p>
   * This method is used to find a network of entity relationships,
   * surrounding the paths between a set of entities.  The entities are
   * identified by their entity IDs.
   *
   * <p>
   * The desired entities are identified by their entity ID's in a JSON
   * document with the following format:
   * <pre>
   *   {
   *     "ENTITIES": [
   *        { "ENTITY_ID": &lt;entity_id1&gt; },
   *        { "ENTITY_ID": &lt;entity_id2&gt; },
   *        . . .
   *        { "ENTITY_ID": &lt;entity_idN&gt; }
   *     ]
   *   }
   * </pre>
   *
   * @param entityList The JSON document specifying the entity ID's of the
   *                   desired entities.
   * @param maxDegrees The maximum number of degrees for the path search
   *                   between the specified entities.
   * @param buildOutDegrees The number of relationship degrees to build out
   *                        from each of the found entities.
   * @param maxEntities The maximum number of entities to build out to.
   * @param response The {@link StringBuffer} to write the JSON response
   *                 document to.
   * @return Zero (0) on success and non-zero on failure.
   */
  int findNetworkByEntityID(String        entityList,
                            int           maxDegrees,
                            int           buildOutDegrees,
                            int           maxEntities,
                            StringBuffer  response);

  /**
   * <p>
   * This method is used to find a network of entity relationships,
   * surrounding the paths between a set of entities.  The entities are
   * identified by their entity IDs.
   *
   * <p>
   * The desired entities are identified by their entity ID's in a JSON
   * document with the following format:
   * <pre>
   *   {
   *     "ENTITIES": [
   *        { "ENTITY_ID": &lt;entity_id1&gt; },
   *        { "ENTITY_ID": &lt;entity_id2&gt; },
   *        . . .
   *        { "ENTITY_ID": &lt;entity_idN&gt; }
   *     ]
   *   }
   * </pre>
   *
   * @param entityList The JSON document specifying the entity ID's of the
   *                   desired entities.
   * @param maxDegrees The maximum number of degrees for the path search
   *                   between the specified entities.
   * @param buildOutDegrees The number of relationship degrees to build out
   *                        from each of the found entities.
   * @param maxEntities The maximum number of entities to build out to.
   * @param flags The flags to control how the operation is performed and
   *              specifically the content of the response JSON document.
   * @param response The {@link StringBuffer} to write the JSON response
   *                 document to.
   * @return Zero (0) on success and non-zero on failure.
   */
  int findNetworkByEntityID(String        entityList,
                            int           maxDegrees,
                            int           buildOutDegrees,
                            int           maxEntities,
                            long          flags,
                            StringBuffer  response);

  /**
   * <p>
   * This method is used to find a network of entity relationships,
   * surrounding the paths between a set of entities.  The entities are
   * identified by their composite records having the specified data source
   * code and record ID pairs.
   *
   * <p>
   * The composite records af the desired entities are identified by the
   * data source code and record ID pairs in a JSON document with the following
   * format:
   * <pre>
   *   {
   *     "ENTITIES": [
   *        {
   *          "DATA_SOURCE": "&lt;data_source1&gt;",
   *          "RECORD_ID":  "&lt;record_id1&gt;"
   *        },
   *        {
   *          "DATA_SOURCE": "&lt;data_source2&gt;",
   *          "RECORD_ID":  "&lt;record_id2&gt;"
   *        },
   *        . . .
   *        {
   *          "DATA_SOURCE": "&lt;data_sourceN&gt;",
   *          "RECORD_ID":  "&lt;record_idN&gt;"
   *        }
   *     ]
   *   }
   * </pre>
   *
   * @param recordList The JSON document containing the data source code and
   *                   record ID pairs for the composite records of the desired
   *                   entities.
   * @param maxDegrees The maximum number of degrees for the path search
   *                   between the specified entities.
   * @param buildOutDegrees The number of relationship degrees to build out
   *                        from each of the found entities.
   * @param maxEntities The maximum number of entities to build out to.
   * @param response The {@link StringBuffer} to write the JSON response
   *                 document to.
   * @return Zero (0) on success and non-zero on failure.
   */
  int findNetworkByRecordID(String        recordList,
                            int           maxDegrees,
                            int           buildOutDegrees,
                            int           maxEntities,
                            StringBuffer  response);

  /**
   * <p>
   * This method is used to find a network of entity relationships,
   * surrounding the paths between a set of entities.  The entities are
   * identified by their composite records having the specified data source
   * code and record ID pairs.
   *
   * <p>
   * The composite records af the desired entities are identified by the
   * data source code and record ID pairs in a JSON document with the following
   * format:
   * <pre>
   *   {
   *     "ENTITIES": [
   *        {
   *          "DATA_SOURCE": "&lt;data_source1&gt;",
   *          "RECORD_ID":  "&lt;record_id1&gt;"
   *        },
   *        {
   *          "DATA_SOURCE": "&lt;data_source2&gt;",
   *          "RECORD_ID":  "&lt;record_id2&gt;"
   *        },
   *        . . .
   *        {
   *          "DATA_SOURCE": "&lt;data_sourceN&gt;",
   *          "RECORD_ID":  "&lt;record_idN&gt;"
   *        }
   *     ]
   *   }
   * </pre>
   *
   * @param recordList The JSON document containing the data source code and
   *                   record ID pairs for the composite records of the desired
   *                   entities.
   * @param maxDegrees The maximum number of degrees for the path search
   *                   between the specified entities.
   * @param buildOutDegrees The number of relationship degrees to build out
   *                        from each of the found entities.
   * @param maxEntities The maximum number of entities to build out to.
   * @param flags The flags to control how the operation is performed and
   *              specifically the content of the response JSON document.
   * @param response The {@link StringBuffer} to write the JSON response
   *                 document to.
   * @return Zero (0) on success and non-zero on failure.
   */
  int findNetworkByRecordID(String        recordList,
                            int           maxDegrees,
                            int           buildOutDegrees,
                            int           maxEntities,
                            long          flags,
                            StringBuffer  response);

  /**
   * This method determines why a particular record is included in its resolved
   * entity.
   *
   * @param dataSourceCode The data source code for the composite record of the
   *                       subject entity.
   * @param recordID The record ID for the composite record of the subject
   *                 entity.
   * @param response The {@link StringBuffer} to write the JSON response
   *                 document to.
   * @return Zero (0) on success and non-zero on failure.
   */
  int whyRecordInEntity(String        dataSourceCode,
                        String        recordID,
                        StringBuffer  response);

  /**
   * This method determines why a particular record is included in its resolved
   * entity.
   *
   * @param dataSourceCode The data source code for the composite record of the
   *                       subject entity.
   * @param recordID The record ID for the composite record of the subject
   *                 entity.
   * @param flags The flags to control how the operation is performed and
   *              specifically the content of the response JSON document.
   * @param response The {@link StringBuffer} to write the JSON response
   *                 document to.
   * @return Zero (0) on success and non-zero on failure.
   */
  int whyRecordInEntity(String        dataSourceCode,
                        String        recordID,
                        long          flags,
                        StringBuffer  response);

  /**
   * This method determines why records are included in the resolved entity
   * they belong to.  The entity for the operation is the one having the
   * record with the specified data source code and record ID.
   *
   * @param dataSourceCode The data source code for the composite record of the
   *                       subject entity.
   * @param recordID The record ID for the composite record of the subject
   *                 entity.
   * @param response The {@link StringBuffer} to write the JSON response
   *                 document to.
   * @return Zero (0) on success and non-zero on failure.
   */
  int whyEntityByRecordID(String        dataSourceCode,
                          String        recordID,
                          StringBuffer  response);

  /**
   * This method determines why records are included in the resolved entity
   * they belong to.  The entity for the operation is the one having the
   * record with the specified data source code and record ID.
   *
   * @param dataSourceCode The data source code for the composite record of the
   *                       subject entity.
   * @param recordID The record ID for the composite record of the subject
   *                 entity.
   * @param flags The flags to control how the operation is performed and
   *              specifically the content of the response JSON document.
   * @param response The {@link StringBuffer} to write the JSON response
   *                 document to.
   * @return Zero (0) on success and non-zero on failure.
   */
  int whyEntityByRecordID(String        dataSourceCode,
                          String        recordID,
                          long          flags,
                          StringBuffer  response);

  /**
   * This method determines why records are included in the resolved entity
   * they belong to.  The entity is identified with the specified entity ID.
   *
   * @param entityID The entity ID of the subject entity.
   * @param response The {@link StringBuffer} to write the JSON response
   *                 document to.
   * @return Zero (0) on success and non-zero on failure.
   */
  int whyEntityByEntityID(long entityID, StringBuffer response);

  /**
   * This method determines why records are included in the resolved entity
   * they belong to.  The entity is identified with the specified entity ID.
   *
   * @param entityID The entity ID of the subject entity.
   * @param flags The flags to control how the operation is performed and
   *              specifically the content of the response JSON document.
   * @param response The {@link StringBuffer} to write the JSON response
   *                 document to.
   * @return Zero (0) on success and non-zero on failure.
   */
  int whyEntityByEntityID(long          entityID,
                          long          flags,
                          StringBuffer  response);

  /**
   * This method determines how two records are related to each other.
   *
   * @param dataSourceCode1 The data source code for the first record.
   * @param recordID1 The record ID for the first record.
   * @param dataSourceCode2 The data source code for the second record.
   * @param recordID2 The record ID for the second record.
   * @param response The {@link StringBuffer} to write the JSON response
   *                 document to.
   * @return Zero (0) on success and non-zero on failure.
   */
  int whyRecords(String       dataSourceCode1,
                 String       recordID1,
                 String       dataSourceCode2,
                 String       recordID2,
                 StringBuffer response);

  /**
   * This method determines how two records are related to each other.
   *
   * @param dataSourceCode1 The data source code for the first record.
   * @param recordID1 The record ID for the first record.
   * @param dataSourceCode2 The data source code for the second record.
   * @param recordID2 The record ID for the second record.
   * @param flags The flags to control how the operation is performed and
   *              specifically the content of the response JSON document.
   * @param response The {@link StringBuffer} to write the JSON response
   *                 document to.
   * @return Zero (0) on success and non-zero on failure.
   */
  int whyRecords(String       dataSourceCode1,
                 String       recordID1,
                 String       dataSourceCode2,
                 String       recordID2,
                 long         flags,
                 StringBuffer response);


  /**
   * This method determines how two entities are related to each other.
   *
   * @param entityID1 The entity ID of the first entity.
   * @param entityID2 The entity ID of the second entity.
   * @param response The {@link StringBuffer} to write the JSON response
   *                 document to.
   * @return Zero (0) on success and non-zero on failure.
   */
  int whyEntities(long entityID1, long entityID2, StringBuffer response);

  /**
   * This method determines how two entities are related to each other.
   *
   * @param entityID1 The entity ID of the first entity.
   * @param entityID2 The entity ID of the second entity.
   * @param flags The flags to control how the operation is performed and
   *              specifically the content of the response JSON document.
   * @param response The {@link StringBuffer} to write the JSON response
   *                 document to.
   * @return Zero (0) on success and non-zero on failure.
   */
  int whyEntities(long          entityID1,
                  long          entityID2,
                  long          flags,
                  StringBuffer  response);

  
  /**
   * This method gives information on how entities were constructed from
   * their base records.
   *
   * @param entityID The entity ID.
   * @param response The {@link StringBuffer} to write the JSON response
   *                 document to.
   * @return Zero (0) on success and non-zero on failure.
   */
  int howEntityByEntityID(long          entityID,
                          StringBuffer  response);
  
  /**
   * This method gives information on how entities were constructed from
   * their base records.
   *
   * @param entityID The entity ID.
   * @param flags The flags to control how the operation is performed and
   *              specifically the content of the response JSON document.
   * @param response The {@link StringBuffer} to write the JSON response
   *                 document to.
   * @return Zero (0) on success and non-zero on failure.
   */
  int howEntityByEntityID(long          entityID,
                          long          flags,
                          StringBuffer  response);


  /**
   * This method gives information on how an entity composed of a given set
   * of records would look.
   *
   * @param recordList The list of records used to build the virtual entity.
   * @param response The {@link StringBuffer} to write the JSON response
   *                 document to.
   * @return Zero (0) on success and non-zero on failure.
   */
  int getVirtualEntityByRecordID(String        recordList,
                                 StringBuffer  response);

  /**
   * This method gives information on how an entity composed of a given set
   * of records would look.
   *
   * @param recordList The list of records used to build the virtual entity.
   * @param flags The flags to control how the operation is performed and
   *              specifically the content of the response JSON document.
   * @param response The {@link StringBuffer} to write the JSON response
   *                 document to.
   * @return Zero (0) on success and non-zero on failure.
   */
  int getVirtualEntityByRecordID(String        recordList,
                                 long          flags,
                                 StringBuffer  response);

  
  /**
   * This method is used to retrieve the stored record.
   *
   * @param dataSourceCode The data source of the observation to search for
   * @param recordID The record ID of the observation to search for
   * @param response A {@link StringBuffer} for returning the response document.
   *                 If an error occurred, an error response is stored here.
   *
   * @return Zero (0) on success and non-zero on failure.
   */
  int getRecord(String dataSourceCode, String recordID, StringBuffer response);

  /**
   * This method is used to retrieve the stored record.
   *
   * @param dataSourceCode The data source of the observation to search for
   * @param recordID The record ID of the observation to search for
   * @param flags The flags to control how the operation is performed and
   *              specifically the content of the response JSON document.
   * @param response A {@link StringBuffer} for returning the response document.
   *                 If an error occurred, an error response is stored here.
   *
   * @return Zero (0) on success and non-zero on failure.
   */
  int getRecord(String        dataSourceCode,
                String        recordID,
                long          flags,
                StringBuffer  response);

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
   * @param exportHandle The {@link Result} object for storing the export
   *                     handle.
   * @return Zero (0) on success and non-zero on failure.
   */
  int exportJSONEntityReport(long flags, Result<Long> exportHandle);

  /**
   * This is used to export entity data from known entities.  This function
   * returns an export-handle that can be read from to get the export data
   * in CSV format.  The export-handle should be read using the "G2_fetchNext"
   * function, and closed when work is complete. The first output row returned
   * by the export-handle contains the JSON column headers as a string.  Each
   * following row contains the exported entity data.
   *
   * @param csvColumnList Specify <code>"*"</code> to indicate "all columns",
   *                      specify empty-string to indicate the "standard
   *                      columns", otherwise specify a comma-separated list of
   *                      column names.
   * @param flags A bit mask specifying other control flags, such as
   *        "G2_EXPORT_INCLUDE_SINGLETONS".  The default and recommended
   *        value is "G2_EXPORT_DEFAULT_FLAGS".
   * @param exportHandle The {@link Result} object for storing the export
   *                     handle.
   *
   * @return Returns an export handle that the entity data can be read from.
   */
  int exportCSVEntityReport(String        csvColumnList,
                            long          flags,
                            Result<Long>  exportHandle);

  /**
   * This function is used to read entity data from an export handle,
   * one data row at a time.
   *
   * @param exportHandle The export handle to retrieve data from
   * @param response The {@link StringBuffer} to write the next exported
   *                 record to.
   * @return Zero (0) on success and non-zero on failure.
   */
  int fetchNext(long exportHandle, StringBuffer response);

  /**
   * This function closes an export handle, to clean up system resources.
   *
   * @param exportHandle The export handle of the export to close.
   * @return Zero (0) on success and non-zero on failure.
   */
  int closeExport(long exportHandle);

  /**
   * Processes a redo record.
   *
   * @param jsonData TBD: Need to find out if this is an input, an output or
   *                 both.
   * @return Zero (0) on success and non-zero on failure.
   */
  int processRedoRecord(StringBuffer jsonData);

  /**
   * Processes a redo record.
   *
   * @param flags The flags to control how the operation is performed and
   *              specifically the content of the response JSON document.
   * @param jsonData A returned JSON document containing the info.
   * @param response A {@link StringBuffer} for returning the response document.
   *                 If an error occurred, an error response is stored here.
   *
   * @return Zero (0) on success and non-zero on failure.
   */
  int processRedoRecordWithInfo(long          flags,
                                StringBuffer  jsonData,
                                StringBuffer  response);


  /**
   * Retrieves a pending redo record from the reevaluation queue.
   *
   * @param jsonData A {@link StringBuffer} to write the redo record to.
   *
   * @return Zero (0) on success and non-zero on failure.
   */
  int getRedoRecord(StringBuffer jsonData);

  /**
   * Gets the number of redo records waiting to be processed.
   *
   * @return The number of redo records waiting to be processed.
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
   * @return Zero (0) on success and non-zero on failure.
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
   * @param flags The flags to control how the operation is performed and
   *              specifically the content of the response JSON document.
   * @param response A {@link StringBuffer} for returning the response document.
   *                 If an error occurred, an error response is stored here.
   * @return Zero (0) on success and non-zero on failure.
   */
  int processWithInfo(String record, long flags, StringBuffer response);

  /**
   * This method will send a record for processing in g2. It is a synchronous
   * call, i.e. it will wait until g2 actually processes the record, and then
   * return a response message.  This is similar to {@link #process(String)}
   * method, but this variant returns a response.  <b>NOTE:</b> there are
   * performance benefits of calling the variant of process that does <b>not</b>
   * require a response.
   *
   * @param record An input record to be processed.
   * @param response A {@link StringBuffer} for returning the response document.
   * @return Zero (0) on success and non-zero on failure.
   */
  int process(String record, StringBuffer response);
}

