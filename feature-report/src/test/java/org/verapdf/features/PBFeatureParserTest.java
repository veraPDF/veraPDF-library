package org.verapdf.features;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.junit.BeforeClass;
import org.junit.Test;
import org.verapdf.core.FeatureParsingException;
import org.verapdf.features.pb.PBFeatureParser;
import org.verapdf.features.tools.FeatureTreeNode;
import org.verapdf.features.tools.FeaturesCollection;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Maksim Bezrukov
 */
public class PBFeatureParserTest {

	private static FeaturesCollection collection;

	@BeforeClass
	public static void before() throws URISyntaxException, IOException {
		File pdf = new File(TestNodeGenerator.getSystemIndependentPath("/FR.pdf"));
		PDDocument document = PDDocument.load(pdf, false, true);
		collection = PBFeatureParser.getFeaturesCollection(document);
	}

	@Test
	public void objectsNumberTest() {
		assertEquals(1, collection.getFeatureTreesForType(FeaturesObjectTypesEnum.INFORMATION_DICTIONARY).size());
		assertEquals(1, collection.getFeatureTreesForType(FeaturesObjectTypesEnum.METADATA).size());
		assertEquals(1, collection.getFeatureTreesForType(FeaturesObjectTypesEnum.LOW_LEVEL_INFO).size());
		assertEquals(4, collection.getFeatureTreesForType(FeaturesObjectTypesEnum.EMBEDDED_FILE).size());
		assertEquals(5, collection.getFeatureTreesForType(FeaturesObjectTypesEnum.ICCPROFILE).size());
		assertEquals(1, collection.getFeatureTreesForType(FeaturesObjectTypesEnum.OUTPUTINTENT).size());
		assertEquals(1, collection.getFeatureTreesForType(FeaturesObjectTypesEnum.OUTLINES).size());
		assertEquals(7, collection.getFeatureTreesForType(FeaturesObjectTypesEnum.ANNOTATION).size());
		assertEquals(1, collection.getFeatureTreesForType(FeaturesObjectTypesEnum.PAGE).size());
		assertEquals(4, collection.getFeatureTreesForType(FeaturesObjectTypesEnum.EXT_G_STATE).size());
		assertEquals(1, collection.getFeatureTreesForType(FeaturesObjectTypesEnum.PROPERTIES).size());
		assertEquals(2, collection.getFeatureTreesForType(FeaturesObjectTypesEnum.ERROR).size());
		assertEquals(2, collection.getFeatureTreesForType(FeaturesObjectTypesEnum.FAILED_XOBJECT).size());
		assertEquals(1, collection.getFeatureTreesForType(FeaturesObjectTypesEnum.SHADING).size());
		assertEquals(2, collection.getFeatureTreesForType(FeaturesObjectTypesEnum.PATTERN).size());
		assertEquals(34, collection.getFeatureTreesForType(FeaturesObjectTypesEnum.COLORSPACE).size());
		assertEquals(10, collection.getFeatureTreesForType(FeaturesObjectTypesEnum.IMAGE_XOBJECT).size());
		assertEquals(12, collection.getFeatureTreesForType(FeaturesObjectTypesEnum.FORM_XOBJECT).size());
		assertEquals(0, collection.getFeatureTreesForType(FeaturesObjectTypesEnum.POSTSCRIPT_XOBJECT).size());
		assertEquals(7, collection.getFeatureTreesForType(FeaturesObjectTypesEnum.FONT).size());

	}

	@Test
	public void typeErrorsCheck() {
		for (FeaturesObjectTypesEnum type : FeaturesObjectTypesEnum.values()) {
			assertTrue(collection.getErrorsForType(type).isEmpty());
		}
	}

	@Test
	public void testInfoDict() throws FeatureParsingException {
		List<FeatureTreeNode> treeNodes = collection.getFeatureTreesForType(FeaturesObjectTypesEnum.INFORMATION_DICTIONARY);
		assertTrue(treeNodes.contains(TestNodeGenerator.getInfDictNode()));
	}

	@Test
	public void testMetadata() throws FeatureParsingException, FileNotFoundException, URISyntaxException {
		List<FeatureTreeNode> treeNodes = collection.getFeatureTreesForType(FeaturesObjectTypesEnum.METADATA);
		assertTrue(treeNodes.contains(TestNodeGenerator.getMetadataNode()));
	}

