package org.verapdf.exceptions.validationprofileparser;

import org.verapdf.exceptions.VeraPDFException;

/**
 * Exception that occurs when there is no hash tag in a validation profile
 * Created by bezrukov on 6/15/15.
 *
 * @author Maksim Bezrukov
 * @version 1.0
 */
public class MissedHashTagException extends VeraPDFException {

    public MissedHashTagException() {
    }

    public MissedHashTagException(String message) {
        super(message);
    }
}
