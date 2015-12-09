package org.verapdf.features.pb.objects;

import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.cos.COSObject;
import org.apache.pdfbox.pdmodel.graphics.color.PDOutputIntent;
import org.verapdf.core.FeatureParsingException;
import org.verapdf.features.FeaturesData;
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
	private String id;
	private String iccProfileID;

	/**
	 * Constructs new OutputIntent Feature Object
	 *
	 * @param outInt       pdfbox class represents OutputIntent object
	 * @param id           id of the outputIntent
	 * @param iccProfileID id of the icc profile which use in this outputIntent
	 */
	public PBOutputIntentsFeaturesObject(PDOutputIntent outInt, String id, String iccProfileID) {
		this.outInt = outInt;
		this.id = id;
		this.iccProfileID = iccProfileID;
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
	 * @param collection collection for feature report
	 * @return FeatureTreeNode class which represents a root node of the constructed collection tree
	 * @throws FeatureParsingException occurs when wrong features tree node constructs
	 */
	@Override
	public FeatureTreeNode reportFeatures(FeaturesCollection collection) throws FeatureParsingException {
		if (outInt != null) {
			FeatureTreeNode root = FeatureTreeNode.createRootNode("outputIntent");
			root.setAttribute("id", id);

			addSubtype(collection, root);

			PBCreateNodeHelper.addNotEmptyNode("outputCondition", outInt.getOutputCondition(), root);
			PBCreateNodeHelper.addNotEmptyNode("outputConditionIdentifier", outInt.getOutputConditionIdentifier(), root);
			PBCreateNodeHelper.addNotEmptyNode("registryName", outInt.getRegistryName(), root);
			PBCreateNodeHelper.addNotEmptyNode("info", outInt.getInfo(), root);

			FeatureTreeNode destOutInt = FeatureTreeNode.createChildNode("destOutputIntent", root);
			destOutInt.setAttribute("id", iccProfileID);

			collection.addNewFeatureTree(FeaturesObjectTypesEnum.OUTPUTINTENT, root);

			return root;
		}
		return null;
	}

	/**
	 * @return null
	 */
	@Override
	public FeaturesData getData() {
		return null;
	}

	private void addSubtype(FeaturesCollection collection, FeatureTreeNode root) throws FeatureParsingException {
		COSBase base = outInt.getCOSObject();
		if (base instanceof COSDictionary) {
			COSDictionary dict = (COSDictionary) base;
			COSBase baseType = dict.getDictionaryObject(COSName.S);

			while (baseType instanceof COSObject) {
				baseType = ((COSObject) baseType).getObject();
			}

			if (baseType != null) {
				FeatureTreeNode type = FeatureTreeNode.createChildNode("subtype", root);
				if (baseType instanceof COSName) {
					type.setValue(((COSName) baseType).getName());
				} else {
					ErrorsHelper.addErrorIntoCollection(collection,
							type,
							"Subtype is not of Name type");
				}
			}
		}
	}
}
