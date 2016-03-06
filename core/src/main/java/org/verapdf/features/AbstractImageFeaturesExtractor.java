package org.verapdf.features;

import org.verapdf.features.tools.FeatureTreeNode;

import java.util.List;

/**
 * Base class for extending to extract features from images
 *
 * @author Maksim Bezrukov
 */
public abstract class AbstractImageFeaturesExtractor extends FeaturesExtractor {

	@Override
	final List<FeatureTreeNode> getFeatures(FeaturesData data) {
		return getImageFeatures((ImageFeaturesData) data);
	}

	@Override
	final FeaturesObjectTypesEnum getType() {
		return FeaturesObjectTypesEnum.IMAGE_XOBJECT;
	}

	/**
	 * Extract features from features data
	 *
	 * @param data features data for extractor
	 * @return list of roots for extracted data tree
	 */
	public abstract List<FeatureTreeNode> getImageFeatures(ImageFeaturesData data);

}


