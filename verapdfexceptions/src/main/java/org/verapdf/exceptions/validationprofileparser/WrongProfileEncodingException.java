package org.verapdf.exceptions.validationprofileparser;

import org.verapdf.exceptions.VeraPDFException;

/** Exception that occurs when a validation profile's encoding is not utf8
 * Created by bezrukov on 6/17/15.
 *
 * @author Maksim Bezrukov
 * @version 1.0
 */
public class WrongProfileEncodingException extends VeraPDFException {

    public WrongProfileEncodingException() {
    }

    public WrongProfileEncodingException(String message) {
        super(message);
    }

}
