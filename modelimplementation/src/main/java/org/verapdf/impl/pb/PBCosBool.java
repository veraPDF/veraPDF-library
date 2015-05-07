package org.verapdf.impl.pb;

import org.apache.pdfbox.cos.COSBoolean;
import org.verapdf.model.coslayer.CosBool;

/**
 * Created by Evgeniy Muravitskiy on 4/28/15.
 * <p>
 *     Current class is representation of CosBool interface of abstract model.
 *     This class is analogue of COSBoolean in pdfbox.
 * </p>
 */
public class PBCosBool extends PBCosObject implements CosBool {

    public PBCosBool(COSBoolean bool) {
        super(bool);
    }

    /**Get value of this object
     */
    @Override
    public Boolean getvalue() {
        return ((COSBoolean)baseObject).getValue();
    }
}
