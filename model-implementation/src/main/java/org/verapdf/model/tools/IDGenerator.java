package org.verapdf.model.tools;

import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSObject;

/**
 * Created by Evgeniy Muravitskiy on 4/27/15.
 * <p>
 * This class specified for creating ID`s for every object from model.
 */
public final class IDGenerator {

    private IDGenerator() {
        // Disable default constructor
    }

    /**
     * Generate ID for pdf box object. Current method generate a string of the
     * form 'N M' for {@link org.apache.pdfbox.cos.COSObject}, where 'N' and 'M'
     * are numbers, and <code>null</code> for other pdf box objects
     * 
     * @param pdfBoxObject
     *            object of pdf box library
     * @return string representation of ID
     */
    public static String generateID(COSBase pdfBoxObject) {
        if (pdfBoxObject instanceof COSObject) {
            return ((COSObject) pdfBoxObject).getObjectNumber() + " "
                    + ((COSObject) pdfBoxObject).getGenerationNumber();
        }
        return null;
    }

	/**
	 * Generate ID for font glyph. Curren method generate a string of the
	 * form 'fontName glyphCode', where 'fontName' is String and
	 * 'glyphCode' is number.
	 *
	 * @param fontName  name of font
	 * @param glyphCode code of glyph
	 * @return string representation of ID
	 */
	public static String generateID(String fontName, int glyphCode) {
		StringBuilder builder = new StringBuilder();
		builder.append(fontName).append(' ').append(glyphCode);
		return builder.toString();
	}

}
