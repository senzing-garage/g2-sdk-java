package com.senzing.g2.engine;

public interface G2SSAdmin extends G2Fallible
{
	int initV2(String moduleName, String iniParams, boolean verboseLogging);
	int destroy();
	
	int initializeNewToken(String defaultSOPin, String newSOPin, String label);
	int reinitializeToken(String soPin, String label);
	
	int setupStore(String soPin);

	int list(StringBuffer response);
	int put(String soPin, String label, String value);

	String createSaltInStore(String soPin, String name, String method);
}
