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

    public static FeatureTreeNode getFont0() throws FeatureParsingException {
        FeatureTreeNode font = FeatureTreeNode.createRootNode("font");
        font.setAttribute("id", "fntIndir90");
        FeatureTreeNode parents = FeatureTreeNode.createChildNode("parents", font);
        FeatureTreeNode page = FeatureTreeNode.createChildNode("page", parents);
        page.setAttribute("id", "page1");
        FeatureTreeNode type = FeatureTreeNode.createChildNode("type", font);
        type.setValue("Type1");
        FeatureTreeNode baseFont = FeatureTreeNode.createChildNode("baseFont", font);
        baseFont.setValue("OLXYQW+MyriadPro-Regular");
        FeatureTreeNode firstChar = FeatureTreeNode.createChildNode("firstChar", font);
        firstChar.setValue("32");
        FeatureTreeNode lastChar = FeatureTreeNode.createChildNode("lastChar", font);
        lastChar.setValue("121");
        FeatureTreeNode widths = FeatureTreeNode.createChildNode("widths", font);
        String[] chars = new String[]{"32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59", "60", "61", "62", "63", "64", "65", "66", "67", "68", "69", "70", "71", "72", "73", "74", "75", "76", "77", "78", "79", "80", "81", "82", "83", "84", "85", "86", "87", "88", "89", "90", "91", "92", "93", "94", "95", "96", "97", "98", "99", "100", "101", "102", "103", "104", "105", "106", "107", "108", "109", "110", "111", "112", "113", "114", "115", "116", "117", "118", "119", "120", "121"};
        String[] values = new String[]{"212", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "513", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "492", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "497", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "569", "0", "564", "501", "292", "0", "0", "0", "0", "0", "0", "834", "555", "549", "569", "0", "0", "0", "331", "0", "0", "0", "0", "471"};
        for (int i = 0; i < values.length; ++i) {
            FeatureTreeNode width = FeatureTreeNode.createChildNode("width", widths);
            width.setValue(values[i]);
            width.setAttribute("char", chars[i]);
        }
        FeatureTreeNode encoding = FeatureTreeNode.createChildNode("encoding", font);
        encoding.setValue("WinAnsiEncoding");
        FeatureTreeNode fontDescriptor = FeatureTreeNode.createChildNode("fontDescriptor", font);
        FeatureTreeNode fontName = FeatureTreeNode.createChildNode("fontName", fontDescriptor);
        fontName.setValue("OLXYQW+MyriadPro-Regular");
        FeatureTreeNode fontFamily = FeatureTreeNode.createChildNode("fontFamily", fontDescriptor);
        fontFamily.setValue("Myriad Pro");
        FeatureTreeNode fontStretch = FeatureTreeNode.createChildNode("fontStretch", fontDescriptor);
        fontStretch.setValue("Normal");
        FeatureTreeNode fontWeight = FeatureTreeNode.createChildNode("fontWeight", fontDescriptor);
        fontWeight.setValue("400.0");
        FeatureTreeNode fixedPitch = FeatureTreeNode.createChildNode("fixedPitch", fontDescriptor);
        fixedPitch.setValue("false");
        FeatureTreeNode serif = FeatureTreeNode.createChildNode("serif", fontDescriptor);
        serif.setValue("false");
        FeatureTreeNode symbolic = FeatureTreeNode.createChildNode("symbolic", fontDescriptor);
        symbolic.setValue("false");
        FeatureTreeNode script = FeatureTreeNode.createChildNode("script", fontDescriptor);
        script.setValue("false");
        FeatureTreeNode nonsymbolic = FeatureTreeNode.createChildNode("nonsymbolic", fontDescriptor);
        nonsymbolic.setValue("true");
        FeatureTreeNode italic = FeatureTreeNode.createChildNode("italic", fontDescriptor);
        italic.setValue("false");
        FeatureTreeNode allCap = FeatureTreeNode.createChildNode("allCap", fontDescriptor);
        allCap.setValue("false");
        FeatureTreeNode smallCap = FeatureTreeNode.createChildNode("smallCap", fontDescriptor);
        smallCap.setValue("false");
        FeatureTreeNode forceBold = FeatureTreeNode.createChildNode("forceBold", fontDescriptor);
        forceBold.setValue("false");
        FeatureTreeNode fontBBox = FeatureTreeNode.createChildNode("fontBBox", fontDescriptor);
        fontBBox.setAttribute("lly", "-250.0");
        fontBBox.setAttribute("llx", "-157.0");
        fontBBox.setAttribute("urx", "1126.0");
        fontBBox.setAttribute("ury", "952.0");
        FeatureTreeNode italicAngle = FeatureTreeNode.createChildNode("italicAngle", fontDescriptor);
        italicAngle.setValue("0.0");
        FeatureTreeNode ascent = FeatureTreeNode.createChildNode("ascent", fontDescriptor);
        ascent.setValue("952.0");
        FeatureTreeNode descent = FeatureTreeNode.createChildNode("descent", fontDescriptor);
        descent.setValue("-250.0");
        FeatureTreeNode leading = FeatureTreeNode.createChildNode("leading", fontDescriptor);
        leading.setValue("0.0");
        FeatureTreeNode capHeight = FeatureTreeNode.createChildNode("capHeight", fontDescriptor);
        capHeight.setValue("674.0");
        FeatureTreeNode xHeight = FeatureTreeNode.createChildNode("xHeight", fontDescriptor);
        xHeight.setValue("484.0");
        FeatureTreeNode stemV = FeatureTreeNode.createChildNode("stemV", fontDescriptor);
        stemV.setValue("88.0");
        FeatureTreeNode stemH = FeatureTreeNode.createChildNode("stemH", fontDescriptor);
        stemH.setValue("0.0");
        FeatureTreeNode averageWidth = FeatureTreeNode.createChildNode("averageWidth", fontDescriptor);
        averageWidth.setValue("0.0");
        FeatureTreeNode maxWidth = FeatureTreeNode.createChildNode("maxWidth", fontDescriptor);
        maxWidth.setValue("0.0");
        FeatureTreeNode missingWidth = FeatureTreeNode.createChildNode("missingWidth", fontDescriptor);
        missingWidth.setValue("0.0");
        FeatureTreeNode charSet = FeatureTreeNode.createChildNode("charSet", fontDescriptor);
        charSet.setValue("/space/one/E/T/b/d/e/f/m/n/o/p/t/y");
        FeatureTreeNode embedded = FeatureTreeNode.createChildNode("embedded", fontDescriptor);
        embedded.setValue("true");
        return font;
    }

    public static FeatureTreeNode getFont1() throws FeatureParsingException {
        FeatureTreeNode font = FeatureTreeNode.createRootNode("font");
        font.setAttribute("id", "fntDir1");
        FeatureTreeNode parents = FeatureTreeNode.createChildNode("parents", font);
        FeatureTreeNode font1 = FeatureTreeNode.createChildNode("font", parents);
        font1.setAttribute("id", "fntIndir88");
        FeatureTreeNode type = FeatureTreeNode.createChildNode("type", font);
        type.setValue("CIDFontType2");
        FeatureTreeNode baseFont = FeatureTreeNode.createChildNode("baseFont", font);
        baseFont.setValue("CBTOEA+ArialMT");
        FeatureTreeNode defaultWidth = FeatureTreeNode.createChildNode("defaultWidth", font);
        defaultWidth.setValue("750");
        FeatureTreeNode cidSystemInfo = FeatureTreeNode.createChildNode("cidSystemInfo", font);
        FeatureTreeNode registry = FeatureTreeNode.createChildNode("registry", cidSystemInfo);
        registry.setValue("Adobe");
        FeatureTreeNode ordering = FeatureTreeNode.createChildNode("ordering", cidSystemInfo);
        ordering.setValue("Identity");
        FeatureTreeNode supplement = FeatureTreeNode.createChildNode("supplement", cidSystemInfo);
        supplement.setValue("0");
        FeatureTreeNode fontDescriptor = FeatureTreeNode.createChildNode("fontDescriptor", font);
        FeatureTreeNode fontName = FeatureTreeNode.createChildNode("fontName", fontDescriptor);
        fontName.setValue("Arial");
        FeatureTreeNode fontWeight = FeatureTreeNode.createChildNode("fontWeight", fontDescriptor);
        fontWeight.setValue("0.0");
        FeatureTreeNode fixedPitch = FeatureTreeNode.createChildNode("fixedPitch", fontDescriptor);
        fixedPitch.setValue("false");
        FeatureTreeNode serif = FeatureTreeNode.createChildNode("serif", fontDescriptor);
        serif.setValue("false");
        FeatureTreeNode symbolic = FeatureTreeNode.createChildNode("symbolic", fontDescriptor);
        symbolic.setValue("false");
        FeatureTreeNode script = FeatureTreeNode.createChildNode("script", fontDescriptor);
        script.setValue("false");
        FeatureTreeNode nonsymbolic = FeatureTreeNode.createChildNode("nonsymbolic", fontDescriptor);
        nonsymbolic.setValue("true");
        FeatureTreeNode italic = FeatureTreeNode.createChildNode("italic", fontDescriptor);
        italic.setValue("false");
        FeatureTreeNode allCap = FeatureTreeNode.createChildNode("allCap", fontDescriptor);
        allCap.setValue("false");
        FeatureTreeNode smallCap = FeatureTreeNode.createChildNode("smallCap", fontDescriptor);
        smallCap.setValue("false");
        FeatureTreeNode forceBold = FeatureTreeNode.createChildNode("forceBold", fontDescriptor);
        forceBold.setValue("false");
        FeatureTreeNode fontBBox = FeatureTreeNode.createChildNode("fontBBox", fontDescriptor);
        fontBBox.setAttribute("lly", "-325.0");
        fontBBox.setAttribute("llx", "-665.0");
        fontBBox.setAttribute("urx", "2000.0");
        fontBBox.setAttribute("ury", "1006.0");
        FeatureTreeNode italicAngle = FeatureTreeNode.createChildNode("italicAngle", fontDescriptor);
        italicAngle.setValue("0.0");
        FeatureTreeNode ascent = FeatureTreeNode.createChildNode("ascent", fontDescriptor);
        ascent.setValue("728.0");
        FeatureTreeNode descent = FeatureTreeNode.createChildNode("descent", fontDescriptor);
        descent.setValue("-210.0");
        FeatureTreeNode leading = FeatureTreeNode.createChildNode("leading", fontDescriptor);
        leading.setValue("0.0");
        FeatureTreeNode capHeight = FeatureTreeNode.createChildNode("capHeight", fontDescriptor);
        capHeight.setValue("677.0");
        FeatureTreeNode xHeight = FeatureTreeNode.createChildNode("xHeight", fontDescriptor);
        xHeight.setValue("480.0");
        FeatureTreeNode stemV = FeatureTreeNode.createChildNode("stemV", fontDescriptor);
        stemV.setValue("88.0");
        FeatureTreeNode stemH = FeatureTreeNode.createChildNode("stemH", fontDescriptor);
        stemH.setValue("0.0");
        FeatureTreeNode averageWidth = FeatureTreeNode.createChildNode("averageWidth", fontDescriptor);
        averageWidth.setValue("0.0");
        FeatureTreeNode maxWidth = FeatureTreeNode.createChildNode("maxWidth", fontDescriptor);
        maxWidth.setValue("0.0");
        FeatureTreeNode missingWidth = FeatureTreeNode.createChildNode("missingWidth", fontDescriptor);
        missingWidth.setValue("0.0");
        FeatureTreeNode embedded = FeatureTreeNode.createChildNode("embedded", fontDescriptor);
        embedded.setValue("true");
        return font;
    }

    public static FeatureTreeNode getFont2() throws FeatureParsingException {
        FeatureTreeNode font = FeatureTreeNode.createRootNode("font");
        font.setAttribute("id", "fntDir3");
        FeatureTreeNode parents = FeatureTreeNode.createChildNode("parents", font);
        FeatureTreeNode font1 = FeatureTreeNode.createChildNode("font", parents);
        font1.setAttribute("id", "fntIndir89");
        FeatureTreeNode type = FeatureTreeNode.createChildNode("type", font);
        type.setValue("CIDFontType0");
        FeatureTreeNode baseFont = FeatureTreeNode.createChildNode("baseFont", font);
        baseFont.setValue("IIWNIN+AdobeFanHeitiStd-Bold");
        FeatureTreeNode defaultWidth = FeatureTreeNode.createChildNode("defaultWidth", font);
        defaultWidth.setValue("1000");
        FeatureTreeNode cidSystemInfo = FeatureTreeNode.createChildNode("cidSystemInfo", font);
        FeatureTreeNode registry = FeatureTreeNode.createChildNode("registry", cidSystemInfo);
        registry.setValue("Adobe");
        FeatureTreeNode ordering = FeatureTreeNode.createChildNode("ordering", cidSystemInfo);
        ordering.setValue("CNS1");
        FeatureTreeNode supplement = FeatureTreeNode.createChildNode("supplement", cidSystemInfo);
        supplement.setValue("6");
        FeatureTreeNode fontDescriptor = FeatureTreeNode.createChildNode("fontDescriptor", font);
        FeatureTreeNode fontName = FeatureTreeNode.createChildNode("fontName", fontDescriptor);
        fontName.setValue("IIWNIN+AdobeFanHeitiStd-Bold");
        FeatureTreeNode fontFamily = FeatureTreeNode.createChildNode("fontFamily", fontDescriptor);
        fontFamily.setValue("Adobe Fan Heiti Std B");
        FeatureTreeNode fontStretch = FeatureTreeNode.createChildNode("fontStretch", fontDescriptor);
        fontStretch.setValue("Normal");
        FeatureTreeNode fontWeight = FeatureTreeNode.createChildNode("fontWeight", fontDescriptor);
        fontWeight.setValue("600.0");
        FeatureTreeNode fixedPitch = FeatureTreeNode.createChildNode("fixedPitch", fontDescriptor);
        fixedPitch.setValue("false");
        FeatureTreeNode serif = FeatureTreeNode.createChildNode("serif", fontDescriptor);
        serif.setValue("false");
        FeatureTreeNode symbolic = FeatureTreeNode.createChildNode("symbolic", fontDescriptor);
        symbolic.setValue("true");
        FeatureTreeNode script = FeatureTreeNode.createChildNode("script", fontDescriptor);
        script.setValue("false");
        FeatureTreeNode nonsymbolic = FeatureTreeNode.createChildNode("nonsymbolic", fontDescriptor);
        nonsymbolic.setValue("false");
        FeatureTreeNode italic = FeatureTreeNode.createChildNode("italic", fontDescriptor);
        italic.setValue("false");
        FeatureTreeNode allCap = FeatureTreeNode.createChildNode("allCap", fontDescriptor);
        allCap.setValue("false");
        FeatureTreeNode smallCap = FeatureTreeNode.createChildNode("smallCap", fontDescriptor);
        smallCap.setValue("false");
        FeatureTreeNode forceBold = FeatureTreeNode.createChildNode("forceBold", fontDescriptor);
        forceBold.setValue("false");
        FeatureTreeNode fontBBox = FeatureTreeNode.createChildNode("fontBBox", fontDescriptor);
        fontBBox.setAttribute("lly", "-283.0");
        fontBBox.setAttribute("llx", "-163.0");
        fontBBox.setAttribute("urx", "1087.0");
        fontBBox.setAttribute("ury", "967.0");
        FeatureTreeNode italicAngle = FeatureTreeNode.createChildNode("italicAngle", fontDescriptor);
        italicAngle.setValue("0.0");
        FeatureTreeNode ascent = FeatureTreeNode.createChildNode("ascent", fontDescriptor);
        ascent.setValue("967.0");
        FeatureTreeNode descent = FeatureTreeNode.createChildNode("descent", fontDescriptor);
        descent.setValue("-283.0");
        FeatureTreeNode leading = FeatureTreeNode.createChildNode("leading", fontDescriptor);
        leading.setValue("0.0");
        FeatureTreeNode capHeight = FeatureTreeNode.createChildNode("capHeight", fontDescriptor);
        capHeight.setValue("766.0");
        FeatureTreeNode xHeight = FeatureTreeNode.createChildNode("xHeight", fontDescriptor);
        xHeight.setValue("551.0");
        FeatureTreeNode stemV = FeatureTreeNode.createChildNode("stemV", fontDescriptor);
        stemV.setValue("116.0");
        FeatureTreeNode stemH = FeatureTreeNode.createChildNode("stemH", fontDescriptor);
        stemH.setValue("0.0");
        FeatureTreeNode averageWidth = FeatureTreeNode.createChildNode("averageWidth", fontDescriptor);
        averageWidth.setValue("0.0");
        FeatureTreeNode maxWidth = FeatureTreeNode.createChildNode("maxWidth", fontDescriptor);
        maxWidth.setValue("1000.0");
        FeatureTreeNode missingWidth = FeatureTreeNode.createChildNode("missingWidth", fontDescriptor);
        missingWidth.setValue("800.0");
        FeatureTreeNode embedded = FeatureTreeNode.createChildNode("embedded", fontDescriptor);
        embedded.setValue("true");

        return font;
    }

    public static FeatureTreeNode getErrorNode(String id, String value) throws FeatureParsingException {
        FeatureTreeNode res = FeatureTreeNode.createRootNode("error");
        res.setValue(value);
        res.setAttribute(ID, id);
        return res;
    }

    public static FeatureTreeNode getTilingPattern() throws FeatureParsingException {
        FeatureTreeNode root = FeatureTreeNode.createRootNode("pattern");
        root.setAttribute(ID, "ptrnIndir49");
        root.setAttribute("type", "tiling");
        FeatureTreeNode parents = FeatureTreeNode.createChildNode("parents", root);
        FeatureTreeNode page = FeatureTreeNode.createChildNode("page", parents);
        page.setAttribute(ID, "page1");
        FeatureTreeNode.createChildNode("paintType", root).setValue("2");
        FeatureTreeNode.createChildNode("tilingType", root).setValue("1");
        FeatureTreeNode bbox = FeatureTreeNode.createChildNode("bbox", root);
        bbox.setAttribute(LLX, "0.0");
        bbox.setAttribute(LLY, "0.0");
        bbox.setAttribute(URX, "5.0");
        bbox.setAttribute(URY, "10.0");
        FeatureTreeNode.createChildNode("xStep", root).setValue("5.0");
        FeatureTreeNode.createChildNode("yStep", root).setValue("10.0");
        getStandartMatrix(root);
        FeatureTreeNode resources = FeatureTreeNode.createChildNode("resources", root);
        FeatureTreeNode colorSpaces = FeatureTreeNode.createChildNode("colorSpaces", resources);
        FeatureTreeNode clr2 = FeatureTreeNode.createChildNode("colorSpace", colorSpaces);
        clr2.setAttribute(ID, "clrspDir19");
        FeatureTreeNode clr1 = FeatureTreeNode.createChildNode("colorSpace", colorSpaces);
        clr1.setAttribute(ID, "clrspDir20");
        FeatureTreeNode xobjects = FeatureTreeNode.createChildNode("xobjects", resources);
        FeatureTreeNode xobj2 = FeatureTreeNode.createChildNode("xobject", xobjects);
        xobj2.setAttribute(ID, "xobjIndir60");
        FeatureTreeNode xobj1 = FeatureTreeNode.createChildNode("xobject", xobjects);
        xobj1.setAttribute(ID, "xobjIndir56");
        return root;
    }

    public static FeatureTreeNode getShadingPattern() throws FeatureParsingException {
        FeatureTreeNode root = FeatureTreeNode.createRootNode("pattern");
        root.setAttribute(ID, "ptrnIndir50");
        root.setAttribute("type", "shading");
        FeatureTreeNode parents = FeatureTreeNode.createChildNode("parents", root);
        FeatureTreeNode page = FeatureTreeNode.createChildNode("page", parents);
        page.setAttribute(ID, "page1");
        FeatureTreeNode font = FeatureTreeNode.createChildNode("font", parents);
        font.setAttribute(ID, "fntIndir91");
        FeatureTreeNode shading = FeatureTreeNode.createChildNode("shading", root);
        shading.setAttribute(ID, "shdngIndir52");
        getStandartMatrix(root);
        FeatureTreeNode gst = FeatureTreeNode.createChildNode("graphicsState", root);
        gst.setAttribute(ID, "exGStIndir93");
        return root;
    }

    public static void getStandartMatrix(FeatureTreeNode root) throws FeatureParsingException {
        FeatureTreeNode matr = FeatureTreeNode.createChildNode("matrix", root);
        addElement("1", "1", "1.0", matr);
        addElement("2", "1", "0.0", matr);
        addElement("1", "2", "0.0", matr);
        addElement("2", "2", "1.0", matr);
        addElement("1", "3", "0.0", matr);
        addElement("2", "3", "0.0", matr);
    }

    public static void addElement(String column, String row, String value, FeatureTreeNode parent) throws FeatureParsingException {
        FeatureTreeNode element = FeatureTreeNode.createChildNode("element", parent);
        element.setAttribute("column", column);
        element.setAttribute("row", row);
        element.setAttribute("value", value);
    }

    public static FeatureTreeNode getShading() throws FeatureParsingException {
        FeatureTreeNode root = FeatureTreeNode.createRootNode("shading");
        root.setAttribute(ID, "shdngIndir52");
        FeatureTreeNode parents = FeatureTreeNode.createChildNode("parents", root);
        FeatureTreeNode page = FeatureTreeNode.createChildNode("page", parents);
        page.setAttribute(ID, "page1");
        FeatureTreeNode pattern = FeatureTreeNode.createChildNode("pattern", parents);
        pattern.setAttribute(ID, "ptrnIndir50");
        FeatureTreeNode font = FeatureTreeNode.createChildNode("font", parents);
        font.setAttribute(ID, "fntIndir91");
        FeatureTreeNode.createChildNode("shadingType", root).setValue("2");
        FeatureTreeNode clr = FeatureTreeNode.createChildNode("colorSpace", root);
        clr.setAttribute(ID, "devrgb");
        FeatureTreeNode bbox = FeatureTreeNode.createChildNode("bbox", root);
        bbox.setAttribute(LLX, "0.0");
        bbox.setAttribute(LLY, "0.0");
        bbox.setAttribute(URX, "400.0");
        bbox.setAttribute(URY, "400.0");
        FeatureTreeNode.createChildNode("antiAlias", root).setValue("false");
        return root;
    }

    public static FeatureTreeNode getFailedXObject(String id, String errorid) throws FeatureParsingException {
        FeatureTreeNode root = FeatureTreeNode.createRootNode("xobject");
        root.setAttribute(ID, id);
        root.setAttribute("errorId", errorid);
        return root;
    }

    public static FeatureTreeNode getProperties() throws FeatureParsingException {
        FeatureTreeNode root = FeatureTreeNode.createRootNode("propertiesDict");
        root.setAttribute(ID, "propDir0");
        FeatureTreeNode parents = FeatureTreeNode.createChildNode("parents", root);
        FeatureTreeNode page = FeatureTreeNode.createChildNode("page", parents);
        page.setAttribute(ID, "page1");
        return root;
    }

    public static FeatureTreeNode getGraphicsState(String id, String page, String pattern, String xobject1, String xobject2, String font, String transparency, String strokeAdjustment,
                                                   String overprintForStroke, String overprintForFill, String fontChild) throws FeatureParsingException {
        FeatureTreeNode root = FeatureTreeNode.createRootNode("graphicsState");
        root.setAttribute(ID, id);
        FeatureTreeNode parents = FeatureTreeNode.createChildNode("parents", root);
        if (page != null) {
            FeatureTreeNode pageNode = FeatureTreeNode.createChildNode("page", parents);
            pageNode.setAttribute(ID, page);
        }
        if (pattern != null) {
            FeatureTreeNode patternNode = FeatureTreeNode.createChildNode("pattern", parents);
            patternNode.setAttribute(ID, pattern);
        }
        if (xobject1 != null) {
            FeatureTreeNode xobjectNode = FeatureTreeNode.createChildNode("xobject", parents);
            xobjectNode.setAttribute(ID, xobject1);
        }
        if (xobject2 != null) {
            FeatureTreeNode xobjectNode = FeatureTreeNode.createChildNode("xobject", parents);
            xobjectNode.setAttribute(ID, xobject2);
        }
        if (font != null) {
            FeatureTreeNode fontNode = FeatureTreeNode.createChildNode("font", parents);
            fontNode.setAttribute(ID, font);
        }


        FeatureTreeNode.createChildNode("transparency", root).setValue(transparency);
        FeatureTreeNode.createChildNode("strokeAdjustment", root).setValue(strokeAdjustment);
        FeatureTreeNode.createChildNode("overprintForStroke", root).setValue(overprintForStroke);
        FeatureTreeNode.createChildNode("overprintForFill", root).setValue(overprintForFill);

        if (fontChild != null) {
            FeatureTreeNode res = FeatureTreeNode.createChildNode("resources", root);
            FeatureTreeNode fons = FeatureTreeNode.createChildNode("fonts", res);
            FeatureTreeNode fon = FeatureTreeNode.createChildNode("font", fons);
            fon.setAttribute(ID, fontChild);
        }
        return root;
    }

    public static FeatureTreeNode getPage() throws FeatureParsingException, URISyntaxException, FileNotFoundException {
        FeatureTreeNode root = FeatureTreeNode.createRootNode("page");
        root.setAttribute(ID, "page1");
        root.setAttribute("orderNumber", "1");

        addBox("mediaBox", root);
        addBox("cropBox", root);
        addBox("trimBox", root);
        addBox("bleedBox", root);
        addBox("artBox", root);
        FeatureTreeNode.createChildNode("rotation", root).setValue("0");
        FeatureTreeNode.createChildNode("scaling", root).setValue("75.0");
        FeatureTreeNode thumb = FeatureTreeNode.createChildNode("thumbnail", root);
        thumb.setAttribute(ID, "xobjIndir126");
        FeatureTreeNode.createChildMetadataNode("metadata", root).setValue(DatatypeConverter.printHexBinary(getMetadataBytesFromFile("/page1_metadata_bytes.txt")));

        List<String> annotations = new ArrayList<>();
        annotations.add("annotIndir13");
        annotations.add("annotIndir42");
        annotations.add("annotIndir37");
        annotations.add("annotIndir38");
        annotations.add("annotIndir39");
        annotations.add("annotIndir41");
        annotations.add("annotIndir40");
        makeList("annotation", annotations, root);

        FeatureTreeNode resources = FeatureTreeNode.createChildNode("resources", root);

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
        FeatureTreeNode head = FeatureTreeNode.createChildNode(name + "s", parent);
        for (String el : values) {
            FeatureTreeNode element = FeatureTreeNode.createChildNode(name, head);
            element.setAttribute(ID, el);
        }
    }

    public static void addBox(String name, FeatureTreeNode parent) throws FeatureParsingException {
        FeatureTreeNode box = FeatureTreeNode.createChildNode(name, parent);
        box.setAttribute(LLX, "0.0");
        box.setAttribute(LLY, "0.0");
        box.setAttribute(URX, "499.977");
        box.setAttribute(URY, "499.977");
    }

    public static FeatureTreeNode getAnnotation(String id, String parentPage, String parentAnnotation,
                                                String subtype, String llx, String lly, String urx, String ury,
                                                String contents, String annotationName, String modifiedDate, Set<String> xobj,
                                                String popup, String red, String green, String blue, String kayan,
                                                String invisible, String hidden, String print, String noZoom,
                                                String noRotate, String noView, String readOnly, String locked,
                                                String toggleNoView, String lockedContents) throws FeatureParsingException {
        FeatureTreeNode root = FeatureTreeNode.createRootNode("annotation");
        root.setAttribute(ID, id);
        if (parentPage != null || parentAnnotation != null) {
            FeatureTreeNode parents = FeatureTreeNode.createChildNode("parents", root);
            if (parentPage != null) {
                FeatureTreeNode page = FeatureTreeNode.createChildNode("page", parents);
                page.setAttribute(ID, parentPage);
            }
            if (parentAnnotation != null) {
                FeatureTreeNode annot = FeatureTreeNode.createChildNode("annotation", parents);
                annot.setAttribute(ID, parentAnnotation);
            }
        }
        PBCreateNodeHelper.addNotEmptyNode("subType", subtype, root);
        FeatureTreeNode rec = FeatureTreeNode.createChildNode("rectangle", root);
        rec.setAttribute(LLX, llx);
        rec.setAttribute(LLY, lly);
        rec.setAttribute(URX, urx);
        rec.setAttribute(URY, ury);
        PBCreateNodeHelper.addNotEmptyNode("contents", contents, root);
        PBCreateNodeHelper.addNotEmptyNode("annotationName", annotationName, root);
        PBCreateNodeHelper.addNotEmptyNode("modifiedDate", modifiedDate, root);

        if (xobj != null && !xobj.isEmpty()) {
            FeatureTreeNode resources = FeatureTreeNode.createChildNode("resources", root);
            for (String objID : xobj) {
                FeatureTreeNode node = FeatureTreeNode.createChildNode("xobject", resources);
                node.setAttribute(ID, objID);
            }
        }
        if (popup != null) {
            FeatureTreeNode pop = FeatureTreeNode.createChildNode("popup", root);
            pop.setAttribute(ID, popup);
        }

        if (red != null && green != null && blue != null && kayan != null) {
            FeatureTreeNode color = FeatureTreeNode.createChildNode("color", root);
            color.setAttribute("cyan", red);
            color.setAttribute("magenta", green);
            color.setAttribute("yellow", blue);
            color.setAttribute("black", blue);
        } else if (red != null && green != null && blue != null) {
            FeatureTreeNode color = FeatureTreeNode.createChildNode("color", root);
            color.setAttribute("red", red);
            color.setAttribute("green", green);
            color.setAttribute("blue", blue);
        } else if (red != null) {
            FeatureTreeNode color = FeatureTreeNode.createChildNode("color", root);
            color.setAttribute("gray", red);
        }

        FeatureTreeNode.createChildNode("invisible", root).setValue(invisible);
        FeatureTreeNode.createChildNode("hidden", root).setValue(hidden);
        FeatureTreeNode.createChildNode("print", root).setValue(print);
        FeatureTreeNode.createChildNode("noZoom", root).setValue(noZoom);
        FeatureTreeNode.createChildNode("noRotate", root).setValue(noRotate);
        FeatureTreeNode.createChildNode("noView", root).setValue(noView);
        FeatureTreeNode.createChildNode("readOnly", root).setValue(readOnly);
        FeatureTreeNode.createChildNode("locked", root).setValue(locked);
        FeatureTreeNode.createChildNode("toggleNoView", root).setValue(toggleNoView);
        FeatureTreeNode.createChildNode("lockedContents", root).setValue(lockedContents);

        return root;
    }

    public static FeatureTreeNode getOutlines() throws FeatureParsingException {
        FeatureTreeNode root = FeatureTreeNode.createRootNode("outlines");
        FeatureTreeNode out1 = FeatureTreeNode.createChildNode("outline", root);
        makeOutline("1 - COLOR", "1.0", "0.0", "0.0", "false", "false", out1);
        FeatureTreeNode out1_1 = FeatureTreeNode.createChildNode("outline", out1);
        makeOutline("1.1", "0.0", "0.0", "1.0", "false", "false", out1_1);
        FeatureTreeNode out2 = FeatureTreeNode.createChildNode("outline", root);
        makeOutline("2 - ITALIC", "0.0", "0.0", "0.0", "true", "false", out2);
        FeatureTreeNode out2_2 = FeatureTreeNode.createChildNode("outline", out2);
        makeOutline("2.2", "0.0", "0.0", "0.0", "true", "false", out2_2);
        FeatureTreeNode out2_2_1 = FeatureTreeNode.createChildNode("outline", out2_2);
        makeOutline("2.2.1", "0.0", "0.0", "0.0", "true", "false", out2_2_1);
        FeatureTreeNode out2_2_2 = FeatureTreeNode.createChildNode("outline", out2_2);
        makeOutline("2.2.2", "0.0", "0.0", "0.0", "true", "false", out2_2_2);
        FeatureTreeNode out2_2_2_1 = FeatureTreeNode.createChildNode("outline", out2_2_2);
        makeOutline("2.2.2.1", "0.0", "0.0", "0.0", "true", "false", out2_2_2_1);
        FeatureTreeNode out2_1 = FeatureTreeNode.createChildNode("outline", out2);
        makeOutline("2.1", "0.0", "0.0", "0.0", "true", "false", out2_1);
        FeatureTreeNode out3 = FeatureTreeNode.createChildNode("outline", root);
        makeOutline("3 - BOLD", "0.0", "0.0", "0.0", "false", "true", out3);
        FeatureTreeNode out4 = FeatureTreeNode.createChildNode("outline", root);
        makeOutline("4", "0.0", "0.0", "0.0", "false", "false", out4);
        return root;
    }

    public static void makeOutline(String title, String red, String green, String blue, String italic, String bold, FeatureTreeNode root) throws FeatureParsingException {
        FeatureTreeNode.createChildNode("title", root).setValue(title);
        FeatureTreeNode color = FeatureTreeNode.createChildNode("color", root);
        color.setAttribute("red", red);
        color.setAttribute("green", green);
        color.setAttribute("blue", blue);
        FeatureTreeNode style = FeatureTreeNode.createChildNode("style", root);
        style.setAttribute("italic", italic);
        style.setAttribute("bold", bold);
    }

    public static FeatureTreeNode getOutputIntent() throws FeatureParsingException {
        FeatureTreeNode root = FeatureTreeNode.createRootNode("outputIntent");
        root.setAttribute(ID, "outIntDir0");
        FeatureTreeNode.createChildNode("subtype", root).setValue("GTS_PDFA1");
        FeatureTreeNode.createChildNode("outputCondition", root).setValue("SomeOutputCondition");
        FeatureTreeNode.createChildNode("outputConditionIdentifier", root).setValue("Apple RGB");
        FeatureTreeNode.createChildNode("registryName", root).setValue("fxqn:/us/va/reston/cnri/ietf/24/asdf%*.fred");
        FeatureTreeNode.createChildNode("info", root).setValue("RGB");
        FeatureTreeNode dest = FeatureTreeNode.createChildNode("destOutputIntent", root);
        dest.setAttribute(ID, "iccProfileIndir19");
        return root;
    }

    public static FeatureTreeNode getICCProfile(String id, Set<String> outInts, Set<String> iccBaseds, String version,
                                                String cmmType, String dataColorSpace, String creator, String creationDate,
                                                String defaultRenderingIntent, String copyright, String description,
                                                String profileId, String deviceModel, String deviceManufacturer, byte[] metadata) throws FeatureParsingException {
        FeatureTreeNode root = FeatureTreeNode.createRootNode("iccProfile");
        root.setAttribute(ID, id);

        if ((outInts != null && !outInts.isEmpty()) || (iccBaseds != null && !iccBaseds.isEmpty())) {
            FeatureTreeNode parents = FeatureTreeNode.createChildNode("parents", root);
            if (outInts != null) {
                for (String out : outInts) {
                    FeatureTreeNode outNode = FeatureTreeNode.createChildNode("outputIntent", parents);
                    outNode.setAttribute(ID, out);
                }
            }
            if (iccBaseds != null) {
                for (String icc : iccBaseds) {
                    FeatureTreeNode iccNode = FeatureTreeNode.createChildNode("iccBased", parents);
                    iccNode.setAttribute(ID, icc);
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
            FeatureTreeNode.createChildMetadataNode("metadata", root).setValue(DatatypeConverter.printHexBinary(metadata));
        }

        return root;
    }

    public static FeatureTreeNode getEmbeddedFileNode(String id, String fileName, String description, String subtype,
                                                      String filter, String creationDate, String modDate, String checkSum,
                                                      String size) throws FeatureParsingException {
        FeatureTreeNode root = FeatureTreeNode.createRootNode("embeddedFile");
        root.setAttribute(ID, id);
        FeatureTreeNode.createChildNode("fileName", root).setValue(fileName);
        FeatureTreeNode.createChildNode("description", root).setValue(description);
        FeatureTreeNode.createChildNode("subtype", root).setValue(subtype);
        FeatureTreeNode.createChildNode("filter", root).setValue(filter);
        FeatureTreeNode.createChildNode("creationDate", root).setValue(creationDate);
        FeatureTreeNode.createChildNode("modDate", root).setValue(modDate);
        FeatureTreeNode.createChildNode("checkSum", root).setValue(checkSum);
        FeatureTreeNode.createChildNode("size", root).setValue(size);
        return root;
    }

    public static FeatureTreeNode getLowLvlInfo() throws FeatureParsingException {
        FeatureTreeNode lli = FeatureTreeNode.createRootNode("lowLevelInfo");
        FeatureTreeNode.createChildNode("indirectObjectsNumber", lli).setValue("129");
        FeatureTreeNode docID = FeatureTreeNode.createChildNode("documentId", lli);
        docID.setAttribute("modificationId", "295EBB0E08D32644B7E5C1825F15AD3A");
        docID.setAttribute("creationId", "85903F3A2C43B1DA24E486CD15B8154E");
        FeatureTreeNode filters = FeatureTreeNode.createChildNode("filters", lli);
        addFilter("FlateDecode", filters);
        addFilter("ASCIIHexDecode", filters);
        addFilter("ASCII85Decode", filters);
        addFilter("CCITTFaxDecode", filters);
        addFilter("DCTDecode", filters);
        return lli;
    }

    public static void addFilter(String name, FeatureTreeNode parent) throws FeatureParsingException {
        FeatureTreeNode filter = FeatureTreeNode.createChildNode("filter", parent);
        filter.setAttribute("name", name);
    }

    public static FeatureTreeNode getInfDictNode() throws FeatureParsingException {
        FeatureTreeNode infDict = FeatureTreeNode.createRootNode("informationDict");
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
        FeatureTreeNode rootMetadataNode = FeatureTreeNode.createRootMetadataNode(METADATA);
        rootMetadataNode.setValue(DatatypeConverter.printHexBinary(getMetadataBytesFromFile("/metadata_bytes.txt")));
        return rootMetadataNode;
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
        FeatureTreeNode entry = FeatureTreeNode.createChildNode(ENTRY, parent);
        entry.setValue(value);
        entry.setAttribute("key", name);
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
            FeatureTreeNode root = FeatureTreeNode.createChildNode("fontDescriptor", parent);

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

            FeatureTreeNode bbox = FeatureTreeNode.createChildNode("fontBBox", root);
            bbox.setAttribute(LLX, fontBBox_llx);
            bbox.setAttribute(LLY, fontBBox_lly);
            bbox.setAttribute(URX, fontBBox_urx);
            bbox.setAttribute(URY, fontBBox_ury);

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
