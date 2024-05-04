package com.senzing.g2.engine;

import java.util.Set;

import static com.senzing.g2.engine.SzException.*;
import static com.senzing.g2.engine.SzFlagUsageGroup.*;
import static com.senzing.g2.engine.Utilities.*;
import static com.senzing.g2.engine.SzFlag.*;

/**
 * The package-protected implementation of {@link SzEngine} that works
 * with the {@link SzCoreEnvironment} class.
 */
public class SzCoreEngine implements SzEngine {
    /**
     * Gets the class prefix to use for {@link SzException} construction.
     */
    private static final String CLASS_PREFIX = SzCoreEngine.class.getSimpleName();

    /**
     * The mask for removing SDK-specific flags that don't go downstream.
     */
    private static final long SDK_FLAG_MASK = ~(SzFlags.SZ_WITH_INFO);

    /**
     * The empty response for operations where the info can optionally
     * generated but was not requested.
     */
    private static final String NO_INFO = "{}";

    /**
     * The {@link SzCoreEnvironment} that constructed this instance.
     */
    private SzCoreEnvironment env = null;

    /**
     * The underlying {@link G2G2JNI} instance.
     */
    G2JNI nativeApi = null;

    /**
     * Constructs with the specified {@link SzCoreEnvironment}.
     * 
     * @param environment
     *            The {@link SzCoreEnvironment} with which to
     *            construct.
     * 
     * @throws IllegalStateException
     *             If the underlying {@link SzCoreEnvironment} instance
     *             has already been destroyed.
     * @throws SzException
     *             If a Senzing failure occurs during initialization.
     */
    SzCoreEngine(SzCoreEnvironment environment)
            throws IllegalStateException, SzException {
        this.env = environment;
        this.env.execute(() -> {
            this.nativeApi = new G2JNI();

            // check if we are initializing with a config ID
            if (this.env.getConfigId() == null) {
                // if not then call the basic init
                int returnCode = this.nativeApi.init(
                        this.env.getInstanceName(),
                        this.env.getSettings(),
                        this.env.isVerboseLogging());

                // handle any failure
                if (returnCode != 0) {
                    this.env.handleReturnCode(
                        returnCode, this.nativeApi,
                        CLASS_PREFIX + "()",
                        paramsOf("instanceName", this.env.getInstanceName(),
                                "settings", redact(this.env.getSettings()),
                                "verboseLogging", this.env.isVerboseLogging()));
                }

            } else {
                // if so then call init with config ID
                int returnCode = this.nativeApi.initWithConfigID(
                        this.env.getInstanceName(),
                        this.env.getSettings(),
                        this.env.getConfigId(),
                        this.env.isVerboseLogging());

                // handle any failure
                if (returnCode != 0) {
                    this.env.handleReturnCode(
                        returnCode, this.nativeApi,
                        CLASS_PREFIX + "()",
                        paramsOf("instanceName", this.env.getInstanceName(),
                                "settings", redact(this.env.getSettings()),
                                "configId", this.env.getConfigId(),
                                "verboseLogging", this.env.isVerboseLogging()));
                }
            }

            return null;
        });
    }

    /**
     * The package-protected function to destroy the Senzing Config SDK.
     */
    void destroy() {
        synchronized (this) {
            if (this.nativeApi == null)
                return;
            this.nativeApi.destroy();
            this.nativeApi = null;
        }
    }

    /**
     * Checks if this instance has been destroyed by the associated
     * {@link SzEnvironment}.
     * 
     * @return <code>true</code> if this instance has been destroyed,
     *         otherwise <code>false</code>.
     */
    protected boolean isDestroyed() {
        synchronized (this) {
            return (this.nativeApi == null);
        }
    }

    @Override
    public String addRecord(String      dataSourceCode, 
                            String      recordId,
                            String      recordDefinition,
                            Set<SzFlag> flags)
        throws SzUnknownDataSourceException,
               SzBadInputException,
               SzException 
    {
        return this.env.execute(() -> {
            // clear out the SDK-specific flags
            long downstreamFlags = SzFlag.toLong(flags) & SDK_FLAG_MASK;

            int returnCode = 0;
            String result = NO_INFO;
            // check if we have flags to pass downstream or need info
            if (downstreamFlags == 0L && !flags.contains(SZ_WITH_INFO)) {
                // no info needed, no flags to pass, go simple
                returnCode = this.nativeApi.addRecord(dataSourceCode,
                                                      recordId,
                                                      recordDefinition);

            } else {
                // we either need info or have flags or both
                StringBuffer sb = new StringBuffer();
                returnCode = this.nativeApi.addRecordWithInfo(dataSourceCode, 
                                                              recordId, 
                                                              recordDefinition, 
                                                              downstreamFlags, 
                                                              sb);

                // set the info result if requested
                if (flags.contains(SZ_WITH_INFO)) result = sb.toString();
            }

            // check the return code
            if (returnCode != 0) {
                this.env.handleReturnCode(
                    returnCode, this.nativeApi,
                    CLASS_PREFIX + ".addRecord(String,String,String,Set<SzFlag>)",
                    paramsOf("dataSourceCode", dataSourceCode,
                             "recordId", recordId,
                             "recordDefinition", redact(recordDefinition),
                             "flags", SzFlag.toString(flags),
                             "[downstreamFlags]", 
                             SZ_MODIFY.toString(downstreamFlags)));
            }

            // return the result
            return result;
        });
    }

