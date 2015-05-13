package org.verapdf.model.factory.cos;

import org.apache.pdfbox.cos.COSNull;
import org.verapdf.model.coslayer.CosNull;
import org.verapdf.model.impl.pb.cos.PBCosNull;
import org.verapdf.model.coslayer.CosObject;

import java.util.List;

/**
 * Created by Evgeniy Muravitskiy on 4/28/15.
 * <p>
 *     Class for transforming COSNull of pdfbox to CosNull of abstract model.
 * </p>
 */
class PBCosNullFactory implements PBCosFactory<CosNull, COSNull> {

    /** Method for transforming COSNull to corresponding CosNull
     */
    @Override
    public CosNull generateCosObject(COSNull pdfBoxObject) {
        return PBCosNull.NULL;
    }

    /** Method for transforming COSNull to corresponding CosNull. Not takes into account already
     * exists objects.
     */
    @Override
    public CosNull generateCosObject(List<CosObject> convertedObjects, COSNull pdfBoxObject) {
        return generateCosObject(pdfBoxObject);
    }
}
