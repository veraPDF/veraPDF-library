package org.verapdf.features;

import org.verapdf.core.FeatureParsingException;
import org.verapdf.features.pb.tools.PBCreateNodeHelper;
import org.verapdf.features.tools.FeatureTreeNode;

import javax.xml.bind.DatatypeConverter;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

/**
 * @author Maksim Bezrukov
 */
public final class TestNodeGenerator {

	public static final String ENTRY = "entry";
	public static final String ID = "id";
	public static final String METADATA = "metadata";
	public static final String LLX = "llx";
	public static final String LLY = "lly";
	public static final String URX = "urx";
	public static final String URY = "ury";

	public static FeatureTreeNode getFontType0(String id, String pageParent, String gstParent, String baseFont, String descFont, FontDescriptorStructure descriptor) throws FeatureParsingException {
		FeatureTreeNode root = FeatureTreeNode.newRootInstance("font");
		root.addAttribute(ID, id);
		FeatureTreeNode parents = FeatureTreeNode.newChildInstance("parents", root);
		FeatureTreeNode page = FeatureTreeNode.newChildInstance("page", parents);
		page.addAttribute(ID, pageParent);
		FeatureTreeNode gstate = FeatureTreeNode.newChildInstance("page", parents);
		gstate.addAttribute(ID, gstParent);
		FeatureTreeNode.newChildInstanceWithValue("type", "Type0", root);
		PBCreateNodeHelper.addNotEmptyNode("baseFont", baseFont, root);
		FeatureTreeNode descs = FeatureTreeNode.newChildInstance("descendedFonts", root);
		FeatureTreeNode desc = FeatureTreeNode.newChildInstance("descendedFont", descs);
		desc.addAttribute(ID, descFont);
		descriptor.generateNode(root);
		return root;
	}

	public static FeatureTreeNode getTilingPattern() throws FeatureParsingException {
		FeatureTreeNode root = FeatureTreeNode.newRootInstance("pattern");
		root.addAttribute(ID, "ptrnIndir49");
		root.addAttribute("type", "tiling");
		FeatureTreeNode parents = FeatureTreeNode.newChildInstance("parents", root);
		FeatureTreeNode page = FeatureTreeNode.newChildInstance("page", parents);
		page.addAttribute(ID, "page1");
		FeatureTreeNode.newChildInstanceWithValue("paintType", "2", root);
		FeatureTreeNode.newChildInstanceWithValue("tilingType", "1", root);
		FeatureTreeNode bbox = FeatureTreeNode.newChildInstance("bbox", root);
		bbox.addAttribute(LLX, "0.0");
		bbox.addAttribute(LLY, "0.0");
		bbox.addAttribute(URX, "5.0");
		bbox.addAttribute(URY, "10.0");
		FeatureTreeNode.newChildInstanceWithValue("xStep", "5.0", root);
		FeatureTreeNode.newChildInstanceWithValue("yStep", "10.0", root);
		getStandartMatrix(root);
		FeatureTreeNode resources = FeatureTreeNode.newChildInstance("resources", root);
		FeatureTreeNode colorSpaces = FeatureTreeNode.newChildInstance("colorSpaces", resources);
		FeatureTreeNode clr2 = FeatureTreeNode.newChildInstance("colorSpace", colorSpaces);
		clr2.addAttribute(ID, "clrspDir19");
		FeatureTreeNode clr1 = FeatureTreeNode.newChildInstance("colorSpace", colorSpaces);
		clr1.addAttribute(ID, "clrspDir20");
		FeatureTreeNode xobjects = FeatureTreeNode.newChildInstance("xobjects", resources);
		FeatureTreeNode xobj2 = FeatureTreeNode.newChildInstance("xobject", xobjects);
		xobj2.addAttribute(ID, "xobjIndir60");
		FeatureTreeNode xobj1 = FeatureTreeNode.newChildInstance("xobject", xobjects);
		xobj1.addAttribute(ID, "xobjIndir56");
		return root;
	}