    @Override
    public String addRecord(SzRecordKey recordKey,
                            String      recordDefinition,
                            Set<SzFlag> flags)
        throws SzUnknownDataSourceException,
               SzBadInputException, 
               SzException
    {
        return this.addRecord(recordKey.dataSourceCode(),
                              recordKey.recordId(),
                              recordDefinition,
                              flags);
    }

    @Override
    public void closeExport(long exportHandle) throws SzException {
        this.env.execute(() -> {
            int returnCode = this.nativeApi.closeExport(exportHandle);

            if (returnCode != 0) {
                this.env.handleReturnCode(
                    returnCode, this.nativeApi,
                    CLASS_PREFIX + ".closeExport(long)",
                    paramsOf("exportHandle", hexFormat(exportHandle)));
            }

            return null;
        });
    }

    @Override
    public long countRedoRecords() throws SzException {
        return this.env.execute(() -> {
            long count = this.nativeApi.countRedoRecords();

            if (count < 0) {
                this.env.handleReturnCode(
                    (int) count, this.nativeApi,
                    CLASS_PREFIX + ".countRedoRecords()");
            }

            // return the count
            return count;
        });
    }

    @Override
    public String deleteRecord(String       dataSourceCode,
                               String       recordId,
                               Set<SzFlag>  flags)
            throws SzUnknownDataSourceException, SzException 
    {
        return this.env.execute(() -> {
            // clear out the SDK-specific flags
            long downstreamFlags = SzFlag.toLong(flags) & SDK_FLAG_MASK;

            int returnCode = 0;
            String result = NO_INFO;
            // check if we have flags to pass downstream or need info
            if (downstreamFlags == 0L && !flags.contains(SZ_WITH_INFO)) {
                // no info needed, no flags to pass, go simple
                returnCode = this.nativeApi.deleteRecord(
                    dataSourceCode, recordId);

            } else {
                // we either need info or have flags or both
                StringBuffer sb = new StringBuffer();
                returnCode = this.nativeApi.deleteRecordWithInfo(
                    dataSourceCode, recordId, downstreamFlags, sb);

                // set the info result if requested
                if (flags.contains(SZ_WITH_INFO)) result = sb.toString();
            }

            // check the return code
            if (returnCode != 0) {
                this.env.handleReturnCode(
                    returnCode, this.nativeApi,
                    CLASS_PREFIX + ".deleteRecord(String,String,Set<SzFlag>)",
                    paramsOf("dataSourceCode", dataSourceCode,
                             "recordId", recordId,
                             "flags", SzFlag.toString(flags),
                             "[downstreamFlags]", 
                             SZ_MODIFY.toString(downstreamFlags)));
            }

            // return the result
            return result;
        });
    }

    @Override
    public String deleteRecord(SzRecordKey recordKey, Set<SzFlag> flags)
            throws SzUnknownDataSourceException, SzException 
    {
        return this.deleteRecord(
            recordKey.dataSourceCode(), recordKey.recordId(), flags);
    }

    @Override
    public long exportCsvEntityReport(String csvColumnList, Set<SzFlag> flags)
            throws SzException 
    {
        // clear out the SDK-specific flags
        long downstreamFlags = SzFlag.toLong(flags) & SDK_FLAG_MASK;

        return this.env.execute(() -> {
            Result<Long> result = new Result<>();

            int returnCode = this.nativeApi.exportCSVEntityReport(
                csvColumnList, downstreamFlags, result);

            // check the return code
            if (returnCode != 0) {
                this.env.handleReturnCode(
                    returnCode, this.nativeApi,
                    CLASS_PREFIX + ".exportCsvEntityReport(String,Set<SzFlag>)",
                    paramsOf("csvColumnList", csvColumnList,
                             "flags", SzFlag.toString(flags),
                             "[downstreamFlags]", 
                             SZ_EXPORT.toString(downstreamFlags)));
            }

            return result.getValue();
        });
    }

    @Override
    public long exportJsonEntityReport(Set<SzFlag> flags) throws SzException {
        // clear out the SDK-specific flags
        long downstreamFlags = SzFlag.toLong(flags) & SDK_FLAG_MASK;

        return this.env.execute(() -> {
            Result<Long> result = new Result<>();

            int returnCode = this.nativeApi.exportJSONEntityReport(
                downstreamFlags, result);

            // check the return code
            if (returnCode != 0) {
                this.env.handleReturnCode(
                    returnCode, this.nativeApi,
                    CLASS_PREFIX + ".exportJsonEntityReport(Set<SzFlag>)",
                    paramsOf("flags", SzFlag.toString(flags),
                             "[downstreamFlags]", 
                             SZ_EXPORT.toString(downstreamFlags)));
            }

            return result.getValue();
        });
    }

