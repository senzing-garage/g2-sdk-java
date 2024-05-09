package com.senzing.g2.engine;

import java.util.Set;

/**
 * 
 */
public interface SzEngine {
    /**
     * May optionally be called to pre-initialize some of the heavier weight
     * internal resources of the {@link SzEngine}.
     *
     * @throws SzException If a failure occurs.
     */
    void primeEngine() throws SzException;

    /**
     * Returns the current internal engine workload statistics for the process.
     * The counters are reset after each call.
     *
     * @return The {@link String} describing the statistics as JSON.
     * 
     * @throws SzException If a failure occurs.
     */
    String getStats() throws SzException;

    /**
     * Loads the record described by the specified {@link String} record
     * definition having the specified data source code and record ID using
     * the specified {@link Set} of {@link SzFlag} values.  If a record already
     * exists with the same data source code and record ID, then it will be replaced.
     * <p>
     * The specified JSON data may optionally contain the <code>DATA_SOURCE</code>
     * and <code>RECORD_ID</code> properties, but, if so, they must match the
     * specified parameters.
     * <p>
     * The specified {@link Set} of {@link SzFlag} instances may contain any 
     * {@link SzFlag} value, but only flags belonging to the {@link
     * SzFlagUsageGroup#SZ_MODIFY} group will be recognized (other {@link SzFlag}
     * instances will be ignored).  <b>NOTE:</b> {@link java.util.EnumSet}
     * offers an efficient means of constructing a {@link Set} of {@link SzFlag}.
     *
     * @param dataSourceCode The data source code for the record to be added.
     * 
     * @param recordId  The record ID for the record to be added.
     * 
     * @param recordDefinition The {@link String} that defines the record, typically
     *                         in JSON format.
     * 
     * @param flags The optional {@link Set} of {@link SzFlag} instances belonging
     *              to the {@link SzFlagUsageGroup#SZ_MODIFY} group to control how
     *              the operation is performed and the content of the response, or
     *              <code>null</code> to default to {@link SzFlag#SZ_NO_FLAGS}.
     *              Specify {@link SzFlag#SZ_WITH_INFO_FLAGS} for an INFO response.
     * 
     * @return The JSON {@link String} result produced by adding the
     *         record to the repository (depending on the specified flags).
     * 
     * @throws SzUnknownDataSourceException If an unrecognized data source
     *                                      code is specified.
     * 
     * @throws SzBadInputException If the specified record definition has a data source
     *                             or record ID value that conflicts with the specified
     *                             data source code and/or record ID values.
     * 
     * @throws SzException If a failure occurs.
     * 
     * @see #addRecord(SzRecordKey, String, Set)
     * 
     * @see SzFlag#SZ_WITH_INFO_FLAGS
     * @see SzFlagUsageGroup#SZ_MODIFY
     */
    String addRecord(String         dataSourceCode, 
                     String         recordId, 
                     String         recordDefinition,
                     Set<SzFlag>    flags)
        throws SzUnknownDataSourceException, SzBadInputException, SzException;


    /**
     * Loads the record described by the specified {@link String} record
     * definition having the specified data source code and record ID using
     * the specified {@link Set} of {@link SzFlag} values.  If a record already
     * exists with the same data source code and record ID, then it will be replaced.
     * <p>
     * The specified JSON data may optionally contain the <code>DATA_SOURCE</code>
     * and <code>RECORD_ID</code> properties, but, if so, they must match the
     * specified parameters.
     * <p>
     * The specified {@link Set} of {@link SzFlag} instances may contain any 
     * {@link SzFlag} value, but only flags belonging to the {@link
     * SzFlagUsageGroup#SZ_MODIFY} group will be recognized (other {@link SzFlag}
     * instances will be ignored).  <b>NOTE:</b> {@link java.util.EnumSet}
     * offers an efficient means of constructing a {@link Set} of {@link SzFlag}.
     *
     * @param recordKey The non-null {@link SzRecordKey} that specifies the
     *                  data source code and record ID of the record being added.
     * 
     * @param recordDefinition The {@link String} that defines the record, typically
     *                         in JSON format.
     * 
     * @param flags The optional {@link Set} of {@link SzFlag} instances belonging
     *              to the {@link SzFlagUsageGroup#SZ_MODIFY} group to control how
     *              the operation is performed and the content of the response, or
     *              <code>null</code> to default to {@link SzFlag#SZ_NO_FLAGS}.
     *              Specify {@link SzFlag#SZ_WITH_INFO_FLAGS} for an INFO response.
     * 
     * @return The JSON {@link String} result produced by adding the
     *         record to the repository (depending on the specified flags).
     * 
     * @throws SzUnknownDataSourceException If an unrecognized data source
     *                                      code is specified.
     * 
     * @throws SzBadInputException If the specified record definition has a data source
     *                             or record ID value that conflicts with the specified
     *                             data source code and/or record ID values.
     * 
     * @throws SzException If a failure occurs.
     * 
     * @see #addRecord(String, String, String, Set)
     * 
     * @see SzFlag#SZ_WITH_INFO_FLAGS
     * @see SzFlagUsageGroup#SZ_MODIFY
     */
    String addRecord(SzRecordKey        recordKey,
                     String             recordDefinition,
                     Set<SzFlag>        flags)
        throws SzUnknownDataSourceException, SzBadInputException, SzException;

    /**
     * Delete a previously loaded record identified by the specified data
     * source code and record ID.  This method is idempotent, meaning multiple
     * calls this method with the same method will all succeed regardless of
     * whether or not the record is found in the repository.
     * <p>
     * The specified {@link Set} of {@link SzFlag} instances may contain any 
     * {@link SzFlag} value, but only flags belonging to the {@link
     * SzFlagUsageGroup#SZ_MODIFY} group will be recognized (other {@link SzFlag}
     * instances will be ignored).  <b>NOTE:</b> {@link java.util.EnumSet}
     * offers an efficient means of constructing a {@link Set} of {@link SzFlag}.
     *
     * @param dataSourceCode The data source code of the record to delete.
     * 
     * @param recordId The record ID of the record to delete.
     * 
     * @param flags The optional {@link Set} of {@link SzFlag} instances belonging
     *              to the {@link SzFlagUsageGroup#SZ_MODIFY} group to control how
     *              the operation is performed and the content of the response, or
     *              <code>null</code> to default to {@link SzFlag#SZ_NO_FLAGS}.
     *              Specify {@link SzFlag#SZ_WITH_INFO_FLAGS} for an INFO response.
     *
     * @return The JSON {@link String} result produced by deleting the
     *         record from the repository (depending on the specified flags).
     * 
     * @throws SzUnknownDataSourceException If an unrecognized data source
     *                                      code is specified.
     * 
     * @throws SzException If a failure occurs.
     * 
     * @see #deleteRecord(SzRecordKey, Set)
     * 
     * @see SzFlag#SZ_WITH_INFO_FLAGS
     * @see SzFlagUsageGroup#SZ_MODIFY
     */
    String deleteRecord(String          dataSourceCode,
                        String          recordId,
                        Set<SzFlag>     flags)
        throws SzUnknownDataSourceException, SzException;

    /**
     * Delete a previously loaded record identified by the data source
     * code and record ID from the specified {@link SzRecordKey}.  This
     * method is idempotent, meaning multiple calls this method with the
     * same method will all succeed regardless of whether or not the
     * record is found in the repository.
     * <p>
     * The specified {@link Set} of {@link SzFlag} instances may contain any 
     * {@link SzFlag} value, but only flags belonging to the {@link
     * SzFlagUsageGroup#SZ_MODIFY} group will be recognized (other {@link SzFlag}
     * instances will be ignored).  <b>NOTE:</b> {@link java.util.EnumSet}
     * offers an efficient means of constructing a {@link Set} of {@link SzFlag}.
     *
     * @param recordKey The non-null {@link SzRecordKey} that specifies the
     *                  data source code and record Id of the record to delete.
     * 
     * @param flags The optional {@link Set} of {@link SzFlag} instances belonging
     *              to the {@link SzFlagUsageGroup#SZ_MODIFY} group to control how
     *              the operation is performed and the content of the response, or
     *              <code>null</code> to default to {@link SzFlag#SZ_NO_FLAGS}.
     *              Specify {@link SzFlag#SZ_WITH_INFO_FLAGS} for an INFO response.
     *
     * @return The JSON {@link String} result produced by deleting the
     *         record from the repository (depending on the specified flags).
     * 
     * @throws SzUnknownDataSourceException If an unrecognized data source
     *                                      code is specified.
     * 
     * @throws SzException If a failure occurs.
     * 
     * @see #deleteRecord(String, String, Set)
     * 
     * @see SzFlag#SZ_WITH_INFO_FLAGS
     * @see SzFlagUsageGroup#SZ_MODIFY
     */
    String deleteRecord(SzRecordKey recordKey, Set<SzFlag> flags)
        throws SzUnknownDataSourceException, SzException;

