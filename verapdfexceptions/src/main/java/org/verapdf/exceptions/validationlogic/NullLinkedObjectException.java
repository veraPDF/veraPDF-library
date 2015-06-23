package org.verapdf.exceptions.validationlogic;

import org.verapdf.exceptions.VeraPDFException;

/** Exception that occurs when there is a null object returns from link
 * Created by bezrukov on 6/5/15.
 *
 * @author Maksim Bezrukov
 * @version 1.0
 */

public class NullLinkedObjectException extends VeraPDFException {

    public NullLinkedObjectException() {
    }

    public NullLinkedObjectException(String message) {
        super(message);
    }
}
