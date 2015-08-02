package org.verapdf.model.impl.pb.cos;

import org.apache.pdfbox.cos.COSBoolean;
import org.verapdf.model.coslayer.CosBool;

/**
 * Current class is representation of CosBool interface of abstract model. This
 * class is analogue of COSBoolean in pdfbox.
 *
 * @author Evgeniy Muravitskiy
 */
public class PBCosBool extends PBCosObject implements CosBool {

    /** Type name for PBCosBool */
    public static final String COS_BOOLTYPE = "CosBool";
    private Boolean value;

    public PBCosBool(COSBoolean cosBoolean) {
        super(cosBoolean, COS_BOOLTYPE);
        this.value = cosBoolean.getValueAsObject();
    }

    /**
     * Get value of this object
     */
    @Override
    public Boolean getvalue() {
        return this.value;
    }
}
