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
     * Returns a long integer representing number of seconds since
     * January 1, 1970 12:00am GMT (epoch time). This indicates the last
     * time the data repository was modified.
     *
     * @return A <code>long</code> integer representing the last modified
     *         epoch time. 
     * 
     * @throws SzException If a failure occurs.
     */
    long getRepositoryLastModifiedTime() throws SzException;

    /**
     * Loads the record described by the specified {@link String} record
     * definition having the specified data source code and record ID using
     * the specified <code>long</code> bit flags.  If a record already exists
     * with the same data source code and record ID, then it will be replaced.
     * <p>
     * The specified JSON data may optionally contain the <code>DATA_SOURCE</code>
     * and <code>RECORD_ID</code> properties, but, if so, they must match the
     * specified parameters.
     * <p>
     * The specified <code>long</code> bit flags may contain any bits, but only
     * those bit {@linkplain SzFlag#toLong() values} belonging to {@link SzFlag}
     * instances from the {@link SzFlagUsageGroup#SZ_MODIFY} group will be
     * recognized (other bits will be ignored).  <b>NOTE:</b> All <code>long</code>
     * bit flags are defined in {@link SzFlags} but you may want to use {@link
     * #addRecord(String,String,String,Set)} to instead specify a {@link Set} of
     * {@link SzFlag} instances.
     *
     * @param dataSourceCode The data source for the record.
     * 
     * @param recordId  The record ID for the record.
     * 
     * @param recordDefinition The {@link String} that defines the record, typically
     *                         in JSON format.
     * 
     * @param flags The <code>long</code> {@link SzFlagUsageGroup#SZ_MODIFY} bit
     *              flags to control how the operation is performed and the content
     *              of the response.  Specify {@link SzFlags#SZ_NO_FLAGS} to pass no flags
     *              or {@link SzFlags#SZ_WITH_INFO_FLAGS} to obtain an INFO response.
     * 
     * @return The JSON {@link String} result produced by adding the
     *         record to the repository (depending on the specified flags).
     * 
     * @throws SzUnknownDataSourceExcpetion If an unrecognized data source
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
     * @see SzFlags#SZ_WITH_INFO_FLAGS
     * @see SzFlagUsageGroup#SZ_MODIFY
     */
    String addRecord(String dataSourceCode, 
                     String recordId, 
                     String recordDefinition,
                     long   flags)
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
     * @param dataSourceCode The data source for the record.
     * 
     * @param recordId  The record ID for the record.
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
     * @throws SzUnknownDataSourceExcpetion If an unrecognized data source
     *                                      code is specified.
     * 
     * @throws SzBadInputException If the specified record definition has a data source
     *                             or record ID value that conflicts with the specified
     *                             data source code and/or record ID values.
     * 
     * @throws SzException If a failure occurs.
     * 
     * @see #addRecord(String, String, String, long)
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
     * Delete the record that has already been loaded.  This method is
     * idempotent, meaning multiple calls this method with the same
     * method will all succeed regardless of whether or not the record
     * is found in the repository.
     * <p>
     * The specified <code>long</code> bit flags may contain any bits, but only
     * those bit {@linkplain SzFlag#toLong() values} belonging to {@link SzFlag}
     * instances from the {@link SzFlagUsageGroup#SZ_MODIFY} group will be
     * recognized (other bits will be ignored).  <b>NOTE:</b> All
     * <code>long</code> bit flags are defined in {@link SzFlags} but you may
     * want to use {@link #deleteRecord(String,String,Set)} to instead specify
     * a {@link Set} of {@link SzFlag} instances.
     *
     * @param dataSourceCode The data source for the record.
     * 
     * @param recordId The record ID for the record.
     * 
     * @param flags The <code>long</code> {@link SzFlagUsageGroup#SZ_MODIFY} bit
     *              flags to control how the operation is performed and the content
     *              of the response.  Specify {@link SzFlags#SZ_NO_FLAGS} to pass
     *              no flags or {@link SzFlags#SZ_WITH_INFO_FLAGS} to obtain an
     *              INFO response.
     *
     * @return The JSON {@link String} result produced by deleting the
     *         record from the repository (depending on the specified flags).
     * 
     * @throws SzUnknownDataSourceExcpetion If an unrecognized data source
     *                                      code is specified.
     * 
     * @throws SzException If a failure occurs.
     * 
     * @see #deleteRecord(String, String, Set)
     * 
     * @see SzFlags#SZ_WITH_INFO_FLAGS
     * @see SzFlagUsageGroup#SZ_MODIFY
     */
    String deleteRecord(String dataSourceCode, String recordId, long flags)
        throws SzUnknownDataSourceException, SzException;

    /**
     * Delete the record that has already been loaded.  This method is
     * idempotent, meaning multiple calls this method with the same
     * method will all succeed regardless of whether or not the record
     * is found in the repository.
     * <p>
     * The specified {@link Set} of {@link SzFlag} instances may contain any 
     * {@link SzFlag} value, but only flags belonging to the {@link
     * SzFlagUsageGroup#SZ_MODIFY} group will be recognized (other {@link SzFlag}
     * instances will be ignored).  <b>NOTE:</b> {@link java.util.EnumSet}
     * offers an efficient means of constructing a {@link Set} of {@link SzFlag}.
     *
     * @param dataSourceCode The data source for the observation.
     * @param recordId The ID for the record.
     * @param flags The optional {@link Set} of {@link SzFlag} instances belonging
     *              to the {@link SzFlagUsageGroup#SZ_MODIFY} group to control how
     *              the operation is performed and the content of the response, or
     *              <code>null</code> to default to {@link SzFlag#SZ_NO_FLAGS}.
     *              Specify {@link SzFlag#SZ_WITH_INFO_FLAGS} for an INFO response.
     *
     * @return The JSON {@link String} result produced by deleting the
     *         record from the repository (depending on the specified flags).
     * 
     * @throws SzUnknownDataSourceExcpetion If an unrecognized data source
     *                                      code is specified.
     * 
     * @throws SzException If a failure occurs.
     * 
     * @see #deleteRecord(String, String, long)
     * 
     * @see SzFlag#SZ_WITH_INFO_FLAGS
     * @see SzFlagUsageGroup#SZ_MODIFY
     */
    String deleteRecord(String      dataSourceCode,
                        String      recordId,
                        Set<SzFlag> flags)
        throws SzUnknownDataSourceException, SzException;

    /**
     * Reevaluate the record identified by the specified data source code and
     * record ID.  If the record is not found then TODO(bcaceres): complete this
     * when GDEV-3790 gets resolved and the "not found" behavior is defined.
     * <p>
     * The specified <code>long</code> bit flags may contain any bits, but only
     * those bit {@linkplain SzFlag#toLong() values} belonging to {@link SzFlag}
     * instances from the {@link SzFlagUsageGroup#SZ_MODIFY} group will be
     * recognized (other bits will be ignored).  <b>NOTE:</b> All
     * <code>long</code> bit flags are defined in {@link SzFlags} but you may want
     * to use {@link #reevaluateRecord(String,String,Set)} to instead specify a
     * {@link Set} of {@link SzFlag} instances.
     *
     * @param dataSourceCode The data source for the record.
     * 
     * @param recordId The record ID for the record.
     * 
     * @param flags The <code>long</code> {@link SzFlagUsageGroup#SZ_MODIFY} bit
     *              flags to control how the operation is performed and the content
     *              of the response.  Specify {@link SzFlags#SZ_NO_FLAGS} to pass
     *              no flags or {@link SzFlags#SZ_WITH_INFO_FLAGS} to obtain an
     *              INFO response.
     *
     * @return The JSON {@link String} result produced by reevaluating the
     *         record in the repository (depending on the specified flags).
     *
     * @throws SzUnknownDataSourceExcpetion If an unrecognized data source
     *                                      code is specified.
     * 
     * @throws SzException If a failure occurs.
     * 
     * @see #reevaluateRecord(String, String, Set)
     * 
     * @see SzFlags#SZ_WITH_INFO_FLAGS
     * @see SzFlagUsageGroup#SZ_MODIFY
     */
    String reevaluateRecord(String dataSourceCode, String recordId, long flags)
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
     * @param dataSourceCode The data source for the record.
     * 
     * @param recordId The record ID for the record.
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
     * @throws SzUnknownDataSourceExcpetion If an unrecognized data source
     *                                      code is specified.
     * 
     * @throws SzException If a failure occurs.
     * 
     * @see #reevaluateRecord(String, String, long)
     * 
     * @see SzFlag#SZ_WITH_INFO_FLAGS
     * @see SzFlagUsageGroup#SZ_MODIFY
     */
    String reevaluateRecord(String      dataSourceCode,
                            String      recordId,
                            Set<SzFlag> flags)
    throws SzUnknownDataSourceException, SzException;

    /**
     * Reevaluate a resolved entity identified by the specified entity ID.
     * If the entity is not found then TODO(bcaceres): complete this when
     * GDEV-3790 gets resolved and the "not found" behavior is defined.
     * <p>
     * The specified <code>long</code> bit flags may contain any bits, but only
     * those bit {@linkplain SzFlag#toLong() values} belonging to {@link SzFlag}
     * instances from the {@link SzFlagUsageGroup#SZ_MODIFY} group will be
     * recognized (other bits will be ignored).  <b>NOTE:</b> All
     * <code>long</code> bit flags are defined in {@link SzFlags} but you may want
     * to use {@link #reevaluateEntity(long, Set)} to instead specify a
     * {@link Set} of {@link SzFlag} instances.
     *
     * @param entityId The ID of the resolved entity to reevaluate.
     * 
     * @param flags The <code>long</code> {@link SzFlagUsageGroup#SZ_MODIFY} bit
     *              flags to control how the operation is performed and the content
     *              of the response.  Specify {@link SzFlags#SZ_NO_FLAGS} to pass no
     *              flags or {@link SzFlags#SZ_WITH_INFO_FLAGS} to obtain an INFO
     *              response.
     *
     * @return The JSON {@link String} result produced by reevaluating the
     *         entity in the repository (depending on the specified flags).
     * 
     * @throws SzException If a failure occurs.
     * 
     * @see #reevaluateEntity(long, Set)
     * 
     * @see SzFlags#SZ_WITH_INFO_FLAGS
     * @see SzFlagUsageGroup#SZ_MODIFY
     */
    String reevaluateEntity(long entityId, long flags)
        throws SzException;

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
     * @see #reevaluateEntity(long, long)
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
     * search (alternatively, use {@link #searchByAttributes(String, long)} to 
     * omit the parameter).  If your search requires different behavior using
     * alternate generic thresholds, please contact 
     * <a href="mailto:support@senzing.com">support@senzing.com</a> for details
     * on configuring a custom search profile.
     * <p>
     * The specified <code>long</code> bit flags may contain any bits, but only
     * those bit {@linkplain SzFlag#toLong() values} belonging to {@link SzFlag}
     * instances from the {@link SzFlagUsageGroup#SZ_SEARCH} group will be
     * recognized (other bits will be ignored).
     * <p>
     * <b>NOTE:</b> All <code>long</code> bit flags are defined in {@link SzFlags}
     * but you may want to use {@link #searchByAttributes(String, String, Set)} to
     * instead specify a {@link Set} of {@link SzFlag} instances.
     * 
     * @param attributes The search attributes defining the hypothetical record
     *                   to match and/or relate to in order to obtain the
     *                   search results.
     * 
     * @param searchProfile The optional search profile identifier, or 
     *                      <code>null</code> if the default search profile
     *                      should be used for the search.
     * 
     * @param flags The <code>long</code> {@link SzFlagUsageGroup#SZ_SEARCH} bit
     *              flags to control how the operation is performed and the content
     *              of the response.  Specify {@link SzFlags#SZ_NO_FLAGS} to pass
     *              no flags or {@link SzFlags#SZ_SEARCH_BY_ATTRIBUTES_DEFAULT_FLAGS}
     *              for the default recommended flags.
     * 
     * @return The resulting JSON {@link String} describing the result of the search.
     *
     * @throws SzException If a failure occurs.
     * 
     * @see #searchByAttributes(String, String, Set)
     * @see #searchByAttributes(String, long)
     * @see #searchByAttributes(String, Set)
     * 
     * @see SzFlags#SZ_SEARCH_BY_ATTRIBUTES_DEFAULT_FLAGS
     * @see SzFlags#SZ_SEARCH_BY_ATTRIBUTES_ALL
     * @see SzFlags#SZ_SEARCH_BY_ATTRIBUTES_STRONG
     * @see SzFlags#SZ_SEARCH_BY_ATTRIBUTES_MINIMAL_ALL
     * @see SzFlags#SZ_SEARCH_BY_ATTRIBUTES_MINIMAL_STRONG
     * @see SzFlagUsageGroup#SZ_SEARCH
     */
    String searchByAttributes(String attributes, 
                              String searchProfile,
                              long   flags);

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
     * @see #searchByAttributes(String, String, long)
     * @see #searchByAttributes(String, long)
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
                              Set<SzFlag>   flags);

    /**
     * This method is equivalent to calling {@link 
     * #searchByAttributes(String, String, long)} with a <code>null</code> value
     * for the search profile parameter.  See {@link 
     * #searchByAttributes(String, String, long)} documentation for details.
     * 
     * @param attributes The search attributes defining the hypothetical record
     *                   to match and/or relate to in order to obtain the
     *                   search results.
     * 
     * @param flags The <code>long</code> {@link SzFlagUsageGroup#SZ_SEARCH} bit
     *              flags to control how the operation is performed and the content
     *              of the response.  Specify {@link SzFlags#SZ_NO_FLAGS} to pass no
     *              flags or {@link SzFlags#SZ_SEARCH_BY_ATTRIBUTES_DEFAULT_FLAGS}
     *              for the default recommended flags.
     * 
     * @return The resulting JSON {@link String} describing the result of the search.
     *
     * @throws SzException If a failure occurs.
     * 
     * @see #searchByAttributes(String, String, long)
     * @see #searchByAttributes(String, String, Set)
     * @see #searchByAttributes(String, Set)
     * 
     * @see SzFlags#SZ_SEARCH_BY_ATTRIBUTES_DEFAULT_FLAGS
     * @see SzFlags#SZ_SEARCH_BY_ATTRIBUTES_ALL
     * @see SzFlags#SZ_SEARCH_BY_ATTRIBUTES_STRONG
     * @see SzFlags#SZ_SEARCH_BY_ATTRIBUTES_MINIMAL_ALL
     * @see SzFlags#SZ_SEARCH_BY_ATTRIBUTES_MINIMAL_STRONG
     * @see SzFlagUsageGroup#SZ_SEARCH
     */
    String searchByAttributes(String attributes, long flags);

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
     * @see #searchByAttributes(String, String, long)
     * @see #searchByAttributes(String, String, Set)
     * @see #searchByAttributes(String, long)
     * 
     * @see SzFlag#SZ_SEARCH_BY_ATTRIBUTES_DEFAULT_FLAGS
     * @see SzFlag#SZ_SEARCH_BY_ATTRIBUTES_ALL
     * @see SzFlag#SZ_SEARCH_BY_ATTRIBUTES_STRONG
     * @see SzFlag#SZ_SEARCH_BY_ATTRIBUTES_MINIMAL_ALL
     * @see SzFlag#SZ_SEARCH_BY_ATTRIBUTES_MINIMAL_STRONG
     * @see SzFlagUsageGroup#SZ_SEARCH
     */
    String searchByAttributes(String attributes, Set<SzFlag> flags);

    /**
     * This method is used to retrieve information about a specific resolved
     * entity that is identified by the specified entity ID. The result is
     * returned as a JSON document describing the entity. The level of detail
     * provided for the entity depends upon the specified bitwise flags.
     * <p>
     * The specified <code>long</code> bit flags may contain any bits, but only
     * those bit {@linkplain SzFlag#toLong() values} belonging to {@link SzFlag}
     * instances from the {@link SzFlagUsageGroup#SZ_ENTITY} group are guaranteed
     * to be recognized (other bits will be ignored).  <b>NOTE:</b> All 
     * <code>long</code> bit flags are defined in {@link SzFlags} but you may
     * want to use {@link #getEntity(long,Set)} to instead specify a {@link Set}
     * of {@link SzFlag} instances.
     * 
     * @param entityId The entity ID identifying the entity to retrieve.
     * 
     * @param flags The <code>long</code> {@link SzFlagUsageGroup#SZ_ENTITY} bit
     *              flags to control how the operation is performed and the content
     *              of the response.  Specify {@link SzFlags#SZ_NO_FLAGS} to pass
     *              no flags or {@link SzFlags#SZ_ENTITY_DEFAULT_FLAGS} for the
     *              default recommended flags.
     *
     * @return The JSON {@link String} describing the entity.
     * 
     * @throws SzNotFoundException If no enitty could be found with the
     *                             specified entity ID.
     * @throws SzException If a failure occurs.
     * 
     * @see #getEntity(long,Set)
     * 
     * @see SzFlags#SZ_ENTITY_DEFAULT_FLAGS
     * @see SzFlags#SZ_ENTITY_BRIEF_DEFAULT_FLAGS
     * @see SzFlagUsageGroup#SZ_ENTITY
     */
    String getEntity(long entityId, long flags)
        throws SzNotFoundException, SzException;

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
     * @see #getEntity(long,long)
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
     * entity depends upon the specified bitwise flags.
     * <p>
     * The specified <code>long</code> bit flags may contain any bits, but only
     * those bit {@linkplain SzFlag#toLong() values} belonging to {@link SzFlag}
     * instances from the {@link SzFlagUsageGroup#SZ_ENTITY} group are guaranteed
     * to be recognized (other bits will be ignored).  <b>NOTE:</b> All
     * <code>long</code> bit flags are defined in {@link SzFlags} but you may
     * want to use {@link #getEntity(long,Set)} to instead specify a {@link Set}
     * of {@link SzFlag} instances.
     * 
     * @param dataSourceCode The data source code of the constituent record for 
     *                       the entity to retrieve.
     * 
     * @param recordId The record ID of the constituent record for the entity
     *                 to retrieve.
     * 
     * @param flags The <code>long</code> {@link SzFlagUsageGroup#SZ_ENTITY} bit
     *              flags to control how the operation is performed and the content
     *              of the response.  Specify {@link SzFlags#SZ_NO_FLAGS} to pass
     *              no flags or {@link SzFlags#SZ_ENTITY_DEFAULT_FLAGS} for the
     *              default recommended flags.
     * 
     * @return The JSON {@link String} describing the entity.
     * 
     * @throws SzUnknownDataSourceExcpetion If an unrecognized data source
     *                                      code is specified.
     * 
     * @throws SzNotFoundException If no enitty could be found with the
     *                             specified entity ID.
     * 
     * @throws SzException If a failure occurs.
     * 
     * @see #getEntity(String,String,Set)
     * 
     * @see SzFlags#SZ_ENTITY_DEFAULT_FLAGS
     * @see SzFlags#SZ_ENTITY_BRIEF_DEFAULT_FLAGS
     * @see SzFlagUsageGroup#SZ_ENTITY
     */
    String getEntity(String dataSourceCode, String recordId, long flags)
        throws SzUnknownDataSourceException, SzNotFoundException, SzException;

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
     * @throws SzUnknownDataSourceExcpetion If an unrecognized data source
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
     * Finds a relationship path between two entities identified by their
     * entity ID's.
     * <p>
     * A JSON exclusions {@link String} may optionally be specified.  If
     * specified as non-null, then the exclusions {@link String} identifies
     * entities to be excluded from the path.  Entities are identified by 
     * their entity ID's or by the data source code and record ID pairs of
     * their consituent records in the following format:
     * <pre>
     *   {
     *     "ENTITIES": [
     *        { "ENTITY_ID": &lt;entity_id1&gt; },
     *        { "ENTITY_ID": &lt;entity_id2&gt; },
     *        . . .
     *        { "ENTITY_ID": &lt;entity_idN&gt; }
     *     ],
     *      "RECORDS": [
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
     * <p>
     * Further, a JSON required data sources {@link String} may optionally be
     * specified.  If specified as non-null, then the required data sources 
     * {@link String} contains data source codes that identify data sources 
     * for which a record from <b>at least one</b> must exist on the path.  The
     * data source codes are specified in the following format:
     * <pre>
     *    { 
     *      "DATA_SOURCES": [
     *        "&lt;data_source_code1&gt;",
     *        "&lt;data_source_code2&gt;",
     *        . . .
     *        "&lt;data_source_codeN&gt;"
     *      ]
     *    }
     * </pre>
     * <p>
     * The specified <code>long</code> bit flags not only control how the operation
     * is performed but also the level of detail provided for the path and the 
     * entities on the path.  Any bits may be set, but only those bit {@linkplain
     * SzFlag#toLong() values} belonging to {@link SzFlag} instances from the 
     * {@link SzFlagUsageGroup#SZ_FIND_PATH} group are guaranteed to be recognized
     * (other bits will be ignored).  <b>NOTE:</b> All <code>long</code> bit flags
     * are defined in {@link SzFlags} but you may want to use {@link 
     * #findPathByEntityId(long,long,int,String,String,Set)} to instead specify a
     * {@link Set} of {@link SzFlag} instances.
     *
     * @param startEntityId The entity ID of the entity at the start of the path.
     * 
     * @param endEntityId The entity ID of the entity at the end of the path.
     * 
     * @param maxDegrees The maximum number of degrees for the path search.
     * 
     * @param exclusions The optional JSON document containing entity ID's identifying
     *                   the entities to be excluded from the path, or <code>null</code>
     *                   if no entities are to be excluded from the path.
     * 
     * @param requiredDataSources The optional JSON document containing data source
     *                            codes identifying the data sources for which at
     *                            least one record must be included on the path, or
     *                            <code>null</code> if no data sources are required
     *                            for the path.
     * 
     * @param flags The <code>long</code> {@link SzFlagUsageGroup#SZ_FIND_PATH} bit
     *              flags to control how the operation is performed and the content
     *              of the response.  Specify {@link SzFlags#SZ_NO_FLAGS} to pass
     *              no flags or {@link SzFlags#SZ_FIND_PATH_DEFAULT_FLAGS} for the
     *              default recommended flags.
     * 
     * @return The JSON {@link String} describing the resultant entity path which
     *         may be an empty path if no path exists between the two entities
     *         given the path parameters.
     * 
     * @throws SzNotFoundException If either the path-start or path-end entities
     *                             for the specified entity ID's cannot be found.
     * 
     * @throws SzUnknownDataSourceExcpetion If an unrecognized required data source
     *                                      code is specified.
     * 
     * @throws SzException If a failure occurs.
     * 
     * @see SzFlags#SZ_FIND_PATH_DEFAULT_FLAGS
     * @see SzFlagUsageGroup#SZ_FIND_PATH
     * 
     * @see #findPathByEntityId(long,long,int,String,String,Set)
     * @see #findPathByEntityId(long,long,int,Set,Set,Set,long)
     * @see #findPathByEntityId(long,long,int,Set,Set,Set,Set)
     */
    String findPathByEntityId(long      startEntityId,
                              long      endEntityId,
                              int       maxDegrees,
                              String    exclusions,
                              String    requiredDataSources,
                              long      flags)
        throws SzNotFoundException, SzUnknownDataSourceException, SzException;

    /**
     * Finds a relationship path between two entities identified by their
     * entity ID's.
     * <p>
     * A JSON exclusions {@link String} may optionally be specified.  If
     * specified as non-null, then the exclusions {@link String} identifies
     * entities to be excluded from the path.  Entities are identified by 
     * their entity ID's or by the data source code and record ID pairs of
     * their consituent records in the following format:
     * <pre>
     *   {
     *     "ENTITIES": [
     *        { "ENTITY_ID": &lt;entity_id1&gt; },
     *        { "ENTITY_ID": &lt;entity_id2&gt; },
     *        . . .
     *        { "ENTITY_ID": &lt;entity_idN&gt; }
     *     ],
     *      "RECORDS": [
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
     * <p>
     * Further, a JSON required data sources {@link String} may optionally be
     * specified.  If specified as non-null, then the required data sources 
     * {@link String} contains data source codes that identify data sources 
     * for which a record from <b>at least one</b> must exist on the path.  The
     * data source codes are specified in the following format:
     * <pre>
     *    { "DATA_SOURCES": [
     *        "&lt;data_source_code1&gt;",
     *        "&lt;data_source_code2&gt;",
     *        . . .
     *        "&lt;data_source_codeN&gt;"
     *      ]
     *    }
     * </pre>
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
     * @param exclusions The optional JSON document containing entity ID's identifying
     *                   the entities to be excluded from the path, or <code>null</code>
     *                   if no entities are to be excluded from the path.
     * 
     * @param requiredDataSources The optional JSON document containing data source
     *                            codes identifying the data sources for which at
     *                            least one record must be included on the path, or
     *                            <code>null</code> if no data sources are required
     *                            for the path.
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
     * @throws SzUnknownDataSourceExcpetion If an unrecognized required data source
     *                                      is specified.
     * 
     * @throws SzException If a failure occurs.
     * 
     * @see #findPathByEntityId(long,long,int,String,String,long)
     * @see #findPathByEntityId(long,long,int,Set,Set,Set,long)
     * @see #findPathByEntityId(long,long,int,Set,Set,Set,Set)
     */
    String findPathByEntityId(long              startEntityId,
                              long              endEntityId,
                              int               maxDegrees,
                              String            exclusions,
                              String            requiredDataSources,
                              Set<SzFlag>       flags)
        throws SzNotFoundException, SzUnknownDataSourceException, SzException;

    /**
     * Finds a relationship path between two entities identified by their
     * entity ID's.
     * <p>
     * Entities to be excluded from the path may be specified in two ways.
     * First, a {@link Set} of {@link Long} entity ID's may optionally be
     * specified.  If specified as as non-null, then the exclusions {@link Set}
     * contains non-null {@link Long} entity ID's that identify entities to be
     * excluded from the path.  Secondly, exclusions may also be specified via
     * a {@link Set} of {@link SzRecordKey} instances.  If specified as non-null,
     * then the exclusions {@link Set} contains the non-null {@link SzRecordKey}
     * instances providing the data source code and record ID pairs that identify
     * the constituent records of entities to be excluded from the path.
     * <p>
     * Further, a JSON required data sources {@link Set} of {@link String} 
     * data source codes may optionally be specified.  If specified as non-null,
     * then the required data sources {@link Set} contains non-null {@link String}
     * data source codes that identify data sources for which a record from
     * <b>at least one</b> must exist on the path.
     * <p>
     * The specified <code>long</code> bit flags not only control how the operation
     * is performed but also the level of detail provided for the path and the 
     * entities on the path.  Any bits may be set, but only those bit {@linkplain
     * SzFlag#toLong() values} belonging to {@link SzFlag} instances from the 
     * {@link SzFlagUsageGroup#SZ_FIND_PATH} group will be recognized (other bits
     * will be ignored).  <b>NOTE:</b> All <code>long</code> bit flags are defined
     * in {@link SzFlags} but you may want to use {@link 
     * #findPathByEntityId(long,long,int,String,String,Set)} to instead specify a
     * {@link Set} of {@link SzFlag} instances.
     *
     * @param startEntityId The entity ID of the entity at the start of the path.
     * 
     * @param endEntityId The entity ID of the entity at the end of the path.
     * 
     * @param maxDegrees The maximum number of degrees for the path search.
     * 
     * @param entityIdExclusions The optional {@link Set} of non-null {@link Long}
     *                           entity ID's identifying entities to be excluded
     *                           from the path, or <code>null</code> if no entities
     *                           identified by entity ID are to be excluded.
     * 
     * @param recordIdExclusions The optional {@link Set} of non-null {@link
     *                           SzRecordKey} instances providing the data source
     *                           code and record ID pairs of the records whose 
     *                           entities are to be excluded from the path, or 
     *                           <code>null</code> if no entities identified by 
     *                           their consituent records are to be excluded.
     * 
     * @param requiredDataSources The optional {@link Set} of non-null {@link String}
     *                            data source codes identifying the data sources for
     *                            which at least one record must be included on the
     *                            path, or <code>null</code> if none are required.
     * 
     * @param flags The <code>long</code> {@link SzFlagUsageGroup#SZ_FIND_PATH} bit
     *              flags to control how the operation is performed and the content
     *              of the response.  Specify {@link SzFlags#SZ_NO_FLAGS} to pass
     *              no flags or {@link SzFlags#SZ_FIND_PATH_DEFAULT_FLAGS} for the
     *              default recommended flags.
     * 
     * @return The JSON {@link String} describing the resultant entity path which
     *         may be an empty path if no path exists between the two entities
     *         given the path parameters.
     * 
     * @throws SzNotFoundException If either the path-start or path-end entities
     *                             for the specified entity ID's cannot be found.
     * 
     * @throws SzUnknownDataSourceExcpetion If an unrecognized required data source
     *                                      code is specified.
     * 
     * @throws SzException If a failure occurs.
     * 
     * @see SzFlags#SZ_FIND_PATH_DEFAULT_FLAGS
     * @see SzFlagUsageGroup#SZ_FIND_PATH
     * 
     * @see #findPathByEntityId(long,long,int,String,String,long)
     * @see #findPathByEntityId(long,long,int,String,String,Set)
     * @see #findPathByEntityId(long,long,int,Set,Set,Set,Set)
     */
    String findPathByEntityId(long              startEntityId,
                              long              endEntityId,
                              int               maxDegrees,
                              Set<Long>         entityIdExclusions,
                              Set<SzRecordKey>  recordIdExclusions,
                              Set<String>       requiredDataSources,
                              long              flags)
        throws SzNotFoundException, SzUnknownDataSourceException, SzException;

    /**
     * Finds a relationship path between two entities identified by their
     * entity ID's.
     * <p>
     * Entities to be excluded from the path may be specified in two ways.
     * First, a {@link Set} of {@link Long} entity ID's may optionally be
     * specified.  If specified as as non-null, then the exclusions {@link Set}
     * contains non-null {@link Long} entity ID's that identify entities to be
     * excluded from the path.  Secondly, exclusions may also be specified via
     * a {@link Set} of {@link SzRecordKey} instances.  If specified as non-null,
     * then the exclusions {@link Set} contains the non-null {@link SzRecordKey}
     * instances providing the data source code and record ID pairs that identify
     * the constituent records of entities to be excluded from the path.
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
     * @param entityIdExclusions The optional {@link Set} of non-null {@link Long}
     *                           entity ID's identifying entities to be excluded
     *                           from the path, or <code>null</code> if no entities
     *                           identified by entity ID are to be excluded.
     * 
     * @param recordIdExclusions The optional {@link Set} of non-null {@link
     *                           SzRecordKey} instances providing the data source
     *                           code and record ID pairs of the records whose 
     *                           entities are to be excluded from the path, or 
     *                           <code>null</code> if no entities identified by 
     *                           their consituent records are to be excluded.
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
     * @throws SzUnknownDataSourceExcpetion If an unrecognized required data source
     *                                      is specified.
     * 
     * @throws SzException If a failure occurs.
     * 
     * @see SzFlag#SZ_FIND_PATH_DEFAULT_FLAGS
     * @see SzFlagUsageGroup#SZ_FIND_PATH
     * 
     * @see #findPathByEntityId(long,long,int,String,String,long)
     * @see #findPathByEntityId(long,long,int,String,String,Set)
     * @see #findPathByEntityId(long,long,int,Set,Set,Set,long)
     */
    String findPathByEntityId(long              startEntityId,
                              long              endEntityId,
                              int               maxDegrees,
                              Set<Long>         entityIdExclusions,
                              Set<SzRecordKey>  recordIdExclusions,
                              Set<String>       requiredDataSources,
                              Set<SzFlag>       flags)
        throws SzNotFoundException, SzUnknownDataSourceException, SzException;

    /**
     * Finds a relationship path between two entities identified by the
     * data source codes and record ID's of their constituent records.
     * <p>
     * A JSON exclusions {@link String} may optionally be specified.  If
     * specified as non-null, then the exclusions {@link String} identifies
     * entities to be excluded from the path.  Entities are identified by 
     * their entity ID's or by the data source code and record ID pairs of
     * their consituent records in the following format:
     * <pre>
     *   {
     *     "ENTITIES": [
     *        { "ENTITY_ID": &lt;entity_id1&gt; },
     *        { "ENTITY_ID": &lt;entity_id2&gt; },
     *        . . .
     *        { "ENTITY_ID": &lt;entity_idN&gt; }
     *     ],
     *      "RECORDS": [
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
     * <p>
     * Further, a JSON required data sources {@link String} may optionally be
     * specified.  If specified as non-null, then the required data sources 
     * {@link String} contains data source codes that identify data sources 
     * for which a record from <b>at least one</b> must exist on the path.  The
     * data source codes are specified in the following format:
     * <pre>
     *    { "DATA_SOURCES": [
     *        "&lt;data_source_code1&gt;",
     *        "&lt;data_source_code2&gt;",
     *        . . .
     *        "&lt;data_source_codeN&gt;"
     *      ]
     *    }
     * </pre>
     * <p>
     * The specified <code>long</code> bit flags not only control how the operation
     * is performed but also the level of detail provided for the path and the 
     * entities on the path.  Any bits may be set, but only those bit {@linkplain
     * SzFlag#toLong() values} belonging to {@link SzFlag} instances from the 
     * {@link SzFlagUsageGroup#SZ_FIND_PATH} group will be recognized (other bits
     * will be ignored).  <b>NOTE:</b> All <code>long</code> bit flags are defined
     * in {@link SzFlags} but you may want to use {@link 
     * #findPathByRecordId(String,String,String,String,int,String,String,Set)} to
     * instead specify a {@link Set} of {@link SzFlag} instances.
     *
     * @param startDataSourceCode The data source code of the record at the start
     *                            of the path.
     * 
     * @param startRecordId The record ID of the record at the start of the path.
     * 
     * @param endDataSourceCode The data source code of the record at the end of
     *                          the path.
     * 
     * @param endRecordId The record ID of the record at the end of the path.
     * 
     * @param maxDegrees The maximum number of degrees for the path search.
     * 
     * @param exclusions The optional JSON document containing data source code and
     *                   record ID pairs identifying the records to be excluded from
     *                   the path, or <code>null</code> if no records are to be
     *                   excluded from the path.
     * 
     * @param requiredDataSources The optional JSON document containing data source
     *                            codes identifying the data sources for which at
     *                            least one record must be included on the path, or
     *                            <code>null</code> if no data sources are required
     *                            for the path.
     * 
     * @param flags The <code>long</code> {@link SzFlagUsageGroup#SZ_FIND_PATH} bit
     *              flags to control how the operation is performed and the content
     *              of the response.  Specify {@link SzFlags#SZ_NO_FLAGS} to pass
     *              no flags or {@link SzFlags#SZ_FIND_PATH_DEFAULT_FLAGS} for the
     *              default recommended flags.
     * 
     * @return The JSON {@link String} describing the resultant entity path which
     *         may be an empty path if no path exists between the two entities
     *         given the path parameters.
     * 
     * @throws SzNotFoundException If either the path-start or path-end records
     *                             for the specified data source code and record ID
     *                             pairs cannot be found.
     * 
     * @throws SzUnknownDataSourceExcpetion If an unrecognized data source
     *                                      code is specified.
     * 
     * @throws SzException If a failure occurs.
     * 
     * @see SzFlags#SZ_FIND_PATH_DEFAULT_FLAGS
     * @see SzFlagUsageGroup#SZ_FIND_PATH
     * 
     * @see #findPathByRecordId(String,String,String,String,int,String,String,Set)
     * @see #findPathByRecordId(SzRecordKey,SzRecordKey,int,Set,Set,Set,long)
     * @see #findPathByRecordId(SzRecordKey,SzRecordKey,int,Set,Set,Set,Set)
     */
    String findPathByRecordId(String    startDataSourceCode,
                              String    startRecordId,
                              String    endDataSourceCode,
                              String    endRecordId,
                              int       maxDegrees,
                              String    exclusions,
                              String    requiredDataSources,
                              long      flags)
        throws SzNotFoundException, SzUnknownDataSourceException, SzException;

    /**
     * Finds a relationship path between two entities identified by the
     * data source codes and record ID's of their constituent records.
     * <p>
     * A JSON exclusions {@link String} may optionally be specified.  If
     * specified as non-null, then the exclusions {@link String} identifies
     * entities to be excluded from the path.  Entities are identified by 
     * their entity ID's or by the data source code and record ID pairs of
     * their consituent records in the following format:
     * <pre>
     *   {
     *     "ENTITIES": [
     *        { "ENTITY_ID": &lt;entity_id1&gt; },
     *        { "ENTITY_ID": &lt;entity_id2&gt; },
     *        . . .
     *        { "ENTITY_ID": &lt;entity_idN&gt; }
     *     ],
     *      "RECORDS": [
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
     * <p>
     * Further, a JSON required data sources {@link String} may optionally be
     * specified.  If specified as non-null, then the required data sources 
     * {@link String} contains data source codes that identify data sources 
     * for which a record from <b>at least one</b> must exist on the path.  The
     * data source codes are specified in the following format:
     * <pre>
     *    { "DATA_SOURCES": [
     *        "&lt;data_source_code1&gt;",
     *        "&lt;data_source_code2&gt;",
     *        . . .
     *        "&lt;data_source_codeN&gt;"
     *      ]
     *    }
     * </pre>
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
     * @param endDataSourceCode The data source code of the record at the end of
     *                          the path.
     * 
     * @param endRecordId The record ID of the record at the end of the path.
     * 
     * @param maxDegrees The maximum number of degrees for the path search.
     * 
     * @param exclusions The optional JSON document containing data source code and
     *                   record ID pairs identifying the records to be excluded from
     *                   the path, or <code>null</code> if no records are to be
     *                   excluded from the path.
     * 
     * @param requiredDataSources The optional JSON document containing data source
     *                            codes identifying the data sources for which at
     *                            least one record must be included on the path, or
     *                            <code>null</code> if no data sources are required
     *                            for the path.
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
     * @throws SzUnknownDataSourceExcpetion If an unrecognized data source
     *                                      code is specified.
     * 
     * @throws SzException If a failure occurs.
     * 
     * @see SzFlags#SZ_FIND_PATH_DEFAULT_FLAGS
     * @see SzFlagUsageGroup#SZ_FIND_PATH
     * 
     * @see #findPathByRecordId(String,String,String,String,int,String,String,long)
     * @see #findPathByRecordId(SzRecordKey,SzRecordKey,int,Set,Set,Set,long)
     * @see #findPathByRecordId(SzRecordKey,SzRecordKey,int,Set,Set,Set,Set)
     */
    String findPathByRecordId(String            startDataSourceCode,
                              String            startRecordId,
                              String            endDataSourceCode,
                              String            endRecordId,
                              int               maxDegrees,
                              String            exclusions,
                              String            requiredDataSources,
                              Set<SzFlag>       flags)
        throws SzNotFoundException, SzUnknownDataSourceException, SzException;

    /**
     * Finds a relationship path between two entities identified by the
     * data source codes and record ID's of their constituent records
     * given by the specified start and end {@link SzRecordKey} instances.
     * <p>
     * Entities to be excluded from the path may be specified in two ways.
     * First, a {@link Set} of {@link Long} entity ID's may optionally be
     * specified.  If specified as as non-null, then the exclusions {@link Set}
     * contains non-null {@link Long} entity ID's that identify entities to be
     * excluded from the path.  Secondly, exclusions may also be specified via
     * a {@link Set} of {@link SzRecordKey} instances.  If specified as non-null,
     * then the exclusions {@link Set} contains the non-null {@link SzRecordKey}
     * instances providing the data source code and record ID pairs that identify
     * the constituent records of entities to be excluded from the path.
     * <p>
     * Further, a JSON required data sources {@link Set} of {@link String} 
     * data source codes may optionally be specified.  If specified as non-null,
     * then the required data sources {@link Set} contains non-null {@link String}
     * data source codes that identify data sources for which a record from
     * <b>at least one</b> must exist on the path.
     * <p>
     * The specified <code>long</code> bit flags not only control how the operation
     * is performed but also the level of detail provided for the path and the 
     * entities on the path.  Any bits may be set, but only those bit {@linkplain
     * SzFlag#toLong() values} belonging to {@link SzFlag} instances from the 
     * {@link SzFlagUsageGroup#SZ_FIND_PATH} group will be recognized (other bits
     * will be ignored).  <b>NOTE:</b> All <code>long</code> bit flags are defined
     * in {@link SzFlags} but you may want to use {@link 
     * #findPathByRecordId(String,String,String,String,int,Set,Set,Set)} to instead
     * specify a {@link Set} of {@link SzFlag} instances.
     *
     * @param startRecordKey The {@link SzRecordKey} containing the data source
     *                       code and record ID identifying the record at the
     *                       start of the path.
     * 
     * @param endRecordKey The {@link SzRecordKey} containing the data source
     *                     code and record ID identifying the record at the end
     *                     of the path.
     * 
     * 
     * @param maxDegrees The maximum number of degrees for the path search.
     * 
     * @param entityIdExclusions The optional {@link Set} of non-null {@link Long}
     *                           entity ID's identifying entities to be excluded
     *                           from the path, or <code>null</code> if no entities
     *                           identified by entity ID are to be excluded.
     * 
     * @param recordIdExclusions The optional {@link Set} of non-null {@link
     *                           SzRecordKey} instances providing the data source
     *                           code and record ID pairs of the records whose 
     *                           entities are to be excluded from the path, or 
     *                           <code>null</code> if no entities identified by 
     *                           their consituent records are to be excluded.
     * 
     * @param requiredDataSources The optional {@link Set} of non-null {@link String}
     *                            data source codes identifying the data sources for
     *                            which at least one record must be included on the
     *                            path, or <code>null</code> if none are required.
     * 
     * @param flags The <code>long</code> {@link SzFlagUsageGroup#SZ_FIND_PATH} bit
     *              flags to control how the operation is performed and the content
     *              of the response.  Specify {@link SzFlags#SZ_NO_FLAGS} to pass
     *              no flags or {@link SzFlags#SZ_FIND_PATH_DEFAULT_FLAGS} for the
     *              default recommended flags.
     * 
     * @return The JSON {@link String} describing the resultant entity path which
     *         may be an empty path if no path exists between the two entities
     *         given the path parameters.
     * 
     * @throws SzNotFoundException If either the path-start or path-end records
     *                             for the specified data source code and record ID
     *                             pairs cannot be found.
     * 
     * @throws SzUnknownDataSourceExcpetion If an unrecognized data source
     *                                      code is specified.
     * 
     * @throws SzException If a failure occurs.
     * 
     * @see SzFlags#SZ_FIND_PATH_DEFAULT_FLAGS
     * @see SzFlagUsageGroup#SZ_FIND_PATH
     * 
     * @see #findPathByRecordId(String,String,String,String,int,String,String,Set)
     * @see #findPathByRecordId(String,String,String,String,int,String,String,long)
     * @see #findPathByRecordId(SzRecordKey,SzRecordKey,int,Set,Set,Set,Set)
     */
    String findPathByRecordId(SzRecordKey       startRecordKey,
                              SzRecordKey       endRecordKey,
                              int               maxDegrees,
                              Set<Long>         entityIdExclusions,
                              Set<SzRecordKey>  recordIdExclusions,
                              Set<String>       requiredDataSources,
                              long              flags)
        throws SzNotFoundException, SzUnknownDataSourceException, SzException;

    /**
     * Finds a relationship path between two entities identified by the
     * data source codes and record ID's of their constituent records
     * given by the specified start and end {@link SzRecordKey} instances.
     * <p>
     * Entities to be excluded from the path may be specified in two ways.
     * First, a {@link Set} of {@link Long} entity ID's may optionally be
     * specified.  If specified as as non-null, then the exclusions {@link Set}
     * contains non-null {@link Long} entity ID's that identify entities to be
     * excluded from the path.  Secondly, exclusions may also be specified via
     * a {@link Set} of {@link SzRecordKey} instances.  If specified as non-null,
     * then the exclusions {@link Set} contains the non-null {@link SzRecordKey}
     * instances providing the data source code and record ID pairs that identify
     * the constituent records of entities to be excluded from the path.
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
     * @param recordIdExclusions The optional {@link Set} of non-null {@link
     *                           SzRecordKey} instances providing the data source
     *                           code and record ID pairs of the records whose 
     *                           entities are to be excluded from the path, or 
     *                           <code>null</code> if no entities identified by 
     *                           their consituent records are to be excluded.
     * 
     * @param requiredDataSources The optional {@link Set} of non-null {@link String}
     *                            data source codes identifying the data sources for
     *                            which at least one record must be included on the
     *                            path, or <code>null</code> if none are required.
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
     * @throws SzUnknownDataSourceExcpetion If an unrecognized data source
     *                                      code is specified.
     * 
     * @throws SzException If a failure occurs.
     * 
     * @see SzFlag#SZ_FIND_PATH_DEFAULT_FLAGS
     * @see SzFlagUsageGroup#SZ_FIND_PATH
     * 
     * @see #findPathByRecordId(String,String,String,String,int,String,String,Set)
     * @see #findPathByRecordId(String,String,String,String,int,String,String,long)
     * @see #findPathByRecordId(SzRecordKey,SzRecordKey,int,Set,Set,Set,long)
     */
    String findPathByRecordId(SzRecordKey       startRecordKey,
                              SzRecordKey       endRecordKey,
                              int               maxDegrees,
                              Set<Long>         entityIdExclusions,
                              Set<SzRecordKey>  recordIdExclusions,
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
     * The specified <code>long</code> bit flags not only control how the operation
     * is performed but also the level of detail provided for the network paths and
     * the entities on the network.  Any bits may be set, but only those bit
     * {@linkplain SzFlag#toLong() values} belonging to {@link SzFlag} instances
     * from the {@link SzFlagUsageGroup#SZ_FIND_NETWORK} group are guaranteed to be
     * recognized (other bits will be ignored).  <b>NOTE:</b> All <code>long</code>
     * bit flags are defined in {@link SzFlags} but you may want to use {@link 
     * #findNetworkByEntityId(Set,int,int,int,Set)} to instead specify a {@link Set}
     * of {@link SzFlag} instances.
     * 
     *
     * @param entityList The JSON document specififying the entity ID's of the
     *                   desired entities.
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
     * @param flags The <code>long</code> {@link SzFlagUsageGroup#SZ_FIND_NETWORK}
     *              bit flags to control how the operation is performed and the
     *              content of the response.  Specify {@link SzFlags#SZ_NO_FLAGS}
     *              to pass no flags or {@link SzFlags#SZ_FIND_NETWORK_DEFAULT_FLAGS}
     *              for the default recommended flags.
     * 
     * @return The JSON {@link String} describing the resultant entity network
     *         and the entities on the network.
     * 
     * @throws SzNotFoundException If any of the entities for the specified
     *                             entity ID's cannot be found.
     * 
     * @throws SzException If a failure occurs.
     * 
     * @see SzFlags#SZ_FIND_NETWORK_DEFAULT_FLAGS
     * @see SzFlagUsageGroup#SZ_FIND_NETWORK
     * 
     * @see #findNetworkByEntityId(Set,int,int,int,Set)
     */
    String findNetworkByEntityId(Set<Long>      entityList,
                                 int            maxDegrees,
                                 int            buildOutDegrees,
                                 int            buildOutMaxEntities,
                                 long           flags)
        throws SzNotFoundException, SzException;

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
     * @param entityList The JSON document specififying the entity ID's of the
     *                   desired entities.
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
     * @param flags The <code>long</code> {@link SzFlagUsageGroup#SZ_FIND_NETWORK}
     *              bit flags to control how the operation is performed and the
     *              content of the response.  Specify {@link SzFlags#SZ_NO_FLAGS}
     *              to pass no flags or {@link SzFlags#SZ_FIND_NETWORK_DEFAULT_FLAGS}
     *              for the default recommended flags.
     * 
     * @return The JSON {@link String} describing the resultant entity network:quitq
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
     * @see #findNetworkByEntityId(Set,int,int,int,long)
     */
    String findNetworkByEntityId(Set<Long>      entityList,
                                 int            maxDegrees,
                                 int            buildOutDegrees,
                                 int            buildOutMaxEntities,
                                 Set<SzFlag>    flags)
        throws SzNotFoundException, SzException;

    /**
     * Finds a network of entity relationships surrounding the paths between
     * a set of entities having one or more consituent records identified by
     * pairs of data source codes and record ID's specified in a JSON
     * {@link String} in the following format:
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
     * <p>
     * Additionally, the maximum degrees of separation for the paths between entities
     * must be specified so as to prevent the network growing beyond the desired size.
     * Further, a non-zero number of degrees to build out the network may be specified
     * to find other related entities.  If build out is specified, it can be limited
     * to a maximum total number of build-out entities for the whole network.
     * <p>
     * The specified <code>long</code> bit flags not only control how the operation
     * is performed but also the level of detail provided for the network paths and
     * the entities on the network.  Any bits may be set, but only those bit
     * {@linkplain SzFlag#toLong() values} belonging to {@link SzFlag} instances
     * from the {@link SzFlagUsageGroup#SZ_FIND_NETWORK} group are guaranteed to be
     * recognized (other bits will be ignored).  <b>NOTE:</b> All <code>long</code>
     * bit flags are defined in {@link SzFlags} but you may want to use {@link 
     * #findNetwork(String,int,int,int,Set)} to instead specify a {@link Set}
     * of {@link SzFlag} instances.
     *
     * @param recordList The JSON document containing the data source code and
     *                   record ID pairs for the composite constituent records
     *                   of the desired entities for the network.
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
     * @param flags The <code>long</code> {@link SzFlagUsageGroup#SZ_FIND_NETWORK}
     *              bit flags to control how the operation is performed and the
     *              content of the response.  Specify {@link SzFlags#SZ_NO_FLAGS}
     *              to pass no flags or {@link SzFlags#SZ_FIND_NETWORK_DEFAULT_FLAGS}
     *              for the default recommended flags.
     * 
     * @return The JSON {@link String} describing the resultant entity network
     *         and the entities on the network.
     * 
     * @throws SzUnknownDataSourceExcpetion If an unrecognized data source
     *                                      code is specified.
     * 
     * @throws SzNotFoundException If any of the entities for the specified
     *                             entity ID's cannot be found.
     * 
     * @throws SzException If a failure occurs.
     * 
     * @see SzFlags#SZ_FIND_NETWORK_DEFAULT_FLAGS
     * @see SzFlagUsageGroup#SZ_FIND_NETWORK
     * 
     * @see #findNetwork(String,int,int,int,Set)
     * @see #findNetwork(Set,int,int,int,long)
     * @see #findNetwork(Set,int,int,int,Set)
     */
    int findNetworkByRecordIds(String    recordList,
                              int       maxDegrees,
                              int       buildOutDegrees,
                              int       maxEntities,
                              long      flags)
        throws SzUnknownDataSourceException, SzNotFoundException, SzException;

    /**
     * This method determines why a particular record is included in its resolved
     * entity.
     *
     * @param dataSourceCode The data source code for the composite record of the
     *                       subject entity.
     * @param recordId       The record ID for the composite record of the subject
     *                       entity.
     * @param response       The {@link StringBuffer} to write the JSON response
     *                       document to.
     * @return Zero (0) on success and non-zero on failure.
     */
    int whyRecordInEntity(String dataSourceCode,
            String recordId,
            StringBuffer response);

    /**
     * This method determines why a particular record is included in its resolved
     * entity.
     *
     * @param dataSourceCode The data source code for the composite record of the
     *                       subject entity.
     * @param recordId       The record ID for the composite record of the subject
     *                       entity.
     * @param flags          The flags to control how the operation is performed and
     *                       specifically the content of the response JSON document.
     * @param response       The {@link StringBuffer} to write the JSON response
     *                       document to.
     * @return Zero (0) on success and non-zero on failure.
     */
    int whyRecordInEntity(String dataSourceCode,
            String recordId,
            long flags,
            StringBuffer response);

    /**
     * This method determines how two records are related to each other.
     *
     * @param dataSourceCode1 The data source code for the first record.
     * @param recordId1       The record ID for the first record.
     * @param dataSourceCode2 The data source code for the second record.
     * @param recordId2       The record ID for the second record.
     * @param response        The {@link StringBuffer} to write the JSON response
     *                        document to.
     * @return Zero (0) on success and non-zero on failure.
     */
    int whyRecords(String dataSourceCode1,
            String recordId1,
            String dataSourceCode2,
            String recordId2,
            StringBuffer response);

    /**
     * This method determines how two records are related to each other.
     *
     * @param dataSourceCode1 The data source code for the first record.
     * @param recordId1       The record ID for the first record.
     * @param dataSourceCode2 The data source code for the second record.
     * @param recordId2       The record ID for the second record.
     * @param flags           The flags to control how the operation is performed
     *                        and
     *                        specifically the content of the response JSON
     *                        document.
     * @param response        The {@link StringBuffer} to write the JSON response
     *                        document to.
     * @return Zero (0) on success and non-zero on failure.
     */
    int whyRecords(String dataSourceCode1,
            String recordId1,
            String dataSourceCode2,
            String recordId2,
            long flags,
            StringBuffer response);

    /**
     * This method determines how two entities are related to each other.
     *
     * @param entityId1 The entity ID of the first entity.
     * @param entityId2 The entity ID of the second entity.
     * @param response  The {@link StringBuffer} to write the JSON response
     *                  document to.
     * @return Zero (0) on success and non-zero on failure.
     */
    int whyEntities(long entityId1, long entityId2, StringBuffer response);

    /**
     * This method determines how two entities are related to each other.
     *
     * @param entityId1 The entity ID of the first entity.
     * @param entityId2 The entity ID of the second entity.
     * @param flags     The flags to control how the operation is performed and
     *                  specifically the content of the response JSON document.
     * @param response  The {@link StringBuffer} to write the JSON response
     *                  document to.
     * @return Zero (0) on success and non-zero on failure.
     */
    int whyEntities(long entityId1,
            long entityId2,
            long flags,
            StringBuffer response);

    /**
     * This method gives information on how entities were constructed from
     * their base records.
     *
     * @param entityId The entity ID.
     * @param response The {@link StringBuffer} to write the JSON response
     *                 document to.
     * @return Zero (0) on success and non-zero on failure.
     */
    int howEntityByEntityID(long entityId,
            StringBuffer response);

    /**
     * This method gives information on how entities were constructed from
     * their base records.
     *
     * @param entityId The entity ID.
     * @param flags    The flags to control how the operation is performed and
     *                 specifically the content of the response JSON document.
     * @param response The {@link StringBuffer} to write the JSON response
     *                 document to.
     * @return Zero (0) on success and non-zero on failure.
     */
    int howEntityByEntityID(long entityId,
            long flags,
            StringBuffer response);

    /**
     * This method gives information on how an entity composed of a given set
     * of records would look.
     *
     * @param recordList The list of records used to build the virtual entity.
     * @param response   The {@link StringBuffer} to write the JSON response
     *                   document to.
     * @return Zero (0) on success and non-zero on failure.
     */
    int getVirtualEntityByRecordID(String recordList,
            StringBuffer response);

    /**
     * This method gives information on how an entity composed of a given set
     * of records would look.
     *
     * @param recordList The list of records used to build the virtual entity.
     * @param flags      The flags to control how the operation is performed and
     *                   specifically the content of the response JSON document.
     * @param response   The {@link StringBuffer} to write the JSON response
     *                   document to.
     * @return Zero (0) on success and non-zero on failure.
     */
    int getVirtualEntityByRecordID(String recordList,
            long flags,
            StringBuffer response);

    /**
     * This method is used to retrieve the stored record.
     *
     * @param dataSourceCode The data source of the observation to search for
     * @param recordId       The record ID of the observation to search for
     * @param response       A {@link StringBuffer} for returning the response
     *                       document.
     *                       If an error occurred, an error response is stored here.
     *
     * @return Zero (0) on success and non-zero on failure.
     */
    int getRecord(String dataSourceCode, String recordId, StringBuffer response);

    /**
     * This method is used to retrieve the stored record.
     *
     * @param dataSourceCode The data source of the observation to search for
     * @param recordId       The record ID of the observation to search for
     * @param flags          The flags to control how the operation is performed and
     *                       specifically the content of the response JSON document.
     * @param response       A {@link StringBuffer} for returning the response
     *                       document.
     *                       If an error occurred, an error response is stored here.
     *
     * @return Zero (0) on success and non-zero on failure.
     */
    int getRecord(String dataSourceCode,
            String recordId,
            long flags,
            StringBuffer response);

    /**
     * This is used to export entity data from known entities. This function
     * returns an export-handle that can be read from to get the export data
     * in JSON format. The export-handle should be read using the "G2_fetchNext"
     * function, and closed when work is complete. Each output row contains the
     * exported entity data for a single resolved entity.
     *
     * @param flags        A bit mask specifying control flags. The default and
     *                     recommended
     *                     value is "G2_EXPORT_DEFAULT_FLAGS".
     * @param exportHandle The {@link Result} object for storing the export
     *                     handle.
     * @return Zero (0) on success and non-zero on failure.
     */
    int exportJSONEntityReport(long flags, Result<Long> exportHandle);

    /**
     * This is used to export entity data from known entities. This function
     * returns an export-handle that can be read from to get the export data
     * in CSV format. The export-handle should be read using the "G2_fetchNext"
     * function, and closed when work is complete. The first output row returned
     * by the export-handle contains the JSON column headers as a string. Each
     * following row contains the exported entity data.
     *
     * @param csvColumnList Specify <code>"*"</code> to indicate "all columns",
     *                      specify empty-string to indicate the "standard
     *                      columns", otherwise specify a comma-sepatated list of
     *                      column names.
     * @param flags         A bit mask specifying other control flags. The default
     *                      and recommended
     *                      value is "G2_EXPORT_DEFAULT_FLAGS".
     * @param exportHandle  The {@link Result} object for storing the export
     *                      handle.
     *
     * @return Returns an export handle that the entity data can be read from.
     */
    int exportCSVEntityReport(String csvColumnList,
            long flags,
            Result<Long> exportHandle);

    /**
     * This function is used to read entity data from an export handle,
     * one data row at a time.
     *
     * @param exportHandle The export handle to retrieve data from
     * @param response     The {@link StringBuffer} to write the next exported
     *                     record to.
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
     * @param redoRecord The record to be processed.
     * @return Zero (0) on success and non-zero on failure.
     */
    int processRedoRecord(String redoRecord);

    /**
     * Processes a redo record.
     *
     * @param redoRecord The record to be processed.
     * @param response   A {@link StringBuffer} for returning the response document.
     *                   If an error occurred, an error response is stored here.
     *
     * @return Zero (0) on success and non-zero on failure.
     */
    int processRedoRecordWithInfo(String redoRecord,
            StringBuffer response);

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

}
