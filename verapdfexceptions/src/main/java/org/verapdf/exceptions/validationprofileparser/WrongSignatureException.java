package org.verapdf.exceptions.validationprofileparser;

import org.verapdf.exceptions.VeraPDFException;

/** Exception that occurs when a validation profile is unsigned or has wrong signature
 * Created by bezrukov on 6/16/15.
 *
 * @author Maksim Bezrukov
 * @version 1.0
 */
public class WrongSignatureException extends VeraPDFException {

    public WrongSignatureException() {
    }

    public WrongSignatureException(String message) {
        super(message);
    }
}