	public static FeatureTreeNode getShadingPattern() throws FeatureParsingException {
		FeatureTreeNode root = FeatureTreeNode.newRootInstance("pattern");
		root.addAttribute(ID, "ptrnIndir50");
		root.addAttribute("type", "shading");
		FeatureTreeNode parents = FeatureTreeNode.newChildInstance("parents", root);
		FeatureTreeNode page = FeatureTreeNode.newChildInstance("page", parents);
		page.addAttribute(ID, "page1");
		FeatureTreeNode font = FeatureTreeNode.newChildInstance("font", parents);
		font.addAttribute(ID, "fntIndir91");
		FeatureTreeNode shading = FeatureTreeNode.newChildInstance("shading", root);
		shading.addAttribute(ID, "shdngIndir52");
		getStandartMatrix(root);
		FeatureTreeNode gst = FeatureTreeNode.newChildInstance("graphicsState", root);
		gst.addAttribute(ID, "exGStIndir93");
		return root;
	}

	public static void getStandartMatrix(FeatureTreeNode root) throws FeatureParsingException {
		FeatureTreeNode matr = FeatureTreeNode.newChildInstance("matrix", root);
		addElement("1", "1", "1.0", matr);
		addElement("2", "1", "0.0", matr);
		addElement("1", "2", "0.0", matr);
		addElement("2", "2", "1.0", matr);
		addElement("1", "3", "0.0", matr);
		addElement("2", "3", "0.0", matr);
	}

	public static void addElement(String column, String row, String value, FeatureTreeNode parent) throws FeatureParsingException {
		FeatureTreeNode element = FeatureTreeNode.newChildInstance("element", parent);
		element.addAttribute("column", column);
		element.addAttribute("row", row);
		element.addAttribute("value", value);
	}

	public static FeatureTreeNode getShading() throws FeatureParsingException {
		FeatureTreeNode root = FeatureTreeNode.newRootInstance("shading");
		root.addAttribute(ID, "shdngIndir52");
		FeatureTreeNode parents = FeatureTreeNode.newChildInstance("parents", root);
		FeatureTreeNode page = FeatureTreeNode.newChildInstance("page", parents);
		page.addAttribute(ID, "page1");
		FeatureTreeNode pattern = FeatureTreeNode.newChildInstance("pattern", parents);
		pattern.addAttribute(ID, "ptrnIndir50");
		FeatureTreeNode font = FeatureTreeNode.newChildInstance("font", parents);
		font.addAttribute(ID, "fntIndir91");
		FeatureTreeNode.newChildInstanceWithValue("shadingType", "2", root);
		FeatureTreeNode clr = FeatureTreeNode.newChildInstance("colorSpace", root);
		clr.addAttribute(ID, "devrgb");
		FeatureTreeNode bbox = FeatureTreeNode.newChildInstance("bbox", root);
		bbox.addAttribute(LLX, "0.0");
		bbox.addAttribute(LLY, "0.0");
		bbox.addAttribute(URX, "400.0");
		bbox.addAttribute(URY, "400.0");
		FeatureTreeNode.newChildInstanceWithValue("antiAlias", "false", root);
		return root;
	}

	public static FeatureTreeNode getFailedXObject(String id, String errorid) throws FeatureParsingException {
		FeatureTreeNode root = FeatureTreeNode.newRootInstance("xobject");
		root.addAttribute(ID, id);
		root.addAttribute("errorID", errorid);
		return root;
	}

	public static FeatureTreeNode getProperties() throws FeatureParsingException {
		FeatureTreeNode root = FeatureTreeNode.newRootInstance("propertiesDict");
		root.addAttribute(ID, "propDir0");
		FeatureTreeNode parents = FeatureTreeNode.newChildInstance("parents", root);
		FeatureTreeNode page = FeatureTreeNode.newChildInstance("page", parents);
		page.addAttribute(ID, "page1");
		return root;
	}

