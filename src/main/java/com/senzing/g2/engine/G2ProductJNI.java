package com.senzing.g2.engine;

/**
 * Implements the {@link G2Product} interface to call the native
 * implementations of each function.
 */
public class G2ProductJNI implements G2Product
{
	static {
		System.loadLibrary("G2");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public native int init(String 	moduleName,
												 String 	iniParams,
												 boolean 	verboseLogging);

	/**
	 * {@inheritDoc}
	 */
	@Override
	public native int destroy();

	/**
	 * {@inheritDoc}
	 */
	@Override
	public native String license();

	/**
	 * {@inheritDoc}
	 */
	@Override
	public native int validateLicenseFile(String 				licenseFile,
																				StringBuffer 	errorResponse);

	/**
	 * {@inheritDoc}
	 */
	@Override
	public native String version();

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
