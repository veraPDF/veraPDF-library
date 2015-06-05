package org.verapdf.exceptions;

/** General exception class for all expected exceptions in veraPDF library
 * Created by bezrukov on 6/5/15.
 *
 * @author Maksim Bezrukov
 * @version 1.0
 */
public class VeraPDFException extends Exception {

    public VeraPDFException() {
    }

    public VeraPDFException(String message) {
        super(message);
    }


}
