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
	 * @param context The {@link GeneralizationCheckContext} for performing the
	 *                operation.
	 *
	 * @return A non-negative number on success and a negative number on failure.
	 */
	int checkForGeneralization(GeneralizationCheckContext context);

	/**
	 * Context for processing.
	 * 
	 */
	class GeneralizationCheckContext
	{
		private FeatureInfo primaryComponents = null;
		private FeatureInfo possibleGeneralizationComponents = null;
		private boolean generalized = false;
		private String errorMessage = null;
		
		/**
		 * Constructs with the features to check for generalization.
		 *
		 * @param primaryFeature The {@link FeatureInfo} describing the primary
		 *                       feature.
		 * @param possibleGeneralizationFeature The {@link FeatureInfo} describing
		 *                                      the possible generalization feature.
		 */
		public GeneralizationCheckContext(FeatureInfo primaryFeature,
																			FeatureInfo possibleGeneralizationFeature)
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
		 * Returns whether or not the feature in question was determined to be a
		 * generalization of the primary feature.
		 *
		 * @return <code>true</code> if the feature in question is a generalization,
		 *         otherwise <code>false</code>.
		 */
		public boolean isGeneralized() { return generalized; }

		/**
		 * Set the flag indicating whether the feature in question was determined to be a generalization of the primary feature
		 * @param gen A flag indicating whether the feature is a generalization. 
		 */
		public void setGeneralized(boolean gen) { generalized = gen; }
		
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
