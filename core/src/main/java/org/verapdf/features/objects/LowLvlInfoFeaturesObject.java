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
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Feature object for low level info part of the features report
 *
 * @author Maksim Bezrukov
 */
public class LowLvlInfoFeaturesObject extends FeaturesObject {

	private static final Logger LOGGER = Logger.getLogger(LowLvlInfoFeaturesObject.class.getCanonicalName());

	private static final String LOW_LEVEL_INFO = "lowLevelInfo";
	private static final String PDF_VERSION = "pdfVersion";
	private static final String INDIRECT_OBJECTS_NUMBER = "indirectObjectsNumber";
	private static final String DOCUMENT_ID = "documentId";
	private static final String CREATION_ID = "creationId";
	private static final String MOD_ID = "modificationId";
	private static final String TAGGED = "tagged";
	private static final String FILTERS = "filters";
	private static final String FILTER = "filter";
	private static final String NAME = "name";

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
		FeatureTreeNode root = FeatureTreeNode.createRootNode(LOW_LEVEL_INFO);

		CreateNodeHelper.addNotEmptyNode(PDF_VERSION, getPDFVersionString(lowLvlAdapter), root);

		root.addChild(INDIRECT_OBJECTS_NUMBER)
				.setValue(String.valueOf(lowLvlAdapter.getIndirectObjectsNumber()));

		String creationId = lowLvlAdapter.getCreationId();
		String modificationId = lowLvlAdapter.getModificationId();

		if (creationId != null || modificationId != null) {
			FeatureTreeNode documentId = root.addChild(DOCUMENT_ID);
			if (creationId != null) {
				documentId.setAttribute(CREATION_ID, creationId);
			}
			if (modificationId != null) {
				documentId.setAttribute(MOD_ID, modificationId);
			}
		}

		CreateNodeHelper.addNotEmptyNode(TAGGED, String.valueOf(lowLvlAdapter.isTagged()), root);

		Set<String> filters = lowLvlAdapter.getFilters();

		if (!filters.isEmpty()) {
			FeatureTreeNode filtersNode = root.addChild(FILTERS);

			for (String filter : filters) {
				if (filter != null) {
					FeatureTreeNode filterNode = filtersNode.addChild(FILTER);
					filterNode.setAttribute(NAME, filter);
				}
			}
		}
		return root;
	}

	private String getPDFVersionString(LowLvlInfoFeaturesObjectAdapter lowLvlAdapter) {
		double res = lowLvlAdapter.getHeaderVersion();
		String catalogVersion = lowLvlAdapter.getCatalogVersion();
		if (catalogVersion != null) {
			try {
				double catalogValue = Double.parseDouble(catalogVersion);
				res = Math.max(res, catalogValue);
			} catch (NumberFormatException e) {
				LOGGER.log(Level.FINE, "Problems in obtaining pdf version number from the catalog", e);
			}
		}
		return CreateNodeHelper.formatDouble(res, 1);
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
		featuresList.add(new Feature("PDF Version",
				generateVariableXPath(LOW_LEVEL_INFO, PDF_VERSION),
				Feature.FeatureType.NUMBER));
		featuresList.add(new Feature("Indirect Objects Number",
				generateVariableXPath(LOW_LEVEL_INFO, INDIRECT_OBJECTS_NUMBER),
				Feature.FeatureType.NUMBER));
		featuresList.add(new Feature("Creation ID",
				generateAttributeXPath(LOW_LEVEL_INFO, DOCUMENT_ID, CREATION_ID),
				Feature.FeatureType.STRING));
		featuresList.add(new Feature("Modification ID",
				generateAttributeXPath(LOW_LEVEL_INFO, DOCUMENT_ID, MOD_ID),
				Feature.FeatureType.STRING));
		featuresList.add(new Feature("Tagged",
				generateVariableXPath(LOW_LEVEL_INFO, TAGGED),
				Feature.FeatureType.BOOLEAN));
		featuresList.add(new Feature("Filter Name",
				generateAttributeXPath(LOW_LEVEL_INFO, FILTERS, FILTER, NAME),
				Feature.FeatureType.STRING));
		featuresList.add(new Feature("Error IDs",
				generateAttributeXPath(LOW_LEVEL_INFO, ErrorsHelper.ERRORID), Feature.FeatureType.STRING));
		return featuresList;
	}
}