    @Override
    public String fetchNext(long exportHandle) throws SzException {
        return this.env.execute(() -> {
            StringBuffer sb = new StringBuffer();

            int returnCode = this.nativeApi.fetchNext(exportHandle, sb);

            if (returnCode != 0) {
                this.env.handleReturnCode(
                    returnCode, this.nativeApi,
                    CLASS_PREFIX + ".fetchNext(long)",
                    paramsOf("exportHandle", hexFormat(exportHandle)));
            }

            // return the count
            return sb.toString();
        });
    }

    /**
     * Encodes the {@link Set} of {@link Long} entity ID's as JSON.
     * The JSON is formatted as:
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
     * @param entityIds The non-null {@link Set} of non-null {@link Long}
     *                  entity ID's.
     * 
     * @return The encoded JSON string of entity ID's.
     */
    protected static String encodeEntityIds(Set<Long> entityIds) {
        StringBuilder sb = new StringBuilder();
        sb.append("{\"ENTITIES\": [");
        if (entityIds != null) {
            String prefix = "";
            for (Long entityId : entityIds) {
                sb.append(prefix);
                sb.append("{\"ENTITY_ID\":").append(entityId).append("}");
                prefix = ",";
            }
        }
        sb.append("]}");
        return sb.toString();
    }

    /**
     * Encodes the {@link Set} of {@link SzRecordKey} instances as JSON.
     * The JSON is formatted as:
     * <pre>
     *   {
     *     "RECORDS": [
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
     * @param recordKeys The non-null {@link Set} of non-null
     *                   {@link SzRecordKey} instances.
     * 
     * @return The encoded JSON string of record keys.
     */
    protected static String encodeRecordKeys(Set<SzRecordKey> recordKeys) {
        StringBuilder sb = new StringBuilder();
        sb.append("{\"RECORDS\": [");
        if (recordKeys != null) {
            String prefix = "";
            for (SzRecordKey recordKey : recordKeys) {
                String dataSourceCode   = (recordKey == null) ? null : recordKey.dataSourceCode();
                String recordId         = (recordKey == null) ? null : recordKey.recordId();
                sb.append(prefix);
                sb.append("{\"DATA_SOURCE\":");
                sb.append(jsonEscape(dataSourceCode));
                sb.append(",\"RECORD_ID\":");
                sb.append(jsonEscape(recordId));
                sb.append("}");
                prefix = ",";
            }
        }
        sb.append("]}");
        return sb.toString();
    }

    /**
     * Encodes the {@link Set} of {@link SzRecordKey} instances as JSON.
     * The JSON is formatted as:
     * <pre>
     *    { "DATA_SOURCES": [
     *        "&lt;data_source_code1&gt;",
     *        "&lt;data_source_code2&gt;",
     *        . . .
     *        "&lt;data_source_codeN&gt;"
     *      ]
     *    }
     * </pre>
     * @param dataSources The {@link Set} of {@link String} data source codes.
     * 
     * @return The encoded JSON string of record keys.
     */
    protected static String encodeDataSources(Set<String> dataSources) {
        StringBuilder sb = new StringBuilder();
        sb.append("{\"DATA_SOURCES\": [");
        if (dataSources != null) {
            String prefix = "";
            for (String dataSourceCode : dataSources) {
                sb.append(prefix);
                sb.append(jsonEscape(dataSourceCode));
                prefix = ",";
            }
        }
        sb.append("]}");
        return sb.toString();
    }

    @Override
    public String findNetworkByEntityId(Set<Long>   entityIds, 
                                        int         maxDegrees,
                                        int         buildOutDegrees,
                                        int         buildOutMaxEntities,
                                        Set<SzFlag> flags)
            throws SzNotFoundException, SzException 
    {        
        // clear out the SDK-specific flags
        long downstreamFlags = SzFlag.toLong(flags) & SDK_FLAG_MASK;

        return this.env.execute(() -> {
            StringBuffer sb = new StringBuffer();

            String jsonEntityIds = encodeEntityIds(entityIds);

            int returnCode = this.nativeApi.findNetworkByEntityID(
                jsonEntityIds,
                maxDegrees,
                buildOutDegrees,
                buildOutMaxEntities,
                downstreamFlags,
                sb);

            // check the return code
            if (returnCode != 0) {
                this.env.handleReturnCode(
                    returnCode, this.nativeApi,
                    CLASS_PREFIX + ".findNetworkByEntityId(Set<Long>,int,int,int,Set<SzFlag>)",
                    paramsOf(
                        "entityIds", entityIds,
                        "maxDegrees", maxDegrees,
                        "buildOutDegrees", buildOutDegrees,
                        "buildOutMaxEntities", buildOutMaxEntities,
                        "flags", SzFlag.toString(flags),
                        "[downstreamFlags]",
                        SZ_FIND_NETWORK.toString(downstreamFlags)));
                }

            return sb.toString();
        });
    }

