/**********************************************************************************
 © Copyright Senzing, Inc. 2021
 The source code for this program is not published or otherwise divested
 of its trade secrets, irrespective of what has been deposited with the U.S.
 Copyright Office.
**********************************************************************************/

package com.senzing.g2.engine;

/**
 * Defines the Java interface to the G2 secure store admin functions.
 */
public interface G2SSAdmin extends G2Fallible
{
	int init(String moduleName, String iniParams, boolean verboseLogging);
	int destroy();

	int initializeNewToken(String defaultSOPin, String newSOPin, String label);
	int reinitializeToken(String soPin, String label);
	
	int setupStore(String soPin);

	int list(StringBuffer response);
	int put(String soPin, String label, String value);

	String createSaltInStore(String soPin, String name, String method);
}
