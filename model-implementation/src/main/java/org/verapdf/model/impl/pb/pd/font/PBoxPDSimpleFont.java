package org.verapdf.model.impl.pb.pd.font;

import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.font.PDFontLike;
import org.verapdf.model.pdlayer.PDSimpleFont;

/**
 * @author Timur Kamalov
 */
public abstract class PBoxPDSimpleFont extends PBoxPDFont implements PDSimpleFont {

    public static final String CUSTOM_ENCODING = "Custom";

	public PBoxPDSimpleFont(PDFontLike font, final String type) {
		super(font, type);
	}

    @Override
    public Long getWidths_size() {
        return Long
                .valueOf(((org.apache.pdfbox.pdmodel.font.PDSimpleFont) this.pdFontLike)
                        .getWidths().size());
    }

	@Override
	public String getEncoding() {
        COSDictionary fontDict = ((org.apache.pdfbox.pdmodel.font.PDSimpleFont) this.pdFontLike)
				.getCOSObject();
        COSBase encodingDict = fontDict.getDictionaryObject(COSName.ENCODING);
        if (encodingDict == null) {
            return null;
        } else if (encodingDict instanceof COSName) {
            return ((COSName) encodingDict).getName();
        } else if (encodingDict instanceof COSDictionary) {
            COSBase differencesDict = ((COSDictionary) encodingDict)
					.getDictionaryObject(COSName.DIFFERENCES);
			return differencesDict != null ? CUSTOM_ENCODING :
					((COSDictionary) encodingDict).getNameAsString(COSName.BASE_ENCODING);
        }
        return null;
	}

	@Override
    public Long getLastChar() {
        return Long
                .valueOf(((org.apache.pdfbox.pdmodel.font.PDSimpleFont) this.pdFontLike)
                        .getCOSObject().getInt(COSName.LAST_CHAR));
    }

    @Override
    public Long getFirstChar() {
        return Long
                .valueOf(((org.apache.pdfbox.pdmodel.font.PDSimpleFont) this.pdFontLike)
                        .getCOSObject().getInt(COSName.FIRST_CHAR));
    }

    @Override
    public abstract Boolean getisStandard();

}
