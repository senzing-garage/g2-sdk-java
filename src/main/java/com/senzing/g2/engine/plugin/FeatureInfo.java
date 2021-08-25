package com.senzing.g2.engine.plugin;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

import static java.util.Objects.*;

/**
 * Describes multiple feature element values for a specific feature type.
 * Instances of this class are not modifiable after being constructed.
 * 
 */
public class FeatureInfo
{
	private String featureTypeCode = null;
	private List<FeatureElementInfo> featureElements = null;

	/**
	 * Constructs with the feature type code and zero or more {@link FeatureElementInfo} instances.
	 * 
	 * @param featureTypeCode
	 * @param elementInfos
	 */
	public FeatureInfo(String 					featureTypeCode,
					   FeatureElementInfo... 	elementInfos)
	{ 
		requireNonNull(featureTypeCode, "The feature type code cannot be null");
	
		this.featureTypeCode = featureTypeCode;
		this.featureElements = new ArrayList<>(elementInfos.length);
		for (FeatureElementInfo info : elementInfos) {
			this.featureElements.add(info);
		}
	}
	
	
	/**
	 * Gets the associated feature type code for the feature values.
	 * @return The feature type code for the feature values.
	 */
	public String getFeatureTypeCode() { return featureTypeCode; }
	
	/**
	 * Gets an <b>unmodifiable</b> {@link List} of the {@link FeatureElementInfo}
	 * instances describing the feature values for this instance.
	 * 
	 * @return An <b>unmodifiable</b> {@link List} of {@link FeatureElementInfo}
	 *         instances for this instance.
	 */
	public List<FeatureElementInfo> getFeatureElementValues() {
		return Collections.unmodifiableList(featureElements);
	}
}
