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
import org.verapdf.features.FontFeaturesData;
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
 * Feature object for font
 *
 * @author Maksim Bezrukov
 */
public class FontFeaturesObject extends FeaturesObject {

	private static final Logger LOGGER = Logger.getLogger(FontFeaturesObject.class.getCanonicalName());

	private static final String ID = "id";
	private static final String FONT = "font";
	private static final String TYPE0 = "Type0";
	private static final String TRUE_TYPE = "TrueType";
	private static final String TYPE1 = "Type1";
	private static final String MM_TYPE1 = "MMType1";
	private static final String TYPE3 = "Type3";
	private static final String CID_FONT_TYPE0 = "CIDFontType0";
	private static final String CID_FONT_TYPE2 = "CIDFontType2";
	private static final String TYPE = "type";

	/**
	 * Constructs new Font Feature Object
	 *
	 * @param adapter class represents font adapter
	 */
	public FontFeaturesObject(FontFeaturesObjectAdapter adapter) {
		super(adapter);
	}

	/**
	 * @return FONT instance of the FeatureObjectType enumeration
	 */
	@Override
	public FeatureObjectType getType() {
		return FeatureObjectType.FONT;
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
		FontFeaturesObjectAdapter fontAdapter = (FontFeaturesObjectAdapter) this.adapter;
		FeatureTreeNode root = FeatureTreeNode.createRootNode("font");
		String id = fontAdapter.getId();
		if (id != null) {
			root.setAttribute(ID, id);
		}

		String fontType = fontAdapter.getType();
		CreateNodeHelper.addNotEmptyNode(TYPE, fontType, root);

		if (!(TYPE3.equals(fontType))) {
			CreateNodeHelper.addNotEmptyNode("baseFont", fontAdapter.getBaseFont(), root);
		}

		if (TYPE0.equals(fontType)) {
			CreateNodeHelper.parseIDSet(fontAdapter.getFontChild(), "descendantFont", null, root.addChild("descendantFonts"));
			parseFontDescriptior(fontAdapter.getFontDescriptor(), root);
		} else if (TRUE_TYPE.equals(fontType) ||
				TYPE1.equals(fontType) ||
				MM_TYPE1.equals(fontType) ||
				TYPE3.equals(fontType)) {

			Long fc = fontAdapter.getFirstChar();
			if (fc != null && fc.longValue() != -1) {
				root.addChild("firstChar").setValue(String.valueOf(fc.longValue()));
			}
			Long lc = fontAdapter.getLastChar();
			if (lc != null && lc.longValue() != -1) {
				root.addChild("lastChar").setValue(String.valueOf(lc.longValue()));
			}

			parseWidths(fontAdapter.getWidth(), fc, root.addChild("widths"));

			CreateNodeHelper.addNotEmptyNode("encoding", fontAdapter.getEncoding(), root);

			if (!TYPE3.equals(fontType)) {
				parseFontDescriptior(fontAdapter.getFontDescriptor(), root);
			}

			if (TYPE3.equals(fontType)) {
				CreateNodeHelper.addBoxFeature("fontBBox", fontAdapter.getBoundingBox(), root);
				CreateNodeHelper.parseMatrix(fontAdapter.getMatrix(), root.addChild("fontMatrix"));

				parseResources(root);
			}

		} else if (CID_FONT_TYPE0.equals(fontType) ||
				CID_FONT_TYPE2.equals(fontType)) {
			Double dw = fontAdapter.getDefaultWidth();
			root.addChild("defaultWidth").setValue(String.valueOf(dw.intValue()));

			if (fontAdapter.isCIDSystemInfoPresent()) {
				FeatureTreeNode cidS = root.addChild("cidSystemInfo");
				CreateNodeHelper.addNotEmptyNode("registry", fontAdapter.getCIDSysInfoRegistry(), cidS);
				CreateNodeHelper.addNotEmptyNode("ordering", fontAdapter.getCIDSysInfoOrdering(), cidS);
				Long supplement = fontAdapter.getCIDSysInfoSupplement();
				if (supplement != null) {
					cidS.addChild("supplement").setValue(String.valueOf(supplement));
				}
			}
			parseFontDescriptior(fontAdapter.getFontDescriptor(), root);
		}
		return root;
	}

