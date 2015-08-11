package org.verapdf.model.impl.pb.operator.inlineimage;

import org.junit.BeforeClass;
import org.junit.Test;
import org.verapdf.model.impl.pb.cos.PBCosDict;
import org.verapdf.model.impl.pb.operator.base.PBOperatorTest;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBOpInlineImageTest extends PBOperatorTest {

	@BeforeClass
	public static void setUp() throws IOException, URISyntaxException {
		setUpOperatorsList(PBOpInlineImage.OP_INLINE_IMAGE, null);
	}

	@Test
	public void testRenderingIntentLink() {
		testLinkToDictionary(PBOpInlineImage.INLINE_IMAGE_DICTIONARY, PBCosDict.COS_DICT_TYPE, Long.valueOf(6));
	}
}
