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
	@Override
	public native int init(String		moduleName,
												 String		iniParams,
												 boolean	verboseLogging);

	/**
	 * {@inheritDoc}
	 */
	@Override
	public native int destroy();

	/**
	 * {@inheritDoc}
	 */
	@Override
	public native int addConfig(String 				configStr,
															String				configComments,
															Result<Long>	configID);

	/**
	 * {@inheritDoc}
	 */
	@Override
	public native int getConfig(long configID, StringBuffer response);

	/**
	 * {@inheritDoc}
	 */
	@Override
	public native int getConfigList(StringBuffer response);

	/**
	 * {@inheritDoc}
	 */
	@Override
	public native int setDefaultConfigID(long configID);

	/**
	 * {@inheritDoc}
	 */
	@Override
	public native int getDefaultConfigID(Result<Long> configID);
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public native int replaceDefaultConfigID(long oldConfigID, long newConfigID);

	/**
	 * {@inheritDoc}
	 */
	@Override
	public native String getLastException();

	/**
	 * {@inheritDoc}
	 */
	@Override
	public native int getLastExceptionCode();

	/**
	 * {@inheritDoc}
	 */
	@Override
	public native void clearLastException();
}
