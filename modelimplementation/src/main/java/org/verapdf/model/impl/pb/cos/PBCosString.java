package org.verapdf.model.impl.pb.cos;

import org.apache.pdfbox.cos.COSString;
import org.verapdf.model.coslayer.CosString;

/**
 * Created by Evgeniy Muravitskiy on 4/28/15.
 * <p>
 *     Current class is representation of CosString interface of abstract model.
 *     This class is analogue of COSString in pdfbox.
 * </p>
 */
public class PBCosString extends PBCosObject implements CosString {

    public PBCosString(COSString value) {
        super(value);
        setType("CosString");
    }

	/**
	 * Get Unicode string value stored in the PDF object
	 */
    @Override
    public String getvalue() {
        return ((COSString) baseObject).getString();
    }

    /** true if the string is stored in Hex format
     */
    @Override
    public Boolean getisHex() {
        return Boolean.valueOf(((COSString) baseObject).isHex() != null);
    }

    /** true if all symbols below range 0-9,a-f,A-F
     */
    @Override
	public Boolean getisHexSymbols() {
        return Boolean.valueOf(((COSString) baseObject).getIsHexSymbols());
    }

    /**
     * contains original hexa string length
     */
    @Override
    public Long gethexCount() {
        return ((COSString) baseObject).getHexCount();
    }
}
