package org.verapdf.exceptions.validationprofileparser;

import org.verapdf.exceptions.VeraPDFException;

/** Exception that occurs when null pointer to the profile used
 * Created by bezrukov on 6/17/15.
 *
 * @author Maksim Bezrukov
 * @version 1.0
 */
public class NullProfileException extends VeraPDFException {

    public NullProfileException() {
    }

    public NullProfileException(String message) {
        super(message);
    }
}
