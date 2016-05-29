package org.verapdf.features;

import org.verapdf.features.tools.FeatureTreeNode;

import java.nio.file.Path;
import java.util.List;

/**
 * Class for features extractors
 *
 * @author Maksim Bezrukov
 */
abstract class FeaturesExtractor {
    public final String DEFAULT_ID = FeaturesExtractor.class.getName();
    public final String DEFAULT_DESCRIPTION = "description";

    private final FeaturesObjectTypesEnum type;
    private final String id;
    private final String description;

    FeaturesExtractor(final FeaturesObjectTypesEnum type, final String id, final String description) {
        this.type = type;
        this.id = id;
        this.description = description;
    }

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
    public FeaturesObjectTypesEnum getType() {
        return this.type;
    }

    /**
     * @return ID of the plugin
     */
    public String getID() {
        return this.id;
    }

    /**
     * @return Description of the plugin
     */
    public String getDescription() {
        return this.description;
    }

}
