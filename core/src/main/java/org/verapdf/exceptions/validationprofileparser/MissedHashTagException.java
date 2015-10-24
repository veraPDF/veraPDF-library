package org.verapdf.exceptions.validationprofileparser;

import org.verapdf.core.VeraPDFException;

/**
 * Exception that occurs when there is no hash tag in a validation profile
 *
 * @author Maksim Bezrukov
 */
public class MissedHashTagException extends VeraPDFException {

	/**
	 *
	 */
	private static final long serialVersionUID = 3393127317304315565L;

	/**
	 * Constructs new MissedHashTagException
	 */
	public MissedHashTagException() {
	}

	/**
	 * Constructs new MissedHashTagException
	 *
	 * @param message the message of the exception
	 */
	public MissedHashTagException(String message) {
		super(message);
	}
}
