package org.verapdf.model.impl.pb.pd.images;

import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.graphics.PDXObject;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.pdlayer.PDColorSpace;
import org.verapdf.model.pdlayer.PDXImage;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBoxPDXImageTest extends PBoxPDAbstractXObjectTest {

	private static final String IMAGE_NAME = "Im0";
	private static final String SUBTYPE = "Image";

	@BeforeClass
	public static void setUp() throws IOException, URISyntaxException {
		expectedType = TYPES.contains(PBoxPDXImage.X_IMAGE_TYPE) ? PBoxPDXImage.X_IMAGE_TYPE : null;
		expectedID = null;

		setUp(FILE_RELATIVE_PATH);
		PDXObject xObject = document.getPage(0).getResources().getXObject(COSName.getPDFName(IMAGE_NAME));
		actual = new PBoxPDXImage((PDImageXObject) xObject);
	}

	@Override
	@Test
	public void testSubtypeMethod() {
		Assert.assertEquals(SUBTYPE, ((PDXImage) actual).getSubtype());
	}

	@Test
	public void testInterpolateMethod() {
		Assert.assertFalse(((PDXImage) actual).getInterpolate().booleanValue());
	}

	@Test
	public void testRenderingIntentLink() {
		List<? extends Object> intents = actual.getLinkedObjects(PBoxPDXImage.INTENT);
		Assert.assertEquals(0, intents.size());
	}

	@Test
	public void testImageColorSpaceLink() {
		List<? extends Object> colorSpace = actual.getLinkedObjects(PBoxPDXImage.IMAGE_CS);
		Assert.assertEquals(1, colorSpace.size());
		for (Object object : colorSpace) {
			Assert.assertTrue(object instanceof PDColorSpace);
		}
	}

	@Test
	public void testAlternatesLink() {
		List<? extends Object> alternate = actual.getLinkedObjects(PBoxPDXImage.ALTERNATES);
		Assert.assertEquals(0, alternate.size());
	}

}
