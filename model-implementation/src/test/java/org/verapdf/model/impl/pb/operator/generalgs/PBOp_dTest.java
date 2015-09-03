package org.verapdf.model.impl.pb.operator.generalgs;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.coslayer.CosArray;
import org.verapdf.model.impl.pb.cos.PBCosArray;
import org.verapdf.model.impl.pb.cos.PBCosReal;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBOp_dTest extends PBOpGeneralGSTest {

	@BeforeClass
	public static void setUp() throws IOException, URISyntaxException {
		setUpOperatorsList(PBOp_d.OP_D_TYPE, null);
	}

	@Test
	public void testDashArrayLink() {
		Object object = testObject(PBOp_d.DASH_ARRAY, 1, PBCosArray.COS_ARRAY_TYPE);
		Assert.assertEquals(Long.valueOf(2), ((CosArray) object).getsize());
	}

	@Test
	public void testDashPhaseLink() {
		testLinkToReal(PBOp_d.DASH_PHASE, PBCosReal.COS_REAL_TYPE, Long.valueOf(0));
	}
}
