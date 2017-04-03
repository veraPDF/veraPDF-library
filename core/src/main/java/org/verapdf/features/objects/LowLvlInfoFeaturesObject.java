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
import org.verapdf.features.tools.FeatureTreeNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Feature object for low level info part of the features report
 *
 * @author Maksim Bezrukov
 */
public class LowLvlInfoFeaturesObject extends FeaturesObject {

	/**
	 * Constructs new low level info feature object.
	 *
	 * @param adapter low lvl info adapter class represents document object
	 */
	public LowLvlInfoFeaturesObject(LowLvlInfoFeaturesObjectAdapter adapter) {
		super(adapter);
	}

	/**
	 * @return LOW_LVL_INFO instance of the FeatureObjectType enumeration
	 */
	@Override
	public FeatureObjectType getType() {
		return FeatureObjectType.LOW_LEVEL_INFO;
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
		LowLvlInfoFeaturesObjectAdapter lowLvlAdapter = (LowLvlInfoFeaturesObjectAdapter) this.adapter;
		FeatureTreeNode root = FeatureTreeNode.createRootNode("lowLevelInfo");

		root.addChild("indirectObjectsNumber")
				.setValue(String.valueOf(lowLvlAdapter.getIndirectObjectsNumber()));

		String creationId = lowLvlAdapter.getCreationId();
		String modificationId = lowLvlAdapter.getModificationId();

		if (creationId != null || modificationId != null) {
			FeatureTreeNode documentId = root.addChild("documentId");
			if (creationId != null) {
				documentId.setAttribute("creationId", creationId);
			}
			if (modificationId != null) {
				documentId.setAttribute("modificationId", modificationId);
			}
		}

		Set<String> filters = lowLvlAdapter.getFilters();

		if (!filters.isEmpty()) {
			FeatureTreeNode filtersNode = root.addChild("filters");

			for (String filter : filters) {
				if (filter != null) {
					FeatureTreeNode filterNode = filtersNode.addChild("filter");
					filterNode.setAttribute("name", filter);
				}
			}
		}
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
		// All fields are present
		List<Feature> featuresList = new ArrayList<>();
		featuresList.add(new Feature("Indirect Objects Number", "/lowLevelInfo/indirectObjectsNumber",
				Feature.FeatureType.NUMBER));
		featuresList.add(new Feature("Creation ID", "/lowLevelInfo/documentId/@creationId",
				Feature.FeatureType.STRING));
		featuresList.add(new Feature("Modification ID", "/lowLevelInfo/documentId/@modificationId",
				Feature.FeatureType.STRING));
		featuresList.add(new Feature("Filter Name", "/lowLevelInfo/filters/filter/@name",
				Feature.FeatureType.STRING));
		featuresList.add(new Feature("Error IDs", "/lowLevelInfo/@errorId", Feature.FeatureType.STRING));
		return featuresList;
	}
}
