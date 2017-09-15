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

import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Feature object for tilling pattern part of the features report
 *
 * @author Maksim Bezrukov
 */
public class TilingPatternFeaturesObject extends FeaturesObject {

	private static final String ID = "id";

	/**
	 * Constructs new tilling pattern feature object.
	 *
	 * @param adapter tilling pattern adapter class represents document object
	 */
	public TilingPatternFeaturesObject(TilingPatternFeaturesObjectAdapter adapter) {
		super(adapter);
	}

	/**
	 * @return PATTERN instance of the FeatureObjectType enumeration
	 */
	@Override
	public FeatureObjectType getType() {
		return FeatureObjectType.PATTERN;
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
		TilingPatternFeaturesObjectAdapter tpAdapter = (TilingPatternFeaturesObjectAdapter) this.adapter;
		FeatureTreeNode root = FeatureTreeNode.createRootNode("pattern");
		String id = tpAdapter.getID();
		if (id != null) {
			root.setAttribute(ID, id);
		}
		root.setAttribute("type", "tiling");
		root.addChild("paintType").setValue(String.valueOf(tpAdapter.getPaintType()));
		root.addChild("tilingType").setValue(String.valueOf(tpAdapter.getTilingType()));
		double[] bBox = tpAdapter.getBBox();
		CreateNodeHelper.addWidthHeightFeatures(bBox, root);
		CreateNodeHelper.addBoxFeature("bbox", bBox, root);
		Double xStep = tpAdapter.getXStep();
		if (xStep != null) {
			root.addChild("xStep").setValue(CreateNodeHelper.formatDouble(xStep, 3));
		}
		Double yStep = tpAdapter.getYStep();
		if (yStep != null) {
			root.addChild("yStep").setValue(CreateNodeHelper.formatDouble(yStep, 3));
		}
		CreateNodeHelper.parseMatrix(tpAdapter.getMatrix(), root.addChild("matrix"));
		parseResources(root);
		return root;
	}

	private void parseResources(FeatureTreeNode root) throws FeatureParsingException {
		TilingPatternFeaturesObjectAdapter tpAdapter = (TilingPatternFeaturesObjectAdapter) this.adapter;
		Set<String> extGStateChild = tpAdapter.getExtGStateChild();
		Set<String> colorSpaceChild = tpAdapter.getColorSpaceChild();
		Set<String> patternChild = tpAdapter.getPatternChild();
		Set<String> shadingChild = tpAdapter.getShadingChild();
		Set<String> xobjectChild = tpAdapter.getXObjectChild();
		Set<String> fontChild = tpAdapter.getFontChild();
		Set<String> propertiesChild = tpAdapter.getPropertiesChild();
		if ((extGStateChild != null && !extGStateChild.isEmpty()) ||
				(colorSpaceChild != null && !colorSpaceChild.isEmpty()) ||
				(patternChild != null && !patternChild.isEmpty()) ||
				(shadingChild != null && !shadingChild.isEmpty()) ||
				(xobjectChild != null && !xobjectChild.isEmpty()) ||
				(fontChild != null && !fontChild.isEmpty()) ||
				(propertiesChild != null && !propertiesChild.isEmpty())) {
			FeatureTreeNode resources = root.addChild("resources");

			CreateNodeHelper.parseIDSet(extGStateChild, "graphicsState", "graphicsStates", resources);
			CreateNodeHelper.parseIDSet(colorSpaceChild, "colorSpace", "colorSpaces", resources);
			CreateNodeHelper.parseIDSet(patternChild, "pattern", "patterns", resources);
			CreateNodeHelper.parseIDSet(shadingChild, "shading", "shadings", resources);
			CreateNodeHelper.parseIDSet(xobjectChild, "xobject", "xobjects", resources);
			CreateNodeHelper.parseIDSet(fontChild, "font", "fonts", resources);
			CreateNodeHelper.parseIDSet(propertiesChild, "propertiesDict", "propertiesDicts", resources);
		}
	}

	/**
	 * @return null
	 */
	@Override
	public FeaturesData getData() {
		return null;
	}

	static List<Feature> getFeaturesList() {
		// All fields are missing.
		// The problem is in Shading and Tilling patterns has the same FeatureObjectType.PATTERN
		// but different structure
		return Collections.emptyList();
	}
}
