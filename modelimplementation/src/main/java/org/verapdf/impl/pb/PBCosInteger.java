package org.verapdf.impl.pb;

import org.apache.pdfbox.cos.COSInteger;
import org.verapdf.model.coslayer.CosInteger;

/**
 * Created by Evgeniy Muravitskiy on 4/27/15.
 * <p>
 *     Current class is representation of CosInteger interface of abstract model.
 *     All methods described in CosNumber. This class is analogue of COSInteger in pdfbox.
 * </p>
 *
 * @see org.verapdf.impl.pb.PBCosNumber
 */
public class PBCosInteger extends PBCosNumber implements CosInteger {

    public PBCosInteger(COSInteger value) {
        super(value);
        setType("CosInteger");
    }
}
