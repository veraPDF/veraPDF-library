package org.verapdf.model.impl.pb.operator.generalgs;

import org.junit.BeforeClass;
import org.junit.Test;
import org.verapdf.model.impl.pb.cos.PBCosInteger;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBOp_J_line_capTest extends PBOpGeneralGSTest {

	@BeforeClass
	public static void setUp() throws IOException, URISyntaxException {
		setUpOperatorsList(PBOp_J_line_cap.OP_J_LINE_CAP_TYPE, null);
	}

	@Test
	public void testLineCapLink() {
		testLinkToReal(PBOp_J_line_cap.LINE_CAP, PBCosInteger.COS_INTEGER_TYPE, Long.valueOf(1));
	}
}
