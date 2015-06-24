package org.verapdf.exceptions.validationlogic;

import org.verapdf.exceptions.VeraPDFException;

/**
 * Exception that occurs when there is a null object returns from link
 *
 * @author Maksim Bezrukov
 */

public class NullLinkedObjectException extends VeraPDFException {

    public NullLinkedObjectException() {
    }

    public NullLinkedObjectException(String message) {
        super(message);
    }
}
