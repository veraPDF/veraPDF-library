package org.verapdf.model.impl.pb.pd;

import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDResources;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.impl.BaseTest;
import org.verapdf.model.impl.pb.cos.PBCosRenderingIntent;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBoxPDExtGStateTest extends BaseTest {

	public static final String FILE_RELATIVE_PATH = "pd/InteractiveObjects.pdf";

	@BeforeClass
	public static void setUp() throws URISyntaxException, IOException {
		expectedType = TYPES.contains(PBoxPDExtGState.EXT_G_STATE_TYPE) ? PBoxPDExtGState.EXT_G_STATE_TYPE : null;
		expectedID = null;

		setUp(FILE_RELATIVE_PATH);
		PDResources resources = document.getPage(0).getResources();
		COSName next = resources.getExtGStateNames().iterator().next();
		actual = new PBoxPDExtGState(resources.getExtGState(next));
	}

	@Test
	public void testTRMethod() {
		Assert.assertNull(((PBoxPDExtGState) actual).getTR());
	}

	@Test
	public void testTR2Method() {
		Assert.assertNull(((PBoxPDExtGState) actual).getTR2());
	}

	@Test
	public void testSMaskMethod() {
		Assert.assertNull(((PBoxPDExtGState) actual).getSMask());
	}

	@Test
	public void testBMMethod() {
		Assert.assertEquals("Screen", ((PBoxPDExtGState) actual).getBM());
	}

	@Test
	public void testFillCAMethod() {
		Assert.assertNull(((PBoxPDExtGState) actual).getca());
	}

	@Test
	public void testStrokeCAMethod() {
		Assert.assertNull(((PBoxPDExtGState) actual).getCA());
	}

	@Test
	public void testRenderingIntentLink() {
		List<? extends Object> ri = actual.getLinkedObjects(PBoxPDExtGState.RI);
		Assert.assertEquals(1, ri.size());
		for (Object object : ri) {
			Assert.assertEquals(PBCosRenderingIntent.COS_RENDERING_INTENT_TYPE, object.getObjectType());
		}
	}

	@Test
	public void testFontSize() {
		List<? extends Object> fontSize = actual.getLinkedObjects(PBoxPDExtGState.FONT_SIZE);
		Assert.assertEquals(0, fontSize.size());
	}

}
