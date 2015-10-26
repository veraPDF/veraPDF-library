package org.verapdf.model.impl.pb.pd.font;

import org.apache.pdfbox.pdmodel.font.PDFontDescriptor;
import org.verapdf.model.pdlayer.PDType1Font;

/**
 * @author Timur Kamalov
 */
public class PBoxPDType1Font extends PBoxPDSimpleFont implements PDType1Font {

	public static final String TYPE1_FONT_TYPE = "PDType1Font";

	public PBoxPDType1Font(org.apache.pdfbox.pdmodel.font.PDType1Font font) {
		super(font, TYPE1_FONT_TYPE);
	}

	public PBoxPDType1Font(org.apache.pdfbox.pdmodel.font.PDType1CFont font) {
		super(font, TYPE1_FONT_TYPE);
	}

	// TODO implement me
	public Boolean getcharSetListsAllGlyphs() {
		return null;
	}

	@Override
	public Boolean getisStandard() {
		return Boolean.valueOf(((org.apache.pdfbox.pdmodel.font.PDSimpleFont) this.pdFontLike)
				.isStandard14());
	}

	@Override
	public String getCharSet() {
		PDFontDescriptor fontDescriptor = pdFontLike.getFontDescriptor();
		if (fontDescriptor != null) {
			return fontDescriptor.getCharSet();
		} else {
			return null;
		}
	}

}
