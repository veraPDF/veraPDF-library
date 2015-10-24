package org.verapdf.core;

/**
 * Top level exception class for all exceptions in veraPDF library.
 *
 * @author Maksim Bezrukov
 */
public class VeraPDFException extends Exception {

    /**
	 *
	 */
    private static final long serialVersionUID = -6566760719467943980L;

    /**
     * Default constructor for VeraPDFException.
     */
    public VeraPDFException() {
        super();
    }

    /**
     * Constructs new VeraPDFException with a String message
     *
     * @param message
     *            a String message describing the cause of the exception.
     */
    public VeraPDFException(String message) {
        super(message);
    }

    /**
     * Constructs new VeraPDFException with a String message and a Throwable
     * cause.
     *
     * @param message
     *            a String message describing the cause of the exception.
     * @param cause
     *            Throwable cause of the exception.
     */
    public VeraPDFException(String message, Throwable cause) {
        super(message, cause);
    }
}
