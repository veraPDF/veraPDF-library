package org.verapdf.model.impl.pb.pd.font;

import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.cos.COSStream;
import org.apache.pdfbox.pdmodel.common.PDStream;
import org.apache.pdfbox.pdmodel.font.PDCIDFontType2;
import org.apache.pdfbox.pdmodel.font.PDFontDescriptor;
import org.apache.pdfbox.pdmodel.font.PDFontLike;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.coslayer.CosStream;
import org.verapdf.model.impl.pb.cos.PBCosStream;
import org.verapdf.model.pdlayer.PDCIDFont;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Timur Kamalov
 */
public class PBoxPDCIDFont extends PBoxPDFont implements PDCIDFont {

    public static final String CID_FONT_TYPE = "PDCIDFont";

    public static final String CID_SET = "CIDSet";

    public static final String IDENTITY = "Identity";
    public static final String CUSTOM = "Custom";

    public PBoxPDCIDFont(PDFontLike font) {
        super(font, CID_FONT_TYPE);
    }

    @Override
    public List<? extends Object> getLinkedObjects(String link) {
        if (CID_SET.equals(link)) {
            return this.getCIDSet();
        }
        return super.getLinkedObjects(link);
    }

    private List<CosStream> getCIDSet() {
        List<CosStream> res = new ArrayList<>();
        PDFontDescriptor fontDescriptor = this.pdFontLike.getFontDescriptor();
        PDStream cidSet;
        if (fontDescriptor != null) {
            cidSet = fontDescriptor.getCIDSet();
            if (cidSet != null) {
                res.add(new PBCosStream(cidSet.getStream()));
            }
        }
        return res;
    }

    @Override
    public String getCIDToGIDMap() {
        if (this.pdFontLike instanceof PDCIDFontType2) {
            COSBase map = ((PDCIDFontType2) this.pdFontLike).getCOSObject()
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
