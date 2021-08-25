package com.senzing.g2.engine.plugin;

/**
 * Determines if one feature is a possible generalization of another feature
 * 
 */
public interface G2SuppressedFeaturePlugin extends G2PluginInterface
{
	
	/**
	 * Determines if one feature is a possible generalization of another feature.
	 * This is based on the features specified in the generalization context.
	 * 
	 */
	int checkForGeneralization(GeneralizationCheckContext context);

	/**
	 * Context for processing.
	 * 
	 */
	public class GeneralizationCheckContext 
	{
		private FeatureInfo primaryComponents = null;
		private FeatureInfo possibleGeneralizationComponents = null;
		private boolean generalized = false;
		private String errorMessage = null;
		
		/**
		 * Constructs with the features to check for generalization.
		 */
		public GeneralizationCheckContext(FeatureInfo primaryFeature,FeatureInfo possibleGeneralizationFeature)
		{
			this.primaryComponents 		= primaryFeature;
			this.possibleGeneralizationComponents 		= possibleGeneralizationFeature;
			generalized = false;
		}
		
		/**
		 * Gets the primary feature.
		 * @return The primary feature.
		 */
		public FeatureInfo getPrimaryComponents() { return primaryComponents; }
		
		/**
		 * Gets the possibly generalized feature.
		 * @return The possibly generalized feature.
		 */
		public FeatureInfo getPossibleGeneralizationComponents() { return possibleGeneralizationComponents; }
		
		/**
		 * Returns whether the feature in question was determined to be a generalization of the primary feature
		 * @return True if the feature in question is a generalization.
		 */
		public boolean isGeneralized() { return generalized; }

		/**
		 * Set the flag indicating whether the feature in question was determined to be a generalization of the primary feature
		 * @param gen A flag indicating whether the feature is a generalization. 
		 */
		public void setGeneralized(boolean gen) { generalized = gen; }
		
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