    @Override
    public String findNetworkByRecordId(Set<SzRecordKey>    recordKeys,
                                        int                 maxDegrees,
                                        int                 buildOutDegrees,
                                        int                 buildOutMaxEntities,
                                        Set<SzFlag>         flags)
        throws SzUnknownDataSourceException, SzNotFoundException, SzException
    {        
        // clear out the SDK-specific flags
        long downstreamFlags = SzFlag.toLong(flags) & SDK_FLAG_MASK;

        return this.env.execute(() -> {
            StringBuffer sb = new StringBuffer();

            String jsonRecordKeys = encodeRecordKeys(recordKeys);

            int returnCode = this.nativeApi.findNetworkByRecordID(
                jsonRecordKeys,
                maxDegrees,
                buildOutDegrees,
                buildOutMaxEntities,
                downstreamFlags,
                sb);

            // check the return code
            if (returnCode != 0) {
                this.env.handleReturnCode(
                    returnCode, this.nativeApi,
                    CLASS_PREFIX + ".findNetworkByRecordId(Set<SzRecordKey>,int,int,int,Set<SzFlag>)",
                    paramsOf(
                        "recordKeys", recordKeys,
                        "maxDegrees", maxDegrees,
                        "buildOutDegrees", buildOutDegrees,
                        "buildOutMaxEntities", buildOutMaxEntities,
                        "flags", SzFlag.toString(flags),
                        "[downstreamFlags]",
                        SZ_FIND_NETWORK.toString(downstreamFlags)));
                }

            return sb.toString();
        });
    }

    @Override
    public String findPathByEntityId(long           startEntityId, 
                                     long           endEntityId,
                                     int            maxDegrees,
                                     Set<Long>      exclusions,
                                     Set<String>    requiredDataSources,
                                     Set<SzFlag>    flags)
        throws SzNotFoundException,
               SzUnknownDataSourceException,
               SzException
    {
        // clear out the SDK-specific flags
        long downstreamFlags = SzFlag.toLong(flags) & SDK_FLAG_MASK;

        return this.env.execute(() -> {
            StringBuffer sb = new StringBuffer();

            int returnCode = 0;
            
            if ((exclusions == null || exclusions.isEmpty())
                && (requiredDataSources == null || requiredDataSources.isEmpty()))
            {
                // call the base function
                returnCode = this.nativeApi.findPathByEntityID(
                    startEntityId, endEntityId, maxDegrees, sb);

            } else if (requiredDataSources == null || requiredDataSources.isEmpty()) {
                // encode the entity ID's
                String exclusionJson = encodeEntityIds(exclusions);

                // call the variant with exclusions, but without required data sources
                returnCode = this.nativeApi.findPathExcludingByEntityID(
                    startEntityId, endEntityId, maxDegrees, exclusionJson, sb);

            } else {
                // encode the entity ID's
                String exclusionJson = encodeEntityIds(exclusions);

                // encode the data sources
                String dataSourceJson = encodeDataSources(requiredDataSources);

                // we have to call the full-blown variant of the function
                returnCode = this.nativeApi.findPathIncludingSourceByEntityID(
                    startEntityId, endEntityId, maxDegrees, exclusionJson, dataSourceJson, sb);
            }

            // check the return code
            if (returnCode != 0) {
                this.env.handleReturnCode(
                    returnCode, this.nativeApi,
                    CLASS_PREFIX + ".findPathByEntityId(long,long,int,Set<Long>,Set<String>,Set<SzFlag>)",
                    paramsOf(
                        "startEntityId", startEntityId,
                        "endEntityId", endEntityId,
                        "maxDegrees", maxDegrees,
                        "exclusions", exclusions, 
                        "requiredDataSources", requiredDataSources,
                        "flags", SzFlag.toString(flags),
                        "[downstreamFlags]",
                        SZ_FIND_PATH.toString(downstreamFlags)));
                }

            return sb.toString();
        });
    }

