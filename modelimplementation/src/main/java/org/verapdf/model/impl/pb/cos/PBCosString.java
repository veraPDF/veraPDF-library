package org.verapdf.model.impl.pb.cos;

import org.apache.pdfbox.cos.COSString;
import org.verapdf.model.coslayer.CosString;

/**
 * Current class is representation of CosString interface of abstract model.
 * This class is analogue of COSString in pdfbox.
 *
 * @author Evgeniy Muravitskiy
 */
public class PBCosString extends PBCosObject implements CosString{

    private Boolean isHex;

    public PBCosString(COSString value) {
        super(value);
        setType("CosString");
        this.isHex = value.isHex() != null;
    }

    /** Get Unicode string value stored in the PDF object
     */
    @Override
    public String getvalue() {
        return ((COSString) baseObject).getString();
    }

    /** true if the string is stored in Hex format
     */
    @Override
    public Boolean getisHex() {
        return isHex;
    }

    /** Get original string value of the string before applying Hex decoding
     *  and any encodings (but after ignoring all white spaces)
     */
    @Override
    public String getorigValue() {
        return isHex ? ((COSString) baseObject).toHexString() : ((COSString) baseObject).getASCII();
    }
}
