package org.verapdf.report;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.junit.BeforeClass;
import org.junit.Test;
import org.verapdf.features.pb.PBFeatureParser;
import org.verapdf.features.tools.FeaturesCollection;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;

/**
 * @author Maksim Bezrukov
 */
public class XMLFeaturesReportTest {

    private static DocumentBuilder builder;

    @BeforeClass
    public static void before() throws ParserConfigurationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        builder = factory.newDocumentBuilder();
    }

    public static void test(String path) throws URISyntaxException, IOException, ParserConfigurationException {
        File pdf = new File(getSystemIndependentPath(path));
        PDDocument document = PDDocument.load(pdf, false, true);
        FeaturesCollection collection = PBFeatureParser.getFeaturesCollection(document);
        Document doc = builder.newDocument();

        Element features = XMLFeaturesReport.makeXMLTree(collection, doc);

        assertEquals("metadata", features.getFirstChild().getNextSibling().getNodeName());
        assertEquals("x:xmpmeta", features.getFirstChild().getNextSibling().getFirstChild().getNodeName());

        document.close();
    }

    @Test
    public void testA() throws URISyntaxException, IOException, ParserConfigurationException {
        test("/veraPDF test suite 6-7-9-t04-pass-a.pdf");
    }

    @Test
    public void testB() throws URISyntaxException, IOException, ParserConfigurationException {
        test("/veraPDF test suite 6-7-9-t04-pass-b.pdf");
    }

    @Test
    public void testC() throws URISyntaxException, IOException, ParserConfigurationException {
        test("/veraPDF test suite 6-7-9-t04-pass-c.pdf");
    }

    @Test
    public void testD() throws URISyntaxException, IOException, ParserConfigurationException {
        test("/veraPDF test suite 6-7-9-t04-pass-d.pdf");
    }

    @Test
    public void testE() throws URISyntaxException, IOException, ParserConfigurationException {
        test("/veraPDF test suite 6-7-9-t04-pass-e.pdf");
    }

    @Test
    public void testF() throws URISyntaxException, IOException, ParserConfigurationException {
        test("/veraPDF test suite 6-7-9-t04-pass-f.pdf");
    }

    @Test
    public void testG() throws URISyntaxException, IOException, ParserConfigurationException {
        test("/veraPDF test suite 6-7-9-t04-pass-g.pdf");
    }

    @Test
    public void testH() throws URISyntaxException, IOException, ParserConfigurationException {
        test("/veraPDF test suite 6-7-9-t04-pass-h.pdf");
    }

    @Test
    public void testI() throws URISyntaxException, IOException, ParserConfigurationException {
        test("/veraPDF test suite 6-7-9-t04-pass-i.pdf");
    }

    @Test
    public void testJ() throws URISyntaxException, IOException, ParserConfigurationException {
        test("/veraPDF test suite 6-7-9-t04-pass-j.pdf");
    }

    @Test
    public void testK() throws URISyntaxException, IOException, ParserConfigurationException {
        test("/veraPDF test suite 6-7-9-t04-pass-k.pdf");
    }

    @Test
    public void testL() throws URISyntaxException, IOException, ParserConfigurationException {
        test("/veraPDF test suite 6-7-9-t04-pass-l.pdf");
    }

    @Test
    public void testM() throws URISyntaxException, IOException, ParserConfigurationException {
        test("/veraPDF test suite 6-7-9-t04-pass-m.pdf");
    }

    @Test
    public void testN() throws URISyntaxException, IOException, ParserConfigurationException {
        test("/veraPDF test suite 6-7-9-t04-pass-n.pdf");
    }

    @Test
    public void testO() throws URISyntaxException, IOException, ParserConfigurationException {
        test("/veraPDF test suite 6-7-9-t04-pass-o.pdf");
    }

    @Test
    public void testP() throws URISyntaxException, IOException, ParserConfigurationException {
        test("/veraPDF test suite 6-7-9-t04-pass-p.pdf");
    }

    @Test
    public void testQ() throws URISyntaxException, IOException, ParserConfigurationException {
        test("/veraPDF test suite 6-7-9-t04-pass-q.pdf");
    }

    @Test
    public void testR() throws URISyntaxException, IOException, ParserConfigurationException {
        test("/veraPDF test suite 6-7-9-t04-pass-r.pdf");
    }

    @Test
    public void testS() throws URISyntaxException, IOException, ParserConfigurationException {
        test("/veraPDF test suite 6-7-9-t04-pass-s.pdf");
    }

    @Test
    public void testT() throws URISyntaxException, IOException, ParserConfigurationException {
        test("/veraPDF test suite 6-7-9-t04-pass-t.pdf");
    }

    private static String getSystemIndependentPath(String path) throws URISyntaxException {
        URL resourceUrl = ClassLoader.class.getResource(path);
        Path resourcePath = Paths.get(resourceUrl.toURI());
        return resourcePath.toString();
    }
}