	@Test
	public void testLowLvlInfo() throws FeatureParsingException {
		List<FeatureTreeNode> treeNodes = collection.getFeatureTreesForType(FeaturesObjectTypesEnum.LOW_LEVEL_INFO);
		assertTrue(treeNodes.contains(TestNodeGenerator.getLowLvlInfo()));
	}

	@Test
	public void testEmbeddedFiles() throws FeatureParsingException {
		List<FeatureTreeNode> treeNodes = collection.getFeatureTreesForType(FeaturesObjectTypesEnum.EMBEDDED_FILE);
		assertTrue(treeNodes.contains(TestNodeGenerator.getEmbeddedFileNode("file1", "1.txt", "",
				"text/plain", "FlateDecode", "2015-08-31T13:33:43.000+03:00", "2015-08-31T13:20:39.000Z", "D41D8CD98F00B204E9800998ECF8427E", "0")));
		assertTrue(treeNodes.contains(TestNodeGenerator.getEmbeddedFileNode("file2", "Arist.jpg", "",
				"image/jpeg", "FlateDecode", "2015-08-31T13:33:33.000+03:00", "2014-08-15T17:17:58.000Z", "F9803872918B24974BE596EA6C986D7D", "26862")));
		assertTrue(treeNodes.contains(TestNodeGenerator.getEmbeddedFileNode("file3", "XMP - 8.xml", "",
				"text/xml", "FlateDecode", "2015-08-31T13:33:38.000+03:00", "2015-08-20T12:24:50.000Z", "0605BCE41755770D87A93EF1380E6ED4", "876")));
		assertTrue(treeNodes.contains(TestNodeGenerator.getEmbeddedFileNode("file4", "fox_1.jpg", "Some Description for embedded file",
				"image/jpeg", "FlateDecode", "2015-08-22T14:01:19.000+03:00", "2014-09-08T12:01:07.000Z", "CBD3FE566607E760BA95E56B153F410D", "67142")));
	}

	@Test
	public void testICCProfiles() throws FeatureParsingException, FileNotFoundException, URISyntaxException {
		List<FeatureTreeNode> treeNodes = collection.getFeatureTreesForType(FeaturesObjectTypesEnum.ICCPROFILE);
		Set<String> outInts19 = new HashSet<>();
		outInts19.add("outIntDir0");
		assertTrue(treeNodes.contains(TestNodeGenerator.getICCProfile("iccProfileIndir19",
				outInts19, null, "2.1.0", "ADBE", "RGB ", "ADBE", "2000-08-11T19:52:24.000Z", "Perceptual", "Copyright 2000 Adobe Systems Incorporated",
				"Apple RGB", null, null, "none", TestNodeGenerator.getMetadataBytesFromFile("/iccprofile19_metadata_bytes.txt"))));
		Set<String> iccbsds81 = new HashSet<>();
		iccbsds81.add("clrspDir10");
		assertTrue(treeNodes.contains(TestNodeGenerator.getICCProfile("iccProfileIndir81",
				null, iccbsds81, "2.1.0", "ADBE", "RGB ", "ADBE", "2000-08-11T19:54:18.000Z", "Perceptual", "Copyright 2000 Adobe Systems Incorporated",
				"PAL/SECAM", null, null, "none", null)));
		Set<String> iccbsds84 = new HashSet<>();
		iccbsds84.add("clrspDir13");
		iccbsds84.add("clrspDir24");
		iccbsds84.add("clrspDir23");
		assertTrue(treeNodes.contains(TestNodeGenerator.getICCProfile("iccProfileIndir84",
				null, iccbsds84, "2.2.0", "appl", "RGB ", "appl", "2000-08-13T16:06:07.000Z", "Media-Relative Colorimetric", "Copyright 1998 - 2003 Apple Computer Inc., all rights reserved.",
				"sRGB Profile", null, null, "appl", null)));
		Set<String> iccbsds85 = new HashSet<>();
		iccbsds85.add("clrspDir14");
		assertTrue(treeNodes.contains(TestNodeGenerator.getICCProfile("iccProfileIndir85",
				null, iccbsds85, "4.2.0", "ADBE", "RGB ", "ADBE", "2007-10-24T00:00:00.000Z", "Media-Relative Colorimetric", "Copyright 2007 Adobe Systems Incorporated",
				"HDTV (Rec. 709)", "t\u001C$ﾦ\u0012\u0017ﾉHQﾃ\uFFEFￋ￨<\uFFE7,", null, null, null)));
		Set<String> iccbsds77 = new HashSet<>();
		iccbsds77.add("clrspDir4");
		iccbsds77.add("clrspDir2");
		iccbsds77.add("clrspDir5");
		assertTrue(treeNodes.contains(TestNodeGenerator.getICCProfile("iccProfileIndir77",
				null, iccbsds77, "2.1.0", "ADBE", "GRAY", "ADBE", "1999-06-03T00:00:00.000Z", "Perceptual", "Copyright 1999 Adobe Systems Incorporated",
				"Dot Gain 20%", null, null, "none", null)));
	}

