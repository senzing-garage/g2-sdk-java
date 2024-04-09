package com.senzing.g2.engine;

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
    private G2JNI nativeApi = null;

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
}
