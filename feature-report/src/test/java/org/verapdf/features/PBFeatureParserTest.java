package org.verapdf.features;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.junit.BeforeClass;
import org.junit.Test;
import org.verapdf.exceptions.featurereport.FeaturesTreeNodeException;
import org.verapdf.features.pb.PBFeatureParser;
import org.verapdf.features.pb.objects.*;
import org.verapdf.features.pb.tools.PBCreateNodeHelper;
import org.verapdf.features.tools.FeatureTreeNode;
import org.verapdf.features.tools.FeaturesCollection;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Maksim Bezrukov
 */
public class PBFeatureParserTest {

	private static FeaturesCollection collection;

	private static final String ENTRY = "entry";
	private static final String ID = "id";
	private static final String METADATA = "metadata";
	private static final String LLX = "llx";
	private static final String LLY = "lly";
	private static final String URX = "urx";
	private static final String URY = "ury";

	@BeforeClass
	public static void before() throws URISyntaxException, IOException {
		File pdf = new File(getSystemIndependentPath("/FR.pdf"));
		PDDocument document = PDDocument.load(pdf, false, true);
		collection = PBFeatureParser.getFeaturesCollection(document);
	}

	@Test
	public void featuresObjectsTypeTest() {
		assertEquals(FeaturesObjectTypesEnum.ANNOTATION, new PBAnnotationFeaturesObject(null, null, null, null, null, null).getType());
		assertEquals(FeaturesObjectTypesEnum.COLORSPACE, new PBColorSpaceFeaturesObject(null, null, null, null, null, null, null, null, null, null).getType());
		assertEquals(FeaturesObjectTypesEnum.DOCUMENT_SECURITY, new PBDocSecurityFeaturesObject(null).getType());
		assertEquals(FeaturesObjectTypesEnum.EMBEDDED_FILE, new PBEmbeddedFileFeaturesObject(null, 0).getType());
		assertEquals(FeaturesObjectTypesEnum.EXT_G_STATE, new PBExtGStateFeaturesObject(null, null, null, null, null, null, null).getType());
		assertEquals(FeaturesObjectTypesEnum.FONT, new PBFontFeaturesObject(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null).getType());
		assertEquals(FeaturesObjectTypesEnum.FORM_XOBJECT, new PBFormXObjectFeaturesObject(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null).getType());
		assertEquals(FeaturesObjectTypesEnum.ICCPROFILE, new PBICCProfileFeaturesObject(null, null, null, null).getType());
		assertEquals(FeaturesObjectTypesEnum.IMAGE_XOBJECT, new PBImageXObjectFeaturesObject(null, null, null, null, null, null, null, null, null, null).getType());
		assertEquals(FeaturesObjectTypesEnum.INFORMATION_DICTIONARY, new PBInfoDictFeaturesObject(null).getType());
		assertEquals(FeaturesObjectTypesEnum.LOW_LEVEL_INFO, new PBLowLvlInfoFeaturesObject(null).getType());
		assertEquals(FeaturesObjectTypesEnum.METADATA, new PBMetadataFeaturesObject(null).getType());
		assertEquals(FeaturesObjectTypesEnum.OUTLINES, new PBOutlinesFeaturesObject(null).getType());
		assertEquals(FeaturesObjectTypesEnum.OUTPUTINTENT, new PBOutputIntentsFeaturesObject(null, null, null).getType());
		assertEquals(FeaturesObjectTypesEnum.PAGE, new PBPageFeaturesObject(null, null, null, null, null, null, null, null, null, null, null, 0).getType());
		assertEquals(FeaturesObjectTypesEnum.PROCSET, new PBProcSetFeaturesObject(null, null, null, null, null, null).getType());
		assertEquals(FeaturesObjectTypesEnum.PROPERTIES, new PBPropertiesDictFeaturesObject(null, null, null, null, null, null).getType());
		assertEquals(FeaturesObjectTypesEnum.SHADING, new PBShadingFeaturesObject(null, null, null, null, null, null, null).getType());
		assertEquals(FeaturesObjectTypesEnum.PATTERN, new PBShadingPatternFeaturesObject(null, null, null, null, null, null, null, null).getType());
		assertEquals(FeaturesObjectTypesEnum.PATTERN, new PBTilingPatternFeaturesObject(null, null, null, null, null, null, null, null, null, null, null, null, null, null).getType());
	}

