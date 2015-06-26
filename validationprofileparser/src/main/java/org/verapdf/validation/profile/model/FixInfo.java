package org.verapdf.validation.profile.model;

/**
 * Structure of the error in a fix.
 *
 * @author Maksim Bezrukov
 */
public class FixInfo {
    private String message;

    /**
     * Creates model of fix info
     *
     * @param message - message of the fix info
     */
    public FixInfo(String message) {
        this.message = message;
    }

    /**
     * @return Text in tag "message".
     */
    public String getMessage() {
        return message;
    }
}
