package org.verapdf.model.impl.pb.pd;

import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDOutlineItem;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.impl.BaseTest;
import org.verapdf.model.impl.pb.pd.actions.PBoxPDAction;
import org.verapdf.model.impl.pb.pd.actions.PBoxPDNamedAction;
import org.verapdf.model.tools.IDGenerator;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBoxPDOutlineTest extends BaseTest {

	public static final String FILE_RELATIVE_PATH = "cos/veraPDF test suite 6-1-2-t02-fail-a.pdf";

	@BeforeClass
	public static void setUp() throws URISyntaxException, IOException {
		expectedType = TYPES.contains(PBoxPDOutline.OUTLINE_TYPE) ? PBoxPDOutline.OUTLINE_TYPE : null;
		expectedID = "outline 6 0";

		setUp(FILE_RELATIVE_PATH);
		PDOutlineItem firstChild = document.getDocumentCatalog().getDocumentOutline().getFirstChild();
		actual = new PBoxPDOutline(firstChild, IDGenerator.generateID(firstChild));
	}

	@Test
	public void testActionLink() {
		List<? extends org.verapdf.model.baselayer.Object> action = actual.getLinkedObjects(PBoxPDOutline.ACTION);
		Assert.assertEquals(0, action.size());
		for (Object object : action) {
			Assert.assertTrue(PBoxPDAction.ACTION_TYPE.equals(object.getObjectType()) ||
					PBoxPDNamedAction.NAMED_ACTION_TYPE.equals(object.getObjectType()));
		}
	}

}
