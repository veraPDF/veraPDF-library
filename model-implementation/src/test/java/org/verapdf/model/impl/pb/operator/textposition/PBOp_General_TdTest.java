package org.verapdf.model.impl.pb.operator.textposition;

import org.junit.Assert;
import org.junit.Test;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.coslayer.CosReal;
import org.verapdf.model.impl.pb.cos.PBCosReal;
import org.verapdf.model.impl.pb.operator.base.PBOperatorTest;

/**
 * @author Evgeniy Muravitskiy
 */
public abstract class PBOp_General_TdTest extends PBOpTextPositionTest {

	protected static Long expectedVertical;
	protected static Long expectedHorizontal;

	@Test
	public void testVerticalOffset() {
		testOffset(PBOp_General_Td.VERTICAL_OFFSET, expectedVertical);
	}

	@Test
	public void testHorizontalOffset() {
		testOffset(PBOp_General_Td.HORIZONTAL_OFFSET, expectedHorizontal);
	}

	private void testOffset(String link, Long value) {
		Object object = testObject(link, 1, PBCosReal.COS_REAL_TYPE);
		Assert.assertEquals(value, ((CosReal) object).getintValue());
	}
}
