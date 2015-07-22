package org.verapdf.model.impl.pb.cos;

import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.graphics.state.RenderingIntent;
import org.junit.BeforeClass;

import java.util.ArrayList;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBCosRenderingIntentTest extends PBCosNameTest {

	@BeforeClass
	public static void setUp() {
		expectedType = "CosRenderingIntent";
		expectedID = null;

		expected = new ArrayList<>(1);
		actual = new ArrayList<>(1);

		String string = RenderingIntent.PERCEPTUAL.stringValue();
		COSName pdfName = COSName.getPDFName(string);
		expected.add(pdfName);
		actual.add(new PBCosRenderingIntent(pdfName));
	}
}
