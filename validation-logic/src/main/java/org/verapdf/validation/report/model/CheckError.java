package org.verapdf.validation.report.model;

import java.util.Collections;
import java.util.List;

/**
 * Structure of the error/warning in a check.
 *
 * @author Maksim Bezrukov
 */
public class CheckError {
	private final String message;
	private final List<String> arguments;

	/**
	 * Creates check error model
	 *
	 * @param message  message of the error
	 * @param argument list of arguments for the message
	 */
	public CheckError(String message, List<String> argument) {
		this.message = message;
		this.arguments = Collections.unmodifiableList(argument);
	}

	/**
	 * @return Error message.
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @return Error arguments.
	 */
	public List<String> getArguments() {
		return arguments;
	}
}
