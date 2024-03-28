package com.senzing.g2.engine;

/**
 * The package-protected implementation of {@link SzProduct} that works
 * with the {@link SenzingSdk} class.
 */
public class SzProductSdk implements SzProduct {
    /**
     * The {@link SenzingSdk} that constructed this instance.
     */
    private SenzingSdk sdk = null;

    /**
     * The underlying {@link G2ProductJNI} instance.
     */
    private G2ProductJNI nativeApi = null;

    /**
     * Default constructor.
     * 
     * @throws IllegalStateException If the underlying {@link SenzingSdk} instance 
     *                               has already been destroyed.
     * @throws SzException If a Senzing failure occurs during initialization.
     */
    SzProductSdk(SenzingSdk sdk) throws IllegalStateException, SzException {
        this.sdk = sdk;
        this.sdk.execute(() -> {
            this.nativeApi = new G2ProductJNI();

            int returnCode = this.nativeApi.init(this.sdk.getInstanceName(),
                                                 this.sdk.getSettings(),
                                                 this.sdk.isVerboseLogging());

            this.sdk.handleReturnCode(returnCode, this.nativeApi);

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


}
