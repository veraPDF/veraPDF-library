package org.verapdf.model.tools.xmp.validators;

import com.adobe.xmp.impl.VeraPDFXMPNode;

/**
 * @author Maksim Bezrukov
 */
public interface TypeValidator {

    boolean isCorresponding(VeraPDFXMPNode node);
}
