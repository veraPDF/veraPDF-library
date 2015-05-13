package org.verapdf.model.factory.cos;

import org.apache.pdfbox.cos.COSBase;
import org.verapdf.model.coslayer.CosObject;
import org.verapdf.model.impl.pb.cos.PBCosObject;

import java.util.List;

/**
 * Created by Evgeniy Muravitskiy on 4/27/15.
 * <p>
 *     Class for transforming COSBase of pdfbox to CosObject of abstract model.
 *     This factory describe case when object of pdfbox model is empty.
 * </p>
 */
class PBCosObjectFactory extends PBLinkedCosFactory<CosObject, COSBase> {

    /** Method for transforming COSBase to corresponding CosObject
     */
    @Override
    public CosObject generateCosObject(COSBase pdfBoxObject) {
        return new PBCosObject(pdfBoxObject);
    }

    /**
     * Method for transforming COSBase to corresponding CosObject. Also takes into account already
     * exists objects.
     */
    @Override
    public CosObject generateCosObject(List<CosObject> convertedObjects, COSBase pdfBoxObject) {
        CosObject object = checkInConvertedObjects(convertedObjects, pdfBoxObject);
        if (object != null)
            return object;

        object = generateCosObject(pdfBoxObject);
        convertedObjects.add(object);
        return object;
    }
}
