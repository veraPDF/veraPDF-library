package org.verapdf.validation.profile.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Structure of the error/warning in a rule.
 *
 * @author Maksim Bezrukov
 */
public class RuleError {
	private final String message;
	private final List<String> argument;

	/**
	 * Creates new  rule error model.
	 *
	 * @param message  error message
	 * @param argument list of arguments for the message
	 */
	public RuleError(final String message, final List<String> argument) {
		this.message = message;
		this.argument = (argument == null) ? new ArrayList<String>() : argument;
	}

	/**
	 * @return Text provided by attribute "argument".
	 */
	public List<String> getArgument() {
		return this.argument;
	}

	/**
	 * @return Text provided by attribute "message".
	 */
	public String getMessage() {
		return this.message;
	}
}
