package org.verapdf.model.impl.pb.pd;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.graphics.form.PDGroup;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.impl.BaseTest;
import org.verapdf.model.pdlayer.PDColorSpace;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBoxPDGroupTest extends BaseTest {

	public static final String FILE_RELATIVE_PATH = "pd/InteractiveObjects.pdf";

	@BeforeClass
	public static void setUp() throws URISyntaxException, IOException {
		expectedType = TYPES.contains(PBoxPDGroup.GROUP_TYPE) ? PBoxPDGroup.GROUP_TYPE : null;
		expectedID = null;

		setUp(FILE_RELATIVE_PATH);
		COSBase groupDictionary = document.getPage(0).getCOSObject().getDictionaryObject(COSName.GROUP);
		actual = new PBoxPDGroup(new PDGroup((COSDictionary) groupDictionary));
	}

	@Test
	public void testSubtypeMethod() {
		Assert.assertEquals("Transparency", ((org.verapdf.model.pdlayer.PDGroup) actual).getS());
	}

	@Test
	public void testColorSpaceLink() {
		List<? extends Object> colorSpace = actual.getLinkedObjects(PBoxPDGroup.COLOR_SPACE);
		Assert.assertEquals(1, colorSpace.size());
		for (Object object : colorSpace) {
			Assert.assertTrue(object instanceof PDColorSpace);
		}
	}

}
