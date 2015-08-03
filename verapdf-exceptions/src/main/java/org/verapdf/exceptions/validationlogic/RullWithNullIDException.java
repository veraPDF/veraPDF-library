package org.verapdf.exceptions.validationlogic;

import org.verapdf.exceptions.VeraPDFException;

/**
 * Exception that occurs when there is a rule with null id
 *
 * @author Maksim Bezrukov
 */
public class RullWithNullIDException extends VeraPDFException {

    /**
     * 
     */
    private static final long serialVersionUID = 4029891805328060539L;

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
