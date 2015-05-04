package org.verapdf.impl.pb;

import org.apache.pdfbox.cos.COSFloat;
import org.verapdf.model.coslayer.CosReal;

/**
 * Created by Evgeniy Muravitskiy on 4/28/15.
 * <p>
 *     Current class is representation of CosReal interface of abstract model.
 *     All methods described in CosNumber. This class is analogue of COSFloat in pdfbox.
 * </p>
 *
 * @see org.verapdf.impl.pb.PBCosNumber
 */
public class PBCosReal extends PBCosNumber implements CosReal {

    public PBCosReal(COSFloat value) {
        super(value);
    }
}
