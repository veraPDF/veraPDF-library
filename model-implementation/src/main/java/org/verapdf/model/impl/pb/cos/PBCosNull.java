package org.verapdf.model.impl.pb.cos;

import org.apache.pdfbox.cos.COSNull;
import org.verapdf.model.coslayer.CosNull;

/**
 * Current class is representation of CosNull interface of abstract model. This
 * class is analogue of COSNull in pdfbox.
 *
 * @author Evgeniy Muravitskiy
 */
public final class PBCosNull extends PBCosObject implements CosNull {

    /** Type name for PBCosNull */
    public static final String COS_NULL_TYPE = "CosNull";

    /**
     * PDF null object
     */
    private static CosNull NULL;

    private PBCosNull(COSNull nil) {
        super(nil, COS_NULL_TYPE);
    }

    /**
     * Method to get instance of PBCosNull object
     * @return PBCosNull object
     */
	public static CosNull getInstance() {
        return NULL == null ? NULL = new PBCosNull(COSNull.NULL) : NULL;
	}
}