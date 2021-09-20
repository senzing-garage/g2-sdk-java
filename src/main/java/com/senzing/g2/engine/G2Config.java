package com.senzing.g2.engine;

/**
 * Defines the Java interface to the G2 configuration functions.
 */
public interface G2Config extends G2Fallible
{
  /**
   * Initializes the G2 config API with the specified module name,
   * init parameters and flag indicating verbose logging.
   *
   * @param moduleName A short name given to this instance of the diagnostic
   *                   object.
   * @param iniParams A JSON string containing configuration parameters.
   * @param verboseLogging Enable diagnostic logging which will print a massive
   *                       amount of information to stdout.
   *
   * @return Zero (0) on success and non-zero on failure.
   */
  int init(String moduleName, String iniParams, boolean verboseLogging);

  /**
   * Uninitializes the G2 config API and cleans up system resources.
   *
   * @return Zero (0) on success and non-zero on failure.
   */
  int destroy();

  /**
   * Creates a new in-memory configuration using the default template and
   * returns the config handle for working with it.
   *
   * @return The configuration handle or a negative number of an error occurs.
   */
  long create();

  /**
   * Creates a new in-memory configuration using the specified JSON text and
   * returns the config handle for working with it.
   *
   * @param jsonConfig The JSON text for the config.
   *
   * @return The configuration handle or a negative number of an error occurs.
   */
  long load(String jsonConfig);

  /**
   * Writes the JSON text for the configuration associated with the specified
   * configuration handle to the specified {@link StringBuffer}.
   *
   * @param configHandle The configuration handle to export the JSON text from.
   * @param response The {@link StringBuffer} to write the JSON text to.
   * @return Zero (0) on success and non-zero on failure.
   */
  int save(long configHandle, StringBuffer response );

  /**
   * Closes the in-memory configuration associated with the specified config
   * handle and cleans up system resources.  After calling this method, the
   * configuration handle can no longer be used and becomes invalid.
   *
   * @param configHandle The config handle identifying the in-memory
   *                     configuration to close.
   */
  void close(long configHandle);

  /**
   * Adds a single data source with the specified data source code to the
   * in-memory configuration associated with the specified config handle.  The
   * data source ID will be automatically set to the next data source ID.
   *
   * @param configHandle The config handle identifying the in-memory
   *                     configuration to close.
   * @param code The data source code for the new data source.
   * @return Zero (0) on success and non-zero on failure.
   */
  int addDataSource(long configHandle, String code);

  /**
   * Adds a single data source with the specified data source code and data
   * source ID to the in-memory configuration associated with the specified
   * config handle.
   *
   * @param configHandle The config handle identifying the in-memory
   *                     configuration to close.
   * @param code The data source code for the new data source.
   * @param id The data source ID for the new data source.
   * @return Zero (0) on success and non-zero on failure.
   */
  int addDataSourceWithID(long configHandle, String code, int id);

  /**
   * Extracts the data sources from the in-memory configuration associated with
   * the specified config handle and writes JSON text to the specified
   * {@link StringBuffer} describing the data sources from the configuration.
   * The format of the JSON response is as follows:
   * <pre>
   * {
   * 	"DSRC_CODE": [
   * 		"TEST",
   * 		"SEARCH",
   * 		"DSRC-789",
   * 		"DSRC-456"
   * 	]
   * }
   * </pre>
   *
   * @param configHandle The config handle identifying the in-memory
   *                     configuration to close.
   * @param response The {@link StringBuffer} to write the JSON response to.
   * @return Zero (0) on success and non-zero on failure.
   * @deprecated
   */
  @Deprecated(since = "2.9.0", forRemoval = true)
  int listDataSources(long configHandle, StringBuffer response);

  /**
   * Extracts the data sources from the in-memory configuration associated with
   * the specified config handle and writes JSON text to the specified
   * {@link StringBuffer} describing the data sources from the configuration.
   * The format of the JSON response is as follows:
   * <pre>
   * {
   * 	"DATA_SOURCES": [
   *    {
   * 			"DSRC_ID": 1,
   * 			"DSRC_CODE": "TEST"
   *    },
   *    {
   * 			"DSRC_ID": 2,
   * 			"DSRC_CODE": "SEARCH"
   *    }
   * 	]
   * }
   * </pre>
   *
   * @param configHandle The config handle identifying the in-memory
   *                     configuration to close.
   * @param response The {@link StringBuffer} to write the JSON response to.
   * @return Zero (0) on success and non-zero on failure.
   */
  int listDataSourcesV2(long configHandle, StringBuffer response);

  /**
   * Adds a data source described by the specified JSON to the in-memory
   * configuration associated with the specified config handle.  The response
   * JSON is written to the specified {@link StringBuffer}.
   * The input JSON has the following format:
   * <pre>
   *   {
   *     "DSRC_CODE": "CUSTOMERS"
   *   }
   * </pre>
   * Optionally, you can specify the data source ID:
   * <pre>
   *   {
   *     "DSRC_CODE": "CUSTOMERS",
   *     "DSRC_ID": 410
   *   }
   * </pre>
   * <p>
   * The response JSON provides the data source ID of the created data source,
   * which is especially useful if the data source ID was not specified in the
   * input:
   * <pre>
   *   {
   *     "DSRC_ID": 410
   *   }
   * </pre>
   *
   * @param configHandle The config handle identifying the in-memory
   *                     configuration to close.
   * @param inputJson The JSON text describing the data source to create.
   * @param response The {@link StringBuffer} to write the JSON response to.
   * @return Zero (0) on success and non-zero on failure.
   */
  int addDataSourceV2(long          configHandle,
                      String        inputJson,
                      StringBuffer  response);

  /**
   * Deletes the data source described by the specified JSON from the in-memory
   * configuration associated with the specified config handle.
   * The input JSON has the following format:
   * <pre>
   *   {
   *     "DSRC_CODE": "CUSTOMERS"
   *   }
   * </pre>
   *
   * @param configHandle The config handle identifying the in-memory
   *                     configuration to close.
   * @param inputJson The JSON text describing the data source to delete.
   * @return Zero (0) on success and non-zero on failure.
   */
  int deleteDataSourceV2(long configHandle, String inputJson);

  /**
   * Extracts the entity classes from the in-memory configuration associated
   * with the specified config handle and writes JSON text to the specified
   * {@link StringBuffer} describing the entity classes from the configuration.
   * The format of the JSON response is as follows:
   * <pre>
   * {
   * 	"ENTITY_CLASSES": [
   *    {
   * 			"ECLASS_ID": 1,
   * 			"ECLASS_CODE": "ACTOR",
   * 		  "RESOLVE": "Yes"
   *    }
   * 	]
   * }
   * </pre>
   *
   * @param configHandle The config handle identifying the in-memory
   *                     configuration to close.
   * @param response The {@link StringBuffer} to write the JSON response to.
   * @return Zero (0) on success and non-zero on failure.
   */
  int listEntityClassesV2(long configHandle, StringBuffer response);

  /**
   * Adds an entity class described by the specified JSON to the in-memory
   * configuration associated with the specified config handle.  The response
   * JSON is written to the specified {@link StringBuffer}.
   * The input JSON has the following format:
   * <pre>
   * {
   * 	"ENTITY_CLASSES": [
   *    {
   * 			"ECLASS_CODE": "OBJECT",
   * 		  "RESOLVE": "Yes"
   *    }
   * 	]
   * }
   * </pre>
   * Optionally, you can specify the entity class ID:
   * <pre>
   *   {
   * 			"ECLASS_CODE": "OBJECT",
   * 		  "RESOLVE": "Yes",
   * 			"ECLASS_ID": 2,
   *   }
   * </pre>
   * <p>
   * The response JSON provides the entity class ID of the created entity class,
   * which is especially useful if the entity class ID was not specified in the
   * input:
   * <pre>
   *   {
   *     "ECLASS_ID": 2
   *   }
   * </pre>
   *
   * @param configHandle The config handle identifying the in-memory
   *                     configuration to close.
   * @param inputJson The JSON text describing the entity class to create.
   * @param response The {@link StringBuffer} to write the JSON response to.
   * @return Zero (0) on success and non-zero on failure.
   */
  int addEntityClassV2(long         configHandle,
                       String       inputJson,
                       StringBuffer response);

  /**
   * Deletes the entity class described by the specified JSON from the
   * in-memory configuration associated with the specified config handle.
   * The input JSON has the following format:
   * <pre>
   *   {
   *     "ECLASS_CODE": "MY_ENTITY_CLASS"
   *   }
   * </pre>
   *
   * @param configHandle The config handle identifying the in-memory
   *                     configuration to close.
   * @param inputJson The JSON text describing the entity class to delete.
   * @return Zero (0) on success and non-zero on failure.
   */
  int deleteEntityClassV2(long configHandle, String inputJson);

  /**
   * Extracts the entity types from the in-memory configuration associated with
   * the specified config handle and writes JSON text to the specified
   * {@link StringBuffer} describing the entity types from the configuration.
   * The format of the JSON response is as follows:
   * <pre>
   * {
   * 	"ENTITY_TYPES": [
   *    {
   * 			"ETYPE_ID": 1,
   * 			"ETYPE_CODE": "TEST",
   * 		  "ECLASS_CODE": "ACTOR"
   *    },
   *    {
   * 			"ETYPE_ID": 3,
   * 			"ETYPE_CODE": "GENERIC",
   * 		  "ECLASS_CODE": "ACTOR"
   *    },
   * 	]
   * }
   * </pre>
   *
   * @param configHandle The config handle identifying the in-memory
   *                     configuration to close.
   * @param response The {@link StringBuffer} to write the JSON response to.
   * @return Zero (0) on success and non-zero on failure.
   */
  int listEntityTypesV2(long configHandle, StringBuffer response);

