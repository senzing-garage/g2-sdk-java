package com.senzing.g2.engine;

public interface G2ConfigMgr extends G2Fallible 
{
	int initV2(String moduleName, String iniParams, boolean verboseLogging);

	int destroy();

	int addConfig(String configStr, String configComments, Result<Long> configID);
	int getConfig(long configID, StringBuffer response);
	
	int getConfigList(StringBuffer response);

	int setDefaultConfigID(long configID);
	int getDefaultConfigID(Result<Long> configID);
	
	int replaceDefaultConfigID(long oldConfigID, long newConfigID);
}
