package org.verapdf.model.impl.pb.cos;

import org.apache.pdfbox.cos.COSString;
import org.verapdf.model.coslayer.CosString;

/**
 * Current class is representation of CosString interface of abstract model.
 * This class is analogue of COSString in pdfbox.
 *
 * @author Evgeniy Muravitskiy
 */
public class PBCosString extends PBCosObject implements CosString {

    /** Type name for PBCosString */
    public static final String COS_STRING_TYPE = "CosString";
    private final String value;
    private final boolean isHex;
    private final boolean containsOnlyHex;
    private final long hexCount;

    /**
     * Default constructor
     * @param cosString pdfbox COSString
     */
    public PBCosString(COSString cosString) {
        super(cosString, COS_STRING_TYPE);
        this.value = cosString.getASCII();
        this.isHex = cosString.isHex();
        this.containsOnlyHex = cosString.isContainsOnlyHex();
        this.hexCount = cosString.getHexCount();
    }

    /**
     * Get Unicode string value stored in the PDF object
     */
    @Override
    public String getvalue() {
        return this.value;
    }

    /**
     * true if the string is stored in Hex format
     */
    @Override
    public Boolean getisHex() {
        return this.isHex;
    }

    /**
     * true if all symbols below range 0-9,a-f,A-F
     */
    @Override
    public Boolean getcontainsOnlyHex() {
        return this.containsOnlyHex;
    }

    /**
     * contains original hexa string length
     */
    @Override
    public Long gethexCount() {
        return this.hexCount;
    }
}
