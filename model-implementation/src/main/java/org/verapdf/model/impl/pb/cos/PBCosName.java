package org.verapdf.model.impl.pb.cos;

import org.apache.pdfbox.cos.COSName;
import org.verapdf.model.coslayer.CosName;

/**
 * Current class is representation of CosName interface of abstract model. This
 * class is analogue of COSName in pdfbox.
 *
 * @author Evgeniy Muravitskiy
 */
public class PBCosName extends PBCosObject implements CosName {

    /** Type name for PBCosName */
    public static final String COS_NAME_TYPE = "CosName";

    private final String value;
    private final long originalLength;

    /**
     * Default constructor
     * @param cosName pdfbox COSName
     */
    public PBCosName(COSName cosName) {
        this(cosName, COS_NAME_TYPE);
    }

    /**
     * Constructor for child classes
     * @param cosName pdfbox COSName
     * @param type child class type
     */
    public PBCosName(COSName cosName, final String type) {
        super(cosName, type);
        this.value = cosName.getName();
        this.originalLength = cosName.getOriginalLength().longValue();
    }

    /**
     * Get Unicode string representation of the Name object after applying
     * escape mechanism and converting to Unicode using Utf8 encoding
     */
    @Override
    public String getvalue() {
        return this.value;
    }

    /**
     * Get original length of the name before applying any escape mechanisms and
     * encodings
     */
    @Override
    public Long getorigLength() {
        return Long.valueOf(this.originalLength);
    }
}
