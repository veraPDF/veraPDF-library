package org.verapdf.core;


/**
 * Exception that occurs when wrong features tree node constructs
 *
 * @author Maksim Bezrukov
 */
public class FeatureParsingException extends VeraPDFException {

    /**
	 *
	 */
    private static final long serialVersionUID = -9004876231849554050L;

    /**
     * Constructs new FeaturesTreeNodeException
     */
    public FeatureParsingException() {
    }

    /**
     * Constructs new FeaturesTreeNodeException
     *
     * @param message
     *            message of the error
     */
    public FeatureParsingException(String message) {
        super(message);
    }
}
