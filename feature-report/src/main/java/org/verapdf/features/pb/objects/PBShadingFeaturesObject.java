package org.verapdf.features.pb.objects;

import org.apache.pdfbox.pdmodel.graphics.shading.PDShading;
import org.verapdf.core.FeatureParsingException;
import org.verapdf.features.FeaturesData;
import org.verapdf.features.FeaturesObjectTypesEnum;
import org.verapdf.features.IFeaturesObject;
import org.verapdf.features.pb.tools.PBCreateNodeHelper;
import org.verapdf.features.tools.FeatureTreeNode;
import org.verapdf.features.tools.FeaturesCollection;

import java.util.Set;

/**
 * Features object for shading
 *
 * @author Maksim Bezrukov
 */
public class PBShadingFeaturesObject implements IFeaturesObject {

	private static final String ID = "id";

	private PDShading shading;
	private String id;
	private String colorSpaceChild;
	private Set<String> pageParent;
	private Set<String> patternParent;
	private Set<String> xobjectParent;
	private Set<String> fontParent;

	/**
	 * Constructs new shading features object
	 *
	 * @param shading         PDShading which represents shading for feature report
	 * @param id              id of the object
	 * @param colorSpaceChild colorSpace id which contains in this shading
	 * @param pageParent      set of page ids which contains the given shading as its resources
	 * @param patternParent   set of pattern ids which contains the given shading as its resources
	 * @param xobjectParent   set of xobject ids which contains the given shading as its resources
	 * @param fontParent      set of font ids which contains the given shading as its resources
	 */
	public PBShadingFeaturesObject(PDShading shading, String id, String colorSpaceChild, Set<String> pageParent, Set<String> patternParent, Set<String> xobjectParent, Set<String> fontParent) {
		this.shading = shading;
		this.id = id;
		this.colorSpaceChild = colorSpaceChild;
		this.pageParent = pageParent;
		this.patternParent = patternParent;
		this.xobjectParent = xobjectParent;
		this.fontParent = fontParent;
	}

	/**
	 * @return SHADING instance of the FeaturesObjectTypesEnum enumeration
	 */
	@Override
	public FeaturesObjectTypesEnum getType() {
		return FeaturesObjectTypesEnum.SHADING;
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
		if (shading != null) {
			FeatureTreeNode root = FeatureTreeNode.createRootNode("shading");
			root.setAttribute(ID, id);

			parseParents(root);

			FeatureTreeNode.createChildNode("shadingType", root).setValue(String.valueOf(shading.getShadingType()));

			if (colorSpaceChild != null) {
				FeatureTreeNode shadingClr = FeatureTreeNode.createChildNode("colorSpace", root);
				shadingClr.setAttribute(ID, colorSpaceChild);
			}

			PBCreateNodeHelper.addBoxFeature("bbox", shading.getBBox(), root);

			FeatureTreeNode.createChildNode("antiAlias", root).setValue(String.valueOf(shading.getAntiAlias()));

			collection.addNewFeatureTree(FeaturesObjectTypesEnum.SHADING, root);
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

	private void parseParents(FeatureTreeNode root) throws FeatureParsingException {
		if ((pageParent != null && !pageParent.isEmpty()) ||
				(patternParent != null && !patternParent.isEmpty()) ||
				(xobjectParent != null && !xobjectParent.isEmpty()) ||
				(fontParent != null && !fontParent.isEmpty())) {
			FeatureTreeNode parents = FeatureTreeNode.createChildNode("parents", root);

			PBCreateNodeHelper.parseIDSet(pageParent, "page", null, parents);
			PBCreateNodeHelper.parseIDSet(patternParent, "pattern", null, parents);
			PBCreateNodeHelper.parseIDSet(xobjectParent, "xobject", null, parents);
			PBCreateNodeHelper.parseIDSet(fontParent, "font", null, parents);
		}
	}
}
