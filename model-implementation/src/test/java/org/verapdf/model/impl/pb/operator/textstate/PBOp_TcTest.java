package org.verapdf.model.impl.pb.operator.textstate;

import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBOp_TcTest extends PBOpTextStateTest {

	@BeforeClass
	public static void setUp() throws IOException, URISyntaxException {
		setUpOperatorsList(PBOp_Tc.OP_TC_TYPE, null);
	}

	@Test
	public void testCharSpacingLink() {
		testReal(PBOp_Tc.CHAR_SPACING, 1);
	}
}
