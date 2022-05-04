package com.senzing.g2.engine;

/**
 * Defines the Java interface to the G2 diagnostic functions.  The G2 diagnostic
 * functions provide diagnostics and statistics pertaining to the host system
 * and the Senzing repository.
 */
public interface G2Diagnostic extends G2Fallible
{
  /**
   * Initializes the G2 Diagnostic object with the specified module name,
   * init parameters and flag indicating verbose logging.  If the
   * <code>G2CONFIGFILE</code> init parameter is absent then the default
   * configuration from the repository is used.
   *
   * @param moduleName A short name given to this instance of the diagnostic
   *                   object.
   * @param iniParams A JSON string containing configuration parameters.
   * @param verboseLogging Enable diagnostic logging which will print a massive
   *                       amount of information to stdout.
   *
   * @return Zero (0) on success, non-zero on failure.
   */
  int init(String moduleName, String iniParams, boolean verboseLogging);

  /**
   * Initializes the G2 Diagnostic object with the module name, initialization
   * parameters, verbose logging flag and a specific configuration ID
   * identifying the configuration to use.
   *
   * @param moduleName The module name with which to initialize.
   * @param iniParams The JSON initialization parameters.
   * @param initConfigID The specific configuration ID to initialize with.
   * @param verboseLogging Whether or not to initialize with verbose logging.
   *
   * @return Zero (0) on success and non-zero on failure.
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
   * @return Zero (0) on success and non-zero on failure.
   */
  int reinit(long initConfigID);

  /**
   * Uninitializes the G2 diagnostic object.
   *
   * @return Zero (0) on success and non-zero on failure.
   */
  int destroy();

  /**
   * Returns the total number of bytes of RAM on the system
   *
   * @return The number of bytes on success and negative one (-1) on failure.
   */
  long getTotalSystemMemory();

  /**
   * Returns the available number of bytes of RAM on the system
   *
   * @return The number of bytes on success and negative one (-1) on failure.
   */
  long getAvailableMemory();


  /**
   * Returns the number of physical cores on the system.
   *
   * @return The number of cores on success and negative one (-1) on failure.
   */
  int getPhysicalCores();

  /**
   * Returns the number of logical cores on the system.  This may be different
   * that physical due to hyper-threading.
   *
   * @return The number of cores on success and negative one (-1) on failure.
   */
  int getLogicalCores();

  /**
   * Runs non-destruction DB performance tests and writes detail of the result
   * as JSON in the specified {@link StringBuffer}.
   *
   * @param secondsToRun How long to run the database performance test.
   * @param response The {@link StringBuffer} in which to write the JSON text
   *                 that details the result of the performance test.
   *
   * @return Zero (0) on success and non-zero on failure.
   */
  int checkDBPerf(int secondsToRun, StringBuffer response);


  /**
   * Gets information about database connections and generates a JSON document
   * describing the result in the following format (sans pretty-printing):
   * <pre>
   *   {
   *     "Hybrid Mode": false,
   *     "Database Details": [
   *       {
   *         "Name": "SENZDB",
   *         "Type": "POSTGRES"
   *       }
   *     ]
   *   }
   * </pre>
   *
   * @param response The {@link StringBuffer} in which to write Json document
   *                 describing the result.
   *
   * @return Zero (0) on success and non-zero on failure.
   */
  int getDBInfo(StringBuffer response);


  /**
   * This methods asks g2 for any entities having any of the lib feat id
   * specified in the "features" doc.  The "features" also contains an
   * entity id that specifies the entity to be ignored in the returned values.
   * The response is written as a JSON document to the specified {@link
   * StringBuffer}.
   *
   * <p>
   * The format of the "features" document is as follows:
   * <pre>
   *   {
   *     "ENTITY_ID": &lt;entity_id&gt;,
   *     "LIB_FEAT_IDS": [ &lt;id1&gt;, &lt;id2&gt;, ... &lt;idn&gt; ]
   *   }
   * </pre>
   *
   * <p>
   * The format of the response document is as follows (sans pretty-printing):
   * <pre>
   *   [
   *     {
   *       "LIB_FEAT_ID": &lt;lib_feat_id&gt;,
   *       "USAGE_TYPE": "&lt;usage_type&gt;",
   *       "RES_ENT_ID": &lt;entity_id&gt;
   *     },
   *     ...
   *   ]
   * </pre>
   *
   * @param features Json document describing the desired features.
   * @param response The {@link StringBuffer} to write the JSON response to.
   *
   * @return Zero (0) on success and non-zero on failure.
   */
  int findEntitiesByFeatureIDs(String features, StringBuffer response);