    /**
     * Reevaluate the record identified by the specified data source code and
     * record ID.  If the record is not found then TODO(bcaceres): complete this
     * when GDEV-3790 gets resolved and the "not found" behavior is defined.
     * <p>
     * The specified {@link Set} of {@link SzFlag} instances may contain any 
     * {@link SzFlag} value, but only flags belonging to the {@link
     * SzFlagUsageGroup#SZ_MODIFY} group will be recognized (other {@link SzFlag}
     * instances will be ignored).  <b>NOTE:</b> {@link java.util.EnumSet}
     * offers an efficient means of constructing a {@link Set} of {@link SzFlag}.
     *
     * @param dataSourceCode The data source of the record to reevaluate.
     * 
     * @param recordId The record ID of the record to reevaluate.
     * 
     * @param flags The optional {@link Set} of {@link SzFlag} instances belonging
     *              to the {@link SzFlagUsageGroup#SZ_MODIFY} group to control how
     *              the operation is performed and the content of the response, or
     *              <code>null</code> to default to {@link SzFlag#SZ_NO_FLAGS}.
     *              Specify {@link SzFlag#SZ_WITH_INFO_FLAGS} for an INFO response.
     *
     * @return The JSON {@link String} result produced by reevaluating the
     *         record in the repository (depending on the specified flags).
     *
     * @throws SzUnknownDataSourceException If an unrecognized data source
     *                                      code is specified.
     * 
     * @throws SzException If a failure occurs.
     *  
     * @see SzFlag#SZ_WITH_INFO_FLAGS
     * @see SzFlagUsageGroup#SZ_MODIFY
     */
    String reevaluateRecord(String      dataSourceCode,
                            String      recordId,
                            Set<SzFlag> flags)
    throws SzUnknownDataSourceException, SzException;

    /**
     * Reevaluate the record identified by the data source code and record ID
     * from the specified {@link SzRecordKey}.  If the record is not found then
     * TODO(bcaceres): complete this when GDEV-3790 gets resolved and the
     * "not found" behavior is defined.
     * <p>
     * The specified {@link Set} of {@link SzFlag} instances may contain any 
     * {@link SzFlag} value, but only flags belonging to the {@link
     * SzFlagUsageGroup#SZ_MODIFY} group will be recognized (other {@link SzFlag}
     * instances will be ignored).  <b>NOTE:</b> {@link java.util.EnumSet}
     * offers an efficient means of constructing a {@link Set} of {@link SzFlag}.
     *
     * @param recordKey The non-null {@link SzRecordKey} that specifies the
     *                  data source code and record Id of the record to reevaluate.
     * 
     * @param flags The optional {@link Set} of {@link SzFlag} instances belonging
     *              to the {@link SzFlagUsageGroup#SZ_MODIFY} group to control how
     *              the operation is performed and the content of the response, or
     *              <code>null</code> to default to {@link SzFlag#SZ_NO_FLAGS}.
     *              Specify {@link SzFlag#SZ_WITH_INFO_FLAGS} for an INFO response.
     *
     * @return The JSON {@link String} result produced by reevaluating the
     *         record in the repository (depending on the specified flags).
     *
     * @throws SzUnknownDataSourceException If an unrecognized data source
     *                                      code is specified.
     * 
     * @throws SzException If a failure occurs.
     * 
     * @see #reevaluateRecord(String, String, Set)
     * 
     * @see SzFlag#SZ_WITH_INFO_FLAGS
     * @see SzFlagUsageGroup#SZ_MODIFY
     */
    String reevaluateRecord(SzRecordKey recordKey, Set<SzFlag> flags)
        throws SzUnknownDataSourceException, SzException;

    /**
     * Reevaluate a resolved entity identified by the specified entity ID.
     * If the entity is not found then TODO(bcaceres): complete this when
     * GDEV-3790 gets resolved and the "not found" behavior is defined.
     * <p>
     * The specified {@link Set} of {@link SzFlag} instances may contain any 
     * {@link SzFlag} value, but only flags belonging to the {@link
     * SzFlagUsageGroup#SZ_MODIFY} group will be recognized (other {@link SzFlag}
     * instances will be ignored).  <b>NOTE:</b> {@link java.util.EnumSet}
     * offers an efficient means of constructing a {@link Set} of {@link SzFlag}.
     *
     * @param entityId The ID of the resolved entity to reevaluate.
     * 
     * @param flags The optional {@link Set} of {@link SzFlag} instances belonging
     *              to the {@link SzFlagUsageGroup#SZ_MODIFY} group to control how
     *              the operation is performed and the content of the response, or
     *              <code>null</code> to default to {@link SzFlag#SZ_NO_FLAGS}.
     *              Specify {@link SzFlag#SZ_WITH_INFO_FLAGS} for an INFO response.
     *
     * @return The JSON {@link String} result produced by reevaluating the
     *         entity in the repository (depending on the specified flags).
     * 
     * @throws SzException If a failure occurs.
     * 
     * @see SzFlag#SZ_WITH_INFO_FLAGS
     * @see SzFlagUsageGroup#SZ_MODIFY
     */
    String reevaluateEntity(long entityId, Set<SzFlag> flags)
        throws SzException;

    /**
     * This method searches for entities that match or relate to the provided
     * search attributes using the optionally specified search profile.  The
     * specified search attributes are treated as a hypothetical record and 
     * the search results are those entities that would match or relate to 
     * that hypothetical record on some level (depending on the specified flags).
     * <p>
     * If the specified search profile is <code>null</code> then the default
     * generic thresholds from the default search profile will be used for the
     * search (alternatively, use {@link #searchByAttributes(String, Set)} to 
     * omit the parameter).  If your search requires different behavior using
     * alternate generic thresholds, please contact 
     * <a href="mailto:support@senzing.com">support@senzing.com</a> for details
     * on configuring a custom search profile.
     * <p>
     * The specified {@link Set} of {@link SzFlag} instances may contain any 
     * {@link SzFlag} value, but only flags belonging to the {@link
     * SzFlagUsageGroup#SZ_SEARCH} group are guaranteed to be recognized (other
     * {@link SzFlag} instances will be ignored unless they have equivalent bit
     * flags).
     * <p>
     * <b>NOTE:</b> {@link java.util.EnumSet} offers an efficient means of
     * constructing a {@link Set} of {@link SzFlag}.
     * 
     * @param attributes The search attributes defining the hypothetical record
     *                   to match and/or relate to in order to obtain the
     *                   search results.
     * 
     * @param searchProfile The optional search profile identifier, or 
     *                      <code>null</code> if the default search profile
     *                      should be used for the search.
     * 
     * @param flags The optional {@link Set} of {@link SzFlag} instances belonging
     *              to the {@link SzFlagUsageGroup#SZ_SEARCH} group to control how
     *              the operation is performed and the content of the response, or
     *              <code>null</code> to default to {@link SzFlag#SZ_NO_FLAGS}
     *              or {@link SzFlag#SZ_SEARCH_BY_ATTRIBUTES_DEFAULT_FLAGS} for
     *              the default recommended flags.
     * 
     * @return The resulting JSON {@link String} describing the result of the search.
     *
     * @throws SzException If a failure occurs.
     * 
     * @see #searchByAttributes(String, Set)
     * 
     * @see SzFlag#SZ_SEARCH_BY_ATTRIBUTES_DEFAULT_FLAGS
     * @see SzFlag#SZ_SEARCH_BY_ATTRIBUTES_ALL
     * @see SzFlag#SZ_SEARCH_BY_ATTRIBUTES_STRONG
     * @see SzFlag#SZ_SEARCH_BY_ATTRIBUTES_MINIMAL_ALL
     * @see SzFlag#SZ_SEARCH_BY_ATTRIBUTES_MINIMAL_STRONG
     * @see SzFlagUsageGroup#SZ_SEARCH
     */
    String searchByAttributes(String        attributes, 
                              String        searchProfile,
                              Set<SzFlag>   flags)
        throws SzException;

