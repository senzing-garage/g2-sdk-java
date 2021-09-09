package com.senzing.g2.engine.internal;
import com.senzing.g2.engine.G2Fallible;
import com.senzing.g2.engine.Result;

/**
 * Defines the Java interface to the G2 audit functions.  The G2 audit
 * functions primarily provide means of auditing the records and entities
 * in the repository.
 *
 * @deprecated
 */
public interface G2Audit extends G2Fallible {
  /**
   * Initializes the G2 Audit API with the specified module name,
   * init parameters and flag indicating verbose logging.  If the
   * <code>G2CONFIGFILE</code> init parameter is absent then the default
   * configuration from the repository is used.
   *
   * @param moduleName A short name given to this audit API instance
   * @param iniParams A JSON string containing configuration parameters
   * @param verboseLogging Enable diagnostic logging which will print a massive
   *                       amount of information to stdout
   *
   * @return Zero (0) on success, non-zero on failure.
   */
  int initV2(String moduleName, String iniParams, boolean verboseLogging);

  /**
   * Initializes the G2 Audit API with the module name, initialization
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
  int initWithConfigIDV2(String   moduleName,
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
  int reinitV2(long initConfigID);

  /**
   * Uninitializes the G2 audit API.
   *
   * @return Zero (0) on success, negative-one (-1) if the engine is not
   *         initialized.
   */
  int destroy();

  /**
   * Opens a new audit session and returns the session handle.  If a failure
   * occurs then a negative number is returned.
   *
   * @return The audit session handle or a negative number if a failure occurs.
   */
  long openSession();

  /**
   * Cancels an audit session and closes it even if it is in the middle of an
   * operation (especially {@link #getSummaryData(long,StringBuffer)}.
   *
   * @param session The session handle that was obtained from {@link
   *                #openSession()}.
   */
  void cancelSession(long session);

  /**
   * Closes an audit session that was previously opened with {@link
   * #openSession()}.
   *
   * @param session The session handle that was obtained from {@link
   *                #openSession()}.
   */
  void closeSession(long session);

  /**
   * Obtains an audit summary using the specified session handle previously
   * obtained from {@link #openSession()}.  The audit summary is provided as
   * a JSON document written to the specified {@link StringBuffer}.
   *
   * <pre>
   * {
   *   "SUMMARY_AUDIT_INFO": {
   * 	   "RESOLVED_ENTITY_COUNT": 6,
   * 		 "OBSERVED_ENTITY_COUNT": 12,
   * 		 "RECORD_COUNT": 12,
   * 		 "AUDIT_INFO": [
   *       {
   * 			   "FROM_DATA_SOURCE": "DATA_SOURCE_A",
   * 				 "TO_DATA_SOURCE": "DATA_SOURCE_B",
   * 			 	 "FROM_ENTITY_COUNT": 3,
   * 				 "TO_ENTITY_COUNT": 3,
   * 				 "FROM_SINGLE_COUNT": 0,
   * 				 "TO_SINGLE_COUNT": 0,
   * 				 "FROM_OBS_ENTITY_COUNT": 3,
   * 				 "TO_OBS_ENTITY_COUNT": 3,
   * 				 "FROM_RECORD_COUNT": 3,
   * 				 "TO_RECORD_COUNT": 3,
   * 				 "MATCHED_COUNT": 0,
   * 				 "MATCHED_ENTITY_COUNT": 0,
   * 				 "POSSIBLE_MATCH_COUNT": 3,
   * 				 "DISCOVERED_RELATIONSHIP_COUNT": 0,
   * 				 "DISCLOSED_RELATIONSHIP_COUNT": 0
   *       },
   *      . . .
   *     ]
   *   }
   * }
   * </pre>
   *
   * @param session The session handle that was obtained from {@link
   *                #openSession()}.
   * @param response The {@link StringBuffer} to write the audit summary JSON
   *                 to.
   * @return Zero (0) on success, non-zero on failure.
   */
  int getSummaryData(long session, StringBuffer response);

