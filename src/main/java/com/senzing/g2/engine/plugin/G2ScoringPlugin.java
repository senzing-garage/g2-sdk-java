package com.senzing.g2.engine.plugin;
import java.util.Set;
import java.util.Map;

/**
 * Scores two different features to determine equivalence.
 */
public interface G2ScoringPlugin extends G2PluginInterface
{
	/**
	 * Run the feature scoring process
	 * @param context The feature scoring context
	 * @return a status code for the feature scoring operation.
	 */
	int score(ScoringContext context);
	
	/**
	 * Context for scoring.
	 */
	class ScoringContext
	{
		private FeatureInfo feat1 = null;
		private FeatureInfo feat2 = null;
		private Map<String,String> results = null;
		private String errorMessage = null;	
	
		/**
		 * Constructs with the features to compare for scoring.
		 *
		 * @param feature1 The first feature to compare.
		 * @param feature2 The second feature to compare.
		 */
		public ScoringContext(FeatureInfo feature1,
							  					FeatureInfo feature2)
		{ 
			this.feat1 		= feature1;
			this.feat2 		= feature2;
			this.results	= new ArrayMap<>();
		}

		/**
		 * Gets the first feature to compare for scoring.
		 * @return The first feature to compare for scoring.
		 */
		public FeatureInfo getFeature1() { return this.feat1; }
		
		/**
		 * Gets the second feature to compare for scoring.
		 * @return The second feature to compare for scoring.
		 */
		public FeatureInfo getFeature2() { return this.feat2; }

		/**
		 * Get the error message (if any).
		 * @return The error message that was set or <code>null</code> if no error.
		 */
		public String getErrorMessage() { return this.errorMessage; }
		
		/**
		 * Sets the error message (if any).
		 * @param message The error message to set if an error occurs, or 
		 *                <code>null</code> to clear an error.
		 */
		public void setErrorMessage(String message) { errorMessage = message; }

		/**
		 * Returns a {@link Map} which can be directly modified to set
		 * the results for this context.  The plugin is expected to call
		 * {@link Map#put(Object, Object)} on the returned {@link Map} 
		 * for each result value to be added.
		 * 
		 * @return The {@link Map} to add the results to.
		 */
		public Map<String, String> getResults() {
			return this.results;
		}
	}

	/**
	 * Gets the score names as XML.  The default implementation calls
	 * {@link #getScoreNames(Set)} and constructs the XML from the
	 * populated list and appends it to the specified {@link StringBuilder}.
	 *
	 * @param scoreNames The {@link StringBuilder} to append the XML to.
	 *
	 * @return A non-negative number on success and a negative number on failure.
	 */
	default int getScoreNames(StringBuilder scoreNames) {
		Set<String> scoreSet = new ArraySet<>();
		int returnCode = getScoreNames(scoreSet);
		if (returnCode < 0) return returnCode;
		scoreNames.append("[");
		boolean firstAdded = false;
		for (String scoreName: scoreSet)
		{
			if (firstAdded)
			{
				scoreNames.append(",");
			}
			firstAdded = true;
			scoreNames.append("\"").append(scoreName).append("\"");
		}
		scoreNames.append("]");
		return returnCode;
	}
	
	/**
	 * This method is called to populate the specified {@link Set} with the
	 * scoring names that will be produced.
	 * 
	 * @param scoreNames The {@link Set} to be populated.
	 * @return A non-negative number on success and a negative number on failure.
	 */
	int getScoreNames(Set<String> scoreNames);
	
}
