package org.verapdf.exceptions.validationprofileparser;

import org.verapdf.exceptions.VeraPDFException;

/**
 * Exception that occurs when null pointer to the profile used
 *
 * @author Maksim Bezrukov
 */
public class NullProfileException extends VeraPDFException {

    /**
     * Constructs new NullProfileException
     */
    public NullProfileException() {
    }

    /**
     * Constructs new NullProfileException
     *
     * @param message - the message of the exception
     */
    public NullProfileException(String message) {
        super(message);
    }
}
