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
import org.verapdf.features.ImageFeaturesData;
import org.verapdf.features.tools.CreateNodeHelper;
import org.verapdf.features.tools.ErrorsHelper;
import org.verapdf.features.tools.FeatureTreeNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Features object for image xobject.
 *
 * @author Sergey Shemyakov
 */
public class ImageXObjectFeaturesObject extends FeaturesObject {

	private static final Logger LOGGER = Logger.getLogger(ImageXObjectFeaturesObject.class.getCanonicalName());

	private static final String X_OBJECT = "xobject";
	private static final String IMAGE = "image";
	private static final String X_OBJECT_XPATH = X_OBJECT + "[@type='" + IMAGE + "']";
	private static final String ID = "id";
	private static final String WIDTH = "width";
	private static final String HEIGHT = "height";
	private static final String BITS_PER_COMPONENT = "bitsPerComponent";
	private static final String INTERPOLATE = "interpolate";
	private static final String STRUCT_PARENT = "structParent";
	private static final String FILTERS = "filters";
	private static final String FILTER = "filter";

	/**
	 * Constructs new Image XObject Feature Object
	 *
	 * @param adapter class represents annotation adapter
	 */
	public ImageXObjectFeaturesObject(ImageXObjectFeaturesObjectAdapter adapter) {
		super(adapter);
	}

	static List<Feature> getFeaturesList() {
		// Missed fields:
		// * image mask
		List<Feature> featuresList = new ArrayList<>();
		featuresList.add(new Feature("Width",
				generateVariableXPath(X_OBJECT_XPATH, WIDTH), Feature.FeatureType.NUMBER));
		featuresList.add(new Feature("Height",
				generateVariableXPath(X_OBJECT_XPATH, HEIGHT), Feature.FeatureType.NUMBER));
		featuresList.add(new Feature("Bits Per Component",
				generateVariableXPath(X_OBJECT_XPATH, BITS_PER_COMPONENT), Feature.FeatureType.NUMBER));
		featuresList.add(new Feature("Interpolate",
				generateVariableXPath(X_OBJECT_XPATH, INTERPOLATE), Feature.FeatureType.BOOLEAN));
		featuresList.add(new Feature("Struct Parent",
				generateVariableXPath(X_OBJECT_XPATH, STRUCT_PARENT), Feature.FeatureType.NUMBER));
		featuresList.add(new Feature("Filter",
				generateVariableXPath(X_OBJECT_XPATH, FILTERS, FILTER), Feature.FeatureType.STRING));
		featuresList.add(new Feature("Error IDs",
				generateAttributeXPath(X_OBJECT_XPATH, ErrorsHelper.ERRORID), Feature.FeatureType.STRING));
		return featuresList;
	}

	@Override
	public FeatureObjectType getType() {
		return FeatureObjectType.IMAGE_XOBJECT;
	}

