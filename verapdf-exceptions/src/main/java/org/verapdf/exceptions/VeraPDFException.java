package org.verapdf.exceptions;

/**
 * General exception class for all expected exceptions in veraPDF library
 *
 * @author Maksim Bezrukov
 */
public class VeraPDFException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = -6566760719467943980L;

    /**
     * Constructs new VeraPDFException
     */
    public VeraPDFException() {
    }

    /**
     * Constructs new VeraPDFException
     *
     * @param message - the message of the exception
     */
    public VeraPDFException(String message) {
        super(message);
    }

    /**
     * Constructs new VeraPDFException
     *
     * @param message - the message of the exception
     * @param cause   - Throwable cause of the exception
     */
    public VeraPDFException(String message, Throwable cause) {
        super(message, cause);
    }
}
