package org.verapdf.model.impl.pb.pd.font;

import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.coslayer.CosName;
import org.verapdf.model.pdlayer.PDCIDFont;
import org.verapdf.model.pdlayer.PDType0Font;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

/**
 * @author Timur Kamalov
 */
public class PBoxPDType0CIDType2Test extends PBoxPDType0FontTest {

	private static final String TYPE0_FONT_NAME = "C2_0";
	private static final String TYPE0_BASE_FONT = "CBTOEA+ArialMT-Identity-H";
	private static final String TYPE0_SUBTYPE = "Type0";

	@BeforeClass
	public static void setUp() throws IOException, URISyntaxException {
		expectedType = TYPES.contains(PBoxPDType0Font.TYPE_0_FONT_TYPE) ? PBoxPDType0Font.TYPE_0_FONT_TYPE : null;

		setUp(FILE_RELATIVE_PATH);
		PDFont type0Font = document.getPage(0).getResources().getFont(COSName.getPDFName(TYPE0_FONT_NAME));
		actual = new PBoxPDType0Font(type0Font);

		expectedID = type0Font.getCOSObject().hashCode() + " CBTOEA+ArialMT-Identity-H";
	}

	@Override
	public void testBaseFont() {
		List<? extends Object> baseFonts = actual.getLinkedObjects(PBoxPDType1Font.BASE_FONT);
		Object object = baseFonts.get(0);
		Assert.assertEquals("CosUnicodeName", object.getObjectType());
		Assert.assertEquals("CBTOEA+ArialMT-Identity-H", ((CosName) object).getinternalRepresentation());
	}

	@Override
	public void testSubtypeMethod() {
		Assert.assertEquals(TYPE0_SUBTYPE, ((PDType0Font) actual).getSubtype());
	}

	@Override
	public void testDescendantFonts() {
		List<? extends Object> descendantFonts = ((PDType0Font) actual).getLinkedObjects(PBoxPDType0Font.DESCENDANT_FONTS);
		Assert.assertEquals(descendantFonts.size(), 1);
		Assert.assertTrue(descendantFonts.get(0) instanceof PDCIDFont);
	}

	@Override
	public void testEncoding() {
		List<? extends Object> encoding = ((PDType0Font) actual).getLinkedObjects(PBoxPDType0Font.ENCODING);
		Assert.assertEquals(encoding.size(), 1);
		Assert.assertTrue(encoding.get(0) instanceof PBoxPDCMap);
	}

	@Override
	public void testAreRegistryOrderingCompatible() {
		Assert.assertTrue(((PDType0Font) actual).getareRegistryOrderingCompatible());
	}

}
