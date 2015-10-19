package org.verapdf.model.impl.pb.pd;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.impl.BaseTest;
import org.verapdf.model.impl.pb.pd.actions.PBoxPDAction;
import org.verapdf.model.impl.pb.pd.actions.PBoxPDNamedAction;
import org.verapdf.model.pdlayer.PDFormField;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBoxPDFormFieldTest extends BaseTest {

	public static final String FILE_RELATIVE_PATH = "pd/InteractiveObjects.pdf";
	public static final String FORM_TYPE = "Btn";

	@BeforeClass
	public static void setUp() throws URISyntaxException, IOException {
		expectedType = TYPES.contains(PBoxPDFormField.FORM_FIELD_TYPE) ? PBoxPDFormField.FORM_FIELD_TYPE : null;
		expectedID = null;

		setUp(FILE_RELATIVE_PATH);
		actual = new PBoxPDFormField(document.getDocumentCatalog().getAcroForm().getFields().get(0));
	}

	@Test
	public void testFTMethod() {
		Assert.assertEquals(FORM_TYPE, ((PDFormField) actual).getFT());
	}

	@Test
	public void testAdditionalActionLink() {
		List<? extends Object> additionalActions = actual.getLinkedObjects(PBoxPDFormField.ADDITIONAL_ACTION);
		Assert.assertEquals(0, additionalActions.size());
		for (Object object : additionalActions) {
			Assert.assertTrue(PBoxPDAction.ACTION_TYPE.equals(object.getObjectType()) ||
					PBoxPDNamedAction.NAMED_ACTION_TYPE.equals(object.getObjectType()));
		}
	}

}
