package org.verapdf.model.impl.pb.cos;

import org.apache.log4j.Logger;
import org.apache.pdfbox.cos.COSName;
import org.verapdf.model.coslayer.CosName;

/**
 * Created by Evgeniy Muravitskiy on 4/27/15.
 * <p>
 *     Current class is representation of CosName interface of abstract model.
 *     This class is analogue of COSName in pdfbox.
 * </p>
 */
public class PBCosName extends PBCosObject implements CosName {

    public PBCosName(COSName value) {
        super(value);
        setType("CosName");
    }

    /** Get Unicode string representation of the Name object after applying escape
     *  mechanism and converting to Unicode using Utf8 encoding
     */
    @Override
    public String getvalue() {
        return ((COSName) baseObject).getName();
    }

    /** Get original length of the name before applying any escape mechanisms and encodings
     */
    @Override
    public Long getorigLength() {
        return Long.valueOf(((COSName) baseObject).getOriginalLength().intValue());
    }
}
