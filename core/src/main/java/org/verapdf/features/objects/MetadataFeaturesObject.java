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
import org.verapdf.features.MetadataFeaturesData;
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
 * Feature object for metadata part of the features report
 *
 * @author Maksim Bezrukov
 */
public class MetadataFeaturesObject extends FeaturesObject {

	private static final Logger LOGGER = Logger.getLogger(MetadataFeaturesObject.class.getCanonicalName());

	private static final String METADATA = "metadata";

	/**
	 * Constructs new metadata feature object.
	 *
	 * @param adapter metadata adapter class represents document object
	 */
	public MetadataFeaturesObject(MetadataFeaturesObjectAdapter adapter) {
		super(adapter);
	}

	/**
	 * @return METADATA instance of the FeatureObjectType enumeration
	 */
	@Override
	public FeatureObjectType getType() {
		return FeatureObjectType.METADATA;
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
		MetadataFeaturesObjectAdapter metadataAdapter = (MetadataFeaturesObjectAdapter) this.adapter;
		FeatureTreeNode root = FeatureTreeNode.createRootNode(METADATA);
		try (InputStream stream = metadataAdapter.getData()) {
			CreateNodeHelper.parseMetadata(stream, "xmpPackage", root, this);
		} catch (IOException e) {
			LOGGER.log(Level.FINE, "Error while obtaining unfiltered metadata stream", e);
			registerNewError(e.getMessage());
		}
		return root;
	}

	/**
	 * @return null if it can not get metadata stream and features data of the metadata in other case.
	 */
	@Override
	public FeaturesData getData() {
		MetadataFeaturesObjectAdapter metadataAdapter = (MetadataFeaturesObjectAdapter) this.adapter;
		InputStream meta = metadataAdapter.getData();
		if (meta == null) {
			LOGGER.log(Level.FINE, "Missed metadata InputStream");
			return null;
		}
		return MetadataFeaturesData.newInstance(meta);
	}

	static List<Feature> getFeaturesList() {
		// Only errors are present
		List<Feature> featuresList = new ArrayList<>();
		featuresList.add(new Feature("Error IDs",
				generateAttributeXPath(METADATA, ErrorsHelper.ERRORID), Feature.FeatureType.STRING));
		return featuresList;
	}
}
