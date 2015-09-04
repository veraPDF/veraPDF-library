package org.verapdf.model.impl.pb.operator.textshow;

import org.junit.Assert;
import org.junit.Test;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.coslayer.CosString;
import org.verapdf.model.impl.pb.cos.PBCosString;

/**
 * @author Evgeniy Muravitskiy
 */
public abstract class PBOpStringTextShowTest extends PBOpTextShowTest {

	protected static String value;

	@Test
	public void testShowStringLink() {
		Object object = testObject(PBOpStringTextShow.SHOW_STRING, 1, PBCosString.COS_STRING_TYPE);
		Assert.assertEquals(value, ((CosString) object).getvalue());
	}

}
