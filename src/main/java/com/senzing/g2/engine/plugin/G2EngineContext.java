package com.senzing.g2.engine.plugin;


/**
 * Provides an interface to internal engine resources and algorithms
 * 
 */
public interface G2EngineContext
{
	
	/**
	 * Retrieves a system parameter
	 */
	int getSystemParameter(SystemParameterContext context);
	
	/**
	 * Context for retrieving system parameters
	 *
	 */
	class SystemParameterContext
	{
		private String parameterGroup;
		private String parameterName;
		private String parameterValue;
				
		/**
		 * Constructs the system parameter context
		 */
		public SystemParameterContext()
		{
			this.parameterGroup = null;
			this.parameterName = null;
			this.parameterValue = null;
		}

		/**
		 * Get the parameter group
		 * @return The parameter group.
		 */
		public String getParameterGroup() { return parameterGroup; }
		
		/**
		 * Sets the parameter group
		 */
		public SystemParameterContext setParameterGroup(String str) { parameterGroup = str; return this; }
		
		/**
		 * Get the parameter name
		 * @return The parameter name.
		 */
		public String getParameterName() { return parameterName; }
		
		/**
		 *  Sets the parameter name
		 */
		public SystemParameterContext setParameterName(String str) { parameterName = str; return this; }
				
		/**
		 * Get the parameter value
		 * @return The parameter value.
		 */
		public String getParameterValue() { return parameterValue; }

		/**
		 * Get the parameter value as a boolean
		 * @return The parameter value as a boolean.
		 */
		public boolean getParameterValueAsBoolean()
		{
			if ((parameterValue != null) && (parameterValue.length() > 0))
			{
				char start = parameterValue.charAt(0);
				switch ( start )
				{
				case 'Y':
				case 'y':
				case 'T':
				case 't':
				case '1':
					return true;
				}
			}
			return false;
		}
	}
	
	
	/**
	 * Performs a string scoring operation
	 */
	int scoreStrings(StringScoringContext context);
	
	/**
	 * Context for scoring strings.
	 *
	 */
	class StringScoringContext
	{
		private String str1;
		private boolean str1IsHashed;
		
		private String str2;
		private boolean str2IsHashed;

		private String elemType;
		
		private int score;
		
		/**
		 * Constructs the scoring context
		 */
		public StringScoringContext()
		{
			this.str1 = null;
			this.str1IsHashed = false;
			this.str2 = null;
			this.str2IsHashed = false;
			this.elemType = null;
			this.score = 0;
		}

		/**
		 * Get the first string value
		 * @return The first string value.
		 */
		public String getString1() { return str1; }
		
		/**
		 * Sets the first string value
		 */
		public StringScoringContext setString1(String str) { str1 = str; return this; }
		
		/**
		 * Get whether the first string value is hashed
		 * @return Boolean flag indicating if the first string value is hashed
		 */
		public boolean isString1Hashed() { return str1IsHashed; }
		
		/**
		 *  Marks the first string value as hashed
		 */
		public StringScoringContext setString1Hashed(boolean hashed) { str1IsHashed = hashed; return this; }
		
		/**
		 * Get the second string value
		 * @return The second string value.
		 */
		public String getString2() { return str2; }
		
		/**
		 *  Sets the second string value
		 */
		public StringScoringContext setString2(String str) { str2 = str; return this; }
		
		/**
		 * Get whether the second string value is hashed
		 * @return Boolean flag indicating if the second string value is hashed
		 */
		public boolean isString2Hashed() { return str2IsHashed; }
		
		/**
		 *  Marks the second string value as hashed
		 */
		public StringScoringContext setString2Hashed(boolean hashed) { str2IsHashed = hashed; return this; }
		
		/**
		 * Get the element type
		 * @return The element type.
		 */
		public String getElemType() { return elemType; }
		
		/**
		 *  Sets the element type
		 */
		public StringScoringContext setElemType(String elemType) { this.elemType = elemType; return this; }
		
		/**
		 * Get the score
		 * @return The score.
		 */
		public int getScore() { return score; }
	}

}
