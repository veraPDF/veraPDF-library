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

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Feature object for form xobject
 *
 * @author Maksim Bezrukov
 */
public class FormXObjectFeaturesObject extends FeaturesObject {

	private static final Logger LOGGER = Logger.getLogger(FormXObjectFeaturesObject.class.getCanonicalName());

	private static final String ID = "id";
	private static final String XOBJECT = "xobject";
	private static final String FORM = "form";
	private static final String XOBJECT_XPATH = XOBJECT + "[@type='" + FORM + "']";
	private static final String TRANSPARENCY = "Transparency";

	/**
	 * Constructs new From XObject Feature Object
	 *
	 * @param adapter class represents form xobject adapter
	 */
	public FormXObjectFeaturesObject(FormXObjectFeaturesObjectAdapter adapter) {
		super(adapter);
	}

	/**
	 * @return FORM_XOBJECT instance of the FeatureObjectType enumeration
	 */
	@Override
	public FeatureObjectType getType() {
		return FeatureObjectType.FORM_XOBJECT;
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
		FormXObjectFeaturesObjectAdapter formAdapter = (FormXObjectFeaturesObjectAdapter) this.adapter;
		FeatureTreeNode root = FeatureTreeNode.createRootNode(XOBJECT);
		root.setAttribute("type", FORM);
		String id = formAdapter.getId();
		if (id != null) {
			root.setAttribute(ID, id);
		}

		CreateNodeHelper.addBoxFeature("bbox", formAdapter.getBBox(), root);
		CreateNodeHelper.parseMatrix(formAdapter.getMatrix(), root.addChild("matrix"));

		if (formAdapter.isGroupPresent()) {
			FeatureTreeNode groupNode = root.addChild("group");
			String groupSubtype = formAdapter.getGroupSubtype();
			CreateNodeHelper.addNotEmptyNode("subtype", groupSubtype, groupNode);
			if (groupSubtype != null) {
				if (TRANSPARENCY.equals(groupSubtype)) {
					String groupColorSpaceChild = formAdapter.getGroupColorSpaceChild();
					if (groupColorSpaceChild != null) {
						FeatureTreeNode clr = groupNode.addChild("colorSpace");
						clr.setAttribute(ID, groupColorSpaceChild);
					}
					groupNode.addChild("isolated").setValue(String.valueOf(formAdapter.isTransparencyGroupIsolated()));
					groupNode.addChild("knockout").setValue(String.valueOf(formAdapter.isTransparencyGroupKnockout()));
				}

			}
		}

		Long structParents = formAdapter.getStructParents();
		if (structParents != null) {
			root.addChild("structParents").setValue(String.valueOf(structParents));
		}

		try (InputStream is = formAdapter.getMetadataStream()) {
			CreateNodeHelper.parseMetadata(is, "metadata", root, this);
		} catch (IOException e) {
			LOGGER.log(Level.FINE, "Error while obtaining unfiltered metadata stream", e);
			registerNewError(e.getMessage());
		}

		parseResources(root);
		return root;
	}

	private void parseResources(FeatureTreeNode root) throws FeatureParsingException {
		FormXObjectFeaturesObjectAdapter formAdapter = (FormXObjectFeaturesObjectAdapter) this.adapter;
		Set<String> extGStateChild = formAdapter.getExtGStateChild();
		Set<String> colorSpaceChild = formAdapter.getColorSpaceChild();
		Set<String> patternChild = formAdapter.getPatternChild();
		Set<String> shadingChild = formAdapter.getShadingChild();
		Set<String> xobjectChild = formAdapter.getXObjectChild();
		Set<String> fontChild = formAdapter.getFontChild();
		Set<String> propertiesChild = formAdapter.getPropertiesChild();
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
		// Missed all fields
		List<Feature> featuresList = new ArrayList<>();
		featuresList.add(new Feature("Error IDs",
				generateAttributeXPath(XOBJECT_XPATH, ErrorsHelper.ERRORID), Feature.FeatureType.STRING));
		return featuresList;
	}
}
