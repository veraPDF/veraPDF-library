package org.verapdf.features;

import org.verapdf.features.tools.FeatureTreeNode;

import java.util.List;

/**
 * Base class for extending to extract features from embedded files
 *
 * @author Maksim Bezrukov
 */
public abstract class AbstractEmbeddedFileFeaturesExtractor extends FeaturesExtractor {

	public AbstractEmbeddedFileFeaturesExtractor() {
		super(FeatureObjectType.EMBEDDED_FILE);
	}

	@Override
	final List<FeatureTreeNode> getFeatures(FeaturesData data) {
		return getEmbeddedFileFeatures((EmbeddedFileFeaturesData) data);
	}

	/**
	 * Extract features from features data
	 *
	 * @param data features data for extractor
	 * @return list of roots for extracted data tree
	 */
	public abstract List<FeatureTreeNode> getEmbeddedFileFeatures(EmbeddedFileFeaturesData data);
}
