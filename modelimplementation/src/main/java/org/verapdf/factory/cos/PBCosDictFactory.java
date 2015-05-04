package org.verapdf.factory.cos;

import org.apache.pdfbox.cos.COSDictionary;
import org.verapdf.model.coslayer.CosDict;
import org.verapdf.impl.pb.PBCosDict;
import org.verapdf.model.coslayer.CosObject;

import java.util.List;

/**
 * Created by Evgeniy Muravitskiy on 4/29/15.
 * <p>
 * Class for transforming COSDictionary of pdfbox to CosDict of abstract model.
 * </p>
 */
public class PBCosDictFactory implements PBCosFactory<CosDict, COSDictionary> {

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
    public CosDict generateCosObject(List<CosObject> parents, COSDictionary pdfBoxObject) {
        for (CosObject object : parents)
            if (object.equals(pdfBoxObject))
                return (CosDict) object;

        CosDict dictionary = generateCosObject(pdfBoxObject);
        parents.add(dictionary);
        return dictionary;
    }
}
