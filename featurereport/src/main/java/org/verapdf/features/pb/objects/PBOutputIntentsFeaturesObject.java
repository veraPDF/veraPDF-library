package org.verapdf.features.pb.objects;

import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.graphics.color.PDOutputIntent;
import org.verapdf.exceptions.featurereport.FeatureValueException;
import org.verapdf.exceptions.featurereport.FeaturesTreeNodeException;
import org.verapdf.features.FeaturesObjectTypesEnum;
import org.verapdf.features.IFeaturesObject;
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
     * @param outInt  - pdfbox class represents OutputIntent object
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
     * @throws FeaturesTreeNodeException   - occurs when wrong features tree node constructs
     * @throws FeatureValueException - occurs when wrong feature feature format found during features parser
     */
    @Override
    public FeatureTreeNode reportFeatures(FeaturesCollection collection) throws FeaturesTreeNodeException, FeatureValueException {
        FeatureTreeNode root = FeatureTreeNode.newInstance("outputIntent", null);

        COSName s = getOutputIntentSValue(outInt);
        if (s != null) {
            FeatureTreeNode type = FeatureTreeNode.newInstance("subtype", s.getName(), root);
        }

        if (outInt.getOutputCondition() != null) {
            FeatureTreeNode outputCondition = FeatureTreeNode.newInstance("outputCondition", outInt.getOutputCondition(), root);
        }

        if (outInt.getOutputConditionIdentifier() != null) {
            FeatureTreeNode outputConditionIdentifier = FeatureTreeNode.newInstance("outputConditionIdentifier", outInt.getOutputConditionIdentifier(), root);
        }

        if (outInt.getRegistryName() != null) {
            FeatureTreeNode registryName = FeatureTreeNode.newInstance("registryName", outInt.getRegistryName(), root);
        }

        if (outInt.getInfo() != null) {
            FeatureTreeNode info = FeatureTreeNode.newInstance("info", outInt.getInfo(), root);
        }

        collection.addNewFeatureTree(FeaturesObjectTypesEnum.OUTPUTINTENT, root);

        return root;
    }

    private static COSName getOutputIntentSValue(PDOutputIntent outInt) throws FeatureValueException {
        COSBase base = outInt.getCOSObject();
        if (base instanceof COSDictionary) {
            COSDictionary dict = (COSDictionary) base;
            COSBase baseType = dict.getDictionaryObject(COSName.S);

            if (baseType instanceof COSName) {
                return (COSName) baseType;
            } else {
                throw new FeatureValueException("In OutputIntent dictionary value for key \"S\" is not of type COSName.");
            }
        } else {
            throw new FeatureValueException("PDOutputIntent base object is not of type COSDictionary.");
        }
    }
}
