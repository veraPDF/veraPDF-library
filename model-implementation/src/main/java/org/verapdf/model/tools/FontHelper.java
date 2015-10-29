package org.verapdf.model.tools;

import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.font.*;
import org.apache.pdfbox.preflight.font.container.*;
import org.verapdf.model.factory.font.FontFactory;

/**
 * Class for transforming Apache PDFBox font to
 * Apache Preflight font container
 *
 * @author Timur Kamalov
 */
public class FontHelper {

	/** CID font type 0 value of Subtype entry for type 0 font */
	public static final String CID_FONT_TYPE_0 = "CIDFontType0";
	/** CID font type 1 value of Subtype entry for type 0 font */
	public static final String CID_FONT_TYPE_2 = "CIDFontType2";

	/**
	 * Transform Apache PDFBox font to Apache Preflight
	 * font container representation
	 *
	 * @param font Apache PDFBox font
	 * @return Apache Preflight font container
	 */
	public static FontContainer getFontContainer(PDFont font) {
		if (font == null) {
			return null;
		}
		switch (font.getSubType()) {
			case FontFactory.TYPE_1:
			case FontFactory.MM_TYPE_1:
				return new Type1Container((PDSimpleFont) font);
			case FontFactory.TRUE_TYPE:
				return new TrueTypeContainer((PDTrueTypeFont) font);
			case FontFactory.TYPE_3:
				return new Type3Container((PDType3Font) font);
			case FontFactory.TYPE_0: {
				Type0Container container = new Type0Container(font);
				PDCIDFont pdcidFont = ((PDType0Font) font)
						.getDescendantFont();
				String cidType = pdcidFont.getCOSObject()
						.getNameAsString(COSName.SUBTYPE);
				if (CID_FONT_TYPE_0.equals(cidType)) {
					CIDType0Container type0Container =
							new CIDType0Container((PDCIDFontType0) pdcidFont);
					container.setDelegateFontContainer(type0Container);
				} else if (CID_FONT_TYPE_2.equals(cidType)) {
					CIDType2Container type2Container =
							new CIDType2Container((PDCIDFontType2) pdcidFont);
					container.setDelegateFontContainer(type2Container);
				}
				return container;
			}
			default:
				return null;
		}
	}

}
