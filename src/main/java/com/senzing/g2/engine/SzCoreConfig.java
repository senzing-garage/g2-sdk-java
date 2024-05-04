package com.senzing.g2.engine;

import static com.senzing.g2.engine.Utilities.*;
import static com.senzing.g2.engine.SzException.*;

/**
 * The package-protected implementation of {@link SzConfig} that works
 * with the {@link SzCoreEnvironment} class.
 */
class SzCoreConfig implements SzConfig {
    /**
     * The {@link SzCoreEnvironment} that constructed this instance.
     */
    private SzCoreEnvironment env = null;

    /**
     * The underlying {@link G2ConfigJNI} instance.
     */
    G2ConfigJNI nativeApi = null;

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
    SzCoreConfig(SzCoreEnvironment environment) throws IllegalStateException, SzException {
        this.env = environment;
        this.env.execute(() -> {
            this.nativeApi = new G2ConfigJNI();

            int returnCode = this.nativeApi.init(this.env.getInstanceName(),
                                                 this.env.getSettings(),
                                                 this.env.isVerboseLogging());

            if (returnCode != 0) {
                this.env.handleReturnCode(
                    returnCode, this.nativeApi, 
                    "SzCoreConfig()",
                    paramsOf("instanceName", this.env.getInstanceName(),
                             "settings", redact(this.env.getSettings()),
                             "verboseLogging", this.env.isVerboseLogging()));
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
    public long createConfig() throws SzException {
        return this.env.execute(() -> {
            // create the result object
            Result<Long> result = new Result<>();
            
            // call the underlying C function
            int returnCode = this.nativeApi.create(result);

            // handle any error code if there is one
            if (returnCode != 0) {
                this.env.handleReturnCode(
                    returnCode, 
                    this.nativeApi,
                    "SzCoreConfig.createConfig()");
            }

            // return the config handle
            return result.getValue();
        });
    }

    @Override
    public long importConfig(String configDefinition) throws SzException {
        return this.env.execute(() -> {
            // create the result object
            Result<Long> result = new Result<>();
            
            // call the underlying C function
            int returnCode = this.nativeApi.load(configDefinition, result);

            // handle any error code if there is one
            if (returnCode != 0) {
                this.env.handleReturnCode(
                    returnCode,
                    this.nativeApi,
                    "SzCoreConfig.importConfig(String)",
                    paramsOf("configDefintion", configDefinition));
            }

            // return the config handle
            return result.getValue();
        });
    }

    @Override
    public String exportConfig(long configHandle) throws SzException {
        return this.env.execute(() -> {
            // create the response buffer
            StringBuffer sb = new StringBuffer();

            // call the underlying C function
            int returnCode = this.nativeApi.save(configHandle, sb);

            // handle any error code if there is one
            if (returnCode != 0) {
                this.env.handleReturnCode(
                    returnCode, 
                    this.nativeApi,
                    "SzCoreConfig.exportConfig(long)",
                    paramsOf("configHandle", hexFormat(configHandle)));
            }

            // return the contents of the buffer
            return sb.toString();
        });
    }

    @Override
    public void closeConfig(long configHandle) throws SzException {
        this.env.execute(() -> {
            // call the underlying C function
            int returnCode = this.nativeApi.close(configHandle);

            // handle any error code if there is one
            if (returnCode != 0) {
                this.env.handleReturnCode(
                    returnCode, 
                    this.nativeApi,
                    "SzCoreConfig.closeConfig(long)",
                    paramsOf("configHandle", hexFormat(configHandle)));
            }
            
            // return null
            return null;
        });
    }

    @Override
    public String getDataSources(long configHandle) throws SzException {
        return this.env.execute(() -> {
            // create the response buffer
            StringBuffer sb = new StringBuffer();

            // call the underlying C function
            int returnCode = this.nativeApi.listDataSources(configHandle, sb);

            // handle any error code if there is one
            if (returnCode != 0) {
                this.env.handleReturnCode(
                    returnCode, 
                    this.nativeApi,
                    "SzCoreConfig.getDataSources(long)",
                    paramsOf("configHandle", hexFormat(configHandle)));

            }

            // return the contents of the buffer
            return sb.toString();
        });
    }

    @Override
    public void addDataSource(long configHandle, String dataSourceCode) 
        throws SzException 
    {
        this.env.execute(() -> {
            // format the JSON for the JNI call
            String inputJson = "{\"DSRC_CODE\":" + jsonEscape(dataSourceCode) + "}";

            // create a StringBuffer for calling the JNI call
            StringBuffer sb = new StringBuffer();

            // call the underlying C function
            int returnCode = this.nativeApi.addDataSource(
                configHandle, inputJson, sb);

            // handle any error code if there is one
            if (returnCode != 0) {
                this.env.handleReturnCode(
                    returnCode, 
                    this.nativeApi,
                    "SzCoreConfig.addDataSource(long,String)",
                    paramsOf("configHandle", hexFormat(configHandle),
                             "dataSourceCOde", dataSourceCode));
            } 

            // return null
            return null;
        });              
    }

    @Override
    public void deleteDataSource(long configHandle, String dataSourceCode) 
        throws SzException 
    {
        this.env.execute(() -> {
            // format the JSON for the JNI call
            String inputJson = "{\"DSRC_CODE\":" + jsonEscape(dataSourceCode) + "}";

            // call the underlying C function
            int returnCode = this.nativeApi.deleteDataSource(
                configHandle, inputJson);
            
            // handle any error code if there is one
            if (returnCode != 0) {
                this.env.handleReturnCode(
                    returnCode, 
                    this.nativeApi,
                    "SzCoreConfig.deleteDataSource(long,String)",
                    paramsOf("configHandle", hexFormat(configHandle),
                             "dataSourceCode", dataSourceCode));
            }

            // return null
            return null;
        });
    }
}
