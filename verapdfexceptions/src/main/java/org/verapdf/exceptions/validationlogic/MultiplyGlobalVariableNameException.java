package org.verapdf.exceptions.validationlogic;

import org.verapdf.exceptions.VeraPDFException;

/** Exception that occurs when there is more than one identical global variable names in the profile model
 * Created by bezrukov on 6/17/15.
 *
 * @author Maksim Bezrukov
 * @version 1.0
 */
public class MultiplyGlobalVariableNameException extends VeraPDFException {

    public MultiplyGlobalVariableNameException() {
    }

    public MultiplyGlobalVariableNameException(String message) {
        super(message);
    }
}
