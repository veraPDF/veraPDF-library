package org.verapdf.model.impl.pb.pd;

import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSObjectKey;
import org.apache.pdfbox.pdmodel.interactive.action.PDActionFactory;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.impl.BaseTest;
import org.verapdf.model.impl.pb.pd.actions.PBoxPDAction;
import org.verapdf.model.pdlayer.PDAction;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBoxPDActionTest extends BaseTest {

	public static final String FILE_RELATIVE_PATH = "pd/InteractiveObjects.pdf";

	@BeforeClass
	public static void setUp() throws URISyntaxException, IOException {
		actual = new PBoxPDAction(setUp(PBoxPDAction.ACTION_TYPE, 69));
	}

	protected static org.apache.pdfbox.pdmodel.interactive.action.PDAction setUp(String actionType, int objectNumber)
			throws URISyntaxException, IOException {
		expectedType = TYPES.contains(actionType) ? actionType : null;
		expectedID = null;

		setUp(FILE_RELATIVE_PATH);
		COSDictionary object = (COSDictionary) document.getDocument()
				.getObjectFromPool(new COSObjectKey(objectNumber, 0)).getObject();
		return PDActionFactory.createAction(object);
	}

	@Test
	public void testSMethod() {
		Assert.assertEquals("Launch", ((PDAction) actual).getS());
	}

	@Test
	public void testNextLink() {
		List<? extends Object> nextAction = actual.getLinkedObjects(PBoxPDAction.NEXT);
		Assert.assertEquals(0, nextAction.size());
	}

}
