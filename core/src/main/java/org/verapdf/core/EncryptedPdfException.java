/**
 * 
 */
package org.verapdf.core;

/**
 * @author  <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *          <a href="https://github.com/carlwilson">carlwilson AT github</a>
 *
 * @version 0.1
 * 
 * Created 21 Sep 2016:11:32:26
 */

public class EncryptedPdfException extends VeraPDFException {
    /**
	 * 
	 */
	private static final long serialVersionUID = -2103117235597202987L;

	/**
     * Default constructor for EncryptedPdfException.
     */
    public EncryptedPdfException() {
        super();
    }

    /**
     * Constructs new EncryptedPdfException with a String message
     *
     * @param message
     *            a String message describing the cause of the exception.
     */
    public EncryptedPdfException(String message) {
        super(message);
    }

    /**
     * Constructs new EncryptedPdfException with a String message and a Throwable
     * cause.
     *
     * @param message
     *            a String message describing the cause of the exception.
     * @param cause
     *            Throwable cause of the exception.
     */
    public EncryptedPdfException(String message, Throwable cause) {
        super(message, cause);
    }
}
