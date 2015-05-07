package org.verapdf.factory.cos;

import org.apache.pdfbox.cos.COSBoolean;
import org.verapdf.model.coslayer.CosBool;
import org.verapdf.impl.pb.PBCosBool;
import org.verapdf.model.coslayer.CosObject;

import java.util.List;

/**
 * Created by Evgeniy Muravitskiy on 4/28/15.
 * <p>
 *     Class for transforming COSBoolean of pdfbox to CosBool of abstract model.
 * </p>
 */
class PBCosBoolFactory implements PBCosFactory<CosBool, COSBoolean> {

    /** Method for transforming COSBoolean to corresponding CosBool
     */
    @Override
    public CosBool generateCosObject(COSBoolean pdfBoxObject) {
        return new PBCosBool(pdfBoxObject);
    }

    /** Method for transforming COSBoolean to corresponding CosBool. Not takes into account already
     * exists objects.
     */
    @Override
    public CosBool generateCosObject(List<CosObject> parents, COSBoolean pdfBoxObject) {
        return generateCosObject(pdfBoxObject);
    }
}
