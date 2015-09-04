package org.verapdf.model.impl.pb.operator.textshow;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.coslayer.CosNumber;
import org.verapdf.model.impl.pb.cos.PBCosReal;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBOp_DoubleQuoteTest extends PBOpStringTextShowTest {

	@BeforeClass
	public static void setUp() throws IOException, URISyntaxException {
		setUpOperatorsList(PBOp_DoubleQuote.OP_DOUBLIE_QUOTE_TYPE, null);
		value = "WH";
	}

	@Test
	public void testWordSpacingLink() {
		Object object = testObject(PBOp_DoubleQuote.WORD_SPACING, 1, PBCosReal.COS_REAL_TYPE);
		Assert.assertEquals(Long.valueOf(50), ((CosNumber) object).getintValue());
	}

	@Test
	public void testCharacterSpacingLink() {
		Object object = testObject(PBOp_DoubleQuote.CHARACTER_SPACING, 1, PBCosReal.COS_REAL_TYPE);
		Assert.assertEquals(Long.valueOf(1), ((CosNumber) object).getintValue());
	}

	@Override
	protected int getUsedGlyphsAmount() {
		return 0;
	}

}
