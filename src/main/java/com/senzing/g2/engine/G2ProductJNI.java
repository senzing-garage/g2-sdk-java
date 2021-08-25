package com.senzing.g2.engine;

public class G2ProductJNI implements G2Product
{
	static {
		System.loadLibrary("G2");
	}

	public native int initV2(String moduleName, String iniParams, boolean verboseLogging);

	public native int destroy();

	public native String license();

	public native int validateLicenseFile(String licenseFile,StringBuffer errorResponse);

	public native String version();

	public native String getLastException();
	public native int getLastExceptionCode();
	public native void clearLastException();
}
