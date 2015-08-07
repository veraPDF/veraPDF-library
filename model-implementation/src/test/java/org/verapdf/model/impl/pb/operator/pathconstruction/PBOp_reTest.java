package org.verapdf.model.impl.pb.operator.pathconstruction;

import org.junit.BeforeClass;
import org.junit.Test;
import org.verapdf.model.impl.pb.cos.PBCosReal;
import org.verapdf.model.impl.pb.operator.base.PBOperatorTest;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBOp_reTest extends PBOperatorTest {

	@BeforeClass
	public static void setUp() throws IOException, URISyntaxException {
		setUpOperatorsList(PBOp_re.OP_RE_TYPE, null);
	}

	@Test
	public void testRectangleBoxTest() {
		testLinksToReals(PBOp_re.RECT_BOX, 4, PBCosReal.COS_REAL_TYPE);
	}
}
