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
		FeatureTreeNode root = FeatureTreeNode.createRootNode("annotation");
		String id = annotationAdapter.getId();
		if (id != null) {
			root.setAttribute(CreateNodeHelper.ID, id);
		}

		CreateNodeHelper.addNotEmptyNode("subType", annotationAdapter.getSubtype(), root);
		CreateNodeHelper.addBoxFeature("rectangle", annotationAdapter.getRectangle(), root);
		CreateNodeHelper.addNotEmptyNode("contents", annotationAdapter.getContents(), root);
		CreateNodeHelper.addNotEmptyNode("annotationName", annotationAdapter.getAnnotationName(), root);
		CreateNodeHelper.addNotEmptyNode("modifiedDate", annotationAdapter.getModifiedDate(), root);

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

		CreateNodeHelper.addNotEmptyNode("invisible", String.valueOf(annotationAdapter.isInvisible()), root);
		CreateNodeHelper.addNotEmptyNode("hidden", String.valueOf(annotationAdapter.isHidden()), root);
		CreateNodeHelper.addNotEmptyNode("print", String.valueOf(annotationAdapter.isPrinted()), root);
		CreateNodeHelper.addNotEmptyNode("noZoom", String.valueOf(annotationAdapter.isNoZoom()), root);
		CreateNodeHelper.addNotEmptyNode("noRotate", String.valueOf(annotationAdapter.isNoRotate()), root);
		CreateNodeHelper.addNotEmptyNode("noView", String.valueOf(annotationAdapter.isNoView()), root);
		CreateNodeHelper.addNotEmptyNode("readOnly", String.valueOf(annotationAdapter.isReadOnly()), root);
		CreateNodeHelper.addNotEmptyNode("locked", String.valueOf(annotationAdapter.isLocked()), root);
		CreateNodeHelper.addNotEmptyNode("toggleNoView", String.valueOf(annotationAdapter.isToggleNoView()), root);
		CreateNodeHelper.addNotEmptyNode("lockedContents", String.valueOf(annotationAdapter.isLockedContents()), root);

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
		featuresList.add(new Feature("Subtype", "/annotation/subType", Feature.FeatureType.STRING));
		featuresList.add(new Feature("Contents", "/annotation/contents", Feature.FeatureType.STRING));
		featuresList.add(new Feature("Annotation Name", "/annotation/annotationName", Feature.FeatureType.STRING));
		featuresList.add(new Feature("Modified Date", "/annotation/modifiedDate", Feature.FeatureType.STRING));
		featuresList.add(new Feature("Invisible", "/annotation/invisible", Feature.FeatureType.BOOLEAN));
		featuresList.add(new Feature("Hidden", "/annotation/hidden", Feature.FeatureType.BOOLEAN));
		featuresList.add(new Feature("Print", "/annotation/print", Feature.FeatureType.BOOLEAN));
		featuresList.add(new Feature("No Zoom", "/annotation/noZoom", Feature.FeatureType.BOOLEAN));
		featuresList.add(new Feature("No Rotate", "/annotation/noRotate", Feature.FeatureType.BOOLEAN));
		featuresList.add(new Feature("No View", "/annotation/noView", Feature.FeatureType.BOOLEAN));
		featuresList.add(new Feature("Read Only", "/annotation/readOnly", Feature.FeatureType.BOOLEAN));
		featuresList.add(new Feature("Locked", "/annotation/locked", Feature.FeatureType.BOOLEAN));
		featuresList.add(new Feature("Toggle No View", "/annotation/toggleNoView", Feature.FeatureType.BOOLEAN));
		featuresList.add(new Feature("Locked Contents", "/annotation/lockedContents", Feature.FeatureType.BOOLEAN));

		featuresList.add(new Feature("Error IDs", "/@errorId", Feature.FeatureType.STRING));
		return featuresList;
	}
}
