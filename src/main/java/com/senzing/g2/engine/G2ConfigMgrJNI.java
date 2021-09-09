package com.senzing.g2.engine;

/**
 * Implements the {@link G2Config} interface to call the native implementations
 * of the functions.
 */
public class G2ConfigMgrJNI implements G2ConfigMgr {
	static {
		System.loadLibrary("G2");
	}

	/**
	 * {@inheritDoc}
	 */
	public native int initV2(String		moduleName,
													 String		iniParams,
													 boolean	verboseLogging);

	/**
	 * {@inheritDoc}
	 */
	public native int destroy();

	/**
	 * {@inheritDoc}
	 */
	public native int addConfig(String 				configStr,
															String				configComments,
															Result<Long>	configID);

	/**
	 * {@inheritDoc}
	 */
	public native int getConfig(long configID, StringBuffer response);

	/**
	 * {@inheritDoc}
	 */
	public native int getConfigList(StringBuffer response);

	/**
	 * {@inheritDoc}
	 */
	public native int setDefaultConfigID(long configID);

	/**
	 * {@inheritDoc}
	 */
	public native int getDefaultConfigID(Result<Long> configID);
	
	/**
	 * {@inheritDoc}
	 */
	public native int replaceDefaultConfigID(long oldConfigID, long newConfigID);

	/**
	 * {@inheritDoc}
	 */
	public native String getLastException();

	/**
	 * {@inheritDoc}
	 */
	public native int getLastExceptionCode();

	/**
	 * {@inheritDoc}
	 */
	public native void clearLastException();
}
