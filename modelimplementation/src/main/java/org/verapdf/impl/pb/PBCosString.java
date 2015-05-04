package org.verapdf.impl.pb;

import org.apache.pdfbox.cos.COSString;
import org.apache.pdfbox.util.Charsets;
import org.verapdf.model.coslayer.CosString;

/**
 * Created by Evgeniy Muravitskiy on 4/28/15.
 * <p>
 *     Current class is representation of CosString interface of abstract model.
 *     This class is analogue of COSString in pdfbox.
 * </p>
 */
public class PBCosString extends PBCosObject implements CosString{

    private boolean isHex;

    public PBCosString(COSString value, boolean isHex) {
        super(value);
        this.isHex = isHex;
    }

    /** Get Unicode string value stored in the PDF object
     */
    @Override
    public String getvalue() {
        return ((COSString)baseObject).getString();
    }

    /** true if the string is stored in Hex format
     */
    //TODO : rewrite COSString for features below. now work not correctly
    @Override
    public Boolean getisHex() {
        return isHex;
    }

    /** Get original string value of the string before applying Hex decoding
     *  and any encodings (but after ignoring all white spaces)
     */
    @Override
    public String getorigValue() {
        System.err.println("Original value of string is not correct. Need to update classes of pdfbox.");
        return new String(((COSString)baseObject).getBytes());
    }
}