  /**
   * Adds an entity type described by the specified JSON to the in-memory
   * configuration associated with the specified config handle.  The response
   * JSON is written to the specified {@link StringBuffer}.
   * The input JSON has the following format:
   * <pre>
   *   {
   *     "ETYPE_CODE": "ORGANIZATION",
   *     "ECLASS_CODE": "ACTOR"
   *   }
   * </pre>
   * Optionally, you can specify the data source ID:
   * <pre>
   *   {
   *     "ETYPE_CODE": "ORGANIZATION",
   *     "ECLASS_CODE": "ACTOR",
   *     "ETYPE_ID": 789
   *   }
   * </pre>
   * <p>
   * The response JSON provides the entity type ID of the created data source,
   * which is especially useful if the entity type ID was not specified in the
   * input:
   * <pre>
   *   {
   *     "ETYPE_ID": 789
   *   }
   * </pre>
   *
   * @param configHandle The config handle identifying the in-memory
   *                     configuration to close.
   * @param inputJson The JSON text describing the entity type to create.
   * @param response The {@link StringBuffer} to write the JSON response to.
   * @return Zero (0) on success and non-zero on failure.
   */
  int addEntityTypeV2(long          configHandle,
                      String        inputJson,
                      StringBuffer  response);

  /**
   * Deletes the entity type described by the specified JSON from the in-memory
   * configuration associated with the specified config handle.
   * The input JSON has the following format:
   * <pre>
   *   {
   *     "ETYPE_CODE": "ORGANIZATION"
   *   }
   * </pre>
   *
   * @param configHandle The config handle identifying the in-memory
   *                     configuration to close.
   * @param inputJson The JSON text describing the entity type to delete.
   * @return Zero (0) on success and non-zero on failure.
   */
  int deleteEntityTypeV2(long configHandle, String inputJson);

  /**
   * Extracts the feature elements from the in-memory configuration associated
   * with the specified config handle and writes JSON text to the specified
   * {@link StringBuffer} describing the feature elements from the
   * configuration.  The format of the JSON response is as follows:
   * <pre>
   * {
   * 	"FEATURE_ELEMENTS": [
   *    {
   * 			"FELEM_ID": 2,
   * 			"FELEM_CODE": "FULL_NAME"
   *    },
   *    {
   * 			"FELEM_ID": 3,
   * 			"FELEM_CODE": "ORG_NAME"
   *    },
   *    . . .
   * 	]
   * }
   * </pre>
   *
   * @param configHandle The config handle identifying the in-memory
   *                     configuration to close.
   * @param response The {@link StringBuffer} to write the JSON response to.
   * @return Zero (0) on success and non-zero on failure.
   */
  int listFeatureElementsV2(long configHandle, StringBuffer response);

  /**
   * Extracts a single feature element that is identified by the specified
   * input JSON from the in-memory configuration associated with the specified
   * config handle and writes the JSON text to the specified {@link
   * StringBuffer} describing the feature element details.
   * The format of the input JSON is as follows:
   * <pre>
   * {
   *   "FELEM_CODE": "GIVEN_NAME"
   * }
   * </pre>
   * <p>
   * The format of the JSON response is as follows:
   * <pre>
   * {
   *   "FELEM_ID": 5,
   *   "FELEM_CODE", "GIVEN_NAME",
   *   "TOKENIZE": "No",
   *   "DATA_TYPE": "string"
   * }
   * </pre>
   *
   *
   * @param configHandle The config handle identifying the in-memory
   *                     configuration to close.
   * @param inputJson The JSON text describing the feature element to retrieve.
   * @param response The {@link StringBuffer} to write the JSON response to.
   * @return Zero (0) on success and non-zero on failure.
   */
  int getFeatureElementV2(long          configHandle,
                          String        inputJson,
                          StringBuffer  response);

  /**
   * Adds the feature element described by the specified JSON to the in-memory
   * configuration associated with the specified config handle.  The response
   * JSON is written to the specified {@link StringBuffer}.
   * The input JSON has the following format:
   * <pre>
   *   {
   *     "FELEM_CODE": "FAVORITE_COLOR",
   *     "TOKENIZE": 1,
   *     "DATA_TYPE": "string"
   *   }
   * </pre>
   * Optionally, you can specify the feature element ID.
   * <pre>
   *   {
   *     "FELEM_CODE": "FAVORITE_COLOR",
   *     "TOKENIZE": 1,
   *     "DATA_TYPE": "string",
   *     "FELEM_ID": 789
   *   }
   * </pre>
   * <p>
   * The response JSON provides the feature element ID of the created feature
   * element, which is especially useful if the feature element ID was not
   * specified in the input:
   * <pre>
   *   {
   *     "FELEM_ID": 789
   *   }
   * </pre>
   *
   * @param configHandle The config handle identifying the in-memory
   *                     configuration to close.
   * @param inputJson The JSON text describing the feature element to create.
   * @param response The {@link StringBuffer} to write the JSON response to.
   * @return Zero (0) on success and non-zero on failure.
   */
  int addFeatureElementV2(long          configHandle,
                          String        inputJson,
                          StringBuffer  response);

  /**
   * Deletes the feature element described by the specified JSON from the
   * in-memory configuration associated with the specified config handle.
   * The input JSON has the following format:
   * <pre>
   *   {
   *     "FELEM_CODE": "FAVORITE_COLOR"
   *   }
   * </pre>
   *
   * @param configHandle The config handle identifying the in-memory
   *                     configuration to close.
   * @param inputJson The JSON text describing the feature element to delete.
   * @return Zero (0) on success and non-zero on failure.
   */
  int deleteFeatureElementV2(long configHandle, String inputJson);

  /**
   * Extracts the feature classes from the in-memory configuration associated
   * with the specified config handle and writes JSON text to the specified
   * {@link StringBuffer} describing the feature classes from the configuration.
   * The format of the JSON response is as follows:
   * <pre>
   * {
   * 	"FEATURE_CLASSES": [
   *    {
   * 			"FCLASS_ID": 1,
   * 			"FCLASS_CODE": "NAME"
   *    },
   *    {
   * 			"FCLASS_ID": 2,
   * 			"FCLASS_CODE": "BIO_DATE"
   *    },
   *    . . .
   * 	]
   * }
   * </pre>
   *
   * @param configHandle The config handle identifying the in-memory
   *                     configuration to close.
   * @param response The {@link StringBuffer} to write the JSON response to.
   * @return Zero (0) on success and non-zero on failure.
   */
  int listFeatureClassesV2(long configHandle, StringBuffer response);

  /**
   * Extracts the features from the in-memory configuration associated with
   * the specified config handle and writes JSON text to the specified
   * {@link StringBuffer} describing the features from the configuration.
   * The format of the JSON response is as follows:
   * <pre>
   * {
   * 	"FEATURE_TYPES": [
   *    {
   * 			"FTYPE_ID": 1,
   * 			"FTYPE_CODE": "NAME"
   *    },
   *    {
   * 			"FTYPE_ID": 2,
   * 			"FTYPE_CODE": "DOB"
   *    },
   *    . . .
   * 	]
   * }
   * </pre>
   *
   * @param configHandle The config handle identifying the in-memory
   *                     configuration to close.
   * @param response The {@link StringBuffer} to write the JSON response to.
   * @return Zero (0) on success and non-zero on failure.
   */
  int listFeaturesV2(long configHandle, StringBuffer response);

  /**
   * Extracts a single feature that is identified by the specified input JSON
   * from the in-memory configuration associated with the specified config
   * handle and writes the JSON text to the specified {@link StringBuffer}
   * describing the feature element details.  The format of the input JSON is
   * as follows:
   * <pre>
   * {
   *   "FTYPE_CODE": "NAME"
   * }
   * </pre>
   * <p>
   * The format of the JSON response is as follows:
   * <pre>
   * {
   *   "FTYPE_ID": 1,
   *   "FTYPE_CODE", "NAME",
   *   "FCLASS_CODE": "NAME",
   *   "BEHAVIOR": "NAME",
   *   "USED_FOR_CAND": "No",
   *   "RTYPE_CODE": "",
   *   "ANONYMIZE": "No",
   *   "VERSION": 2,
   *   "FEATURE_ELEMENTS": [
   *     { "FELEM_CODE": "FULL_NAME" },
   *     { "FELEM_CODE": "ORG_NAME" },
   *     { "FELEM_CODE": "SUR_NAME" },
   *     { "FELEM_CODE": "GIVEN_NAME" },
   *     { "FELEM_CODE": "MIDDLE_NAME" },
   *     { "FELEM_CODE": "NAME_PREFIX" },
   *     . . .
   *   ],
   *   "ATTRIBUTES": [
   *     { "ATTR_CODE": "NAME_TYPE" },
   *     { "ATTR_CODE": "NAME_FULL" },
   *     { "ATTR_CODE": "NAME_ORG" },
   *     . . .
   *   ],
   *   "STANDARDIZE_FUNCS": [
   *     {
   *       "SFCALL_ID": 1,
   *       "SFUNC_CODE": "PARSE_NAME",
   *       "FTYPE_CODE": "NAME",
   *       "FELEM_CODE": "",
   *       "EXEC_ORDER": 1
   *     },
   *     {
   *       "SFCALL_ID": 20,
   *       "SFUNC_CODE": "TOKENIZE_NAME",
   *       "FTYPE_CODE": "NAME",
   *       "FELEM_CODE": "",
   *       "EXEC_ORDER": 50
   *     }
   *   ],
   *   "EXPRESSION_FUNCS": [
   *     {
   *       "EFCALL_ID": 7,
   *       "EFUNC_CODE": "NAME_HASHER",
   *       "FTYPE_CODE": "NAME",
   *       "FELEM_CODE": "",
   *       "EXEC_ORDER": 2
   *     }
   *   ],
   *   "COMPARISON_FUNCS": [
   *     {
   *       "CFCALL_ID": 1,
   *       "CFUNC_CODE": "GNR_COMP"
   *     }
   *   ],
   *   "DISTINCT_FUNCS": [
   *     {
   *       "DFCALL_ID": 1,
   *       "DFUNC_CODE": "PARTIAL_NAMES"
   *     }
   *   ]
   * }
   * </pre>
   *
   * @param configHandle The config handle identifying the in-memory
   *                     configuration to close.
   * @param inputJson The JSON text describing the feature to retrieve.
   * @param response The {@link StringBuffer} to write the JSON response to.
   * @return Zero (0) on success and non-zero on failure.
   */
  int getFeatureV2(long configHandle, String inputJson, StringBuffer response);

