package org.verapdf.report;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
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
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * @author Maksim Bezrukov
 */
@RunWith(Parameterized.class)
public class XMLFeaturesReportTest {

	private static DocumentBuilder builder;

	@BeforeClass
	public static void before() throws ParserConfigurationException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		builder = factory.newDocumentBuilder();
	}

	@Parameterized.Parameters
	public static List<String> data() {
		List<String> parameters = new ArrayList<>();
		for (char c = 'a'; c <= 't'; ++c) {
			parameters.add("/veraPDF test suite 6-7-9-t04-pass-" + c + ".pdf");
		}
		return parameters;
	}

	@Parameterized.Parameter
	public String filePath;

	@Test
	public void test() throws URISyntaxException, IOException, ParserConfigurationException {
		File pdf = new File(getSystemIndependentPath(filePath));
		PDDocument document = PDDocument.load(pdf, false, true);
		FeaturesCollection collection = PBFeatureParser.getFeaturesCollection(document);
		Document doc = builder.newDocument();

		Element features = XMLFeaturesReport.makeXMLTree(collection, doc);

		assertEquals("metadata", features.getFirstChild().getNextSibling().getNodeName());
		assertEquals("x:xmpmeta", features.getFirstChild().getNextSibling().getFirstChild().getNodeName());

		document.close();
	}

	private static String getSystemIndependentPath(String path) throws URISyntaxException {
		URL resourceUrl = ClassLoader.class.getResource(path);
		Path resourcePath = Paths.get(resourceUrl.toURI());
		return resourcePath.toString();
	}
}
