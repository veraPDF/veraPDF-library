package org.verapdf.model.impl.pb.pd;

import org.apache.pdfbox.pdmodel.interactive.action.PDActionRemoteGoTo;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.impl.pb.pd.actions.PBoxPDAction;
import org.verapdf.model.impl.pb.pd.actions.PBoxPDGoToRemoteAction;
import org.verapdf.model.pdlayer.PDAction;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBoxPDGoToRemoteActionTest extends PBoxPDGoToActionTest {

	@BeforeClass
	public static void setUp() throws URISyntaxException, IOException {
		actual = new PBoxPDGoToRemoteAction((PDActionRemoteGoTo) setUp(PBoxPDGoToRemoteAction.GOTO_REMOTE_ACTION_TYPE, 68));
	}

	@Override
	@Test
	public void testSMethod() {
		Assert.assertEquals("GoToR", ((PDAction) actual).getS());
	}

	@Override
	@Test
	public void testNextLink() {
		List<? extends Object> nextAction = actual.getLinkedObjects(PBoxPDAction.NEXT);
		Assert.assertEquals(1, nextAction.size());
		Assert.assertEquals(PBoxPDAction.ACTION_TYPE, nextAction.get(0).getObjectType());
	}
}