	@Test
	public void testInfoDict() throws FeaturesTreeNodeException {
		assertEquals(1, collection.getFeatureTreesForType(FeaturesObjectTypesEnum.INFORMATION_DICTIONARY).size());
		assertTrue(collection.getFeatureTreesForType(FeaturesObjectTypesEnum.INFORMATION_DICTIONARY).contains(getInfDictNode()));
	}

	@Test
	public void testMetadata() throws FeaturesTreeNodeException, FileNotFoundException, URISyntaxException {
		assertEquals(1, collection.getFeatureTreesForType(FeaturesObjectTypesEnum.METADATA).size());
		assertTrue(collection.getFeatureTreesForType(FeaturesObjectTypesEnum.METADATA).contains(getMetadataNode()));
	}

	@Test
	public void testLowLvlInfo() throws FeaturesTreeNodeException {
		assertEquals(1, collection.getFeatureTreesForType(FeaturesObjectTypesEnum.LOW_LEVEL_INFO).size());
		assertTrue(collection.getFeatureTreesForType(FeaturesObjectTypesEnum.LOW_LEVEL_INFO).contains(getLowLvlInfo()));
	}

	@Test
	public void testEmbeddedFiles() throws FeaturesTreeNodeException {
		assertEquals(4, collection.getFeatureTreesForType(FeaturesObjectTypesEnum.EMBEDDED_FILE).size());
		assertTrue(collection.getFeatureTreesForType(FeaturesObjectTypesEnum.EMBEDDED_FILE).contains(getEmbeddedFileNode("file1", "1.txt", "",
				"text/plain", "FlateDecode", "2015-08-31T13:33:43.000+03:00", "2015-08-31T13:20:39.000Z", "Ô˛„Ù‘\u0000²\u0004é•\tŸìøB~", "0")));
		assertTrue(collection.getFeatureTreesForType(FeaturesObjectTypesEnum.EMBEDDED_FILE).contains(getEmbeddedFileNode("file2", "Arist.jpg", "",
				"image/jpeg", "FlateDecode", "2015-08-31T13:33:33.000+03:00", "2014-08-15T17:17:58.000Z", "ù•8r‚‰$ŠKåŒêlŸm}", "26862")));
		assertTrue(collection.getFeatureTreesForType(FeaturesObjectTypesEnum.EMBEDDED_FILE).contains(getEmbeddedFileNode("file3", "XMP - 8.xml", "",
				"text/xml", "FlateDecode", "2015-08-31T13:33:38.000+03:00", "2015-08-20T12:24:50.000Z", "\u0006\u0005¼ä\u0017Uw\r⁄©>ñ8\u000EnÔ", "876")));
		assertTrue(collection.getFeatureTreesForType(FeaturesObjectTypesEnum.EMBEDDED_FILE).contains(getEmbeddedFileNode("file4", "fox_1.jpg", "Some Description for embedded file",
				"image/jpeg", "FlateDecode", "2015-08-22T14:01:19.000+03:00", "2014-09-08T12:01:07.000Z", "ËÓþVf\u0007ç`ºŁåk\u0015?A\r", "67142")));
	}

