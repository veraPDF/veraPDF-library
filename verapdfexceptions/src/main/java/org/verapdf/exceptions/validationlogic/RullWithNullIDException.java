package org.verapdf.exceptions.validationlogic;

import org.verapdf.exceptions.VeraPDFException;

/**
 * Exception that occurs when there is a rule with null id
 *
 * @author Maksim Bezrukov
 */
public class RullWithNullIDException extends VeraPDFException {

    /**
     * Constructs new RullWithNullIDException
     */
    public RullWithNullIDException() {
    }

    /**
     * Constructs new RullWithNullIDException
     *
     * @param message - the message of the exception
     */
    public RullWithNullIDException(String message) {
        super(message);
    }
}
