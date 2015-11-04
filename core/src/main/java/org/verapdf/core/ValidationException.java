package org.verapdf.core;

/**
 * Exception type for problems encountered while performing validation.
 *
 * @author Maksim Bezrukov
 */

public class ValidationException extends VeraPDFException {

    /**
	 *
	 */
    private static final long serialVersionUID = 2140287213702618800L;

    /**
     * Default constructor for ValidationException.
     */
    public ValidationException() {
        super();
    }

    /**
     * Constructs new ValidationException with a String message
     *
     * @param message
     *            a String message describing the cause of the exception.
     */
    public ValidationException(String message) {
        super(message);
    }

    /**
     * Constructs new ValidationException with a String message and a Throwable
     * cause.
     *
     * @param message
     *            a String message describing the cause of the exception.
     * @param cause
     *            Throwable cause of the exception.
     */
    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