	@Test
	public void testICCProfiles() throws FeaturesTreeNodeException, FileNotFoundException, URISyntaxException {
		assertEquals(5, collection.getFeatureTreesForType(FeaturesObjectTypesEnum.ICCPROFILE).size());
		Set<String> outInts19 = new HashSet<>();
		outInts19.add("outIntDir0");
		assertTrue(collection.getFeatureTreesForType(FeaturesObjectTypesEnum.ICCPROFILE).contains(getICCProfile("iccProfileIndir19",
				outInts19, null, "2.1.0", "ADBE", "RGB ", "ADBE", "2000-08-11T19:52:24.000+03:00", null, "Copyright 2000 Adobe Systems Incorporated",
				"Apple RGB", null, null, "none", getMetadataBytesFromFile("/iccprofile19_metadata_bytes.txt"))));
		Set<String> iccbsds81 = new HashSet<>();
		iccbsds81.add("clrspDir9");
		assertTrue(collection.getFeatureTreesForType(FeaturesObjectTypesEnum.ICCPROFILE).contains(getICCProfile("iccProfileIndir81",
				null, iccbsds81, "2.1.0", "ADBE", "RGB ", "ADBE", "2000-08-11T19:54:18.000+03:00", null, "Copyright 2000 Adobe Systems Incorporated",
				"PAL/SECAM", null, null, "none", null)));
		Set<String> iccbsds84 = new HashSet<>();
		iccbsds84.add("clrspDir12");
		iccbsds84.add("clrspDir22");
		iccbsds84.add("clrspDir21");
		assertTrue(collection.getFeatureTreesForType(FeaturesObjectTypesEnum.ICCPROFILE).contains(getICCProfile("iccProfileIndir84",
				null, iccbsds84, "2.2.0", "appl", "RGB ", "appl", "2000-08-13T16:06:07.000+03:00", "\u0000\u0000\u0000\u0001", "Copyright 1998 - 2003 Apple Computer Inc., all rights reserved.",
				"sRGB Profile", null, null, "appl", null)));
		Set<String> iccbsds85 = new HashSet<>();
		iccbsds85.add("clrspDir13");
		assertTrue(collection.getFeatureTreesForType(FeaturesObjectTypesEnum.ICCPROFILE).contains(getICCProfile("iccProfileIndir85",
				null, iccbsds85, "4.2.0", "ADBE", "RGB ", "ADBE", "2007-10-24T00:00:00.000+03:00", "\u0000\u0000\u0000\u0001", "Copyright 2007 Adobe Systems Incorporated",
				"HDTV (Rec. 709)", "t\u001C$ﾦ\u0012\u0017ﾉHQﾃ\uFFEFￋ￨<\uFFE7,", null, null, null)));
		Set<String> iccbsds77 = new HashSet<>();
		iccbsds77.add("clrspDir3");
		iccbsds77.add("clrspDir2");
		iccbsds77.add("clrspDir0");
		assertTrue(collection.getFeatureTreesForType(FeaturesObjectTypesEnum.ICCPROFILE).contains(getICCProfile("iccProfileIndir77",
				null, iccbsds77, "2.1.0", "ADBE", "GRAY", "ADBE", "1999-06-03T00:00:00.000+03:00", null, "Copyright 1999 Adobe Systems Incorporated",
				"Dot Gain 20%", null, null, "none", null)));
	}

	@Test
	public void testOutputIntent() throws FeaturesTreeNodeException {
		assertEquals(1, collection.getFeatureTreesForType(FeaturesObjectTypesEnum.OUTPUTINTENT).size());
		assertTrue(collection.getFeatureTreesForType(FeaturesObjectTypesEnum.OUTPUTINTENT).contains(getOutputIntent()));
	}

	@Test
	public void testOutlines() throws FeaturesTreeNodeException {
		assertEquals(1, collection.getFeatureTreesForType(FeaturesObjectTypesEnum.OUTLINES).size());
		assertTrue(collection.getFeatureTreesForType(FeaturesObjectTypesEnum.OUTLINES).contains(getOutlines()));
	}

