package com.senzing.g2.engine;

/**
 * The package-protected implementation of {@link SzDiagnostic} that works
 * with the {@link SenzingSDK} class.
 */
public class SzDiagnosticSdk implements SzDiagnostic {
    /**
     * The {@link SenzingSdk} that constructed this instance.
     */
    private SenzingSdk sdk = null;

    /**
     * The underlying {@link G2DiagnosticJNI} instance.
     */
    private G2DiagnosticJNI nativeApi = null;

    /**
     * Default constructor.
     * 
     * @throws IllegalStateException If the underlying {@link SenzingSdk} instance 
     *                               has already been destroyed.
     * @throws SzException If a Senzing failure occurs during initialization.
     */
    SzDiagnosticSdk(SenzingSdk sdk) 
        throws IllegalStateException, SzException 
    {
        this.sdk = sdk;
        this.sdk.execute(() -> {
            this.nativeApi = new G2DiagnosticJNI();

            // check if we are initializing with a config ID
            if (this.sdk.getConfigId() == null) {
                // if not then call the basic init
                int returnCode = this.nativeApi.init(
                    this.sdk.getInstanceName(),
                    this.sdk.getSettings(),
                    this.sdk.isVerboseLogging());
 
                // handle any failure
                this.sdk.handleReturnCode(returnCode, this.nativeApi);

            } else {
                // if so then call init with config ID
                int returnCode = this.nativeApi.initWithConfigID(
                    this.sdk.getInstanceName(),
                    this.sdk.getSettings(),
                    this.sdk.getConfigId(),
                    this.sdk.isVerboseLogging());

                // handle any failure
                this.sdk.handleReturnCode(returnCode, this.nativeApi);
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

}
