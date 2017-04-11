/**
 * This file is part of veraPDF Library core, a module of the veraPDF project.
 * Copyright (c) 2015, veraPDF Consortium <info@verapdf.org>
 * All rights reserved.
 * <p>
 * veraPDF Library core is free software: you can redistribute it and/or modify
 * it under the terms of either:
 * <p>
 * The GNU General public license GPLv3+.
 * You should have received a copy of the GNU General Public License
 * along with veraPDF Library core as the LICENSE.GPL file in the root of the source
 * tree.  If not, see http://www.gnu.org/licenses/ or
 * https://www.gnu.org/licenses/gpl-3.0.en.html.
 * <p>
 * The Mozilla Public License MPLv2+.
 * You should have received a copy of the Mozilla Public License along with
 * veraPDF Library core as the LICENSE.MPL file in the root of the source tree.
 * If a copy of the MPL was not distributed with this file, you can obtain one at
 * http://mozilla.org/MPL/2.0/.
 */
package org.verapdf.features.objects;

import org.verapdf.core.FeatureParsingException;
import org.verapdf.features.FeatureObjectType;
import org.verapdf.features.FeaturesData;
import org.verapdf.features.tools.CreateNodeHelper;
import org.verapdf.features.tools.ErrorsHelper;
import org.verapdf.features.tools.FeatureTreeNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Feature object for annotation
 *
 * @author Maksim Bezrukov
 */
public class AnnotationFeaturesObject extends FeaturesObject {

	private static final String SUB_TYPE = "subType";
	private static final String ANNOTATION = "annotation";
	private static final String CONTENTS = "contents";
	private static final String ANNOTATION_NAME = "annotationName";
	private static final String MODIFIED_DATE = "modifiedDate";
	private static final String INVISIBLE = "invisible";
	private static final String HIDDEN = "hidden";
	private static final String PRINT = "print";
	private static final String NO_ZOOM = "noZoom";
	private static final String NO_ROTATE = "noRotate";
	private static final String NO_VIEW = "noView";
	private static final String READ_ONLY = "readOnly";
	private static final String LOCKED = "locked";
	private static final String TOGGLE_NO_VIEW = "toggleNoView";
	private static final String LOCKED_CONTENTS = "lockedContents";

	/**
	 * Constructs new Annotation Feature Object
	 *
	 * @param adapter class represents annotation adapter
	 */
	public AnnotationFeaturesObject(AnnotationFeaturesObjectAdapter adapter) {
		super(adapter);
	}

	/**
	 * @return ANNOTATION instance of the FeatureObjectType enumeration
	 */
	@Override
	public FeatureObjectType getType() {
		return FeatureObjectType.ANNOTATION;
	}

	/**
	 * Reports all features from the object into the collection
	 *
	 * @return FeatureTreeNode class which represents a root node of the
	 * constructed collection tree
	 * @throws FeatureParsingException occurs when wrong features tree node constructs
	 */
	@Override
	public FeatureTreeNode collectFeatures() throws FeatureParsingException {
		AnnotationFeaturesObjectAdapter annotationAdapter = (AnnotationFeaturesObjectAdapter) this.adapter;
		FeatureTreeNode root = FeatureTreeNode.createRootNode(ANNOTATION);
		String id = annotationAdapter.getId();
		if (id != null) {
			root.setAttribute(CreateNodeHelper.ID, id);
		}

		CreateNodeHelper.addNotEmptyNode(SUB_TYPE, annotationAdapter.getSubtype(), root);
		CreateNodeHelper.addBoxFeature("rectangle", annotationAdapter.getRectangle(), root);
		CreateNodeHelper.addNotEmptyNode(CONTENTS, annotationAdapter.getContents(), root);
		CreateNodeHelper.addNotEmptyNode(ANNOTATION_NAME, annotationAdapter.getAnnotationName(), root);
		CreateNodeHelper.addNotEmptyNode(MODIFIED_DATE, annotationAdapter.getModifiedDate(), root);

		Set<String> formXObjects = annotationAdapter.getFormXObjectsResources();

		if (formXObjects != null && !formXObjects.isEmpty()) {
			FeatureTreeNode resources = root.addChild("resources");
			for (String xObjID : formXObjects) {
				if (xObjID != null) {
					FeatureTreeNode xObjNode = resources.addChild("xobject");
					xObjNode.setAttribute(CreateNodeHelper.ID, xObjID);
				}
			}
		}

		String popupId = annotationAdapter.getPopupId();
		if (popupId != null) {
			FeatureTreeNode popup = root.addChild("popup");
			popup.setAttribute(CreateNodeHelper.ID, popupId);
		}

		CreateNodeHelper.addDeviceColorSpaceNode("color", annotationAdapter.getColor(), root, this);

		CreateNodeHelper.addNotEmptyNode(INVISIBLE, String.valueOf(annotationAdapter.isInvisible()), root);
		CreateNodeHelper.addNotEmptyNode(HIDDEN, String.valueOf(annotationAdapter.isHidden()), root);
		CreateNodeHelper.addNotEmptyNode(PRINT, String.valueOf(annotationAdapter.isPrinted()), root);
		CreateNodeHelper.addNotEmptyNode(NO_ZOOM, String.valueOf(annotationAdapter.isNoZoom()), root);
		CreateNodeHelper.addNotEmptyNode(NO_ROTATE, String.valueOf(annotationAdapter.isNoRotate()), root);
		CreateNodeHelper.addNotEmptyNode(NO_VIEW, String.valueOf(annotationAdapter.isNoView()), root);
		CreateNodeHelper.addNotEmptyNode(READ_ONLY, String.valueOf(annotationAdapter.isReadOnly()), root);
		CreateNodeHelper.addNotEmptyNode(LOCKED, String.valueOf(annotationAdapter.isLocked()), root);
		CreateNodeHelper.addNotEmptyNode(TOGGLE_NO_VIEW, String.valueOf(annotationAdapter.isToggleNoView()), root);
		CreateNodeHelper.addNotEmptyNode(LOCKED_CONTENTS, String.valueOf(annotationAdapter.isLockedContents()), root);

		return root;
	}

