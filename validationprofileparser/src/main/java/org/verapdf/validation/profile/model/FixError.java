package org.verapdf.validation.profile.model;

/**
 * Structure of the error in a fix.
 *
 * @author Maksim Bezrukov
 */
public class FixError {
    private String message;

    /**
     * Creates fix error model.
     *
     * @param message - message of the fix error.
     */
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
