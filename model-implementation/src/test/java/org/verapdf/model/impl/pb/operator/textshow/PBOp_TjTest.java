package org.verapdf.model.impl.pb.operator.textshow;

import org.junit.BeforeClass;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBOp_TjTest extends PBOpStringTextShowTest {

	@BeforeClass
	public static void setUp() throws IOException, URISyntaxException {
		setUpOperatorsList(PBOp_Tj.OP_TJ_TYPE, null);
		value = "Hello World";
	}

	@Override
	protected int getUsedGlyphsAmount() {
		return 11;
	}

}
