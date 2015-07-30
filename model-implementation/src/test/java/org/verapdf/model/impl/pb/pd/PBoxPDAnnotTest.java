package org.verapdf.model.impl.pb.pd;

import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotation;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.impl.BaseTest;
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
		expectedType = PBoxPDAnnot.ANNOTATION_TYPE;
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
	public void testAdditionalActionLink() {
		List<? extends Object> action = actual.getLinkedObjects(PBoxPDAnnot.ADDITIONAL_ACTION);
		Assert.assertEquals(0, action.size());
		for (Object object : action) {
			Assert.assertTrue("PDAction".equals(object.getType()) || "PDNamedAction".equals(object.getType()));
		}
	}

	@Test
	public void testActionLink() {
		List<? extends Object> action = actual.getLinkedObjects(PBoxPDAnnot.A);
		Assert.assertEquals(1, action.size());
		for (Object object : action) {
			Assert.assertTrue("PDAction".equals(object.getType()) || "PDNamedAction".equals(object.getType()));
		}
	}

	@Test
	public void testICLink() {
		List<? extends Object> action = actual.getLinkedObjects(PBoxPDAnnot.IC);
		Assert.assertEquals(0, action.size());
		for (Object object : action) {
			Assert.assertEquals("CosReal", object.getType());
		}
	}

	@Test
	public void testCLink() {
		List<? extends Object> action = actual.getLinkedObjects(PBoxPDAnnot.C);
		Assert.assertEquals(0, action.size());
		for (Object object : action) {
			Assert.assertEquals("CosReal", object.getType());
		}
	}

	@Test
	public void testAppearanceLink() {
		List<? extends Object> action = actual.getLinkedObjects(PBoxPDAnnot.APPEARANCE);
		Assert.assertEquals(1, action.size());
		for (Object object : action) {
			Assert.assertEquals("PDContentStream", object.getType());
		}
	}

}
