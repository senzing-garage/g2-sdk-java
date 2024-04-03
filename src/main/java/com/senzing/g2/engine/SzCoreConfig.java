package com.senzing.g2.engine;

import javax.json.Json;
import javax.json.JsonObjectBuilder;
import javax.json.JsonObject;

/**
 * The package-protected implementation of {@link SzConfig} that works
 * with the {@link SzCoreProvider} class.
 */
class SzCoreConfig implements SzConfig {
    /**
     * The {@link SzCoreProvider} that constructed this instance.
     */
    private SzCoreProvider provider = null;

    /**
     * The underlying {@link G2ConfigJNI} instance.
     */
    private G2ConfigJNI nativeApi = null;

    /**
     * Default constructor.
     * 
     * @throws IllegalStateException If the underlying {@link SzCoreProvider} instance 
     *                               has already been destroyed.
     * @throws SzException If a Senzing failure occurs during initialization.
     */
    SzCoreConfig(SzCoreProvider provider) throws IllegalStateException, SzException {
        this.provider = provider;
        this.provider.execute(() -> {
            this.nativeApi = new G2ConfigJNI();

            int returnCode = this.nativeApi.init(this.provider.getInstanceName(),
                                                 this.provider.getSettings(),
                                                 this.provider.isVerboseLogging());

            this.provider.handleReturnCode(returnCode, this.nativeApi);

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
     * {@link SzProvider}.
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
    public SzProvider getProvider() {
        return this.provider;
    }

    @Override
    public long createConfig() throws SzException {
        return this.provider.execute(() -> {
            // create the result object
            Result<Long> result = new Result<>();
            
            // call the underlying C function
            int returnCode = this.nativeApi.create(result);

            // handle any error code if there is one
            this.provider.handleReturnCode(returnCode, this.nativeApi);

            // return the config handle
            return result.getValue();
        });
    }

    @Override
    public long importConfig(String configDefinition) throws SzException {
        return this.provider.execute(() -> {
            // create the result object
            Result<Long> result = new Result<>();
            
            // call the underlying C function
            int returnCode = this.nativeApi.load(configDefinition, result);

            // handle any error code if there is one
            this.provider.handleReturnCode(returnCode, this.nativeApi);

            // return the config handle
            return result.getValue();
        });
    }

    @Override
    public String exportConfig(long configHandle) throws SzException {
        return this.provider.execute(() -> {
            // create the response buffer
            StringBuffer sb = new StringBuffer();

            // call the underlying C function
            int returnCode = this.nativeApi.save(configHandle, sb);

            // handle any error code if there is one
            this.provider.handleReturnCode(returnCode, this.nativeApi);

            // return the contents of the buffer
            return sb.toString();
        });
    }

    @Override
    public void closeConfig(long configHandle) throws SzException {
        this.provider.execute(() -> {
            // call the underlying C function
            int returnCode = this.nativeApi.close(configHandle);

            // handle any error code if there is one
            this.provider.handleReturnCode(returnCode, this.nativeApi);
            
            // return null
            return null;
        });
    }

    @Override
    public String getDataSources(long configHandle) throws SzException {
        return this.provider.execute(() -> {
            // create the response buffer
            StringBuffer sb = new StringBuffer();

            // call the underlying C function
            int returnCode = this.nativeApi.listDataSources(configHandle, sb);

            // handle any error code if there is one
            this.provider.handleReturnCode(returnCode, this.nativeApi);

            // return the contents of the buffer
            return sb.toString();
        });
    }

    @Override
    public void addDataSource(long configHandle, String dataSourceCode) 
        throws SzException 
    {
        this.provider.execute(() -> {
            // format the JSON for the JNI call
            JsonObjectBuilder job = Json.createObjectBuilder();
            job.add("DSRC_CODE", dataSourceCode);
            JsonObject obj = job.build();
            String inputJson = obj.toString();

            // create a StringBuffer for calling the JNI call
            StringBuffer sb = new StringBuffer();

            // call the underlying C function
            int returnCode = this.nativeApi.addDataSource(
                configHandle, inputJson, sb);

            // handle any error code if there is one
            this.provider.handleReturnCode(returnCode, this.nativeApi);

            // return null
            return null;
        });              
    }

    @Override
    public void deleteDataSource(long configHandle, String dataSourceCode) 
        throws SzException 
    {
        this.provider.execute(() -> {
            // format the JSON for the JNI call
            JsonObjectBuilder job = Json.createObjectBuilder();
            job.add("DSRC_CODE", dataSourceCode);
            JsonObject obj = job.build();
            String inputJson = obj.toString();

            // call the underlying C function
            int returnCode = this.nativeApi.deleteDataSource(
                configHandle, inputJson);
            
            // handle any error code if there is one
            this.provider.handleReturnCode(returnCode, this.nativeApi);

            // return null
            return null;
        });
    }
}
