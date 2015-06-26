package org.verapdf.exceptions.validationlogic;

import org.verapdf.exceptions.VeraPDFException;

/**
 * Exception that occurs when there is a null link in an object
 *
 * @author Maksim Bezrukov
 */
public class NullLinkException extends VeraPDFException {

    public NullLinkException() {
    }

    public NullLinkException(String message) {
        super(message);
    }
}