    /**
     * This method is equivalent to calling {@link 
     * #searchByAttributes(String, String, Set)} with a <code>null</code> value
     * for the search profile parameter.  See {@link 
     * #searchByAttributes(String, String, Set)} documentation for details.
     * 
     * @param attributes The search attributes defining the hypothetical record
     *                   to match and/or relate to in order to obtain the
     *                   search results.
     * 
     * @param flags The optional {@link Set} of {@link SzFlag} instances belonging
     *              to the {@link SzFlagUsageGroup#SZ_SEARCH} group to control how
     *              the operation is performed and the content of the response, or
     *              <code>null</code> to default to {@link SzFlag#SZ_NO_FLAGS}
     *              or {@link SzFlag#SZ_SEARCH_BY_ATTRIBUTES_DEFAULT_FLAGS} for
     *              the default recommended flags.
     * 
     * @return The resulting JSON {@link String} describing the result of the search.
     *
     * @throws SzException If a failure occurs.
     * 
     * @see #searchByAttributes(String, String, Set)
     * 
     * @see SzFlag#SZ_SEARCH_BY_ATTRIBUTES_DEFAULT_FLAGS
     * @see SzFlag#SZ_SEARCH_BY_ATTRIBUTES_ALL
     * @see SzFlag#SZ_SEARCH_BY_ATTRIBUTES_STRONG
     * @see SzFlag#SZ_SEARCH_BY_ATTRIBUTES_MINIMAL_ALL
     * @see SzFlag#SZ_SEARCH_BY_ATTRIBUTES_MINIMAL_STRONG
     * @see SzFlagUsageGroup#SZ_SEARCH
     */
    String searchByAttributes(String attributes, Set<SzFlag> flags)
        throws SzException;

    /**
     * This method is used to retrieve information about a specific resolved
     * entity. The result is returned as a JSON document describing the entity.
     * The level of detail provided for the entity depends upon the specified
     * {@link Set} of {@link SzFlag} instances.
     * <p>
     * The specified {@link Set} of {@link SzFlag} instances may contain any 
     * {@link SzFlag} value, but only flags belonging to the {@link
     * SzFlagUsageGroup#SZ_ENTITY} group are guaranteed to be recognized 
     * (other {@link SzFlag} instances will be ignored unless they have
     * equivalent bit flags).
     * <p>
     * <b>NOTE:</b> {@link java.util.EnumSet} offers an efficient means of
     * constructing a {@link Set} of {@link SzFlag}.
     * 
     * @param entityId The entity ID identifying the entity to retrieve.
     * 
     * @param flags The optional {@link Set} of {@link SzFlag} instances belonging
     *              to the {@link SzFlagUsageGroup#SZ_ENTITY} group to control how
     *              the operation is performed and the content of the response, or
     *              <code>null</code> to default to {@link SzFlag#SZ_NO_FLAGS}
     *              or {@link SzFlag#SZ_ENTITY_DEFAULT_FLAGS} for the default
     *              recommended flags.
     *
     * @return The JSON {@link String} describing the entity.
     * 
     * @throws SzNotFoundException If no enitty could be found with the
     *                             specified entity ID.
     * @throws SzException If a failure occurs.
     * 
     * @see SzFlag#SZ_ENTITY_DEFAULT_FLAGS
     * @see SzFlag#SZ_ENTITY_BRIEF_DEFAULT_FLAGS
     * @see SzFlagUsageGroup#SZ_ENTITY
     */
    String getEntity(long entityId, Set<SzFlag> flags)
        throws SzNotFoundException, SzException;

    /**
     * This method is used to retrieve information about the resolved entity
     * that contains a specific record that is identified by the specified
     * data source code and record ID.  The result is returned as a JSON
     * document describing the entity.  The level of detail provided for the
     * entity depends upon the specified {@link Set} of {@link SzFlag}
     * instances.
     * <p>
     * The specified {@link Set} of {@link SzFlag} instances may contain any 
     * {@link SzFlag} value, but only flags belonging to the {@link
     * SzFlagUsageGroup#SZ_ENTITY} group are guaranteed to be recognized (other
     * {@link SzFlag} instances will be ignored unless they have equivalent bit
     * flags).
     * <p>
     * <b>NOTE:</b> {@link java.util.EnumSet} offers an efficient means of
     * constructing a {@link Set} of {@link SzFlag}.
     * 
     * @param dataSourceCode The data source code of the constituent record for 
     *                       the entity to retrieve.
     * 
     * @param recordId The record ID of the constituent record for the entity
     *                 to retrieve.
     * 
     * @param flags The optional {@link Set} of {@link SzFlag} instances belonging
     *              to the {@link SzFlagUsageGroup#SZ_ENTITY} group to control how
     *              the operation is performed and the content of the response, or
     *              <code>null</code> to default to {@link SzFlag#SZ_NO_FLAGS}
     *              or {@link SzFlag#SZ_ENTITY_DEFAULT_FLAGS} for the default
     *              recommended flags.
     * 
     * @return The JSON {@link String} describing the entity.
     * 
     * @throws SzUnknownDataSourceException If an unrecognized data source
     *                                      code is specified.
     * 
     * @throws SzNotFoundException If no enitty could be found with the
     *                             specified entity ID.
     * 
     * @throws SzException If a failure occurs.
     * 
     * @see #getEntity(String,String,Set)
     * 
     * @see SzFlag#SZ_ENTITY_DEFAULT_FLAGS
     * @see SzFlag#SZ_ENTITY_BRIEF_DEFAULT_FLAGS
     * @see SzFlagUsageGroup#SZ_ENTITY
     */
    String getEntity(String dataSourceCode, String recordId, Set<SzFlag> flags)
        throws SzUnknownDataSourceException, SzNotFoundException, SzException;

    /**
     * This method is used to retrieve information about the resolved entity
     * that contains a specific record that is identified by the data source
     * code and record ID associated with the specified {@link SzRecordKey}.
     * The result is returned as a JSON document describing the entity.  The
     * level of detail provided for the entity depends upon the specified
     * {@link Set} of {@link SzFlag} instances.
     * <p>
     * The specified {@link Set} of {@link SzFlag} instances may contain any 
     * {@link SzFlag} value, but only flags belonging to the {@link
     * SzFlagUsageGroup#SZ_ENTITY} group are guaranteed to be recognized (other
     * {@link SzFlag} instances will be ignored unless they have equivalent bit
     * flags).
     * <p>
     * <b>NOTE:</b> {@link java.util.EnumSet} offers an efficient means of
     * constructing a {@link Set} of {@link SzFlag}.
     * 
     * @param recordKey The non-null {@link SzRecordKey} that specifies the
     *                  data source code and record Id of the consituent record
     *                  for the entity to retrieve.
     * 
     * @param flags The optional {@link Set} of {@link SzFlag} instances belonging
     *              to the {@link SzFlagUsageGroup#SZ_ENTITY} group to control how
     *              the operation is performed and the content of the response, or
     *              <code>null</code> to default to {@link SzFlag#SZ_NO_FLAGS}
     *              or {@link SzFlag#SZ_ENTITY_DEFAULT_FLAGS} for the default
     *              recommended flags.
     * 
     * @return The JSON {@link String} describing the entity.
     * 
     * @throws SzUnknownDataSourceException If an unrecognized data source
     *                                      code is specified.
     * 
     * @throws SzNotFoundException If no enitty could be found with the
     *                             specified entity ID.
     * 
     * @throws SzException If a failure occurs.
     * 
     * @see #getEntity(String,String,Set)
     * 
     * @see SzFlag#SZ_ENTITY_DEFAULT_FLAGS
     * @see SzFlag#SZ_ENTITY_BRIEF_DEFAULT_FLAGS
     * @see SzFlagUsageGroup#SZ_ENTITY
     */
    String getEntity(SzRecordKey recordKey, Set<SzFlag> flags)
        throws SzUnknownDataSourceException, SzNotFoundException, SzException;