	@Test
	public void testOutputIntent() throws FeatureParsingException {
		List<FeatureTreeNode> treeNodes = collection.getFeatureTreesForType(FeaturesObjectTypesEnum.OUTPUTINTENT);
		assertTrue(treeNodes.contains(TestNodeGenerator.getOutputIntent()));
	}

	@Test
	public void testOutlines() throws FeatureParsingException {
		List<FeatureTreeNode> treeNodes = collection.getFeatureTreesForType(FeaturesObjectTypesEnum.OUTLINES);
		assertTrue(treeNodes.contains(TestNodeGenerator.getOutlines()));
	}

	@Test
	public void testAnnotations() throws FeatureParsingException {
		Set<String> xobj37 = new HashSet<>();
		xobj37.add("xobjIndir28");
		List<FeatureTreeNode> treeNodes = collection.getFeatureTreesForType(FeaturesObjectTypesEnum.ANNOTATION);
		assertTrue(treeNodes.contains(TestNodeGenerator.getAnnotation("annotIndir37",
				"page1", null, "Text", "368.092", "423.522", "386.092", "441.522", "Annotation with pop-up window",
				"d48d8e43-b22c-41ce-8cfa-28c1ca955d97", "D:20150822140440+03'00'", xobj37, "annotIndir38", "1.0", "1.0", "0.0", null,
				"false", "false", "true", "true", "true", "false", "false", "false", "false", "false")));
		assertTrue(treeNodes.contains(TestNodeGenerator.getAnnotation("annotIndir38",
				"page1", "annotIndir37", "Popup", "370.08", "265.547", "550.081", "385.984", null,
				null, null, null, null, null, null, null, null,
				"false", "false", "true", "true", "true", "false", "false", "false", "false", "false")));
		Set<String> xobj13 = new HashSet<>();
		xobj13.add("xobjIndir22");
		xobj13.add("xobjIndir21");
		xobj13.add("xobjIndir23");
		assertTrue(treeNodes.contains(TestNodeGenerator.getAnnotation("annotIndir13",
				"page1", null, "Widget", "112.562", "398.933", "161.251", "450.764", null,
				null, null, xobj13, null, null, null, null, null,
				"false", "false", "true", "false", "false", "false", "false", "false", "false", "false")));
		assertTrue(treeNodes.contains(TestNodeGenerator.getAnnotation("annotIndir42",
				"page1", "annotIndir41", "Popup", "499.977", "350.004", "679.977", "470.004", null,
				null, null, null, null, null, null, null, null,
				"false", "false", "true", "true", "true", "false", "false", "false", "false", "false")));
		assertTrue(treeNodes.contains(TestNodeGenerator.getAnnotation("annotIndir40",
				"page1", "annotIndir39", "Popup", "499.977", "322.78", "679.977", "442.78", null,
				null, null, null, null, null, null, null, null,
				"false", "false", "true", "true", "true", "false", "false", "false", "false", "false")));
		Set<String> xobj41 = new HashSet<>();
		xobj41.add("xobjIndir27");
		assertTrue(treeNodes.contains(TestNodeGenerator.getAnnotation("annotIndir41",
				"page1", null, "Text", "338.339", "452.004", "356.339", "470.004", "annotation with CMYK colorspace\r",
				"a21bf4d8-e9fe-4e29-89a0-26e416fc8ca7", "D:20150831140530+03'00'", xobj41, "annotIndir42", "1.0", "0.0", "0.0", "0.0",
				"false", "false", "true", "true", "true", "false", "false", "false", "false", "false")));
		Set<String> xobj39 = new HashSet<>();
		xobj39.add("xobjIndir27");
		assertTrue(treeNodes.contains(TestNodeGenerator.getAnnotation("annotIndir39",
				"page1", null, "Text", "307.974", "424.78", "325.974", "442.78", "annotation with gray colorspace\r",
				"85f36ad6-ae92-479e-9b24-ba07c8702837", "D:20150831140515+03'00'", xobj39, "annotIndir40", "1.0", null, null, null,
				"false", "false", "true", "true", "true", "false", "false", "false", "false", "false")));
	}

