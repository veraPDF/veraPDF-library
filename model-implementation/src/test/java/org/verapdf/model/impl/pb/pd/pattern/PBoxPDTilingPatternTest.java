package org.verapdf.model.impl.pb.pd.pattern;

import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.graphics.pattern.PDTilingPattern;
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
		expectedType = TYPES.contains(PBoxPDTilingPattern.TILING_PATTERN_TYPE) ?
															PBoxPDTilingPattern.TILING_PATTERN_TYPE : null;
		expectedID = null;

		setUp(FILE_RELATIVE_PATH);
		PDResources resources = document.getPage(0).getResources();
		COSName patternName = COSName.getPDFName("P0");
		actual = new PBoxPDTilingPattern((PDTilingPattern) resources.getPattern(patternName));
	}

	@Test
	public void testContentStreamLink() {
		List<? extends Object> contentStream = actual.getLinkedObjects(PBoxPDTilingPattern.CONTENT_STREAM);
		Assert.assertEquals(1, contentStream.size());
		for (Object object : contentStream) {
			Assert.assertEquals(PBoxPDContentStream.CONTENT_STREAM_TYPE, object.getType());
		}
	}
}