  /**
   * Adds the feature described by the specified JSON to the in-memory
   * configuration associated with the specified config handle.  The response
   * JSON is written to the specified {@link StringBuffer}.
   * The input JSON has the following format:
   * <pre>
   *   {
   *     "FTYPE_CODE": "MY_FEATURE",
   *     "FTYPE_ID": 789,
   *     "FCLASS_CODE": "ELECTED_ID",
   *     "FTYPE_FREQ": "FF",
   *     "FEATURE_ELEMENTS": [
   *       {
   *         "FELEM_CODE": "expression"
   *       }
   *     ]
   *   }
   * </pre>
   * <p>
   * The response JSON provides the feature type ID of the created feature:
   * <pre>
   *   {
   *     "FTYPE_ID": 789
   *   }
   * </pre>
   *
   * @param configHandle The config handle identifying the in-memory
   *                     configuration to close.
   * @param inputJson The JSON text describing the feature to create.
   * @param response The {@link StringBuffer} to write the JSON response to.
   * @return Zero (0) on success and non-zero on failure.
   */
  int addFeatureV2(long configHandle, String inputJson, StringBuffer response);

  /**
   * Modifies the feature described by the specified JSON in the in-memory
   * configuration associated with the specified config handle.
   * The input JSON has the following format:
   * <pre>
   * {
   *   "FTYPE_CODE": "MY_FEATURE",
   *   "FCLASS_CODE": "PARTIAL_ID",
   *   "RTYPE_CODE": "POSSIBLY_SAME",
   *   "FTYPE_FREQ": "FM",
   *   "FTYPE_EXCL": "Y",
   *   "FTYPE_STAB": "Y",
   *   "USED_FOR_CAND": "Y",
   *   "DERIVED": "Y",
   *   "ANONYMIZE": "Y",
   *   "VERSION": "17"
   * }
   * </pre>
   *
   * @param configHandle The config handle identifying the in-memory
   *                     configuration to close.
   * @param inputJson The JSON text describing the feature to modify.
   * @return Zero (0) on success and non-zero on failure.
   */
  int modifyFeatureV2(long configHandle, String inputJson);

  /**
   * Deletes the feature described by the specified JSON from the in-memory
   * configuration associated with the specified config handle.
   * The input JSON has the following format:
   * <pre>
   * {
   *   "FTYPE_CODE": "MY_FEATURE"
   * }
   * </pre>
   *
   * @param configHandle The config handle identifying the in-memory
   *                     configuration to close.
   * @param inputJson The JSON text describing the feature to delete.
   * @return Zero (0) on success and non-zero on failure.
   */
  int deleteFeatureV2(long configHandle, String inputJson);

  /**
   * Adds an element to the feature described by the specified JSON in
   * the in-memory configuration associated with the specified config handle.
   * The input JSON has the following format:
   * <pre>
   * {
   *   "FTYPE_CODE": "MY_FEATURE",
   *   "FELEM_CODE": "addr4",
   *   "EXEC_ORDER": 4
   * }
   * </pre>
   *
   * @param configHandle The config handle identifying the in-memory
   *                     configuration to close.
   * @param inputJson The JSON text describing the feature element to add.
   * @return Zero (0) on success and non-zero on failure.
   */
  int addElementToFeatureV2(long configHandle, String inputJson);

  /**
   * Modifies an element for the feature described by the specified JSON in
   * the in-memory configuration associated with the specified config handle.
   * The input JSON has the following format:
   * <pre>
   * {
   *   "FTYPE_CODE": "MY_FEATURE",
   *   "FELEM_CODE": "addr6",
   *   "EXEC_ORDER": 2134,
   *   "DERIVED": "N",
   *   "DISPLAY_LEVEL": 19
   * }
   * </pre>
   *
   * @param configHandle The config handle identifying the in-memory
   *                     configuration to close.
   * @param inputJson The JSON text describing the feature element to modify.
   * @return Zero (0) on success and non-zero on failure.
   */
  int modifyElementForFeatureV2(long configHandle, String inputJson);

  /**
   * Deletes an element from the feature described by the specified JSON in
   * the in-memory configuration associated with the specified config handle.
   * The input JSON has the following format:
   * <pre>
   * {
   *   "FTYPE_CODE": "MY_FEATURE",
   *   "FELEM_CODE": "addr5"
   * }
   * </pre>
   *
   * @param configHandle The config handle identifying the in-memory
   *                     configuration to close.
   * @param inputJson The JSON text describing the feature element to modify.
   * @return Zero (0) on success and non-zero on failure.
   */
  int deleteElementFromFeatureV2(long configHandle, String inputJson);

  /**
   * Extracts the feature standardization functions from the in-memory
   * configuration associated with the specified config handle and writes JSON
   * text to the specified {@link StringBuffer} describing the feature
   * standardization functions from the configuration.
   * The format of the JSON response is as follows:
   * <pre>
   * {
   *   "FEATURE_STANDARDIZATION_FUNCTIONS": [
   *     { "SFUNC_ID": 1, "SFUNC_CODE": "PARSE_NAME" },
   *     { "SFUNC_ID": 2, "SFUNC_CODE": "PARSE_DOB" },
   *     { "SFUNC_ID": 4, "SFUNC_CODE": "FORMAT_SSN" },
   *     . . .
   *   ]
   * }
   * </pre>
   *
   * @param configHandle The config handle identifying the in-memory
   *                     configuration to close.
   * @param response The {@link StringBuffer} to write the JSON response to.
   * @return Zero (0) on success and non-zero on failure.
   */
  int listFeatureStandardizationFunctionsV2(long          configHandle,
                                            StringBuffer  response);

  /**
   * Adds a feature standardization function described by the specified JSON to
   * the in-memory configuration associated with the specified config handle.
   * The response JSON is written to the specified {@link StringBuffer}.
   * The input JSON has the following format:
   * <pre>
   *   {
   *     "SFUNC_CODE": "MY_SFUNC_2",
   *     "CONNECT_STR": "MyConnectStr2"
   *   }
   * </pre>
   * <p>
   * The response JSON provides the standardization function ID of the
   * registered function:
   * <pre>
   *   {
   *     "SFUNC_ID": 1001
   *   }
   * </pre>
   *
   * @param configHandle The config handle identifying the in-memory
   *                     configuration to close.
   * @param inputJson The JSON text describing the standardization function
   *                  to register.
   * @param response The {@link StringBuffer} to write the JSON response to.
   * @return Zero (0) on success and non-zero on failure.
   */
  int addFeatureStandardizationFunctionV2(long configHandle, String inputJson, StringBuffer response);

  /**
   * Extracts the feature expression functions from the in-memory
   * configuration associated with the specified config handle and writes JSON
   * text to the specified {@link StringBuffer} describing the feature
   * expression functions from the configuration.
   * The format of the JSON response is as follows:
   * <pre>
   * {
   *   "FEATURE_EXPRESSION_FUNCTIONS" : [
   *     { "EFUNC_ID": 1, "EFUNC_CODE": "EXPRESS_BOM" },
   *     { "EFUNC_ID": 2, "EFUNC_CODE": "NAME_HASHER" },
   *     { "EFUNC_ID": 3, "EFUNC_CODE": "ADDR_HASHER" },
   *     { "EFUNC_ID": 4, "EFUNC_CODE": "PHONE_HASHER" },
   *     { "EFUNC_ID": 5, "EFUNC_CODE": "EXPRESS_ID" },
   *     { "EFUNC_ID": 6, "EFUNC_CODE": "STB_HASHER" },
   *     { "EFUNC_ID": 7, "EFUNC_CODE": "FEAT_BUILDER"}
   *   ]
   * }
   * </pre>
   *
   * @param configHandle The config handle identifying the in-memory
   *                     configuration to close.
   * @param response The {@link StringBuffer} to write the JSON response to.
   * @return Zero (0) on success and non-zero on failure.
   */
  int listFeatureExpressionFunctionsV2(long         configHandle,
                                       StringBuffer response);

  /**
   * Adds a feature expression function described by the specified JSON to
   * the in-memory configuration associated with the specified config handle.
   * The response JSON is written to the specified {@link StringBuffer}.
   * The input JSON has the following format:
   * <pre>
   * {
   *   "EFUNC_ID": 789,
   *   "EFUNC_CODE": "MY_EFUNC_1",
   *   "FUNC_LIB": "MyFuncLib",
   *   "FUNC_VER": 28,
   *   "CONNECT_STR": "MyConnectStr1"
   * }
   * </pre>
   * <p>
   * The response JSON provides the expression function ID of the registered
   * function:
   * <pre>
   *   {
   *     "EFUNC_ID": 789
   *   }
   * </pre>
   *
   * @param configHandle The config handle identifying the in-memory
   *                     configuration to close.
   * @param inputJson The JSON text describing the expression function
   *                  to register.
   * @param response The {@link StringBuffer} to write the JSON response to.
   * @return Zero (0) on success and non-zero on failure.
   */
  int addFeatureExpressionFunctionV2(long         configHandle,
                                     String       inputJson,
                                     StringBuffer response);

  /**
   * Modifies the feature expression function described by the specified JSON
   * to the in-memory configuration associated with the specified config
   * handle.  The response JSON is written to the specified {@link
   * StringBuffer}.  The input JSON has the following format:
   * <pre>
   * {
   *   "EFUNC_CODE": "MY_EFUNC_1",
   *   "FUNC_LIB": "MyFuncLib",
   *   "FUNC_VER": 28,
   *   "CONNECT_STR": "MyConnectStr1"
   * }
   * </pre>
   *
   * @param configHandle The config handle identifying the in-memory
   *                     configuration to close.
   * @param inputJson The JSON text describing the expression function
   *                  to modify.
   * @return Zero (0) on success and non-zero on failure.
   */
  int modifyFeatureExpressionFunctionV2(long configHandle, String inputJson);

