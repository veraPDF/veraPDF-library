package org.verapdf.model.impl.pb.operator.specialgs;

import org.junit.BeforeClass;
import org.junit.Test;
import org.verapdf.model.impl.pb.cos.PBCosReal;
import org.verapdf.model.impl.pb.operator.base.PBOperatorTest;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBOp_cmTest extends PBOperatorTest {

	@BeforeClass
	public static void setUp() throws IOException, URISyntaxException {
		setUpOperatorsList(PBOp_cm.OP_CM_TYPE, null);
	}

	@Test
	public void testMatrixLink() {
		testLinksToReals(PBOp_cm.MATRIX, 6, PBCosReal.COS_REAL_TYPE);
	}
}
