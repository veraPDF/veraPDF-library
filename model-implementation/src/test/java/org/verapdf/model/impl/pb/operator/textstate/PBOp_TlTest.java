package org.verapdf.model.impl.pb.operator.textstate;

import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBOp_TlTest extends PBOpTextStateTest {

	@BeforeClass
	public static void setUp() throws IOException, URISyntaxException {
		setUpOperatorsList(PBOp_Tl.OP_TL_TYPE, null);
	}

	@Test
	public void testLeadingLink() {
		testReal(PBOp_Tl.LEADING, 0);
	}
}
