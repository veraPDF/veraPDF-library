package org.verapdf.exceptions.validationprofileparser;

import org.verapdf.exceptions.VeraPDFException;

/**
 * Exception that occurs when a validation profile is unsigned or has wrong signature
 *
 * @author Maksim Bezrukov
 */
public class WrongSignatureException extends VeraPDFException {

    /**
     * Constructs new WrongSignatureException
     */
    public WrongSignatureException() {
    }

    /**
     * Constructs new WrongSignatureException
     *
     * @param message - the message of the exception
     */
    public WrongSignatureException(String message) {
        super(message);
    }
}
