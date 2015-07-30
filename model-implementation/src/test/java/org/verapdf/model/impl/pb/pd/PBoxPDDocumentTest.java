package org.verapdf.model.impl.pb.pd;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.impl.BaseTest;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBoxPDDocumentTest extends BaseTest{

	public static final String FILE_RELATIVE_PATH = "cos/veraPDF test suite 6-1-2-t02-fail-a.pdf";

	@BeforeClass
	public static void setUp() throws URISyntaxException, IOException {
		expectedType = TYPES.contains(PBoxPDDocument.PD_DOCUMENT_TYPE) ? PBoxPDDocument.PD_DOCUMENT_TYPE : null;
		expectedID = null;

		setUp(FILE_RELATIVE_PATH);
		actual = new PBoxPDDocument(document);
	}

	@Test
	public void testOutlinesLink() {
		List<? extends Object> outlines = actual.getLinkedObjects(PBoxPDDocument.OUTLINES);
		Assert.assertEquals(6, outlines.size());
		for (Object object : outlines) {
			Assert.assertEquals(PBoxPDOutline.OUTLINE_TYPE, object.getType());
		}
	}

	@Test
	public void testOpenActionLink() {
		isCorrectActions(PBoxPDDocument.OPEN_ACTION);
	}

	@Test
	public void testActionsLink() {
		isCorrectActions(PBoxPDDocument.ACTIONS);
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

	private void isCorrectActions(String link) {
		List<? extends Object> actions = actual.getLinkedObjects(link);
		Assert.assertEquals(0, actions.size());
		for (Object object : actions) {
			Assert.assertTrue("PDAction".equals(object.getType()) || "PDNamedAction".equals(object.getType()));
		}
	}
}
