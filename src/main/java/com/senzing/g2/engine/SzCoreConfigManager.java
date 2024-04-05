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

  	@Override
	public long addConfig(String configDefinition, String configComment)
        throws SzException 
    {
        return this.provider.execute(() -> {
            // create the result object
            Result<Long> result = new Result<>();
            
            // call the underlying C function
            int returnCode = this.nativeApi.addConfig(
                configDefinition, configComment, result);

            // handle any error code if there is one
            this.provider.handleReturnCode(returnCode, this.nativeApi);

            // return the config handle
            return result.getValue();
        });
    }

    @Override
	public String getConfig(long configId) throws SzException 
    {
        return this.provider.execute(() -> {
            // create the result object
            StringBuffer sb = new StringBuffer();

            // call the underlying C function
            int returnCode = this.nativeApi.getConfig(configId, sb);

            // handle any error code if there is one
            this.provider.handleReturnCode(returnCode, this.nativeApi);

            // return the config handle
            return sb.toString();
        });
    }

    @Override
	public String getConfigList() throws SzException {
        return this.provider.execute(() -> {
            // create the result object
            StringBuffer sb = new StringBuffer();

            // call the underlying C function
            int returnCode = this.nativeApi.getConfigList(sb);

            // handle any error code if there is one
            this.provider.handleReturnCode(returnCode, this.nativeApi);

            // return the config handle
            return sb.toString();
        });
    }

    @Override
	public long getDefaultConfigId() throws SzException {
        return this.provider.execute(() -> {
            // create the result object
            Result<Long> result = new Result<>();
            
            // call the underlying C function
            int returnCode = this.nativeApi.getDefaultConfigID(result);

            // handle any error code if there is one
            this.provider.handleReturnCode(returnCode, this.nativeApi);

            // return the config handle
            return result.getValue();
        });
    }

    @Override
	public void replaceDefaultConfigId(long currentDefaultConfigId, long newDefaultConfigId)
        throws SzException 
    {
        this.provider.execute(() -> {
            // call the underlying C function
            int returnCode = this.nativeApi.replaceDefaultConfigID(
                currentDefaultConfigId, newDefaultConfigId);

            // handle any error code if there is one
            this.provider.handleReturnCode(returnCode, this.nativeApi);

            // return null
            return null;
        });
    }

    @Override
	public void setDefaultConfigId(long configId) throws SzException {
        this.provider.execute(() -> {
            // call the underlying C function
            int returnCode = this.nativeApi.setDefaultConfigID(configId);

            // handle any error code if there is one
            this.provider.handleReturnCode(returnCode, this.nativeApi);

            // return null
            return null;
        });
    }

}
