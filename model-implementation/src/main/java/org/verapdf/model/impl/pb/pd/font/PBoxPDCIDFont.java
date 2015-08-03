package org.verapdf.model.impl.pb.pd.font;

import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.cos.COSStream;
import org.apache.pdfbox.pdmodel.font.PDCIDFontType2;
import org.apache.pdfbox.pdmodel.font.PDFontLike;
import org.verapdf.model.pdlayer.PDCIDFont;

/**
 * @author Timur Kamalov
 */
public class PBoxPDCIDFont extends PBoxPDFont implements PDCIDFont {

    public static final String CID_FONT_TYPE = "PDCIDFont";
    public static final String IDENTITY = "Identity";
    public static final String CUSTOM = "Custom";

    public PBoxPDCIDFont(PDFontLike font) {
        super(font);
        setType(CID_FONT_TYPE);
    }

    @Override
    public String getCIDToGIDMap() {
        if (pdFontLike instanceof PDCIDFontType2) {
            COSBase map = ((PDCIDFontType2) pdFontLike).getCOSObject()
                    .getDictionaryObject(COSName.CID_TO_GID_MAP);
            if (map instanceof COSStream) {
                return CUSTOM;
            }
            if (map instanceof COSName
                    && ((COSName) map).getName().equals(IDENTITY)) {
                return IDENTITY;
            }
        }
        return null;
    }
}
