package org.verapdf.validation.profile.model;

/**
 * Structure of the error in a fix.
 * Created by bezrukov on 4/24/15.
 *
 * @author Maksim Bezrukov
 * @version 1.0
 * @see Fix
 */
public class FixError {
    public String message;

    public FixError(String message) {
        this.message = message;
    }

    /**
     * @return Text in tag "message".
     */
    public String getMessage() {
        return message;
    }
}