	/**
	 * @return null
	 */
	@Override
	public FeaturesData getData() {
		return null;
	}

	static List<Feature> getFeaturesList() {
		// Missed fields:
		// * rectangle
		// * resources
		// * popup
		// * color
		List<Feature> featuresList = new ArrayList<>();
		featuresList.add(new Feature("Subtype",
				generateVariableXPath(ANNOTATION, SUB_TYPE), Feature.FeatureType.STRING));
		featuresList.add(new Feature("Contents",
				generateVariableXPath(ANNOTATION, CONTENTS), Feature.FeatureType.STRING));
		featuresList.add(new Feature("Annotation Name",
				generateVariableXPath(ANNOTATION, ANNOTATION_NAME), Feature.FeatureType.STRING));
		featuresList.add(new Feature("Modified Date",
				generateVariableXPath(ANNOTATION, MODIFIED_DATE), Feature.FeatureType.STRING));
		featuresList.add(new Feature("Invisible",
				generateVariableXPath(ANNOTATION, INVISIBLE), Feature.FeatureType.BOOLEAN));
		featuresList.add(new Feature("Hidden",
				generateVariableXPath(ANNOTATION, HIDDEN), Feature.FeatureType.BOOLEAN));
		featuresList.add(new Feature("Print",
				generateVariableXPath(ANNOTATION, PRINT), Feature.FeatureType.BOOLEAN));
		featuresList.add(new Feature("No Zoom",
				generateVariableXPath(ANNOTATION, NO_ZOOM), Feature.FeatureType.BOOLEAN));
		featuresList.add(new Feature("No Rotate",
				generateVariableXPath(ANNOTATION, NO_ROTATE), Feature.FeatureType.BOOLEAN));
		featuresList.add(new Feature("No View",
				generateVariableXPath(ANNOTATION, NO_VIEW), Feature.FeatureType.BOOLEAN));
		featuresList.add(new Feature("Read Only",
				generateVariableXPath(ANNOTATION, READ_ONLY), Feature.FeatureType.BOOLEAN));
		featuresList.add(new Feature("Locked",
				generateVariableXPath(ANNOTATION, LOCKED), Feature.FeatureType.BOOLEAN));
		featuresList.add(new Feature("Toggle No View",
				generateVariableXPath(ANNOTATION, TOGGLE_NO_VIEW), Feature.FeatureType.BOOLEAN));
		featuresList.add(new Feature("Locked Contents",
				generateVariableXPath(ANNOTATION, LOCKED_CONTENTS), Feature.FeatureType.BOOLEAN));

		featuresList.add(new Feature("Error IDs",
				generateAttributeXPath(ANNOTATION, ErrorsHelper.ERRORID), Feature.FeatureType.STRING));
		return featuresList;
	}
}
