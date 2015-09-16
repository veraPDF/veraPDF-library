package org.verapdf.model.impl.pb.operator.textstate;

import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBOp_TsTest extends PBOpTextStateTest {

	@BeforeClass
	public static void setUp() throws IOException, URISyntaxException {
		setUpOperatorsList(PBOp_Ts.OP_TS_TYPE, null);
	}

	@Test
	public void testRiseLink() {
		testReal(PBOp_Ts.RISE, 0);
	}
}
