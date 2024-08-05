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
import org.verapdf.features.tools.CreateNodeHelper;
import org.verapdf.features.tools.FeatureTreeNode;

import java.util.Collections;
import java.util.List;

/**
 * Feature object for shading pattern part of the features report
 *
 * @author Maksim Bezrukov
 */
public class ShadingPatternFeaturesObject extends FeaturesObject {

	private static final String ID = "id";

	/**
	 * Constructs new shading pattern feature object.
	 *
	 * @param adapter shading pattern adapter class represents document object
	 */
	public ShadingPatternFeaturesObject(ShadingPatternFeaturesObjectAdapter adapter) {
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
		ShadingPatternFeaturesObjectAdapter spAdapter = (ShadingPatternFeaturesObjectAdapter) this.adapter;
		FeatureTreeNode root = FeatureTreeNode.createRootNode("pattern");
		String id = spAdapter.getID();
		if (id != null) {
			root.setAttribute(ID, id);
		}
		root.setAttribute("type", "shading");

		String shadingChild = spAdapter.getShadingChild();
		if (shadingChild != null) {
			FeatureTreeNode shading = root.addChild("shading");
			shading.setAttribute(ID, shadingChild);
		}

		CreateNodeHelper.parseMatrix(spAdapter.getMatrix(), root.addChild("matrix"));

		String extGStateChild = spAdapter.getExtGStateChild();
		if (extGStateChild != null) {
			FeatureTreeNode exGState = root.addChild("graphicsState");
			exGState.setAttribute(ID, extGStateChild);
		}
		return root;
	}

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