  /**
   * Obtains an audit summary without incurring the cost of generating an
   * audit session first.  This is a faster way of obtaining the audit summary
   * than first opening an audit session.  The audit summary is provided as
   * a JSON document written to the specified {@link StringBuffer}.
   *
   * <pre>
   * {
   *   "SUMMARY_AUDIT_INFO": {
   * 	   "RESOLVED_ENTITY_COUNT": 6,
   * 		 "OBSERVED_ENTITY_COUNT": 12,
   * 		 "RECORD_COUNT": 12,
   * 		 "AUDIT_INFO": [
   *       {
   * 			   "FROM_DATA_SOURCE": "DATA_SOURCE_A",
   * 				 "TO_DATA_SOURCE": "DATA_SOURCE_B",
   * 			 	 "FROM_ENTITY_COUNT": 3,
   * 				 "TO_ENTITY_COUNT": 3,
   * 				 "FROM_SINGLE_COUNT": 0,
   * 				 "TO_SINGLE_COUNT": 0,
   * 				 "FROM_OBS_ENTITY_COUNT": 3,
   * 				 "TO_OBS_ENTITY_COUNT": 3,
   * 				 "FROM_RECORD_COUNT": 3,
   * 				 "TO_RECORD_COUNT": 3,
   * 				 "MATCHED_COUNT": 0,
   * 				 "MATCHED_ENTITY_COUNT": 0,
   * 				 "POSSIBLE_MATCH_COUNT": 3,
   * 				 "DISCOVERED_RELATIONSHIP_COUNT": 0,
   * 				 "DISCLOSED_RELATIONSHIP_COUNT": 0
   *       },
   *      . . .
   *     ]
   *   }
   * }
   * </pre>
   *
   * @param response The {@link StringBuffer} to write the audit summary JSON
   *                 to.
   * @return Zero (0) on success, non-zero on failure.
   */
  int getSummaryDataDirect(StringBuffer response);

  /**
   * Using the specified audit session handle, obtains the match keys that are
   * used for entities connected by the two specified data sources for the given
   * match level.  Match levels are as follows:
   * <ul>
   *   <li>1: Match</li>
   *   <li>2: Possible Match</li>
   *   <li>3: Possibly Related</li>
   * </ul>
   *
   * <p>
   * The JSON response is formatted as follows:
   * <pre>
   * {
   * 	"DISTINCT_MATCH_KEY_INFO": {
   * 		"MATCH_KEY_INFO": [
   *            {
   * 				"MATCH_KEY": "+ADDRESS+SSN",
   * 				"COUNT": 2
   *      },
   *      {
   * 				"MATCH_KEY": "+SSN",
   * 				"COUNT": 6
   *      }
   * 		]
   * 	}
   * }
   * </pre>
   *
   * @param session The session handle that was obtained from {@link
   *                #openSession()}.
   * @param fromDataSource The "from" data source code.
   * @param toDataSource The "to" data source code.
   * @param matchLevel The match level.
   * @param response The {@link StringBuffer} to write the JSON response to.
   * @return Zero (0) on success, non-zero on failure.
   */
  int getUsedMatchKeys(long         session,
                       String       fromDataSource,
                       String       toDataSource,
                       int          matchLevel,
                       StringBuffer response);

  /**
   * Using the specified audit session handle, obtains the principles that are
   * used for entities connected by the two specified data sources for the given
   * match level.  Match levels are as follows:
   * <ul>
   *   <li>1: Match</li>
   *   <li>2: Possible Match</li>
   *   <li>3: Possibly Related</li>
   * </ul>
   *
   * <p>
   * The JSON response is formatted as follows:
   * <pre>
   * {
   * 	"DISTINCT_PRINCIPLE_INFO": {
   * 		"PRINCIPLE_INFO": [
   *            {
   * 				"PRINCIPLE": "DISCLOSED",
   * 				"COUNT": 8
   *      },
   *      {
   * 				"PRINCIPLE": "DISCLOSED",
   * 				"COUNT": 2
   *      },
   *      {
   * 				"PRINCIPLE": "SF1E",
   * 				"COUNT": 4
   *      }
   * 		]
   * 	}
   * }
   * </pre>
   *
   * @param session The session handle that was obtained from {@link
   *                #openSession()}.
   * @param fromDataSource The "from" data source code.
   * @param toDataSource The "to" data source code.
   * @param matchLevel The match level.
   * @param response The {@link StringBuffer} to write the JSON response to.
   * @return Zero (0) on success, non-zero on failure.
   */
  int getUsedPrinciples(long          session,
                        String        fromDataSource,
                        String        toDataSource,
                        int           matchLevel,
                        StringBuffer  response);