	@Test
	public void testAnnotations() throws FeaturesTreeNodeException {
		assertEquals(7, collection.getFeatureTreesForType(FeaturesObjectTypesEnum.ANNOTATION).size());
		Set<String> xobj37 = new HashSet<>();
		xobj37.add("xobjIndir28");
		assertTrue(collection.getFeatureTreesForType(FeaturesObjectTypesEnum.ANNOTATION).contains(getAnnotation("annotIndir37",
				"page1", null, "Text", "368.092", "423.522", "386.092", "441.522", "Annotation with pop-up window",
				"d48d8e43-b22c-41ce-8cfa-28c1ca955d97", "D:20150822140440+03'00'", xobj37, "annotIndir38", "1.0", "1.0", "0.0", null,
				"false", "false", "true", "true", "true", "false", "false", "false", "false", "false")));
		assertTrue(collection.getFeatureTreesForType(FeaturesObjectTypesEnum.ANNOTATION).contains(getAnnotation("annotIndir38",
				"page1", "annotIndir37", "Popup", "370.08", "265.547", "550.081", "385.984", null,
				null, null, null, null, null, null, null, null,
				"false", "false", "true", "true", "true", "false", "false", "false", "false", "false")));
		Set<String> xobj13 = new HashSet<>();
		xobj13.add("xobjIndir22");
		xobj13.add("xobjIndir21");
		xobj13.add("xobjIndir23");
		assertTrue(collection.getFeatureTreesForType(FeaturesObjectTypesEnum.ANNOTATION).contains(getAnnotation("annotIndir13",
				"page1", null, "Widget", "112.562", "398.933", "161.251", "450.764", null,
				null, null, xobj13, null, null, null, null, null,
				"false", "false", "true", "false", "false", "false", "false", "false", "false", "false")));
		assertTrue(collection.getFeatureTreesForType(FeaturesObjectTypesEnum.ANNOTATION).contains(getAnnotation("annotIndir42",
				"page1", "annotIndir41", "Popup", "499.977", "350.004", "679.977", "470.004", null,
				null, null, null, null, null, null, null, null,
				"false", "false", "true", "true", "true", "false", "false", "false", "false", "false")));
		assertTrue(collection.getFeatureTreesForType(FeaturesObjectTypesEnum.ANNOTATION).contains(getAnnotation("annotIndir40",
				"page1", "annotIndir39", "Popup", "499.977", "322.78", "679.977", "442.78", null,
				null, null, null, null, null, null, null, null,
				"false", "false", "true", "true", "true", "false", "false", "false", "false", "false")));
		Set<String> xobj41 = new HashSet<>();
		xobj41.add("xobjIndir27");
		assertTrue(collection.getFeatureTreesForType(FeaturesObjectTypesEnum.ANNOTATION).contains(getAnnotation("annotIndir41",
				"page1", null, "Text", "338.339", "452.004", "356.339", "470.004", "annotation with CMYK colorspace\r",
				"a21bf4d8-e9fe-4e29-89a0-26e416fc8ca7", "D:20150831140530+03'00'", xobj41, "annotIndir42", "1.0", "0.0", "0.0", "0.0",
				"false", "false", "true", "true", "true", "false", "false", "false", "false", "false")));
		Set<String> xobj39 = new HashSet<>();
		xobj39.add("xobjIndir27");
		assertTrue(collection.getFeatureTreesForType(FeaturesObjectTypesEnum.ANNOTATION).contains(getAnnotation("annotIndir39",
				"page1", null, "Text", "307.974", "424.78", "325.974", "442.78", "annotation with gray colorspace\r",
				"85f36ad6-ae92-479e-9b24-ba07c8702837", "D:20150831140515+03'00'", xobj39, "annotIndir40", "1.0", null, null, null,
				"false", "false", "true", "true", "true", "false", "false", "false", "false", "false")));
	}

	@Test
	public void testPage() throws FeaturesTreeNodeException, FileNotFoundException, URISyntaxException {
		assertEquals(1, collection.getFeatureTreesForType(FeaturesObjectTypesEnum.PAGE).size());
		assertTrue(collection.getFeatureTreesForType(FeaturesObjectTypesEnum.PAGE).contains(getPage()));
	}

