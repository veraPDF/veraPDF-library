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
class PBCosArrayFactory implements PBCosFactory<org.verapdf.model.coslayer.CosArray, COSArray> {

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
    public CosArray generateCosObject(List<CosObject> parents, COSArray pdfBoxObject) {
        for (CosObject object : parents)
            if (((PBCosObject)object).compareTo(pdfBoxObject))
                return (CosArray) object;

        CosArray array = generateCosObject(pdfBoxObject);
        parents.add(array);
        return array;
    }
}
