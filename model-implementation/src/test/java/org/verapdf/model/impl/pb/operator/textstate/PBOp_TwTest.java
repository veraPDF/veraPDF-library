package org.verapdf.model.impl.pb.operator.textstate;

import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBOp_TwTest extends PBOpTextStateTest {

	@BeforeClass
	public static void setUp() throws IOException, URISyntaxException {
		setUpOperatorsList(PBOp_Tw.OP_TW_TYPE, null);
	}

	@Test
	public void testRiseLink() {
		testReal(PBOp_Tw.WORD_SPACE, 50);
	}
}