	public static FeatureTreeNode getGraphicsState(String id, String page, String pattern, String xobject1, String xobject2, String font, String transparency, String strokeAdjustment,
												   String overprintForStroke, String overprintForFill, String fontChild) throws FeatureParsingException {
		FeatureTreeNode root = FeatureTreeNode.newRootInstance("graphicsState");
		root.addAttribute(ID, id);
		FeatureTreeNode parents = FeatureTreeNode.newChildInstance("parents", root);
		if (page != null) {
			FeatureTreeNode pageNode = FeatureTreeNode.newChildInstance("page", parents);
			pageNode.addAttribute(ID, page);
		}
		if (pattern != null) {
			FeatureTreeNode patternNode = FeatureTreeNode.newChildInstance("pattern", parents);
			patternNode.addAttribute(ID, pattern);
		}
		if (xobject1 != null) {
			FeatureTreeNode xobjectNode = FeatureTreeNode.newChildInstance("xobject", parents);
			xobjectNode.addAttribute(ID, xobject1);
		}
		if (xobject2 != null) {
			FeatureTreeNode xobjectNode = FeatureTreeNode.newChildInstance("xobject", parents);
			xobjectNode.addAttribute(ID, xobject2);
		}
		if (font != null) {
			FeatureTreeNode fontNode = FeatureTreeNode.newChildInstance("font", parents);
			fontNode.addAttribute(ID, font);
		}


		FeatureTreeNode.newChildInstanceWithValue("transparency", transparency, root);
		FeatureTreeNode.newChildInstanceWithValue("strokeAdjustment", strokeAdjustment, root);
		FeatureTreeNode.newChildInstanceWithValue("overprintForStroke", overprintForStroke, root);
		FeatureTreeNode.newChildInstanceWithValue("overprintForFill", overprintForFill, root);

		if (fontChild != null) {
			FeatureTreeNode res = FeatureTreeNode.newChildInstance("resources", root);
			FeatureTreeNode fons = FeatureTreeNode.newChildInstance("fonts", res);
			FeatureTreeNode fon = FeatureTreeNode.newChildInstance("font", fons);
			fon.addAttribute(ID, fontChild);
		}
		return root;
	}

	public static FeatureTreeNode getPage() throws FeatureParsingException, URISyntaxException, FileNotFoundException {
		FeatureTreeNode root = FeatureTreeNode.newRootInstance("page");
		root.addAttribute(ID, "page1");
		root.addAttribute("orderNumber", "1");

		addBox("mediaBox", root);
		addBox("cropBox", root);
		addBox("trimBox", root);
		addBox("bleedBox", root);
		addBox("artBox", root);
		FeatureTreeNode.newChildInstanceWithValue("rotation", "0", root);
		FeatureTreeNode.newChildInstanceWithValue("scaling", "75.0", root);
		FeatureTreeNode thumb = FeatureTreeNode.newChildInstance("thumbnail", root);
		thumb.addAttribute(ID, "xobjIndir126");
		FeatureTreeNode.newChildInstanceWithValue("metadata", DatatypeConverter.printHexBinary(getMetadataBytesFromFile("/page1_metadata_bytes.txt")), root);

		List<String> annotations = new ArrayList<>();
		annotations.add("annotIndir13");
		annotations.add("annotIndir42");
		annotations.add("annotIndir37");
		annotations.add("annotIndir38");
		annotations.add("annotIndir39");
		annotations.add("annotIndir41");
		annotations.add("annotIndir40");
		makeList("annotation", annotations, root);

		FeatureTreeNode resources = FeatureTreeNode.newChildInstance("resources", root);

		List<String> graphicsStates = new ArrayList<>();
		graphicsStates.add("exGStIndir47");
		makeList("graphicsState", graphicsStates, resources);

		List<String> colorSpaces = new ArrayList<>();

		colorSpaces.add("clrspDir10");
		colorSpaces.add("clrspDir11");
		colorSpaces.add("devcmyk");
		colorSpaces.add("clrspDir18");
		colorSpaces.add("clrspDir4");
		colorSpaces.add("clrspDir5");
		colorSpaces.add("clrspDir6");
		colorSpaces.add("clrspDir17");
		colorSpaces.add("clrspDir14");
		colorSpaces.add("clrspDir15");
		colorSpaces.add("clrspDir12");
		colorSpaces.add("clrspDir2");
		colorSpaces.add("devgray");
		colorSpaces.add("clrspDir13");
		colorSpaces.add("devrgb");
		colorSpaces.add("clrspDir7");
		colorSpaces.add("clrspDir9");

		makeList("colorSpace", colorSpaces, resources);

		List<String> patterns = new ArrayList<>();
		patterns.add("ptrnIndir49");
		patterns.add("ptrnIndir50");
		makeList("pattern", patterns, resources);

		List<String> shadings = new ArrayList<>();
		shadings.add("shdngIndir52");
		makeList("shading", shadings, resources);

		List<String> xobject = new ArrayList<>();
		xobject.add("xobjIndir60");
		xobject.add("xobjIndir58");
		xobject.add("xobjIndir59");
		xobject.add("xobjIndir62");
		xobject.add("xobjIndir56");
		xobject.add("xobjIndir61");
		xobject.add("xobjIndir57");
		xobject.add("xobjIndir63");
		xobject.add("xobjIndir55");
		makeList("xobject", xobject, resources);

		List<String> fonts = new ArrayList<>();
		fonts.add("fntIndir90");
		fonts.add("fntIndir91");
		fonts.add("fntIndir92");
		fonts.add("fntIndir88");
		fonts.add("fntIndir89");
		makeList("font", fonts, resources);

		List<String> propertiesDicts = new ArrayList<>();
		propertiesDicts.add("propDir0");
		makeList("propertiesDict", propertiesDicts, resources);

		return root;
	}

