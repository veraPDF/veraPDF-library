package org.verapdf.features;

import org.apache.pdfbox.pdmodel.PDDocument;
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
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Maksim Bezrukov
 */
public class PBFeatureParserTest {

    private static final String ENTRY = "entry";
    private static final String ID = "id";
    private static final String METADATA = "metadata";

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
    public void test() throws URISyntaxException, IOException, FeaturesTreeNodeException {
        File pdf = new File(getSystemIndependentPath("/FR.pdf"));
        PDDocument document = PDDocument.load(pdf, false, true);
        FeaturesCollection collection = PBFeatureParser.getFeaturesCollection(document);

        assertEquals(1, collection.getFeatureTreesForType(FeaturesObjectTypesEnum.INFORMATION_DICTIONARY).size());
        FeatureTreeNode infDict = getInfDictNode();
        assertEquals(infDict, collection.getFeatureTreesForType(FeaturesObjectTypesEnum.INFORMATION_DICTIONARY).get(0));

        assertEquals(1, collection.getFeatureTreesForType(FeaturesObjectTypesEnum.METADATA).size());
        FeatureTreeNode metadata = getMetadataNode();
        assertEquals(metadata, collection.getFeatureTreesForType(FeaturesObjectTypesEnum.METADATA).get(0));

        assertEquals(1, collection.getFeatureTreesForType(FeaturesObjectTypesEnum.LOW_LEVEL_INFO).size());
        FeatureTreeNode lli = getLowLvlInfo();
        assertEquals(lli, collection.getFeatureTreesForType(FeaturesObjectTypesEnum.LOW_LEVEL_INFO).get(0));

        assertEquals(4, collection.getFeatureTreesForType(FeaturesObjectTypesEnum.EMBEDDED_FILE).size());
        assertTrue(collection.getFeatureTreesForType(FeaturesObjectTypesEnum.EMBEDDED_FILE).contains(getEmbeddedFileNode("file1", "1.txt", "",
                "text/plain", "FlateDecode", "2015-08-31T13:33:43.000+03:00", "2015-08-31T13:20:39.000Z", "Ô˛„Ù‘\u0000²\u0004é•\tŸìøB~", "0")));
        assertTrue(collection.getFeatureTreesForType(FeaturesObjectTypesEnum.EMBEDDED_FILE).contains(getEmbeddedFileNode("file2", "Arist.jpg", "",
                "image/jpeg", "FlateDecode", "2015-08-31T13:33:33.000+03:00", "2014-08-15T17:17:58.000Z", "ù•8r‚‰$ŠKåŒêlŸm}", "26862")));
        assertTrue(collection.getFeatureTreesForType(FeaturesObjectTypesEnum.EMBEDDED_FILE).contains(getEmbeddedFileNode("file3", "XMP - 8.xml", "",
                "text/xml", "FlateDecode", "2015-08-31T13:33:38.000+03:00", "2015-08-20T12:24:50.000Z", "\u0006\u0005¼ä\u0017Uw\r⁄©>ñ8\u000EnÔ", "876")));
        assertTrue(collection.getFeatureTreesForType(FeaturesObjectTypesEnum.EMBEDDED_FILE).contains(getEmbeddedFileNode("file4", "fox_1.jpg", "Some Description for embedded file",
                "image/jpeg", "FlateDecode", "2015-08-22T14:01:19.000+03:00", "2014-09-08T12:01:07.000Z", "ËÓþVf\u0007ç`ºŁåk\u0015?A\r", "67142")));

        assertEquals(5, collection.getFeatureTreesForType(FeaturesObjectTypesEnum.ICCPROFILE).size());
        Set<String> outInts19 = new HashSet<>();
        outInts19.add("outIntDir0");
        assertTrue(collection.getFeatureTreesForType(FeaturesObjectTypesEnum.ICCPROFILE).contains(getICCProfile("iccProfileIndir19",
                outInts19, null, "2.1.0", "ADBE", "RGB ", "ADBE", "2000-08-11T19:52:24.000+03:00", null, "Copyright 2000 Adobe Systems Incorporated",
                "Apple RGB", null, null, "none", getMetadataBytesFromFile("/iccprofile19_metadata_bytes"))));
        Set<String> iccbsds81 = new HashSet<>();
        iccbsds81.add("clrspDir10");
        assertTrue(collection.getFeatureTreesForType(FeaturesObjectTypesEnum.ICCPROFILE).contains(getICCProfile("iccProfileIndir81",
                null, iccbsds81, "2.1.0", "ADBE", "RGB ", "ADBE", "2000-08-11T19:54:18.000+03:00", null, "Copyright 2000 Adobe Systems Incorporated",
                "PAL/SECAM", null, null, "none", null)));
        Set<String> iccbsds84 = new HashSet<>();
        iccbsds84.add("clrspDir27");
        iccbsds84.add("clrspDir14");
        iccbsds84.add("clrspDir26");
        assertTrue(collection.getFeatureTreesForType(FeaturesObjectTypesEnum.ICCPROFILE).contains(getICCProfile("iccProfileIndir84",
                null, iccbsds84, "2.2.0", "appl", "RGB ", "appl", "2000-08-13T16:06:07.000+03:00", "\u0000\u0000\u0000\u0001", "Copyright 1998 - 2003 Apple Computer Inc., all rights reserved.",
                "sRGB Profile", null, null, "appl", null)));
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
        return FeatureTreeNode.newRootInstanceWIthValue(METADATA, getMetadataBytesFromFile("/metadata_bytes"));
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
