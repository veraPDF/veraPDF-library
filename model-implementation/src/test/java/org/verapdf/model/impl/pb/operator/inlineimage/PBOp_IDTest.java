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
public class PBOp_IDTest extends PBOperatorTest {

	@BeforeClass
	public static void setUp() throws IOException, URISyntaxException {
		setUpOperatorsList(PBOp_ID.OP_ID_TYPE, null);
	}

	@Test
	public void testInlineImageDictionaryLink() {
		testLinkToDictionary(PBOp_ID.INLINE_IMAGE_DICTIONARY, PBCosDict.COS_DICT_TYPE, Long.valueOf(6));
	}
}
