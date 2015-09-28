package org.verapdf.features.pb.objects;

import org.apache.log4j.Logger;
import org.apache.pdfbox.cos.*;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.common.PDStream;
import org.apache.pdfbox.pdmodel.font.*;
import org.verapdf.exceptions.featurereport.FeaturesTreeNodeException;
import org.verapdf.features.FeaturesData;
import org.verapdf.features.FeaturesObjectTypesEnum;
import org.verapdf.features.IFeaturesObject;
import org.verapdf.features.pb.tools.PBCreateNodeHelper;
import org.verapdf.features.tools.FeatureTreeNode;
import org.verapdf.features.tools.FeaturesCollection;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * Feature object for fonts
 *
 * @author Maksim Bezrukov
 */
public class PBFontFeaturesObject implements IFeaturesObject {

	private static final Logger LOGGER = Logger
			.getLogger(PBFontFeaturesObject.class);

	private static final String ID = "id";

	private PDFontLike fontLike;
	private String id;
	private Set<String> extGStateChild;
	private Set<String> colorSpaceChild;
	private Set<String> patternChild;
	private Set<String> shadingChild;
	private Set<String> xobjectChild;
	private Set<String> fontChild;
	private Set<String> procSetChild;
	private Set<String> propertiesChild;
	private Set<String> extGStateParent;
	private Set<String> pageParent;
	private Set<String> patternParent;
	private Set<String> xobjectParent;
	private Set<String> fontParent;

	/**
	 * Constructs new font features object
	 *
	 * @param fontLike        PDFontLike which represents font for feature report
	 * @param id              id of the object
	 * @param extGStateChild  set of external graphics state id which contains in resource dictionary of this font
	 * @param colorSpaceChild set of ColorSpace id which contains in resource dictionary of this font
	 * @param patternChild    set of pattern id which contains in resource dictionary of this font
	 * @param shadingChild    set of shading id which contains in resource dictionary of this font
	 * @param xobjectChild    set of XObject id which contains in resource dictionary of this font
	 * @param fontChild       set of font id which contains in resource dictionary of this font
	 * @param procSetChild    set of procedure set id which contains in resource dictionary of this font
	 * @param propertiesChild set of properties id which contains in resource dictionary of this font
	 * @param pageParent      set of page ids which contains the given font as its resources
	 * @param extGStateParent set of graphicsState ids which contains the given font as their resource
	 * @param patternParent   set of pattern ids which contains the given font as its resources
	 * @param xobjectParent   set of xobject ids which contains the given font as its resources
	 * @param fontParent      set of font ids which contains the given font as its resources
	 */
	public PBFontFeaturesObject(PDFontLike fontLike, String id, Set<String> extGStateChild, Set<String> colorSpaceChild, Set<String> patternChild, Set<String> shadingChild, Set<String> xobjectChild, Set<String> fontChild, Set<String> procSetChild, Set<String> propertiesChild, Set<String> extGStateParent, Set<String> pageParent, Set<String> patternParent, Set<String> xobjectParent, Set<String> fontParent) {
		this.fontLike = fontLike;
		this.id = id;
		this.extGStateChild = extGStateChild;
		this.colorSpaceChild = colorSpaceChild;
		this.patternChild = patternChild;
		this.shadingChild = shadingChild;
		this.xobjectChild = xobjectChild;
		this.fontChild = fontChild;
		this.procSetChild = procSetChild;
		this.propertiesChild = propertiesChild;
		this.extGStateParent = extGStateParent;
		this.pageParent = pageParent;
		this.patternParent = patternParent;
		this.xobjectParent = xobjectParent;
		this.fontParent = fontParent;
	}

	/**
	 * @return FONT instance of the FeaturesObjectTypesEnum enumeration
	 */
	@Override
	public FeaturesObjectTypesEnum getType() {
		return FeaturesObjectTypesEnum.FONT;
	}

