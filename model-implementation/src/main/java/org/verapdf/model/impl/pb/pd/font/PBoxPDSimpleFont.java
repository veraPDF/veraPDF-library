package org.verapdf.model.impl.pb.pd.font;

import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.font.PDFontLike;
import org.verapdf.model.pdlayer.PDSimpleFont;

/**
 * @author Timur Kamalov
 */
public abstract class PBoxPDSimpleFont extends PBoxPDFont implements PDSimpleFont {

	public PBoxPDSimpleFont(PDFontLike font, final String type) {
		super(font, type);
	}

    @Override
    public Long getWidths_size() {
        return Long
                .valueOf(((org.apache.pdfbox.pdmodel.font.PDSimpleFont) pdFontLike)
                        .getWidths().size());
    }

	@Override
	public String getEncoding() {
		// TODO : implement me
		return null;
	}

	@Override
    public Long getLastChar() {
        return Long
                .valueOf(((org.apache.pdfbox.pdmodel.font.PDSimpleFont) pdFontLike)
                        .getCOSObject().getInt(COSName.LAST_CHAR));
    }

    @Override
    public Long getFirstChar() {
        return Long
                .valueOf(((org.apache.pdfbox.pdmodel.font.PDSimpleFont) pdFontLike)
                        .getCOSObject().getInt(COSName.FIRST_CHAR));
    }

    @Override
    public abstract Boolean getisStandard();

}