    /**
     * Finds a relationship path between two entities identified by their
     * entity ID's.
     * <p>
     * Entities to be excluded from the path may optionally be specified as a
     * non-null {@link Set} of {@link Long} entity ID's.  If specified as as
     * non-null, then the exclusions {@link Set} contains non-null {@link Long}
     * entity ID's that identify entities to be excluded from the path.
     * <p>
     * Further, a JSON required data sources {@link Set} of {@link String} 
     * data source codes may optionally be specified.  If specified as non-null,
     * then the required data sources {@link Set} contains non-null {@link String}
     * data source codes that identify data sources for which a record from
     * <b>at least one</b> must exist on the path.
     * <p>
     * The optionally specified {@link Set} of {@link SzFlag} instances not only
     * control how the operation is performed but also the level of detail provided
     * for the path and the entities on the path.  The {@link Set} may contain any
     * {@link SzFlag} value, but only flags belonging to the {@link 
     * SzFlagUsageGroup#SZ_FIND_PATH} group will be recognized (other {@link SzFlag}
     * instance will be ignored unless they have equivalent bit flags).
     * <p>
     * <b>NOTE:</b> {@link java.util.EnumSet} offers an efficient means of
     * constructing a {@link Set} of {@link SzFlag}.
     *
     * @param startEntityId The entity ID of the first entity.
     * 
     * @param endEntityId The entity ID of the second entity.
     * 
     * @param maxDegrees The maximum number of degrees for the path search.
     * 
     * @param exclusions The optional {@link Set} of non-null {@link Long}
     *                   entity ID's identifying entities to be excluded
     *                   from the path, or <code>null</code> if no entities
     *                   identified by are to be excluded.
     * 
     * @param requiredDataSources The optional {@link Set} of non-null {@link String}
     *                            data source codes identifying the data sources for
     *                            which at least one record must be included on the
     *                            path, or <code>null</code> if none are required.
     * 
     * @param flags The optional {@link Set} of {@link SzFlag} instances belonging
     *              to the {@link SzFlagUsageGroup#SZ_FIND_PATH} group to control
     *              how the operation is performed and the content of the response,
     *              or <code>null</code> to default to {@link SzFlag#SZ_NO_FLAGS}
     *              or {@link SzFlag#SZ_FIND_PATH_DEFAULT_FLAGS} for the default
     *              recommended flags.
     * 
     * @return The JSON {@link String} describing the resultant entity path which
     *         may be an empty path if no path exists between the two entities
     *         given the path parameters.
     * 
     * @throws SzNotFoundException If either the path-start or path-end entities
     *                             for the specified entity ID's cannot be found.
     * 
     * @throws SzUnknownDataSourceException If an unrecognized required data source
     *                                      is specified.
     * 
     * @throws SzException If a failure occurs.
     * 
     * @see SzFlag#SZ_FIND_PATH_DEFAULT_FLAGS
     * @see SzFlagUsageGroup#SZ_FIND_PATH
     * 
     * @see #findPathByRecordId(String,String,String,String,int,Set,Set,Set)
     * @see #findPathByRecordId(SzRecordKey,SzRecordKey,int,Set,Set,Set)
     */
    String findPathByEntityId(long              startEntityId,
                              long              endEntityId,
                              int               maxDegrees,
                              Set<Long>         exclusions,
                              Set<String>       requiredDataSources,
                              Set<SzFlag>       flags)
        throws SzNotFoundException, SzUnknownDataSourceException, SzException;

    /**
     * Finds a relationship path between two entities identified by the
     * data source codes and record ID's of their constituent records.
     * <p>
     * Entities to be excluded from the path may be optionally specified as
     * a non-null {@link Set} of {@link SzRecordKey} instances.  If specified
     * as non-null, then the exclusions {@link Set} contains the non-null
     * {@link SzRecordKey} instances providing the data source code and record
     * ID pairs that identify the constituent records of entities to be
     * excluded from the path.
     * <p>
     * Further, a JSON required data sources {@link Set} of {@link String} 
     * data source codes may optionally be specified.  If specified as non-null,
     * then the required data sources {@link Set} contains non-null {@link String}
     * data source codes that identify data sources for which a record from
     * <b>at least one</b> must exist on the path.
     * <p>
     * The optionally specified {@link Set} of {@link SzFlag} instances not only
     * control how the operation is performed but also the level of detail provided
     * for the path and the entities on the path.  The {@link Set} may contain any
     * {@link SzFlag} value, but only flags belonging to the {@link 
     * SzFlagUsageGroup#SZ_FIND_PATH} group will be recognized (other {@link SzFlag}
     * instance will be ignored unless they have equivalent bit flags).
     * <p>
     * <b>NOTE:</b> {@link java.util.EnumSet} offers an efficient means of
     * constructing a {@link Set} of {@link SzFlag}.
     *
     * @param startDataSourceCode The data source code of the record at the start
     *                            of the path.
     * 
     * @param startRecordId The record ID of the record at the start of the path.
     * 
     * @param endDataSourceCode The data source code of the record at the end
     *                          of the path.
     * 
     * @param endRecordId The record ID of the record at the end of the path.
     * 
     * @param maxDegrees The maximum number of degrees for the path search.
     * 
     * @param exclusions The optional {@link Set} of non-null {@link
     *                   SzRecordKey} instances providing the data source
     *                   code and record ID pairs of the records whose 
     *                   entities are to be excluded from the path, or 
     *                   <code>null</code> if no entities are to be excluded.
     * 
     * @param requiredDataSources The optional {@link Set} of non-null {@link String}
     *                            data source codes identifying the data sources for
     *                            which at least one record must be included on the
     *                            path, or <code>null</code> if none are required.
     * 
     * @param flags The optional {@link Set} of {@link SzFlag} instances belonging
     *              to the {@link SzFlagUsageGroup#SZ_FIND_PATH} group to control
     *              how the operation is performed and the content of the response,
     *              or <code>null</code> to default to {@link SzFlag#SZ_NO_FLAGS}
     *              or {@link SzFlag#SZ_FIND_PATH_DEFAULT_FLAGS} for the default
     *              recommended flags.
     * 
     * @return The JSON {@link String} describing the resultant entity path which
     *         may be an empty path if no path exists between the two entities
     *         given the path parameters.
     * 
     * @throws SzNotFoundException If either the path-start or path-end records
     *                             for the specified data source code and record ID
     *                             pairs cannot be found.
     * 
     * @throws SzUnknownDataSourceException If an unrecognized data source
     *                                      code is specified.
     * 
     * @throws SzException If a failure occurs.
     * 
     * @see SzFlag#SZ_FIND_PATH_DEFAULT_FLAGS
     * @see SzFlagUsageGroup#SZ_FIND_PATH
     * 
     * @see #findPathByEntityId(long,long,int,Set,Set,Set)
     * @see #findPathByRecordId(SzRecordKey,SzRecordKey,int,Set,Set,Set)
     */
    String findPathByRecordId(String            startDataSourceCode,
                              String            startRecordId,
                              String            endDataSourceCode,
                              String            endRecordId,
                              int               maxDegrees,
                              Set<SzRecordKey>  exclusions,
                              Set<String>       requiredDataSources,
                              Set<SzFlag>       flags)
        throws SzNotFoundException, SzUnknownDataSourceException, SzException;

    /**
     * Finds a relationship path between two entities identified by the
     * data source codes and record ID's of their constituent records
     * given by the specified start and end {@link SzRecordKey} instances.
     * <p>
     * Entities to be excluded from the path may be optionally specified as
     * a non-null {@link Set} of {@link SzRecordKey} instances.  If specified
     * as non-null, then the exclusions {@link Set} contains the non-null
     * {@link SzRecordKey} instances providing the data source code and record
     * ID pairs that identify the constituent records of entities to be
     * excluded from the path.
     * <p>
     * Further, a JSON required data sources {@link Set} of {@link String} 
     * data source codes may optionally be specified.  If specified as non-null,
     * then the required data sources {@link Set} contains non-null {@link String}
     * data source codes that identify data sources for which a record from
     * <b>at least one</b> must exist on the path.
     * <p>
     * The optionally specified {@link Set} of {@link SzFlag} instances not only
     * control how the operation is performed but also the level of detail provided
     * for the path and the entities on the path.  The {@link Set} may contain any
     * {@link SzFlag} value, but only flags belonging to the {@link 
     * SzFlagUsageGroup#SZ_FIND_PATH} group will be recognized (other {@link SzFlag}
     * instance will be ignored unless they have equivalent bit flags).
     * <p>
     * <b>NOTE:</b> {@link java.util.EnumSet} offers an efficient means of
     * constructing a {@link Set} of {@link SzFlag}.
     *
     * @param startRecordKey The {@link SzRecordKey} containing the data source
     *                       code and record ID identifying the record at the
     *                       start of the path.
     * 
     * @param endRecordKey The {@link SzRecordKey} containing the data source
     *                     code and record ID identifying the record at the end
     *                     of the path.
     * 
     * @param maxDegrees The maximum number of degrees for the path search.
     * 
     * @param exclusions The optional {@link Set} of non-null {@link
     *                   SzRecordKey} instances providing the data source
     *                   code and record ID pairs of the records whose 
     *                   entities are to be excluded from the path, or 
     *                   <code>null</code> if no entities are to be excluded.
     * 
     * @param requiredDataSources The optional {@link Set} of non-null {@link String}
     *                            data source codes identifying the data sources for
     *                            which at least one record must be included on the
     *                            path, or <code>null</code> if none are required.
     * 
     * @param flags The optional {@link Set} of {@link SzFlag} instances belonging
     *              to the {@link SzFlagUsageGroup#SZ_FIND_PATH} group to control
     *              how the operation is performed and the content of the response,
     *              or <code>null</code> to default to {@link SzFlag#SZ_NO_FLAGS}
     *              or {@link SzFlag#SZ_FIND_PATH_DEFAULT_FLAGS} for the default
     *              recommended flags.
     * 
     * @return The JSON {@link String} describing the resultant entity path which
     *         may be an empty path if no path exists between the two entities
     *         given the path parameters.
     * 
     * @throws SzNotFoundException If either the path-start or path-end records
     *                             for the specified data source code and record ID
     *                             pairs cannot be found.
     * 
     * @throws SzUnknownDataSourceException If an unrecognized data source
     *                                      code is specified.
     * 
     * @throws SzException If a failure occurs.
     * 
     * @see SzFlag#SZ_FIND_PATH_DEFAULT_FLAGS
     * @see SzFlagUsageGroup#SZ_FIND_PATH
     * 
     * @see #findPathByRecordId(String,String,String,String,int,Set,Set,Set)
     * @see #findPathByEntityId(long,long,int,Set,Set,Set)
     */
    String findPathByRecordId(SzRecordKey       startRecordKey,
                              SzRecordKey       endRecordKey,
                              int               maxDegrees,
                              Set<SzRecordKey>  exclusions,
                              Set<String>       requiredDataSources,
                              Set<SzFlag>       flags)
        throws SzNotFoundException, SzUnknownDataSourceException, SzException;