  /**
   * Extracts the feature comparison functions from the in-memory
   * configuration associated with the specified config handle and writes JSON
   * text to the specified {@link StringBuffer} describing the feature
   * comparison functions from the configuration.  The format of the JSON
   * response is as follows:
   * <pre>
   * {
   *   "FEATURE_COMPARISON_FUNCTIONS": [
   *     { "CFUNC_ID": 1, "CFUNC_CODE": "STR_COMP" },
   *     { "CFUNC_ID": 2, "CFUNC_CODE": "GNR_COMP" },
   *     { "CFUNC_ID": 3, "CFUNC_CODE": "EXACT_COMP" },
   *     { "CFUNC_ID": 4, "CFUNC_CODE": "ADDR_COMP" },
   *     { "CFUNC_ID": 5, "CFUNC_CODE": "DOB_COMP" },
   *     { "CFUNC_ID": 7, "CFUNC_CODE": "PHONE_COMP" },
   *     { "CFUNC_ID": 8, "CFUNC_CODE": "SSN_COMP" },
   *     { "CFUNC_ID": 9, "CFUNC_CODE": "ID_COMP" },
   *     { "CFUNC_ID": 10, "CFUNC_CODE": "GEOLOC_COMP" },
   *     { "CFUNC_ID": 11, "CFUNC_CODE": "EXACT_DOMAIN_COMP" },
   *     { "CFUNC_ID": 12, "CFUNC_CODE": "GROUP_ASSOCIATION_COMP" },
   *     { "CFUNC_ID": 13, "CFUNC_CODE": "EMAIL_COMP" }
   *   ]
   * }
   * </pre>
   *
   * @param configHandle The config handle identifying the in-memory
   *                     configuration to close.
   * @param response The {@link StringBuffer} to write the JSON response to.
   * @return Zero (0) on success and non-zero on failure.
   */
  int listFeatureComparisonFunctionsV2(long configHandle, StringBuffer response);

  /**
   * Adds a feature comparison function described by the specified JSON to
   * the in-memory configuration associated with the specified config handle.
   * The response JSON is written to the specified {@link StringBuffer}.
   * The input JSON has the following format:
   * <pre>
   * {
   *   "CFUNC_ID": 789,
   *   "CFUNC_CODE": "MY_CFUNC_1",
   *   "FUNC_LIB": "MyFuncLib",
   *   "FUNC_VER": 28,
   *   "CONNECT_STR": "MyConnectStr1",
   *   "ANON_SUPPORT": "N"
   * }
   * </pre>
   * <p>
   * The response JSON provides the comparison function ID of the registered
   * function:
   * <pre>
   *   {
   *     "EFUNC_ID": 789
   *   }
   * </pre>
   *
   * @param configHandle The config handle identifying the in-memory
   *                     configuration to close.
   * @param inputJson The JSON text describing the comparison function
   *                  to register.
   * @param response The {@link StringBuffer} to write the JSON response to.
   * @return Zero (0) on success and non-zero on failure.
   */
  int addFeatureComparisonFunctionV2(long         configHandle,
                                     String       inputJson,
                                     StringBuffer response);

  /**
   * Adds a feature comparison function return code described by the specified
   * JSON to the in-memory configuration associated with the specified config
   * handle.  The response JSON is written to the specified {@link
   * StringBuffer}.  The input JSON has the following format:
   * <pre>
   * {
   *   "CFRTN_ID": 7890,
   *   "CFUNC_CODE": "MY_CFUNC_2",
   *   "CFUNC_RTNVAL": "MY_SCORE_1",
   *   "EXEC_ORDER": 345,
   *   "SAME_SCORE": 29,
   *   "CLOSE_SCORE": 28,
   *   "LIKELY_SCORE": 27,
   *   "PLAUSIBLE_SCORE": 26,
   *   "UN_LIKELY_SCORE": 25
   * }
   * </pre>
   * <p>
   * The response JSON provides the comparison function return code ID of the
   * registered function:
   * <pre>
   *   {
   *     "CFRTN_ID": 789
   *   }
   * </pre>
   *
   * @param configHandle The config handle identifying the in-memory
   *                     configuration to close.
   * @param inputJson The JSON text describing the comparison function
   *                  return code to register.
   * @param response The {@link StringBuffer} to write the JSON response to.
   * @return Zero (0) on success and non-zero on failure.
   */
  int addFeatureComparisonFunctionReturnCodeV2(long         configHandle,
                                               String       inputJson,
                                               StringBuffer response);

  /**
   * Extracts the feature distinct functions from the in-memory configuration
   * associated with the specified config handle and writes JSON text to the
   * specified {@link StringBuffer} describing the feature distinct functions
   * from the configuration.  The format of the JSON response is as follows:
   * <pre>
   * {
   *   "FEATURE_DISTINCT_FUNCTIONS": [
   *     { "DFUNC_ID": 1, "DFUNC_CODE": "FELEM_STRICT_SUBSET" },
   *     { "DFUNC_ID": 2, "DFUNC_CODE": "TOKEN_STRICT_SUBSET" },
   *     { "DFUNC_ID": 3, "DFUNC_CODE": "FELEM_NORM_STRICT_SUBSET" },
   *     { "DFUNC_ID": 4, "DFUNC_CODE": "PARTIAL_DATES" },
   *     { "DFUNC_ID": 5, "DFUNC_CODE": "PARTIAL_NAMES" },
   *     { "DFUNC_ID": 6, "DFUNC_CODE": "PARTIAL_ADDRESSES" }
   *   ]
   * }
   * </pre>
   *
   * @param configHandle The config handle identifying the in-memory
   *                     configuration to close.
   * @param response The {@link StringBuffer} to write the JSON response to.
   * @return Zero (0) on success and non-zero on failure.
   */
  int listFeatureDistinctFunctionsV2(long configHandle, StringBuffer response);

  /**
   * Adds a feature distinct function described by the specified JSON to
   * the in-memory configuration associated with the specified config handle.
   * The response JSON is written to the specified {@link StringBuffer}.
   * The input JSON has the following format:
   * <pre>
   * {
   *   "DFUNC_ID": 789,
   *   "DFUNC_CODE": "MY_DFUNC_1",
   *   "FUNC_LIB": "MyFuncLib",
   *   "FUNC_VER": 28,
   *   "CONNECT_STR": "MyConnectStr1",
   *   "ANON_SUPPORT": "N"
   * }
   * </pre>
   * <p>
   * The response JSON provides the feature distinct function ID of the
   * registered function:
   * <pre>
   *   {
   *     "DFUNC_ID": 789
   *   }
   * </pre>
   *
   * @param configHandle The config handle identifying the in-memory
   *                     configuration to close.
   * @param inputJson The JSON text describing the expression function
   *                  to register.
   * @param response The {@link StringBuffer} to write the JSON response to.
   * @return Zero (0) on success and non-zero on failure.
   */
  int addFeatureDistinctFunctionV2(long         configHandle,
                                   String       inputJson,
                                   StringBuffer response);

  /**
   * Extracts the feature standardization function calls from the in-memory
   * configuration associated with the specified config handle and writes JSON
   * text to the specified {@link StringBuffer} describing the feature
   * standardization function calls from the configuration.
   * The format of the JSON response is as follows:
   * <pre>
   * {
   *   "FEATURE_STANDARDIZATION_FUNCTION_CALLS": [
   *     {
   *       "SFCALL_ID": 1,
   *       "SFUNC_CODE": "PARSE_NAME",
   *       "FTYPE_CODE": "NAME",
   *       "FELEM_CODE": ""
   *     },
   *     {
   *       "SFCALL_ID": 2,
   *       "SFUNC_CODE": "PARSE_DOB",
   *       "FTYPE_CODE": "DOB",
   *       "FELEM_CODE": ""
   *     },
   *     {
   *       "SFCALL_ID": 3,
   *       "SFUNC_CODE": "PARSE_ADDR",
   *       "FTYPE_CODE": "ADDRESS",
   *       "FELEM_CODE": ""
   *     },
   *     . . .
   *   ]
   * }
   * </pre>
   *
   * @param configHandle The config handle identifying the in-memory
   *                     configuration to close.
   * @param response The {@link StringBuffer} to write the JSON response to.
   * @return Zero (0) on success and non-zero on failure.
   */
  int listFeatureStandardizationFunctionCallsV2(long          configHandle,
                                                StringBuffer  response);

  /**
   * Extracts a single feature standardization function call that is identified
   * by the specified input JSON from the in-memory configuration associated
   * with the specified config handle and writes the JSON text to the specified
   * {@link StringBuffer} describing the feature standardization function
   * details.  The format of the input JSON is as follows:
   * <pre>
   * {
   *   "SFCALL_ID": 1
   * }
   * </pre>
   * <p>
   * The format of the JSON response is as follows:
   * <pre>
   * {
   *   "SFCALL_ID": 1,
   *   "SFUNC_CODE": "PARSE_NAME",
   *   "FTYPE_CODE": "NAME",
   *   "FELEM_CODE": "",
   *   "EXEC_ORDER": 1
   * }
   * </pre>
   *
   *
   * @param configHandle The config handle identifying the in-memory
   *                     configuration to close.
   * @param inputJson The JSON text describing the feature element to retrieve.
   * @param response The {@link StringBuffer} to write the JSON response to.
   * @return Zero (0) on success and non-zero on failure.
   */
  int getFeatureStandardizationFunctionCallV2(long          configHandle,
                                              String        inputJson,
                                              StringBuffer  response);

  /**
   * Registers the feature standardization function described by the specified
   * JSON to the in-memory configuration associated with the specified config
   * handle.  The response JSON is written to the specified {@link
   * StringBuffer}.  The input JSON has the following format:
   * <pre>
   * {
   *   "FTYPE_CODE": "veh_vin",
   *   "SFCALL_ID": 789,
   *   "SFUNC_CODE": "PARSE_ID",
   *   "EXEC_ORDER": 15
   * }
   * </pre>
   * <p>
   * The response JSON provides the standardization function ID of the
   * registered feature standardization function:
   * <pre>
   *   {
   *     "SFCALL_ID": 789
   *   }
   * </pre>
   *
   * @param configHandle The config handle identifying the in-memory
   *                     configuration to close.
   * @param inputJson The JSON text describing the feature standardization
   *                  function to be registered.
   * @param response The {@link StringBuffer} to write the JSON response to.
   * @return Zero (0) on success and non-zero on failure.
   */
  int addFeatureStandardizationFunctionCallV2(long          configHandle,
                                              String        inputJson,
                                              StringBuffer  response);

  /**
   * Deletes/unregisters the feature standardization function described by the
   * specified JSON from the in-memory configuration associated with the
   * specified config handle.  The input JSON has the following format:
   * <pre>
   *   {
   *     "SFCALL_ID": 1004
   *   }
   * </pre>
   *
   * @param configHandle The config handle identifying the in-memory
   *                     configuration to close.
   * @param inputJson The JSON text identifying the feature standardization
   *                  function.
   * @return Zero (0) on success and non-zero on failure.
   */
  int deleteFeatureStandardizationFunctionCallV2(long   configHandle,
                                                 String inputJson);