	@Test
	public void testGraphicsState() throws FeaturesTreeNodeException {
		assertEquals(4, collection.getFeatureTreesForType(FeaturesObjectTypesEnum.EXT_G_STATE).size());
		assertTrue(collection.getFeatureTreesForType(FeaturesObjectTypesEnum.EXT_G_STATE).contains(getGraphicsState("exGStIndir93",
				null, "ptrnIndir50", null, null, "true", "false", "false", "false", "fntIndir89")));
		assertTrue(collection.getFeatureTreesForType(FeaturesObjectTypesEnum.EXT_G_STATE).contains(getGraphicsState("exGStDir2",
				null, null, "xobjIndir28", null, "true", "false", "false", "false", null)));
		assertTrue(collection.getFeatureTreesForType(FeaturesObjectTypesEnum.EXT_G_STATE).contains(getGraphicsState("exGStDir3",
				null, null, "xobjIndir27", null, "true", "false", "false", "false", null)));
		assertTrue(collection.getFeatureTreesForType(FeaturesObjectTypesEnum.EXT_G_STATE).contains(getGraphicsState("exGStIndir47",
				"page1", null, null, "fntIndir91", "false", "false", "false", "true", "fntIndir88")));
	}

	@Test
	public void testPropertiesDicts() throws FeaturesTreeNodeException {
		assertEquals(1, collection.getFeatureTreesForType(FeaturesObjectTypesEnum.PROPERTIES).size());
		assertTrue(collection.getFeatureTreesForType(FeaturesObjectTypesEnum.PROPERTIES).contains(getProperties()));
	}

	@Test
	public void testErrors() throws FeaturesTreeNodeException {
		assertEquals(1, collection.getFeatureTreesForType(FeaturesObjectTypesEnum.ERROR).size());
		assertTrue(collection.getFeatureTreesForType(FeaturesObjectTypesEnum.ERROR).contains(getError()));
	}

	@Test
	public void testFailedXObjects() throws FeaturesTreeNodeException {
		assertEquals(2, collection.getFeatureTreesForType(FeaturesObjectTypesEnum.FAILED_XOBJECT).size());
		assertTrue(collection.getFeatureTreesForType(FeaturesObjectTypesEnum.FAILED_XOBJECT).contains(getFailedXObject("xobjIndir53")));
		assertTrue(collection.getFeatureTreesForType(FeaturesObjectTypesEnum.FAILED_XOBJECT).contains(getFailedXObject("xobjIndir54")));
	}

	@Test
	public void testShadings() throws FeaturesTreeNodeException {
		assertEquals(1, collection.getFeatureTreesForType(FeaturesObjectTypesEnum.SHADING).size());
		assertTrue(collection.getFeatureTreesForType(FeaturesObjectTypesEnum.SHADING).contains(getShading()));
	}

	@Test
	public void testPatterns() throws FeaturesTreeNodeException {
		assertEquals(2, collection.getFeatureTreesForType(FeaturesObjectTypesEnum.PATTERN).size());
		assertTrue(collection.getFeatureTreesForType(FeaturesObjectTypesEnum.PATTERN).contains(getShadingPattern()));
		assertTrue(collection.getFeatureTreesForType(FeaturesObjectTypesEnum.PATTERN).contains(getTilingPattern()));
	}

	@Test
	public void testColorSpaces() {
		assertEquals(32, collection.getFeatureTreesForType(FeaturesObjectTypesEnum.COLORSPACE).size());
		//TODO: Finish this
	}

	@Test
	public void testImageXObjects() {
		assertEquals(9, collection.getFeatureTreesForType(FeaturesObjectTypesEnum.IMAGE_XOBJECT).size());
		//TODO: Finish this
	}

	@Test
	public void testFormXObjects() {
		assertEquals(12, collection.getFeatureTreesForType(FeaturesObjectTypesEnum.FORM_XOBJECT).size());
		//TODO: Finish this
	}

	@Test
	public void testFonts() {
		assertEquals(7, collection.getFeatureTreesForType(FeaturesObjectTypesEnum.FONT).size());
		//TODO: Finish this
	}

	@Test
	public void testProcSets() {
		assertEquals(7, collection.getFeatureTreesForType(FeaturesObjectTypesEnum.PROCSET).size());
		//TODO: Finish this
	}

