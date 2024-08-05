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
import org.verapdf.features.SignatureFeaturesData;
import org.verapdf.features.tools.CreateNodeHelper;
import org.verapdf.features.tools.ErrorsHelper;
import org.verapdf.features.tools.FeatureTreeNode;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Feature object for signature part of the features report
 *
 * @author Maksim Bezrukov
 */
public class SignatureFeaturesObject extends FeaturesObject {

	private static final String SIGNATURE = "signature";
	private static final String FILTER = "filter";
	private static final String SUB_FILTER = "subFilter";
	private static final String CONTENTS = "contents";
	private static final String NAME = "name";
	private static final String SIGN_DATE = "signDate";
	private static final String LOCATION = "location";
	private static final String REASON = "reason";
	private static final String CONTACT_INFO = "contactInfo";

	/**
	 * Constructs new signature feature object.
	 *
	 * @param adapter signature adapter class represents document object
	 */
	public SignatureFeaturesObject(SignatureFeaturesObjectAdapter adapter) {
		super(adapter);
	}

	/**
	 * @return SIGNATURE instance of the FeatureObjectType enumeration
	 */
	@Override
	public FeatureObjectType getType() {
		return FeatureObjectType.SIGNATURE;
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
		SignatureFeaturesObjectAdapter signAdapter = (SignatureFeaturesObjectAdapter) this.adapter;
		FeatureTreeNode root = FeatureTreeNode.createRootNode(SIGNATURE);
		CreateNodeHelper.addNotEmptyNode(FILTER, signAdapter.getFilter(), root);
		CreateNodeHelper.addNotEmptyNode(SUB_FILTER, signAdapter.getSubFilter(), root);
		CreateNodeHelper.addNotEmptyNode(CONTENTS, signAdapter.getHexContents(), root);
		CreateNodeHelper.addNotEmptyNode(NAME, signAdapter.getName(), root);
		CreateNodeHelper.createDateNode(SIGN_DATE, root, signAdapter.getSignDate(), this);
		CreateNodeHelper.addNotEmptyNode(LOCATION, signAdapter.getLocation(), root);
		CreateNodeHelper.addNotEmptyNode(REASON, signAdapter.getReason(), root);
		CreateNodeHelper.addNotEmptyNode(CONTACT_INFO, signAdapter.getContactInfo(), root);
		return root;
	}

	@Override
	public FeaturesData getData() {
		SignatureFeaturesObjectAdapter signAdapter = (SignatureFeaturesObjectAdapter) this.adapter;
		InputStream stream = signAdapter.getData();
		return SignatureFeaturesData.newInstance(
				stream, signAdapter.getFilter(),
				signAdapter.getSubFilter(), signAdapter.getName(),
				signAdapter.getSignDate(), signAdapter.getLocation(),
				signAdapter.getReason(), signAdapter.getContactInfo());
	}

	static List<Feature> getFeaturesList() {
		// All fields are present
		List<Feature> featuresList = new ArrayList<>();
		featuresList.add(new Feature("Filter",
				generateVariableXPath(SIGNATURE, FILTER), Feature.FeatureType.STRING));
		featuresList.add(new Feature("SubFilter",
				generateVariableXPath(SIGNATURE, SUB_FILTER), Feature.FeatureType.STRING));
		featuresList.add(new Feature("Contents",
				generateVariableXPath(SIGNATURE, CONTENTS), Feature.FeatureType.STRING));
		featuresList.add(new Feature("Name",
				generateVariableXPath(SIGNATURE, NAME), Feature.FeatureType.STRING));
		featuresList.add(new Feature("Sign Date",
				generateVariableXPath(SIGNATURE, SIGN_DATE), Feature.FeatureType.STRING));
		featuresList.add(new Feature("Location",
				generateVariableXPath(SIGNATURE, LOCATION), Feature.FeatureType.STRING));
		featuresList.add(new Feature("Reason",
				generateVariableXPath(SIGNATURE, REASON), Feature.FeatureType.STRING));
		featuresList.add(new Feature("Contact Info",
				generateVariableXPath(SIGNATURE, CONTACT_INFO), Feature.FeatureType.STRING));
		featuresList.add(new Feature("Error IDs",
				generateAttributeXPath(SIGNATURE, ErrorsHelper.ERRORID), Feature.FeatureType.STRING));
		return featuresList;
	}
}
