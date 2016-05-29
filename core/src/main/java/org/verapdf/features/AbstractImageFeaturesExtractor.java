package org.verapdf.features;

import org.verapdf.features.tools.FeatureTreeNode;

import java.util.List;

/**
 * Base class for extending to extract features from images
 *
 * @author Maksim Bezrukov
 */
public abstract class AbstractImageFeaturesExtractor extends FeaturesExtractor {

	public AbstractImageFeaturesExtractor(final String id, final String description) {
		super(FeaturesObjectTypesEnum.IMAGE_XOBJECT, id, description);
	}

	@Override
	final List<FeatureTreeNode> getFeatures(FeaturesData data) {
		return getImageFeatures((ImageFeaturesData) data);
	}

	/**
	 * Extract features from features data
	 *
	 * @param data features data for extractor
	 * @return list of roots for extracted data tree
	 */
	public abstract List<FeatureTreeNode> getImageFeatures(ImageFeaturesData data);

}


