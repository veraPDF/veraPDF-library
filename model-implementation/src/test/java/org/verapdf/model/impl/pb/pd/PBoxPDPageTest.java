package org.verapdf.model.impl.pb.pd;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.impl.BaseTest;
import org.verapdf.model.impl.pb.pd.actions.PBoxPDAction;
import org.verapdf.model.pdlayer.PDPage;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBoxPDPageTest extends BaseTest {

	public static final String FILE_RELATIVE_PATH = "cos/veraPDF test suite 6-1-2-t02-fail-a.pdf";

	@BeforeClass
	public static void setUp() throws URISyntaxException, IOException {
		expectedType = TYPES.contains(PBoxPDPage.PAGE_TYPE) ? PBoxPDPage.PAGE_TYPE : null;
		expectedID = null;

		setUp(FILE_RELATIVE_PATH);
		actual = new PBoxPDPage(document.getPage(0));
	}

	@Test
	public void testGroupLink() {
		List<? extends Object> groups = ((PDPage) actual).getLinkedObjects(PBoxPDPage.GROUP);
		Assert.assertEquals(0, groups.size());
		for (Object object : groups) {
			Assert.assertEquals(PBoxPDGroup.GROUP_TYPE, object.getObjectType());
		}
	}

	@Test
	public void testAnnotsLink() {
		List<? extends Object> annots = ((PDPage) actual).getLinkedObjects(PBoxPDPage.ANNOTS);
		Assert.assertEquals(0, annots.size());
		for (Object object : annots) {
			Assert.assertEquals(PBoxPDAnnot.ANNOTATION_TYPE, object.getObjectType());
		}
	}

	@Test
	public void testActionLink() {
		List<? extends Object> actions = ((PDPage) actual).getLinkedObjects(PBoxPDPage.ACTION);
		Assert.assertEquals(0, actions.size());
		for (Object object : actions) {
			Assert.assertEquals(PBoxPDAction.ACTION_TYPE, object.getObjectType());
		}
	}

	@Test
	public void testContentStreamLink() {
		List<? extends Object> streams = ((PDPage) actual).getLinkedObjects(PBoxPDPage.CONTENT_STREAM);
		Assert.assertEquals(1, streams.size());
		for (Object object : streams) {
			Assert.assertEquals(PBoxPDContentStream.CONTENT_STREAM_TYPE, object.getObjectType());
		}
	}

}