  /**
   * Extracts the registered feature expression functions from the in-memory
   * configuration associated with the specified config handle and writes JSON
   * text to the specified {@link StringBuffer} describing the feature
   * expression functions from the configuration.  The format of the JSON
   * response is as follows:
   * <pre>
   * {
   *   "FEATURE_EXPRESSION_FUNCTIONS": [
   *     { "EFUNC_ID": 1, "EFUNC_CODE": "EXPRESS_BOM" },
   *     { "EFUNC_ID": 2, "EFUNC_CODE": "NAME_HASHER" },
   *     { "EFUNC_ID": 3, "EFUNC_CODE": "ADDR_HASHER" },
   *     { "EFUNC_ID": 4, "EFUNC_CODE": "PHONE_HASHER" },
   *     { "EFUNC_ID": 5, "EFUNC_CODE": "EXPRESS_ID" },
   *     { "EFUNC_ID": 6, "EFUNC_CODE": "STB_HASHER" },
   *     { "EFUNC_ID": 7, "EFUNC_CODE": "FEAT_BUILDER" }
   *   ]
   * }
   * </pre>
   *
   * @param configHandle The config handle identifying the in-memory
   *                     configuration to close.
   * @param response The {@link StringBuffer} to write the JSON response to.
   * @return Zero (0) on success and non-zero on failure.
   */
  int listFeatureExpressionFunctionCallsV2(long         configHandle,
                                           StringBuffer response);

  /**
   * Extracts a single feature expression function call that is identified
   * by the specified input JSON from the in-memory configuration associated
   * with the specified config handle and writes the JSON text to the specified
   * {@link StringBuffer} describing the feature expression function details.
   * The format of the input JSON is as follows:
   * <pre>
   * {
   *   "EFCALL_ID": 1
   * }
   * </pre>
   * <p>
   * The format of the JSON response is as follows:
   * <pre>
   * {
   *   "EFCALL_ID": 1,
   *   "EFUNC_CODE": "PHONE_HASHER",
   *   "FTYPE_CODE": "PHONE",
   *   "FELEM_CODE": "",
   *   "EXEC_ORDER": 1,
   *   "EFEAT_FTYPE_CODE": "",
   *   "IS_VIRTUAL": "No",
   *   "BOM_ELEMENTS": [
   *     {
   *       "EXEC_ORDER": 1,
   *       "FTYPE_CODE": "PHONE",
   *       "FELEM_CODE": "PHONE_LAST_10",
   *       "FTYPE_LINK": "",
   *       "FELEM_REQ": "Yes"
   *     }
   *   ]
   * }
   * </pre>
   *
   *
   * @param configHandle The config handle identifying the in-memory
   *                     configuration to close.
   * @param inputJson The JSON text describing the feature expression function
   *                  to retrieve.
   * @param response The {@link StringBuffer} to write the JSON response to.
   * @return Zero (0) on success and non-zero on failure.
   */
  int getFeatureExpressionFunctionCallV2(long         configHandle,
                                         String       inputJson,
                                         StringBuffer response);

  /**
   * Registers the feature expression function described by the specified
   * JSON to the in-memory configuration associated with the specified config
   * handle.  The response JSON is written to the specified {@link
   * StringBuffer}.  The input JSON has the following format:
   * <pre>
   * {
   *   "FTYPE_CODE": "veh_vin",
   *   "EFCALL_ID": 789,
   *   "EFUNC_CODE": "EXPRESS_ID",
   *   "EXEC_ORDER": 15,
   *   "BOM_ELEMENTS": [
   *     {
   *       "FTYPE_CODE": "veh_vin",
   *       "FELEM_CODE": "id_num",
   *       "REQUIRED": "Yes"
   *     }
   *   ]
   * }
   * </pre>
   * <p>
   * The response JSON provides the expression function ID of the registered
   * feature expression function:
   * <pre>
   *   {
   *     "EFCALL_ID": 789
   *   }
   * </pre>
   *
   * @param configHandle The config handle identifying the in-memory
   *                     configuration to close.
   * @param inputJson The JSON text describing the feature expression function
   *                  to be registered.
   * @param response The {@link StringBuffer} to write the JSON response to.
   * @return Zero (0) on success and non-zero on failure.
   */
  int addFeatureExpressionFunctionCallV2(long         configHandle,
                                         String       inputJson,
                                         StringBuffer response);

  /**
   * Deletes/unregisters the feature expression function described by the
   * specified JSON from the in-memory configuration associated with the
   * specified config handle.  The input JSON has the following format:
   * <pre>
   *   {
   *     "EFCALL_ID": 1004
   *   }
   * </pre>
   *
   * @param configHandle The config handle identifying the in-memory
   *                     configuration to close.
   * @param inputJson The JSON text identifying the feature expression function.
   * @return Zero (0) on success and non-zero on failure.
   */
  int deleteFeatureExpressionFunctionCallV2(long    configHandle,
                                            String  inputJson);

  /**
   * Adds an element to the feature expression function described by the
   * specified JSON to the in-memory configuration associated with the
   * specified config handle.  The input JSON has the following format:
   * <pre>
   * {
   *   "EFCALL_ID": 1002,
   *   "FELEM_CODE": "ADDR4",
   *   "REQUIRED": "Yes",
   *   "EXEC_ORDER": 4
   * }
   * </pre>
   *
   * @param configHandle The config handle identifying the in-memory
   *                     configuration to close.
   * @param inputJson The JSON text describing the feature expression function
   *                  element to be added.
   * @return Zero (0) on success and non-zero on failure.
   */
  int addFeatureExpressionFunctionCallElementV2(long    configHandle,
                                                String  inputJson);

  /**
   * Deletes an element from the feature expression function described by the
   * specified JSON to the in-memory configuration associated with the
   * specified config handle.  The input JSON has the following format:
   * <pre>
   * {
   *   "EFCALL_ID": 1002,
   *   "FELEM_CODE": "ADDR4"
   * }
   * </pre>
   *
   * @param configHandle The config handle identifying the in-memory
   *                     configuration to close.
   * @param inputJson The JSON text describing the feature expression function
   *                  element to be deleted.
   * @return Zero (0) on success and non-zero on failure.
   */
  int deleteFeatureExpressionFunctionCallElementV2(long   configHandle,
                                                   String inputJson);

  /**
   * Extracts the registered feature comparison functions from the in-memory
   * configuration associated with the specified config handle and writes JSON
   * text to the specified {@link StringBuffer} describing the feature
   * comparison functions from the configuration.  The format of the JSON
   * response is as follows:
   * <pre>
   * {
   *   "FEATURE_COMPARISON_FUNCTIONS": [
   *     { "CFUNC_ID": 1, "CFUNC_CODE": "STR_COMP" },
   *     { "CFUNC_ID": 2, "CFUNC_CODE": "GNR_COMP" },
   *     { "CFUNC_ID": 3, "CFUNC_CODE": "EXACT_COMP" },
   *     { "CFUNC_ID": 4, "CFUNC_CODE": "ADDR_COMP" },
   *     { "CFUNC_ID": 5, "CFUNC_CODE": "DOB_COMP" },
   *     { "CFUNC_ID": 7, "CFUNC_CODE": "PHONE_COMP" },
   *     { "CFUNC_ID": 8, "CFUNC_CODE": "SSN_COMP" },
   *     { "CFUNC_ID": 9, "CFUNC_CODE": "ID_COMP" },
   *     { "CFUNC_ID": 10, "CFUNC_CODE": "GEOLOC_COMP" },
   *     { "CFUNC_ID": 11, "CFUNC_CODE": "EXACT_DOMAIN_COMP" },
   *     { "CFUNC_ID": 12, "CFUNC_CODE": "GROUP_ASSOCIATION_COMP" },
   *     { "CFUNC_ID": 13, "CFUNC_CODE": "EMAIL_COMP" }
   *   ]
   * }
   * </pre>
   *
   * @param configHandle The config handle identifying the in-memory
   *                     configuration to close.
   * @param response The {@link StringBuffer} to write the JSON response to.
   * @return Zero (0) on success and non-zero on failure.
   */
  int listFeatureComparisonFunctionCallsV2(long         configHandle,
                                           StringBuffer response);

  /**
   * Extracts a single feature comparison function call that is identified
   * by the specified input JSON from the in-memory configuration associated
   * with the specified config handle and writes the JSON text to the specified
   * {@link StringBuffer} describing the feature comparison function details.
   * The format of the input JSON is as follows:
   * <pre>
   * {
   *   "CFCALL_ID": 1
   * }
   * </pre>
   * <p>
   * The format of the JSON response is as follows:
   * <pre>
   * {
   *   "CFCALL_ID": 1,
   *   "CFUNC_CODE": "GNR_COMP",
   *   "FTYPE_CODE": "NAME",
   *   "BOM_ELEMENTS": [
   *     { "EXEC_ORDER": 1, "FELEM_CODE": "FULL_NAME" },
   *     { "EXEC_ORDER": 2, "FELEM_CODE": "ORG_NAME" },
   *     { "EXEC_ORDER": 3, "FELEM_CODE": "SUR_NAME" },
   *     { "EXEC_ORDER": 4, "FELEM_CODE": "GIVEN_NAME" },
   *     { "EXEC_ORDER": 5, "FELEM_CODE": "MIDDLE_NAME" },
   *     { "EXEC_ORDER": 6, "FELEM_CODE": "NAME_PREFIX" },
   *     { "EXEC_ORDER": 7, "FELEM_CODE": "NAME_SUFFIX" },
   *     { "EXEC_ORDER": 8, "FELEM_CODE": "NAME_GEN" },
   *     { "EXEC_ORDER": 9, "FELEM_CODE": "CULTURE" },
   *     { "EXEC_ORDER": 10, "FELEM_CODE": "SCRIPT" },
   *     { "EXEC_ORDER": 11, "FELEM_CODE": "CATEGORY" },
   *     { "EXEC_ORDER": 12, "FELEM_CODE": "ORIG_GN" },
   *     { "EXEC_ORDER": 13, "FELEM_CODE": "META_GN" },
   *     { "EXEC_ORDER": 14, "FELEM_CODE": "ORIG_SN" },
   *     { "EXEC_ORDER": 15, "FELEM_CODE": "META_SN" },
   *     { "EXEC_ORDER": 16, "FELEM_CODE": "ORIG_ON" },
   *     { "EXEC_ORDER": 17, "FELEM_CODE": "META_ON" },
   *     { "EXEC_ORDER": 18, "FELEM_CODE": "STD_PRE" },
   *     { "EXEC_ORDER": 19, "FELEM_CODE": "STD_SUF" },
   *     { "EXEC_ORDER": 20, "FELEM_CODE": "STD_GEN" },
   *     { "EXEC_ORDER": 21, "FELEM_CODE": "ORIG_FN" },
   *     { "EXEC_ORDER": 22, "FELEM_CODE": "META_FN" }
   *   ]
   * }
   * </pre>
   *
   * @param configHandle The config handle identifying the in-memory
   *                     configuration to close.
   * @param inputJson The JSON text describing the feature comparison function
   *                  to retrieve.
   * @param response The {@link StringBuffer} to write the JSON response to.
   * @return Zero (0) on success and non-zero on failure.
   */
  int getFeatureComparisonFunctionCallV2(long         configHandle,
                                         String       inputJson,
                                         StringBuffer response);

