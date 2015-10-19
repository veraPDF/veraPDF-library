package org.verapdf.model.impl.pb.pd.font;

import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.font.PDType1CFont;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.verapdf.model.pdlayer.PDSimpleFont;
import org.verapdf.model.pdlayer.PDType1Font;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * @author Timur Kamalov
 */
public class PBoxPDType1FontTest extends PBoxPDSimpleFontTest {

	private static final String TYPE1_FONT_NAME = "T1_0";
	private static final String TYPE1_BASE_FONT = "OLXYQW+MyriadPro-Regular";
	private static final String TYPE1_SUBTYPE = "Type1";

	private static final String CHAR_SET = "/space/one/E/T/b/d/e/f/m/n/o/p/t/y";
	private static final String ENCODING = "WinAnsiEncoding";

	private static final Long WIDTHS_SIZE = 90l;
	private static final Long FIRST_CHAR = 32l;
	private static final Long LAST_CHAR = 121l;

	@BeforeClass
	public static void setUp() throws IOException, URISyntaxException {
		expectedType = TYPES.contains(PBoxPDType1Font.TYPE1_FONT_TYPE) ? PBoxPDType1Font.TYPE1_FONT_TYPE : null;

		setUp(FILE_RELATIVE_PATH);
		PDType1CFont type1Font = (PDType1CFont) document.getPage(0).getResources().getFont(COSName.getPDFName(TYPE1_FONT_NAME));
		actual = new PBoxPDType1Font(type1Font);

		expectedID = type1Font.getCOSObject().hashCode() + " OLXYQW+MyriadPro-Regular";
	}

	@Override
	public void testBaseFont() {
		Assert.assertEquals(TYPE1_BASE_FONT, ((PDSimpleFont) actual).getBaseFont());
	}

	@Override
	public void testSubtypeMethod() {
		Assert.assertEquals(TYPE1_SUBTYPE, ((PDSimpleFont) actual).getSubtype());
	}

	@Override
	public void testWidthsSize() {
		Assert.assertEquals(WIDTHS_SIZE, ((PDSimpleFont) actual).getWidths_size());
	}

	@Override
	public void testLastChar() {
		Assert.assertEquals(LAST_CHAR, ((PDSimpleFont) actual).getLastChar());
	}

	@Override
	public void testFirstChar() {
		Assert.assertEquals(FIRST_CHAR, ((PDSimpleFont) actual).getFirstChar());
	}

	@Override
	public void testIsStandard() {
		Assert.assertFalse(((PDSimpleFont) actual).getisStandard());
	}

	@Override
	public void testEncoding() {
		Assert.assertEquals(((PDType1Font) actual).getEncoding(), ENCODING);
	}

	@Test
	public void testCharSet() {
		Assert.assertEquals(((PDType1Font) actual).getCharSet(), CHAR_SET);
	}

}
