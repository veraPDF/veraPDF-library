package org.verapdf.exceptions.featurereport;

import org.verapdf.exceptions.VeraPDFException;

/**
 * Exception that occurs when wrong features tree node constructs
 *
 * @author Maksim Bezrukov
 */
public class FeaturesTreeNodeException extends VeraPDFException {

    /**
     * Constructs new FeaturesTreeNodeException
     */
    public FeaturesTreeNodeException() {
    }

    /**
     * Constructs new FeaturesTreeNodeException
     *
     * @param message - message of the error
     */
    public FeaturesTreeNodeException(String message) {
        super(message);
    }
}