	public static void makeList(String name, List<String> values, FeatureTreeNode parent) throws FeatureParsingException {
		FeatureTreeNode head = FeatureTreeNode.newChildInstance(name + "s", parent);
		for (String el : values) {
			FeatureTreeNode element = FeatureTreeNode.newChildInstance(name, head);
			element.addAttribute(ID, el);
		}
	}

	public static void addBox(String name, FeatureTreeNode parent) throws FeatureParsingException {
		FeatureTreeNode box = FeatureTreeNode.newChildInstance(name, parent);
		box.addAttribute(LLX, "0.0");
		box.addAttribute(LLY, "0.0");
		box.addAttribute(URX, "499.977");
		box.addAttribute(URY, "499.977");
	}

	public static FeatureTreeNode getAnnotation(String id, String parentPage, String parentAnnotation,
												String subtype, String llx, String lly, String urx, String ury,
												String contents, String annotationName, String modifiedDate, Set<String> xobj,
												String popup, String red, String green, String blue, String kayan,
												String invisible, String hidden, String print, String noZoom,
												String noRotate, String noView, String readOnly, String locked,
												String toggleNoView, String lockedContents) throws FeatureParsingException {
		FeatureTreeNode root = FeatureTreeNode.newRootInstance("annotation");
		root.addAttribute(ID, id);
		if (parentPage != null || parentAnnotation != null) {
			FeatureTreeNode parents = FeatureTreeNode.newChildInstance("parents", root);
			if (parentPage != null) {
				FeatureTreeNode page = FeatureTreeNode.newChildInstance("page", parents);
				page.addAttribute(ID, parentPage);
			}
			if (parentAnnotation != null) {
				FeatureTreeNode annot = FeatureTreeNode.newChildInstance("annotation", parents);
				annot.addAttribute(ID, parentAnnotation);
			}
		}
		PBCreateNodeHelper.addNotEmptyNode("subType", subtype, root);
		FeatureTreeNode rec = FeatureTreeNode.newChildInstance("rectangle", root);
		rec.addAttribute(LLX, llx);
		rec.addAttribute(LLY, lly);
		rec.addAttribute(URX, urx);
		rec.addAttribute(URY, ury);
		PBCreateNodeHelper.addNotEmptyNode("contents", contents, root);
		PBCreateNodeHelper.addNotEmptyNode("annotationName", annotationName, root);
		PBCreateNodeHelper.addNotEmptyNode("modifiedDate", modifiedDate, root);

		if (xobj != null && !xobj.isEmpty()) {
			FeatureTreeNode resources = FeatureTreeNode.newChildInstance("resources", root);
			for (String objID : xobj) {
				FeatureTreeNode node = FeatureTreeNode.newChildInstance("xobject", resources);
				node.addAttribute(ID, objID);
			}
		}
		if (popup != null) {
			FeatureTreeNode pop = FeatureTreeNode.newChildInstance("popup", root);
			pop.addAttribute(ID, popup);
		}

		if (red != null && green != null && blue != null && kayan != null) {
			FeatureTreeNode color = FeatureTreeNode.newChildInstance("color", root);
			color.addAttribute("cyan", red);
			color.addAttribute("magenta", green);
			color.addAttribute("yellow", blue);
			color.addAttribute("black", blue);
		} else if (red != null && green != null && blue != null) {
			FeatureTreeNode color = FeatureTreeNode.newChildInstance("color", root);
			color.addAttribute("red", red);
			color.addAttribute("green", green);
			color.addAttribute("blue", blue);
		} else if (red != null) {
			FeatureTreeNode color = FeatureTreeNode.newChildInstance("color", root);
			color.addAttribute("gray", red);
		}

		FeatureTreeNode.newChildInstanceWithValue("invisible", invisible, root);
		FeatureTreeNode.newChildInstanceWithValue("hidden", hidden, root);
		FeatureTreeNode.newChildInstanceWithValue("print", print, root);
		FeatureTreeNode.newChildInstanceWithValue("noZoom", noZoom, root);
		FeatureTreeNode.newChildInstanceWithValue("noRotate", noRotate, root);
		FeatureTreeNode.newChildInstanceWithValue("noView", noView, root);
		FeatureTreeNode.newChildInstanceWithValue("readOnly", readOnly, root);
		FeatureTreeNode.newChildInstanceWithValue("locked", locked, root);
		FeatureTreeNode.newChildInstanceWithValue("toggleNoView", toggleNoView, root);
		FeatureTreeNode.newChildInstanceWithValue("lockedContents", lockedContents, root);

		return root;
	}

