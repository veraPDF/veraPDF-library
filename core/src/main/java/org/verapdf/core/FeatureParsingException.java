package org.verapdf.core;

/**
 * Exception type for errors that occur while parsing PDF Features
 *
 * @author Maksim Bezrukov
 */
public class FeatureParsingException extends VeraPDFException {

    /**
	 *
	 */
    private static final long serialVersionUID = -9004876231849554050L;

    /**
     * Default constructor for FeatureParsingException.
     */
    public FeatureParsingException() {
        super();
}

    /**
     * Constructs new FeatureParsingException with a String message.
     *
     * @param message
     *            a String message describing the cause of the exception.
     */
    public FeatureParsingException(String message) {
        super(message);
    }

    /**
     * Constructs new FeatureParsingException with a String message and a
     * Throwable cause.
     *
     * @param message
     *            a String message describing the cause of the exception.
     * @param cause
     *            Throwable cause of the exception.
     */
    public FeatureParsingException(String message, Throwable cause) {
        super(message, cause);
    }
}
