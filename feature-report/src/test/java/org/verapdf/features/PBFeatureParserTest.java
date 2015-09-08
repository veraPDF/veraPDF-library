package org.verapdf.features;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.verapdf.exceptions.featurereport.FeaturesTreeNodeException;
import org.verapdf.features.pb.PBFeatureParser;
import org.verapdf.features.pb.objects.*;
import org.verapdf.features.tools.FeaturesCollection;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
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
		assertEquals(FeaturesObjectTypesEnum.PAGE, new PBPageFeaturesObject(null, null, null, null, null, null, null, null, null, null, null, null, 0).getType());
		assertEquals(FeaturesObjectTypesEnum.PROCSET, new PBProcSetFeaturesObject(null, null, null, null, null, null).getType());
		assertEquals(FeaturesObjectTypesEnum.PROPERTIES, new PBPropertiesDictFeaturesObject(null, null, null, null, null, null).getType());
		assertEquals(FeaturesObjectTypesEnum.SHADING, new PBShadingFeaturesObject(null, null, null, null, null, null, null).getType());
		assertEquals(FeaturesObjectTypesEnum.PATTERN, new PBShadingPatternFeaturesObject(null, null, null, null, null, null, null, null).getType());
		assertEquals(FeaturesObjectTypesEnum.PATTERN, new PBTilingPatternFeaturesObject(null, null, null, null, null, null, null, null, null, null, null, null, null, null).getType());
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
		assertEquals(1, collection.getFeatureTreesForType(FeaturesObjectTypesEnum.ERROR).size());
		assertEquals(2, collection.getFeatureTreesForType(FeaturesObjectTypesEnum.FAILED_XOBJECT).size());
		assertEquals(1, collection.getFeatureTreesForType(FeaturesObjectTypesEnum.SHADING).size());
		assertEquals(2, collection.getFeatureTreesForType(FeaturesObjectTypesEnum.PATTERN).size());
		assertEquals(7, collection.getFeatureTreesForType(FeaturesObjectTypesEnum.PROCSET).size());
		assertEquals(34, collection.getFeatureTreesForType(FeaturesObjectTypesEnum.COLORSPACE).size());
		assertEquals(10, collection.getFeatureTreesForType(FeaturesObjectTypesEnum.IMAGE_XOBJECT).size());
		assertEquals(12, collection.getFeatureTreesForType(FeaturesObjectTypesEnum.FORM_XOBJECT).size());
		assertEquals(7, collection.getFeatureTreesForType(FeaturesObjectTypesEnum.FONT).size());

	}

	@Test
	public void testInfoDict() throws FeaturesTreeNodeException {
		assertTrue(collection.getFeatureTreesForType(FeaturesObjectTypesEnum.INFORMATION_DICTIONARY).contains(TestNodeGenerator.getInfDictNode()));
	}

	@Test
	public void testMetadata() throws FeaturesTreeNodeException, FileNotFoundException, URISyntaxException {
		assertTrue(collection.getFeatureTreesForType(FeaturesObjectTypesEnum.METADATA).contains(TestNodeGenerator.getMetadataNode()));
	}

	@Test
	@Ignore
	public void testLowLvlInfo() throws FeaturesTreeNodeException {
		assertTrue(collection.getFeatureTreesForType(FeaturesObjectTypesEnum.LOW_LEVEL_INFO).contains(TestNodeGenerator.getLowLvlInfo()));
	}

    @Test
    public void testICCProfiles() throws FeaturesTreeNodeException, FileNotFoundException, URISyntaxException {
        assertEquals(5, collection.getFeatureTreesForType(FeaturesObjectTypesEnum.ICCPROFILE).size());
        Set<String> outInts19 = new HashSet<>();
        outInts19.add("outIntDir0");
        assertTrue(collection.getFeatureTreesForType(FeaturesObjectTypesEnum.ICCPROFILE).contains(getICCProfile("iccProfileIndir19",
                outInts19, null, "2.1.0", "ADBE", "RGB ", "ADBE", "2000-08-11T19:52:24.000Z", null, "Copyright 2000 Adobe Systems Incorporated",
                "Apple RGB", null, null, "none", getMetadataBytesFromFile("/iccprofile19_metadata_bytes.txt"))));
        Set<String> iccbsds81 = new HashSet<>();
        iccbsds81.add("clrspDir9");
        assertTrue(collection.getFeatureTreesForType(FeaturesObjectTypesEnum.ICCPROFILE).contains(getICCProfile("iccProfileIndir81",
                null, iccbsds81, "2.1.0", "ADBE", "RGB ", "ADBE", "2000-08-11T19:54:18.000Z", null, "Copyright 2000 Adobe Systems Incorporated",
                "PAL/SECAM", null, null, "none", null)));
        Set<String> iccbsds84 = new HashSet<>();
        iccbsds84.add("clrspDir12");
        iccbsds84.add("clrspDir22");
        iccbsds84.add("clrspDir21");
        assertTrue(collection.getFeatureTreesForType(FeaturesObjectTypesEnum.ICCPROFILE).contains(getICCProfile("iccProfileIndir84",
                null, iccbsds84, "2.2.0", "appl", "RGB ", "appl", "2000-08-13T16:06:07.000Z", "\u0000\u0000\u0000\u0001", "Copyright 1998 - 2003 Apple Computer Inc., all rights reserved.",
                "sRGB Profile", null, null, "appl", null)));
        Set<String> iccbsds85 = new HashSet<>();
        iccbsds85.add("clrspDir13");
        assertTrue(collection.getFeatureTreesForType(FeaturesObjectTypesEnum.ICCPROFILE).contains(getICCProfile("iccProfileIndir85",
                null, iccbsds85, "4.2.0", "ADBE", "RGB ", "ADBE", "2007-10-24T00:00:00.000Z", "\u0000\u0000\u0000\u0001", "Copyright 2007 Adobe Systems Incorporated",
                "HDTV (Rec. 709)", "t\u001C$ﾦ\u0012\u0017ﾉHQﾃ\uFFEFￋ￨<\uFFE7,", null, null, null)));
        Set<String> iccbsds77 = new HashSet<>();
        iccbsds77.add("clrspDir3");
        iccbsds77.add("clrspDir2");
        iccbsds77.add("clrspDir0");
        assertTrue(collection.getFeatureTreesForType(FeaturesObjectTypesEnum.ICCPROFILE).contains(getICCProfile("iccProfileIndir77",
                null, iccbsds77, "2.1.0", "ADBE", "GRAY", "ADBE", "1999-06-03T00:00:00.000Z", null, "Copyright 1999 Adobe Systems Incorporated",
                "Dot Gain 20%", null, null, "none", null)));
    }
	@Test
	public void testEmbeddedFiles() throws FeaturesTreeNodeException {
		assertTrue(collection.getFeatureTreesForType(FeaturesObjectTypesEnum.EMBEDDED_FILE).contains(TestNodeGenerator.getEmbeddedFileNode("file1", "1.txt", "",
				"text/plain", "FlateDecode", "2015-08-31T13:33:43.000+03:00", "2015-08-31T13:20:39.000Z", "Ô˛„Ù‘\u0000²\u0004é•\tŸìøB~", "0")));
		assertTrue(collection.getFeatureTreesForType(FeaturesObjectTypesEnum.EMBEDDED_FILE).contains(TestNodeGenerator.getEmbeddedFileNode("file2", "Arist.jpg", "",
				"image/jpeg", "FlateDecode", "2015-08-31T13:33:33.000+03:00", "2014-08-15T17:17:58.000Z", "ù•8r‚‰$ŠKåŒêlŸm}", "26862")));
		assertTrue(collection.getFeatureTreesForType(FeaturesObjectTypesEnum.EMBEDDED_FILE).contains(TestNodeGenerator.getEmbeddedFileNode("file3", "XMP - 8.xml", "",
				"text/xml", "FlateDecode", "2015-08-31T13:33:38.000+03:00", "2015-08-20T12:24:50.000Z", "\u0006\u0005¼ä\u0017Uw\r⁄©>ñ8\u000EnÔ", "876")));
		assertTrue(collection.getFeatureTreesForType(FeaturesObjectTypesEnum.EMBEDDED_FILE).contains(TestNodeGenerator.getEmbeddedFileNode("file4", "fox_1.jpg", "Some Description for embedded file",
				"image/jpeg", "FlateDecode", "2015-08-22T14:01:19.000+03:00", "2014-09-08T12:01:07.000Z", "ËÓþVf\u0007ç`ºŁåk\u0015?A\r", "67142")));
	}

	@Test
	public void testOutputIntent() throws FeaturesTreeNodeException {
		assertTrue(collection.getFeatureTreesForType(FeaturesObjectTypesEnum.OUTPUTINTENT).contains(TestNodeGenerator.getOutputIntent()));
	}

	@Test
	public void testOutlines() throws FeaturesTreeNodeException {
		assertTrue(collection.getFeatureTreesForType(FeaturesObjectTypesEnum.OUTLINES).contains(TestNodeGenerator.getOutlines()));
	}

	@Test
	public void testAnnotations() throws FeaturesTreeNodeException {
		Set<String> xobj37 = new HashSet<>();
		xobj37.add("xobjIndir28");
		assertTrue(collection.getFeatureTreesForType(FeaturesObjectTypesEnum.ANNOTATION).contains(TestNodeGenerator.getAnnotation("annotIndir37",
				"page1", null, "Text", "368.092", "423.522", "386.092", "441.522", "Annotation with pop-up window",
				"d48d8e43-b22c-41ce-8cfa-28c1ca955d97", "D:20150822140440+03'00'", xobj37, "annotIndir38", "1.0", "1.0", "0.0", null,
				"false", "false", "true", "true", "true", "false", "false", "false", "false", "false")));
		assertTrue(collection.getFeatureTreesForType(FeaturesObjectTypesEnum.ANNOTATION).contains(TestNodeGenerator.getAnnotation("annotIndir38",
				"page1", "annotIndir37", "Popup", "370.08", "265.547", "550.081", "385.984", null,
				null, null, null, null, null, null, null, null,
				"false", "false", "true", "true", "true", "false", "false", "false", "false", "false")));
		Set<String> xobj13 = new HashSet<>();
		xobj13.add("xobjIndir22");
		xobj13.add("xobjIndir21");
		xobj13.add("xobjIndir23");
		assertTrue(collection.getFeatureTreesForType(FeaturesObjectTypesEnum.ANNOTATION).contains(TestNodeGenerator.getAnnotation("annotIndir13",
				"page1", null, "Widget", "112.562", "398.933", "161.251", "450.764", null,
				null, null, xobj13, null, null, null, null, null,
				"false", "false", "true", "false", "false", "false", "false", "false", "false", "false")));
		assertTrue(collection.getFeatureTreesForType(FeaturesObjectTypesEnum.ANNOTATION).contains(TestNodeGenerator.getAnnotation("annotIndir42",
				"page1", "annotIndir41", "Popup", "499.977", "350.004", "679.977", "470.004", null,
				null, null, null, null, null, null, null, null,
				"false", "false", "true", "true", "true", "false", "false", "false", "false", "false")));
		assertTrue(collection.getFeatureTreesForType(FeaturesObjectTypesEnum.ANNOTATION).contains(TestNodeGenerator.getAnnotation("annotIndir40",
				"page1", "annotIndir39", "Popup", "499.977", "322.78", "679.977", "442.78", null,
				null, null, null, null, null, null, null, null,
				"false", "false", "true", "true", "true", "false", "false", "false", "false", "false")));
		Set<String> xobj41 = new HashSet<>();
		xobj41.add("xobjIndir27");
		assertTrue(collection.getFeatureTreesForType(FeaturesObjectTypesEnum.ANNOTATION).contains(TestNodeGenerator.getAnnotation("annotIndir41",
				"page1", null, "Text", "338.339", "452.004", "356.339", "470.004", "annotation with CMYK colorspace\r",
				"a21bf4d8-e9fe-4e29-89a0-26e416fc8ca7", "D:20150831140530+03'00'", xobj41, "annotIndir42", "1.0", "0.0", "0.0", "0.0",
				"false", "false", "true", "true", "true", "false", "false", "false", "false", "false")));
		Set<String> xobj39 = new HashSet<>();
		xobj39.add("xobjIndir27");
		assertTrue(collection.getFeatureTreesForType(FeaturesObjectTypesEnum.ANNOTATION).contains(TestNodeGenerator.getAnnotation("annotIndir39",
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
	@Ignore
	public void testGraphicsState() throws FeaturesTreeNodeException {
		assertTrue(collection.getFeatureTreesForType(FeaturesObjectTypesEnum.EXT_G_STATE).contains(TestNodeGenerator.getGraphicsState("exGStIndir93",
				null, "ptrnIndir50", null, null, null, "true", "false", "false", "false", "fntIndir89")));
		assertTrue(collection.getFeatureTreesForType(FeaturesObjectTypesEnum.EXT_G_STATE).contains(TestNodeGenerator.getGraphicsState("exGStDir2",
				null, null, "xobjIndir28", null, null, "true", "false", "false", "false", null)));
		assertTrue(collection.getFeatureTreesForType(FeaturesObjectTypesEnum.EXT_G_STATE).contains(TestNodeGenerator.getGraphicsState("exGStDir3",
				null, null, "xobjIndir27", null, null, "true", "false", "false", "false", null)));
		assertTrue(collection.getFeatureTreesForType(FeaturesObjectTypesEnum.EXT_G_STATE).contains(TestNodeGenerator.getGraphicsState("exGStIndir47",
				"page1", null, "xobjIndir56", "xobjIndir57", "fntIndir91", "false", "false", "false", "true", "fntIndir88")));
	}

	@Test
	public void testPropertiesDicts() throws FeaturesTreeNodeException {
		assertTrue(collection.getFeatureTreesForType(FeaturesObjectTypesEnum.PROPERTIES).contains(TestNodeGenerator.getProperties()));
	}

	@Test
	public void testErrors() throws FeaturesTreeNodeException {
		assertTrue(collection.getFeatureTreesForType(FeaturesObjectTypesEnum.ERROR).contains(TestNodeGenerator.getError()));
	}

	@Test
	public void testFailedXObjects() throws FeaturesTreeNodeException {
		assertTrue(collection.getFeatureTreesForType(FeaturesObjectTypesEnum.FAILED_XOBJECT).contains(TestNodeGenerator.getFailedXObject("xobjIndir53")));
		assertTrue(collection.getFeatureTreesForType(FeaturesObjectTypesEnum.FAILED_XOBJECT).contains(TestNodeGenerator.getFailedXObject("xobjIndir54")));
	}

	@Test
	public void testShadings() throws FeaturesTreeNodeException {
		assertTrue(collection.getFeatureTreesForType(FeaturesObjectTypesEnum.SHADING).contains(TestNodeGenerator.getShading()));
	}

	@Test
	@Ignore
	public void testPatterns() throws FeaturesTreeNodeException {
		assertTrue(collection.getFeatureTreesForType(FeaturesObjectTypesEnum.PATTERN).contains(TestNodeGenerator.getShadingPattern()));
		assertTrue(collection.getFeatureTreesForType(FeaturesObjectTypesEnum.PATTERN).contains(TestNodeGenerator.getTilingPattern()));
	}

	@Test
	public void testProcSets() throws FeaturesTreeNodeException {
		List<String> prsetDir6 = new ArrayList<>();
		prsetDir6.add("PDF");
		assertTrue(collection.getFeatureTreesForType(FeaturesObjectTypesEnum.PROCSET).contains(TestNodeGenerator.getProcSet("prsetDir6",
				null, "xobjIndir21", prsetDir6)));
		List<String> prsetDir5 = new ArrayList<>();
		prsetDir5.add("PDF");
		prsetDir5.add("ImageC");
		prsetDir5.add("ImageI");
		assertTrue(collection.getFeatureTreesForType(FeaturesObjectTypesEnum.PROCSET).contains(TestNodeGenerator.getProcSet("prsetDir5",
				null, "xobjIndir25", prsetDir5)));

		assertTrue(collection.getFeatureTreesForType(FeaturesObjectTypesEnum.PROCSET).contains(TestNodeGenerator.getProcSet("prsetDir4",
				null, "xobjIndir23", prsetDir6)));
		assertTrue(collection.getFeatureTreesForType(FeaturesObjectTypesEnum.PROCSET).contains(TestNodeGenerator.getProcSet("prsetDir3",
				null, "xobjIndir26", prsetDir5)));
		assertTrue(collection.getFeatureTreesForType(FeaturesObjectTypesEnum.PROCSET).contains(TestNodeGenerator.getProcSet("prsetDir2",
				null, "xobjIndir22", prsetDir6)));
		assertTrue(collection.getFeatureTreesForType(FeaturesObjectTypesEnum.PROCSET).contains(TestNodeGenerator.getProcSet("prsetDir1",
				null, "xobjIndir24", prsetDir5)));
		List<String> prsetDir0 = new ArrayList<>();
		prsetDir0.add("PDF");
		prsetDir0.add("ImageC");
		assertTrue(collection.getFeatureTreesForType(FeaturesObjectTypesEnum.PROCSET).contains(TestNodeGenerator.getProcSet("prsetDir0",
				"page1", null, prsetDir0)));
	}

	@Test
	public void testFonts() {
		//TODO: Finish this
	}

	@Test
	public void testColorSpaces() {
		//TODO: Finish this
	}

	@Test
	public void testImageXObjects() {
		//TODO: Finish this
	}

	@Test
	public void testFormXObjects() {
		//TODO: Finish this
	}
}
