package org.verapdf.exceptions.validationlogic;

import org.verapdf.exceptions.VeraPDFException;

/**
 * Exception that occurs in evaluating javascript
 *
 * @author Maksim Bezrukov
 */
public class JavaScriptEvaluatingException extends VeraPDFException {

    /**
     * Constructs new JavaScriptEvaluatingException
     */
    public JavaScriptEvaluatingException() {
    }

    /**
     * Constructs new JavaScriptEvaluatingException
     *
     * @param message - the message of the exception
     */
    public JavaScriptEvaluatingException(String message) {
        super(message);
    }

    /**
     * Constructs new JavaScriptEvaluatingException
     *
     * @param message - the message of the exception
     * @param cause   - Throwable cause of the exception
     */
    public JavaScriptEvaluatingException(String message, Throwable cause) {
        super(message, cause);
    }
}
