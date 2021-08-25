package com.senzing.g2.engine.plugin;

public interface G2PluginInterface
{

	/* Status codes for plugin operations */
	static int PLUGIN_SUCCESS = 0;
	static int PLUGIN_SUCCESS_WITH_INFO = 1;
	static int PLUGIN_SIMPLE_ERROR = -1;
	static int PLUGIN_CRITICAL_ERROR = -20;
	static int PLUGIN_OUTPUT_BUFFER_SIZE_ERROR = -5;

	  
	/**
	 * Initializes the plugin
	 * @param context The plugin initialization context
	 * @return a status code for the initialization
	 */
	int init(InitContext context);
	
	/**
	 * Shuts down the plugin
	 * @return a status code for the initialization
	 */
	int shutdown();

	/**
	 * Gets the plugin version context information
	 * @param context The version context
	 * @return a status code for the initialization
	 */
	int getVersion(VersionContext version);

	
	/**
	 * Context for initializing.
	 *
	 */
	class InitContext 
	{
		private G2EngineContext engineContext;
		private String configInfo;
		private String errorMessage;
		
		/**
		 * Constructs the initialization context
		 */
		public InitContext(String configInfo,G2EngineContext engineContext)
		{
			this.configInfo = configInfo;
			this.engineContext = engineContext;
			errorMessage = null;
		}

		/**
		 * Gets the config info
		 * @return The config info string
		 */
		public String getConfigInfo() { return configInfo; }
		
		/**
		 * Gets the engine context
		 * @return The engine context
		 */
		public G2EngineContext getEngineContext() { return engineContext; }
		
		/**
		 * Get the error message (if any).
		 * @return The error message that was set or <tt>null</tt> if no error.
		 */
		public String getErrorMessage() { return errorMessage; }
		
		/**
		 * Sets the error message (if any).
		 * @param message The error message to set if an error occurs, or 
		 *                <tt>null</tt> to clear an error.
		 */
		public void setErrorMessage(String message) { errorMessage = message; }
	}

	/**
	 * Context for version.
	 *
	 */
	class VersionContext 
	{
		private String versionInfo;
		
		/**
		 * Constructs the version context
		 */
		public VersionContext()
		{
		}

		/**
		 * Gets the version info
		 * @return The version info
		 */
		public String getVersionInfo() { return versionInfo; }
		
		/**
		 * Gets the version info
		 * @param version The version info
		 */
		public void setVersionInfo(String version) { versionInfo = version; }
	}
}
