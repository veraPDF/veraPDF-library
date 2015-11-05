package org.verapdf.core;

/**
 * Exception that occurs when there is a problem parsing a validation profile.
 *
 * @author Maksim Bezrukov
 */
public class ProfileException extends VeraPDFException {

    /**
	 *
	 */
    private static final long serialVersionUID = 3393127317304315565L;

    /**
     * Default constructor for ProfileException.
     */
    public ProfileException() {
        super();
    }

    /**
     * Constructs new ProfileException with a String message
     *
     * @param message
     *            a String message describing the cause of the exception.
     */
    public ProfileException(String message) {
        super(message);
    }

    /**
     * Constructs new ProfileException with a String message and a Throwable
     * cause.
     *
     * @param message
     *            a String message describing the cause of the exception.
     * @param cause
     *            Throwable cause of the exception.
     */
    public ProfileException(String message, Throwable cause) {
        super(message, cause);
    }

}
