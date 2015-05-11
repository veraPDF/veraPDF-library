package org.verapdf.factory.cos;

import org.apache.pdfbox.cos.COSDictionary;
import org.verapdf.impl.pb.PBCosTrailer;
import org.verapdf.model.coslayer.CosObject;
import org.verapdf.model.coslayer.CosTrailer;

import java.util.List;

/**
 * Created by Evgeniy Muravitskiy on 5/1/15.
 * <p>
 *     Class for transforming trailer of pdf document to CosTrailer of abstract model.
 *     In pdf box trailer is representing by COSDictionary and gets with specified method of COSDocument
 * </p>
 */
class PBCosTrailerFactory implements PBCosFactory<CosTrailer, COSDictionary> {

    /** Method for transforming COSDictionary to corresponding CosTrailer
     */
    @Override
    public CosTrailer generateCosObject(COSDictionary pdfBoxObject) {
        return new PBCosTrailer(pdfBoxObject);
    }

    /**
     * Method for transforming COSDictionary to corresponding CosTrailer. Also takes into account already
     * exists objects.
     */
    @Override
    public CosTrailer generateCosObject(List<CosObject> parents, COSDictionary pdfBoxObject) {
        for (CosObject object : parents)
            if (object.equals(pdfBoxObject))
                return (CosTrailer) object;

        CosTrailer trailer = generateCosObject(pdfBoxObject);
        parents.add(trailer);
        return trailer;
    }
}
