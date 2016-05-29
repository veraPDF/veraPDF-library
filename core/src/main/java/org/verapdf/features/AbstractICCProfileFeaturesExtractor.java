package org.verapdf.features;

import org.verapdf.features.tools.FeatureTreeNode;

import java.util.List;

/**
 * Base class for extending to extract features from ICCProfiles
 *
 * @author Maksim Bezrukov
 */
public abstract class AbstractICCProfileFeaturesExtractor extends FeaturesExtractor {

	public AbstractICCProfileFeaturesExtractor(final String id, final String description) {
		super(FeaturesObjectTypesEnum.ICCPROFILE, id, description);
	}

	@Override
	final List<FeatureTreeNode> getFeatures(FeaturesData data) {
		return getICCProfileFeatures((ICCProfileFeaturesData) data);
	}

	/**
	 * Extract features from features data
	 *
	 * @param data features data for extractor
	 * @return list of roots for extracted data tree
	 */
	public abstract List<FeatureTreeNode> getICCProfileFeatures(ICCProfileFeaturesData data);

}
