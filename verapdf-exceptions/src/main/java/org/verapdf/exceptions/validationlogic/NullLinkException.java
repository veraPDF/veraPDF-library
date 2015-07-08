package org.verapdf.exceptions.validationlogic;

import org.verapdf.exceptions.VeraPDFException;

/**
 * Exception that occurs when there is a null link in an object
 *
 * @author Maksim Bezrukov
 */
public class NullLinkException extends VeraPDFException {

    /**
     * Constructs new NullLinkException
     */
    public NullLinkException() {
    }

    /**
     * Constructs new NullLinkException
     *
     * @param message - the message of the exception
     */
    public NullLinkException(String message) {
        super(message);
    }
}
