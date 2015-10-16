package org.verapdf.features;

import org.verapdf.features.tools.FeatureTreeNode;

import java.util.List;
import java.util.Map;

/**
 * Base class for extending to extract features from ICCProfiles
 *
 * @author Maksim Bezrukov
 */
public abstract class AbstractICCProfileFeaturesExtractor extends FeaturesExtractor {

	@Override
	List<FeatureTreeNode> getFeatures(FeaturesData data) {
		return getICCProfileFeatures((ICCProfileFeaturesData) data);
	}

	@Override
	FeaturesObjectTypesEnum getType() {
		return FeaturesObjectTypesEnum.ICCPROFILE;
	}

	/**
	 * Extract features from features data
	 *
	 * @param data features data for extractor
	 * @return root for extracted data tree
	 */
	public abstract List<FeatureTreeNode> getICCProfileFeatures(ICCProfileFeaturesData data);

	/**
	 * Initializing
	 *
	 * @param parametrs parametrs for the initializing
	 */
	@Override
	public abstract void initialize(Map<String, String> parametrs);
}