  /**
   * Uses the specified session handle obtained from {@link #openSession()} to
   * create an audit report for the entities connected at the specified match
   * level having the specified data sources.  A handle to the report is
   * returned and the pages of the report can be obtained via {@link
   * #fetchNextV2(long, StringBuffer)} (or {@link #fetchNext(long)}).
   * Match levels are as follows:
   * <ul>
   *   <li>1: Match</li>
   *   <li>2: Possible Match</li>
   *   <li>3: Possibly Related</li>
   * </ul>
   *
   * @param session The session handle that was obtained from {@link
   *                #openSession()}.
   * @param fromDataSource The "from" data source code.
   * @param toDataSource The "to" data source code.
   * @param matchLevel The match level.
   * @return A positive report handle on success and a negative number
   *         on failure.
   */
  long getAuditReport(long    session,
                      String  fromDataSource,
                      String  toDataSource,
                      int     matchLevel);

  /**
   * Uses the specified session handle obtained from {@link #openSession()} to
   * create an audit report for the entities connected at the specified match
   * level having the specified data sources.  A handle to the report is
   * returned and the pages of the report can be obtained via {@link
   * #fetchNextV2(long, StringBuffer)} (or {@link #fetchNext(long)}).
   *
   * @param session The session handle that was obtained from {@link
   *                #openSession()}.
   * @param fromDataSource The "from" data source code.
   * @param toDataSource The "to" data source code.
   * @param matchLevel The match level.
   * @param reportHandle The {@link Result} that will be set with the report
   *                     handle value on success.
   * @return Zero (0) on success, non-zero on failure.
   */
  int getAuditReportV2(long         session,
                       String       fromDataSource,
                       String       toDataSource,
                       int          matchLevel,
                       Result<Long> reportHandle);

  /**
   * This function is used to fetch the next audit group from an audit report
   * using the specified audit report handle.  The JSON text for the next
   * audit group is returned.  The audit group JSON is formatted as follows:
   * <pre>
   * {
   * 		"AUDIT_PAGE": {
   * 			"AUDIT_GROUPS": [
   *                {
   * 					"BASE_RECORD": [
   *            {
   * 							"ENTITY_ID": 1,
   * 							"PRINCIPLE": "SF1_PNAME_CSTAB",
   * 							"MATCH_KEY": "+NAME+SSN+SSN_LAST4",
   * 							"RECORD_ID": "test-1-2",
   * 							"ENTITY_TYPE": "TEST",
   * 							"DATA_SOURCE": "DSRC_TYPE_222",
   * 							"MATCH_TYPE": 1,
   * 							"REF_SCORE": 9,
   * 							"NAME_DATA": [
   * 								"Washington George"
   * 							],
   * 							"ATTRIBUTE_DATA": [],
   * 							"IDENTIFIER_DATA": [
   * 								"SSN_LAST4: 2623",
   * 								"SSN: 353-35-2623",
   * 								"SSN: 434-85-3685"
   * 							],
   * 							"ADDRESS_DATA": [],
   * 							"PHONE_DATA": [],
   * 							"RELATIONSHIP_DATA": [],
   * 							"ENTITY_DATA": [],
   * 							"OTHER_DATA": []
   *            }
   * 					],
   * 					"AUDIT_RECORD": [
   *            {
   * 							"ENTITY_ID": 1,
   * 							"PRINCIPLE": "SF1_PNAME_CSTAB",
   * 							"MATCH_KEY": "+NAME+SSN+SSN_LAST4",
   * 							"RECORD_ID": "test-1-3",
   * 							"ENTITY_TYPE": "TEST",
   * 							"DATA_SOURCE": "DSRC_TYPE_333",
   * 							"MATCH_TYPE": 1,
   * 							"REF_SCORE": 9,
   * 							"NAME_DATA": [
   * 								"Washington George"
   * 							],
   * 							"ATTRIBUTE_DATA": [],
   * 							"IDENTIFIER_DATA": [
   * 								"SSN_LAST4: 2623",
   * 								"SSN: 353-35-2623",
   * 								"SSN: 434-85-3685"
   * 							],
   * 							"ADDRESS_DATA": [],
   * 							"PHONE_DATA": [],
   * 							"RELATIONSHIP_DATA": [],
   * 							"ENTITY_DATA": [],
   * 							"OTHER_DATA": []
   *            }
   * 					],
   * 					"ECL_RECORD": [
   *            {
   * 							"ENTITY_ID": 1,
   * 							"PRINCIPLE": "",
   * 							"MATCH_KEY": "",
   * 							"RECORD_ID": "test-1-1",
   * 							"ENTITY_TYPE": "TEST",
   * 							"DATA_SOURCE": "DSRC_TYPE_111",
   * 							"MATCH_TYPE": 0,
   * 							"REF_SCORE": 0,
   * 							"NAME_DATA": [
   * 								"Washington George"
   * 							],
   * 							"ATTRIBUTE_DATA": [],
   * 							"IDENTIFIER_DATA": [
   * 								"SSN_LAST4: 2623",
   * 								"SSN: 353-35-2623",
   * 								"SSN: 434-85-3685"
   * 							],
   * 							"ADDRESS_DATA": [],
   * 							"PHONE_DATA": [],
   * 							"RELATIONSHIP_DATA": [],
   * 							"ENTITY_DATA": [],
   * 							"OTHER_DATA": []
   *            },
   *            {
   * 							"ENTITY_ID": 1,
   * 							"PRINCIPLE": "SF1_PNAME_CSTAB",
   * 							"MATCH_KEY": "+NAME+SSN+SSN_LAST4",
   * 							"RECORD_ID": "test-1-4",
   * 							"ENTITY_TYPE": "TEST",
   * 							"DATA_SOURCE": "DSRC_TYPE_444",
   * 							"MATCH_TYPE": 1,
   * 							"REF_SCORE": 9,
   * 							"NAME_DATA": [
   * 								"Washington George"
   * 							],
   * 							"ATTRIBUTE_DATA": [],
   * 							"IDENTIFIER_DATA": [
   * 								"SSN_LAST4: 2623",
   * 								"SSN: 353-35-2623",
   * 								"SSN: 434-85-3685"
   * 							],
   * 							"ADDRESS_DATA": [],
   * 							"PHONE_DATA": [],
   * 							"RELATIONSHIP_DATA": [],
   * 							"ENTITY_DATA": [],
   * 							"OTHER_DATA": []
   *            }
   * 					],
   * 					"AUDIT_KEY": "1",
   * 					"AUDIT_NAME": "Washington George",
   * 					"AUDIT_TYPE": "MATCH",
   * 					"FROM_DATA_SOURCE": "DSRC_TYPE_222",
   * 					"TO_DATA_SOURCE": "DSRC_TYPE_333"
   *        }
   * 			],
   * 			"FROM_DATA_SOURCE": "DSRC_TYPE_222",
   * 			"TO_DATA_SOURCE": "DSRC_TYPE_333",
   * 			"MATCH_LEVEL": 1,
   * 			"FIRST_RECORD_INDEX": 654321
   * 		}
   * }
   * </pre>
   *
   * @param reportHandle The report handle to retrieve data from
   *
   * @return Returns the {@link String} containing the JSON representation of
   *         the audit group.
   */
  String fetchNext(long reportHandle);

