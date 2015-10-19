package org.verapdf.model.tools;

import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSObject;
import org.apache.pdfbox.pdmodel.common.COSObjectable;
import org.apache.pdfbox.pdmodel.font.PDFontLike;

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
     * Generate ID for font glyph. Current method generate a string of the
     * form 'hashcode fontName glyphCode', where 'hashcode' is hashcode of
     * font dictionary, 'fontName' is String and 'glyphCode' is number.
     *
     * @param hashcode  hashcode of font dictionary
     * @param fontName  name of font
     * @param glyphCode code of glyph
     * @return string representation of ID
     */
    public static String generateID(int hashcode, String fontName, int glyphCode) {
        return String.valueOf(hashcode) + ' ' + fontName + ' ' + glyphCode;
    }

    /**
     * Generate ID for font. Current method generate a string of the form
     * 'hashcode fontName', where 'hashcode' is hashcode of font dictionary
     * and 'fontName' is String
     *
     * @param font target font for generating ID
     * @return string representation of ID
     */
    public static String generateID(PDFontLike font) {
        int hashcode = font instanceof COSObjectable ?
                ((COSObjectable) font).getCOSObject().hashCode() : -1;
        return String.valueOf(hashcode) + ' ' + font.getName();
    }
}
