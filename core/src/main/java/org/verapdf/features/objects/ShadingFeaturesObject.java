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

/**
 * Feature object for shading part of the features report
 *
 * @author Maksim Bezrukov
 */
public class ShadingFeaturesObject extends FeaturesObject {

	private static final String ID = "id";
	private static final String SHADING = "shading";
	private static final String SHADING_TYPE = "shadingType";
	private static final String ANTI_ALIAS = "antiAlias";

	/**
	 * Constructs new shading feature object.
	 *
	 * @param adapter shading adapter class represents document object
	 */
	public ShadingFeaturesObject(ShadingFeaturesObjectAdapter adapter) {
		super(adapter);
	}

	/**
	 * @return SHADING instance of the FeatureObjectType enumeration
	 */
	@Override
	public FeatureObjectType getType() {
		return FeatureObjectType.SHADING;
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
		ShadingFeaturesObjectAdapter shadingAdapter = (ShadingFeaturesObjectAdapter) this.adapter;
		FeatureTreeNode root = FeatureTreeNode.createRootNode(SHADING);
		String id = shadingAdapter.getID();
		if (id != null) {
			root.setAttribute(ID, id);
		}

		root.addChild(SHADING_TYPE).setValue(String.valueOf(shadingAdapter.getShadingType()));

		String colorSpaceChild = shadingAdapter.getColorSpaceChild();
		if (colorSpaceChild != null) {
			FeatureTreeNode shadingClr = root.addChild("colorSpace");
			shadingClr.setAttribute(ID, colorSpaceChild);
		}
		CreateNodeHelper.addBoxFeature("bbox", shadingAdapter.getBBox(), root);
		root.addChild(ANTI_ALIAS).setValue(String.valueOf(shadingAdapter.getAntiAlias()));
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
		// Missing fields:
		// * BBox
		List<Feature> featuresList = new ArrayList<>();
		featuresList.add(new Feature("Shading Type",
				generateVariableXPath(SHADING, SHADING_TYPE), Feature.FeatureType.NUMBER));
		featuresList.add(new Feature("Anti Alias",
				generateVariableXPath(SHADING, ANTI_ALIAS), Feature.FeatureType.BOOLEAN));
		featuresList.add(new Feature("Error IDs",
				generateAttributeXPath(SHADING, ErrorsHelper.ERRORID), Feature.FeatureType.STRING));
		return featuresList;
	}
}
