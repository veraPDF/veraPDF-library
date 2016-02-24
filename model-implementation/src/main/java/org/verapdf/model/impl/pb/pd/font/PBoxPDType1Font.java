package org.verapdf.model.impl.pb.pd.font;

import org.apache.fontbox.FontBoxFont;
import org.apache.fontbox.cff.CFFFont;
import org.apache.fontbox.type1.Type1Font;
import org.apache.log4j.Logger;
import org.apache.pdfbox.pdmodel.font.PDFontDescriptor;
import org.verapdf.model.pdlayer.PDType1Font;

import java.io.IOException;

/**
 * @author Timur Kamalov
 */
public class PBoxPDType1Font extends PBoxPDSimpleFont implements PDType1Font {

	private static final Logger LOGGER = Logger.getLogger(PBoxPDType1Font.class);

	public static final String UNDEFINED_GLYPH = ".notdef";
	public static final String TYPE1_FONT_TYPE = "PDType1Font";

	public PBoxPDType1Font(org.apache.pdfbox.pdmodel.font.PDType1Font font) {
		super(font, TYPE1_FONT_TYPE);
	}

	public PBoxPDType1Font(org.apache.pdfbox.pdmodel.font.PDType1CFont font) {
		super(font, TYPE1_FONT_TYPE);
	}

	@Override
	public Boolean getcharSetListsAllGlyphs() {
		try {
			PDFontDescriptor fontDescriptor = pdFontLike.getFontDescriptor();
			if (fontDescriptor != null) {
				String charSet = fontDescriptor.getCharSet();
				if (charSet != null) {
					String[] splittedCharSet = fontDescriptor.getCharSet().split("/");
					// TODO : Log warning if charset doesn't start with '/'

					FontBoxFont font = ((org.apache.pdfbox.pdmodel.font.PDSimpleFont) pdFontLike).getFontBoxFont();
					for (int i = 1; i < splittedCharSet.length; i++) {
						if (!font.hasGlyph(splittedCharSet[i])) {
							return Boolean.FALSE;
						}
					}
					if (font instanceof Type1Font) {
						if (((Type1Font) font).getCharStringsDict().size() != splittedCharSet.length) {
							return Boolean.FALSE;
						}
					} else if (font instanceof CFFFont) {
						if (((CFFFont) font).getNumCharStrings() != splittedCharSet.length) {
							return Boolean.FALSE;
						}
					}

					// Do not check .undef glyph presence in font file, though it's required by ISO-32000
//					if (!font.hasGlyph(UNDEFINED_GLYPH)) {
//						return Boolean.FALSE;
//					}
					return Boolean.TRUE;
				}
			}
		} catch (IOException e) {
			LOGGER.error("Error while parsing embedded font program. " + e.getMessage(), e);
		}
		return Boolean.FALSE;
	}

	@Override
	public Boolean getisStandard() {
		return Boolean.valueOf(((org.apache.pdfbox.pdmodel.font.PDSimpleFont) this.pdFontLike)
				.isStandard14());
	}

	@Override
	public String getCharSet() {
		PDFontDescriptor fontDescriptor = pdFontLike.getFontDescriptor();
		return fontDescriptor != null ? fontDescriptor.getCharSet() : null;
	}

}