	public static FeatureTreeNode getOutlines() throws FeatureParsingException {
		FeatureTreeNode root = FeatureTreeNode.newRootInstance("outlines");
		FeatureTreeNode out1 = FeatureTreeNode.newChildInstance("outline", root);
		makeOutline("1 - COLOR", "1.0", "0.0", "0.0", "false", "false", out1);
		FeatureTreeNode out1_1 = FeatureTreeNode.newChildInstance("outline", out1);
		makeOutline("1.1", "0.0", "0.0", "1.0", "false", "false", out1_1);
		FeatureTreeNode out2 = FeatureTreeNode.newChildInstance("outline", root);
		makeOutline("2 - ITALIC", "0.0", "0.0", "0.0", "true", "false", out2);
		FeatureTreeNode out2_2 = FeatureTreeNode.newChildInstance("outline", out2);
		makeOutline("2.2", "0.0", "0.0", "0.0", "true", "false", out2_2);
		FeatureTreeNode out2_2_1 = FeatureTreeNode.newChildInstance("outline", out2_2);
		makeOutline("2.2.1", "0.0", "0.0", "0.0", "true", "false", out2_2_1);
		FeatureTreeNode out2_2_2 = FeatureTreeNode.newChildInstance("outline", out2_2);
		makeOutline("2.2.2", "0.0", "0.0", "0.0", "true", "false", out2_2_2);
		FeatureTreeNode out2_2_2_1 = FeatureTreeNode.newChildInstance("outline", out2_2_2);
		makeOutline("2.2.2.1", "0.0", "0.0", "0.0", "true", "false", out2_2_2_1);
		FeatureTreeNode out2_1 = FeatureTreeNode.newChildInstance("outline", out2);
		makeOutline("2.1", "0.0", "0.0", "0.0", "true", "false", out2_1);
		FeatureTreeNode out3 = FeatureTreeNode.newChildInstance("outline", root);
		makeOutline("3 - BOLD", "0.0", "0.0", "0.0", "false", "true", out3);
		FeatureTreeNode out4 = FeatureTreeNode.newChildInstance("outline", root);
		makeOutline("4", "0.0", "0.0", "0.0", "false", "false", out4);
		return root;
	}

