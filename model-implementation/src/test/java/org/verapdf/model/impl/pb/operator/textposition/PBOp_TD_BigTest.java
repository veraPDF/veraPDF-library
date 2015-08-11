package org.verapdf.model.impl.pb.operator.textposition;

import org.junit.BeforeClass;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBOp_TD_BigTest extends PBOp_General_TdTest {

	@BeforeClass
	public static void setUp() throws IOException, URISyntaxException {
		setUpOperatorsList(PBOp_TD_Big.OP_TD_BIG_TYPE, null);
		expectedVertical = Long.valueOf(10);
		expectedHorizontal = Long.valueOf(100);
	}
}
