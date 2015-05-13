package org.verapdf.model.factory.cos;

import org.apache.pdfbox.cos.COSDictionary;
import org.verapdf.model.coslayer.CosDict;
import org.verapdf.model.impl.pb.cos.PBCosDict;
import org.verapdf.model.coslayer.CosObject;

import java.util.List;

/**
 * Created by Evgeniy Muravitskiy on 4/29/15.
 * <p>
 * Class for transforming COSDictionary of pdfbox to CosDict of abstract model.
 * </p>
 */
class PBCosDictFactory extends PBLinkedCosFactory<CosDict, COSDictionary> {

    /**
     * Method for transforming COSDictionary to corresponding CosDict
     */
    @Override
    public CosDict generateCosObject(COSDictionary pdfBoxObject) {
        return new PBCosDict(pdfBoxObject);
    }

    /**
     * Method for transforming COSDictionary to corresponding CosDict. Also takes into account already
     * exists objects.
     */
    @Override
    public CosDict generateCosObject(List<CosObject> convertedObjects, COSDictionary pdfBoxObject) {
        CosDict dictionary = checkInConvertedObjects(convertedObjects, pdfBoxObject);
        if (dictionary != null)
            return dictionary;

        dictionary = generateCosObject(pdfBoxObject);
        convertedObjects.add(dictionary);
        return dictionary;
    }
}
