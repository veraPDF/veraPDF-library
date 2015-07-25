package org.verapdf.features.pb.objects;

import org.apache.pdfbox.pdmodel.common.PDMetadata;
import org.verapdf.exceptions.featurereport.FeaturesTreeNodeException;
import org.verapdf.features.FeaturesObjectTypesEnum;
import org.verapdf.features.IFeaturesObject;
import org.verapdf.features.tools.ErrorsHelper;
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
     */
    @Override
    public FeatureTreeNode reportFeatures(FeaturesCollection collection) throws FeaturesTreeNodeException{
        if (metadata != null) {
            FeatureTreeNode root = FeatureTreeNode.newRootInstance("metadata");

            try {
                String metadataString = metadata.getInputStreamAsString();
                root.setValue(metadataString);
            } catch (IOException e) {
                root.addAttribute(ErrorsHelper.ERRORID, ErrorsHelper.METADATACONVERT_ID);
                ErrorsHelper.addErrorIntoCollection(collection, ErrorsHelper.METADATACONVERT_ID, ErrorsHelper.METADATACONVERT_MESSAGE);
            }

            collection.addNewFeatureTree(FeaturesObjectTypesEnum.METADATA, root);
            return root;
        } else {
            return null;
        }
    }
}
