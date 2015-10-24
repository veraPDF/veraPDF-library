package org.verapdf.core;


/**
 * Exception that occurs when there is a null object returns from link
 *
 * @author Maksim Bezrukov
 */

public class ValidationException extends VeraPDFException {

	/**
	 *
	 */
	private static final long serialVersionUID = 2140287213702618800L;

	/**
	 * Constructs new NullLinkedObjectException
	 */
	public ValidationException() {
	}

	/**
	 * Constructs new NullLinkedObjectException
	 *
	 * @param message the message of the exception
	 */
	public ValidationException(String message) {
		super(message);
	}
}
