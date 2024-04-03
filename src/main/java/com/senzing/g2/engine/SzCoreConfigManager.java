package com.senzing.g2.engine;

/**
 * The package-protected implementation of {@link SzConfigManager} that works
 * with the {@link SzCoreProvider} class.
 */
public class SzCoreConfigManager implements SzConfigManager {
    /**
     * The {@link SzCoreProvider} that constructed this instance.
     */
    private SzCoreProvider provider = null;

    /**
     * The underlying {@link G2ConfigMgrJNI} instance.
     */
    private G2ConfigMgrJNI nativeApi = null;

    /**
     * Default constructor.
     * 
     * @throws IllegalStateException If the underlying {@link SzCoreProvider} instance 
     *                               has already been destroyed.
     * @throws SzException If a Senzing failure occurs during initialization.
     */
    SzCoreConfigManager(SzCoreProvider provider) 
        throws IllegalStateException, SzException 
    {
        this.provider = provider;
        this.provider.execute(() -> {
            this.nativeApi = new G2ConfigMgrJNI();

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

}
