package org.verapdf.model.impl.pb.operator.generalgs;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.coslayer.CosRenderingIntent;
import org.verapdf.model.impl.pb.cos.PBCosRenderingIntent;
import org.verapdf.model.impl.pb.operator.base.PBOperatorTest;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBOp_riTest extends PBOperatorTest {

	private static final String RELATIVE_COLORIMETRIC = "RelativeColorimetric";

	@BeforeClass
	public static void setUp() throws IOException, URISyntaxException {
		setUpOperatorsList(PBOp_ri.OP_RI_TYPE, null);
	}

	@Test
	public void testRenderingIntentLink() {
		Object object = testObject(PBOp_ri.RENDERING_INTENT, 1, PBCosRenderingIntent.COS_RENDERING_INTENT_TYPE);
		Assert.assertEquals(RELATIVE_COLORIMETRIC, ((CosRenderingIntent) object).getinternalRepresentation());
	}
}
