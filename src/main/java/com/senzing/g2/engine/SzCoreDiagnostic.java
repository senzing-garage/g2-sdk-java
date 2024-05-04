package com.senzing.g2.engine;

import static com.senzing.g2.engine.SzException.*;

/**
 * The package-protected implementation of {@link SzDiagnostic} that works
 * with the {@link SzCoreEnvironment} class.
 */
public class SzCoreDiagnostic implements SzDiagnostic {
    /**
     * Gets the class prefix to use for {@link SzException} construction.
     */
    private static final String CLASS_PREFIX = SzCoreDiagnostic.class.getSimpleName();

    /**
     * The {@link SzCoreEnvironment} that constructed this instance.
     */
    private SzCoreEnvironment env = null;

    /**
     * The underlying {@link G2DiagnosticJNI} instance.
     */
    G2DiagnosticJNI nativeApi = null;

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
    SzCoreDiagnostic(SzCoreEnvironment environment) 
        throws IllegalStateException, SzException 
    {
        this.env = environment;
        this.env.execute(() -> {
            this.nativeApi = new G2DiagnosticJNI();

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
    public String checkDatastorePerformance(int secondsToRun) throws SzException {
        return this.env.execute(() -> {
            // declare the buffer for the result
            StringBuffer sb = new StringBuffer();

            // call the underlying C function
            int returnCode = this.nativeApi.checkDBPerf(secondsToRun, sb);

            // handle any error code if there is one
            if (returnCode != 0) {
                this.env.handleReturnCode(
                    returnCode, this.nativeApi,
                    CLASS_PREFIX + ".checkDatastorePerformance(int)",
                    paramsOf("secondsToRun", secondsToRun));
            }

            // return null
            return sb.toString();
        });
    }

    @Override
    public String getFeature(long featureId) throws SzException {
        return this.env.execute(() -> {
            // declare the buffer for the result
            StringBuffer sb = new StringBuffer();

            // call the underlying C function
            int returnCode = this.nativeApi.getFeature(featureId, sb);

            // handle any error code if there is one
            if (returnCode != 0) {
                this.env.handleReturnCode(
                    returnCode, this.nativeApi,
                    CLASS_PREFIX + ".getFeature(long)",
                    paramsOf("featureId", featureId));
            }

            // return null
            return sb.toString();
        });
    }

    @Override
    public void purgeRepository() throws SzException {
        this.env.execute(() -> {
            // call the underlying C function
            int returnCode = this.nativeApi.purgeRepository();
            
            // handle any error code if there is one
            if (returnCode != 0) {
                this.env.handleReturnCode(
                    returnCode, this.nativeApi,
                    CLASS_PREFIX + ".purgeRepository()");
            }

            // return null
            return null;
        });
        
    }
}