	/**
	 * @return null if it can not get font file stream and features data of the font file and descriptor in other case.
	 */
	@Override
	public FeaturesData getData() {
		FontFeaturesObjectAdapter fontAdapter = (FontFeaturesObjectAdapter) this.adapter;
		FontFeaturesObjectAdapter.FontDescriptorAdapter descriptor = fontAdapter.getFontDescriptor();
		if (descriptor != null) {
			InputStream fontIn = descriptor.getData();
			if (fontIn == null) {
				LOGGER.log(Level.FINE, "Missed font file InputStream");
				return null;
			}
			FontFeaturesData.Builder builder = new FontFeaturesData.Builder(fontIn);
			builder.metadata(descriptor.getMetadataStream());

			builder.fontName(descriptor.getFontName());
			builder.fontFamily(descriptor.getFontFamily());
			builder.fontStretch(descriptor.getFontStretch());
			builder.fontWeight(descriptor.getFontWeight());
			Long flags = descriptor.getFlags();
			builder.flags(flags == null ? null : flags.intValue());
			double[] rex = descriptor.getFontBoundingBox();
			if (rex != null) {
				List<Double> rect = new ArrayList<>(rex.length);
				for (int i = 0; i < rex.length; ++i) {
					rect.add(rex[i]);
				}
				builder.fontBBox(rect);
			}
			builder.italicAngle(descriptor.getItalicAngle());
			builder.ascent(descriptor.getAscent());
			builder.descent(descriptor.getDescent());
			builder.leading(descriptor.getLeading());
			builder.capHeight(descriptor.getCapHeight());
			builder.xHeight(descriptor.getXHeight());
			builder.stemV(descriptor.getStemV());
			builder.stemH(descriptor.getStemH());
			builder.avgWidth(descriptor.getAverageWidth());
			builder.maxWidth(descriptor.getMaxWidth());
			builder.missingWidth(descriptor.getMissingWidth());
			builder.charSet(descriptor.getCharSet());

			return builder.build();
		}
		return null;
	}

	static List<Feature> getFeaturesList() {
		// Only font type is present
		List<Feature> featuresList = new ArrayList<>();
		featuresList.add(new Feature("Font Type",
				generateVariableXPath(FONT, TYPE), Feature.FeatureType.STRING));

		featuresList.add(new Feature("Error IDs",
				generateAttributeXPath(FONT, ErrorsHelper.ERRORID), Feature.FeatureType.STRING));
		return featuresList;
	}

