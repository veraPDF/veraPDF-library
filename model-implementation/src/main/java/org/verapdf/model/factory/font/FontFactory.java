package org.verapdf.model.factory.font;

import org.verapdf.model.impl.pb.pd.font.PBoxPDSimpleFont;
import org.verapdf.model.impl.pb.pd.font.PBoxPDType0Font;
import org.verapdf.model.impl.pb.pd.font.PBoxPDType3Font;
import org.verapdf.model.pdlayer.PDFont;

/**
 * @author Timur Kamalov
 */
public final class FontFactory {

    public static final String TYPE_0 = "Type0";
    public static final String TYPE_1 = "Type1";
    public static final String TYPE_1C = "Type1C";
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
		case TYPE_1C:
            return new PBoxPDSimpleFont(pdfboxFont);
        case TYPE_3:
            return new PBoxPDType3Font(pdfboxFont);
        case TRUE_TYPE:
            return new PBoxPDSimpleFont(pdfboxFont);
        default:
            return null;
        }
    }

}
