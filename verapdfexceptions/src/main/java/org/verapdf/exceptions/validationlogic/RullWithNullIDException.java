package org.verapdf.exceptions.validationlogic;

import org.verapdf.exceptions.VeraPDFException;

/**
 * Exception that occurs when there is a rule with null id
 *
 * @author Maksim Bezrukov
 */
public class RullWithNullIDException extends VeraPDFException {

    public RullWithNullIDException() {
    }

    public RullWithNullIDException(String message) {
        super(message);
    }
}
