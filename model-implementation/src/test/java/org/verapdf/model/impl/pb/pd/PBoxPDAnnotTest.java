package org.verapdf.model.impl.pb.pd;

import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotation;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.impl.BaseTest;
import org.verapdf.model.impl.pb.cos.PBCosReal;
import org.verapdf.model.impl.pb.pd.actions.PBoxPDAction;
import org.verapdf.model.impl.pb.pd.actions.PBoxPDGoToAction;
import org.verapdf.model.impl.pb.pd.actions.PBoxPDGoToRemoteAction;
import org.verapdf.model.impl.pb.pd.actions.PBoxPDNamedAction;
import org.verapdf.model.pdlayer.PDAnnot;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBoxPDAnnotTest extends BaseTest {

	public static final String FILE_RELATIVE_PATH = "pd/InteractiveObjects.pdf";

	@BeforeClass
	public static void setUp() throws URISyntaxException, IOException {
		expectedType = TYPES.contains(PBoxPDAnnot.ANNOTATION_TYPE) ? PBoxPDAnnot.ANNOTATION_TYPE : null;
		expectedID = null;

		setUp(FILE_RELATIVE_PATH);
		List<PDAnnotation> annotations = document.getPage(0).getAnnotations();
		actual = new PBoxPDAnnot(annotations.get(annotations.size() - 1));
	}

	@Test
	public void testSubtypeMethod() {
		Assert.assertEquals("Widget", ((PDAnnot) actual).getSubtype());
	}

	@Test
	public void testAPMethod() {
		Assert.assertNotNull(((PDAnnot) actual).getAP());
	}

	@Test
	public void testFMethod() {
		Assert.assertEquals(Long.valueOf(4), ((PDAnnot) actual).getF());
	}

	@Test
	public void testCAMethod() {
		Assert.assertNull(((PDAnnot) actual).getCA());
	}

	@Test
	public void testNTypeMethod() {
		Assert.assertEquals("Stream", ((PDAnnot) actual).getN_type());
	}

	@Test
	public void testFTMethod() {
		Assert.assertEquals("Btn", ((PDAnnot) actual).getFT());
	}

	@Test
	public void testAdditionalActionLink() {
		List<? extends Object> action = actual.getLinkedObjects(PBoxPDAnnot.ADDITIONAL_ACTION);
		Assert.assertEquals(0, action.size());
		for (Object object : action) {
			Assert.assertTrue(PBoxPDAction.ACTION_TYPE.equals(object.getObjectType()) ||
					PBoxPDNamedAction.NAMED_ACTION_TYPE.equals(object.getObjectType()));
		}
	}

	@Test
	public void testActionLink() {
		List<? extends Object> action = actual.getLinkedObjects(PBoxPDAnnot.A);
		Assert.assertEquals(1, action.size());
		for (Object object : action) {
			Assert.assertTrue(PBoxPDAction.ACTION_TYPE.equals(object.getObjectType()) ||
					PBoxPDNamedAction.NAMED_ACTION_TYPE.equals(object.getObjectType()) ||
					PBoxPDGoToAction.GOTO_ACTION_TYPE.equals(object.getObjectType()) ||
					PBoxPDGoToRemoteAction.GOTO_REMOTE_ACTION_TYPE.equals(object.getObjectType()));
		}
	}

	@Test
	public void testICLink() {
		List<? extends Object> action = actual.getLinkedObjects(PBoxPDAnnot.IC);
		Assert.assertEquals(0, action.size());
		for (Object object : action) {
			Assert.assertEquals(PBCosReal.COS_REAL_TYPE, object.getObjectType());
		}
	}

	@Test
	public void testCLink() {
		List<? extends Object> action = actual.getLinkedObjects(PBoxPDAnnot.C);
		Assert.assertEquals(0, action.size());
		for (Object object : action) {
			Assert.assertEquals(PBCosReal.COS_REAL_TYPE, object.getObjectType());
		}
	}

	@Test
	public void testAppearanceLink() {
		List<? extends Object> action = actual.getLinkedObjects(PBoxPDAnnot.APPEARANCE);
		Assert.assertEquals(1, action.size());
		for (Object object : action) {
			Assert.assertEquals(PBoxPDContentStream.CONTENT_STREAM_TYPE, object.getObjectType());
		}
	}

}
