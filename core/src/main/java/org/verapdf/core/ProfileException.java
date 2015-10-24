package org.verapdf.core;


/**
 * Exception that occurs when there is no hash tag in a validation profile
 *
 * @author Maksim Bezrukov
 */
public class ProfileException extends VeraPDFException {

	/**
	 *
	 */
	private static final long serialVersionUID = 3393127317304315565L;

	/**
	 * Constructs new MissedHashTagException
	 */
	public ProfileException() {
	}

	/**
	 * Constructs new MissedHashTagException
	 *
	 * @param message the message of the exception
	 */
	public ProfileException(String message) {
		super(message);
	}
}