    @Override
    public String findPathByRecordId(String             startDataSourceCode,
                                     String             startRecordId, 
                                     String             endDataSourceCode, 
                                     String             endRecordId,
                                     int                maxDegrees,
                                     Set<SzRecordKey>   exclusions,
                                     Set<String>        requiredDataSources,
                                     Set<SzFlag>        flags)
            throws SzNotFoundException,
                   SzUnknownDataSourceException,
                   SzException 
    {
        // clear out the SDK-specific flags
        long downstreamFlags = SzFlag.toLong(flags) & SDK_FLAG_MASK;

        return this.env.execute(() -> {
            StringBuffer sb = new StringBuffer();

            int returnCode = 0;
            
            if ((exclusions == null || exclusions.isEmpty())
                && (requiredDataSources == null || requiredDataSources.isEmpty()))
            {
                // call the base function
                returnCode = this.nativeApi.findPathByRecordID(
                    startDataSourceCode, 
                    startRecordId, 
                    endDataSourceCode, 
                    endRecordId,
                    maxDegrees,
                    sb);

            } else if (requiredDataSources == null || requiredDataSources.isEmpty()) {
                // encode the entity ID's
                String exclusionJson = encodeRecordKeys(exclusions);

                // call the variant with exclusions, but without required data sources
                returnCode = this.nativeApi.findPathExcludingByRecordID(
                    startDataSourceCode, 
                    startRecordId, 
                    endDataSourceCode, 
                    endRecordId,
                    maxDegrees,
                    exclusionJson,
                    sb);

            } else {
                // encode the entity ID's
                String exclusionJson = encodeRecordKeys(exclusions);

                // encode the data sources
                String dataSourceJson = encodeDataSources(requiredDataSources);

                // we have to call the full-blown variant of the function
                returnCode = this.nativeApi.findPathIncludingSourceByRecordID(
                    startDataSourceCode, 
                    startRecordId, 
                    endDataSourceCode, 
                    endRecordId,
                    maxDegrees,
                    exclusionJson,
                    dataSourceJson,
                    sb);
            }

            // check the return code
            if (returnCode != 0) {
                this.env.handleReturnCode(
                    returnCode, this.nativeApi,
                    CLASS_PREFIX + ".findPathByRecordId(String,String,String,String,int,Set<SzRecordKey>,Set<String>,Set<SzFlag>)",
                    paramsOf(
                        "startDataSourceCode", startDataSourceCode,
                        "startRecordId", startRecordId,
                        "endDataSourceCode", endDataSourceCode,
                        "endRecordId", endRecordId,
                        "maxDegrees", maxDegrees,
                        "exclusions", exclusions, 
                        "requiredDataSources", requiredDataSources,
                        "flags", SzFlag.toString(flags),
                        "[downstreamFlags]",
                        SZ_FIND_PATH.toString(downstreamFlags)));
                }

            return sb.toString();
        });
    }

    @Override
    public String findPathByRecordId(SzRecordKey        startRecordKey,
                                     SzRecordKey        endRecordKey,
                                     int                maxDegrees,
                                     Set<SzRecordKey>   exclusions,
                                     Set<String>        requiredDataSources,
                                     Set<SzFlag>        flags)
            throws SzNotFoundException,
                   SzUnknownDataSourceException,
                   SzException 
    {
        return this.findPathByRecordId(startRecordKey.dataSourceCode(),
                                       startRecordKey.recordId(),
                                       endRecordKey.dataSourceCode(),
                                       endRecordKey.recordId(),
                                       maxDegrees,
                                       exclusions,
                                       requiredDataSources,
                                       flags);
    }

    @Override
    public String getEntity(long entityId, Set<SzFlag> flags)
            throws SzNotFoundException, SzException 
    {
        return this.env.execute(() -> {
            // clear out the SDK-specific flags
            long downstreamFlags = SzFlag.toLong(flags) & SDK_FLAG_MASK;

            // check if we have flags to pass downstream
            StringBuffer sb = new StringBuffer();
            int returnCode = this.nativeApi.getEntityByEntityID(
                entityId, downstreamFlags, sb);

            // check the return code
            if (returnCode != 0) {
                this.env.handleReturnCode(
                    returnCode, this.nativeApi,
                    CLASS_PREFIX + ".getEntity(long,Set<SzFlag>)",
                    paramsOf("entityId", entityId,
                             "flags", SzFlag.toString(flags),
                             "[downstreamFlags]", 
                             SZ_ENTITY.toString(downstreamFlags)));
            }

            // return the result
            return sb.toString();
        });
    }

    @Override
    public String getEntity(String      dataSourceCode,
                            String      recordId,
                            Set<SzFlag> flags) 
        throws SzUnknownDataSourceException,
               SzNotFoundException,
               SzException 
    {
        return this.env.execute(() -> {
            // clear out the SDK-specific flags
            long downstreamFlags = SzFlag.toLong(flags) & SDK_FLAG_MASK;

            // check if we have flags to pass downstream
            StringBuffer sb = new StringBuffer();
            int returnCode = this.nativeApi.getEntityByRecordID(
                dataSourceCode, recordId, downstreamFlags, sb);

            // check the return code
            if (returnCode != 0) {
                this.env.handleReturnCode(
                    returnCode, this.nativeApi,
                    CLASS_PREFIX + ".getEntity(String,String,Set<SzFlag>)",
                    paramsOf("dataSourceCode", dataSourceCode,
                             "recordId", recordId,
                             "flags", SzFlag.toString(flags),
                             "[downstreamFlags]", 
                             SZ_ENTITY.toString(downstreamFlags)));
            }

            // return the result
            return sb.toString();
        });
    }

