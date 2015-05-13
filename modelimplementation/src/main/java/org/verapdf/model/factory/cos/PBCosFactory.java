package org.verapdf.model.factory.cos;

import org.apache.pdfbox.cos.COSBase;
import org.verapdf.model.coslayer.CosObject;

import java.util.List;

/**
 * Created by Evgeniy Muravitskiy on 4/27/15.
 * <p>
 *     Interface which define transforming specific pdfbox object`s to specific object of
 *     abstract model.
 * </p>
 */
interface PBCosFactory<T extends CosObject, S extends COSBase> {

    /** Interface of method for transforming specific S type to corresponding T type
     */
    T generateCosObject(S pdfBoxObject);

    /** Interface of method for transforming specific S type to corresponding T type with convertedObjects check
     */
    T generateCosObject(List<CosObject> convertedObjects, S pdfBoxObject);
}