  /**
   * Registers the feature comparison function described by the specified
   * JSON to the in-memory configuration associated with the specified config
   * handle.  The response JSON is written to the specified {@link
   * StringBuffer}.  The input JSON has the following format:
   * <pre>
   * {
   *   "FTYPE_CODE": "name_key",
   *   "CFCALL_ID": 789,
   *   "CFUNC_CODE": "ID_COMP",
   *   "EXEC_ORDER": 15,
   *   "BOM_ELEMENTS": [
   *     { "FELEM_CODE": "expression" }
   *   ]
   * }
   * </pre>
   * <p>
   * The response JSON provides the comparison function ID of the registered
   * feature comparison function:
   * <pre>
   *   {
   *     "CFCALL_ID": 789
   *   }
   * </pre>
   *
   * @param configHandle The config handle identifying the in-memory
   *                     configuration to close.
   * @param inputJson The JSON text describing the feature comparison function
   *                  to be registered.
   * @param response The {@link StringBuffer} to write the JSON response to.
   * @return Zero (0) on success and non-zero on failure.
   */
  int addFeatureComparisonFunctionCallV2(long         configHandle,
                                         String       inputJson,
                                         StringBuffer response);

  /**
   * Deletes/unregisters the feature comparison function described by the
   * specified JSON from the in-memory configuration associated with the
   * specified config handle.  The input JSON can identify the comparison
   * function to delete by function ID as follows:
   * <pre>
   *   {
   *     "CFCALL_ID": 1004
   *   }
   * </pre>
   * Alternatively, it can be identified by the feature type code:
   * <pre>
   *   {
   *     "FTYPE_CODE": "phone_key"
   *   }
   * </pre>
   *
   * @param configHandle The config handle identifying the in-memory
   *                     configuration to close.
   * @param inputJson The JSON text identifying the feature comparison function.
   * @return Zero (0) on success and non-zero on failure.
   */
  int deleteFeatureComparisonFunctionCallV2(long    configHandle,
                                            String  inputJson);

  /**
   * Adds an element to the feature comparison function described by the
   * specified JSON to the in-memory configuration associated with the
   * specified config handle.  The input JSON has the following format:
   * <pre>
   * {
   *   "FTYPE_CODE": "address",
   *   "FELEM_CODE": "ADDR4",
   *   "EXEC_ORDER": 34
   * }
   * </pre>
   * Alternatively, instead of the feature type code the function can be
   * identified by its function ID:
   * <pre>
   * {
   *   "CFCALL_ID": 5,
   *   "FELEM_CODE": "ADDR4",
   *   "EXEC_ORDER": 34
   * }
   * </pre>
   *
   * @param configHandle The config handle identifying the in-memory
   *                     configuration to close.
   * @param inputJson The JSON text describing the feature comparison function
   *                  element to be added.
   * @return Zero (0) on success and non-zero on failure.
   */
  int addFeatureComparisonFunctionCallElementV2(long    configHandle,
                                                String  inputJson);

  /**
   * Deletes an element from the feature comparison function described by the
   * specified JSON to the in-memory configuration associated with the
   * specified config handle.  The input JSON has the following format:
   * <pre>
   * {
   *   "FTYPE_CODE": "address",
   *   "FELEM_CODE": "ADDR5"
   * }
   * </pre>
   * Alternatively, instead of the feature type code the function can be
   * identified by its function ID:
   * <pre>
   * {
   *   "CFCALL_ID": 5,
   *   "FELEM_CODE": "ADDR5"
   * }
   * </pre>
   *
   * @param configHandle The config handle identifying the in-memory
   *                     configuration to close.
   * @param inputJson The JSON text describing the feature comparison function
   *                  element to be deleted.
   * @return Zero (0) on success and non-zero on failure.
   */
  int deleteFeatureComparisonFunctionCallElementV2(long   configHandle,
                                                   String inputJson);

  /**
   * Extracts the registered feature distinct functions from the in-memory
   * configuration associated with the specified config handle and writes JSON
   * text to the specified {@link StringBuffer} describing the feature
   * distinct functions from the configuration.  The format of the JSON
   * response is as follows:
   * <pre>
   * {
   *   "FEATURE_DISTINCT_FUNCTIONS": [
   *     { "DFUNC_ID": 1, "DFUNC_CODE": "FELEM_STRICT_SUBSET" },
   *     { "DFUNC_ID": 2, "DFUNC_CODE": "TOKEN_STRICT_SUBSET" },
   *     { "DFUNC_ID": 3, "DFUNC_CODE": "FELEM_NORM_STRICT_SUBSET" },
   *     { "DFUNC_ID": 4, "DFUNC_CODE": "PARTIAL_DATES" },
   *     { "DFUNC_ID": 5, "DFUNC_CODE": "PARTIAL_NAMES" },
   *     { "DFUNC_ID": 6, "DFUNC_CODE": "PARTIAL_ADDRESSES" }
   *   ]
   * }
   * </pre>
   *
   * @param configHandle The config handle identifying the in-memory
   *                     configuration to close.
   * @param response The {@link StringBuffer} to write the JSON response to.
   * @return Zero (0) on success and non-zero on failure.
   */
  int listFeatureDistinctFunctionCallsV2(long         configHandle,
                                         StringBuffer response);

  /**
   * Extracts a single feature distinct function call that is identified
   * by the specified input JSON from the in-memory configuration associated
   * with the specified config handle and writes the JSON text to the specified
   * {@link StringBuffer} describing the feature distinct function details.
   * The format of the input JSON is as follows:
   * <pre>
   * {
   *   "DFCALL_ID": 1
   * }
   * </pre>
   * <p>
   * The format of the JSON response is as follows:
   * <pre>
   * {
   *   "DFCALL_ID": 1,
   *   "DFUNC_CODE": "PARTIAL_NAMES",
   *   "FTYPE_CODE": "NAME",
   *   "BOM_ELEMENTS": [
   *     { "EXEC_ORDER": 1, "FELEM_CODE": "FULL_NAME" },
   *     { "EXEC_ORDER": 2, "FELEM_CODE": "ORG_NAME" },
   *     { "EXEC_ORDER": 3, "FELEM_CODE": "SUR_NAME" },
   *     { "EXEC_ORDER": 4, "FELEM_CODE": "GIVEN_NAME" },
   *     { "EXEC_ORDER": 5, "FELEM_CODE": "MIDDLE_NAME" },
   *     { "EXEC_ORDER": 6, "FELEM_CODE": "NAME_PREFIX" },
   *     { "EXEC_ORDER": 7, "FELEM_CODE": "NAME_SUFFIX" },
   *     { "EXEC_ORDER": 8, "FELEM_CODE": "NAME_GEN" },
   *     { "EXEC_ORDER": 10, "FELEM_CODE": "SCRIPT" },
   *     { "EXEC_ORDER": 11, "FELEM_CODE": "CATEGORY" },
   *     { "EXEC_ORDER": 12, "FELEM_CODE": "ALL_NM" },
   *     { "EXEC_ORDER": 13, "FELEM_CODE": "ALL_META" },
   *     { "EXEC_ORDER": 14, "FELEM_CODE": "NM_PAIR" },
   *     { "EXEC_ORDER": 15, "FELEM_CODE": "ORIG_GN" },
   *     { "EXEC_ORDER": 16, "FELEM_CODE": "META_GN" },
   *     { "EXEC_ORDER": 17, "FELEM_CODE": "ORIG_SN" },
   *     { "EXEC_ORDER": 18, "FELEM_CODE": "META_SN" },
   *     { "EXEC_ORDER": 19, "FELEM_CODE": "ORIG_ON" },
   *     { "EXEC_ORDER": 20, "FELEM_CODE": "META_ON" },
   *     { "EXEC_ORDER": 21, "FELEM_CODE": "STD_PRE" },
   *     { "EXEC_ORDER": 22, "FELEM_CODE": "STD_SUF" },
   *     { "EXEC_ORDER": 23, "FELEM_CODE": "STD_GEN" },
   *     { "EXEC_ORDER": 24, "FELEM_CODE": "ORIG_FN" },
   *     { "EXEC_ORDER": 25, "FELEM_CODE": "META_FN" }
   *   ]
   * }
   * </pre>
   *
   * @param configHandle The config handle identifying the in-memory
   *                     configuration to close.
   * @param inputJson The JSON text describing the feature distinct function
   *                  to retrieve.
   * @param response The {@link StringBuffer} to write the JSON response to.
   * @return Zero (0) on success and non-zero on failure.
   */
  int getFeatureDistinctFunctionCallV2(long         configHandle,
                                       String       inputJson,
                                       StringBuffer response);

