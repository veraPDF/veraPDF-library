package org.verapdf.model.impl.pb.pd.font;

import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.font.PDFontLike;
import org.verapdf.model.factory.font.FontFactory;
import org.verapdf.model.pdlayer.PDSimpleFont;

/**
 * @author Timur Kamalov
 */
public class PBoxPDSimpleFont extends PBoxPDFont implements PDSimpleFont {

	public static final String SIMPLE_FONT_TYPE = "PDSimpleFont";

	public PBoxPDSimpleFont(PDFontLike font) {
		super(font);
		setType(SIMPLE_FONT_TYPE);
	}

	@Override
	public Long getWidths_size() {
		Long widthSize = Long.valueOf(((org.apache.pdfbox.pdmodel.font.PDSimpleFont) pdFontLike).getWidths().size());
		return widthSize;
	}

	@Override
	public Long getLastChar() {
		Long lastChar = Long.valueOf(((org.apache.pdfbox.pdmodel.font.PDSimpleFont) pdFontLike).getCOSObject().getInt(COSName.LAST_CHAR));
		return lastChar;
	}

	@Override
	public Long getFirstChar() {
		Long firstChar = Long.valueOf(((org.apache.pdfbox.pdmodel.font.PDSimpleFont) pdFontLike).getCOSObject().getInt(COSName.FIRST_CHAR));
		return firstChar;
	}

	@Override
	public Boolean getisStandard() {
		if (getSubtype().equals(FontFactory.TYPE_1)) {
			return Boolean.valueOf(((org.apache.pdfbox.pdmodel.font.PDSimpleFont) pdFontLike).isStandard14());
		}
		return Boolean.FALSE;
	}

}