	/**
	 * Reports featurereport into collection
	 *
	 * @param collection collection for feature report
	 * @return FeatureTreeNode class which represents a root node of the constructed collection tree
	 * @throws FeaturesTreeNodeException occurs when wrong features tree node constructs
	 */
	@Override
	public FeatureTreeNode reportFeatures(FeaturesCollection collection) throws FeaturesTreeNodeException {
		if (fontLike != null) {
			FeatureTreeNode root = FeatureTreeNode.newRootInstance("font");
			root.addAttribute(ID, id);

			parseParents(root);

			if (fontLike instanceof PDFont) {
				PDFont font = (PDFont) fontLike;
				PBCreateNodeHelper.addNotEmptyNode("type", font.getSubType(), root);
				PBCreateNodeHelper.addNotEmptyNode("baseFont", font.getName(), root);

				if (font instanceof PDType0Font) {
					PBCreateNodeHelper.parseIDSet(fontChild, "descendedFont", null, FeatureTreeNode.newChildInstance("descendedFonts", root));
					parseFontDescriptior(fontLike.getFontDescriptor(), root, collection);
				} else if (font instanceof PDSimpleFont) {
					PDSimpleFont sFont = (PDSimpleFont) font;

					int fc = sFont.getCOSObject().getInt(COSName.FIRST_CHAR);
					if (fc != -1) {
						FeatureTreeNode.newChildInstanceWithValue("firstChar", String.valueOf(fc), root);
					}
					int lc = sFont.getCOSObject().getInt(COSName.LAST_CHAR);
					if (lc != -1) {
						FeatureTreeNode.newChildInstanceWithValue("lastChar", String.valueOf(lc), root);
					}

					parseIntList(sFont.getWidths(), FeatureTreeNode.newChildInstance("widths", root));

					COSBase enc = sFont.getCOSObject().getDictionaryObject(COSName.ENCODING);
					if (enc instanceof COSName) {
						PBCreateNodeHelper.addNotEmptyNode("encoding", ((COSName) enc).getName(), root);
					} else if (enc instanceof COSDictionary) {
						COSBase name = ((COSDictionary) enc).getDictionaryObject(COSName.BASE_ENCODING);
						if (name instanceof COSName) {
							PBCreateNodeHelper.addNotEmptyNode("encoding", ((COSName) name).getName(), root);
						}
					}

					parseFontDescriptior(fontLike.getFontDescriptor(), root, collection);

					if (sFont instanceof PDType3Font) {
						PDType3Font type3 = (PDType3Font) sFont;

						PBCreateNodeHelper.addBoxFeature("fontBBox", type3.getFontBBox(), root);
						parseFloatMatrix(type3.getFontMatrix().getValues(), FeatureTreeNode.newChildInstance("fontMatrix", root));

						parseResources(root);
					}
				}

			} else if (fontLike instanceof PDCIDFont) {
				PDCIDFont cid = (PDCIDFont) fontLike;
				PBCreateNodeHelper.addNotEmptyNode("type", cid.getCOSObject().getNameAsString(COSName.SUBTYPE), root);
				PBCreateNodeHelper.addNotEmptyNode("baseFont", cid.getBaseFont(), root);
				COSBase dw = cid.getCOSObject().getDictionaryObject(COSName.DW);
				if (dw instanceof COSNumber) {
					FeatureTreeNode.newChildInstanceWithValue("defaultWidth", String.valueOf(((COSNumber) dw).floatValue()), root);
				}

				if (cid.getCIDSystemInfo() != null) {
					FeatureTreeNode cidS = FeatureTreeNode.newChildInstance("cidSystemInfo", root);
					PBCreateNodeHelper.addNotEmptyNode("registry", cid.getCIDSystemInfo().getRegistry(), cidS);
					PBCreateNodeHelper.addNotEmptyNode("ordering", cid.getCIDSystemInfo().getOrdering(), cidS);
					FeatureTreeNode.newChildInstanceWithValue("supplement", String.valueOf(cid.getCIDSystemInfo().getSupplement()), cidS);

				}
				parseFontDescriptior(fontLike.getFontDescriptor(), root, collection);
			}

			collection.addNewFeatureTree(FeaturesObjectTypesEnum.FONT, root);
			return root;
		}

		return null;
	}

