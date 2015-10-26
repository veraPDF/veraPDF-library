package org.verapdf.model.impl.pb.pd;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.impl.BaseTest;
import org.verapdf.model.impl.pb.external.PBoxICCOutputProfile;
import org.verapdf.model.pdlayer.PDOutputIntent;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBoxPDOutputIntentTest extends BaseTest {

	public static final String FILE_RELATIVE_PATH = "cos/veraPDF test suite 6-1-2-t02-fail-a.pdf";

	@BeforeClass
	public static void setUp() throws URISyntaxException, IOException {
		expectedType = TYPES.contains(PBoxPDOutputIntent.OUTPUT_INTENT_TYPE) ?
															PBoxPDOutputIntent.OUTPUT_INTENT_TYPE : null;
		expectedID = null;

		setUp(FILE_RELATIVE_PATH);
		actual = new PBoxPDOutputIntent(document.getDocumentCatalog().getOutputIntents().get(0));
	}

	@Test
	public void testDestOutputProfileRefMethod() {
		Assert.assertEquals("7 0", ((PDOutputIntent) actual).getdestOutputProfileIndirect());
	}

	@Test
	public void testDestProfileLink() {
		List<? extends Object> destProfile = actual.getLinkedObjects(PBoxPDOutputIntent.DEST_PROFILE);
		Assert.assertEquals(1, destProfile.size());
		for (Object object : destProfile) {
			Assert.assertEquals(PBoxICCOutputProfile.ICC_OUTPUT_PROFILE_TYPE, object.getObjectType());
		}
	}

}