  /**
   * Registers the feature distinct function described by the specified
   * JSON to the in-memory configuration associated with the specified config
   * handle.  The response JSON is written to the specified {@link
   * StringBuffer}.  The input JSON has the following format:
   * <pre>
   * {
   *   "FTYPE_CODE": "name_key",
   *   "DFCALL_ID": 789,
   *   "DFUNC_CODE": "FELEM_STRICT_SUBSET",
   *   "EXEC_ORDER": 15,
   *   "BOM_ELEMENTS": [
   *     { "FELEM_CODE": "expression" }
   *   ]
   * }
   * </pre>
   * <p>
   * The response JSON provides the comparison function ID of the registered
   * feature comparison function:
   * <pre>
   *   {
   *     "DFCALL_ID": 789
   *   }
   * </pre>
   *
   * @param configHandle The config handle identifying the in-memory
   *                     configuration to close.
   * @param inputJson The JSON text describing the feature distinct function
   *                  to be registered.
   * @param response The {@link StringBuffer} to write the JSON response to.
   * @return Zero (0) on success and non-zero on failure.
   */
  int addFeatureDistinctFunctionCallV2(long         configHandle,
                                       String       inputJson,
                                       StringBuffer response);

  /**
   * Deletes/unregisters the feature distinct function described by the
   * specified JSON from the in-memory configuration associated with the
   * specified config handle.  The input JSON can identify the distinct
   * function to delete by function ID as follows:
   * <pre>
   *   {
   *     "DFCALL_ID": 1004
   *   }
   * </pre>
   * Alternatively, it can be identified by the feature type code:
   * <pre>
   *   {
   *     "FTYPE_CODE": "phone_key"
   *   }
   * </pre>
   *
   * @param configHandle The config handle identifying the in-memory
   *                     configuration to close.
   * @param inputJson The JSON text identifying the feature distinct function.
   * @return Zero (0) on success and non-zero on failure.
   */
  int deleteFeatureDistinctFunctionCallV2(long configHandle, String inputJson);

  /**
   * Adds an element to the feature distinct function described by the
   * specified JSON to the in-memory configuration associated with the
   * specified config handle.  The input JSON has the following format:
   * <pre>
   * {
   *   "FTYPE_CODE": "address",
   *   "FELEM_CODE": "ADDR4",
   *   "EXEC_ORDER": 34
   * }
   * </pre>
   * Alternatively, instead of the feature type code the function can be
   * identified by its function ID:
   * <pre>
   * {
   *   "DFCALL_ID": 5,
   *   "FELEM_CODE": "ADDR4",
   *   "EXEC_ORDER": 34
   * }
   * </pre>
   *
   * @param configHandle The config handle identifying the in-memory
   *                     configuration to close.
   * @param inputJson The JSON text describing the feature distinct function
   *                  element to be added.
   * @return Zero (0) on success and non-zero on failure.
   */
  int addFeatureDistinctFunctionCallElementV2(long    configHandle,
                                              String  inputJson);

  /**
   * Deletes an element from the feature distinct function described by the
   * specified JSON to the in-memory configuration associated with the
   * specified config handle.  The input JSON has the following format:
   * <pre>
   * {
   *    "FTYPE_CODE": "address",
   *    "FELEM_CODE": "ADDR5"
   * }
   * </pre>
   * Alternatively, instead of the feature type code the function can be
   * identified by its function ID:
   * <pre>
   * {
   *   "DFCALL_ID": 5,
   *   "FELEM_CODE": "ADDR5"
   * }
   * </pre>
   *
   * @param configHandle The config handle identifying the in-memory
   *                     configuration to close.
   * @param inputJson The JSON text describing the feature distinct function
   *                  element to be deleted.
   * @return Zero (0) on success and non-zero on failure.
   */
  int deleteFeatureDistinctFunctionCallElementV2(long   configHandle,
                                                 String inputJson);

  /**
   * Extracts the attribute classes from the in-memory configuration associated
   * with the specified config handle and writes JSON text to the specified
   * {@link StringBuffer} describing the attribute classes from the
   * configuration.  The format of the JSON response is as follows:
   * <pre>
   * {
   *   "ATTRIBUTE_CLASSES": [
   *     { "ATTR_CLASS": "ADDRESS" },
   *     { "ATTR_CLASS": "ATTRIBUTE" },
   *     { "ATTR_CLASS": "IDENTIFIER" },
   *     { "ATTR_CLASS": "NAME" },
   *     { "ATTR_CLASS": "OBSERVATION" },
   *     { "ATTR_CLASS": "OTHER" },
   *     { "ATTR_CLASS": "PHONE" },
   *     { "ATTR_CLASS": "RELATIONSHIP" }
   *   ]
   * }
   * </pre>
   *
   * @param configHandle The config handle identifying the in-memory
   *                     configuration to close.
   * @param response The {@link StringBuffer} to write the JSON response to.
   * @return Zero (0) on success and non-zero on failure.
   */
  int listAttributeClassesV2(long configHandle, StringBuffer response);

  /**
   * Extracts the data sources from the in-memory configuration associated with
   * the specified config handle and writes JSON text to the specified
   * {@link StringBuffer} describing the data sources from the configuration.
   * The format of the JSON response is as follows:
   * <pre>
   * {
   *   "ATTRIBUTES": [
   *     { "ATTR_ID": 1001, "ATTR_CODE": "DATA_SOURCE" },
   *     { "ATTR_ID": 1002, "ATTR_CODE": "ROUTE_CODE" },
   *     {\"ATTR_ID\":1003,\"ATTR_CODE\":\"RECORD_ID\"},
   *     {\"ATTR_ID\":1004,\"ATTR_CODE\":\"ENTITY_TYPE\"},
   *     {\"ATTR_ID\":1005,\"ATTR_CODE\":\"ENTITY_KEY\"},
   *     {\"ATTR_ID\":1006,\"ATTR_CODE\":\"LOAD_ID\"},
   *     {\"ATTR_ID\":1007,\"ATTR_CODE\":\"DSRC_ACTION\"},
   *     {\"ATTR_ID\":1101,\"ATTR_CODE\":\"NAME_TYPE\"},
   *     {\"ATTR_ID\":1102,\"ATTR_CODE\":\"NAME_FULL\"},
   *     {\"ATTR_ID\":1103,\"ATTR_CODE\":\"NAME_ORG\"},
   *     {\"ATTR_ID\":1104,\"ATTR_CODE\":\"NAME_LAST\"},
   *     {\"ATTR_ID\":1105,\"ATTR_CODE\":\"NAME_FIRST\"},
   *     {\"ATTR_ID\":1106,\"ATTR_CODE\":\"NAME_MIDDLE\"},
   *     . . .
   *   ]
   * }
   * </pre>
   *
   * @param configHandle The config handle identifying the in-memory
   *                     configuration to close.
   * @param response The {@link StringBuffer} to write the JSON response to.
   * @return Zero (0) on success and non-zero on failure.
   */
  int listAttributesV2(long configHandle, StringBuffer response);

  /**
   * Extracts a single attribute that is identified by the specified input
   * JSON from the in-memory configuration associated with the specified
   * config handle and writes the JSON text to the specified {@link
   * StringBuffer} describing the attribute details.
   * The format of the input JSON is as follows:
   * <pre>
   * {
   *   "ATTR_CODE": "NAME_FIRST"
   * }
   * </pre>
   * <p>
   * The format of the JSON response is as follows:
   * <pre>
   * {
   *   "ATTR_ID": 1105,
   *   "ATTR_CODE": "NAME_FIRST",
   *   "ATTR_CLASS": "NAME",
   *   "FTYPE_CODE": "NAME",
   *   "FELEM_CODE": "GIVEN_NAME",
   *   "FELEM_REQ": "Any",
   *   "DEFAULT_VALUE": "",
   *   "ADVANCED": "No",
   *   "INTERNAL": "No"
   * }
   * </pre>
   *
   * @param configHandle The config handle identifying the in-memory
   *                     configuration to close.
   * @param inputJson The JSON text describing the attribute to retrieve.
   * @param response The {@link StringBuffer} to write the JSON response to.
   * @return Zero (0) on success and non-zero on failure.
   */
  int getAttributeV2(long         configHandle,
                     String       inputJson,
                     StringBuffer response);

  /**
   * Adds the attribute described by the specified JSON to the in-memory
   * configuration associated with the specified config handle.  The response
   * JSON is written to the specified {@link StringBuffer}.
   * The input JSON has the following format:
   * <pre>
   * {
   *   "ATTR_CODE": "attr-789",
   *   "ATTR_ID": 789,
   *   "ATTR_CLASS": "attribute",
   *   "FTYPE_CODE": "name",
   *   "FELEM_CODE": "latitude",
   *   "FELEM_REQ": "myReqValue",
   *   "DEFAULT_VALUE": "myDefault",
   *   "ADVANCED": "Y",
   *   "INTERNAL": 1
   * }
   * </pre>
   * <p>
   * The response JSON provides the attribute ID of the created attribute:
   * <pre>
   * {
   *   "ATTR_ID": 789
   * }
   * </pre>
   *
   * @param configHandle The config handle identifying the in-memory
   *                     configuration to close.
   * @param inputJson The JSON text describing the attribute to create.
   * @param response The {@link StringBuffer} to write the JSON response to.
   * @return Zero (0) on success and non-zero on failure.
   */
  int addAttributeV2(long         configHandle,
                     String       inputJson,
                     StringBuffer response);

  /**
   * Deletes the attribute described by the specified JSON from the
   * in-memory configuration associated with the specified config handle.
   * The input JSON has the following format:
   * <pre>
   * {
   *   "ATTR_CODE": "attr-789"
   * }
   * </pre>
   *
   * @param configHandle The config handle identifying the in-memory
   *                     configuration to close.
   * @param inputJson The JSON text describing the attribute to delete.
   * @return Zero (0) on success and non-zero on failure.
   */
  int deleteAttributeV2(long configHandle, String inputJson);

  /**
   * Extracts the resolution rules from the in-memory configuration associated
   * with the specified config handle and writes JSON text to the specified
   * {@link StringBuffer} describing the resolution rules from the
   * configuration.  The format of the JSON response is as follows:
   * <pre>
   * {
   *   "RULES": [
   *     { "ERRULE_ID": 99, "ERRULE_CODE": "GROUPER_KEY" },
   *     { "ERRULE_ID": 100, "ERRULE_CODE": "SAME_A1" },
   *     { "ERRULE_ID": 108, "ERRULE_CODE": "SF1_SNAME_CFF_CSTAB" },
   *     { "ERRULE_ID": 110, "ERRULE_CODE": "SF1_PNAME_CFF_CSTAB" },
   *     { "ERRULE_ID": 111, "ERRULE_CODE": "SF1_SNAME_CFF_CSTAB_DEXCL" },
   *     { "ERRULE_ID": 112, "ERRULE_CODE": "SF1_PNAME_CFF_OLD" },
   *     . . .
   *   ]
   * }
   * </pre>
   *
   * @param configHandle The config handle identifying the in-memory
   *                     configuration to close.
   * @param response The {@link StringBuffer} to write the JSON response to.
   * @return Zero (0) on success and non-zero on failure.
   */
  int listRulesV2(long configHandle, StringBuffer response);

