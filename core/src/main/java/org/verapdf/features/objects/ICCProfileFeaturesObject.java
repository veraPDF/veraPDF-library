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
import org.verapdf.features.ICCProfileFeaturesData;
import org.verapdf.features.tools.CreateNodeHelper;
import org.verapdf.features.tools.ErrorsHelper;
import org.verapdf.features.tools.FeatureTreeNode;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Feature object for iccprofile
 *
 * @author Maksim Bezrukov
 */
public class ICCProfileFeaturesObject extends FeaturesObject {

	private static final Logger LOGGER = Logger.getLogger(ICCProfileFeaturesObject.class.getCanonicalName());

	private static final String ID = "id";
	private static final String ICCPROFILE = "iccProfile";
	private static final String VERSION = "version";
	private static final String CMM_TYPE = "cmmType";
	private static final String DATA_COLOR_SPACE = "dataColorSpace";
	private static final String CREATOR = "creator";
	private static final String CREATION_DATE = "creationDate";
	private static final String DEFAULT_RENDERING_INTENT = "defaultRenderingIntent";
	private static final String COPYRIGHT = "copyright";
	private static final String DESCRIPTION = "description";
	private static final String PROFILE_ID = "profileId";
	private static final String DEVICE_MODEL = "deviceModel";
	private static final String DEVICE_MANUFACTURER = "deviceManufacturer";

	/**
	 * Constructs new ICCProfile Feature Object
	 *
	 * @param adapter class represents icc profile adapter
	 */
	public ICCProfileFeaturesObject(ICCProfileFeaturesObjectAdapter adapter) {
		super(adapter);
	}

	/**
	 * @return ICCPROFILE instance of the FeatureObjectType enumeration
	 */
	@Override
	public FeatureObjectType getType() {
		return FeatureObjectType.ICCPROFILE;
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
		ICCProfileFeaturesObjectAdapter ipAdapter = (ICCProfileFeaturesObjectAdapter) this.adapter;
		FeatureTreeNode root = FeatureTreeNode.createRootNode(ICCPROFILE);
		String id = ipAdapter.getId();
		if (id != null) {
			root.setAttribute(ID, id);
		}

		parseProfileHeader(root);
		try (InputStream is = ipAdapter.getMetadataStream()) {
			CreateNodeHelper.parseMetadata(is, "metadata", root, this);
		} catch (IOException e) {
			LOGGER.log(Level.FINE, "Error while obtaining unfiltered metadata stream", e);
			registerNewError(e.getMessage());
		}
		return root;
	}

	private void parseProfileHeader(FeatureTreeNode root) throws FeatureParsingException {
		ICCProfileFeaturesObjectAdapter ipAdapter = (ICCProfileFeaturesObjectAdapter) this.adapter;
		CreateNodeHelper.addNotEmptyNode(VERSION, ipAdapter.getVersion(), root);
		CreateNodeHelper.addNotEmptyNode(CMM_TYPE, ipAdapter.getCMMType(), root);
		CreateNodeHelper.addNotEmptyNode(DATA_COLOR_SPACE, ipAdapter.getDataColorSpace(), root);
		CreateNodeHelper.addNotEmptyNode(CREATOR, ipAdapter.getCreator(), root);
		CreateNodeHelper.createDateNode(CREATION_DATE, root, ipAdapter.getCreationDate(), this);
		CreateNodeHelper.addNotEmptyNode(DEFAULT_RENDERING_INTENT, ipAdapter.getDefaultRenderingIntent(), root);
		CreateNodeHelper.addNotEmptyNode(COPYRIGHT, ipAdapter.getCopyright(), root);
		CreateNodeHelper.addNotEmptyNode(DESCRIPTION, ipAdapter.getDescription(), root);
		CreateNodeHelper.addNotEmptyNode(PROFILE_ID, ipAdapter.getProfileID(), root);
		CreateNodeHelper.addNotEmptyNode(DEVICE_MODEL, ipAdapter.getDeviceModel(), root);
		CreateNodeHelper.addNotEmptyNode(DEVICE_MANUFACTURER, ipAdapter.getDeviceManufacturer(), root);
	}

	@Override
	public FeaturesData getData() {
		ICCProfileFeaturesObjectAdapter ipAdapter = (ICCProfileFeaturesObjectAdapter) this.adapter;
		InputStream stream = ipAdapter.getData();
		if (stream == null) {
			LOGGER.log(Level.FINE, "Missed icc profile InputStream");
			return null;
		}
		InputStream metadata = ipAdapter.getMetadataStream();
		Integer n = ipAdapter.getN();
		List<Double> range = ipAdapter.getRange();
		return ICCProfileFeaturesData.newInstance(metadata, stream, n, range);
	}

	static List<Feature> getFeaturesList() {
		// Missed fields:
		// * metadata
		List<Feature> featuresList = new ArrayList<>();
		featuresList.add(new Feature("Version",
				generateVariableXPath(ICCPROFILE, VERSION), Feature.FeatureType.STRING));
		featuresList.add(new Feature("CMM Type",
				generateVariableXPath(ICCPROFILE, CMM_TYPE), Feature.FeatureType.STRING));
		featuresList.add(new Feature("Data Color Space",
				generateVariableXPath(ICCPROFILE, DATA_COLOR_SPACE), Feature.FeatureType.STRING));
		featuresList.add(new Feature("Creator",
				generateVariableXPath(ICCPROFILE, CREATOR), Feature.FeatureType.STRING));
		featuresList.add(new Feature("Creation Date",
				generateVariableXPath(ICCPROFILE, CREATION_DATE), Feature.FeatureType.STRING));
		featuresList.add(new Feature("Default Rendering Intent",
				generateVariableXPath(ICCPROFILE, DEFAULT_RENDERING_INTENT), Feature.FeatureType.STRING));
		featuresList.add(new Feature("Copyright",
				generateVariableXPath(ICCPROFILE, COPYRIGHT), Feature.FeatureType.STRING));
		featuresList.add(new Feature("Description",
				generateVariableXPath(ICCPROFILE, DESCRIPTION), Feature.FeatureType.STRING));
		featuresList.add(new Feature("Profile ID",
				generateVariableXPath(ICCPROFILE, PROFILE_ID), Feature.FeatureType.STRING));
		featuresList.add(new Feature("Device Model",
				generateVariableXPath(ICCPROFILE, DEVICE_MODEL), Feature.FeatureType.STRING));
		featuresList.add(new Feature("Device Manufacturer",
				generateVariableXPath(ICCPROFILE, DEVICE_MANUFACTURER), Feature.FeatureType.STRING));

		featuresList.add(new Feature("Error IDs",
				generateAttributeXPath(ICCPROFILE, ErrorsHelper.ERRORID), Feature.FeatureType.STRING));
		return featuresList;
	}
}
