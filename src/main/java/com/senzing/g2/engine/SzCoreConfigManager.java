package com.senzing.g2.engine;

import static com.senzing.g2.engine.SzException.*;

/**
 * The package-protected implementation of {@link SzConfigManager} that works with the {@link
 * SzCoreEnvironment} class.
 */
public class SzCoreConfigManager implements SzConfigManager {
  /** Gets the class prefix to use for {@link SzException} construction. */
  private static final String CLASS_PREFIX = SzCoreConfigManager.class.getSimpleName();

  /** The {@link SzCoreEnvironment} that constructed this instance. */
  private SzCoreEnvironment env = null;

  /** The underlying {@link G2ConfigMgrJNI} instance. */
  G2ConfigMgrJNI nativeApi = null;

  /**
   * Constructs with the specified {@link SzCoreEnvironment}.
   *
   * @param environment The {@link SzCoreEnvironment} with which to construct.
   * @throws IllegalStateException If the underlying {@link SzCoreEnvironment} instance has already
   *     been destroyed.
   * @throws SzException If a Senzing failure occurs during initialization.
   */
  SzCoreConfigManager(SzCoreEnvironment environment) throws IllegalStateException, SzException {
    this.env = environment;
    this.env.execute(
        () -> {
          this.nativeApi = new G2ConfigMgrJNI();

          int returnCode =
              this.nativeApi.init(
                  this.env.getInstanceName(), this.env.getSettings(), this.env.isVerboseLogging());

          if (returnCode != 0) {
            this.env.handleReturnCode(
                returnCode,
                this.nativeApi,
                CLASS_PREFIX + "()",
                paramsOf(
                    "instanceName",
                    this.env.getInstanceName(),
                    "settings",
                    redact(this.env.getSettings()),
                    "verboseLogging",
                    this.env.isVerboseLogging()));
          }

          return null;
        });
  }

  /** The package-protected function to destroy the Senzing Config SDK. */
  void destroy() {
    synchronized (this) {
      if (this.nativeApi == null) return;
      this.nativeApi.destroy();
      this.nativeApi = null;
    }
  }

  /**
   * Checks if this instance has been destroyed by the associated {@link SzEnvironment}.
   *
   * @return <code>true</code> if this instance has been destroyed, otherwise <code>false</code>.
   */
  protected boolean isDestroyed() {
    synchronized (this) {
      return (this.nativeApi == null);
    }
  }

  @Override
  public long addConfig(String configDefinition, String configComment) throws SzException {
    return this.env.execute(
        () -> {
          // create the result object
          Result<Long> result = new Result<>();

          // call the underlying C function
          int returnCode = this.nativeApi.addConfig(configDefinition, configComment, result);

          // handle any error code if there is one
          if (returnCode != 0) {
            this.env.handleReturnCode(
                returnCode,
                this.nativeApi,
                CLASS_PREFIX + ".addConfig(String,String)",
                paramsOf("configDefinition", configDefinition, "configComment", configComment));
          }

          // return the config handle
          return result.getValue();
        });
  }

  @Override
  public String getConfig(long configId) throws SzException {
    return this.env.execute(
        () -> {
          // create the result object
          StringBuffer sb = new StringBuffer();

          // call the underlying C function
          int returnCode = this.nativeApi.getConfig(configId, sb);

          // handle any error code if there is one
          if (returnCode != 0) {
            this.env.handleReturnCode(
                returnCode,
                this.nativeApi,
                CLASS_PREFIX + ".getConfig(long)",
                paramsOf("configId", configId));
          }

          // return the config handle
          return sb.toString();
        });
  }

  @Override
  public String getConfigList() throws SzException {
    return this.env.execute(
        () -> {
          // create the result object
          StringBuffer sb = new StringBuffer();

          // call the underlying C function
          int returnCode = this.nativeApi.getConfigList(sb);

          // handle any error code if there is one
          if (returnCode != 0) {
            this.env.handleReturnCode(
                returnCode, this.nativeApi, CLASS_PREFIX + ".getConfigList()");
          }

          // return the config handle
          return sb.toString();
        });
  }

  @Override
  public long getDefaultConfigId() throws SzException {
    return this.env.execute(
        () -> {
          // create the result object
          Result<Long> result = new Result<>();

          // call the underlying C function
          int returnCode = this.nativeApi.getDefaultConfigID(result);

          // handle any error code if there is one
          if (returnCode != 0) {
            this.env.handleReturnCode(
                returnCode, this.nativeApi, CLASS_PREFIX + ".getDefaultConfigId()");
          }

          // return the config handle
          return result.getValue();
        });
  }

  @Override
  public void replaceDefaultConfigId(long currentDefaultConfigId, long newDefaultConfigId)
      throws SzException {
    this.env.execute(
        () -> {
          // call the underlying C function
          int returnCode =
              this.nativeApi.replaceDefaultConfigID(currentDefaultConfigId, newDefaultConfigId);

          // handle any error code if there is one
          if (returnCode != 0) {
            this.env.handleReturnCode(
                returnCode,
                this.nativeApi,
                CLASS_PREFIX + ".replaceDefaultConfigId(long,long)",
                paramsOf(
                    "currentDefaultConfigId", currentDefaultConfigId,
                    "newDefaultConfigId", newDefaultConfigId));
          }

          // return null
          return null;
        });
  }

  @Override
  public void setDefaultConfigId(long configId) throws SzException {
    this.env.execute(
        () -> {
          // call the underlying C function
          int returnCode = this.nativeApi.setDefaultConfigID(configId);

          // handle any error code if there is one
          if (returnCode != 0) {
            this.env.handleReturnCode(
                returnCode,
                this.nativeApi,
                CLASS_PREFIX + ".setDefaultConfigId(long)",
                paramsOf("configId", configId));
          }

          // return null
          return null;
        });
  }
}
