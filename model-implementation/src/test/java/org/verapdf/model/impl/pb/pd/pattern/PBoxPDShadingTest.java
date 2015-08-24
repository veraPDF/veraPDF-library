package org.verapdf.model.impl.pb.pd.pattern;

import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDResources;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.impl.pb.pd.colors.PBoxPDDeviceRGB;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBoxPDShadingTest extends PBoxPDPatternTest {

	@BeforeClass
	public static void setUp() throws URISyntaxException, IOException {
		expectedType = TYPES.contains(PBoxPDShading.SHADING_TYPE) ? PBoxPDShading.SHADING_TYPE : null;
		expectedID = null;

		setUp(FILE_RELATIVE_PATH);
		PDResources resources = document.getPage(0).getResources();
		COSName patternName = COSName.getPDFName("SH0");
		actual = new PBoxPDShading(resources.getShading(patternName));
	}

	@Test
	public void testColorSpaceLink() {
		List<? extends Object> colorSpace = actual.getLinkedObjects(PBoxPDShading.COLOR_SPACE);
		Assert.assertEquals(1, colorSpace.size());
		for (Object object : colorSpace) {
			Assert.assertEquals(PBoxPDDeviceRGB.DEVICE_RGB_TYPE, object.getObjectType());
		}
	}

}
