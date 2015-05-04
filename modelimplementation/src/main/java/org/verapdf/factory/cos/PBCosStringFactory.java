package org.verapdf.factory.cos;

import org.apache.pdfbox.cos.COSString;
import org.verapdf.model.coslayer.CosObject;
import org.verapdf.model.coslayer.CosString;
import org.verapdf.impl.pb.PBCosString;

import java.util.List;

/**
 * Created by Evgeniy Muravitskiy on 4/28/15.
 * <p>
 *     Class for transforming COSString of pdfbox to CosString of abstract model.
 * </p>
 */
public class PBCosStringFactory implements PBCosFactory<CosString, COSString> {

    /** Method for transforming COSString to corresponding CosString
     */
    @Override
    public CosString generateCosObject(COSString pdfBoxObject) {
        return new PBCosString(pdfBoxObject, pdfBoxObject.getForceHexForm());
    }

    @Override
    public CosString generateCosObject(List<CosObject> parents, COSString pdfBoxObject) {
        return generateCosObject(pdfBoxObject);
    }
}
