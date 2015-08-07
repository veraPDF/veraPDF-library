package org.verapdf.model.impl.pb.operator.textposition;

import org.junit.BeforeClass;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBOp_TdTest extends PBOp_General_TdTest {

	@BeforeClass
	public static void setUp() throws IOException, URISyntaxException {
		setUpOperatorsList(PBOp_Td.OP_TD_TYPE, null);
		expectedVertical = Long.valueOf(150);
		expectedHorizontal = Long.valueOf(50);
	}
}