	private static FeatureTreeNode getTilingPattern() throws FeaturesTreeNodeException {
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
		getPatternMatrix(root);
		FeatureTreeNode resources = FeatureTreeNode.newChildInstance("resources", root);
		FeatureTreeNode colorSpaces = FeatureTreeNode.newChildInstance("colorSpaces", resources);
		FeatureTreeNode clr2 = FeatureTreeNode.newChildInstance("colorSpace", colorSpaces);
		clr2.addAttribute(ID, "clrspDir19");
		FeatureTreeNode clr1 = FeatureTreeNode.newChildInstance("colorSpace", colorSpaces);
		clr1.addAttribute(ID, "clrspDir18");
		FeatureTreeNode xobjects = FeatureTreeNode.newChildInstance("xobjects", resources);
		FeatureTreeNode xobj2 = FeatureTreeNode.newChildInstance("xobject", xobjects);
		xobj2.addAttribute(ID, "xobjIndir60");
		FeatureTreeNode xobj1 = FeatureTreeNode.newChildInstance("xobject", xobjects);
		xobj1.addAttribute(ID, "xobjIndir56");
		return root;
	}

	private static FeatureTreeNode getShadingPattern() throws FeaturesTreeNodeException {
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
		getPatternMatrix(root);
		FeatureTreeNode gst = FeatureTreeNode.newChildInstance("graphicsState", root);
		gst.addAttribute(ID, "exGStIndir93");
		return root;
	}

	private static void getPatternMatrix(FeatureTreeNode root) throws FeaturesTreeNodeException {
		FeatureTreeNode matr = FeatureTreeNode.newChildInstance("matrix", root);
		addElement("1", "1", "1.0", matr);
		addElement("2", "1", "0.0", matr);
		addElement("1", "2", "0.0", matr);
		addElement("2", "2", "1.0", matr);
		addElement("1", "3", "0.0", matr);
		addElement("2", "3", "0.0", matr);
	}

	private static void addElement(String column, String row, String value, FeatureTreeNode parent) throws FeaturesTreeNodeException {
		FeatureTreeNode element = FeatureTreeNode.newChildInstance("element", parent);
		element.addAttribute("column", column);
		element.addAttribute("row", row);
		element.addAttribute("value", value);
	}

