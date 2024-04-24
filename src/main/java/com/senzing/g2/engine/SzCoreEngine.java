package com.senzing.g2.engine;

import java.util.Set;

/**
 * The package-protected implementation of {@link SzEngine} that works
 * with the {@link SzCoreEnvironment} class.
 */
public class SzCoreEngine implements SzEngine {
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
     * @param environment The {@link SzCoreEnvironment} with which to 
     *                    construct.
     * 
     * @throws IllegalStateException If the underlying {@link SzCoreEnvironment} instance 
     *                               has already been destroyed.
     * @throws SzException If a Senzing failure occurs during initialization.
     */
    SzCoreEngine(SzCoreEnvironment environment) 
        throws IllegalStateException, SzException 
    {
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
                this.env.handleReturnCode(returnCode, this.nativeApi);

            } else {
                // if so then call init with config ID
                int returnCode = this.nativeApi.initWithConfigID(
                    this.env.getInstanceName(),
                    this.env.getSettings(),
                    this.env.getConfigId(),
                    this.env.isVerboseLogging());

                // handle any failure
                this.env.handleReturnCode(returnCode, this.nativeApi);
            }

            return null;
        });
    }

    /**
     * The package-protected function to destroy the Senzing Config SDK.
     */
    void destroy() {
        synchronized (this) {
            if (this.nativeApi == null) return;
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
    public String addRecord(String dataSourceCode, String recordId,
            String recordDefinition, Set<SzFlag> flags)
            throws SzUnknownDataSourceException, SzBadInputException,
            SzException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String addRecord(SzRecordKey recordKey, String recordDefinition,
            Set<SzFlag> flags) throws SzUnknownDataSourceException,
            SzBadInputException, SzException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void closeExport(long exportHandle) throws SzException {
        // TODO Auto-generated method stub
        
    }

    @Override
    public long countRedoRecords() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public String deleteRecord(String dataSourceCode, String recordId,
            Set<SzFlag> flags)
            throws SzUnknownDataSourceException, SzException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String deleteRecord(SzRecordKey recordKey, Set<SzFlag> flags)
            throws SzUnknownDataSourceException, SzException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long exportCsvEntityReport(String csvColumnList, Set<SzFlag> flags)
            throws SzException {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public long exportJsonEntityReport(Set<SzFlag> flags) throws SzException {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public String fetchNext(long exportHandle) throws SzException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String findNetworkByEntityId(Set<Long> entities, int maxDegrees,
            int buildOutDegrees, int buildOutMaxEntities, Set<SzFlag> flags)
            throws SzNotFoundException, SzException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String findNetworkByRecordId(Set<SzRecordKey> records,
            int maxDegrees, int buildOutDegrees, int buildOutMaxEntities,
            Set<SzFlag> flags) throws SzUnknownDataSourceException,
            SzNotFoundException, SzException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String findPathByEntityId(long startEntityId, long endEntityId,
            int maxDegrees, Set<Long> exclusions,
            Set<String> requiredDataSources, Set<SzFlag> flags)
            throws SzNotFoundException, SzUnknownDataSourceException,
            SzException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String findPathByRecordId(String startDataSourceCode,
            String startRecordId, String endDataSourceCode, String endRecordId,
            int maxDegrees, Set<SzRecordKey> exclusions,
            Set<String> requiredDataSources, Set<SzFlag> flags)
            throws SzNotFoundException, SzUnknownDataSourceException,
            SzException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String findPathByRecordId(SzRecordKey startRecordKey,
            SzRecordKey endRecordKey, int maxDegrees,
            Set<SzRecordKey> exclusions, Set<String> requiredDataSources,
            Set<SzFlag> flags)
            throws SzNotFoundException, SzUnknownDataSourceException,
            SzException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getEntity(long entityId, Set<SzFlag> flags)
            throws SzNotFoundException, SzException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getEntity(String dataSourceCode, String recordId,
            Set<SzFlag> flags) throws SzUnknownDataSourceException,
            SzNotFoundException, SzException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getEntity(SzRecordKey recordKey, Set<SzFlag> flags)
            throws SzUnknownDataSourceException, SzNotFoundException,
            SzException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getRecord(String dataSourceCode, String recordId,
            Set<SzFlag> flags) throws SzUnknownDataSourceException,
            SzNotFoundException, SzException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getRecord(SzRecordKey recordKey, Set<SzFlag> flags)
            throws SzUnknownDataSourceException, SzNotFoundException,
            SzException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getRedoRecord() throws SzException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getRepositoryLastModifiedTime() throws SzException {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public String getStats() throws SzException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getVirtualEntity(Set<SzRecordKey> recordKeys,
            Set<SzFlag> flags) throws SzNotFoundException, SzException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String howEntity(long entityId, Set<SzFlag> flags)
            throws SzNotFoundException, SzException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void primeEngine() throws SzException {
        // TODO Auto-generated method stub
        
    }

    @Override
    public String processRedoRecord(String redoRecord, Set<SzFlag> flags)
            throws SzException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String reevaluateEntity(long entityId, Set<SzFlag> flags)
            throws SzException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String reevaluateRecord(String dataSourceCode, String recordId,
            Set<SzFlag> flags)
            throws SzUnknownDataSourceException, SzException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String reevaluateRecord(SzRecordKey recordKey, Set<SzFlag> flags)
            throws SzUnknownDataSourceException, SzException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String searchByAttributes(String attributes, String searchProfile,
            Set<SzFlag> flags) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String searchByAttributes(String attributes, Set<SzFlag> flags) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String whyEntities(long entityId1, long entityId2, Set<SzFlag> flags)
            throws SzNotFoundException, SzException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String whyRecordInEntity(String dataSourceCode, String recordId,
            Set<SzFlag> flags) throws SzUnknownDataSourceException,
            SzNotFoundException, SzException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String whyRecordInEntity(SzRecordKey recordKey, Set<SzFlag> flags)
            throws SzUnknownDataSourceException, SzNotFoundException,
            SzException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String whyRecords(String dataSourceCode1, String recordId1,
            String dataSourceCode2, String recordId2, Set<SzFlag> flags)
            throws SzUnknownDataSourceException, SzNotFoundException,
            SzException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String whyRecords(SzRecordKey recordKey1, SzRecordKey recordKey2,
            Set<SzFlag> flags) throws SzUnknownDataSourceException,
            SzNotFoundException, SzException {
        // TODO Auto-generated method stub
        return null;
    }
}
