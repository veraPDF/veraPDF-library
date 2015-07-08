package org.verapdf.exceptions.validationlogic;

import org.verapdf.exceptions.VeraPDFException;

/**
 * Exception that occurs when there is more than one identical global variable names in the profile model
 *
 * @author Maksim Bezrukov
 */
public class MultiplyGlobalVariableNameException extends VeraPDFException {

    /**
     * Constructs new MultiplyGlobalVariableNameException
     */
    public MultiplyGlobalVariableNameException() {
    }

    /**
     * Constructs new MultiplyGlobalVariableNameException
     *
     * @param message - the message of the exception
     */
    public MultiplyGlobalVariableNameException(String message) {
        super(message);
    }
}
