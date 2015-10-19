package org.verapdf.model.impl.pb.pd;

import org.apache.pdfbox.pdmodel.interactive.action.PDActionNamed;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.impl.pb.pd.actions.PBoxPDAction;
import org.verapdf.model.impl.pb.pd.actions.PBoxPDGoToRemoteAction;
import org.verapdf.model.impl.pb.pd.actions.PBoxPDNamedAction;
import org.verapdf.model.pdlayer.PDAction;
import org.verapdf.model.pdlayer.PDNamedAction;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBoxPDNamedActionTest extends PBoxPDActionTest {

	@BeforeClass
	public static void setUp() throws URISyntaxException, IOException {
		actual = new PBoxPDNamedAction((PDActionNamed) setUp(PBoxPDNamedAction.NAMED_ACTION_TYPE, 67));
	}

	@Override
	@Test
	public void testSMethod() {
		Assert.assertEquals("Named", ((PDAction) actual).getS());
	}

	@Test
	public void testNMethod() {
		Assert.assertEquals("FirstPage", ((PDNamedAction) actual).getN());
	}

	@Override
	@Test
	public void testNextLink() {
		List<? extends Object> nextAction = actual.getLinkedObjects(PBoxPDAction.NEXT);
		Assert.assertEquals(1, nextAction.size());
		Assert.assertEquals(PBoxPDGoToRemoteAction.GOTO_REMOTE_ACTION_TYPE, nextAction.get(0).getObjectType());
	}

}
