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
public class PBCosIndirectFactory implements PBCosFactory<CosIndirect, COSBase> {
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
    public CosIndirect generateCosObject(List<CosObject> parents, COSBase pdfBoxObject) {
        for (CosObject object : parents)
            if (((PBCosObject)object).compareTo(pdfBoxObject))
                return (CosIndirect) object;

        CosIndirect indirect = generateCosObject(pdfBoxObject);
        parents.add(indirect);
        return indirect;
    }
}
