package org.verapdf.model.factory.font;

import org.apache.pdfbox.pdmodel.font.PDTrueTypeFont;
import org.apache.pdfbox.pdmodel.font.PDType1CFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.verapdf.model.impl.pb.pd.font.PBoxPDTrueTypeFont;
import org.verapdf.model.impl.pb.pd.font.PBoxPDType0Font;
import org.verapdf.model.impl.pb.pd.font.PBoxPDType1Font;
import org.verapdf.model.impl.pb.pd.font.PBoxPDType3Font;
import org.verapdf.model.pdlayer.PDFont;

/**
 * @author Timur Kamalov
 */
public final class FontFactory {

    public static final String TYPE_0 = "Type0";
    public static final String TYPE_1 = "Type1";
    public static final String TYPE_3 = "Type3";
    public static final String TRUE_TYPE = "TrueType";

    private FontFactory() {
        // Disable default constructor
    }

	public static PDFont parseFont(
			org.apache.pdfbox.pdmodel.font.PDFont pdfboxFont) {
		switch (pdfboxFont.getSubType()) {
			case TYPE_0:
				return new PBoxPDType0Font(pdfboxFont);
			case TYPE_1:
				if (pdfboxFont instanceof PDType1Font) {
					return new PBoxPDType1Font((PDType1Font) pdfboxFont);
				} else if (pdfboxFont instanceof PDType1CFont) {
					return new PBoxPDType1Font((PDType1CFont) pdfboxFont);
				}
			case TYPE_3:
				return new PBoxPDType3Font(pdfboxFont);
			case TRUE_TYPE:
				return new PBoxPDTrueTypeFont((PDTrueTypeFont) pdfboxFont);
			default:
				return null;
		}
	}

}
