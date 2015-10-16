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

	public static final CosBool TRUE = new PBCosBool(COSBoolean.TRUE);
	public static final CosBool FALSE = new PBCosBool(COSBoolean.FALSE);

    /** Type name for PBCosBool */
    public static final String COS_BOOLEAN_TYPE = "CosBool";
    private boolean value;

    private PBCosBool(COSBoolean cosBoolean) {
        super(cosBoolean, COS_BOOLEAN_TYPE);
        this.value = cosBoolean.getValue();
    }

    /**
     * Get value of this object
     */
    @Override
    public Boolean getvalue() {
        return this.value;
    }

    /**
     * This method will create CosBool object instance from pdfbox COSBoolean
     * @param bool pdfbox COSBoolean
     * @return instance of CosBool
     */
	public static CosBool valueOf(COSBoolean bool) {
		return bool.getValue() ? TRUE : FALSE;
	}
}
