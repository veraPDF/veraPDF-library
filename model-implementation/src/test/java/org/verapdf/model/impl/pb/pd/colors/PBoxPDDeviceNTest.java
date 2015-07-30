package org.verapdf.model.impl.pb.pd.colors;

import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.graphics.color.PDColorSpace;
import org.apache.pdfbox.pdmodel.graphics.color.PDDeviceN;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.verapdf.model.baselayer.Object;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBoxPDDeviceNTest extends PBoxPDColorSpaceTest {

	private static final String COLOR_SPACE_NAME = "DeviceNCS";

	@BeforeClass
	public static void setUp() throws IOException, URISyntaxException {
		expectedType = TYPES.contains(PBoxPDDeviceN.DEVICE_N_TYPE) ? PBoxPDDeviceN.DEVICE_N_TYPE : null;
		expectedID = null;

		setUp(FILE_RELATIVE_PATH);
		PDColorSpace deviceN = document.getPage(0).getResources().getColorSpace(COSName.getPDFName(COLOR_SPACE_NAME));
		actual = new PBoxPDDeviceN((PDDeviceN) deviceN);
	}

	@Test
	public void testNumberOfComponentsMethod() {
		super.testNumberOfComponentsMethod(4);
	}

	@Test
	public void testAlternateLink() {
		List<? extends Object> alternate = actual.getLinkedObjects(PBoxPDDeviceN.ALTERNATE);
		Assert.assertEquals(1, alternate.size());
		for (Object object : alternate) {
			Assert.assertTrue(object instanceof org.verapdf.model.pdlayer.PDColorSpace);
		}
	}


}