  /**
   * Extracts a single resolution rule that is identified by the specified
   * input JSON from the in-memory configuration associated with the specified
   * config handle and writes the JSON text to the specified {@link
   * StringBuffer} describing the resolution rule details.
   * The format of the input JSON is as follows:
   * <pre>
   * {
   *   "ERRULE_CODE": "RULE-789"
   * }
   * </pre>
   * <p>
   * The format of the JSON response is as follows:
   * <pre>
   * {
   *   "ERRULE_ID": 789,
   *   "ERRULE_CODE": "RULE-789",
   *   "ERRULE_TIER": 56,
   *   "RESOLVE": "Yes",
   *   "RELATE": "Yes",
   *   "REF_SCORE": 27,
   *   "QUAL_ERFRAG_CODE": "CLOSE_FFES",
   *   "DISQ_ERFRAG_CODE": "NO_FF",
   *   "RTYPE_CODE": "POSSIBLY_SAME"
   * }
   * </pre>
   *
   *
   * @param configHandle The config handle identifying the in-memory
   *                     configuration to close.
   * @param inputJson The JSON text describing the resolution rule to retrieve.
   * @param response The {@link StringBuffer} to write the JSON response to.
   * @return Zero (0) on success and non-zero on failure.
   */
  int getRuleV2(long configHandle, String inputJson, StringBuffer response);

  /**
   * Adds the resolution rule described by the specified JSON to the in-memory
   * configuration associated with the specified config handle.  The response
   * JSON is written to the specified {@link StringBuffer}.
   * The input JSON has the following format:
   * <pre>
   * {
   *   "ERRULE_CODE": "rule-88",
   *   "RESOLVE": "N",
   *   "RELATE": "N",
   *   "REF_SCORE": 38,
   *   "RTYPE_CODE": "POSSIBLY_SAME",
   *   "QUAL_ERFRAG_CODE": "CLOSE_FFES",
   *   "DISQ_ERFRAG_CODE": "NO_FF",
   *   "ERRULE_TIER": 83
   * }
   * </pre>
   * <p>
   * The response JSON provides the resolution rule ID:
   * <pre>
   * {
   *   "ERRULE_ID": 1001
   * }
   * </pre>
   *
   * @param configHandle The config handle identifying the in-memory
   *                     configuration to close.
   * @param inputJson The JSON text describing the resolution rule to create.
   * @param response The {@link StringBuffer} to write the JSON response to.
   * @return Zero (0) on success and non-zero on failure.
   */
  int addRuleV2(long configHandle, String inputJson, StringBuffer response);


  /**
   * Deletes the resolution rule described by the specified JSON from the
   * in-memory configuration associated with the specified config handle.
   * The input JSON has the following format:
   * <pre>
   *   {
   *     "ERRULE_CODE": "rule-789"
   *   }
   * </pre>
   *
   * @param configHandle The config handle identifying the in-memory
   *                     configuration to close.
   * @param inputJson The JSON text describing the resolution rule to delete.
   * @return Zero (0) on success and non-zero on failure.
   */
  int deleteRuleV2(long configHandle, String inputJson);

  /**
   * Extracts the resolution rule fragments from the in-memory configuration
   * associated with the specified config handle and writes JSON text to the
   * specified {@link StringBuffer} describing the resolution rule fragments
   * from the configuration.  The format of the JSON response is as follows:
   * <pre>
   * {
   *   "RULE_FRAGMENTS": [
   *     { "ERFRAG_ID": 9, "ERFRAG_CODE": "GROUPER_KEY" },
   *     { "ERFRAG_ID": 10, "ERFRAG_CODE": "TRUSTED_ID" },
   *     { "ERFRAG_ID": 11, "ERFRAG_CODE": "SAME_NAME" },
   *     { "ERFRAG_ID": 12, "ERFRAG_CODE": "CLOSE_NAME" },
   *     { "ERFRAG_ID": 13, "ERFRAG_CODE": "LIKELY_NAME" },
   *     . . .
   *   ]
   * }
   * </pre>
   *
   * @param configHandle The config handle identifying the in-memory
   *                     configuration to close.
   * @param response The {@link StringBuffer} to write the JSON response to.
   * @return Zero (0) on success and non-zero on failure.
   */
  int listRuleFragmentsV2(long configHandle, StringBuffer response);

  /**
   * Extracts a single resolution rule fragment that is identified by the
   * specified input JSON from the in-memory configuration associated with the
   * specified config handle and writes the JSON text to the specified {@link
   * StringBuffer} describing the resolution rule fragment details.
   * The format of the input JSON is as follows:
   * <pre>
   * {
   *   "ERFRAG_CODE": "SUR_NAME"
   * }
   * </pre>
   * <p>
   * The format of the JSON response is as follows:
   * <pre>
   * {
   *   "ERFRAG_ID": 15,
   *   "ERFRAG_CODE": "SUR_NAME",
   *   "ERFRAG_SOURCE": "./SCORES/NAME[./GNR_SN&gt;=92]\",
   *   "ERFRAG_DEPENDS": ""
   * }
   * </pre>
   *
   * @param configHandle The config handle identifying the in-memory
   *                     configuration to close.
   * @param inputJson The JSON text describing the resolution rule fragment
   *                  to retrieve.
   * @param response The {@link StringBuffer} to write the JSON response to.
   * @return Zero (0) on success and non-zero on failure.
   */
  int getRuleFragmentV2(long          configHandle,
                        String        inputJson,
                        StringBuffer  response);

  /**
   * Adds the resolution rule fragment described by the specified JSON to the
   * in-memory configuration associated with the specified config handle.  The
   * response JSON is written to the specified {@link StringBuffer}.
   * The input JSON has the following format:
   * <pre>
   * {
   *   "ERFRAG_CODE": "fragment-789",
   *   "ERFRAG_ID": 789,
   *   "ERFRAG_SOURCE": "./SCORES/NAME[./GENERATION_MATCH = 0]\",
   *   "FRAGMENT_DEPENDENCIES": [
   *     { "ERFRAG_CODE": "CLOSE_F1" },
   *     { "ERFRAG_CODE": "CLOSE_F1E" },
   *     { "ERFRAG_CODE": "CLOSE_F1ES" }
   *   ]
   * }
   * </pre>
   * <p>
   * The response JSON provides the resolution rule fragment ID:
   * <pre>
   * {
   *   "ERFRAG_ID": 789
   * }
   * </pre>
   *
   * @param configHandle The config handle identifying the in-memory
   *                     configuration to close.
   * @param inputJson The JSON text describing the resolution rule fragment
   *                  to create.
   * @param response The {@link StringBuffer} to write the JSON response to.
   * @return Zero (0) on success and non-zero on failure.
   */
  int addRuleFragmentV2(long          configHandle,
                        String        inputJson,
                        StringBuffer  response);


  /**
   * Deletes the resolution rule fragment described by the specified JSON from
   * the in-memory configuration associated with the specified config handle.
   * The input JSON has the following format:
   * <pre>
   *   {
   *     "ERFRAG_CODE": "fragment-789"
   *   }
   * </pre>
   *
   * @param configHandle The config handle identifying the in-memory
   *                     configuration to close.
   * @param inputJson The JSON text describing the resolution rule fragment
   *                  to delete.
   * @return Zero (0) on success and non-zero on failure.
   */
  int deleteRuleFragmentV2(long configHandle, String inputJson);

  /**
   * Adds a config section described by the specified JSON to the in-memory
   * configuration associated with the specified config handle.
   * The input JSON has the following format:
   * <pre>
   * {
   *   "SECTION_NAME": "CUSTOM_TABLE_1"
   * }
   * </pre>
   *
   * @param configHandle The config handle identifying the in-memory
   *                     configuration to close.
   * @param inputJson The JSON text describing the config section to add.
   * @return Zero (0) on success and non-zero on failure.
   */
  int addConfigSectionV2(long configHandle, String inputJson);

  /**
   * Adds a config section field described by the specified JSON to the
   * in-memory configuration associated with the specified config handle.
   * The input JSON has the following format:
   * <pre>
   * {
   *   "SECTION_NAME": "CFG_RCLASS",
   *   "FIELD_NAME": "CUSTOM_SECTION_1",
   *   "FIELD_VALUE": 123
   * }
   * </pre>
   *
   * @param configHandle The config handle identifying the in-memory
   *                     configuration to close.
   * @param inputJson The JSON text describing the config section field to add.
   * @return Zero (0) on success and non-zero on failure.
   */
  int addConfigSectionFieldV2(long configHandle, String inputJson);

  /**
   * Gets the config compatibility version from the in-memory configuration
   * associated with the specified config handle and writes the JSON text to
   * the specified {@link StringBuffer} describing the compatibility version.
   * The format of the JSON response is as follows:
   * <pre>
   * {
   *   "COMPATIBILITY_VERSION": "9"
   * }
   * </pre>
   *
   * @param configHandle The config handle identifying the in-memory
   *                     configuration to close.
   * @param response The {@link StringBuffer} to write the JSON response to.
   * @return Zero (0) on success and non-zero on failure.
   */
  int getCompatibilityVersionV2(long configHandle, StringBuffer response);

  /**
   * Sets the config compatibility version in the in-memory configuration
   * associated with the specified config handle to the compatibility version
   * described in the specified JSON.  The format of the input JSON is as
   * follows:
   * <pre>
   * {
   *   "COMPATIBILITY_VERSION": "9"
   * }
   * </pre>
   *
   * @param configHandle The config handle identifying the in-memory
   *                     configuration to close.
   * @param inputJson The JSON text describing the new compatibility version.
   * @return Zero (0) on success and non-zero on failure.
   */
  int modifyCompatibilityVersionV2(long configHandle, String inputJson);
}

