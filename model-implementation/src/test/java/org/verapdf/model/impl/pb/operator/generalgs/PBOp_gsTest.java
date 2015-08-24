package org.verapdf.model.impl.pb.operator.generalgs;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.impl.pb.pd.PBoxPDExtGState;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBOp_gsTest extends PBOpGeneralGSTest {

	@BeforeClass
	public static void setUp() throws IOException, URISyntaxException {
		setUpOperatorsList(PBOp_gs.OP_GS_TYPE, null);
	}

	@Test
	public void testExtGStateLink() {
		List<? extends Object> extGState = actual.getLinkedObjects(PBOp_gs.EXT_G_STATE);

		Assert.assertEquals(1, extGState.size());
		Assert.assertEquals(PBoxPDExtGState.EXT_G_STATE_TYPE, extGState.get(0).getObjectType());
	}
}
