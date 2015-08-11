package org.verapdf.model.impl.pb.operator.generalgs;

import org.junit.BeforeClass;
import org.junit.Test;
import org.verapdf.model.impl.pb.cos.PBCosInteger;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBOp_j_line_joinTest extends PBOpGeneralGSTest {

	@BeforeClass
	public static void setUp() throws IOException, URISyntaxException {
		setUpOperatorsList(PBOp_j_line_join.OP_J_LINE_JOIN_TYPE, null);
	}

	@Test
	public void testLineJoinLink() {
		testLinkToReal(PBOp_j_line_join.LINE_JOIN, PBCosInteger.COS_INTEGER_TYPE, Long.valueOf(1));
	}
}
