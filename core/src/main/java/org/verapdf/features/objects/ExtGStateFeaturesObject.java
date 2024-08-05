/**
 * This file is part of veraPDF Library core, a module of the veraPDF project.
 * Copyright (c) 2015-2024, veraPDF Consortium <info@verapdf.org>
 * All rights reserved.
 *
 * veraPDF Library core is free software: you can redistribute it and/or modify
 * it under the terms of either:
 *
 * The GNU General public license GPLv3+.
 * You should have received a copy of the GNU General Public License
 * along with veraPDF Library core as the LICENSE.GPL file in the root of the source
 * tree.  If not, see http://www.gnu.org/licenses/ or
 * https://www.gnu.org/licenses/gpl-3.0.en.html.
 *
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
import org.verapdf.features.tools.ErrorsHelper;
import org.verapdf.features.tools.FeatureTreeNode;

import java.util.ArrayList;
import java.util.List;

/**
 * Feature object for ExtGState
 *
 * @author Maksim Bezrukov
 */
public class ExtGStateFeaturesObject extends FeaturesObject {

	private static final String ID = "id";
	private static final String GRAPHICS_STATE = "graphicsState";
	private static final String TRANSPARENCY = "transparency";
	private static final String STROKE_ADJUSTMENT = "strokeAdjustment";
	private static final String OVERPRINT_FOR_STROKE = "overprintForStroke";
	private static final String OVERPRINT_FOR_FILL = "overprintForFill";

	/**
	 * Constructs new ExtGState Feature Object
	 *
	 * @param adapter class represents extfstate adapter
	 */
	public ExtGStateFeaturesObject(ExtGStateFeaturesObjectAdapter adapter) {
		super(adapter);
	}

	/**
	 * @return EXT_G_STATE instance of the FeatureObjectType enumeration
	 */
	@Override
	public FeatureObjectType getType() {
		return FeatureObjectType.EXT_G_STATE;
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
		ExtGStateFeaturesObjectAdapter docSecAdapter = (ExtGStateFeaturesObjectAdapter) this.adapter;
		FeatureTreeNode root = FeatureTreeNode.createRootNode(GRAPHICS_STATE);

		String id = docSecAdapter.getId();
		if (id != null) {
			root.setAttribute(ID, id);
		}

		Boolean transparency = docSecAdapter.getTransparency();
		if (transparency != null) {
			root.addChild(TRANSPARENCY).setValue(String.valueOf(transparency.booleanValue()));
		}
		Boolean strokeAdjustment = docSecAdapter.getStrokeAdjustment();
		if (strokeAdjustment != null) {
			root.addChild(STROKE_ADJUSTMENT).setValue(String.valueOf(strokeAdjustment.booleanValue()));
		}
		Boolean overprintForStroke = docSecAdapter.getOverprintForStroke();
		if (overprintForStroke != null) {
			root.addChild(OVERPRINT_FOR_STROKE).setValue(String.valueOf(overprintForStroke.booleanValue()));
		}
		Boolean overprintForFill = docSecAdapter.getOverprintForFill();
		if (overprintForFill != null) {
			root.addChild(OVERPRINT_FOR_FILL).setValue(String.valueOf(overprintForFill.booleanValue()));
		}

		String fontChildID = docSecAdapter.getFontChildId();
		if (fontChildID != null) {
			FeatureTreeNode resources = root.addChild("resources");
			FeatureTreeNode fonts = resources.addChild("fonts");
			FeatureTreeNode font = fonts.addChild("font");
			font.setAttribute(ID, fontChildID);
		}
		return root;
	}

	@Override
	public FeaturesData getData() {
		return null;
	}

	static List<Feature> getFeaturesList() {
		// All fields are present
		List<Feature> featuresList = new ArrayList<>();
		featuresList.add(new Feature("Transparency",
				generateVariableXPath(GRAPHICS_STATE, TRANSPARENCY), Feature.FeatureType.BOOLEAN));
		featuresList.add(new Feature("Stroke Adjustment",
				generateVariableXPath(GRAPHICS_STATE, STROKE_ADJUSTMENT), Feature.FeatureType.BOOLEAN));
		featuresList.add(new Feature("Overprint For Stroke",
				generateVariableXPath(GRAPHICS_STATE, OVERPRINT_FOR_STROKE), Feature.FeatureType.BOOLEAN));
		featuresList.add(new Feature("Overprint For Fill",
				generateVariableXPath(GRAPHICS_STATE, OVERPRINT_FOR_FILL), Feature.FeatureType.BOOLEAN));

		featuresList.add(new Feature("Error IDs",
				generateAttributeXPath(GRAPHICS_STATE, ErrorsHelper.ERRORID), Feature.FeatureType.STRING));
		return featuresList;
	}
}
