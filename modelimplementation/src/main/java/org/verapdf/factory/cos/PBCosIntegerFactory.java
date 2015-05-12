package org.verapdf.factory.cos;

import org.apache.pdfbox.cos.COSInteger;
import org.verapdf.model.coslayer.CosInteger;
import org.verapdf.impl.pb.PBCosInteger;
import org.verapdf.model.coslayer.CosObject;

import java.util.List;

/**
 * Created by Evgeniy Muravitskiy on 4/27/15.
 * <p>
 *     Class for transforming COSInteger of pdfbox to CosInteger of abstract model.
 * </p>
 */
class PBCosIntegerFactory implements PBCosFactory<CosInteger, COSInteger> {

    /** Method for transforming COSInteger to corresponding CosInteger
     */
    @Override
    public CosInteger generateCosObject(COSInteger pdfBoxObject) {
        return new PBCosInteger(pdfBoxObject);
    }

    /** Method for transforming COSInteger to corresponding CosInteger. Not takes into account already
     * exists objects.
     */
    @Override
    public CosInteger generateCosObject(List<CosObject> convertedObjects, COSInteger pdfBoxObject) {
        return generateCosObject(pdfBoxObject);
    }
}
