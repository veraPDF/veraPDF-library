package org.verapdf.features.pb.objects;

import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.cos.COSObject;
import org.apache.pdfbox.pdmodel.graphics.color.PDOutputIntent;
import org.verapdf.exceptions.featurereport.FeaturesTreeNodeException;
import org.verapdf.features.FeaturesObjectTypesEnum;
import org.verapdf.features.IFeaturesObject;
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
            FeatureTreeNode root = FeatureTreeNode.newInstance("outputIntent", null);

            COSBase base = outInt.getCOSObject();

            while (base instanceof COSObject) {
                base = ((COSObject) base).getObject();
            }

            if (base instanceof COSDictionary) {
                COSDictionary dict = (COSDictionary) base;
                COSBase baseType = dict.getDictionaryObject(COSName.S);

                while (baseType instanceof COSObject) {
                    baseType = ((COSObject) baseType).getObject();
                }

                if (baseType != null) {
                    FeatureTreeNode type = FeatureTreeNode.newInstance("subtype", root);
                    if (baseType instanceof COSName) {
                        type.setValue(((COSName) baseType).getName());
                    } else {
                        type.addAttribute(ErrorsHelper.ERRORID, ErrorsHelper.OUTPUTINTENTSTYPE_ID);
                        ErrorsHelper.addErrorIntoCollection(collection, ErrorsHelper.OUTPUTINTENTSTYPE_ID, ErrorsHelper.OUTPUTINTENTSTYPE_MESSAGE);
                    }
                }
            }

            if (outInt.getOutputCondition() != null) {
                FeatureTreeNode.newInstance("outputCondition", outInt.getOutputCondition(), root);
            }

            if (outInt.getOutputConditionIdentifier() != null) {
                FeatureTreeNode.newInstance("outputConditionIdentifier", outInt.getOutputConditionIdentifier(), root);
            }

            if (outInt.getRegistryName() != null) {
                FeatureTreeNode.newInstance("registryName", outInt.getRegistryName(), root);
            }

            if (outInt.getInfo() != null) {
                FeatureTreeNode.newInstance("info", outInt.getInfo(), root);
            }

            collection.addNewFeatureTree(FeaturesObjectTypesEnum.OUTPUTINTENT, root);

            return root;
        } else {
            return null;
        }
    }
}
