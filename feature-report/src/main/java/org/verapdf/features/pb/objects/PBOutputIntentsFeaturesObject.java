package org.verapdf.features.pb.objects;

import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.cos.COSObject;
import org.apache.pdfbox.pdmodel.graphics.color.PDOutputIntent;
import org.verapdf.exceptions.featurereport.FeaturesTreeNodeException;
import org.verapdf.features.FeaturesObjectTypesEnum;
import org.verapdf.features.IFeaturesObject;
import org.verapdf.features.pb.tools.PBCreateNodeHelper;
import org.verapdf.features.tools.ErrorsHelper;
import org.verapdf.features.tools.FeatureTreeNode;
import org.verapdf.features.tools.FeaturesCollection;

/**
 * Feature object for output intents
 *
 * @author Maksim Bezrukov
 */
public class PBOutputIntentsFeaturesObject implements IFeaturesObject {

    private PDOutputIntent outInt;

    /**
     * Constructs new OutputIntent Feature Object
     *
     * @param outInt - pdfbox class represents OutputIntent object
     */
    public PBOutputIntentsFeaturesObject(PDOutputIntent outInt) {
        this.outInt = outInt;
    }

    /**
     * @return OUTPUTINTENT instance of the FeaturesObjectTypesEnum enumeration
     */
    @Override
    public FeaturesObjectTypesEnum getType() {
        return FeaturesObjectTypesEnum.OUTPUTINTENT;
    }

    /**
     * Reports featurereport into collection
     *
     * @param collection - collection for feature report
     * @return FeatureTreeNode class which represents a root node of the constructed collection tree
     * @throws FeaturesTreeNodeException - occurs when wrong features tree node constructs
     */
    @Override
    public FeatureTreeNode reportFeatures(FeaturesCollection collection) throws FeaturesTreeNodeException {
        if (outInt != null) {
            FeatureTreeNode root = FeatureTreeNode.newRootInstance("outputIntent");

            addSubtype(collection, root);

            PBCreateNodeHelper.addNotEmptyNode("outputCondition", outInt.getOutputCondition(), root);
            PBCreateNodeHelper.addNotEmptyNode("outputConditionIdentifier", outInt.getOutputConditionIdentifier(), root);
            PBCreateNodeHelper.addNotEmptyNode("registryName", outInt.getRegistryName(), root);
            PBCreateNodeHelper.addNotEmptyNode("info", outInt.getInfo(), root);

            // TODO: Add iccProfiles support

            collection.addNewFeatureTree(FeaturesObjectTypesEnum.OUTPUTINTENT, root);

            return root;
        }
        return null;
    }

    private void addSubtype(FeaturesCollection collection, FeatureTreeNode root) throws FeaturesTreeNodeException {
        COSBase base = outInt.getCOSObject();
        if (base instanceof COSDictionary) {
            COSDictionary dict = (COSDictionary) base;
            COSBase baseType = dict.getDictionaryObject(COSName.S);

            while (baseType instanceof COSObject) {
                baseType = ((COSObject) baseType).getObject();
            }

            if (baseType != null) {
                FeatureTreeNode type = FeatureTreeNode.newChildInstance("subtype", root);
                if (baseType instanceof COSName) {
                    type.setValue(((COSName) baseType).getName());
                } else {
                    type.addAttribute(ErrorsHelper.ERRORID, ErrorsHelper.OUTPUTINTENTSTYPE_ID);
                    ErrorsHelper.addErrorIntoCollection(collection, ErrorsHelper.OUTPUTINTENTSTYPE_ID, ErrorsHelper.OUTPUTINTENTSTYPE_MESSAGE);
                }
            }
        }
    }
}
