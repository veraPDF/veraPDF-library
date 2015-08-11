package org.verapdf.model.impl.pb.operator.generalgs;

import org.junit.BeforeClass;
import org.junit.Test;
import org.verapdf.model.impl.pb.cos.PBCosReal;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBOp_w_line_widthTest extends PBOpGeneralGSTest {

	@BeforeClass
	public static void setUp() throws IOException, URISyntaxException {
		setUpOperatorsList(PBOp_w_line_width.OP_W_LINE_WIDTH_TYPE, null);
	}

	@Test
	public void testLineWidthLink() {
		testLinkToReal(PBOp_w_line_width.LINE_WIDTH, PBCosReal.COS_REAL_TYPE, Long.valueOf(10));
	}
}
