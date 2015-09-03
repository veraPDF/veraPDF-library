package org.verapdf.model.impl.pb.operator.inlineimage;

import org.junit.BeforeClass;
import org.junit.Test;
import org.verapdf.model.impl.pb.operator.base.PBOperatorTest;
import org.verapdf.model.impl.pb.pd.images.PBoxPDInlineImage;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * @author Evgeniy Muravitskiy
 */
public class PB_Op_EITest extends PBOperatorTest {

	@BeforeClass
	public static void setUp() throws IOException, URISyntaxException {
		setUpOperatorsList(PBOp_EI.OP_EI_TYPE, null);
	}

	@Test
	public void testInlineImageLink() {
		testObject(PBOp_EI.INLINE_IMAGE, 1, PBoxPDInlineImage.INLINE_IMAGE_TYPE);
	}
}
