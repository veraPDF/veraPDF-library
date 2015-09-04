package org.verapdf.model.impl.pb.pd.pattern;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.impl.pb.pd.PBoxPDContentStream;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBoxPDTilingPatternTest extends PBoxPDPatternTest{

	@BeforeClass
	public static void setUp() throws URISyntaxException, IOException {
		setUp(PBoxPDTilingPattern.TILING_PATTERN_TYPE, "P0");
	}

	@Test
	public void testContentStreamLink() {
		List<? extends Object> contentStream = actual.getLinkedObjects(PBoxPDTilingPattern.CONTENT_STREAM);
		Assert.assertEquals(1, contentStream.size());
		for (Object object : contentStream) {
			Assert.assertEquals(PBoxPDContentStream.CONTENT_STREAM_TYPE, object.getObjectType());
		}
	}
}