    /**
     * Finds a network of entity relationships surrounding the paths between
     * a set of entities identified by one or more entity ID's specified in
     * a {@link Set} of non-null {@link Long} entity ID's.
     * <p>
     * Additionally, the maximum degrees of separation for the paths between entities
     * must be specified so as to prevent the network growing beyond the desired size.
     * Further, a non-zero number of degrees to build out the network may be specified
     * to find other related entities.  If build out is specified, it can be limited
     * to a maximum total number of build-out entities for the whole network.
     * <p>
     * The optionally specified {@link Set} of {@link SzFlag} instances not only
     * control how the operation is performed but also the level of detail provided
     * for the network and the entities on the network.  The {@link Set} may contain
     * any {@link SzFlag} value, but only flags belonging to the {@link 
     * SzFlagUsageGroup#SZ_FIND_NETWORK} group are guaranteed to be recognized (other
     * {@link SzFlag} instances will be ignored unless they have equivalent bit flags).
     * <p>
     * <b>NOTE:</b> {@link java.util.EnumSet} offers an efficient means of
     * constructing a {@link Set} of {@link SzFlag}.
     * 
     *
     * @param entityIds The {@link Set} of non-null {@link Long} entity ID's 
     *                  identifying the entities for which to build the network.
     * 
     * @param maxDegrees The maximum number of degrees for the path search
     *                   between the specified entities.
     * 
     * @param buildOutDegrees The number of relationship degrees to build out
     *                        from each of the found entities on the network,
     *                        or zero to prevent network build-out.
     * 
     * @param buildOutMaxEntities The maximum number of entities to build out for
     *                            the entire network.
     * 
     * @param flags The optional {@link Set} of {@link SzFlag} instances belonging
     *              to the {@link SzFlagUsageGroup#SZ_FIND_NETWORK} group to control
     *              how the operation is performed and the content of the response,
     *              or <code>null</code> to default to {@link SzFlag#SZ_NO_FLAGS}
     *              or {@link SzFlag#SZ_FIND_NETWORK_DEFAULT_FLAGS} for the default
     *              recommended flags.
     * 
     * @return The JSON {@link String} describing the resultant entity network
     *         and the entities on the network.
     * 
     * @throws SzNotFoundException If any of the entities for the specified
     *                             entity ID's cannot be found.
     * 
     * @throws SzException If a failure occurs.
     * 
     * @see SzFlag#SZ_FIND_NETWORK_DEFAULT_FLAGS
     * @see SzFlagUsageGroup#SZ_FIND_NETWORK
     * 
     * @see #findNetworkByRecordId(Set,int,int,int,Set)
     */
    String findNetworkByEntityId(Set<Long>      entityIds,
                                 int            maxDegrees,
                                 int            buildOutDegrees,
                                 int            buildOutMaxEntities,
                                 Set<SzFlag>    flags)
        throws SzNotFoundException, SzException;

    /**
     * Finds a network of entity relationships surrounding the paths between
     * a set of entities having the constituent records identified by the
     * data source code and record ID pairs contained in the specified
     * {@link Set} of one or more non-null {@link SzRecordKey} instances.
     * <p>
     * Additionally, the maximum degrees of separation for the paths between entities
     * must be specified so as to prevent the network growing beyond the desired size.
     * Further, a non-zero number of degrees to build out the network may be specified
     * to find other related entities.  If build out is specified, it can be limited
     * to a maximum total number of build-out entities for the whole network.
     * <p>
     * The optionally specified {@link Set} of {@link SzFlag} instances not only
     * control how the operation is performed but also the level of detail provided
     * for the network and the entities on the network.  The {@link Set} may contain
     * any {@link SzFlag} value, but only flags belonging to the {@link 
     * SzFlagUsageGroup#SZ_FIND_NETWORK} group are guaranteed to be recognized (other
     * {@link SzFlag} instances will be ignored unless they have equivalent bit flags).
     * <p>
     * <b>NOTE:</b> {@link java.util.EnumSet} offers an efficient means of
     * constructing a {@link Set} of {@link SzFlag}.
     * 
     *
     * @param recordKeys The {@link Set} of non-null {@link SzRecordKey}
     *                   instances providiung the data source code and
     *                   record ID pairs for the consituent records of the
     *                   entities for which to build the network.
     * 
     * @param maxDegrees The maximum number of degrees for the path search
     *                   between the specified entities.
     * 
     * @param buildOutDegrees The number of relationship degrees to build out
     *                        from each of the found entities on the network,
     *                        or zero to prevent network build-out.
     * 
     * @param buildOutMaxEntities The maximum number of entities to build out for
     *                            the entire network.
     * 
     * @param flags The optional {@link Set} of {@link SzFlag} instances belonging
     *              to the {@link SzFlagUsageGroup#SZ_FIND_NETWORK} group to control
     *              how the operation is performed and the content of the response,
     *              or <code>null</code> to default to {@link SzFlag#SZ_NO_FLAGS}
     *              or {@link SzFlag#SZ_FIND_NETWORK_DEFAULT_FLAGS} for the default
     *              recommended flags.
     * 
     * @return The JSON {@link String} describing the resultant entity network
     *         and the entities on the network.
     * 
     * @throws SzUnknownDataSourceException If an unrecognized data source
     *                                      code is specified.
     * 
     * @throws SzNotFoundException If any of the records for the specified
     *                             data source code and record ID pairs 
     *                             cannot be found.
     * 
     * @throws SzException If a failure occurs.
     * 
     * @see SzFlag#SZ_FIND_NETWORK_DEFAULT_FLAGS
     * @see SzFlagUsageGroup#SZ_FIND_NETWORK
     * 
     * @see #findNetworkByEntityId(Set,int,int,int,Set)
     */
    String findNetworkByRecordId(Set<SzRecordKey>       recordKeys,
                                 int                    maxDegrees,
                                 int                    buildOutDegrees,
                                 int                    buildOutMaxEntities,
                                 Set<SzFlag>            flags)
        throws SzUnknownDataSourceException, SzNotFoundException, SzException;

