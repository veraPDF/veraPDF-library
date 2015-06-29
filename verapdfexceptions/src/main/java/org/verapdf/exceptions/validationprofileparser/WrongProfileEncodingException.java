package org.verapdf.exceptions.validationprofileparser;

import org.verapdf.exceptions.VeraPDFException;

/**
 * Exception that occurs when a validation profile's encoding is not utf8
 *
 * @author Maksim Bezrukov
 */
public class WrongProfileEncodingException extends VeraPDFException {

    /**
     * Constructs new WrongProfileEncodingException
     */
    public WrongProfileEncodingException() {
    }

    /**
     * Constructs new WrongProfileEncodingException
     *
     * @param message - the message of the exception
     */
    public WrongProfileEncodingException(String message) {
        super(message);
    }

}
