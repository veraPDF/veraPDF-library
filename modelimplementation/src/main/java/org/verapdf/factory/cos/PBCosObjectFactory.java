package org.verapdf.factory.cos;

import org.apache.pdfbox.cos.COSBase;
import org.verapdf.model.coslayer.CosObject;
import org.verapdf.impl.pb.PBCosObject;

import java.util.List;

/**
 * Created by Evgeniy Muravitskiy on 4/27/15.
 * <p>
 *     Class for transforming COSBase of pdfbox to CosObject of abstract model.
 *     This factory describe case when object of pdfbox model is empty.
 * </p>
 */
class PBCosObjectFactory implements PBCosFactory<CosObject, COSBase> {

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
    public CosObject generateCosObject(List<CosObject> parents, COSBase pdfBoxObject) {
        for (CosObject object : parents)
            if (((PBCosObject)object).compareTo(pdfBoxObject))
                return object;

        CosObject object = generateCosObject(pdfBoxObject);
        parents.add(object);
        return object;
    }
}
