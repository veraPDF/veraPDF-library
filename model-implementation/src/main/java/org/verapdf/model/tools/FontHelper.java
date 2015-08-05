package org.verapdf.model.tools;

import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDTrueTypeFont;
import org.apache.pdfbox.preflight.font.container.FontContainer;
import org.apache.pdfbox.preflight.font.container.TrueTypeContainer;

/**
 * @author Timur Kamalov
 */
public class FontHelper {

	public static FontContainer getFontContainer(PDFont font) {
		if (font instanceof PDTrueTypeFont) {
			return new TrueTypeContainer((PDTrueTypeFont) font);
		}
		return null;
	}

}
