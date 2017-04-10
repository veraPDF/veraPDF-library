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

import static org.verapdf.features.tools.CreateNodeHelper.ID;

/**
 * Feature object for colorspace
 *
 * @author Maksim Bezrukov
 */
public class ColorSpaceFeaturesObject extends FeaturesObject {

	private static final String CALGRAY = "CalGray";
	private static final String CALRGB = "CalRGB";
	private static final String LAB = "Lab";
	private static final String ICCBASED = "ICCBased";
	private static final String INDEXED = "Indexed";
	private static final String SEPARATION = "Separation";
	private static final String DEVICEN = "DeviceN";
	private static final String COLOR_SPACE = "colorSpace";
	private static final String FAMILY = "family";

	/**
	 * Constructs new ColorSpace Feature Object
	 *
	 * @param adapter class represents colorspace adapter
	 */
	public ColorSpaceFeaturesObject(ColorSpaceFeaturesObjectAdapter adapter) {
		super(adapter);
	}

	/**
	 * @return COLORSPACE instance of the FeatureObjectType enumeration
	 */
	@Override
	public FeatureObjectType getType() {
		return FeatureObjectType.COLORSPACE;
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
		ColorSpaceFeaturesObjectAdapter csAdapter = (ColorSpaceFeaturesObjectAdapter) this.adapter;
		FeatureTreeNode root = FeatureTreeNode.createRootNode(COLOR_SPACE);

		String id = csAdapter.getId();
		if (id != null) {
			root.setAttribute(ID, id);
		}
		String colorSpaceType = csAdapter.getFamily();
		root.setAttribute(FAMILY, colorSpaceType);

		if (CALGRAY.equals(colorSpaceType)
				|| CALRGB.equals(colorSpaceType)
				|| LAB.equals(colorSpaceType)) {
			parseCIEDictionaryBased(root, csAdapter);
		} else if (ICCBASED.equals(colorSpaceType)) {
			String colorSpaceChild = csAdapter.getColorSpaceChild();
			if (colorSpaceChild != null) {
				FeatureTreeNode alt = root.addChild("alternate");
				alt.setAttribute(ID, colorSpaceChild);
			}
			root.addChild("components").setValue(String.valueOf(csAdapter.getNumberOfComponents()));
			String iccProfileChild = csAdapter.getICCProfileChild();
			if (iccProfileChild != null) {
				FeatureTreeNode prof = root.addChild("iccProfile");
				prof.setAttribute(ID, iccProfileChild);
			}
		} else if (INDEXED.equals(colorSpaceType)) {
			String colorSpaceChild = csAdapter.getColorSpaceChild();
			if (colorSpaceChild != null) {
				FeatureTreeNode alt = root.addChild("base");
				alt.setAttribute(ID, colorSpaceChild);
			}

			Long hival = csAdapter.getHival();
			if (hival != null) {
				CreateNodeHelper.addNotEmptyNode("hival", String.valueOf(hival.longValue()), root);
			}
			CreateNodeHelper.addNotEmptyNode("lookup", csAdapter.getHexEncodedLookup(), root);
		} else if (SEPARATION.equals(colorSpaceType)) {
			String colorSpaceChild = csAdapter.getColorSpaceChild();
			if (colorSpaceChild != null) {
				FeatureTreeNode alt = root.addChild("alternate");
				alt.setAttribute(ID, colorSpaceChild);
			}
			CreateNodeHelper.addNotEmptyNode("colorantName", csAdapter.getColorantName(), root);
		} else if (DEVICEN.equals(colorSpaceType)) {
			String colorSpaceChild = csAdapter.getColorSpaceChild();
			if (colorSpaceChild != null) {
				FeatureTreeNode alt = root.addChild("alternate");
				alt.setAttribute(ID, colorSpaceChild);
			}
			List<String> devNColorantNames = csAdapter.getColorantNames();
			if (devNColorantNames != null) {
				FeatureTreeNode colorantNames = root.addChild("colorantNames");
				for (String name : devNColorantNames) {
					CreateNodeHelper.addNotEmptyNode("colorantName", name, colorantNames);
				}
			}
		}

		return root;
	}

	private void parseCIEDictionaryBased(FeatureTreeNode root, ColorSpaceFeaturesObjectAdapter adapter) throws FeatureParsingException {
		parseTristimulus(adapter.getWhitePoint(), root.addChild("whitePoint"));
		parseTristimulus(adapter.getBlackPoint(), root.addChild("blackPoint"));

		String cieType = adapter.getFamily();
		if (CALGRAY.equals(cieType)) {
			Double gamma = adapter.getCalGrayGamma();
			if (gamma != null) {
				CreateNodeHelper.addNotEmptyNode("gamma", String.format("%.3f", gamma), root);
			}
		} else if (CALRGB.equals(cieType)) {
			FeatureTreeNode gamma = root.addChild("gamma");
			double[] gammaValue = adapter.getCalRGBGamma();
			if (gammaValue == null) {
				registerNewError("Gamma value is not present");
			} else if (gammaValue.length < 3) {
				registerNewError("Gamma value contains less than three elements");
			} else {
				gamma.setAttribute("red", String.format("%.3f", gammaValue[0]));
				gamma.setAttribute("green", String.format("%.3f", gammaValue[1]));
				gamma.setAttribute("blue", String.format("%.3f", gammaValue[2]));
			}
			parseFloatArray(adapter.getMatrix(), root.addChild("matrix"));
		} else if (LAB.equals(cieType)) {
			FeatureTreeNode range = root.addChild("range");
			double[] rangeValue = adapter.getLabRange();
			if (rangeValue.length < 4) {
				registerNewError("Gamma value contains less than three elements");
			} else {
				range.setAttribute("aMin", String.format("%.3f", rangeValue[0]));
				range.setAttribute("aMax", String.format("%.3f", rangeValue[1]));
				range.setAttribute("bMin", String.format("%.3f", rangeValue[2]));
				range.setAttribute("bMax", String.format("%.3f", rangeValue[3]));
			}
		}

	}

	private void parseFloatArray(double[] array, FeatureTreeNode parent) throws FeatureParsingException {
		for (int i = 0; i < array.length; ++i) {
			FeatureTreeNode element = parent.addChild("element");
			element.setAttribute("number", String.valueOf(i));
			element.setAttribute("value", String.format("%.3f", array[i]));
		}
	}

	private void parseTristimulus(double[] tris, FeatureTreeNode curNode) {
		if (tris == null) {
			return;
		} else if (tris.length < 3) {
			registerNewError("Tristimulus value contains less than three elements");
		} else {
			curNode.setAttribute("x", String.format("%.3f", tris[0]));
			curNode.setAttribute("y", String.format("%.3f", tris[1]));
			curNode.setAttribute("z", String.format("%.3f", tris[2]));
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
		// Only family field is present
		List<Feature> featuresList = new ArrayList<>();
		featuresList.add(new Feature("Family",
				generateAttributeXPath(COLOR_SPACE, FAMILY), Feature.FeatureType.STRING));
		featuresList.add(new Feature("Error IDs",
				generateAttributeXPath(COLOR_SPACE, ErrorsHelper.ERRORID), Feature.FeatureType.STRING));
		return featuresList;
	}
}
