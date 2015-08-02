package org.verapdf.exceptions.validationprofileparser;

import org.verapdf.exceptions.VeraPDFException;

/**
 * Exception that occurs when there is an incorrect import path in a validation profile
 *
 * @author Maksim Bezrukov
 */
public class IncorrectImportPathException extends VeraPDFException {

    /**
     * 
     */
    private static final long serialVersionUID = -23463583610584494L;

    /**
     * Constructs new IncorrectImportPathException
     */
    public IncorrectImportPathException() {
    }

    /**
     * Constructs new IncorrectImportPathException
     *
     * @param message - the message of the exception
     */
    public IncorrectImportPathException(String message) {
        super(message);
    }
}