	@Override
	protected FeatureTreeNode collectFeatures() throws FeatureParsingException {
		ImageXObjectFeaturesObjectAdapter xImageAdapter = (ImageXObjectFeaturesObjectAdapter) this.adapter;
		FeatureTreeNode root = FeatureTreeNode.createRootNode(X_OBJECT);
		root.setAttribute("type", IMAGE);
		String id = xImageAdapter.getID();
		if (id != null) {
			root.setAttribute(ID, id);
		}
		Long width = xImageAdapter.getWidth();
		if (width != null) {
			CreateNodeHelper.addNotEmptyNode(WIDTH, String.valueOf(width.longValue()), root);
		}
		Long height = xImageAdapter.getHeight();
		if (height != null) {
			CreateNodeHelper.addNotEmptyNode(HEIGHT, String.valueOf(height.longValue()), root);
		}

		String colorSpaceChild = xImageAdapter.getColorSpaceChild();
		if (colorSpaceChild != null) {
			FeatureTreeNode shading = root.addChild("colorSpace");
			shading.setAttribute(ID, colorSpaceChild);
		}

		Long bitsPerComponent = xImageAdapter.getBitsPerComponent();
		if (bitsPerComponent != null) {
			CreateNodeHelper.addNotEmptyNode(BITS_PER_COMPONENT, String.valueOf(bitsPerComponent.longValue()), root);
		}

		root.addChild("imageMask").setValue(String.valueOf(xImageAdapter.getImageMask()));

		String maskChild = xImageAdapter.getMaskChild();
		if (maskChild != null) {
			FeatureTreeNode mask = root.addChild("mask");
			mask.setAttribute(ID, maskChild);
		}

		CreateNodeHelper.addNotEmptyNode(INTERPOLATE, String.valueOf(xImageAdapter.isInterpolate()), root);
		CreateNodeHelper.parseIDSet(xImageAdapter.getAlternatesChild(),
				"alternate", "alternates", root);
		String sMaskChild = xImageAdapter.getSMaskChild();
		if (sMaskChild != null) {
			FeatureTreeNode mask = root.addChild("sMask");
			mask.setAttribute(ID, sMaskChild);
		}

		Long struct = xImageAdapter.getStructParent();
		if (struct != null) {
			CreateNodeHelper.addNotEmptyNode(STRUCT_PARENT, String.valueOf(struct.longValue()), root);
		}

		List<String> filtersList = xImageAdapter.getFilters();
		if (!filtersList.isEmpty()) {
			FeatureTreeNode filters = root.addChild(FILTERS);
			for (String name : filtersList) {
				CreateNodeHelper.addNotEmptyNode(FILTER, name, filters);
			}
		}

		CreateNodeHelper.parseMetadata(xImageAdapter.getMetadata(), "metadata", root, this);
		return root;
	}

	@Override
	public FeaturesData getData() {
		ImageXObjectFeaturesObjectAdapter xImageAdapter = (ImageXObjectFeaturesObjectAdapter) this.adapter;
		List<ImageFeaturesData.Filter> resFilters = new ArrayList<>();
		List<String> filterNames = xImageAdapter.getFilters();
		if (!filterNames.isEmpty()) {
			List<ImageXObjectFeaturesObjectAdapter.StreamFilterAdapter> filterAdapters =
					xImageAdapter.getFilterAdapters();
			for (int i = 0; i < filterNames.size(); ++i) {
				String filterNameValue = filterNames.get(i);
				ImageXObjectFeaturesObjectAdapter.StreamFilterAdapter adapter = i < filterAdapters.size() ?
						filterAdapters.get(i) : null;
				switch (filterNameValue) {
					case "LZWDecode":
						resFilters.add(ImageFeaturesData.Filter.newInstance(filterNameValue, createLZWFilterMap(adapter),
								null));
						break;
					case "FlateDecode":
						resFilters.add(ImageFeaturesData.Filter.newInstance(filterNameValue, createFlatFilterMap(adapter),
								null));
						break;
					case "CCITTFaxDecode":
						resFilters.add(ImageFeaturesData.Filter.newInstance(filterNameValue, getCCITTFaxFiltersMap(adapter), null));
						break;
					case "DCTDecode":
						resFilters.add(ImageFeaturesData.Filter.newInstance(filterNameValue, getDCTFiltersMap(adapter), null));
						break;
					case "JBIG2Decode":
						resFilters.add(ImageFeaturesData.Filter.newInstance(filterNameValue, new HashMap<>(),
								adapter.getJBIG2Global()));
						break;
					case "Crypt":
						if (adapter.hasCryptFilter()) {
							LOGGER.log(Level.FINE, "An Image has a Crypt filter");
							return null;
						}
						//$FALL-THROUGH$
					default:
						resFilters.add(
								ImageFeaturesData.Filter.newInstance(filterNameValue, new HashMap<>(), null));
				}
			}
		}

		Integer width = getIntegerWithDefault(xImageAdapter.getWidth(), null);
		Integer height = getIntegerWithDefault(xImageAdapter.getHeight(), null);

		return ImageFeaturesData.newInstance(xImageAdapter.getMetadata(),
				xImageAdapter.getRawStreamData(), width, height, resFilters);
	}

