package org.verapdf.model.impl.pb.pd;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.impl.BaseTest;
import org.verapdf.model.pdlayer.PDAcroForm;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBoxPDAcroFormTest extends BaseTest {

	public static final String FILE_RELATIVE_PATH = "pd/InteractiveObjects.pdf";

	@BeforeClass
	public static void setUp() throws URISyntaxException, IOException {
		expectedType = TYPES.contains(PBoxPDAcroForm.ACRO_FORM_TYPE) ? PBoxPDAcroForm.ACRO_FORM_TYPE : null;
		expectedID = null;

		setUp(FILE_RELATIVE_PATH);
		actual = new PBoxPDAcroForm(document.getDocumentCatalog().getAcroForm());
	}

	@Test
	public void testNeedAppearancesMethod() {
		Assert.assertFalse(((PDAcroForm) actual).getNeedAppearances().booleanValue());
	}

	@Test
	public void testFormFieldLink() {
		List<? extends Object> formFields = actual.getLinkedObjects(PBoxPDAcroForm.FORM_FIELDS);
		Assert.assertEquals(1, formFields.size());
		for (Object object : formFields) {
			Assert.assertEquals(PBoxPDFormField.FORM_FIELD_TYPE, object.getObjectType());
		}
	}

}
