package org.verapdf.model.impl.pb.pd.images;

import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.graphics.form.PDFormXObject;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.impl.pb.pd.PBoxPDContentStream;
import org.verapdf.model.pdlayer.PDXForm;
import org.verapdf.model.tools.resources.PDInheritableResources;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBoxPDXFormTest extends PBoxPDAbstractXObjectTest {

	private static final String FORM_NAME = "X0";
	private static final String FORM_SUBTYPE = "Form";

	@BeforeClass
	public static void setUp() throws IOException, URISyntaxException {
		expectedType = TYPES.contains(PBoxPDXForm.X_FORM_TYPE) ? PBoxPDXForm.X_FORM_TYPE : null;
		expectedID = null;

		setUp(FILE_RELATIVE_PATH);
		PDResources pageResources = document.getPage(0).getResources();
		PDFormXObject xObject = (PDFormXObject) pageResources.getXObject(COSName.getPDFName(FORM_NAME));
		actual = new PBoxPDXForm(xObject, PDInheritableResources.getInstance(pageResources, xObject.getResources()));
	}

	@Override
	@Test
	public void testSubtypeMethod() {
		Assert.assertEquals(FORM_SUBTYPE, ((PDXForm) actual).getSubtype());
	}

	@Test
	public void testSubtype2Method() {
		Assert.assertNull(((PDXForm) actual).getSubtype2());
	}

	@Test
	public void testGroupLink() {
		List<? extends Object> group = actual.getLinkedObjects(PBoxPDXForm.GROUP);
		Assert.assertTrue(group.isEmpty());
	}

	@Test
	public void testPostScriptStreamLink() {
		List<? extends Object> psStream = actual.getLinkedObjects(PBoxPDXForm.PS);
		Assert.assertTrue(psStream.isEmpty());
	}

	@Test
	public void testReferenceLink() {
		List<? extends Object> reference = actual.getLinkedObjects(PBoxPDXForm.REF);
		Assert.assertTrue(reference.isEmpty());
	}

	@Test
	public void testContentStreamLink() {
		List<? extends Object> contentStream = actual.getLinkedObjects(PBoxPDXForm.CONTENT_STREAM);
		Assert.assertEquals(1, contentStream.size());
		for (Object object : contentStream) {
			Assert.assertEquals(PBoxPDContentStream.CONTENT_STREAM_TYPE, object.getObjectType());
		}
	}

}
