package com.senzing.g2.engine.plugin;

import java.util.List;

/**
 * Standardizes a feature
 *
 */
public interface G2StandardizationPlugin extends G2PluginInterface
{
	/**
	 * Runs the feature standardization process
	 * 
	 */
	int process(ProcessingContext context);
	
	/**
	 * Context for processing.
	 * 
	 */
	class ProcessingContext
	{
		private FeatureInfo input = null;
		private FeatureInfo result = null;
		private String errorMessage = null;	

		/**
		 * Constructs an instance based on an input feature.
		 * 
		 * @param input The input feature.
		 */
		public ProcessingContext(FeatureInfo input) { 
			this.input = input;
			this.result = null;
		}
		
		/**
		 * Gets the input feature.
		 * @return The input feature.
		 */
		public FeatureInfo getInput() { return input; }
		
		/**
		 * Gets the result feature.
		 * @return The result feature.
		 */
		public FeatureInfo getResult() { return result; }

		/**
		 * Sets the result feature
		 * @param result The result feature
		 * @return The modifiable result {@link List}.
		 */
		public void setResult(FeatureInfo result) { this.result = result; }
		
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
	
}
