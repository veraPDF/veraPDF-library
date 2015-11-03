package org.verapdf.model.factory.font;

import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.font.PDTrueTypeFont;
import org.apache.pdfbox.pdmodel.font.PDType1CFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.PDType3Font;
import org.verapdf.model.impl.pb.pd.font.PBoxPDTrueTypeFont;
import org.verapdf.model.impl.pb.pd.font.PBoxPDType0Font;
import org.verapdf.model.impl.pb.pd.font.PBoxPDType1Font;
import org.verapdf.model.impl.pb.pd.font.PBoxPDType3Font;
import org.verapdf.model.pdlayer.PDFont;
import org.verapdf.model.tools.PDExtendedResources;

/**
 * Font factory for transforming Apache PDFBox
 * font representation to VeraPDF fonts
 *
 * @author Timur Kamalov
 */
public final class FontFactory {

	/** Type name for {@code Type0} font */
    public static final String TYPE_0 = "Type0";
	/** Type name for {@code Type1} font */
    public static final String TYPE_1 = "Type1";
	/** Type name for {@code MMType1} font */
    public static final String MM_TYPE_1 = "MMType1";
	/** Type name for {@code Type3} font */
    public static final String TYPE_3 = "Type3";
	/** Type name for {@code TrueType} font */
    public static final String TRUE_TYPE = "TrueType";
	/** Type name for {@code CIDFontType2} font */
    public static final String CID_FONT_TYPE_2 = "CIDFontType2";


    private FontFactory() {
        // Disable default constructor
    }

	/**
	 * Transform Apache PDFBox font representation to
	 * VeraPDF font representation
	 *
	 * @param pdfboxFont Apache PDFBox font representation
	 * @return VeraPDF font representation
	 */
	public static PDFont parseFont(
			org.apache.pdfbox.pdmodel.font.PDFont pdfboxFont) {
		return parseFont(pdfboxFont, new PDResources());
	}

	public static PDFont parseFont(
			org.apache.pdfbox.pdmodel.font.PDFont pdfboxFont,
			PDResources resources) {
		if (pdfboxFont == null) {
			return null;
		}
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
				return new PBoxPDType3Font(pdfboxFont,
						getResources(pdfboxFont, resources));
			case TRUE_TYPE:
				return new PBoxPDTrueTypeFont((PDTrueTypeFont) pdfboxFont);
			default:
				return null;
		}
	}

	private static PDResources getResources(
			org.apache.pdfbox.pdmodel.font.PDFont pdfboxFont,
			PDResources resources) {
		PDResources fontResources = ((PDType3Font) pdfboxFont).getResources();
		if (resources instanceof PDExtendedResources) {
			PDExtendedResources extRes = (PDExtendedResources) resources;
			return PDExtendedResources.getInstance(extRes.getPageResources(),
					fontResources);
		} else if (resources != null) {
			return PDExtendedResources.getInstance(resources, fontResources);
		} else {
			return fontResources;
		}
	}

}
