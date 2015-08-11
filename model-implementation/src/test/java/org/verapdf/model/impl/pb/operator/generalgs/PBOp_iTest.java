package org.verapdf.model.impl.pb.operator.generalgs;

import org.junit.BeforeClass;
import org.junit.Test;
import org.verapdf.model.impl.pb.cos.PBCosReal;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBOp_iTest extends PBOpGeneralGSTest {

	@BeforeClass
	public static void setUp() throws IOException, URISyntaxException {
		setUpOperatorsList(PBOp_i.OP_I_TYPE, null);
	}

	@Test
	public void testFlatnessLink() {
		testLinkToReal(PBOp_i.FLATNESS, PBCosReal.COS_REAL_TYPE, Long.valueOf(50));
	}
}
