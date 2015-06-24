package org.verapdf.exceptions.validationlogic;

import org.verapdf.exceptions.VeraPDFException;

/**
 * Exception that occurs in evaluating javascript
 *
 * @author Maksim Bezrukov
 */
public class JavaScriptEvaluatingException extends VeraPDFException {

    public JavaScriptEvaluatingException() {
    }

    public JavaScriptEvaluatingException(String message) {
        super(message);
    }

    public JavaScriptEvaluatingException(String message, Throwable cause) {
        super(message, cause);
    }
}
