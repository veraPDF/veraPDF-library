package org.verapdf.model.impl.pb.pd.images;

import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.graphics.image.PDInlineImage;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBoxPDInlineImageTest extends PBoxPDXImageTest {

	@BeforeClass
	public static void setUp() throws IOException, URISyntaxException {
		expectedType = TYPES.contains(PBoxPDInlineImage.INLINE_IMAGE_TYPE) ?
				PBoxPDInlineImage.INLINE_IMAGE_TYPE : null;
		expectedID = null;

		final COSDictionary parameters = getParameters();
		PDInlineImage image = new PDInlineImage(parameters, new byte[0], new PDResources(new COSDictionary()));
		actual = new PBoxPDInlineImage(image);
	}

	private static COSDictionary getParameters() {
		COSDictionary dictionary = new COSDictionary();
		dictionary.setItem(COSName.CS, COSName.RGB);
		dictionary.setItem(COSName.F, COSName.LZW_DECODE_ABBREVIATION);
		return dictionary;
	}

	@Override
	@Test
	public void testSubtypeMethod() {
		Assert.assertNull(((org.verapdf.model.pdlayer.PDInlineImage) actual).getSubtype());
	}

	@Test
	public void testFMethod() {
		Assert.assertEquals("LZW", ((org.verapdf.model.pdlayer.PDInlineImage) actual).getF());
	}

	@AfterClass
	public static void tearDown() throws IOException {
		expectedType = null;
		expectedID = null;
		actual = null;
	}

}
