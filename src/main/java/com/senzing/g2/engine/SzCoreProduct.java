package com.senzing.g2.engine;

/**
 * The package-protected implementation of {@link SzProduct} that works
 * with the {@link SzCoreProvider} class.
 */
public class SzCoreProduct implements SzProduct {
    /**
     * The {@link SzCoreProvider} that constructed this instance.
     */
    private SzCoreProvider provider = null;

    /**
     * The underlying {@link G2ProductJNI} instance.
     */
    private G2ProductJNI nativeApi = null;

    /**
     * Default constructor.
     * 
     * @throws IllegalStateException If the underlying {@link SzCoreProvider} instance 
     *                               has already been destroyed.
     * @throws SzException If a Senzing failure occurs during initialization.
     */
    SzCoreProduct(SzCoreProvider provider) throws IllegalStateException, SzException {
        this.provider = provider;
        this.provider.execute(() -> {
            this.nativeApi = new G2ProductJNI();

            int returnCode = this.nativeApi.init(this.provider.getInstanceName(),
                                                 this.provider.getSettings(),
                                                 this.provider.isVerboseLogging());

            this.provider.handleReturnCode(returnCode, this.nativeApi);

            return null;
        });
    }

    /**
     * The package-protected function to destroy the Senzing Product SDK.
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
