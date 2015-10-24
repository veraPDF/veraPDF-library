package org.verapdf.exceptions.validationlogic;

import org.verapdf.core.VeraPDFException;

/**
 * Exception that occurs when there is a null link in an object
 *
 * @author Maksim Bezrukov
 */
public class NullLinkException extends VeraPDFException {

	/**
	 *
	 */
	private static final long serialVersionUID = 554502477751657123L;

	/**
	 * Constructs new NullLinkException
	 */
	public NullLinkException() {
	}

	/**
	 * Constructs new NullLinkException
	 *
	 * @param message the message of the exception
	 */
	public NullLinkException(String message) {
		super(message);
	}
}
