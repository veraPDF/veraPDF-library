package org.verapdf.model.impl.pb.pd.images;

import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.graphics.PDXObject;
import org.junit.Assert;
import org.junit.BeforeClass;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBoxPDXObjectTest extends PBoxPDAbstractXObjectTest {

	private static final String POST_SCRIPT_NAME = "X2";
	private static final String POST_SCRIPT_SUBTYPE = "PS";

	@BeforeClass
	public static void setUp() throws IOException, URISyntaxException {
		expectedType = TYPES.contains(PBoxPDXObject.X_OBJECT_TYPE) ? PBoxPDXObject.X_OBJECT_TYPE : null;
		expectedID = null;

		setUp(FILE_RELATIVE_PATH);
		PDXObject xObject = document.getPage(0).getResources().getXObject(COSName.getPDFName(POST_SCRIPT_NAME));
		actual = new PBoxPDXObject(xObject);
	}

	@Override
	public void testSubtypeMethod() {
		Assert.assertEquals(POST_SCRIPT_SUBTYPE, ((org.verapdf.model.pdlayer.PDXObject) actual).getSubtype());
	}

}
