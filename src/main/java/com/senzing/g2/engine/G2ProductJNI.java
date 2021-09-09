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
	public native int initV2(String 	moduleName,
													 String 	iniParams,
													 boolean 	verboseLogging);

	/**
	 * {@inheritDoc}
	 */
	public native int destroy();

	/**
	 * {@inheritDoc}
	 */
	public native String license();

	/**
	 * {@inheritDoc}
	 */
	public native int validateLicenseFile(String 				licenseFile,
																				StringBuffer 	errorResponse);

	/**
	 * {@inheritDoc}
	 */
	public native String version();

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