	private static Map<String, String> getCCITTFaxFiltersMap(
			ImageXObjectFeaturesObjectAdapter.StreamFilterAdapter filter) {
		Map<String, String> res = new HashMap<>();
		if (filter != null) {
			putIntegerAsStringWithDefault(res, "K", filter.getCCITTK(), 0);
			putBooleanAsStringWithDefault(res, "EndOfLine", filter.getCCITTEndOfLine(), Boolean.FALSE);
			putBooleanAsStringWithDefault(res, "EncodedByteAlign", filter.getCCITTEncodedByteAlign(),
					Boolean.FALSE);
			putIntegerAsStringWithDefault(res, "Columns", filter.getCCITTColumns(), 1728);
			putIntegerAsStringWithDefault(res, "Rows", filter.getCCITTRows(), 0);
			putBooleanAsStringWithDefault(res, "EndOfBlock", filter.getCCITTEndOfBlock(),
					Boolean.TRUE);
			putBooleanAsStringWithDefault(res, "BlackIs1", filter.getCCITTBlackIs1(), Boolean.FALSE);
			putIntegerAsStringWithDefault(res, "DamagedRowsBeforeError",
					filter.getCCITTDamagedRowsBeforeError(), 0);
		} else {
			res.put("K", "0");
			res.put("EndOfLine", "false");
			res.put("EncodedByteAlign", "false");
			res.put("Columns", "1728");
			res.put("Rows", "0");
			res.put("EndOfBlock", "true");
			res.put("BlackIs1", "false");
			res.put("DamagedRowsBeforeError", "0");
		}

		return res;
	}

	private static Map<String, String> getDCTFiltersMap(
			ImageXObjectFeaturesObjectAdapter.StreamFilterAdapter filter) {
		Map<String, String> res = new HashMap<>();
		if (filter != null) {
			Long colorTransform = filter.getDCTColorTransform();
			if (colorTransform != null) {
				res.put("ColorTransform", String.valueOf(colorTransform.longValue()));
			}
		}
		return res;
	}

	private static Map<String, String> createLZWFilterMap(
			ImageXObjectFeaturesObjectAdapter.StreamFilterAdapter filter) {
		if (filter == null) {
			Map<String, String> retVal = createDefaultFlatFilterMap();
			retVal.put("EarlyChange", "1");
			return retVal;
		}

		Map<String, String> retVal = createFlatFilterMap(filter);
		putIntegerAsStringWithDefault(retVal, "EarlyChange", filter.getLZWEarlyChange(), 1);
		return retVal;

	}

	private static Map<String, String> createFlatFilterMap(
			ImageXObjectFeaturesObjectAdapter.StreamFilterAdapter filter) {
		if (filter == null) {
			return createDefaultFlatFilterMap();
		}

		Map<String, String> res = new HashMap<>();

		putIntegerAsStringWithDefault(res, "Predictor", filter.getFlatePredictor(), 1);
		putIntegerAsStringWithDefault(res, "Colors", filter.getFlateColors(), 1);
		putIntegerAsStringWithDefault(res, "BitsPerComponent", filter.getFlateBitsPerComponent(), 8);
		putIntegerAsStringWithDefault(res, "Columns", filter.getFlateColumns(), 1);
		return res;
	}

	private static Map<String, String> createDefaultFlatFilterMap() {
		Map<String, String> res = new HashMap<>();
		res.put("Predictor", "1");
		res.put("Colors", "1");
		res.put("BitsPerComponent", "8");
		res.put("Columns", "1");
		return res;
	}

	private static Integer getIntegerWithDefault(Long value, Integer defaultValue) {
		if (value != null) {
			return value.intValue();
		}
		return defaultValue;
	}

	private static void putIntegerAsStringWithDefault(Map<String, String> map, String key, Long value,
													  Integer defaultValue) {
		if (value != null) {
			map.put(key, String.valueOf(value.longValue()));
		} else {
			if (defaultValue != null) {
				map.put(key, defaultValue.toString());
			}
		}
	}

	private static void putBooleanAsStringWithDefault(Map<String, String> map, String key, Boolean value,
													  Boolean defaultValue) {
		if (value != null) {
			map.put(key, String.valueOf(value.booleanValue()));
		} else {
			if (defaultValue != null) {
				map.put(key, defaultValue.toString());
			}
		}
	}
}
