package org.verapdf.exceptions.validationlogic;

import org.verapdf.exceptions.VeraPDFException;

/** Exception that occurs when there is a rule with null id
 * Created by bezrukov on 6/9/15.
 *
 * @author Maksim Bezrukov
 * @version 1.0
 */
public class RullWithNullIDException extends VeraPDFException{

    public RullWithNullIDException() {
    }

    public RullWithNullIDException(String message) {
        super(message);
    }
}
