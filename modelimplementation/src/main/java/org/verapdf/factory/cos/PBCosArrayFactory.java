package org.verapdf.factory.cos;

import org.apache.pdfbox.cos.COSArray;
import org.verapdf.impl.pb.PBCosArray;
import org.verapdf.impl.pb.PBCosObject;
import org.verapdf.model.coslayer.CosArray;
import org.verapdf.model.coslayer.CosObject;

import java.util.List;

/**
 * Created by Evgeniy Muravitskiy on 4/27/15.
 * <p>
 * Class for transforming COSArray of pdfbox to CosArray of abstract model.
 * </p>
 */
class PBCosArrayFactory extends PBCosFactory<org.verapdf.model.coslayer.CosArray, COSArray> {

    /**
     * Method for transforming COSArray to corresponding CosArray
     */
    @Override
    public CosArray generateCosObject(COSArray pdfBoxObject) {
        return new PBCosArray(pdfBoxObject);
    }

    /**
     * Method for transforming COSArray to corresponding CosArray. Also takes into account already
     * exists objects.
     */
    @Override
    public CosArray generateCosObject(List<CosObject> convertedObjects, COSArray pdfBoxObject) {
        CosArray array = checkInConvertedObjects(convertedObjects, pdfBoxObject);
        if (array != null)
            return array;

        array = generateCosObject(pdfBoxObject);
        convertedObjects.add(array);
        return array;
    }
}
