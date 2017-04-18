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
	private static final String FONT_DESCRIPTOR = "fontDescriptor";
	private static final String FONT_NAME = "fontName";
	private static final String FONT_FAMILY = "fontFamily";
	private static final String FONT_STRETCH = "fontStretch";
	private static final String FONT_WEIGHT = "fontWeight";
	private static final String FIXED_PITCH = "fixedPitch";
	private static final String SERIF = "serif";
	private static final String SYMBOLIC = "symbolic";
	private static final String SCRIPT = "script";
	private static final String NONSYMBOLIC = "nonsymbolic";
	private static final String ITALIC = "italic";
	private static final String ALL_CAP = "allCap";
	private static final String SMALL_CAP = "smallCap";
	private static final String FORCE_BOLD = "forceBold";
	private static final String ITALIC_ANGLE = "italicAngle";
	private static final String ASCENT = "ascent";
	private static final String DESCENT = "descent";
	private static final String LEADING = "leading";
	private static final String CAP_HEIGHT = "capHeight";
	private static final String X_HEIGHT = "xHeight";
	private static final String STEM_V = "stemV";
	private static final String STEM_H = "stemH";
	private static final String AVERAGE_WIDTH = "averageWidth";
	private static final String MAX_WIDTH = "maxWidth";
	private static final String MISSING_WIDTH = "missingWidth";
	private static final String CHAR_SET = "charSet";
	private static final String EMBEDDED = "embedded";
	private static final String SUBSET = "subset";

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

			CreateNodeHelper.addNotEmptyNode("encoding", fontAdapter.getEncoding(), root);

			if (TYPE3.equals(fontType)) {
				CreateNodeHelper.addBoxFeature("fontBBox", fontAdapter.getBoundingBox(), root);
				CreateNodeHelper.parseMatrix(fontAdapter.getMatrix(), root.addChild("fontMatrix"));
				parseFontDescriptior(fontAdapter.getFontDescriptor(), root);
				parseResources(root);
			} else {
				parseFontDescriptior(fontAdapter.getFontDescriptor(), root);
			}

		} else if (CID_FONT_TYPE0.equals(fontType) ||
				CID_FONT_TYPE2.equals(fontType)) {
			Double dw = fontAdapter.getDefaultWidth();
			if (dw != null) {
				root.addChild("defaultWidth").setValue(String.valueOf(dw.intValue()));
			}
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
		// From font: only font type is present
		// From font descriptor missing fields:
		// * font bbox
		// * metadata
		List<Feature> featuresList = new ArrayList<>();
		featuresList.add(new Feature("Font Type",
				generateVariableXPath(FONT, TYPE), Feature.FeatureType.STRING));
		featuresList.add(new Feature("Font Name",
				generateVariableXPath(FONT, FONT_DESCRIPTOR, FONT_NAME), Feature.FeatureType.STRING));
		featuresList.add(new Feature("Subset",
				generateVariableXPath(FONT, FONT_DESCRIPTOR, SUBSET), Feature.FeatureType.BOOLEAN));
		featuresList.add(new Feature("Font Family",
				generateVariableXPath(FONT, FONT_DESCRIPTOR, FONT_FAMILY), Feature.FeatureType.STRING));
		featuresList.add(new Feature("Font Stretch",
				generateVariableXPath(FONT, FONT_DESCRIPTOR, FONT_STRETCH), Feature.FeatureType.STRING));
		featuresList.add(new Feature("Font Weight",
				generateVariableXPath(FONT, FONT_DESCRIPTOR, FONT_WEIGHT), Feature.FeatureType.NUMBER));
		featuresList.add(new Feature("Fixed Pitch",
				generateVariableXPath(FONT, FONT_DESCRIPTOR, FIXED_PITCH), Feature.FeatureType.BOOLEAN));
		featuresList.add(new Feature("Serif",
				generateVariableXPath(FONT, FONT_DESCRIPTOR, SERIF), Feature.FeatureType.BOOLEAN));
		featuresList.add(new Feature("Symbolic",
				generateVariableXPath(FONT, FONT_DESCRIPTOR, SYMBOLIC), Feature.FeatureType.BOOLEAN));
		featuresList.add(new Feature("Script",
				generateVariableXPath(FONT, FONT_DESCRIPTOR, SCRIPT), Feature.FeatureType.BOOLEAN));
		featuresList.add(new Feature("Non Symbolic",
				generateVariableXPath(FONT, FONT_DESCRIPTOR, NONSYMBOLIC), Feature.FeatureType.BOOLEAN));
		featuresList.add(new Feature("Italic",
				generateVariableXPath(FONT, FONT_DESCRIPTOR, ITALIC), Feature.FeatureType.BOOLEAN));
		featuresList.add(new Feature("All Cap",
				generateVariableXPath(FONT, FONT_DESCRIPTOR, ALL_CAP), Feature.FeatureType.BOOLEAN));
		featuresList.add(new Feature("Small Cap",
				generateVariableXPath(FONT, FONT_DESCRIPTOR, SMALL_CAP), Feature.FeatureType.BOOLEAN));
		featuresList.add(new Feature("Force Bold",
				generateVariableXPath(FONT, FONT_DESCRIPTOR, FORCE_BOLD), Feature.FeatureType.BOOLEAN));
		featuresList.add(new Feature("Italic Angle",
				generateVariableXPath(FONT, FONT_DESCRIPTOR, ITALIC_ANGLE), Feature.FeatureType.NUMBER));
		featuresList.add(new Feature("Ascent",
				generateVariableXPath(FONT, FONT_DESCRIPTOR, ASCENT), Feature.FeatureType.NUMBER));
		featuresList.add(new Feature("Descent",
				generateVariableXPath(FONT, FONT_DESCRIPTOR, DESCENT), Feature.FeatureType.NUMBER));
		featuresList.add(new Feature("Leading",
				generateVariableXPath(FONT, FONT_DESCRIPTOR, LEADING), Feature.FeatureType.NUMBER));
		featuresList.add(new Feature("Cap Height",
				generateVariableXPath(FONT, FONT_DESCRIPTOR, CAP_HEIGHT), Feature.FeatureType.NUMBER));
		featuresList.add(new Feature("X Height",
				generateVariableXPath(FONT, FONT_DESCRIPTOR, X_HEIGHT), Feature.FeatureType.NUMBER));
		featuresList.add(new Feature("Stem V",
				generateVariableXPath(FONT, FONT_DESCRIPTOR, STEM_V), Feature.FeatureType.NUMBER));
		featuresList.add(new Feature("Stem H",
				generateVariableXPath(FONT, FONT_DESCRIPTOR, STEM_H), Feature.FeatureType.NUMBER));
		featuresList.add(new Feature("Average Width",
				generateVariableXPath(FONT, FONT_DESCRIPTOR, AVERAGE_WIDTH), Feature.FeatureType.NUMBER));
		featuresList.add(new Feature("Max Width",
				generateVariableXPath(FONT, FONT_DESCRIPTOR, MAX_WIDTH), Feature.FeatureType.NUMBER));
		featuresList.add(new Feature("Missing Width",
				generateVariableXPath(FONT, FONT_DESCRIPTOR, MISSING_WIDTH), Feature.FeatureType.NUMBER));
		featuresList.add(new Feature("Char Set",
				generateVariableXPath(FONT, FONT_DESCRIPTOR, CHAR_SET), Feature.FeatureType.STRING));
		featuresList.add(new Feature("Embedded",
				generateVariableXPath(FONT, FONT_DESCRIPTOR, EMBEDDED), Feature.FeatureType.BOOLEAN));
		featuresList.add(new Feature("Error IDs",
				generateAttributeXPath(FONT, ErrorsHelper.ERRORID), Feature.FeatureType.STRING));
		return featuresList;
	}

	private void parseFontDescriptior(FontFeaturesObjectAdapter.FontDescriptorAdapter descriptor,
									  FeatureTreeNode root) throws FeatureParsingException {
		if (descriptor != null) {
			FeatureTreeNode descriptorNode = root.addChild(FONT_DESCRIPTOR);

			String actualFontName = descriptor.getFontName();
			if (actualFontName == null) {
				actualFontName = "";
			}
			boolean subset = actualFontName.matches("^[A-Z]{6}\\+.*");
			String fontName = subset ? actualFontName.substring(7, actualFontName.length()) : actualFontName;
			CreateNodeHelper.addNotEmptyNode(SUBSET, String.valueOf(subset), descriptorNode);
			CreateNodeHelper.addNotEmptyNode(FONT_NAME, fontName, descriptorNode);
			CreateNodeHelper.addNotEmptyNode(FONT_FAMILY, descriptor.getFontFamily(), descriptorNode);
			CreateNodeHelper.addNotEmptyNode(FONT_STRETCH, descriptor.getFontStretch(), descriptorNode);
			CreateNodeHelper.addNotEmptyNode(FONT_WEIGHT, getStringFromDouble(descriptor.getFontWeight()), descriptorNode);
			descriptorNode.addChild(FIXED_PITCH).setValue(String.valueOf(descriptor.isFixedPitch()));
			descriptorNode.addChild(SERIF).setValue(String.valueOf(descriptor.isSerif()));
			descriptorNode.addChild(SYMBOLIC).setValue(String.valueOf(descriptor.isSymbolic()));
			descriptorNode.addChild(SCRIPT).setValue(String.valueOf(descriptor.isScript()));
			descriptorNode.addChild(NONSYMBOLIC).setValue(String.valueOf(descriptor.isNonSymbolic()));
			descriptorNode.addChild(ITALIC).setValue(String.valueOf(descriptor.isItalic()));
			descriptorNode.addChild(ALL_CAP).setValue(String.valueOf(descriptor.isAllcap()));
			descriptorNode.addChild(SMALL_CAP).setValue(String.valueOf(descriptor.isSmallCap()));
			descriptorNode.addChild(FORCE_BOLD).setValue(String.valueOf(descriptor.isForceBold()));
			CreateNodeHelper.addBoxFeature("fontBBox", descriptor.getFontBoundingBox(), descriptorNode);

			CreateNodeHelper.addNotEmptyNode(ITALIC_ANGLE, getStringFromDouble(descriptor.getItalicAngle()), descriptorNode);
			CreateNodeHelper.addNotEmptyNode(ASCENT, getStringFromDouble(descriptor.getAscent()), descriptorNode);
			CreateNodeHelper.addNotEmptyNode(DESCENT, getStringFromDouble(descriptor.getDescent()), descriptorNode);
			CreateNodeHelper.addNotEmptyNode(LEADING, getStringFromDouble(descriptor.getLeading()), descriptorNode);
			CreateNodeHelper.addNotEmptyNode(CAP_HEIGHT, getStringFromDouble(descriptor.getCapHeight()), descriptorNode);
			CreateNodeHelper.addNotEmptyNode(X_HEIGHT, getStringFromDouble(descriptor.getXHeight()), descriptorNode);
			CreateNodeHelper.addNotEmptyNode(STEM_V, getStringFromDouble(descriptor.getStemV()), descriptorNode);
			CreateNodeHelper.addNotEmptyNode(STEM_H, getStringFromDouble(descriptor.getStemH()), descriptorNode);
			CreateNodeHelper.addNotEmptyNode(AVERAGE_WIDTH, getStringFromDouble(descriptor.getAverageWidth()), descriptorNode);
			CreateNodeHelper.addNotEmptyNode(MAX_WIDTH, getStringFromDouble(descriptor.getMaxWidth()), descriptorNode);
			CreateNodeHelper.addNotEmptyNode(MISSING_WIDTH, getStringFromDouble(descriptor.getMissingWidth()), descriptorNode);
			CreateNodeHelper.addNotEmptyNode(CHAR_SET, descriptor.getCharSet(), descriptorNode);

			descriptorNode.addChild(EMBEDDED).setValue(String.valueOf(descriptor.isEmbedded()));
			try (InputStream metadata = descriptor.getMetadataStream()) {
				CreateNodeHelper.parseMetadata(metadata, "embeddedFileMetadata", descriptorNode, this);
			} catch (IOException e) {
				LOGGER.log(Level.FINE, "Error while obtaining unfiltered metadata stream", e);
				registerNewError(e.getMessage());
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
		return d == null ? null : String.format("%.3f", d);
	}

}
