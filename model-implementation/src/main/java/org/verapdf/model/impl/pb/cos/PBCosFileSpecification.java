package org.verapdf.model.impl.pb.cos;

import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSName;
import org.verapdf.model.coslayer.CosFileSpecification;

/**
 * Represent a specific type of Dictionary - File Specification Dictionary.
 *
 * @author Evgeniy Muravitskiy
 */
public class PBCosFileSpecification extends PBCosDict implements
        CosFileSpecification {

    /** Type name for PBCosFileSpecification */
    public static final String COS_FILE_SPECIFICATION_TYPE = "CosFileSpecification";

    private final String ef;

    public PBCosFileSpecification(COSDictionary dictionary) {
        super(dictionary, COS_FILE_SPECIFICATION_TYPE);
        this.ef = dictionary.getItem(COSName.EF) == null ? null : dictionary
                .getItem(COSName.EF).toString();
    }

    /**
     * string representation of the EF dictionary, or null if EF key is not
     * present in the file specification dictionary
     */
    @Override
    public String getEF() {
        return this.ef;
    }

}
