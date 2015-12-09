package org.verapdf.features.pb.objects;

import org.apache.pdfbox.pdmodel.graphics.state.PDExtendedGraphicsState;
import org.verapdf.core.FeatureParsingException;
import org.verapdf.features.FeaturesData;
import org.verapdf.features.FeaturesObjectTypesEnum;
import org.verapdf.features.IFeaturesObject;
import org.verapdf.features.pb.tools.PBCreateNodeHelper;
import org.verapdf.features.tools.FeatureTreeNode;
import org.verapdf.features.tools.FeaturesCollection;

import java.util.Set;

/**
 * Feature object for extended graphics state
 *
 * @author Maksim Bezrukov
 */
public class PBExtGStateFeaturesObject implements IFeaturesObject {

	private static final String ID = "id";

	private PDExtendedGraphicsState exGState;
	private String id;
	private String fontChildID;
	private Set<String> pageParentsID;
	private Set<String> patternParentsID;
	private Set<String> xobjectParentsID;
	private Set<String> fontParentsID;

	/**
	 * Constructs new extended graphics state feature object
	 *
	 * @param exGState         PDExtendedGraphicsState which represents extended graphics state for feature report
	 * @param id               id of the object
	 * @param fontChildID      id of the font child
	 * @param pageParentsID    set of page ids which contains the given extended graphics state as its resources
	 * @param patternParentsID set of pattern ids which contains the given extended graphics state as its resources
	 * @param xobjectParentsID set of xobject ids which contains the given extended graphics state as its resources
	 * @param fontParentsID    set of font ids which contains the given extended graphics state as its resources
	 */
	public PBExtGStateFeaturesObject(PDExtendedGraphicsState exGState,
									 String id,
									 String fontChildID,
									 Set<String> pageParentsID,
									 Set<String> patternParentsID,
									 Set<String> xobjectParentsID,
									 Set<String> fontParentsID) {
		this.exGState = exGState;
		this.id = id;
		this.fontChildID = fontChildID;
		this.pageParentsID = pageParentsID;
		this.patternParentsID = patternParentsID;
		this.xobjectParentsID = xobjectParentsID;
		this.fontParentsID = fontParentsID;
	}

	/**
	 * @return EXT_G_STATE instance of the FeaturesObjectTypesEnum enumeration
	 */
	@Override
	public FeaturesObjectTypesEnum getType() {
		return FeaturesObjectTypesEnum.EXT_G_STATE;
	}

	/**
	 * Reports all features from the object into the collection
	 *
	 * @param collection collection for feature report
	 * @return FeatureTreeNode class which represents a root node of the constructed collection tree
	 * @throws FeatureParsingException occurs when wrong features tree node constructs
	 */
	@Override
	public FeatureTreeNode reportFeatures(FeaturesCollection collection) throws FeatureParsingException {
		if (exGState != null) {
			FeatureTreeNode root = FeatureTreeNode.createRootNode("graphicsState");

			root.setAttribute(ID, this.id);

			parseParents(root);

			FeatureTreeNode.createChildNode("transparency", root).setValue(String.valueOf(!exGState.getAlphaSourceFlag()));
			FeatureTreeNode.createChildNode("strokeAdjustment", root).setValue(String.valueOf(exGState.getAutomaticStrokeAdjustment()));
			FeatureTreeNode.createChildNode("overprintForStroke", root).setValue(String.valueOf(exGState.getStrokingOverprintControl()));
			FeatureTreeNode.createChildNode("overprintForFill", root).setValue(String.valueOf(exGState.getNonStrokingOverprintControl()));

			if (fontChildID != null) {
				FeatureTreeNode resources = FeatureTreeNode.createChildNode("resources", root);
				FeatureTreeNode fonts = FeatureTreeNode.createChildNode("fonts", resources);
				FeatureTreeNode font = FeatureTreeNode.createChildNode("font", fonts);
				font.setAttribute(ID, fontChildID);
			}

			collection.addNewFeatureTree(FeaturesObjectTypesEnum.EXT_G_STATE, root);
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
		if ((pageParentsID != null && !pageParentsID.isEmpty()) ||
				(patternParentsID != null && !patternParentsID.isEmpty()) ||
				(xobjectParentsID != null && !xobjectParentsID.isEmpty()) ||
				(fontParentsID != null && !fontParentsID.isEmpty())) {
			FeatureTreeNode parents = FeatureTreeNode.createChildNode("parents", root);

			PBCreateNodeHelper.parseIDSet(pageParentsID, "page", null, parents);
			PBCreateNodeHelper.parseIDSet(patternParentsID, "pattern", null, parents);
			PBCreateNodeHelper.parseIDSet(xobjectParentsID, "xobject", null, parents);
			PBCreateNodeHelper.parseIDSet(fontParentsID, "font", null, parents);
		}
	}
}
