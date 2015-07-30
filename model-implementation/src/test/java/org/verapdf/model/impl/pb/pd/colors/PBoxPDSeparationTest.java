package org.verapdf.model.impl.pb.pd.colors;

import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.graphics.color.PDColorSpace;
import org.apache.pdfbox.pdmodel.graphics.color.PDSeparation;
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
public class PBoxPDSeparationTest extends PBoxPDColorSpaceTest {

	private static final String COLOR_SPACE_NAME = "SeparationCS";

	@BeforeClass
	public static void setUp() throws IOException, URISyntaxException {
		expectedType = TYPES.contains(PBoxPDSeparation.SEPARATION_TYPE) ? PBoxPDSeparation.SEPARATION_TYPE : null;
		expectedID = null;

		setUp(FILE_RELATIVE_PATH);
		PDColorSpace separation = document.getPage(0).getResources().getColorSpace(COSName.getPDFName(COLOR_SPACE_NAME));
		actual = new PBoxPDSeparation((PDSeparation) separation);
	}

	@Test
	public void testNumberOfComponentsMethod() {
		super.testNumberOfComponentsMethod(1);
	}

	@Test
	public void testAlternateLink() {
		List<? extends Object> alternate = actual.getLinkedObjects(PBoxPDSeparation.ALTERNATE);
		Assert.assertEquals(1, alternate.size());
		for (Object object : alternate) {
			Assert.assertTrue(object instanceof org.verapdf.model.pdlayer.PDColorSpace);
		}
	}
}
