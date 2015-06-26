package org.verapdf.exceptions;

/**
 * General exception class for all expected exceptions in veraPDF library
 *
 * @author Maksim Bezrukov
 */
public class VeraPDFException extends Exception {

    public VeraPDFException() {
    }

    public VeraPDFException(String message) {
        super(message);
    }

    public VeraPDFException(String message, Throwable cause) {
        super(message, cause);
    }
}
