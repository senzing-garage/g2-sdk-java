package com.senzing.g2.engine;

/**
 * Implements the {@link G2SSAdmin} interface to call the native function
 * implementations.
 */
public class G2SSAdminJNI implements G2SSAdmin
{
	static {
		System.loadLibrary("G2SSAdm");
	}

	/**
	 * {@inheritDoc}
	 */
	public native int initV2(String 	moduleName,
													 String 	iniParams,
													 boolean	verboseLogging);

	/**
	 * {@inheritDoc}
	 */
	public native int destroy();

	/**
	 * {@inheritDoc}
	 */
	public native int initializeNewToken(String defaultSOPin,
																			 String newSOPin,
																			 String label);

	/**
	 * {@inheritDoc}
	 */
	public native int reinitializeToken(String soPin, String label);

	/**
	 * {@inheritDoc}
	 */
	public native int setupStore(String soPin);

	/**
	 * {@inheritDoc}
	 */
	public native int list(StringBuffer response);

	/**
	 * {@inheritDoc}
	 */
	public native int put(String soPin, String label, String value);

	/**
	 * {@inheritDoc}
	 */
	public native String createSaltInStore(String	soPin,
																				 String name,
																				 String method);

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