    /**
     * Determines why the record identified by the specified data source code
     * and record ID is included in its respective entity.
     * <p>
     * The optionally specified {@link Set} of {@link SzFlag} instances that
     * not only control how the operation is performed but also the level of
     * detail provided for the entity and record being analyzed.  The
     * {@link Set} may contain any {@link SzFlag} value, but only flags
     * belonging to the {@link SzFlagUsageGroup#SZ_WHY} group are guaranteed
     * to be recognized (other {@link SzFlag} instances will be ignored unless
     * they have equivalent bit flags).
     * <p>
     * <b>NOTE:</b> {@link java.util.EnumSet} offers an efficient means of
     * constructing a {@link Set} of {@link SzFlag}.
     *
     * @param dataSourceCode The data source code for the record.
     * 
     * @param recordId The record ID for the record.
     * 
     * @param flags The optional {@link Set} of {@link SzFlag} instances belonging
     *              to the {@link SzFlagUsageGroup#SZ_WHY} group to control how the
     *              operation is performed and the content of the response, or
     *              <code>null</code> to default to {@link SzFlag#SZ_NO_FLAGS}
     *              or {@link SzFlag#SZ_WHY_RECORD_IN_ENTITY_DEFAULT_FLAGS} for the
     *              default recommended flags.
     * 
     * @return The JSON {@link String} describing why the record is included
     *         in its respective entity.
     * 
     * @throws SzUnknownDataSourceException If an unrecognized data source
     *                                      code is specified.
     * 
     * @throws SzNotFoundException If any of the records for the specified
     *                             data source code and record ID pairs 
     *                             cannot be found.
     * 
     * @throws SzException If a failure occurs.
     * 
     * @see SzFlag#SZ_WHY_RECORD_IN_ENTITY_DEFAULT_FLAGS
     * @see SzFlagUsageGroup#SZ_WHY
     * 
     * @see #whyRecordInEntity(SzRecordKey, Set)
     * @see #whyEntities(long, long, Set)
     * @see #whyRecords(String, String, String, String, Set)
     * @see #whyRecords(SzRecordKey, SzRecordKey, Set)
     */
    String whyRecordInEntity(String             dataSourceCode,
                             String             recordId,
                             Set<SzFlag>        flags)
        throws SzUnknownDataSourceException, SzNotFoundException, SzException;

    /**
     * Determines why the record identified by the data source code and
     * record ID in the specified in an {@link SzRecordKey} is included
     * in its respective entity.
     * <p>
     * The optionally specified {@link Set} of {@link SzFlag} instances that
     * not only control how the operation is performed but also the level of
     * detail provided for the entity and record being analyzed.  The
     * {@link Set} may contain any {@link SzFlag} value, but only flags
     * belonging to the {@link SzFlagUsageGroup#SZ_WHY} group are guaranteed
     * to be recognized (other {@link SzFlag} instances will be ignored unless
     * they have equivalent bit flags).
     * <p>
     * <b>NOTE:</b> {@link java.util.EnumSet} offers an efficient means of
     * constructing a {@link Set} of {@link SzFlag}.
     *
     * @param recordKey The {@link SzRecordKey} that has the data source code
     *                  and record ID identifying the record.
     * 
     * @param flags The optional {@link Set} of {@link SzFlag} instances belonging
     *              to the {@link SzFlagUsageGroup#SZ_WHY} group to control how the
     *              operation is performed and the content of the response, or
     *              <code>null</code> to default to {@link SzFlag#SZ_NO_FLAGS}
     *              or {@link SzFlag#SZ_WHY_RECORD_IN_ENTITY_DEFAULT_FLAGS} for the
     *              default recommended flags.
     * 
     * @return The JSON {@link String} describing why the record is included
     *         in its respective entity.
     * 
     * @throws SzUnknownDataSourceException If an unrecognized data source
     *                                      code is specified.
     * 
     * @throws SzNotFoundException If any of the records for the specified
     *                             data source code and record ID pairs 
     *                             cannot be found.
     * 
     * @throws SzException If a failure occurs.
     * 
     * @see SzFlag#SZ_WHY_RECORD_IN_ENTITY_DEFAULT_FLAGS
     * @see SzFlagUsageGroup#SZ_WHY
     * 
     * @see #whyRecordInEntity(String, String, Set)
     * @see #whyEntities(long, long, Set)
     * @see #whyRecords(String, String, String, String, Set)
     * @see #whyRecords(SzRecordKey, SzRecordKey, Set)
     */
    String whyRecordInEntity(SzRecordKey recordKey, Set<SzFlag> flags)
        throws SzUnknownDataSourceException, SzNotFoundException, SzException;

    /**
     * Determines ways in which two records identified by the specified
     * data source code and record ID pairs are related to each other.
     * <p>
     * The optionally specified {@link Set} of {@link SzFlag} instances that
     * not only control how the operation is performed but also the level of
     * detail provided for the entity and record being analyzed.  The
     * {@link Set} may contain any {@link SzFlag} value, but only flags
     * belonging to the {@link SzFlagUsageGroup#SZ_WHY} group are guaranteed
     * to be recognized (other {@link SzFlag} instances will be ignored unless
     * they have equivalent bit flags).
     * <p>
     * <b>NOTE:</b> {@link java.util.EnumSet} offers an efficient means of
     * constructing a {@link Set} of {@link SzFlag}.
     *
     * @param dataSourceCode1 The data source code for the first record.
     * 
     * @param recordId1 The record ID for the first record.
     * 
     * @param dataSourceCode2 The data source code for the second record.
     * 
     * @param recordId2 The record ID for the second record.
     * 
     * @param flags The optional {@link Set} of {@link SzFlag} instances belonging
     *              to the {@link SzFlagUsageGroup#SZ_WHY} group to control how the
     *              operation is performed and the content of the response, or
     *              <code>null</code> to default to {@link SzFlag#SZ_NO_FLAGS}
     *              or {@link SzFlag#SZ_WHY_RECORDS_DEFAULT_FLAGS} for the
     *              default recommended flags.
     * 
     * @return The JSON {@link String} describing the ways in which the records
     *         are related to one another.
     * 
     * @throws SzUnknownDataSourceException If an unrecognized data source
     *                                      code is specified.
     * 
     * @throws SzNotFoundException If any of the records for the specified
     *                             data source code and record ID pairs 
     *                             cannot be found.
     * 
     * @throws SzException If a failure occurs.
     * 
     * @see SzFlag#SZ_WHY_RECORDS_DEFAULT_FLAGS
     * @see SzFlagUsageGroup#SZ_WHY
     * 
     * @see #whyRecords(SzRecordKey, SzRecordKey, Set)
     * @see #whyRecordInEntity(String, String, Set)
     * @see #whyRecordInEntity(SzRecordKey, Set)
     * @see #whyEntities(long, long, Set)
     */
    String whyRecords(String            dataSourceCode1,
                      String            recordId1,
                      String            dataSourceCode2,
                      String            recordId2,
                      Set<SzFlag>       flags)
        throws SzUnknownDataSourceException, SzNotFoundException, SzException;

    /**
     * Determines ways in which two records identified by the data source
     * code and record ID pairs from the specified {@link SzRecordKey}
     * instances are related to each other.
     * <p>
     * The optionally specified {@link Set} of {@link SzFlag} instances that
     * not only control how the operation is performed but also the level of
     * detail provided for the entity and record being analyzed.  The
     * {@link Set} may contain any {@link SzFlag} value, but only flags
     * belonging to the {@link SzFlagUsageGroup#SZ_WHY} group are guaranteed
     * to be recognized (other {@link SzFlag} instances will be ignored unless
     * they have equivalent bit flags).
     * <p>
     * <b>NOTE:</b> {@link java.util.EnumSet} offers an efficient means of
     * constructing a {@link Set} of {@link SzFlag}.
     *
     * @param recordKey1 The non-null {@link SzRecordKey} providing the
     *                   data source code and record ID for the first record.
     * 
     * @param recordKey2 The non-null {@link SzRecordKey} providing the
     *                   data source code and record ID for the second record.
     * 
     * @param flags The optional {@link Set} of {@link SzFlag} instances belonging
     *              to the {@link SzFlagUsageGroup#SZ_WHY} group to control how the
     *              operation is performed and the content of the response, or
     *              <code>null</code> to default to {@link SzFlag#SZ_NO_FLAGS}
     *              or {@link SzFlag#SZ_WHY_RECORDS_DEFAULT_FLAGS} for the
     *              default recommended flags.
     * 
     * @return The JSON {@link String} describing the ways in which the records
     *         are related to one another.
     * 
     * @throws SzUnknownDataSourceException If an unrecognized data source
     *                                      code is specified.
     * 
     * @throws SzNotFoundException If either of the records for the specified
     *                             data source code and record ID pairs 
     *                             cannot be found.
     * 
     * @throws SzException If a failure occurs.
     * 
     * @see SzFlag#SZ_WHY_RECORDS_DEFAULT_FLAGS
     * @see SzFlagUsageGroup#SZ_WHY
     * 
     * @see #whyRecords(String, String, String, String, Set)
     * @see #whyRecordInEntity(String, String, Set)
     * @see #whyRecordInEntity(SzRecordKey, Set)
     * @see #whyEntities(long, long, Set)
     */
    String whyRecords(SzRecordKey       recordKey1,
                      SzRecordKey       recordKey2,
                      Set<SzFlag>       flags)
        throws SzUnknownDataSourceException, SzNotFoundException, SzException;

