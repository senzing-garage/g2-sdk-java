package com.senzing.g2.engine.plugin;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

/**
 * Generates new expressed features from existing features
 */
public interface G2ExpressedFeaturePlugin extends G2PluginInterface
{
	/**
	 * Runs the expressed feature processing.
	 * @param context The {@link ProcessingContext} for performing the operation.
	 * @return A non-negative number on success and a negative number on failure.
	 */
	int process(ProcessingContext context);

	/**
	 * Context for processing.
	 */
	class ProcessingContext
	{
		private List<FeatureInfo> input = null;
		private List<FeatureInfo> result = null;		
		private String errorMessage = null;	

		/**
		 * Constructs with the {@link List} of {@link FeatureInfo} instances
		 * describing the feature values to be processed.
		 * @param input The {@link List} of {@link FeatureInfo} instances to be
		 *              processed.
		 */
		public ProcessingContext(List<FeatureInfo> input) {
			this.input  = input;
			this.result = new ArrayList<>(input.size() < 5 ? 10 : input.size() * 2);
		}
		
		/**
		 * Gets an <b>unmodifiable</b> {@link List} containing the {@link
		 * FeatureInfo} instances describing the input feature values.
		 * 
		 * @return An <b>unmodifiable</b> {@link List} of {@link FeatureInfo}
		 *         instances.
		 */
		public List<FeatureInfo> getInput() { 
			return Collections.unmodifiableList(input); 
		}
		
		/**
		 * Gets the modifiable result {@link List}.  The caller is expected to
		 * call {@link List#add(Object)} one or more times to add results.
		 * 
		 * @return The modifiable result {@link List}.
		 */
		public List<FeatureInfo> getResult() { 
			return this.result;
		}
		
		/**
		 * Get the error message (if any).
		 * @return The error message that was set or <code>null</code> if no error.
		 */
		public String getErrorMessage() { return errorMessage; }
		
		/**
		 * Sets the error message (if any).
		 * @param message The error message to set if an error occurs, or 
		 *                <code>null</code> to clear an error.
		 */
		public void setErrorMessage(String message) { errorMessage = message; }
	}
	
}