    @Override
    public String getEntity(SzRecordKey recordKey, Set<SzFlag> flags)
            throws SzUnknownDataSourceException,
                   SzNotFoundException,
                   SzException 
    {
        return this.getEntity(
            recordKey.dataSourceCode(), recordKey.recordId(), flags);
    }

    @Override
    public String getRecord(String      dataSourceCode,
                            String      recordId,
                            Set<SzFlag> flags)
        throws SzUnknownDataSourceException,
               SzNotFoundException,
               SzException
    {
        return this.env.execute(() -> {
            // clear out the SDK-specific flags
            long downstreamFlags = SzFlag.toLong(flags) & SDK_FLAG_MASK;

            // check if we have flags to pass downstream
            StringBuffer sb = new StringBuffer();
            int returnCode = this.nativeApi.getRecord(
                dataSourceCode, recordId, downstreamFlags, sb);

            // check the return code
            if (returnCode != 0) {
                this.env.handleReturnCode(
                    returnCode, this.nativeApi,
                    CLASS_PREFIX + ".getRecord(String,String,Set<SzFlag>)",
                    paramsOf("dataSourceCode", dataSourceCode,
                             "recordId", recordId,
                             "flags", SzFlag.toString(flags),
                             "[downstreamFlags]", 
                             SZ_RECORD.toString(downstreamFlags)));
            }

            // return the result
            return sb.toString();
        });
    }

    @Override
    public String getRecord(SzRecordKey recordKey, Set<SzFlag> flags)
        throws SzUnknownDataSourceException,
               SzNotFoundException,
               SzException 
    {
        return this.getRecord(
            recordKey.dataSourceCode(), recordKey.recordId(), flags);
    }

    @Override
    public String getRedoRecord() throws SzException {
        return this.env.execute(() -> {
            // check if we have flags to pass downstream
            StringBuffer sb = new StringBuffer();
            int returnCode = this.nativeApi.getRedoRecord(sb);

            // check the return code
            if (returnCode != 0) {
                this.env.handleReturnCode(
                    returnCode, this.nativeApi,
                    CLASS_PREFIX + ".getRedoRecord()");
            }

            // return the result
            return sb.toString();
        });
    }

    @Override
    public String getStats() throws SzException {
        return this.env.execute(() -> {
            return this.nativeApi.stats();
        });
    }

    @Override
    public String getVirtualEntity(Set<SzRecordKey> recordKeys,
                                   Set<SzFlag>      flags)
        throws SzNotFoundException, SzException 
    {
        return this.env.execute(() -> {
            // clear out the SDK-specific flags
            long downstreamFlags = SzFlag.toLong(flags) & SDK_FLAG_MASK;

            // get the record ID JSON
            String jsonRecordString = encodeRecordKeys(recordKeys);

            // check if we have flags to pass downstream
            StringBuffer sb = new StringBuffer();
            int returnCode = this.nativeApi.getVirtualEntityByRecordID(
                jsonRecordString, downstreamFlags, sb);

            // check the return code
            if (returnCode != 0) {
                this.env.handleReturnCode(
                    returnCode, this.nativeApi,
                    CLASS_PREFIX + ".getVirtualEntity(Set<SzRecordKey>,Set<SzFlag>)",
                    paramsOf("recordKeys", recordKeys,
                             "flags", SzFlag.toString(flags),
                             "[downstreamFlags]", 
                             SZ_VIRTUAL_ENTITY.toString(downstreamFlags)));
            }

            // return the result
            return sb.toString();
        });
    }

    @Override
    public String howEntity(long entityId, Set<SzFlag> flags)
            throws SzNotFoundException, SzException 
    {
        return this.env.execute(() -> {
            // clear out the SDK-specific flags
            long downstreamFlags = SzFlag.toLong(flags) & SDK_FLAG_MASK;

            // check if we have flags to pass downstream
            StringBuffer sb = new StringBuffer();
            int returnCode = this.nativeApi.howEntityByEntityID(
                entityId, downstreamFlags, sb);

            // check the return code
            if (returnCode != 0) {
                this.env.handleReturnCode(
                    returnCode, this.nativeApi,
                    CLASS_PREFIX + ".howEntity(long,Set<SzFlag>)",
                    paramsOf("entityId", entityId,
                             "flags", SzFlag.toString(flags),
                             "[downstreamFlags]", 
                             SZ_HOW.toString(downstreamFlags)));
            }

            // return the result
            return sb.toString();
        });
    }

    @Override
    public void primeEngine() throws SzException {
        this.env.execute(() -> {
            // check if we have flags to pass downstream
            int returnCode = this.nativeApi.primeEngine();

            // check the return code
            if (returnCode != 0) {
                this.env.handleReturnCode(
                    returnCode, this.nativeApi,
                    CLASS_PREFIX + ".primeEngine()");
            }

            // return the result
            return null;
        });
    }