  /**
   * This function is used to fetch the next audit group from an audit report
   * using the specified audit report handle.  The JSON text for the next
   * audit group is written to the specified {@link StringBuffer}.  The audit
   * group JSON is formatted as follows:
   * <pre>
   * {
   * 		"AUDIT_PAGE": {
   * 			"AUDIT_GROUPS": [
   *                {
   * 					"BASE_RECORD": [
   *            {
   * 							"ENTITY_ID": 1,
   * 							"PRINCIPLE": "SF1_PNAME_CSTAB",
   * 							"MATCH_KEY": "+NAME+SSN+SSN_LAST4",
   * 							"RECORD_ID": "test-1-2",
   * 							"ENTITY_TYPE": "TEST",
   * 							"DATA_SOURCE": "DSRC_TYPE_222",
   * 							"MATCH_TYPE": 1,
   * 							"REF_SCORE": 9,
   * 							"NAME_DATA": [
   * 								"Washington George"
   * 							],
   * 							"ATTRIBUTE_DATA": [],
   * 							"IDENTIFIER_DATA": [
   * 								"SSN_LAST4: 2623",
   * 								"SSN: 353-35-2623",
   * 								"SSN: 434-85-3685"
   * 							],
   * 							"ADDRESS_DATA": [],
   * 							"PHONE_DATA": [],
   * 							"RELATIONSHIP_DATA": [],
   * 							"ENTITY_DATA": [],
   * 							"OTHER_DATA": []
   *            }
   * 					],
   * 					"AUDIT_RECORD": [
   *            {
   * 							"ENTITY_ID": 1,
   * 							"PRINCIPLE": "SF1_PNAME_CSTAB",
   * 							"MATCH_KEY": "+NAME+SSN+SSN_LAST4",
   * 							"RECORD_ID": "test-1-3",
   * 							"ENTITY_TYPE": "TEST",
   * 							"DATA_SOURCE": "DSRC_TYPE_333",
   * 							"MATCH_TYPE": 1,
   * 							"REF_SCORE": 9,
   * 							"NAME_DATA": [
   * 								"Washington George"
   * 							],
   * 							"ATTRIBUTE_DATA": [],
   * 							"IDENTIFIER_DATA": [
   * 								"SSN_LAST4: 2623",
   * 								"SSN: 353-35-2623",
   * 								"SSN: 434-85-3685"
   * 							],
   * 							"ADDRESS_DATA": [],
   * 							"PHONE_DATA": [],
   * 							"RELATIONSHIP_DATA": [],
   * 							"ENTITY_DATA": [],
   * 							"OTHER_DATA": []
   *            }
   * 					],
   * 					"ECL_RECORD": [
   *            {
   * 							"ENTITY_ID": 1,
   * 							"PRINCIPLE": "",
   * 							"MATCH_KEY": "",
   * 							"RECORD_ID": "test-1-1",
   * 							"ENTITY_TYPE": "TEST",
   * 							"DATA_SOURCE": "DSRC_TYPE_111",
   * 							"MATCH_TYPE": 0,
   * 							"REF_SCORE": 0,
   * 							"NAME_DATA": [
   * 								"Washington George"
   * 							],
   * 							"ATTRIBUTE_DATA": [],
   * 							"IDENTIFIER_DATA": [
   * 								"SSN_LAST4: 2623",
   * 								"SSN: 353-35-2623",
   * 								"SSN: 434-85-3685"
   * 							],
   * 							"ADDRESS_DATA": [],
   * 							"PHONE_DATA": [],
   * 							"RELATIONSHIP_DATA": [],
   * 							"ENTITY_DATA": [],
   * 							"OTHER_DATA": []
   *            },
   *            {
   * 							"ENTITY_ID": 1,
   * 							"PRINCIPLE": "SF1_PNAME_CSTAB",
   * 							"MATCH_KEY": "+NAME+SSN+SSN_LAST4",
   * 							"RECORD_ID": "test-1-4",
   * 							"ENTITY_TYPE": "TEST",
   * 							"DATA_SOURCE": "DSRC_TYPE_444",
   * 							"MATCH_TYPE": 1,
   * 							"REF_SCORE": 9,
   * 							"NAME_DATA": [
   * 								"Washington George"
   * 							],
   * 							"ATTRIBUTE_DATA": [],
   * 							"IDENTIFIER_DATA": [
   * 								"SSN_LAST4: 2623",
   * 								"SSN: 353-35-2623",
   * 								"SSN: 434-85-3685"
   * 							],
   * 							"ADDRESS_DATA": [],
   * 							"PHONE_DATA": [],
   * 							"RELATIONSHIP_DATA": [],
   * 							"ENTITY_DATA": [],
   * 							"OTHER_DATA": []
   *            }
   * 					],
   * 					"AUDIT_KEY": "1",
   * 					"AUDIT_NAME": "Washington George",
   * 					"AUDIT_TYPE": "MATCH",
   * 					"FROM_DATA_SOURCE": "DSRC_TYPE_222",
   * 					"TO_DATA_SOURCE": "DSRC_TYPE_333"
   *        }
   * 			],
   * 			"FROM_DATA_SOURCE": "DSRC_TYPE_222",
   * 			"TO_DATA_SOURCE": "DSRC_TYPE_333",
   * 			"MATCH_LEVEL": 1,
   * 			"FIRST_RECORD_INDEX": 654321
   * 		}
   * }
   * </pre>
   *
   * @param reportHandle The report handle to retrieve data from
   * @param response The {@link StringBuffer} to write the JSON response to.
   * @return Zero (0) on success, non-zero on failure.
   */
  int fetchNextV2(long reportHandle, StringBuffer response);

  /**
   * This function closes the report handle that was previously obtained from
   * {@link #openSession()} and subsequently releases the associated system
   * resources.
   *
   * @param reportHandle The report handle to retrieve data from
   */
  void closeReport(long reportHandle);

  /**
   * This function closes the report handle that was previously obtained from
   * {@link #openSession()} and subsequently releases the associated system
   * resources.
   *
   * @param reportHandle The report handle to retrieve data from
   * @return Zero (0) on success, non-zero on failure.
   */
  int closeReportV2(long reportHandle);
}
