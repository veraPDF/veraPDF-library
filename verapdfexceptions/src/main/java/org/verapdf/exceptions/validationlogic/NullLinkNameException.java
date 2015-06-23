package org.verapdf.exceptions.validationlogic;

import org.verapdf.exceptions.VeraPDFException;

/** Exception that occurs when there is a null link name in an object's link names
 * Created by bezrukov on 6/5/15.
 *
 * @author Maksim Bezrukov
 * @version 1.0
 */
public class NullLinkNameException extends VeraPDFException {

    public NullLinkNameException() {
    }

    public NullLinkNameException(String message) {
        super(message);
    }
}
