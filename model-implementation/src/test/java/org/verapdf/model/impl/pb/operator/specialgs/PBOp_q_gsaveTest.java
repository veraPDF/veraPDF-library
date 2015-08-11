package org.verapdf.model.impl.pb.operator.specialgs;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.verapdf.model.impl.pb.operator.base.PBOperatorTest;
import org.verapdf.model.operator.Op_q_gsave;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBOp_q_gsaveTest extends PBOperatorTest {

	@BeforeClass
	public static void setUp() throws IOException, URISyntaxException {
		setUpOperatorsList(PBOp_q_gsave.OP_Q_GSAVE_TYPE, null);
	}

	@Test
	public void testNestingLevelMethod() {
		Assert.assertEquals(Long.valueOf(1), ((Op_q_gsave) actual).getnestingLevel());
	}
}
