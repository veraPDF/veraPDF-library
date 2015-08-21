package org.verapdf.model.impl.pb.operator.inlineimage;

import org.junit.BeforeClass;
import org.verapdf.model.impl.pb.operator.base.PBOperatorTest;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBOp_BITest extends PBOperatorTest {

	@BeforeClass
	public static void setUp() throws IOException, URISyntaxException {
		setUpOperatorsList(PBOp_EI.OP_EI_TYPE, null);
	}

}
