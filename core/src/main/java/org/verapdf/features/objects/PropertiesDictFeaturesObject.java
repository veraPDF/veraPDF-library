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
import org.verapdf.features.tools.ErrorsHelper;
import org.verapdf.features.tools.FeatureTreeNode;

import java.util.ArrayList;
import java.util.List;

/**
 * Feature object for properties dict part of the features report
 *
 * @author Maksim Bezrukov
 */
public class PropertiesDictFeaturesObject extends FeaturesObject {

	private static final String PROPDICT = "propertiesDict";
	private static final String TYPE = "type";

	/**
	 * Constructs new properties dict feature object.
	 *
	 * @param adapter properties dict adapter class represents document object
	 */
	public PropertiesDictFeaturesObject(PropertiesDictFeaturesObjectAdapter adapter) {
		super(adapter);
	}

	/**
	 * @return PROPERTIES instance of the FeatureObjectType enumeration
	 */
	@Override
	public FeatureObjectType getType() {
		return FeatureObjectType.PROPERTIES;
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
		PropertiesDictFeaturesObjectAdapter pdAdapter = (PropertiesDictFeaturesObjectAdapter) this.adapter;
		FeatureTreeNode root = FeatureTreeNode.createRootNode(PROPDICT);
		String id = pdAdapter.getID();
		if (id != null) {
			root.setAttribute("id", id);
		}
		CreateNodeHelper.addNotEmptyNode(TYPE, pdAdapter.getType(), root);
		return root;
	}

	@Override
	public FeaturesData getData() {
		return null;
	}

	static List<Feature> getFeaturesList() {
		// All fields are present
		List<Feature> featuresList = new ArrayList<>();
		featuresList.add(new Feature("Type",
				generateVariableXPath(PROPDICT, TYPE), Feature.FeatureType.STRING));
		featuresList.add(new Feature("Error IDs",
				generateAttributeXPath(PROPDICT, ErrorsHelper.ERRORID), Feature.FeatureType.STRING));
		return featuresList;
	}
}