	public static void makeOutline(String title, String red, String green, String blue, String italic, String bold, FeatureTreeNode root) throws FeatureParsingException {
		FeatureTreeNode.newChildInstanceWithValue("title", title, root);
		FeatureTreeNode color = FeatureTreeNode.newChildInstance("color", root);
		color.addAttribute("red", red);
		color.addAttribute("green", green);
		color.addAttribute("blue", blue);
		FeatureTreeNode style = FeatureTreeNode.newChildInstance("style", root);
		style.addAttribute("italic", italic);
		style.addAttribute("bold", bold);
	}

	public static FeatureTreeNode getOutputIntent() throws FeatureParsingException {
		FeatureTreeNode root = FeatureTreeNode.newRootInstance("outputIntent");
		root.addAttribute(ID, "outIntDir0");
		FeatureTreeNode.newChildInstanceWithValue("subtype", "GTS_PDFA1", root);
		FeatureTreeNode.newChildInstanceWithValue("outputCondition", "SomeOutputCondition", root);
		FeatureTreeNode.newChildInstanceWithValue("outputConditionIdentifier", "Apple RGB", root);
		FeatureTreeNode.newChildInstanceWithValue("registryName", "fxqn:/us/va/reston/cnri/ietf/24/asdf%*.fred", root);
		FeatureTreeNode.newChildInstanceWithValue("info", "RGB", root);
		FeatureTreeNode dest = FeatureTreeNode.newChildInstance("destOutputIntent", root);
		dest.addAttribute(ID, "iccProfileIndir19");
		return root;
	}

	public static FeatureTreeNode getICCProfile(String id, Set<String> outInts, Set<String> iccBaseds, String version,
												String cmmType, String dataColorSpace, String creator, String creationDate,
												String defaultRenderingIntent, String copyright, String description,
												String profileId, String deviceModel, String deviceManufacturer, byte[] metadata) throws FeatureParsingException {
		FeatureTreeNode root = FeatureTreeNode.newRootInstance("iccProfile");
		root.addAttribute(ID, id);

		if ((outInts != null && !outInts.isEmpty()) || (iccBaseds != null && !iccBaseds.isEmpty())) {
			FeatureTreeNode parents = FeatureTreeNode.newChildInstance("parents", root);
			if (outInts != null) {
				for (String out : outInts) {
					FeatureTreeNode outNode = FeatureTreeNode.newChildInstance("outputIntent", parents);
					outNode.addAttribute(ID, out);
				}
			}
			if (iccBaseds != null) {
				for (String icc : iccBaseds) {
					FeatureTreeNode iccNode = FeatureTreeNode.newChildInstance("iccBased", parents);
					iccNode.addAttribute(ID, icc);
				}
			}
		}

		PBCreateNodeHelper.addNotEmptyNode("version", version, root);
		PBCreateNodeHelper.addNotEmptyNode("cmmType", cmmType, root);
		PBCreateNodeHelper.addNotEmptyNode("dataColorSpace", dataColorSpace, root);
		PBCreateNodeHelper.addNotEmptyNode("creator", creator, root);
		PBCreateNodeHelper.addNotEmptyNode("creationDate", creationDate, root);
		PBCreateNodeHelper.addNotEmptyNode("defaultRenderingIntent", defaultRenderingIntent, root);
		PBCreateNodeHelper.addNotEmptyNode("copyright", copyright, root);
		PBCreateNodeHelper.addNotEmptyNode("description", description, root);
		PBCreateNodeHelper.addNotEmptyNode("profileId", profileId, root);
		PBCreateNodeHelper.addNotEmptyNode("deviceModel", deviceModel, root);
		PBCreateNodeHelper.addNotEmptyNode("deviceManufacturer", deviceManufacturer, root);
		if (metadata != null) {
			FeatureTreeNode.newChildInstanceWithValue("metadata", DatatypeConverter.printHexBinary(metadata), root);
		}

		return root;
	}

