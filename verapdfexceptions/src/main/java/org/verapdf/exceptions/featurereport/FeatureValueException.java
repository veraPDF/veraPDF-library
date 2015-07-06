package org.verapdf.exceptions.featurereport;

import org.verapdf.exceptions.VeraPDFException;

/**
 * Exception that occurs when wrong feature value found
 *
 * @author Maksim Bezrukov
 */
public class FeatureValueException extends VeraPDFException {

    /**
     * Creates new FeatureValueException
     */
    public FeatureValueException() {
    }

    /**
     * Creates new FeatureValueException
     *
     * @param message - message of the error
     */
    public FeatureValueException(String message) {
        super(message);
    }

    /**
     * Constructs new FeatureValueException
     *
     * @param message - the message of the exception
     * @param cause   - Throwable cause of the exception
     */
    public FeatureValueException(String message, Throwable cause) {
        super(message, cause);
    }
}
