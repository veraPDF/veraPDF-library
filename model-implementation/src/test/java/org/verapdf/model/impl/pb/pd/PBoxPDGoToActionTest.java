package org.verapdf.model.impl.pb.pd;

import org.apache.pdfbox.pdmodel.interactive.action.PDActionGoTo;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.impl.pb.pd.actions.PBoxPDAction;
import org.verapdf.model.impl.pb.pd.actions.PBoxPDGoToAction;
import org.verapdf.model.impl.pb.pd.actions.PBoxPDNamedAction;
import org.verapdf.model.pdlayer.PDAction;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBoxPDGoToActionTest extends PBoxPDActionTest {

	@BeforeClass
	public static void setUp() throws URISyntaxException, IOException {
		actual = new PBoxPDGoToAction((PDActionGoTo) setUp(PBoxPDGoToAction.GOTO_ACTION_TYPE, 66));
	}

	@Override
	@Test
	public void testSMethod() {
		Assert.assertEquals("GoTo", ((PDAction) actual).getS());
	}

	@Override
	@Test
	public void testNextLink() {
		List<? extends Object> nextAction = actual.getLinkedObjects(PBoxPDAction.NEXT);
		Assert.assertEquals(1, nextAction.size());
		Assert.assertEquals(PBoxPDNamedAction.NAMED_ACTION_TYPE, nextAction.get(0).getObjectType());
	}

	@Test
	public void testDLink() {
		List<? extends Object> link = actual.getLinkedObjects(PBoxPDGoToAction.D);
		Assert.assertEquals(0, link.size());
	}

}
