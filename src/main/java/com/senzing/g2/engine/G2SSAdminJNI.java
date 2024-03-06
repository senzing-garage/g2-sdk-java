/**********************************************************************************
 © Copyright Senzing, Inc. 2021
 The source code for this program is not published or otherwise divested
 of its trade secrets, irrespective of what has been deposited with the U.S.
 Copyright Office.
**********************************************************************************/

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
	@Override
	public native int init(String 	moduleName,
												 String 	iniParams,
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
	public native int initializeNewToken(String defaultSOPin,
																			 String newSOPin,
																			 String label);

	/**
	 * {@inheritDoc}
	 */
	@Override
	public native int reinitializeToken(String soPin, String label);

	/**
	 * {@inheritDoc}
	 */
	@Override
	public native int setupStore(String soPin);

	/**
	 * {@inheritDoc}
	 */
	@Override
	public native int list(StringBuffer response);

	/**
	 * {@inheritDoc}
	 */
	@Override
	public native int put(String soPin, String label, String value);

	/**
	 * {@inheritDoc}
	 */
	@Override
	public native String createSaltInStore(String	soPin,
																				 String name,
																				 String method);

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
