package org.verapdf.features;

import org.verapdf.features.tools.FeatureTreeNode;

import java.util.List;

/**
 * @author Maksim Bezrukov
 */
public abstract class AbstractSignatureFeaturesExtractor extends FeaturesExtractor {

    public AbstractSignatureFeaturesExtractor() {
        super(FeatureObjectType.SIGNATURE);
    }

    @Override
    final List<FeatureTreeNode> getFeatures(FeaturesData data) {
        return getSignatureFeatures((SignatureFeaturesData) data);
    }

    /**
     * Extract features from features data
     *
     * @param data features data for extractor
     * @return list of roots for extracted data tree
     */
    public abstract List<FeatureTreeNode> getSignatureFeatures(SignatureFeaturesData data);
}
