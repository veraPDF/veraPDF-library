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
import org.verapdf.features.FeatureExtractionResult;
import org.verapdf.features.FeatureObjectType;
import org.verapdf.features.FeaturesData;
import org.verapdf.features.tools.ErrorsHelper;
import org.verapdf.features.tools.FeatureTreeNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Maksim Bezrukov
 */
public class ActionFeaturesObject extends FeaturesObject {

	private static final String ACTION = "action";
	private static final String TYPE = "type";
	private static final String LOCATION = "location";

	public ActionFeaturesObject(ActionFeaturesObjectAdapter adapter) {
		super(adapter);
	}

	@Override
	public FeatureObjectType getType() {
		return FeatureObjectType.ACTION;
	}

	@Override
	public FeatureTreeNode reportFeatures(FeatureExtractionResult collection) throws FeatureParsingException {
		this.errors.clear();
		if (this.adapter.isPDFObjectPresent()) {
			ActionFeaturesObjectAdapter actionAdapter = (ActionFeaturesObjectAdapter) this.adapter;
			String type = actionAdapter.getType();
			if (type == null) {
				return null;
			}
			FeatureTreeNode root = findRootForType(collection, type);
			if (root == null) {
				root = createNewRoot(collection, type);
			}
			addLocation(root, actionAdapter.getLocation());
			this.errors.addAll(this.adapter.getErrors());
			if (!this.errors.isEmpty()) {
				for (String error : this.errors) {
					ErrorsHelper.addErrorIntoCollection(collection, root, error);
				}
			}
			return root;
		}
		return null;
	}

	private void addLocation(FeatureTreeNode root, ActionFeaturesObjectAdapter.Location location) throws FeatureParsingException {
		if (location != null) {
			List<FeatureTreeNode> children = root.getChildren();
			for (FeatureTreeNode child : children) {
				if (LOCATION.equals(child.getName()) && location.getText().equals(child.getValue())) {
					return;
				}
			}
			FeatureTreeNode locationNode = root.addChild(LOCATION);
			locationNode.setValue(location.getText());
		}
	}

	private FeatureTreeNode createNewRoot(FeatureExtractionResult collection, String type) {
		FeatureTreeNode root = FeatureTreeNode.createRootNode(ACTION);
		root.setAttribute(TYPE, type);
		collection.addNewFeatureTree(getType(), root);
		return root;
	}

	private FeatureTreeNode findRootForType(FeatureExtractionResult collection, String type) {
		List<FeatureTreeNode> featureTreesForType = collection.getFeatureTreesForType(FeatureObjectType.ACTION);
		for (FeatureTreeNode node : featureTreesForType) {
			Map<String, String> attributes = node.getAttributes();
			String attrType = attributes.get(TYPE);
			if (type.equals(attrType)) {
				return node;
			}
		}
		return null;
	}

	@Override
	public FeaturesData getData() {
		return null;
	}

	static List<Feature> getFeaturesList() {
		// All fields are present
		List<Feature> featuresList = new ArrayList<>();
		featuresList.add(new Feature("Type",
				generateAttributeXPath(ACTION, TYPE), Feature.FeatureType.STRING));
		featuresList.add(new Feature("Location",
				generateAttributeXPath(ACTION, LOCATION), Feature.FeatureType.STRING));

		featuresList.add(new Feature("Error IDs",
				generateAttributeXPath(ACTION, ErrorsHelper.ERRORID), Feature.FeatureType.STRING));
		return featuresList;
	}
}
