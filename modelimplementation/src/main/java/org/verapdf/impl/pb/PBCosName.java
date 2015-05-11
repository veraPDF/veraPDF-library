package org.verapdf.impl.pb;

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
    //TODO : update pdfbox (field in COSName and parser) for saving orig length of name
    @Override
    public Integer getorigLength() {
        System.err.println("Length of name is not correct. Need to update classes of pdfbox.");
        return ((COSName) baseObject).getName().length();
    }
}
