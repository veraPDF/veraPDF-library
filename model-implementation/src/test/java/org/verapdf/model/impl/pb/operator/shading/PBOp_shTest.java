package org.verapdf.model.impl.pb.operator.shading;

import org.junit.BeforeClass;
import org.junit.Test;
import org.verapdf.model.impl.pb.operator.base.PBOperatorTest;
import org.verapdf.model.impl.pb.pd.pattern.PBoxPDShading;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBOp_shTest extends PBOperatorTest {

	@BeforeClass
	public static void setUp() throws IOException, URISyntaxException {
		setUpOperatorsList(PBOp_sh.OP_SH_TYPE, null);
	}

	@Test
	public void testShadingLink() {
		testObject(PBOp_sh.SHADING, 1, PBoxPDShading.SHADING_TYPE);
	}
}
