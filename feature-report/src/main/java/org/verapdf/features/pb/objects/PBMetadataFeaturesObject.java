package org.verapdf.features.pb.objects;

import org.apache.log4j.Logger;
import org.apache.pdfbox.pdmodel.common.PDMetadata;
import org.verapdf.exceptions.featurereport.FeaturesTreeNodeException;
import org.verapdf.features.FeaturesObjectTypesEnum;
import org.verapdf.features.IFeaturesObject;
import org.verapdf.features.tools.ErrorsHelper;
import org.verapdf.features.tools.FeatureTreeNode;
import org.verapdf.features.tools.FeaturesCollection;

import java.io.IOException;
import java.nio.charset.Charset;

/**
 * Feature object for metadata
 *
 * @author Maksim Bezrukov
 */
public class PBMetadataFeaturesObject implements IFeaturesObject {

    private static final Logger LOGGER = Logger
            .getLogger(PBMetadataFeaturesObject.class);

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
            FeatureTreeNode root = parseMetadata(metadata, "metadata", null, collection);

            collection.addNewFeatureTree(FeaturesObjectTypesEnum.METADATA, root);
            return root;
        }
        return null;
    }

    /**
     * Creates FeatureTreeNode with name {@code nodeName}, parent {@code parent}, and content which is a stream r
     * epresentation of the {@code metadata} content. If there is an exception during getting metadata, then it
     * will create node with errorID and error for this situation.
     *
     * @param metadata   - PDMetadata class from which metadata will be taken
     * @param nodeName   - name for the created node
     * @param collection - collection for the created node
     * @return created node
     * @throws FeaturesTreeNodeException - occurs when wrong features tree node constructs
     */
    public static FeatureTreeNode parseMetadata(PDMetadata metadata, String nodeName, FeatureTreeNode parent, FeaturesCollection collection) throws FeaturesTreeNodeException {
        FeatureTreeNode node;
        if (parent == null) {
            node = FeatureTreeNode.newRootInstance(nodeName);
        } else {
            node = FeatureTreeNode.newChildInstance(nodeName, parent);
        }
        try {
            byte[] bStream = metadata.getByteArray();
            String metadataString = new String(bStream, Charset.forName("UTF-8"));
            node.setValue(metadataString);
        } catch (IOException e) {
            LOGGER.debug("Error while converting stream to string", e);
            node.addAttribute(ErrorsHelper.ERRORID, ErrorsHelper.METADATACONVERT_ID);
            ErrorsHelper.addErrorIntoCollection(collection, ErrorsHelper.METADATACONVERT_ID, ErrorsHelper.METADATACONVERT_MESSAGE);
        }

        return node;
    }
}
