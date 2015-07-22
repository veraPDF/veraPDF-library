package org.verapdf.features.pb.objects;

import org.apache.pdfbox.cos.COSString;
import org.apache.pdfbox.pdmodel.encryption.AccessPermission;
import org.apache.pdfbox.pdmodel.encryption.PDEncryption;
import org.verapdf.exceptions.featurereport.FeaturesTreeNodeException;
import org.verapdf.features.FeaturesObjectTypesEnum;
import org.verapdf.features.IFeaturesObject;
import org.verapdf.features.pb.tools.PBCreateNodeHelper;
import org.verapdf.features.tools.ErrorsHelper;
import org.verapdf.features.tools.FeatureTreeNode;
import org.verapdf.features.tools.FeaturesCollection;

import java.io.IOException;

/**
 * Features object for document security
 *
 * @author Maksim Bezrukov
 */
public class PBDocSecurityFeaturesObject implements IFeaturesObject {

    private PDEncryption encryption;

    /**
     * Constructs new Document Security Feature Object
     *
     * @param encryption - pdfbox class represents Encryption object
     */
    public PBDocSecurityFeaturesObject(PDEncryption encryption) {
        this.encryption = encryption;
    }

    /**
     * @return DOCUMENT_SECURITY instance of the FeaturesObjectTypesEnum enumeration
     */
    @Override
    public FeaturesObjectTypesEnum getType() {
        return FeaturesObjectTypesEnum.DOCUMENT_SECURITY;
    }

    /**
     * Reports all features from the object into the collection
     *
     * @param collection - collection for feature report
     * @return FeatureTreeNode class which represents a root node of the constructed collection tree
     * @throws FeaturesTreeNodeException - occurs when wrong features tree node constructs
     */
    @Override
    public FeatureTreeNode reportFeatures(FeaturesCollection collection) throws FeaturesTreeNodeException {
        if (encryption != null) {
            FeatureTreeNode root = FeatureTreeNode.newInstance("documentSecurity", null);
            PBCreateNodeHelper.addNotEmptyNode("filter", encryption.getFilter(), root);
            PBCreateNodeHelper.addNotEmptyNode("subFilter", encryption.getSubFilter(), root);
            PBCreateNodeHelper.addNotEmptyNode("version", String.valueOf(encryption.getVersion()), root);
            PBCreateNodeHelper.addNotEmptyNode("length", String.valueOf(encryption.getLength()), root);

            try {
                String ownerKey = new COSString(encryption.getOwnerKey()).toString();
                PBCreateNodeHelper.addNotEmptyNode("ownerKey", ownerKey, root);
            } catch (IOException e) {
                FeatureTreeNode ownerKey = FeatureTreeNode.newInstance("ownerKey", root);
                ownerKey.addAttribute(ErrorsHelper.ERRORID, ErrorsHelper.BYTETOSTRING_ID);
                ErrorsHelper.addErrorIntoCollection(collection, ErrorsHelper.BYTETOSTRING_ID, ErrorsHelper.BYTETOSTRING_MESSAGE);
            }

            try {
                String userKey = new COSString(encryption.getUserKey()).toString();
                PBCreateNodeHelper.addNotEmptyNode("userKey", userKey, root);
            } catch (IOException e) {
                FeatureTreeNode userKey = FeatureTreeNode.newInstance("userKey", root);
                userKey.addAttribute(ErrorsHelper.ERRORID, ErrorsHelper.BYTETOSTRING_ID);
                ErrorsHelper.addErrorIntoCollection(collection, ErrorsHelper.BYTETOSTRING_ID, ErrorsHelper.BYTETOSTRING_MESSAGE);
            }

            PBCreateNodeHelper.addNotEmptyNode("encryptMetadata", String.valueOf(encryption.isEncryptMetaData()), root);

            try {
                if (encryption.getSecurityHandler() != null) {
                    AccessPermission accessPermissions = new AccessPermission(encryption.getPermissions());

                    PBCreateNodeHelper.addNotEmptyNode("assembleDocumentAllowed", String.valueOf(accessPermissions.canAssembleDocument()), root);
                    PBCreateNodeHelper.addNotEmptyNode("extractContentAllowed", String.valueOf(accessPermissions.canExtractContent()), root);
                    PBCreateNodeHelper.addNotEmptyNode("extractForAccessibilityAllowed", String.valueOf(accessPermissions.canExtractForAccessibility()), root);
                    PBCreateNodeHelper.addNotEmptyNode("fillInFormAllowed", String.valueOf(accessPermissions.canFillInForm()), root);
                    PBCreateNodeHelper.addNotEmptyNode("modifyAllowed", String.valueOf(accessPermissions.canModify()), root);
                    PBCreateNodeHelper.addNotEmptyNode("modifyAnnotationsAllowed", String.valueOf(accessPermissions.canModifyAnnotations()), root);
                    PBCreateNodeHelper.addNotEmptyNode("printAllowed", String.valueOf(accessPermissions.canPrint()), root);
                    PBCreateNodeHelper.addNotEmptyNode("printDegradedAllowed", String.valueOf(accessPermissions.canPrintDegraded()), root);
                }
            } catch (IOException e) {
                FeatureTreeNode.newInstance("securityHandler", "No security handler", root);
            }

            collection.addNewFeatureTree(FeaturesObjectTypesEnum.DOCUMENT_SECURITY, root);
            return root;
        } else {
            return null;
        }
    }
}
