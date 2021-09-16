package com.senzing.g2.engine.plugin;

import static java.util.Objects.*;
import static com.senzing.g2.engine.plugin.FeatureElementValueFormat.*;

/**
 * Describes a feature element value with its code, value and formatting.
 * Instances of this class are not modifiable after being constructed.
 *
 */
public class FeatureElementInfo
{
	private String elementCode = null;
	private String elementValue = null;
	private FeatureElementValueFormat felemValueFormat = NONE;

	/**
	 * Constructs an instance with the specified element code and element value
	 * using {@link FeatureElementValueFormat#NONE} as the format.
	 * 
	 * @param elementCode The feature element code.
	 * @param elementValue The feature element value.
	 */
	public FeatureElementInfo(String elementCode,
							  String elementValue)
	{
		this(elementCode, elementValue, NONE);
	}
	
	/**
	 * Constructs an instance with the specified element code, element value
	 * and {@link FeatureElementValueFormat}.
	 * 
	 * @param elementCode The feature element code.
	 * @param elementValue The feature element value.
	 * @param format The {@link FeatureElementValueFormat} to use.
	 */
	public FeatureElementInfo(String 					elementCode,
			                  String 					elementValue,
			                  FeatureElementValueFormat	format)  
	{
		requireNonNull(elementCode, "The feature element code cannot be null.");
		requireNonNull(format, "The feature element value format cannot be null");
		this.elementCode 		= elementCode;
		this.elementValue 		= (elementValue == null ? "" : elementValue);
		this.felemValueFormat	= format;
	}
	
	/**
	 * Gets the feature element code.
	 * @return The feature element code.
	 */
	public String getElementCode() { return elementCode; }
	
	/**
	 * Gets the feature element value.
	 * @return The feature element value.
	 */
	public String getElementValue() { return elementValue; }
	
	/**
	 * Gets the feature element value format for the value.
	 * @return The feature element value format for the value.
	 */
	public FeatureElementValueFormat getElementValueFormat() { return felemValueFormat; }
}