    /**
     * Determines the ways in which two entities identified by the specified
     * entity ID's are related to each other.
     * <p>
     * The optionally specified {@link Set} of {@link SzFlag} instances that
     * not only control how the operation is performed but also the level of
     * detail provided for the entity and record being analyzed.  The
     * {@link Set} may contain any {@link SzFlag} value, but only flags
     * belonging to the {@link SzFlagUsageGroup#SZ_WHY} group are guaranteed
     * to be recognized (other {@link SzFlag} instances will be ignored unless
     * they have equivalent bit flags).
     * <p>
     * <b>NOTE:</b> {@link java.util.EnumSet} offers an efficient means of
     * constructing a {@link Set} of {@link SzFlag}.
     *
     * @param entityId1 The entity ID of the first entity.
     * 
     * @param entityId2 The entity ID of the second entity.
     * 
     * @param flags The optional {@link Set} of {@link SzFlag} instances belonging
     *              to the {@link SzFlagUsageGroup#SZ_WHY} group to control how the
     *              operation is performed and the content of the response, or
     *              <code>null</code> to default to {@link SzFlag#SZ_NO_FLAGS}
     *              or {@link SzFlag#SZ_WHY_ENTITIES_DEFAULT_FLAGS} for the
     *              default recommended flags.
     * 
     * @return The JSON {@link String} describing the ways in which the entities
     *         are related to one another.
     * 
     * @throws SzNotFoundException If either of the entities for the specified
     *                             entity ID's could not be found.
     * 
     * @throws SzException If a failure occurs.
     * 
     * @see SzFlag#SZ_WHY_ENTITIES_DEFAULT_FLAGS
     * @see SzFlagUsageGroup#SZ_WHY
     * 
     * @see #whyRecords(String, String, String, String, Set)
     * @see #whyRecords(SzRecordKey, SzRecordKey, Set)
     * @see #whyRecordInEntity(String, String, Set)
     * @see #whyRecordInEntity(SzRecordKey, Set)
    */
    String whyEntities(long entityId1, long entityId2, Set<SzFlag> flags)
        throws SzNotFoundException, SzException;

    /**
     * Deterimes how an entity identified by the specified entity ID was
     * constructed from its constituent records.
     * <p>
     * The optionally specified {@link Set} of {@link SzFlag} instances that
     * not only control how the operation is performed but also the level of
     * detail provided for the entity and record being analyzed.  The
     * {@link Set} may contain any {@link SzFlag} value, but only flags
     * belonging to the {@link SzFlagUsageGroup#SZ_HOW} group are guaranteed
     * to be recognized (other {@link SzFlag} instances will be ignored unless
     * they have equivalent bit flags).
     * <p>
     * <b>NOTE:</b> {@link java.util.EnumSet} offers an efficient means of
     * constructing a {@link Set} of {@link SzFlag}.
     *
     * @param entityId The entity ID of the entity.
     * 
     * @param flags The optional {@link Set} of {@link SzFlag} instances belonging
     *              to the {@link SzFlagUsageGroup#SZ_HOW} group to control how the
     *              operation is performed and the content of the response, or
     *              <code>null</code> to default to {@link SzFlag#SZ_NO_FLAGS}
     *              or {@link SzFlag#SZ_HOW_ENTITY_DEFAULT_FLAGS} for the default
     *              recommended flags.
     * 
     * @return The JSON {@link String} describing the how the entity was
     *         constructed.
     * 
     * @throws SzNotFoundException If the entity for the specified entity ID
     *                             could not be found.
     * 
     * @throws SzException If a failure occurs.
     * 
     * @see SzFlag#SZ_HOW_ENTITY_DEFAULT_FLAGS
     * @see SzFlagUsageGroup#SZ_HOW
     * 
     * @see #getVirtualEntity(Set, Set)
     */
    String howEntity(long entityId, Set<SzFlag> flags)
        throws SzNotFoundException, SzException;

    /**
     * Describes a hypothetically entity that would be composed of the one
     * or more records identified by the data source code and record ID pairs
     * in the specified {@link Set} of {@link SzRecordKey} instances.
     * <p>
     * The optionally specified {@link Set} of {@link SzFlag} instances that
     * not only control how the operation is performed but also the level of
     * detail provided for the entity and record being analyzed.  The
     * {@link Set} may contain any {@link SzFlag} value, but only flags
     * belonging to the {@link SzFlagUsageGroup#SZ_VIRTUAL_ENTITY} group are
     * guaranteed to be recognized (other {@link SzFlag} instances will be
     * ignored unless they have equivalent bit flags).
     * <p>
     * <b>NOTE:</b> {@link java.util.EnumSet} offers an efficient means of
     * constructing a {@link Set} of {@link SzFlag}.
     *
     * @param recordKeys The non-null non-empty {@link Set} of non-null {@link
     *                   SzRecordKey} instances that identify the records to 
     *                   use to build the virtual entity.
     * 
     * @param flags The optional {@link Set} of {@link SzFlag} instances
     *              belonging to the {@link SzFlagUsageGroup#SZ_VIRTUAL_ENTITY}
     *              group to control how the operation is performed and
     *              the content of the response, or <code>null</code> to
     *              default to {@link SzFlag#SZ_NO_FLAGS} or
     *              {@link SzFlag#SZ_VIRTUAL_ENTITY_DEFAULT_FLAGS} for the
     *              default recommended flags.
     * 
     * @return The JSON {@link String} describing the virtual entity having
     *         the specified constituent records.
     * 
     * @throws SzNotFoundException If any of the records for the specified
     *                             data source code and record ID pairs 
     *                             cannot be found.
     * 
     * @throws SzException If a failure occurs.
     * 
     * @see SzFlag#SZ_HOW_ENTITY_DEFAULT_FLAGS
     * @see SzFlagUsageGroup#SZ_HOW
     * 
     * @see #howEntity(long, Set)
     */
    String getVirtualEntity(Set<SzRecordKey>  recordKeys,
                            Set<SzFlag>       flags)
        throws SzNotFoundException, SzException;

    /**
     * Retrieves the record identified by the specified data source code
     * and record ID.
     * <p>
     * The optionally specified {@link Set} of {@link SzFlag} instances that
     * not only control how the operation is performed but also the level of
     * detail provided for the entity and record being analyzed.  The
     * {@link Set} may contain any {@link SzFlag} value, but only flags
     * belonging to the {@link SzFlagUsageGroup#SZ_RECORD} group are
     * guaranteed to be recognized (other {@link SzFlag} instances will be
     * ignored unless they have equivalent bit flags).
     * <p>
     * <b>NOTE:</b> {@link java.util.EnumSet} offers an efficient means of
     * constructing a {@link Set} of {@link SzFlag}.
     * 
     * @param dataSourceCode The data source of the record to retrieve.
     * 
     * @param recordId The record ID of the record to retrieve.
     * 
     * @param flags The optional {@link Set} of {@link SzFlag} instances belonging
     *              to the {@link SzFlagUsageGroup#SZ_RECORD} group to control how
     *              the operation is performed and the content of the response, or
     *              <code>null</code> to default to {@link SzFlag#SZ_NO_FLAGS}
     *              or {@link SzFlag#SZ_RECORD_DEFAULT_FLAGS} for the default 
     *              recommended flags.
     *
     * @return The JSON {@link String} describing the record.
     * 
     * @throws SzUnknownDataSourceException If an unrecognized data source
     *                                      code is specified.
     * 
     * @throws SzNotFoundException If the record for the specified data source
     *                             code and record ID pairs cannot be found.
     * 
     * @throws SzException If a failure occurs.
     * 
     * @see SzFlag#SZ_RECORD_DEFAULT_FLAGS
     * @see SzFlagUsageGroup#SZ_RECORD
     * 
     * @see #getRecord(SzRecordKey, Set)
     */
    String getRecord(String             dataSourceCode,
                     String             recordId,
                     Set<SzFlag>        flags)
        throws SzUnknownDataSourceException, SzNotFoundException, SzException;

