package org.verapdf.core;

/**
 * Exception type for PDFParser problems.
 *
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 */

public class ModelParsingException extends VeraPDFException {
    /**
     * 
     */
    private static final long serialVersionUID = -6507757998419697602L;

    /**
     * Default constructor for ModelParsingException.
     */
    public ModelParsingException() {
        super();
    }

    /**
     * Constructs new ModelParsingException with a String message
     *
     * @param message
     *            a String message describing the cause of the exception.
     */
    public ModelParsingException(String message) {
        super(message);
    }

    /**
     * Constructs new ModelParsingException with a String message and a Throwable
     * cause.
     *
     * @param message
     *            a String message describing the cause of the exception.
     * @param cause
     *            Throwable cause of the exception.
     */
    public ModelParsingException(String message, Throwable cause) {
        super(message, cause);
    }
}
