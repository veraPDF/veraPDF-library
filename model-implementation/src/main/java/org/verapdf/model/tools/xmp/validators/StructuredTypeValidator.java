package org.verapdf.model.tools.xmp.validators;

import com.adobe.xmp.impl.VeraPDFXMPNode;

/**
 * @author Maksim Bezrukov
 */
public class StructuredTypeValidator implements TypeValidator {


    @Override
    public boolean isCorresponding(VeraPDFXMPNode node) {
        return false;
    }
}
