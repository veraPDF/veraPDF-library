package org.verapdf.features;

import org.verapdf.features.tools.FeatureTreeNode;

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
}