  /**
   * Experimental/internal method for obtaining data source counts.
   *
   * @param response The {@link StringBuffer} to write the JSON response
   *                 document to.
   * @return Zero (0) on success and non-zero on failure.
   */
  int getDataSourceCounts(StringBuffer response);

  /**
   * Experimental/internal method for obtaining mapping statistics.
   *
   * @param includeInternalFeatures <code>true</code> if internal features
   *                                should be included and <code>false</code>
   *                                if they should be excluded.
   * @param response The {@link StringBuffer} to write the JSON response
   *                 document to.
   * @return Zero (0) on success and non-zero on failure.
   */
  int getMappingStatistics(boolean includeInternalFeatures, StringBuffer response);

  /**
   * Experimental/internal method for obtaining generic features.
   * @param featureType The feature type code.
   * @param maximumEstimatedCount The maximum estimated count.
   * @param response The {@link StringBuffer} to write the JSON response
   *                 document to.
   * @return Zero (0) on success and non-zero on failure.
   */
  int getGenericFeatures(String       featureType,
                         long         maximumEstimatedCount,
                         StringBuffer response);

  /**
   * Experimental/internal method for obtaining entity size breakdown.
   * @param minimumEntitySize The minimum entity size.
   * @param includeInternalFeatures <code>true</code> if internal features
   *                                should be included and <code>false</code>
   *                                if they should be excluded.
   * @param response The {@link StringBuffer} to write the JSON response
   *                 document to.
   * @return Zero (0) on success and non-zero on failure.
   */
  int getEntitySizeBreakdown(long         minimumEntitySize,
                             boolean      includeInternalFeatures,
                             StringBuffer response);

  /**
   * Experimental/internal method for obtaining diagnostic entity details.
   *
   * @param entityID The entity ID for the entity.
   * @param includeInternalFeatures <code>true</code> if internal features
   *                                should be included and <code>false</code>
   *                                if they should be excluded.
   * @param response The {@link StringBuffer} to write the JSON response
   *                 document to.
   * @return Zero (0) on success and non-zero on failure.
   */
  int getEntityDetails(long         entityID,
                       boolean      includeInternalFeatures,
                       StringBuffer response);


  /**
   * Experimental/internal method for obtaining resolution statistics.
   *
   * @param response The {@link StringBuffer} to write the JSON response
   *                 document to.
   * @return Zero (0) on success and non-zero on failure.
   */
  int getResolutionStatistics(StringBuffer response);

  /**
   * Experimental/internal method for obtaining diagnostic relationship
   * details.
   *
   * @param relationshipID The relationship ID identifying the relationship.
   * @param includeInternalFeatures <code>true</code> if internal features
   *                                should be included and <code>false</code>
   *                                if they should be excluded.
   * @param response The {@link StringBuffer} to write the JSON response
   *                 document to.
   * @return Zero (0) on success and non-zero on failure.
   */
  int getRelationshipDetails(long         relationshipID,
                             boolean      includeInternalFeatures,
                             StringBuffer response);

  /**
   * Experimental/internal method for obtaining diagnostic entity resume.
   *
   * @param entityID The entity ID.
   * @param response The {@link StringBuffer} to write the JSON result
   *                 document to.
   * @return Zero (0) on success and non-zero on failure.
   */
  int getEntityResume(long entityID, StringBuffer response);

  /**
   * Experimental/internal method for obtaining diagnostic feature information.
   *
   * @param libFeatID The <code>LIB_FEAT_ID</code> identifying the feature.
   * @param response The {@link StringBuffer} to write the JSON result
   *                 document to.
   * @return Zero (0) on success and non-zero on failure.
   */
  int getFeature(long libFeatID, StringBuffer response);

  /**
   * Experimental/internal method for obtaining diagnostic information on
   * sized entities.
   *
   * @param entitySize The entity size.
   *
   * @param exportHandle The {@link Result} object on which the result handle
   *                     will be set.
   *
   * @return Zero (0) on success and non-zero on failure.
   */
  int getEntityListBySize(long entitySize, Result<Long> exportHandle);

  /**
   * Experimental/internal method for obtaining the next sized entity result
   * with the specified handle obtained from {@link
   * #getEntityListBySize(long,Result)}.
   *
   * @param entityListBySizeHandle The handle for the result set.
   * @param response The {@link StringBuffer} to write the JSON result
   *                 document to.
   *
   * @return Zero (0) on success and non-zero on failure.
   */
  int fetchNextEntityBySize(long entityListBySizeHandle, StringBuffer response);

  /**
   * Experimental/internal method to close the result set associated with the
   * specified handle obtained from {@link
   * #getEntityListBySize(long,Result)}.
   *
   * @param entityListBySizeHandle The handle for the result set.
   * @return Zero (0) on success and non-zero on failure.
   */
  int closeEntityListBySize(long entityListBySizeHandle);

}
