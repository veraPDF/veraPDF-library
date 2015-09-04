package org.verapdf.model.impl.pb.operator.textshow;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.verapdf.model.coslayer.CosArray;
import org.verapdf.model.impl.pb.cos.PBCosArray;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBOp_TJ_BigTest extends PBOpTextShowTest {

	@BeforeClass
	public static void setUp() throws IOException, URISyntaxException {
		setUpOperatorsList(PBOp_TJ_Big.OP_TJ_BIG_TYPE, null);
	}

	@Test
	public void testSpecialStringsLink() {
		CosArray array = (CosArray) testObject(PBOp_TJ_Big.SPECIAL_STRINGS, 1, PBCosArray.COS_ARRAY_TYPE);
		Assert.assertEquals(Long.valueOf(5), array.getsize());
	}

	@Override
	protected int getUsedGlyphsAmount() {
		return 10;
	}

}
