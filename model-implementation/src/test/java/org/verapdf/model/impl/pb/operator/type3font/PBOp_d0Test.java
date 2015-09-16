package org.verapdf.model.impl.pb.operator.type3font;

import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBOp_d0Test extends PBOpType3FontTest {

	@BeforeClass
	public static void setUp() throws IOException, URISyntaxException {
		setUpOperatorsList(PBOp_d0.OP_D0_TYPE, null);
	}

	@Test
	public void testHorizontalDisplacementLink() {
		testReal(PBOp_d0.HORIZONTAL_DISPLACEMENT, 300);
	}

	@Test
	public void testVerticalDisplacementLink() {
		testReal(PBOp_d0.VERTICAL_DISPLACEMENT, 50);
	}
}
