package org.verapdf.model.factory.colors;

import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.graphics.color.PDColorSpace;
import org.apache.pdfbox.pdmodel.graphics.color.PDJPXColorSpace;
import org.apache.pdfbox.pdmodel.graphics.pattern.PDAbstractPattern;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.verapdf.model.pdlayer.*;
import org.verapdf.model.tools.resources.PDInheritableResources;

import java.awt.color.ColorSpace;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.awt.color.ColorSpace.CS_GRAY;

/**
 * @author Evgeniy Muravitskiy
 */
public class ColorSpaceFactoryTest {

	public static final String FILE_RELATIVE_PATH = "/model/impl/pb/pd/ColorSpaces.pdf";

	private static PDResources resources;
	private static PDDocument document;

	@BeforeClass
	public static void setUp() throws URISyntaxException, IOException {
		String fileAbsolutePath = getSystemIndependentPath(FILE_RELATIVE_PATH);
		File file = new File(fileAbsolutePath);
		document = PDDocument.load(file, false, true);
		resources = document.getPage(0).getResources();
	}

	@Test
	public void testCalGrayGenerating() throws IOException {
		PDColorSpace colorSpace = resources.getColorSpace(COSName.getPDFName("CalGrayCS"));
		Assert.assertTrue(ColorSpaceFactory.getColorSpace(colorSpace) instanceof PDCalGray);
	}

	@Test
	public void testCalRGBGenerating() throws IOException {
		PDColorSpace colorSpace = resources.getColorSpace(COSName.getPDFName("CalRGBCS"));
		Assert.assertTrue(ColorSpaceFactory.getColorSpace(colorSpace) instanceof PDCalRGB);
	}

	@Test
	public void testDeviceCMYKGenerating() {
		PDColorSpace colorSpace = org.apache.pdfbox.pdmodel.graphics.color.PDDeviceCMYK.INSTANCE;
		Assert.assertTrue(ColorSpaceFactory.getColorSpace(colorSpace) instanceof PDDeviceCMYK);
	}

	@Test
	public void testDeviceGrayGenerating() {
		PDColorSpace colorSpace = org.apache.pdfbox.pdmodel.graphics.color.PDDeviceGray.INSTANCE;
		Assert.assertTrue(ColorSpaceFactory.getColorSpace(colorSpace) instanceof PDDeviceGray);
	}

	@Test
	public void testDeviceRGBGenerating() {
		PDColorSpace colorSpace = org.apache.pdfbox.pdmodel.graphics.color.PDDeviceRGB.INSTANCE;
		Assert.assertTrue(ColorSpaceFactory.getColorSpace(colorSpace) instanceof PDDeviceRGB);
	}

	@Test
	public void testICCBasedGenerating() throws IOException {
		PDColorSpace colorSpace = resources.getColorSpace(COSName.getPDFName("ICCBasedCS"));
		Assert.assertTrue(ColorSpaceFactory.getColorSpace(colorSpace) instanceof PDICCBased);
	}

	@Test
	public void testLabGenerating() throws IOException {
		PDColorSpace colorSpace = resources.getColorSpace(COSName.getPDFName("LabCS"));
		Assert.assertTrue(ColorSpaceFactory.getColorSpace(colorSpace) instanceof PDLab);
	}

	@Test
	public void testSeparationGenerating() throws IOException {
		PDColorSpace colorSpace = resources.getColorSpace(COSName.getPDFName("SeparationCS"));
		Assert.assertTrue(ColorSpaceFactory.getColorSpace(colorSpace) instanceof PDSeparation);
	}

	@Test
	public void testIndexedGenerating() throws IOException {
		PDColorSpace colorSpace = resources.getColorSpace(COSName.getPDFName("IndexedCS"));
		Assert.assertTrue(ColorSpaceFactory.getColorSpace(colorSpace) instanceof PDIndexed);
	}

	@Test
	public void testDeviceNGenerating() throws IOException {
		PDColorSpace colorSpace = resources.getColorSpace(COSName.getPDFName("DeviceNCS"));
		Assert.assertTrue(ColorSpaceFactory.getColorSpace(colorSpace) instanceof PDDeviceN);
	}

	@Test
	public void testTillingPatternGenerating() throws IOException {
		PDColorSpace colorSpace = resources.getColorSpace(COSName.getPDFName("PatternCS"));
		PDAbstractPattern pattern = resources.getPattern(COSName.getPDFName("P0"));
		PDInheritableResources extRes = PDInheritableResources.getInstance(resources);
		Assert.assertTrue(ColorSpaceFactory.getColorSpace(colorSpace, pattern, extRes) instanceof PDTilingPattern);
	}

	@Test
	public void testNullGenerating() {
		Assert.assertNull(ColorSpaceFactory.getColorSpace(null));
	}

	@Test
	public void testUnsupportedColorSpaceGenerating() {
		PDColorSpace colorSpace = new PDJPXColorSpace(ColorSpace.getInstance(CS_GRAY));
		Assert.assertNull(ColorSpaceFactory.getColorSpace(colorSpace));
	}

	protected static String getSystemIndependentPath(String path) throws URISyntaxException {
		URL resourceUrl = ClassLoader.class.getResource(path);
		Path resourcePath = Paths.get(resourceUrl.toURI());
		return resourcePath.toString();
	}

	@AfterClass
	public static void tearDown() throws IOException {
		resources = null;
		document.close();
		document = null;
	}
}