    @Override
    public String processRedoRecord(String redoRecord, Set<SzFlag> flags)
            throws SzException 
    {
        return this.env.execute(() -> {
            int returnCode = 0;
            String result = NO_INFO;
            // check if we have flags to pass downstream
            if (!flags.contains(SZ_WITH_INFO)) {
                returnCode = this.nativeApi.processRedoRecord(redoRecord);

            } else {
                StringBuffer sb = new StringBuffer();
                returnCode = this.nativeApi.processRedoRecordWithInfo(redoRecord, sb);
                result = sb.toString();
            }

            // check the return code
            if (returnCode != 0) {
                this.env.handleReturnCode(
                    returnCode, this.nativeApi,
                    CLASS_PREFIX + ".processRedoRecord(redoRecord,flags)",
                    paramsOf("redoRecord", redact(redoRecord),
                             "flags", SzFlag.toString(flags)));
            }

            // return the result
            return result;
        });

    }

    @Override
    public String reevaluateEntity(long entityId, Set<SzFlag> flags)
            throws SzException 
    {
        return this.env.execute(() -> {
            // clear out the SDK-specific flags
            long downstreamFlags = SzFlag.toLong(flags) & SDK_FLAG_MASK;

            int returnCode = 0;
            String result = NO_INFO;

            // check if we have flags to pass downstream or need info
            if (!flags.contains(SZ_WITH_INFO)) {
                // no info needed, no flags to pass, go simple
                returnCode = this.nativeApi.reevaluateEntity(
                    entityId, downstreamFlags);
                
            } else {
                // we either need info or have flags or both
                StringBuffer sb = new StringBuffer();
                returnCode = this.nativeApi.reevaluateEntityWithInfo(
                    entityId, downstreamFlags, sb);
                    
                // set the info result if requested
                result = sb.toString();
            }

            // check the return code
            if (returnCode != 0) {
                this.env.handleReturnCode(
                    returnCode, this.nativeApi,
                    CLASS_PREFIX + ".reevaluateEntity(long,Set<SzFlag>)",
                    paramsOf("entityId", entityId,
                             "flags", SzFlag.toString(flags),
                             "[downstreamFlags]", 
                             SZ_MODIFY.toString(downstreamFlags)));
            }

            // return the result
            return result;
        });
    }

    @Override
    public String reevaluateRecord(String       dataSourceCode, 
                                   String       recordId,
                                   Set<SzFlag>  flags)
            throws SzUnknownDataSourceException, SzException 
    {
        return this.env.execute(() -> {
            // clear out the SDK-specific flags
            long downstreamFlags = SzFlag.toLong(flags) & SDK_FLAG_MASK;

            int returnCode = 0;
            String result = NO_INFO;

            // check if we have flags to pass downstream or need info
            if (!flags.contains(SZ_WITH_INFO)) {
                // no info needed, no flags to pass, go simple
                returnCode = this.nativeApi.reevaluateRecord(
                    dataSourceCode, recordId, downstreamFlags);
                
            } else {
                // we either need info or have flags or both
                StringBuffer sb = new StringBuffer();
                returnCode = this.nativeApi.reevaluateRecordWithInfo(
                    dataSourceCode, recordId, downstreamFlags, sb);
                    
                // set the info result
                result = sb.toString();
            }

            // check the return code
            if (returnCode != 0) {
                this.env.handleReturnCode(
                    returnCode, this.nativeApi,
                    CLASS_PREFIX + ".reevaluateEntity(long,Set<SzFlag>)",
                    paramsOf("dataSourceCode", dataSourceCode,
                             "recordId", recordId,
                             "flags", SzFlag.toString(flags),
                             "[downstreamFlags]", 
                             SZ_MODIFY.toString(downstreamFlags)));
            }

            // return the result
            return result;
        });
    }

    @Override
    public String reevaluateRecord(SzRecordKey recordKey, Set<SzFlag> flags)
            throws SzUnknownDataSourceException, SzException 
    {
        return this.reevaluateRecord(
            recordKey.dataSourceCode(), recordKey.recordId(), flags);
    }

    @Override
    public String searchByAttributes(String         attributes,
                                     String         searchProfile,
                                     Set<SzFlag>    flags) 
        throws SzException
    {
        return this.env.execute(() -> {
            // clear out the SDK-specific flags
            long downstreamFlags = SzFlag.toLong(flags) & SDK_FLAG_MASK;

            // declare the result variables
            StringBuffer    sb          = new StringBuffer();
            int             returnCode  = 0;
            
            // check if have a search profile
            if (searchProfile == null) {
                // search with the default search profile
                returnCode = this.nativeApi.searchByAttributes(
                    attributes, downstreamFlags, sb);

            } else {
                returnCode = this.nativeApi.searchByAttributes(
                    attributes, searchProfile, downstreamFlags, sb);
            }

            // check the return code
            if (returnCode != 0) {
                this.env.handleReturnCode(
                    returnCode, this.nativeApi,
                    CLASS_PREFIX + ".searchByAttributes(String,String,Set<SzFlag>)",
                    paramsOf("attributes", redact(attributes),
                              "searchProfile", searchProfile,
                             "flags", SzFlag.toString(flags),
                             "[downstreamFlags]", 
                             SZ_SEARCH.toString(downstreamFlags)));
            }

            // return the result
            return sb.toString();
        });
    }

