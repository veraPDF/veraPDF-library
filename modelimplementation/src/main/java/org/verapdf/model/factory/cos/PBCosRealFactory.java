package org.verapdf.model.factory.cos;

import org.apache.pdfbox.cos.COSFloat;
import org.verapdf.model.coslayer.CosObject;
import org.verapdf.model.coslayer.CosReal;
import org.verapdf.model.impl.pb.cos.PBCosReal;

import java.util.List;

/**
 * Created by Evgeniy Muravitskiy on 4/28/15.
 * <p>
 *     Class for transforming COSFloat of pdfbox to CosReal of abstract model.
 * </p>
 */
class PBCosRealFactory implements PBCosFactory<CosReal, COSFloat> {

    /** Method for transforming COSFloat to corresponding CosReal
     */
    @Override
    public CosReal generateCosObject(COSFloat pdfBoxObject) {
        return new PBCosReal(pdfBoxObject);
    }

    /** Method for transforming COSFloat to corresponding CosReal. Not takes into account already
     * exists objects.
     */
    @Override
    public CosReal generateCosObject(List<CosObject> convertedObjects, COSFloat pdfBoxObject) {
        return generateCosObject(pdfBoxObject);
    }
}