	@Test
	public void testPage() throws FeatureParsingException, FileNotFoundException, URISyntaxException {
		List<FeatureTreeNode> treeNodes = collection.getFeatureTreesForType(FeaturesObjectTypesEnum.PAGE);
		assertTrue(treeNodes.contains(TestNodeGenerator.getPage()));
	}

	@Test
	public void testGraphicsState() throws FeatureParsingException {
		List<FeatureTreeNode> treeNodes = collection.getFeatureTreesForType(FeaturesObjectTypesEnum.EXT_G_STATE);
		assertTrue(treeNodes.contains(TestNodeGenerator.getGraphicsState("exGStIndir93",
				null, "ptrnIndir50", null, null, null, "true", "false", "false", "false", "fntIndir89")));
		assertTrue(treeNodes.contains(TestNodeGenerator.getGraphicsState("exGStDir2",
				null, null, "xobjIndir28", null, null, "true", "false", "false", "false", null)));
		assertTrue(treeNodes.contains(TestNodeGenerator.getGraphicsState("exGStDir3",
				null, null, "xobjIndir27", null, null, "true", "false", "false", "false", null)));
		assertTrue(treeNodes.contains(TestNodeGenerator.getGraphicsState("exGStIndir47",
				"page1", null, "xobjIndir56", "xobjIndir57", "fntIndir91", "false", "false", "false", "true", "fntIndir88")));
	}

	@Test
	public void testPropertiesDicts() throws FeatureParsingException {
		List<FeatureTreeNode> treeNodes = collection.getFeatureTreesForType(FeaturesObjectTypesEnum.PROPERTIES);
		assertTrue(treeNodes.contains(TestNodeGenerator.getProperties()));
	}

	@Test
	public void testFailedXObjects() throws FeatureParsingException {
		List<FeatureTreeNode> treeNodes = collection.getFeatureTreesForType(FeaturesObjectTypesEnum.FAILED_XOBJECT);
		assertTrue(treeNodes.contains(TestNodeGenerator.getFailedXObject("xobjIndir53", "error0")));
		assertTrue(treeNodes.contains(TestNodeGenerator.getFailedXObject("xobjIndir54", "error1")));
	}

	@Test
	public void testShadings() throws FeatureParsingException {
		List<FeatureTreeNode> treeNodes = collection.getFeatureTreesForType(FeaturesObjectTypesEnum.SHADING);
		assertTrue(treeNodes.contains(TestNodeGenerator.getShading()));
	}

	@Test
	public void testPatterns() throws FeatureParsingException {
		List<FeatureTreeNode> treeNodes = collection.getFeatureTreesForType(FeaturesObjectTypesEnum.PATTERN);
		assertTrue(treeNodes.contains(TestNodeGenerator.getShadingPattern()));
		assertTrue(treeNodes.contains(TestNodeGenerator.getTilingPattern()));
	}

	@Test
	public void testErrors() throws FeatureParsingException {
		List<FeatureTreeNode> treeNodes = collection.getFeatureTreesForType(FeaturesObjectTypesEnum.ERROR);
		assertTrue(treeNodes.contains(TestNodeGenerator.getErrorNode("error0", "Invalid XObject Subtype: Custom")));
		assertTrue(treeNodes.contains(TestNodeGenerator.getErrorNode("error1", "Unexpected object type: org.apache.pdfbox.cos.COSDictionary")));
	}

	@Test
	public void testFonts() throws FeatureParsingException {
		List<FeatureTreeNode> treeNodes = collection.getFeatureTreesForType(FeaturesObjectTypesEnum.FONT);
		assertTrue(treeNodes.contains(TestNodeGenerator.getFont0()));
		assertTrue(treeNodes.contains(TestNodeGenerator.getFont1()));
		assertTrue(treeNodes.contains(TestNodeGenerator.getFont2()));
	}

	@Test
	public void testImageXObjects() {
		//TODO: Finish this
	}

	@Test
	public void testFormXObjects() {
		//TODO: Finish this
	}

	@Test
	public void testColorSpaces() {
		//TODO: Finish this
	}
}