	private static byte[] inputStreamToByteArray(InputStream is) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int reads = is.read();
		while (reads != -1) {
			baos.write(reads);
			reads = is.read();
		}
		return baos.toByteArray();
	}

	/**
	 * @return null if it can not get font file stream and features data of the font file and descriptor in other case.
	 */
	@Override
	public FeaturesData getData() {
		PDFontDescriptor descriptor = fontLike.getFontDescriptor();
		if (descriptor != null) {
			PDStream file = descriptor.getFontFile();
			if (file == null) {
				file = descriptor.getFontFile2();
			}
			if (file == null) {
				file = descriptor.getFontFile3();
			}
			if (file != null) {
				try {
					byte[] stream = inputStreamToByteArray(file.getStream().getUnfilteredStream());
					byte[] metadata = null;
					if (file.getMetadata() != null) {
						try {
							metadata = inputStreamToByteArray(file.getMetadata().getStream().getUnfilteredStream());
						} catch (IOException e) {
							LOGGER.error("Can not get metadata stream for font file", e);
						}
					}

					Map<String, Object> properties = new HashMap<>();

					putIfNotNull(properties, "FontName", descriptor.getFontName());
					putIfNotNull(properties, "FontFamily", descriptor.getFontFamily());
					putIfNotNull(properties, "FontStretch", descriptor.getFontStretch());
					putNumberWithDefault(properties, "FontWeight",
							descriptor.getCOSObject().getDictionaryObject(COSName.FONT_WEIGHT), null);
					COSBase fl = descriptor.getCOSObject().getDictionaryObject(COSName.FLAGS);
					if (fl instanceof COSInteger) {
						properties.put("Flags", String.valueOf(((COSInteger) fl).intValue()));
					}
					PDRectangle rex = descriptor.getFontBoundingBox();
					if (rex != null) {
						List<String> rect = new ArrayList<>();
						rect.add(String.valueOf(rex.getLowerLeftX()));
						rect.add(String.valueOf(rex.getLowerLeftY()));
						rect.add(String.valueOf(rex.getUpperRightX()));
						rect.add(String.valueOf(rex.getUpperRightY()));
						properties.put("FontBBox", rect);
					}
					putNumberWithDefault(properties, "ItalicAngle",
							descriptor.getCOSObject().getDictionaryObject(COSName.ITALIC_ANGLE), null);
					putNumberWithDefault(properties, "Ascent",
							descriptor.getCOSObject().getDictionaryObject(COSName.ASCENT), null);
					putNumberWithDefault(properties, "Descent",
							descriptor.getCOSObject().getDictionaryObject(COSName.DESCENT), null);
					putNumberWithDefault(properties, "Leading",
							descriptor.getCOSObject().getDictionaryObject(COSName.LEADING), "0");
					putNumberWithDefault(properties, "CapHeight",
							descriptor.getCOSObject().getDictionaryObject(COSName.CAP_HEIGHT), null);
					putNumberWithDefault(properties, "XHeight",
							descriptor.getCOSObject().getDictionaryObject(COSName.XHEIGHT), "0");
					putNumberWithDefault(properties, "StemV",
							descriptor.getCOSObject().getDictionaryObject(COSName.STEM_V), null);
					putNumberWithDefault(properties, "StemH",
							descriptor.getCOSObject().getDictionaryObject(COSName.STEM_H), "0");
					putNumberWithDefault(properties, "AvgWidth",
							descriptor.getCOSObject().getDictionaryObject(COSName.AVG_WIDTH), "0");
					putNumberWithDefault(properties, "MaxWidth",
							descriptor.getCOSObject().getDictionaryObject(COSName.MAX_WIDTH), "0");
					putNumberWithDefault(properties, "MissingWidth",
							descriptor.getCOSObject().getDictionaryObject(COSName.MISSING_WIDTH), "0");

					putIfNotNull(properties, "CharSet", descriptor.getCharSet());

					ArrayList<byte[]> fontFileList = new ArrayList<>();
					fontFileList.add(stream);
					return new FeaturesData(metadata, fontFileList, properties);
				} catch (IOException e) {
					LOGGER.error("Error in obtaining features data for fonts", e);
				}
			}
		}
		return null;
	}

	private static void putNumberWithDefault(Map<String, Object> map, String key, Object value, String defaultValue) {
		if (value instanceof COSNumber) {
			map.put(key, String.valueOf(((COSNumber) value).doubleValue()));
		} else {
			if (!(defaultValue == null)) {
				map.put(key, defaultValue);
			}
		}
	}

	private static void putIfNotNull(Map<String, Object> map, String key, Object value) {
		if (key != null && value != null) {
			map.put(key, value);
		}
	}

	private static void parseFontDescriptior(PDFontDescriptor descriptor, FeatureTreeNode root, FeaturesCollection collection) throws FeaturesTreeNodeException {
		if (descriptor != null) {
			FeatureTreeNode descriptorNode = FeatureTreeNode.newChildInstance("fontDescriptor", root);

			PBCreateNodeHelper.addNotEmptyNode("fontName", descriptor.getFontName(), descriptorNode);
			PBCreateNodeHelper.addNotEmptyNode("fontFamily", descriptor.getFontFamily(), descriptorNode);
			PBCreateNodeHelper.addNotEmptyNode("fontStretch", descriptor.getFontStretch(), descriptorNode);
			FeatureTreeNode.newChildInstanceWithValue("fontWeight", String.valueOf(descriptor.getFontWeight()), descriptorNode);
			FeatureTreeNode.newChildInstanceWithValue("fixedPitch", String.valueOf(descriptor.isFixedPitch()), descriptorNode);
			FeatureTreeNode.newChildInstanceWithValue("serif", String.valueOf(descriptor.isSerif()), descriptorNode);
			FeatureTreeNode.newChildInstanceWithValue("symbolic", String.valueOf(descriptor.isSymbolic()), descriptorNode);
			FeatureTreeNode.newChildInstanceWithValue("script", String.valueOf(descriptor.isScript()), descriptorNode);
			FeatureTreeNode.newChildInstanceWithValue("nonsymbolic", String.valueOf(descriptor.isNonSymbolic()), descriptorNode);
			FeatureTreeNode.newChildInstanceWithValue("italic", String.valueOf(descriptor.isItalic()), descriptorNode);
			FeatureTreeNode.newChildInstanceWithValue("allCap", String.valueOf(descriptor.isAllCap()), descriptorNode);
			FeatureTreeNode.newChildInstanceWithValue("smallCap", String.valueOf(descriptor.isScript()), descriptorNode);
			FeatureTreeNode.newChildInstanceWithValue("forceBold", String.valueOf(descriptor.isForceBold()), descriptorNode);
			PBCreateNodeHelper.addBoxFeature("fontBBox", descriptor.getFontBoundingBox(), descriptorNode);

			FeatureTreeNode.newChildInstanceWithValue("italicAngle", String.valueOf(descriptor.getItalicAngle()), descriptorNode);
			FeatureTreeNode.newChildInstanceWithValue("ascent", String.valueOf(descriptor.getAscent()), descriptorNode);
			FeatureTreeNode.newChildInstanceWithValue("descent", String.valueOf(descriptor.getDescent()), descriptorNode);
			FeatureTreeNode.newChildInstanceWithValue("leading", String.valueOf(descriptor.getLeading()), descriptorNode);
			FeatureTreeNode.newChildInstanceWithValue("capHeight", String.valueOf(descriptor.getCapHeight()), descriptorNode);
			FeatureTreeNode.newChildInstanceWithValue("xHeight", String.valueOf(descriptor.getXHeight()), descriptorNode);
			FeatureTreeNode.newChildInstanceWithValue("stemV", String.valueOf(descriptor.getStemV()), descriptorNode);
			FeatureTreeNode.newChildInstanceWithValue("stemH", String.valueOf(descriptor.getStemH()), descriptorNode);
			FeatureTreeNode.newChildInstanceWithValue("averageWidth", String.valueOf(descriptor.getAverageWidth()), descriptorNode);
			FeatureTreeNode.newChildInstanceWithValue("maxWidth", String.valueOf(descriptor.getMaxWidth()), descriptorNode);
			FeatureTreeNode.newChildInstanceWithValue("missingWidth", String.valueOf(descriptor.getMissingWidth()), descriptorNode);
			PBCreateNodeHelper.addNotEmptyNode("charSet", descriptor.getCharSet(), descriptorNode);

			PDStream file = descriptor.getFontFile();
			if (file == null) {
				file = descriptor.getFontFile2();
			}
			if (file == null) {
				file = descriptor.getFontFile3();
			}

			if (file != null) {
				FeatureTreeNode fileNode = FeatureTreeNode.newChildInstance("fontFile", descriptorNode);
				COSBase len1 = file.getStream().getDictionaryObject(COSName.LENGTH1);
				if (len1 instanceof COSInteger) {
					FeatureTreeNode.newChildInstanceWithValue("length1", String.valueOf(((COSInteger) len1).intValue()), fileNode);
				}
				COSBase len2 = file.getStream().getDictionaryObject(COSName.LENGTH2);
				if (len2 instanceof COSInteger) {
					FeatureTreeNode.newChildInstanceWithValue("length2", String.valueOf(((COSInteger) len2).intValue()), fileNode);
				}
				COSBase len3 = file.getStream().getDictionaryObject(COSName.getPDFName("Length3"));
				if (len3 instanceof COSInteger) {
					FeatureTreeNode.newChildInstanceWithValue("length3", String.valueOf(((COSInteger) len3).intValue()), fileNode);
				}
				COSBase subType = file.getStream().getDictionaryObject(COSName.SUBTYPE);
				if (subType instanceof COSName) {
					FeatureTreeNode.newChildInstanceWithValue("subtype", ((COSName) subType).getName(), fileNode);
				}
				PBCreateNodeHelper.parseMetadata(file.getMetadata(), "metadata", fileNode, collection);
			}
		}
	}

	private static void parseFloatMatrix(float[][] array, FeatureTreeNode parent) throws FeaturesTreeNodeException {
		for (int i = 0; i < array.length; ++i) {
			for (int j = 0; j < array.length - 1; ++j) {
				FeatureTreeNode element = FeatureTreeNode.newChildInstance("element", parent);
				element.addAttribute("row", String.valueOf(i));
				element.addAttribute("column", String.valueOf(j));
				element.addAttribute("value", String.valueOf(array[i][j]));
			}
		}
	}

	private static void parseIntList(List<Integer> array, FeatureTreeNode parent) throws FeaturesTreeNodeException {
		for (int i = 0; i < array.size(); ++i) {
			FeatureTreeNode element = FeatureTreeNode.newChildInstance("element", parent);
			element.addAttribute("number", String.valueOf(i));
			element.addAttribute("value", String.valueOf(array.get(i)));
		}
	}

	private void parseParents(FeatureTreeNode root) throws FeaturesTreeNodeException {
		if ((pageParent != null && !pageParent.isEmpty()) ||
				(extGStateParent != null && !extGStateParent.isEmpty()) ||
				(patternParent != null && !patternParent.isEmpty()) ||
				(xobjectParent != null && !xobjectParent.isEmpty()) ||
				(fontParent != null && !fontParent.isEmpty())) {
			FeatureTreeNode parents = FeatureTreeNode.newChildInstance("parents", root);

			PBCreateNodeHelper.parseIDSet(pageParent, "page", null, parents);
			PBCreateNodeHelper.parseIDSet(extGStateParent, "graphicsState", null, parents);
			PBCreateNodeHelper.parseIDSet(patternParent, "pattern", null, parents);
			PBCreateNodeHelper.parseIDSet(xobjectParent, "xobject", null, parents);
			PBCreateNodeHelper.parseIDSet(fontParent, "font", null, parents);
		}
	}

	private void parseResources(FeatureTreeNode root) throws FeaturesTreeNodeException {

		if ((extGStateChild != null && !extGStateChild.isEmpty()) ||
				(colorSpaceChild != null && !colorSpaceChild.isEmpty()) ||
				(patternChild != null && !patternChild.isEmpty()) ||
				(shadingChild != null && !shadingChild.isEmpty()) ||
				(xobjectChild != null && !xobjectChild.isEmpty()) ||
				(fontChild != null && !fontChild.isEmpty()) ||
				(procSetChild != null && !procSetChild.isEmpty()) ||
				(propertiesChild != null && !propertiesChild.isEmpty())) {
			FeatureTreeNode resources = FeatureTreeNode.newChildInstance("resources", root);

			PBCreateNodeHelper.parseIDSet(extGStateChild, "graphicsState", "graphicsStates", resources);
			PBCreateNodeHelper.parseIDSet(colorSpaceChild, "colorSpace", "colorSpaces", resources);
			PBCreateNodeHelper.parseIDSet(patternChild, "pattern", "patterns", resources);
			PBCreateNodeHelper.parseIDSet(shadingChild, "shading", "shadings", resources);
			PBCreateNodeHelper.parseIDSet(xobjectChild, "xobject", "xobjects", resources);
			PBCreateNodeHelper.parseIDSet(fontChild, "font", "fonts", resources);
			PBCreateNodeHelper.parseIDSet(procSetChild, "procSet", "procSets", resources);
			PBCreateNodeHelper.parseIDSet(propertiesChild, "propertiesDict", "propertiesDicts", resources);
		}
	}
}
