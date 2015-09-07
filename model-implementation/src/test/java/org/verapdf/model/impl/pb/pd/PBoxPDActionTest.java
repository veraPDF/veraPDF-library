package org.verapdf.model.impl.pb.pd;

import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotation;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.impl.BaseTest;
import org.verapdf.model.pdlayer.PDAction;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

/**
 * @author Evgeniy Muravitskiy
 */
@Ignore
public class PBoxPDActionTest extends BaseTest {

	public static final String FILE_RELATIVE_PATH = "pd/InteractiveObjects.pdf";

	@BeforeClass
	public static void setUp() throws URISyntaxException, IOException {
		expectedType = TYPES.contains(PBoxPDAction.ACTION_TYPE) ? PBoxPDAction.ACTION_TYPE : null;
		expectedID = null;

		setUp(FILE_RELATIVE_PATH);
		List<PDAnnotation> annotations = document.getPage(0).getAnnotations();
		PBoxPDAnnot annotation = new PBoxPDAnnot(annotations.get(annotations.size() - 1));
		actual = annotation.getLinkedObjects(PBoxPDAnnot.A).get(0);
	}

	@Test
	public void testSMethod() {
		Assert.assertEquals("GoTo", ((PDAction) actual).getS());
	}

	@Test
	public void testNextLink() {
		List<? extends Object> nextAction = actual.getLinkedObjects(PBoxPDAction.NEXT);
		Assert.assertEquals(0, nextAction.size());
		for (Object object : nextAction) {
			Assert.assertTrue(PBoxPDAction.ACTION_TYPE.equals(object.getObjectType()) ||
					PBoxPDNamedAction.NAMED_ACTION_TYPE.equals(object.getObjectType()));
		}
	}

}