	public static FeatureTreeNode getEmbeddedFileNode(String id, String fileName, String description, String subtype,
													  String filter, String creationDate, String modDate, String checkSum,
													  String size) throws FeatureParsingException {
		FeatureTreeNode root = FeatureTreeNode.newRootInstance("embeddedFile");
		root.addAttribute(ID, id);
		FeatureTreeNode.newChildInstanceWithValue("fileName", fileName, root);
		FeatureTreeNode.newChildInstanceWithValue("description", description, root);
		FeatureTreeNode.newChildInstanceWithValue("subtype", subtype, root);
		FeatureTreeNode.newChildInstanceWithValue("filter", filter, root);
		FeatureTreeNode.newChildInstanceWithValue("creationDate", creationDate, root);
		FeatureTreeNode.newChildInstanceWithValue("modDate", modDate, root);
		FeatureTreeNode.newChildInstanceWithValue("checkSum", checkSum, root);
		FeatureTreeNode.newChildInstanceWithValue("size", size, root);
		return root;
	}

	public static FeatureTreeNode getLowLvlInfo() throws FeatureParsingException {
		FeatureTreeNode lli = FeatureTreeNode.newRootInstance("lowLevelInfo");
		FeatureTreeNode.newChildInstanceWithValue("indirectObjectsNumber", "129", lli);
		FeatureTreeNode docID = FeatureTreeNode.newChildInstance("documentId", lli);
		docID.addAttribute("modificationId", "295EBB0E08D32644B7E5C1825F15AD3A");
		docID.addAttribute("creationId", "85903F3A2C43B1DA24E486CD15B8154E");
		FeatureTreeNode filters = FeatureTreeNode.newChildInstance("filters", lli);
		addFilter("FlateDecode", filters);
		addFilter("ASCIIHexDecode", filters);
		addFilter("ASCII85Decode", filters);
		addFilter("CCITTFaxDecode", filters);
		addFilter("DCTDecode", filters);
		return lli;
	}

	public static void addFilter(String name, FeatureTreeNode parent) throws FeatureParsingException {
		FeatureTreeNode filter = FeatureTreeNode.newChildInstance("filter", parent);
		filter.addAttribute("name", name);
	}

	public static FeatureTreeNode getInfDictNode() throws FeatureParsingException {
		FeatureTreeNode infDict = FeatureTreeNode.newRootInstance("informationDict");
		addEntry("Title", "SomeTitle", infDict);
		addEntry("Author", "SomeAuthor", infDict);
		addEntry("Subject", "SomeSubject", infDict);
		addEntry("Keywords", "Some Keywords", infDict);
		addEntry("Creator", "SomeCreator", infDict);
		addEntry("Producer", "SomeProducer", infDict);
		addEntry("CreationDate", "2015-08-22T14:04:45.000+03:00", infDict);
		addEntry("ModDate", "2015-08-31T14:05:31.000+03:00", infDict);
		addEntry("Trapped", "False", infDict);
		addEntry("CustomEntry", "CustomValue", infDict);
		addEntry("SecondCustomEntry", "SomeCustomValue", infDict);
		return infDict;
	}

	public static FeatureTreeNode getMetadataNode() throws FeatureParsingException, FileNotFoundException, URISyntaxException {
		return FeatureTreeNode.newRootInstanceWIthValue(METADATA, DatatypeConverter.printHexBinary(getMetadataBytesFromFile("/metadata_bytes.txt")));
	}

	public static byte[] getMetadataBytesFromFile(String path) throws URISyntaxException, FileNotFoundException {
		Scanner scan = new Scanner(new File(getSystemIndependentPath(path)));
		int n = scan.nextInt();
		byte[] res = new byte[n];
		for (int i = 0; scan.hasNextInt(); ++i) {
			res[i] = (byte) scan.nextInt();
		}
		return res;
	}

