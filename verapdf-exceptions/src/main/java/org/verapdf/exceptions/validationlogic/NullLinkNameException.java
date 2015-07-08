package org.verapdf.exceptions.validationlogic;

import org.verapdf.exceptions.VeraPDFException;

/**
 * Exception that occurs when there is a null link name in an object's link names
 *
 * @author Maksim Bezrukov
 */
public class NullLinkNameException extends VeraPDFException {

    /**
     * Constructs new NullLinkNameException
     */
    public NullLinkNameException() {
    }

    /**
     * Constructs new NullLinkNameException
     *
     * @param message - the message of the exception
     */
    public NullLinkNameException(String message) {
        super(message);
    }
}
