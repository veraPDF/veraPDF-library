package org.verapdf.model.factory.font;

import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.verapdf.model.pdlayer.PDTrueTypeFont;
import org.verapdf.model.pdlayer.PDType0Font;
import org.verapdf.model.pdlayer.PDType1Font;
import org.verapdf.model.pdlayer.PDType3Font;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author Timur Kamalov
 */
public class FontFactoryTest {

	public static final String FILE_RELATIVE_PATH = "/model/impl/pb/pd/Fonts.pdf";

	private static PDResources resources;
	private static PDDocument document;

	@BeforeClass
	public static void setUp() throws URISyntaxException, IOException {
		String fileAbsolutePath = getSystemIndependentPath(FILE_RELATIVE_PATH);
		File file = new File(fileAbsolutePath);
		document = PDDocument.load(file, false, true);
		resources = document.getPage(0).getResources();
	}

	protected static String getSystemIndependentPath(String path) throws URISyntaxException {
		URL resourceUrl = ClassLoader.class.getResource(path);
		Path resourcePath = Paths.get(resourceUrl.toURI());
		return resourcePath.toString();
	}

	@Test
	public void testType0CID0Generating() throws IOException {
		PDFont font = resources.getFont(COSName.getPDFName("C0_0"));
		org.verapdf.model.pdlayer.PDFont convertedFont = FontFactory.parseFont(font);
		Assert.assertTrue(convertedFont instanceof PDType0Font);
	}

	@Test
	public void testType0CID2Generating() throws IOException {
		PDFont font = resources.getFont(COSName.getPDFName("C2_0"));
		Assert.assertTrue(FontFactory.parseFont(font) instanceof PDType0Font);
	}

	@Test
	public void testType1Generating() throws IOException {
		PDFont font = resources.getFont(COSName.getPDFName("T1_0"));
		Assert.assertTrue(FontFactory.parseFont(font) instanceof PDType1Font);
	}

	@Test
	public void testType3Generating() throws IOException {
		PDFont font = resources.getFont(COSName.getPDFName("T3_0"));
		Assert.assertTrue(FontFactory.parseFont(font) instanceof PDType3Font);
	}

	@Test
	public void testTrueTypeGenerating() throws IOException {
		PDFont font = resources.getFont(COSName.getPDFName("TT0"));
		Assert.assertTrue(FontFactory.parseFont(font) instanceof PDTrueTypeFont);
	}

	@AfterClass
	public static void tearDown() throws IOException {
		resources = null;
		document.close();
		document = null;
	}

}