    /**
     * Retrieves the record identified by the data source code and record ID
     * from the specified {@link SzRecordKey}.
     * <p>
     * The optionally specified {@link Set} of {@link SzFlag} instances that
     * not only control how the operation is performed but also the level of
     * detail provided for the entity and record being analyzed.  The
     * {@link Set} may contain any {@link SzFlag} value, but only flags
     * belonging to the {@link SzFlagUsageGroup#SZ_RECORD} group are
     * guaranteed to be recognized (other {@link SzFlag} instances will be
     * ignored unless they have equivalent bit flags).
     * <p>
     * <b>NOTE:</b> {@link java.util.EnumSet} offers an efficient means of
     * constructing a {@link Set} of {@link SzFlag}.
     * 
     * @param recordKey The non-null {@link SzRecordKey} providing the 
     *                  data source code and record ID that identify the
     *                  record to retrieve.
     * 
     * @param flags The optional {@link Set} of {@link SzFlag} instances belonging
     *              to the {@link SzFlagUsageGroup#SZ_RECORD} group to control how
     *              the operation is performed and the content of the response, or
     *              <code>null</code> to default to {@link SzFlag#SZ_NO_FLAGS}
     *              or {@link SzFlag#SZ_RECORD_DEFAULT_FLAGS} for the default 
     *              recommended flags.
     *
     * @return The JSON {@link String} describing the record.
     * 
     * @throws SzUnknownDataSourceException If an unrecognized data source
     *                                      code is specified.
     * 
     * @throws SzNotFoundException If the record for the specified data source
     *                             code and record ID pairs cannot be found.
     * 
     * @throws SzException If a failure occurs.
     * 
     * @see SzFlag#SZ_RECORD_DEFAULT_FLAGS
     * @see SzFlagUsageGroup#SZ_RECORD
     * 
     * @see #getRecord(String, String, Set)
     */
    String getRecord(SzRecordKey        recordKey,
                     Set<SzFlag>        flags)
        throws SzUnknownDataSourceException, SzNotFoundException, SzException;

    /**
     * Iniitiates an export of entity data as JSON-lines format and returns an
     * "export handle" that can be used to {@linkplain #fetchNext(long) read
     * the export data} and must be {@linkplain #closeExport(long) closed} when
     * complete.  Each output line contains the exported entity data for a
     * single resolved entity.
     * <p>
     * The optionally specified {@link Set} of {@link SzFlag} instances that
     * not only control how the operation is performed but also the level of
     * detail provided for the entity and record being analyzed.  The
     * {@link Set} may contain any {@link SzFlag} value, but only flags
     * belonging to the {@link SzFlagUsageGroup#SZ_EXPORT} group are
     * guaranteed to be recognized (other {@link SzFlag} instances will be
     * ignored unless they have equivalent bit flags).
     * <p>
     * <b>NOTE:</b> {@link java.util.EnumSet} offers an efficient means of
     * constructing a {@link Set} of {@link SzFlag}.
     *
     * @param flags The optional {@link Set} of {@link SzFlag} instances belonging
     *              to the {@link SzFlagUsageGroup#SZ_EXPORT} group to control how
     *              the operation is performed and the content of the export, or
     *              <code>null</code> to default to {@link SzFlag#SZ_NO_FLAGS}
     *              or {@link SzFlag#SZ_EXPORT_DEFAULT_FLAGS} for the default
     *              recommended flags.
     * 
     * @return The export handle to use for retrieving the export data.
     * 
     * @throws SzException If a failure occurs.
     * 
     * @see SzFlag#SZ_EXPORT_DEFAULT_FLAGS
     * @see SzFlagUsageGroup#SZ_EXPORT
     * 
     * @see #fetchNext(long)
     * @see #closeExport(long)
     * @see #exportCsvEntityReport(String, Set)
     */
    long exportJsonEntityReport(Set<SzFlag> flags) throws SzException;

    /**
     * Initiates an export of entity data in CSV format and returns an 
     * "export handle" that can be used to {@linkplain #fetchNext(long) read
     * the export data} and must be {@linkplain #closeExport(long) closed}
     * when complete.  The first exported line contains the CSV header and
     * each subsequent line contains the exported entity data for a single
     * resolved entity.
     *
     * @param csvColumnList Specify <code>"*"</code> to indicate "all columns",
     *                      specify empty-string to indicate the "standard
     *                      columns", otherwise specify a comma-sepatated list of
     *                      column names.
     * 
     * @param flags The optional {@link Set} of {@link SzFlag} instances belonging
     *              to the {@link SzFlagUsageGroup#SZ_EXPORT} group to control how
     *              the operation is performed and the content of the export, or
     *              <code>null</code> to default to {@link SzFlag#SZ_NO_FLAGS}
     *              or {@link SzFlag#SZ_EXPORT_DEFAULT_FLAGS} for the default
     *              recommended flags.
     * 
     * @return The export handle to use for retrieving the export data.
     *
     * @throws SzException If a failure occurs.
     * 
     * @see SzFlag#SZ_EXPORT_DEFAULT_FLAGS
     * @see SzFlagUsageGroup#SZ_EXPORT
     * 
     * @see #fetchNext(long)
     * @see #closeExport(long)
     * @see #exportJsonEntityReport(Set)
     */
    long exportCsvEntityReport(String csvColumnList, Set<SzFlag> flags)
        throws SzException;

    /**
     * Fetches the next line of entity data from the export identified
     * by the specified export handle.  The specified export handle can
     * be obtained from {@link #exportJsonEntityReport(Set)} or {@link
     * #exportCsvEntityReport(String, Set)}.
     * 
     * @param exportHandle The export handle to identify the export from
     *                     which to retrieve the next line of data.
     * 
     * @return The next line of export data whose format depends on which
     *         function was used to initiate the export.
     * 
     * @see #exportCsvEntityReport(String, Set)
     * @see #exportJsonEntityReport(Set)
     * @see #closeExport(long)
     * 
     * @throws SzException If a failure occurs.
     */
    String fetchNext(long exportHandle) throws SzException;

    /**
     * This function closes an export handle of a previously opened 
     * export to clean up system resources.  This function is idempotent
     * and may be called for an export that has already been closed.
     *
     * @param exportHandle The export handle of the export to close.
     * 
     * @throws SzException If a failure occurs.
     * 
     * @see #exportCsvEntityReport(String, Set)
     * @see #exportJsonEntityReport(Set)
     * @see #fetchNext(long)
     */
    void closeExport(long exportHandle) throws SzException;

    /**
     * Processes the specified redo record using the specified flags.
     * The redo record can be retrieved from {@link #getRedoRecord()}.
     * <p>
     * The optionally specified {@link Set} of {@link SzFlag} instances that
     * not only control how the operation is performed but also the level of
     * detail provided for the entity and record being analyzed.  The
     * {@link Set} may contain any {@link SzFlag} value, but only flags
     * belonging to the {@link SzFlagUsageGroup#SZ_MODIFY} group are
     * guaranteed to be recognized (other {@link SzFlag} instances will be
     * ignored unless they have equivalent bit flags).
     * <p>
     * <b>NOTE:</b> {@link java.util.EnumSet} offers an efficient means of
     * constructing a {@link Set} of {@link SzFlag}.
     *
     * @param redoRecord The redorecord to be processed.
     * 
     * @param flags The optional {@link Set} of {@link SzFlag} instances belonging
     *              to the {@link SzFlagUsageGroup#SZ_MODIFY} group to control how
     *              the operation is performed and the content of the response, or
     *              <code>null</code> to default to {@link SzFlag#SZ_NO_FLAGS}.
     *              Specify {@link SzFlag#SZ_WITH_INFO_FLAGS} for an INFO response.
     * 
     * @return The JSON {@link String} result produced by adding the
     *         record to the repository (depending on the specified flags).
     * 
     * @throws SzException If a failure occurs.
     * 
     * @see SzFlag#SZ_WITH_INFO_FLAGS
     * @see SzFlagUsageGroup#SZ_MODIFY
     * 
     * @see #getRedoRecord()
     * @see #countRedoRecords()
     */
    String processRedoRecord(String redoRecord, Set<SzFlag> flags)
        throws SzException;

    /**
     * Retrieves a pending redo record from the reevaluation queue.  If no
     * redo records are availbale then this returns an <code>null</code>.
     *
     * @return The retrieved redo record or <code>null</code> if there were
     *         no pending redo records.
     * 
     * @see #processRedoRecord(String, Set)
     * @see #countRedoRecords()
     * 
     * @throws SzException If a failure occurs.
     */
    String getRedoRecord() throws SzException;

    /**
     * Gets the number of redo records pending to be processed.
     *
     * @return The number of redo records pending to be processed.
     * 
     * @see #processRedoRecord(String, Set)
     * @see #getRedoRecord()
     * 
     * @throws SzException If a failure occurs.
     */
    long countRedoRecords() throws SzException;
}