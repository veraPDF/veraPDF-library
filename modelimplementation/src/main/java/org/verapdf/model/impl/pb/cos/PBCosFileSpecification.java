package org.verapdf.model.impl.pb.cos;

import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSDictionary;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBCosFileSpecification extends PBCosObject implements CosFileSpecification {

    public PBCosFileSpecification(COSDictionary pdfBoxObject) {
        super(pdfBoxObject);
        setType("CosFileSpecification");
    }

    /** string representation of the EF dictionary, or null if EF key is not present in the file specification dictionary
     */
    public String getEF() {
        final COSBase ef = ((COSDictionary) baseObject).getItem("EF");
        return ef != null ? ef.toString() : null;
    }
}
