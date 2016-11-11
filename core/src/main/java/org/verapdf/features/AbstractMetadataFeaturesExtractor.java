package org.verapdf.features;

import org.verapdf.features.tools.FeatureTreeNode;

import java.util.List;

/**
 * @author Maksim Bezrukov
 */
public abstract class AbstractMetadataFeaturesExtractor extends AbstractFeaturesExtractor {


    public AbstractMetadataFeaturesExtractor() {
        super(FeatureObjectType.METADATA);
    }

    @Override
    final List<FeatureTreeNode> getFeatures(FeaturesData data) {
        return getMetadataFeatures((MetadataFeaturesData) data);
    }

    /**
     * Extract features from features data
     *
     * @param data features data for extractor
     * @return list of roots for extracted data tree
     */
    public abstract List<FeatureTreeNode> getMetadataFeatures(MetadataFeaturesData data);
}
