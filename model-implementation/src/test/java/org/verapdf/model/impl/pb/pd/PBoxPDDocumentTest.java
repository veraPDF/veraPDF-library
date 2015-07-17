package org.verapdf.model.impl.pb.pd;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.impl.BaseTest;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBoxPDDocumentTest extends BaseTest{

	public static final String FILE_RELATIVE_PATH = "/model/impl/pb/cos/veraPDF test suite 6-1-2-t02-fail-a.pdf";

	private static PDDocument document;

	@BeforeClass
	public static void setUp() throws URISyntaxException, IOException {
		expectedType = "PDDocument";
		expectedID = null;

		String fileAbsolutePath = getSystemIndependentPath(FILE_RELATIVE_PATH);
		File file = new File(fileAbsolutePath);
		document = PDDocument.load(file, Boolean.FALSE, Boolean.TRUE);
		actual = new PBoxPDDocument(document);
	}

	@Test
	public void testOutlinesLink() {
		Assert.assertEquals(0, actual.getLinkedObjects(PBoxPDDocument.OUTLINES).size());
	}

	@Test
	public void testOpenActionLink() {
		Assert.assertEquals(0, actual.getLinkedObjects(PBoxPDDocument.OPEN_ACTION).size());
	}

	@Test
	public void testActionsLink() {
		Assert.assertEquals(0, actual.getLinkedObjects(PBoxPDDocument.ACTIONS).size());
	}

	@Test
	public void testPagesLink() {
		List<? extends org.verapdf.model.baselayer.Object> pages = actual.getLinkedObjects(PBoxPDDocument.PAGES);
		Assert.assertEquals(1, pages.size());
		for (Object object : pages) {
			Assert.assertEquals("PDPage", object.getType());
		}
	}

	@Test
	public void testMetadataLink() {
		List<? extends Object> metadata = actual.getLinkedObjects(PBoxPDDocument.METADATA);
		Assert.assertEquals(1, metadata.size());
		for (Object object : metadata) {
			Assert.assertEquals("PDMetadata", object.getType());
		}
	}

	@Test
	public void testOutputIntentsLink() {
		List<? extends Object> outputIntents = actual.getLinkedObjects(PBoxPDDocument.OUTPUT_INTENTS);
		Assert.assertEquals(1, outputIntents.size());
		for (Object object : outputIntents) {
			Assert.assertEquals("PDOutputIntent", object.getType());
		}
	}

	@Test
	public void testAcroFormsLink() {
		Assert.assertEquals(0, actual.getLinkedObjects(PBoxPDDocument.ACRO_FORMS).size());
	}

	@AfterClass
	public static void tearDown() throws IOException {
		expectedType = null;
		expectedID = null;
		actual = null;

		document.close();
	}
}
