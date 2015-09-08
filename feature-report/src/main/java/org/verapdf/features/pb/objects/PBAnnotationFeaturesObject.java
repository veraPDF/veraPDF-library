package org.verapdf.features.pb.objects;

import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotation;
import org.verapdf.exceptions.featurereport.FeaturesTreeNodeException;
import org.verapdf.features.FeaturesObjectTypesEnum;
import org.verapdf.features.IFeaturesObject;
import org.verapdf.features.pb.tools.PBCreateNodeHelper;
import org.verapdf.features.tools.FeatureTreeNode;
import org.verapdf.features.tools.FeaturesCollection;

import java.util.Set;

/**
 * Feature object for annotation
 *
 * @author Maksim Bezrukov
 */
public class PBAnnotationFeaturesObject implements IFeaturesObject {

	private static final String ID = "id";
	private static final int LOCKED_CONTENTS_FLAG = 512;

	private PDAnnotation annot;
	private String id;
	private Set<String> pages;
	private String annotId;
	private String popupId;
	private Set<String> formXObjects;


	/**
	 * Constructs new Annotation Feature Object
	 *
	 * @param annot        pdfbox class represents annotation object
	 * @param id           annotation id
	 * @param pages        set of ids of all parent pages for this annotation
	 * @param annotId      ids of a parent annotation for this annotation
	 * @param popupId      id of the popup annotation
	 * @param formXObjects set of id of the form XObjects which used in appearance stream of this annotation
	 */
	public PBAnnotationFeaturesObject(PDAnnotation annot, String id, Set<String> pages,
									  String annotId, String popupId, Set<String> formXObjects) {
		this.annot = annot;
		this.id = id;
		this.pages = pages;
		this.annotId = annotId;
		this.popupId = popupId;
		this.formXObjects = formXObjects;
	}

	/**
	 * @return ANNOTATION instance of the FeaturesObjectTypesEnum enumeration
	 */
	@Override
	public FeaturesObjectTypesEnum getType() {
		return FeaturesObjectTypesEnum.ANNOTATION;
	}

	/**
	 * Reports featurereport into collection
	 *
	 * @param collection collection for feature report
	 * @return FeatureTreeNode class which represents a root node of the constructed collection tree
	 * @throws FeaturesTreeNodeException occurs when wrong features tree node constructs
	 */
	@Override
	public FeatureTreeNode reportFeatures(FeaturesCollection collection) throws FeaturesTreeNodeException {
		if (annot != null) {
			FeatureTreeNode root = FeatureTreeNode.newRootInstance("annotation");
			root.addAttribute(ID, id);

			addParents(root);

			PBCreateNodeHelper.addNotEmptyNode("subType", annot.getSubtype(), root);
			PBCreateNodeHelper.addBoxFeature("rectangle", annot.getRectangle(), root);
			PBCreateNodeHelper.addNotEmptyNode("contents", annot.getContents(), root);
			PBCreateNodeHelper.addNotEmptyNode("annotationName", annot.getAnnotationName(), root);
			PBCreateNodeHelper.addNotEmptyNode("modifiedDate", annot.getModifiedDate(), root);

			if (formXObjects != null && !formXObjects.isEmpty()) {
				FeatureTreeNode resources = FeatureTreeNode.newChildInstance("resources", root);
				for (String xObjID : formXObjects) {
					if (xObjID != null) {
						FeatureTreeNode xObjNode = FeatureTreeNode.newChildInstance("xObject", resources);
						xObjNode.addAttribute(ID, xObjID);
					}
				}
			}

			if (popupId != null) {
				FeatureTreeNode popup = FeatureTreeNode.newChildInstance("popup", root);
				popup.addAttribute(ID, popupId);
			}

			PBCreateNodeHelper.addDeviceColorSpaceNode("color", annot.getColor(), root, collection);

			PBCreateNodeHelper.addNotEmptyNode("invisible", String.valueOf(annot.isInvisible()), root);
			PBCreateNodeHelper.addNotEmptyNode("hidden", String.valueOf(annot.isHidden()), root);
			PBCreateNodeHelper.addNotEmptyNode("print", String.valueOf(annot.isPrinted()), root);
			PBCreateNodeHelper.addNotEmptyNode("noZoom", String.valueOf(annot.isNoZoom()), root);
			PBCreateNodeHelper.addNotEmptyNode("noRotate", String.valueOf(annot.isNoRotate()), root);
			PBCreateNodeHelper.addNotEmptyNode("noView", String.valueOf(annot.isNoView()), root);
			PBCreateNodeHelper.addNotEmptyNode("readOnly", String.valueOf(annot.isReadOnly()), root);
			PBCreateNodeHelper.addNotEmptyNode("locked", String.valueOf(annot.isLocked()), root);
			PBCreateNodeHelper.addNotEmptyNode("toggleNoView", String.valueOf(annot.isToggleNoView()), root);

			boolean lockedContents = (annot.getAnnotationFlags() & LOCKED_CONTENTS_FLAG) == LOCKED_CONTENTS_FLAG;
			PBCreateNodeHelper.addNotEmptyNode("lockedContents", String.valueOf(lockedContents), root);

			collection.addNewFeatureTree(FeaturesObjectTypesEnum.ANNOTATION, root);
			return root;
		}
		return null;
	}

	private void addParents(FeatureTreeNode root) throws FeaturesTreeNodeException {
		if ((pages != null && !pages.isEmpty()) || annotId != null) {
			FeatureTreeNode parents = FeatureTreeNode.newChildInstance("parents", root);

			if (pages != null) {
				for (String page : pages) {
					if (page != null) {
						FeatureTreeNode pageNode = FeatureTreeNode.newChildInstance("page", parents);
						pageNode.addAttribute(ID, page);
					}
				}
			}

			if (annotId != null) {
				FeatureTreeNode annotNode = FeatureTreeNode.newChildInstance("annotation", parents);
				annotNode.addAttribute(ID, annotId);
			}
		}
	}
}
