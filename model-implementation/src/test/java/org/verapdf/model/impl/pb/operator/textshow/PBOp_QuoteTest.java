package org.verapdf.model.impl.pb.operator.textshow;

import org.junit.BeforeClass;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBOp_QuoteTest extends PBOpStringTextShowTest {

	@BeforeClass
	public static void setUp() throws IOException, URISyntaxException {
		setUpOperatorsList(PBOp_Quote.OP_QUOTE_TYPE, null);
		value = "Hello";
	}

	@Override
	public void testStrokeColorSpaceLink() {
		super.testStrokeColorSpaceLink();
	}

	@Override
	protected int getUsedGlyphsAmount() {
		return 5;
	}

}