	private void parseFontDescriptior(FontFeaturesObjectAdapter.FontDescriptorAdapter descriptor,
									  FeatureTreeNode root) throws FeatureParsingException {
		if (descriptor != null) {
			FeatureTreeNode descriptorNode = root.addChild("fontDescriptor");

			CreateNodeHelper.addNotEmptyNode("fontName", descriptor.getFontName(), descriptorNode);
			CreateNodeHelper.addNotEmptyNode("fontFamily", descriptor.getFontFamily(), descriptorNode);
			CreateNodeHelper.addNotEmptyNode("fontStretch", descriptor.getFontStretch(), descriptorNode);
			CreateNodeHelper.addNotEmptyNode("fontWeight", getStringFromDouble(descriptor.getFontWeight()), descriptorNode);
			descriptorNode.addChild("fixedPitch").setValue(String.valueOf(descriptor.isFixedPitch()));
			descriptorNode.addChild("serif").setValue(String.valueOf(descriptor.isSerif()));
			descriptorNode.addChild("symbolic").setValue(String.valueOf(descriptor.isSymbolic()));
			descriptorNode.addChild("script").setValue(String.valueOf(descriptor.isScript()));
			descriptorNode.addChild("nonsymbolic").setValue(String.valueOf(descriptor.isNonSymbolic()));
			descriptorNode.addChild("italic").setValue(String.valueOf(descriptor.isItalic()));
			descriptorNode.addChild("allCap").setValue(String.valueOf(descriptor.isAllcap()));
			descriptorNode.addChild("smallCap").setValue(String.valueOf(descriptor.isScript()));
			descriptorNode.addChild("forceBold").setValue(String.valueOf(descriptor.isForceBold()));
			CreateNodeHelper.addBoxFeature("fontBBox", descriptor.getFontBoundingBox(), descriptorNode);

			CreateNodeHelper.addNotEmptyNode("italicAngle", getStringFromDouble(descriptor.getItalicAngle()), descriptorNode);
			CreateNodeHelper.addNotEmptyNode("ascent", getStringFromDouble(descriptor.getAscent()), descriptorNode);
			CreateNodeHelper.addNotEmptyNode("descent", getStringFromDouble(descriptor.getDescent()), descriptorNode);
			CreateNodeHelper.addNotEmptyNode("leading", getStringFromDouble(descriptor.getLeading()), descriptorNode);
			CreateNodeHelper.addNotEmptyNode("capHeight", getStringFromDouble(descriptor.getCapHeight()), descriptorNode);
			CreateNodeHelper.addNotEmptyNode("xHeight", getStringFromDouble(descriptor.getXHeight()), descriptorNode);
			CreateNodeHelper.addNotEmptyNode("stemV", getStringFromDouble(descriptor.getStemV()), descriptorNode);
			CreateNodeHelper.addNotEmptyNode("stemH", getStringFromDouble(descriptor.getStemH()), descriptorNode);
			CreateNodeHelper.addNotEmptyNode("averageWidth", getStringFromDouble(descriptor.getAverageWidth()), descriptorNode);
			CreateNodeHelper.addNotEmptyNode("maxWidth", getStringFromDouble(descriptor.getMaxWidth()), descriptorNode);
			CreateNodeHelper.addNotEmptyNode("missingWidth", getStringFromDouble(descriptor.getMissingWidth()), descriptorNode);
			CreateNodeHelper.addNotEmptyNode("charSet", descriptor.getCharSet(), descriptorNode);

			descriptorNode.addChild("embedded").setValue(String.valueOf(descriptor.isEmbedded()));
			try (InputStream metadata = descriptor.getMetadataStream()) {
				CreateNodeHelper.parseMetadata(metadata, "embeddedFileMetadata", descriptorNode, this);
			} catch (IOException e) {
				LOGGER.log(Level.FINE, "Error while obtaining unfiltered metadata stream", e);
				registerNewError(e.getMessage());
			}
		}
	}

	private static void parseWidths(List<Long> array, Long firstChar,
									FeatureTreeNode parent) throws FeatureParsingException {
		if (firstChar != null) {
			int fc = firstChar.intValue() == -1 ? 0 : firstChar.intValue();
			for (int i = 0; i < array.size(); ++i) {
				FeatureTreeNode element = parent.addChild("width");
				Long arElement = array.get(i);
				element.setValue(String.valueOf(arElement.longValue()));
				element.setAttribute("char", String.valueOf(i + fc));
			}
		}
	}

	private void parseResources(FeatureTreeNode root) throws FeatureParsingException {
		FontFeaturesObjectAdapter fontAdapter = (FontFeaturesObjectAdapter) this.adapter;
		Set<String> extGStateChild = fontAdapter.getExtGStateChild();
		Set<String> colorSpaceChild = fontAdapter.getColorSpaceChild();
		Set<String> patternChild = fontAdapter.getPatternChild();
		Set<String> shadingChild = fontAdapter.getShadingChild();
		Set<String> xobjectChild = fontAdapter.getXObjectChild();
		Set<String> fontChild = fontAdapter.getFontChild();
		Set<String> propertiesChild = fontAdapter.getPropertiesChild();
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

	private static String getStringFromDouble(Double d) {
		return d == null ? null : Double.toString(d);
	}

}
