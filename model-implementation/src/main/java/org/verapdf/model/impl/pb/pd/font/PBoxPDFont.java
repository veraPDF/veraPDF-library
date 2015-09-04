package org.verapdf.model.impl.pb.pd.font;

import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.common.PDStream;
import org.apache.pdfbox.pdmodel.font.PDCIDFont;
import org.apache.pdfbox.pdmodel.font.PDFontDescriptor;
import org.apache.pdfbox.pdmodel.font.PDFontLike;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.factory.font.FontFactory;
import org.verapdf.model.impl.pb.external.PBoxFontProgram;
import org.verapdf.model.impl.pb.pd.PBoxPDResources;
import org.verapdf.model.pdlayer.PDFont;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Timur Kamalov
 */
public abstract class PBoxPDFont extends PBoxPDResources implements PDFont {

    public static final String FONT_FILE = "fontFile";

	protected PBoxPDFont(PDFontLike font, final String type) {
		super(font, type);
	}

	public String getType() {
		String subtype = null;
		if (this.pdFontLike instanceof org.apache.pdfbox.pdmodel.font.PDFont) {
			subtype = ((org.apache.pdfbox.pdmodel.font.PDFont) this.pdFontLike)
					.getType();
		} else if (this.pdFontLike instanceof PDCIDFont) {
			subtype = ((PDCIDFont) this.pdFontLike).getCOSObject().getNameAsString(
					COSName.TYPE);
		}
		return subtype;
	}

	@Override
	public String getSubtype() {
		String subtype = null;
		if (this.pdFontLike instanceof org.apache.pdfbox.pdmodel.font.PDFont) {
			subtype = ((org.apache.pdfbox.pdmodel.font.PDFont) this.pdFontLike)
					.getSubType();
		} else if (this.pdFontLike instanceof PDCIDFont) {
			subtype = ((PDCIDFont) this.pdFontLike).getCOSObject().getNameAsString(
					COSName.SUBTYPE);
		}
		return subtype;
	}

	@Override
	public String getBaseFont() {
		return this.pdFontLike.getName();
	}

	@Override
	public Boolean getisSymbolic() {
		PDFontDescriptor fontDescriptor = this.pdFontLike.getFontDescriptor();
		return Boolean.valueOf(fontDescriptor.isSymbolic());
	}

	@Override
    public List<? extends Object> getLinkedObjects(String link) {
        if (FONT_FILE.equals(link)) {
            return this.getFontFile();
        }
        return super.getLinkedObjects(link);
    }

    private List<PBoxFontProgram> getFontFile() {
        List<PBoxFontProgram> list = new ArrayList<>(MAX_NUMBER_OF_ELEMENTS);
        if (!getSubtype().equals(FontFactory.TYPE_3)
                && (this.pdFontLike.isEmbedded())) {
            PDFontDescriptor fontDescriptor = pdFontLike.getFontDescriptor();
            PDStream fontFile;
            if (getSubtype().equals(FontFactory.TYPE_1)) {
                fontFile = fontDescriptor.getFontFile();
            } else if (getSubtype().equals(FontFactory.TRUE_TYPE)) {
                fontFile = fontDescriptor.getFontFile2();
            } else {
                fontFile = fontDescriptor.getFontFile3();
            }
            if (fontFile != null) {
                list.add(new PBoxFontProgram(fontFile));
            }
        }
        return list;
    }

}