    @Override
    public String searchByAttributes(String attributes, Set<SzFlag> flags) 
        throws SzException
    {
        return this.searchByAttributes(attributes, null, flags);
    }

    @Override
    public String whyEntities(long entityId1, long entityId2, Set<SzFlag> flags)
            throws SzNotFoundException, SzException 
    {
        return this.env.execute(() -> {
            // clear out the SDK-specific flags
            long downstreamFlags = SzFlag.toLong(flags) & SDK_FLAG_MASK;

            // check if we have flags to pass downstream
            StringBuffer sb = new StringBuffer();
            int returnCode = this.nativeApi.whyEntities(
                entityId1, entityId2, downstreamFlags, sb);

            // check the return code
            if (returnCode != 0) {
                this.env.handleReturnCode(
                    returnCode, this.nativeApi,
                    CLASS_PREFIX + ".whyEntities(long,long,Set<SzFlag>)",
                    paramsOf("entityId1", entityId1,
                             "entityId2", entityId2,
                             "flags", SzFlag.toString(flags),
                             "[downstreamFlags]", 
                             SZ_WHY.toString(downstreamFlags)));
            }

            // return the result
            return sb.toString();
        });
    }

    @Override
    public String whyRecordInEntity(String      dataSourceCode,
                                    String      recordId,
                                    Set<SzFlag> flags)
        throws SzUnknownDataSourceException,
               SzNotFoundException,
               SzException 
    {
        return this.env.execute(() -> {
            // clear out the SDK-specific flags
            long downstreamFlags = SzFlag.toLong(flags) & SDK_FLAG_MASK;

            // check if we have flags to pass downstream
            StringBuffer sb = new StringBuffer();
            int returnCode = this.nativeApi.whyRecordInEntity(
                dataSourceCode, recordId, downstreamFlags, sb);

            // check the return code
            if (returnCode != 0) {
                this.env.handleReturnCode(
                    returnCode, this.nativeApi,
                    CLASS_PREFIX + ".whyRecordInEntity(String,String,Set<SzFlag>)",
                    paramsOf("dataSourceCode", dataSourceCode,
                             "recordId", recordId,
                             "flags", SzFlag.toString(flags),
                             "[downstreamFlags]", 
                             SZ_WHY.toString(downstreamFlags)));
            }

            // return the result
            return sb.toString();
        });
    }

    @Override
    public String whyRecordInEntity(SzRecordKey recordKey, Set<SzFlag> flags)
        throws SzUnknownDataSourceException,
               SzNotFoundException,
               SzException 
    {
        return this.whyRecordInEntity(
            recordKey.dataSourceCode(), recordKey.recordId(), flags);
    }

    @Override
    public String whyRecords(String         dataSourceCode1, 
                             String         recordId1,
                             String         dataSourceCode2, 
                             String         recordId2, 
                             Set<SzFlag>    flags)
        throws SzUnknownDataSourceException,
               SzNotFoundException,
               SzException 
    {
        return this.env.execute(() -> {
            // clear out the SDK-specific flags
            long downstreamFlags = SzFlag.toLong(flags) & SDK_FLAG_MASK;

            // check if we have flags to pass downstream
            StringBuffer sb = new StringBuffer();
            int returnCode = this.nativeApi.whyRecords(dataSourceCode1,
                                                       recordId1,
                                                       dataSourceCode2,
                                                       recordId2,
                                                       downstreamFlags,
                                                       sb);

            // check the return code
            if (returnCode != 0) {
                this.env.handleReturnCode(
                    returnCode, this.nativeApi,
                    CLASS_PREFIX + ".whyRecords(String,String,String,String,Set<SzFlag>)",
                    paramsOf("dataSourceCode1", dataSourceCode1,
                             "recordId1", recordId1,
                             "dataSourceCode2", dataSourceCode2,
                             "recordId2", recordId1,
                             "flags", SzFlag.toString(flags),
                             "[downstreamFlags]", 
                             SZ_WHY.toString(downstreamFlags)));
            }

            // return the result
            return sb.toString();
        });
    }

    @Override
    public String whyRecords(SzRecordKey recordKey1, 
                             SzRecordKey recordKey2,
                             Set<SzFlag> flags)
        throws SzUnknownDataSourceException,
               SzNotFoundException,
               SzException 
    {
        return this.whyRecords(recordKey1.dataSourceCode(),
                               recordKey1.recordId(),
                               recordKey2.dataSourceCode(),
                               recordKey2.recordId(),
                               flags);
    }
}
