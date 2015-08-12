package org.verapdf.model.tools;

import org.apache.pdfbox.pdmodel.font.*;
import org.apache.pdfbox.preflight.font.container.*;

/**
 * @author Timur Kamalov
 */
public class FontHelper {

	public static FontContainer getFontContainer(PDFont font) {
		if (font instanceof PDTrueTypeFont) {
			return new TrueTypeContainer((PDTrueTypeFont) font);
		} else if (font instanceof PDType1Font) {
			return new Type1Container((PDType1Font) font);
		} else if (font instanceof PDType3Font) {
			return new Type3Container((PDType3Font) font);
		} else if (font instanceof PDType0Font) {
			Type0Container container = new Type0Container(font);
			org.apache.pdfbox.pdmodel.font.PDCIDFont pdcidFont = ((PDType0Font) font).getDescendantFont();
			if (pdcidFont instanceof PDCIDFontType0) {
				container.setDelegateFontContainer(new CIDType0Container((PDCIDFontType0) pdcidFont));
			} else if (pdcidFont instanceof PDCIDFontType2) {
				container.setDelegateFontContainer(new CIDType2Container((PDCIDFontType2) pdcidFont));
			}
			return container;
		}
		return null;
	}

}
