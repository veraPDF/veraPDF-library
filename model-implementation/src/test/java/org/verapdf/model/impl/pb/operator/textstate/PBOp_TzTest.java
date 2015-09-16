package org.verapdf.model.impl.pb.operator.textstate;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.coslayer.CosReal;
import org.verapdf.model.impl.pb.cos.PBCosReal;
import org.verapdf.model.impl.pb.operator.base.PBOperatorTest;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBOp_TzTest extends PBOpTextStateTest {

	@BeforeClass
	public static void setUp() throws IOException, URISyntaxException {
		setUpOperatorsList(PBOp_Tz.OP_TZ_TYPE, null);
	}

	@Test
	public void testScaleLink() {
		Object object = testObject(PBOp_Tz.SCALE, 1, PBCosReal.COS_REAL_TYPE);
		Assert.assertEquals(Long.valueOf(120), ((CosReal) object).getintValue());
	}
}
