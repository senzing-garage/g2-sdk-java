package com.senzing.g2.engine;

public class G2SSAdminJNI implements G2SSAdmin
{
	static {
		System.loadLibrary("G2SSAdm");
	}

	public native int initV2(String moduleName, String iniParams, boolean verboseLogging);
	public native int destroy();
	
	public native int initializeNewToken(String defaultSOPin, String newSOPin, String label);
	public native int reinitializeToken(String soPin, String label);
	
	public native int setupStore(String soPin);

	public native int list(StringBuffer response);
	public native int put(String soPin, String label, String value);

	public native String createSaltInStore(String soPin, String name, String method);

	public native String getLastException();
	public native int getLastExceptionCode();
	public native void clearLastException();
}
