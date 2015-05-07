package org.verapdf.factory.cos;

import org.apache.pdfbox.cos.COSName;
import org.verapdf.model.coslayer.CosName;
import org.verapdf.impl.pb.PBCosName;
import org.verapdf.model.coslayer.CosObject;

import java.util.List;

/**
 * Created by Evgeniy Muravitskiy on 4/27/15.
 * <p>
 *     Class for transforming COSName of pdfbox to CosName of abstract model.
 * </p>
 */
public class PBCosNameFactory implements PBCosFactory<CosName, COSName> {

    /** Method for transforming COSName to corresponding CosName
     */
    @Override
    public CosName generateCosObject(COSName pdfBoxObject) {
        return new PBCosName(pdfBoxObject);
    }

    /** Method for transforming COSName to corresponding CosName. Not takes into account already
     * exists objects.
     */
    @Override
    public CosName generateCosObject(List<CosObject> parents, COSName pdfBoxObject) {
        return generateCosObject(pdfBoxObject);
    }
}
