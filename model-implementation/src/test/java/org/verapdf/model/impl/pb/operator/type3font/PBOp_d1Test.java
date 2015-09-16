package org.verapdf.model.impl.pb.operator.type3font;

import org.apache.pdfbox.contentstream.operator.Operator;
import org.apache.pdfbox.cos.COSInteger;
import org.junit.BeforeClass;
import org.junit.Test;
import org.verapdf.model.factory.operator.OperatorFactory;
import org.verapdf.model.impl.pb.cos.PBCosReal;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBOp_d1Test extends PBOpType3FontTest {

	@BeforeClass
	public static void setUp() throws IOException, URISyntaxException {
		expectedType = PBOp_d1.OP_D1_TYPE;
		List<Object> objects = new ArrayList<>(7);
		Random random = new Random(150);
		for (int i = 0; i < 6; i++) {
			objects.add(COSInteger.get(random.nextInt()));
		}
		objects.add(Operator.getOperator("d1"));
		actual = OperatorFactory.operatorsFromTokens(objects, null).get(0);
	}

	@Test
	public void testControlPointsLink() {
		testLinksToReals(PBOp_d1.CONTROL_POINTS, 6, PBCosReal.COS_REAL_TYPE);
	}
}
