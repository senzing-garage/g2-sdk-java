package com.senzing.g2.engine;

public interface NativeApiProvider {
  /**
   * Provides a new instance of {@link NativeEngine} to use.
   *
   * @return A new instance of {@link NativeEngine} to use.
   */
  NativeEngine createEngineApi();

  /**
   * Provides a new instance of {@link NativeConfig} to use.
   *
   * @return A new instance of {@link NativeConfig} to use.
   */
  NativeConfig createConfigApi();

  /**
   * Provides a new instance of {@link NativeProduct} to use.
   *
   * @return A new instance of {@link NativeProduct} to use.
   */
  NativeProduct createProductApi();

  /**
   * Provides a new instance of {@link NativeConfigMgr} to use.
   *
   * @return A new instance of {@link NativeConfigMgr} to use.
   *
   */
  NativeConfigMgr createConfigMgrApi();

  /**
   * Provides a new instance of {@link NativeDiagnostic} to use.
   *
   * @return A new instance of {@link NativeDiagnostic} to use.
   */
  NativeDiagnostic createDiagnosticApi();
}