	private static FeatureTreeNode getShading() throws FeaturesTreeNodeException {
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

	private static FeatureTreeNode getFailedXObject(String id) throws FeaturesTreeNodeException {
		FeatureTreeNode root = FeatureTreeNode.newRootInstance("xobject");
		root.addAttribute(ID, id);
		root.addAttribute("errorID", "xobjerr1");
		return root;
	}

	private static FeatureTreeNode getError() throws FeaturesTreeNodeException {
		FeatureTreeNode root = FeatureTreeNode.newRootInstanceWIthValue("error", "Error while getting xobject.");
		root.addAttribute(ID, "xobjerr1");
		return root;
	}

	private static FeatureTreeNode getProperties() throws FeaturesTreeNodeException {
		FeatureTreeNode root = FeatureTreeNode.newRootInstance("propertiesDict");
		root.addAttribute(ID, "propDir0");
		FeatureTreeNode parents = FeatureTreeNode.newChildInstance("parents", root);
		FeatureTreeNode page = FeatureTreeNode.newChildInstance("page", parents);
		page.addAttribute(ID, "page1");
		return root;
	}

	private static FeatureTreeNode getGraphicsState(String id, String page, String pattern, String xobject, String font, String transparency, String strokeAdjustment,
													String overprintForStroke, String overprintForFill, String fontChild) throws FeaturesTreeNodeException {
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
		if (xobject != null) {
			FeatureTreeNode xobjectNode = FeatureTreeNode.newChildInstance("xobject", parents);
			xobjectNode.addAttribute(ID, xobject);
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

	private static FeatureTreeNode getPage() throws FeaturesTreeNodeException, URISyntaxException, FileNotFoundException {
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
		FeatureTreeNode.newChildInstanceWithValue("thumbnail", "false", root);
		FeatureTreeNode.newChildInstanceWithValue("metadata", getMetadataBytesFromFile("/page1_metadata_bytes.txt"), root);

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
		colorSpaces.add("devrgb");
		colorSpaces.add("clrspDir9");
		colorSpaces.add("clrspDir8");
		colorSpaces.add("clrspDir5");
		colorSpaces.add("clrspDir17");
		colorSpaces.add("clrspDir3");
		colorSpaces.add("clrspDir16");
		colorSpaces.add("clrspDir4");
		colorSpaces.add("clrspDir14");
		colorSpaces.add("clrspDir2");
		colorSpaces.add("clrspDir13");
		colorSpaces.add("clrspDir12");
		colorSpaces.add("clrspDir0");
		colorSpaces.add("clrspDir11");
		colorSpaces.add("clrspDir10");
		colorSpaces.add("devcmyk");
		colorSpaces.add("devgray");
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

		List<String> procSets = new ArrayList<>();
		procSets.add("prsetDir0");
		makeList("procSet", procSets, resources);

		List<String> propertiesDicts = new ArrayList<>();
		propertiesDicts.add("propDir0");
		makeList("propertiesDict", propertiesDicts, resources);

		return root;
	}

	private static void makeList(String name, List<String> values, FeatureTreeNode parent) throws FeaturesTreeNodeException {
		FeatureTreeNode head = FeatureTreeNode.newChildInstance(name + "s", parent);
		for (String el : values) {
			FeatureTreeNode element = FeatureTreeNode.newChildInstance(name, head);
			element.addAttribute(ID, el);
		}
	}

	private static void addBox(String name, FeatureTreeNode parent) throws FeaturesTreeNodeException {
		FeatureTreeNode box = FeatureTreeNode.newChildInstance(name, parent);
		box.addAttribute(LLX, "0.0");
		box.addAttribute(LLY, "0.0");
		box.addAttribute(URX, "499.977");
		box.addAttribute(URY, "499.977");
	}

	private static FeatureTreeNode getAnnotation(String id, String parentPage, String parentAnnotation,
												 String subtype, String llx, String lly, String urx, String ury,
												 String contents, String annotationName, String modifiedDate, Set<String> xobj,
												 String popup, String red, String green, String blue, String kayan,
												 String invisible, String hidden, String print, String noZoom,
												 String noRotate, String noView, String readOnly, String locked,
												 String toggleNoView, String lockedContents) throws FeaturesTreeNodeException {
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
				FeatureTreeNode node = FeatureTreeNode.newChildInstance("xObject", resources);
				node.addAttribute(ID, objID);
			}
		}
		if (popup != null) {
			FeatureTreeNode pop = FeatureTreeNode.newChildInstance("popup", root);
			pop.addAttribute(ID, popup);
		}

		if (red != null && green != null && blue != null && kayan != null) {
			FeatureTreeNode color = FeatureTreeNode.newChildInstance("color", root);
			FeatureTreeNode.newChildInstanceWithValue("cyan", red, color);
			FeatureTreeNode.newChildInstanceWithValue("magenta", green, color);
			FeatureTreeNode.newChildInstanceWithValue("yellow", blue, color);
			FeatureTreeNode.newChildInstanceWithValue("black", blue, color);
		} else if (red != null && green != null && blue != null) {
			FeatureTreeNode color = FeatureTreeNode.newChildInstance("color", root);
			FeatureTreeNode.newChildInstanceWithValue("red", red, color);
			FeatureTreeNode.newChildInstanceWithValue("green", green, color);
			FeatureTreeNode.newChildInstanceWithValue("blue", blue, color);
		} else if (red != null) {
			FeatureTreeNode color = FeatureTreeNode.newChildInstance("color", root);
			FeatureTreeNode.newChildInstanceWithValue("gray", red, color);
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

	private static FeatureTreeNode getOutlines() throws FeaturesTreeNodeException {
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

	private static void makeOutline(String title, String red, String green, String blue, String italic, String bold, FeatureTreeNode root) throws FeaturesTreeNodeException {
		FeatureTreeNode.newChildInstanceWithValue("title", title, root);
		FeatureTreeNode color = FeatureTreeNode.newChildInstance("color", root);
		FeatureTreeNode.newChildInstanceWithValue("red", red, color);
		FeatureTreeNode.newChildInstanceWithValue("green", green, color);
		FeatureTreeNode.newChildInstanceWithValue("blue", blue, color);
		FeatureTreeNode.newChildInstanceWithValue("italic", italic, root);
		FeatureTreeNode.newChildInstanceWithValue("bold", bold, root);
	}

	private static FeatureTreeNode getOutputIntent() throws FeaturesTreeNodeException {
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

	private static FeatureTreeNode getICCProfile(String id, Set<String> outInts, Set<String> iccBaseds, String version,
												 String cmmType, String dataColorSpace, String creator, String creationDate,
												 String defaultRenderingIntent, String copyright, String description,
												 String profileId, String deviceModel, String deviceManufacturer, byte[] metadata) throws FeaturesTreeNodeException {
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
			FeatureTreeNode.newChildInstanceWithValue("metadata", metadata, root);
		}

		return root;
	}

	private static FeatureTreeNode getEmbeddedFileNode(String id, String fileName, String description, String subtype,
													   String filter, String creationDate, String modDate, String checkSum,
													   String size) throws FeaturesTreeNodeException {
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

	private static FeatureTreeNode getLowLvlInfo() throws FeaturesTreeNodeException {
		FeatureTreeNode lli = FeatureTreeNode.newRootInstance("lowLevelInfo");
		FeatureTreeNode.newChildInstanceWithValue("indirectObjectsNumber", "125", lli);
		FeatureTreeNode docID = FeatureTreeNode.newChildInstance("documentId", lli);
		docID.addAttribute("modificationId", "295EBB0E08D32644B7E5C1825F15AD3A");
		docID.addAttribute("creationId", "85903F3A2C43B1DA24E486CD15B8154E");
		FeatureTreeNode filters = FeatureTreeNode.newChildInstance("filters", lli);
		addFilter("FlateDecode", filters);
		addFilter("ASCIIHexDecode", filters);
		addFilter("CCITTFaxDecode", filters);
		addFilter("DCTDecode", filters);
		return lli;
	}

	private static void addFilter(String name, FeatureTreeNode parent) throws FeaturesTreeNodeException {
		FeatureTreeNode filter = FeatureTreeNode.newChildInstance("filter", parent);
		filter.addAttribute("name", name);
	}

	private static FeatureTreeNode getInfDictNode() throws FeaturesTreeNodeException {
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

	private static FeatureTreeNode getMetadataNode() throws FeaturesTreeNodeException, FileNotFoundException, URISyntaxException {
		return FeatureTreeNode.newRootInstanceWIthValue(METADATA, getMetadataBytesFromFile("/metadata_bytes.txt"));
	}

	private static byte[] getMetadataBytesFromFile(String path) throws URISyntaxException, FileNotFoundException {
		Scanner scan = new Scanner(new File(getSystemIndependentPath(path)));
		int n = scan.nextInt();
		byte[] res = new byte[n];
		for (int i = 0; scan.hasNextInt(); ++i) {
			res[i] = (byte) scan.nextInt();
		}
		return res;
	}

	private static void addEntry(String name, String value, FeatureTreeNode parent) throws FeaturesTreeNodeException {
		FeatureTreeNode entry = FeatureTreeNode.newChildInstanceWithValue(ENTRY, value, parent);
		entry.addAttribute("key", name);
	}

	private static String getSystemIndependentPath(String path) throws URISyntaxException {
		URL resourceUrl = ClassLoader.class.getResource(path);
		Path resourcePath = Paths.get(resourceUrl.toURI());
		return resourcePath.toString();
	}
}
