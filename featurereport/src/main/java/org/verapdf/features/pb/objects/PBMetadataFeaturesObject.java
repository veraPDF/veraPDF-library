package org.verapdf.features.pb.objects;

import org.apache.pdfbox.pdmodel.common.PDMetadata;
import org.verapdf.exceptions.featurereport.FeatureValueException;
import org.verapdf.exceptions.featurereport.FeaturesTreeNodeException;
import org.verapdf.features.FeaturesObjectTypesEnum;
import org.verapdf.features.IFeaturesObject;
import org.verapdf.features.tools.FeatureTreeNode;
import org.verapdf.features.tools.FeaturesCollection;

import java.io.IOException;

/**
 * Feature object for metadata
 *
 * @author Maksim Bezrukov
 */
public class PBMetadataFeaturesObject implements IFeaturesObject {

    private PDMetadata metadata;

    /**
     * Constructs new Metadata Feature Object
     *
     * @param metadata  - pdfbox class represents metadata object
     */
    public PBMetadataFeaturesObject(PDMetadata metadata) {
        this.metadata = metadata;
    }

    /**
     * @return METADATA instance of the FeaturesObjectTypesEnum enumeration
     */
    @Override
    public FeaturesObjectTypesEnum getType() {
        return FeaturesObjectTypesEnum.METADATA;
    }

    /**
     * Reports featurereport into collection
     *
     * @param collection - collection for feature report
     * @return FeatureTreeNode class which represents a root node of the constructed collection tree
     * @throws FeaturesTreeNodeException   - occurs when wrong features tree node constructs
     * @throws FeatureValueException - occurs when wrong feature feature format found during features parser
     */
    @Override
    public FeatureTreeNode reportFeatures(FeaturesCollection collection) throws FeaturesTreeNodeException, FeatureValueException {

        try {
            String metadataString = metadata.getInputStreamAsString();

            FeatureTreeNode root = FeatureTreeNode.newInstance("metadata", metadataString, null);

            collection.addNewFeatureTree(FeaturesObjectTypesEnum.METADATA, root);

            return root;
        } catch (IOException e) {
            throw new FeatureValueException("Error while converting metadata stream into a string value with use of ISO-8859-1 encoding.", e);
        }
    }
}
