package org.verapdf.features;

import org.verapdf.features.tools.FeatureTreeNode;

import java.util.List;

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
	 * @return list of roots for extracted data tree
	 */
	abstract List<FeatureTreeNode> getFeatures(FeaturesData data);

	/**
	 * @return type of object for which this extractor applies
	 */
	abstract FeaturesObjectTypesEnum getType();

}
