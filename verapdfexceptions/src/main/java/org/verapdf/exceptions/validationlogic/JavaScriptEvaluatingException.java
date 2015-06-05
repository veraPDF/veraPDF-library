package org.verapdf.exceptions.validationlogic;

import org.verapdf.exceptions.VeraPDFException;

/** Exception that occurs in evaluating javascript
 * Created by bezrukov on 6/5/15.
 *
 * @author Maksim Bezrukov
 * @version 1.0
 */
public class JavaScriptEvaluatingException extends VeraPDFException {

    public JavaScriptEvaluatingException() {
    }

    public JavaScriptEvaluatingException(String message) {
        super(message);
    }
}
