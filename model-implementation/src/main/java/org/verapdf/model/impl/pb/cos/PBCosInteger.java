package org.verapdf.model.impl.pb.cos;

import org.apache.pdfbox.cos.COSInteger;
import org.verapdf.model.coslayer.CosInteger;

/**
 * Current class is representation of CosInteger interface of abstract model.
 * All methods described in CosNumber. This class is analogue of COSInteger in
 * pdfbox.
 *
 * @author Evgeniy Muravitskiy
 * @see PBCosNumber
 */
public class PBCosInteger extends PBCosNumber implements CosInteger {

    /** Type name for PBCosInteger */
    public static final String COS_INTEGER_TYPE = "CosInteger";

    /**
     * Default constructor
     * @param value pdfbox COSInteger
     */
    public PBCosInteger(COSInteger value) {
        super(value, COS_INTEGER_TYPE);
    }
}
