package org.verapdf.model.impl.pb.operator.textstate;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.verapdf.model.coslayer.CosName;
import org.verapdf.model.impl.pb.cos.PBCosName;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBOp_TfTest extends PBOpTextStateTest {

	@BeforeClass
	public static void setUp() throws IOException, URISyntaxException {
		setUpOperatorsList(PBOp_Tf.OP_TF_TYPE, null);
	}

	@Test
	public void testSizeLink() {
		testReal(PBOp_Tf.SIZE, 12);
	}

	@Test
	public void testFontName() {
		CosName object = (CosName) testObject(PBOp_Tf.FONT_NAME, 1, PBCosName.COS_NAME_TYPE);
		Assert.assertNotNull(object);
		Assert.assertEquals("F1", object.getinternalRepresentation());
	}
}
