package org.verapdf.features.pb.objects;

import org.apache.pdfbox.pdmodel.graphics.state.PDExtendedGraphicsState;
import org.verapdf.exceptions.featurereport.FeaturesTreeNodeException;
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
	 * @throws FeaturesTreeNodeException occurs when wrong features tree node constructs
	 */
	@Override
	public FeatureTreeNode reportFeatures(FeaturesCollection collection) throws FeaturesTreeNodeException {
		if (exGState != null) {
			FeatureTreeNode root = FeatureTreeNode.newRootInstance("graphicsState");

			root.addAttribute(ID, this.id);

			parseParents(root);

			FeatureTreeNode.newChildInstanceWithValue("transparency", String.valueOf(!exGState.getAlphaSourceFlag()), root);
			FeatureTreeNode.newChildInstanceWithValue("strokeAdjustment", String.valueOf(exGState.getAutomaticStrokeAdjustment()), root);
			FeatureTreeNode.newChildInstanceWithValue("overprintForStroke", String.valueOf(exGState.getStrokingOverprintControl()), root);
			FeatureTreeNode.newChildInstanceWithValue("overprintForFill", String.valueOf(exGState.getNonStrokingOverprintControl()), root);

			if (fontChildID != null) {
				FeatureTreeNode resources = FeatureTreeNode.newChildInstance("resources", root);
				FeatureTreeNode fonts = FeatureTreeNode.newChildInstance("fonts", resources);
				FeatureTreeNode font = FeatureTreeNode.newChildInstance("font", fonts);
				font.addAttribute(ID, fontChildID);
			}

			collection.addNewFeatureTree(FeaturesObjectTypesEnum.EXT_G_STATE, root);
			return root;
		}

		return null;
	}

	private void parseParents(FeatureTreeNode root) throws FeaturesTreeNodeException {
		if ((pageParentsID != null && !pageParentsID.isEmpty()) ||
				(patternParentsID != null && !patternParentsID.isEmpty()) ||
				(xobjectParentsID != null && !xobjectParentsID.isEmpty()) ||
				(fontParentsID != null && !fontParentsID.isEmpty())) {
			FeatureTreeNode parents = FeatureTreeNode.newChildInstance("parents", root);

			PBCreateNodeHelper.parseIDSet(pageParentsID, "page", null, parents);
			PBCreateNodeHelper.parseIDSet(patternParentsID, "pattern", null, parents);
			PBCreateNodeHelper.parseIDSet(xobjectParentsID, "xobject", null, parents);
			PBCreateNodeHelper.parseIDSet(fontParentsID, "font", null, parents);
		}
	}
}
