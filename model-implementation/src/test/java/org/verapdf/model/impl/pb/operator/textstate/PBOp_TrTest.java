package org.verapdf.model.impl.pb.operator.textstate;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.verapdf.model.impl.pb.operator.base.PBOperatorTest;
import org.verapdf.model.operator.Op_Tr;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBOp_TrTest extends PBOpTextStateTest {

	@BeforeClass
	public static void setUp() throws IOException, URISyntaxException {
		setUpOperatorsList(PBOp_Tr.OP_TR_TYPE, null);
	}

	@Test
	public void testRenderingModeMethod() {
		Assert.assertEquals(Long.valueOf(2), ((Op_Tr) actual).getrenderingMode());
	}

}
