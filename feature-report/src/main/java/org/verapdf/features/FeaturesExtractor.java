package org.verapdf.features;

import org.verapdf.features.tools.FeatureTreeNode;

import java.util.List;
import java.util.Map;

/**
 * Class for features extractors
 *
 * @author Maksim Bezrukov
 */
abstract class FeaturesExtractor {

	/**
	 * Extract features from features data
	 *
	 * @param data features data for extractor
	 * @return root for extracted data tree
	 */
	abstract List<FeatureTreeNode> getFeatures(FeaturesData data);

	/**
	 * @return type of object for which this extractor applies
	 */
	abstract FeaturesObjectTypesEnum getType();

	/**
	 * Initializing extractor
	 *
	 * @param parametrs parametrs for the initializing
	 */
	public abstract void initialize(Map<String, String> parametrs);
}
