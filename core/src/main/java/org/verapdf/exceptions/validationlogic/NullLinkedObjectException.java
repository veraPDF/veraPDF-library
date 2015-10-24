package org.verapdf.exceptions.validationlogic;

import org.verapdf.core.VeraPDFException;

/**
 * Exception that occurs when there is a null object returns from link
 *
 * @author Maksim Bezrukov
 */

public class NullLinkedObjectException extends VeraPDFException {

	/**
	 *
	 */
	private static final long serialVersionUID = 2140287213702618800L;

	/**
	 * Constructs new NullLinkedObjectException
	 */
	public NullLinkedObjectException() {
	}

	/**
	 * Constructs new NullLinkedObjectException
	 *
	 * @param message the message of the exception
	 */
	public NullLinkedObjectException(String message) {
		super(message);
	}
}
