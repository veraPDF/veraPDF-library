package org.verapdf.exceptions.validationprofileparser;

import org.verapdf.exceptions.VeraPDFException;

/**
 * Exception that occurs when there is an incorrect import path in a validation profile
 * Created by bezrukov on 6/5/15.
 *
 * @author Maksim Bezrukov
 * @version 1.0
 */
public class IncorrectImportPathException extends VeraPDFException {

    public IncorrectImportPathException() {
    }

    public IncorrectImportPathException(String message) {
        super(message);
    }
}
