package org.verapdf.features;

import org.verapdf.features.tools.FeatureTreeNode;

import java.util.List;

/**
 * Base class for extending to extract features from fonts
 *
 * @author Maksim Bezrukov
 */
public abstract class AbstractFontFeaturesExtractor extends FeaturesExtractor {

	@Override
	List<FeatureTreeNode> getFeatures(FeaturesData data) {
		return getFontFeatures((FontFeaturesData) data);
	}

	@Override
	FeaturesObjectTypesEnum getType() {
		return FeaturesObjectTypesEnum.FONT;
	}

	/**
	 * Extract features from features data
	 *
	 * @param data features data for extractor
	 * @return list of roots for extracted data tree
	 */
	public abstract List<FeatureTreeNode> getFontFeatures(FontFeaturesData data);

}
