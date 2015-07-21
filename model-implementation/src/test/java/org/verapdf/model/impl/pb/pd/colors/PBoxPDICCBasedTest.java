package org.verapdf.model.impl.pb.pd.colors;

import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.graphics.color.PDColorSpace;
import org.apache.pdfbox.pdmodel.graphics.color.PDICCBased;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.impl.pb.external.PBoxICCInputProfile;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBoxPDICCBasedTest extends PBoxPDColorSpaceTest {

	private static final String COLOR_SPACE_NAME = "ICCBasedCS";

	@BeforeClass
	public static void setUp() throws IOException, URISyntaxException {
		expectedType = PBoxPDICCBased.ICC_BASED_TYPE;
		expectedID = null;

		setUp(FILE_RELATIVE_PATH);
		PDColorSpace iccBased = document.getPage(0).getResources().getColorSpace(COSName.getPDFName(COLOR_SPACE_NAME));
		actual = new PBoxPDICCBased((PDICCBased) iccBased);
	}

	@Test
	public void testNumberOfComponentsMethod() {
		super.testNumberOfComponentsMethod(3);
	}

	@Test
	public void testICCProfileLink() {
		List<? extends Object> profile = actual.getLinkedObjects(PBoxPDICCBased.ICC_PROFILE);
		Assert.assertEquals(1, profile.size());
		for (Object object : profile) {
			Assert.assertEquals(PBoxICCInputProfile.ICC_INPUT_PROFILE_TYPE, object.getType());
		}
	}

}
