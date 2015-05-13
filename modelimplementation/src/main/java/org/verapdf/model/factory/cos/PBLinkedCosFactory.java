package org.verapdf.model.factory.cos;

import org.apache.pdfbox.cos.COSBase;
import org.verapdf.model.impl.pb.cos.PBCosObject;
import org.verapdf.model.coslayer.CosObject;

import java.util.List;

/**
 * Created by Evgeniy on 5/12/2015.
 */
abstract class PBLinkedCosFactory<T extends CosObject, S extends COSBase> implements PBCosFactory<T, S> {

    protected T checkInConvertedObjects(List<CosObject> convertedObjects, S pdfBoxObject) {
        for (CosObject object : convertedObjects) {
            if (((PBCosObject) object).compareTo(pdfBoxObject)) {
                return (T) object;
            }
        }
        return null;
    }
}