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

/**
 * Feature object for page part of the features report
 *
 * @author Maksim Bezrukov
 */
public class PageFeaturesObject extends FeaturesObject {

	private static final String ID = "id";
	private static final String PAGE = "page";
	private static final String ROTATION = "rotation";
	private static final String SCALING = "scaling";

	/**
	 * Constructs new page feature object.
	 *
	 * @param adapter page adapter class represents document object
	 */
	public PageFeaturesObject(PageFeaturesObjectAdapter adapter) {
		super(adapter);
	}

	/**
	 * @return PAGE instance of the FeatureObjectType enumeration
	 */
	@Override
	public FeatureObjectType getType() {
		return FeatureObjectType.PAGE;
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
		PageFeaturesObjectAdapter pageAdapter = (PageFeaturesObjectAdapter) this.adapter;
		FeatureTreeNode root = FeatureTreeNode.createRootNode(PAGE);
		root.setAttribute("orderNumber", Integer.toString(pageAdapter.getIndex()));
		double[] mediaBox = pageAdapter.getMediaBox();
		CreateNodeHelper.addWidthHeightFeatures(mediaBox, root);
		CreateNodeHelper.addBoxFeature("mediaBox", mediaBox, root);
		CreateNodeHelper.addBoxFeature("cropBox", pageAdapter.getCropBox(), root);
		CreateNodeHelper.addBoxFeature("trimBox", pageAdapter.getTrimBox(), root);
		CreateNodeHelper.addBoxFeature("bleedBox", pageAdapter.getBleedBox(), root);
		CreateNodeHelper.addBoxFeature("artBox", pageAdapter.getArtBox(), root);
		Long rotation = pageAdapter.getRotation();
		if (rotation != null) {
			root.addChild(ROTATION).setValue(String.valueOf(rotation));
		}
		Double scaling = pageAdapter.getScaling();
		if (scaling != null) {
			root.addChild(SCALING).setValue(String.format("%.3f", scaling));
		}
		String thumb = pageAdapter.getThumb();
		if (thumb != null) {
			FeatureTreeNode thumbNode = root.addChild("thumbnail");
			thumbNode.setAttribute(ID, thumb);
		}
		CreateNodeHelper.parseMetadata(pageAdapter.getMetadataStream(), "metadata", root, this);
		CreateNodeHelper.parseIDSet(pageAdapter.getAnnotsId(), "annotation", "annotations", root);
		parseResources(root);
		return root;
	}

	private void parseResources(FeatureTreeNode root) throws FeatureParsingException {
		PageFeaturesObjectAdapter pageAdapter = (PageFeaturesObjectAdapter) this.adapter;
		Set<String> extGStateChild = pageAdapter.getExtGStateChild();
		Set<String> colorSpaceChild = pageAdapter.getColorSpaceChild();
		Set<String> patternChild = pageAdapter.getPatternChild();
		Set<String> shadingChild = pageAdapter.getShadingChild();
		Set<String> xobjectChild = pageAdapter.getXObjectChild();
		Set<String> fontChild = pageAdapter.getFontChild();
		Set<String> propertiesChild = pageAdapter.getPropertiesChild();
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
		// Only rotation and scaling are present
		List<Feature> featuresList = new ArrayList<>();
		featuresList.add(new Feature("Width",
				generateVariableXPath(PAGE, CreateNodeHelper.WIDTH), Feature.FeatureType.NUMBER));
		featuresList.add(new Feature("Height",
				generateVariableXPath(PAGE, CreateNodeHelper.HEIGHT), Feature.FeatureType.NUMBER));
		featuresList.add(new Feature("Rotation",
				generateVariableXPath(PAGE, ROTATION), Feature.FeatureType.NUMBER));
		featuresList.add(new Feature("Scaling",
				generateVariableXPath(PAGE, SCALING), Feature.FeatureType.NUMBER));
		featuresList.add(new Feature("Error IDs",
				generateAttributeXPath(PAGE, ErrorsHelper.ERRORID), Feature.FeatureType.STRING));
		return featuresList;
	}
}