	public static void addEntry(String name, String value, FeatureTreeNode parent) throws FeatureParsingException {
		FeatureTreeNode entry = FeatureTreeNode.newChildInstanceWithValue(ENTRY, value, parent);
		entry.addAttribute("key", name);
	}

	public static String getSystemIndependentPath(String path) throws URISyntaxException {
		URL resourceUrl = ClassLoader.class.getResource(path);
		Path resourcePath = Paths.get(resourceUrl.toURI());
		return resourcePath.toString();
	}

	public class FontDescriptorStructure {
		public String fontName = null;
		public String fontFamily = null;
		public String fontStretch = null;
		public String fontWeight = null;
		public String fixedPitch = null;
		public String serif = null;
		public String symbolic = null;
		public String script = null;
		public String nonsymbolic = null;
		public String italic = null;
		public String allCap = null;
		public String smallCap = null;
		public String forceBold = null;
		public String fontBBox_llx = null;
		public String fontBBox_lly = null;
		public String fontBBox_urx = null;
		public String fontBBox_ury = null;
		public String italicAngle = null;
		public String ascent = null;
		public String descent = null;
		public String leading = null;
		public String capHeight = null;
		public String xHeight = null;
		public String stemV = null;
		public String stemH = null;
		public String averageWidth = null;
		public String maxWidth = null;
		public String missingWidth = null;

		public FontDescriptorStructure() {
		}

		public FeatureTreeNode generateNode(FeatureTreeNode parent) throws FeatureParsingException {
			FeatureTreeNode root = FeatureTreeNode.newChildInstance("fontDescriptor", parent);

			PBCreateNodeHelper.addNotEmptyNode("fontName", fontName, root);
			PBCreateNodeHelper.addNotEmptyNode("fontFamily", fontFamily, root);
			PBCreateNodeHelper.addNotEmptyNode("fontStretch", fontStretch, root);
			PBCreateNodeHelper.addNotEmptyNode("fontWeight", fontWeight, root);
			PBCreateNodeHelper.addNotEmptyNode("fixedPitch", fixedPitch, root);
			PBCreateNodeHelper.addNotEmptyNode("serif", serif, root);
			PBCreateNodeHelper.addNotEmptyNode("symbolic", symbolic, root);
			PBCreateNodeHelper.addNotEmptyNode("script", script, root);
			PBCreateNodeHelper.addNotEmptyNode("nonsymbolic", nonsymbolic, root);
			PBCreateNodeHelper.addNotEmptyNode("italic", italic, root);
			PBCreateNodeHelper.addNotEmptyNode("allCap", allCap, root);
			PBCreateNodeHelper.addNotEmptyNode("smallCap", smallCap, root);
			PBCreateNodeHelper.addNotEmptyNode("forceBold", forceBold, root);

			FeatureTreeNode bbox = FeatureTreeNode.newChildInstance("fontBBox", root);
			bbox.addAttribute(LLX, fontBBox_llx);
			bbox.addAttribute(LLY, fontBBox_lly);
			bbox.addAttribute(URX, fontBBox_urx);
			bbox.addAttribute(URY, fontBBox_ury);

			PBCreateNodeHelper.addNotEmptyNode("italicAngle", italicAngle, root);
			PBCreateNodeHelper.addNotEmptyNode("ascent", ascent, root);
			PBCreateNodeHelper.addNotEmptyNode("descent", descent, root);
			PBCreateNodeHelper.addNotEmptyNode("leading", leading, root);
			PBCreateNodeHelper.addNotEmptyNode("capHeight", capHeight, root);
			PBCreateNodeHelper.addNotEmptyNode("xHeight", xHeight, root);
			PBCreateNodeHelper.addNotEmptyNode("stemV", stemV, root);
			PBCreateNodeHelper.addNotEmptyNode("stemH", stemH, root);
			PBCreateNodeHelper.addNotEmptyNode("averageWidth", averageWidth, root);
			PBCreateNodeHelper.addNotEmptyNode("maxWidth", maxWidth, root);
			PBCreateNodeHelper.addNotEmptyNode("missingWidth", missingWidth, root);
			return parent;
		}
	}
}
