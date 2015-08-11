package org.verapdf.model.impl.pb.operator.markedcontent;

import org.junit.BeforeClass;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBOp_DPTest extends PBOpTagPropertiesLink {

	@BeforeClass
	public static void setUp() throws IOException, URISyntaxException {
		setUpOperatorsList(PBOp_DP.OP_DP_TYPE, null);
	}
}
