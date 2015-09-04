package org.verapdf.model.impl.pb.operator.generalgs;

import org.junit.Assert;
import org.verapdf.model.coslayer.CosNumber;
import org.verapdf.model.impl.pb.operator.base.PBOperatorTest;

/**
 * @author Evgeniy Muravitskiy
 */
public abstract class PBOpGeneralGSTest extends PBOperatorTest {

	protected static void testLinkToReal(String link, String type, Number expectedResult) {
		org.verapdf.model.baselayer.Object object = testObject(link, 1, type);
		Assert.assertEquals(expectedResult, ((CosNumber) object).getintValue());
	}
}
