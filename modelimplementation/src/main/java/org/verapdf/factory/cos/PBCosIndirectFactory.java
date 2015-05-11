package org.verapdf.factory.cos;

import org.apache.pdfbox.cos.COSBase;
import org.verapdf.impl.pb.PBCosObject;
import org.verapdf.model.coslayer.CosIndirect;
import org.verapdf.impl.pb.PBCosIndirect;
import org.verapdf.model.coslayer.CosObject;

import java.util.List;

/**
 * Created by Evgeniy Muravitskiy on 4/29/15.
 * <p>
 *     Class for transforming COSIndirect of pdfbox to CosInteger of abstract model.
 * </p>
 */
class PBCosIndirectFactory extends PBCosFactory<CosIndirect, COSBase> {
    // TODO : check spacings
    /** Method for transforming COSObject to corresponding CosIndirect
     */
    @Override
    public CosIndirect generateCosObject(COSBase pdfBoxObject) {
        return new PBCosIndirect(pdfBoxObject, true);
    }

    /** Method for transforming COSObject to corresponding CosIndirect. Also takes into account already
     * exists objects.
     */
    @Override
    public CosIndirect generateCosObject(List<CosObject> convertedObjects, COSBase pdfBoxObject) {
        CosIndirect indirect = checkInConvertedObjects(convertedObjects, pdfBoxObject);
        if (indirect != null)
            return indirect;

        indirect = generateCosObject(pdfBoxObject);
        convertedObjects.add(indirect);
        return indirect;
    }
}
