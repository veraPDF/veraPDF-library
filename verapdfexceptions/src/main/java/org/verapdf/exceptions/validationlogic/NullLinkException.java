package org.verapdf.exceptions.validationlogic;

import org.verapdf.exceptions.VeraPDFException;

/** Exception that occurs when there is a null link in an object
 * Created by bezrukov on 6/5/15.
 *
 * @author Maksim Bezrukov
 * @version 1.0
 */
public class NullLinkException extends VeraPDFException {

    public NullLinkException() {
    }

    public NullLinkException(String message) {
        super(message);
    }
}
