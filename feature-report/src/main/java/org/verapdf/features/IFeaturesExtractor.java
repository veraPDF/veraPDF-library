package org.verapdf.features;

import org.verapdf.features.tools.FeatureTreeNode;

import java.util.Map;

/**
 * Interface for features extractors
 *
 * @author Maksim Bezrukov
 */
public interface IFeaturesExtractor {

	/**
	 * Extract features from features data
	 *
	 * @param data features data for extractor
	 * @return root for extracted data tree
	 */
	FeatureTreeNode getFeatures(FeaturesData data);

	/**
	 * @return type of object for which this extractor applies
	 */
	FeaturesObjectTypesEnum getType();

	/**
	 * Initializing extractor
	 *
	 * @param parametrs parametrs for the initializing
	 */
	void initialize(Map<String, String> parametrs);
}
