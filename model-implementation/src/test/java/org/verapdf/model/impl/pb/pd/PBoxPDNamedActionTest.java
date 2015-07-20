package org.verapdf.model.impl.pb.pd;

import org.apache.pdfbox.pdmodel.interactive.action.PDActionNamed;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDOutlineItem;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.verapdf.model.pdlayer.PDAction;
import org.verapdf.model.pdlayer.PDNamedAction;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBoxPDNamedActionTest extends PBoxPDActionTest {

	public static final String FILE_RELATIVE_PATH = "pd/InteractiveObjects.pdf";

	@BeforeClass
	public static void setUp() throws URISyntaxException, IOException {
		expectedType = PBoxPDNamedAction.NAMED_ACTION_TYPE;
		expectedID = null;

		setUp(FILE_RELATIVE_PATH);
		PDOutlineItem node = document.getDocumentCatalog().getDocumentOutline().getFirstChild().getFirstChild();
		actual = new PBoxPDNamedAction((PDActionNamed) node.getAction());
	}

	// TODO : try to simplify
	@Override
	@Test
	public void testSMethod() {
		Assert.assertEquals("Named", ((PDAction) actual).getS());
	}

	@Test
	public void testNMethod() {
		Assert.assertEquals("NextPage", ((PDNamedAction) actual).getN());
	}

	@AfterClass
	public static void tearDown() throws IOException {
		expectedType = null;
		expectedID = null;
		actual = null;

		document.close();
	}
}
