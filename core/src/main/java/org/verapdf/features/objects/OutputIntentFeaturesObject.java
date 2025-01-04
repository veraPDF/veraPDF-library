/**
 * This file is part of veraPDF Library core, a module of the veraPDF project.
 * Copyright (c) 2015-2025, veraPDF Consortium <info@verapdf.org>
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
 * Feature object for output intent part of the features report
 *
 * @author Maksim Bezrukov
 */
public class OutputIntentFeaturesObject extends FeaturesObject {

	private static final String OUTPUTINTENT = "outputIntent";
	private static final String SUBTYPE = "subtype";
	private static final String OUTPUT_CONDITION = "outputCondition";
	private static final String OUTPUT_CONDITION_IDENTIFIER = "outputConditionIdentifier";
	private static final String REGISTRY_NAME = "registryName";
	private static final String INFO = "info";

	/**
	 * Constructs new output intent feature object.
	 *
	 * @param adapter output intent adapter class represents document object
	 */
	public OutputIntentFeaturesObject(OutputIntentFeaturesObjectAdapter adapter) {
		super(adapter);
	}

	/**
	 * @return OUTPUTINTENT instance of the FeatureObjectType enumeration
	 */
	@Override
	public FeatureObjectType getType() {
		return FeatureObjectType.OUTPUTINTENT;
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
		OutputIntentFeaturesObjectAdapter outIntAdapter = (OutputIntentFeaturesObjectAdapter) this.adapter;
		FeatureTreeNode root = FeatureTreeNode.createRootNode(OUTPUTINTENT);

		CreateNodeHelper.addNotEmptyNode(SUBTYPE, outIntAdapter.getSubType(), root);
		CreateNodeHelper.addNotEmptyNode(OUTPUT_CONDITION, outIntAdapter.getOutputCondition(), root);
		CreateNodeHelper.addNotEmptyNode(OUTPUT_CONDITION_IDENTIFIER, outIntAdapter.getOutputConditionIdentifier(), root);
		CreateNodeHelper.addNotEmptyNode(REGISTRY_NAME, outIntAdapter.getRegistryName(), root);
		CreateNodeHelper.addNotEmptyNode(INFO, outIntAdapter.getInfo(), root);

		String iccProfileID = outIntAdapter.getICCProfileID();
		if (iccProfileID != null) {
			FeatureTreeNode destOutInt = root.addChild("destOutputIntent");
			destOutInt.setAttribute("id", iccProfileID);
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
		featuresList.add(new Feature("Subtype",
				generateVariableXPath(OUTPUTINTENT, SUBTYPE), Feature.FeatureType.STRING));
		featuresList.add(new Feature("Output Condition",
				generateVariableXPath(OUTPUTINTENT, OUTPUT_CONDITION), Feature.FeatureType.STRING));
		featuresList.add(new Feature("Output Condition Identified",
				generateVariableXPath(OUTPUTINTENT, OUTPUT_CONDITION_IDENTIFIER), Feature.FeatureType.STRING));
		featuresList.add(new Feature("Registry Name",
				generateVariableXPath(OUTPUTINTENT, REGISTRY_NAME), Feature.FeatureType.STRING));
		featuresList.add(new Feature("Info",
				generateVariableXPath(OUTPUTINTENT, INFO), Feature.FeatureType.STRING));
		featuresList.add(new Feature("Error IDs",
				generateAttributeXPath(OUTPUTINTENT, ErrorsHelper.ERRORID), Feature.FeatureType.STRING));
		return featuresList;
	}
}
