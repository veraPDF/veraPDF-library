package org.verapdf.validation.report.model;

/**
 * Structure of the validation info part of the report.
 *
 * @author Maksim Bezrukov
 */
public class ValidationInfo {
	private final Profile profile;
	private final Result result;

	/**
	 * Creates model of validation report
	 *
	 * @param profile model of an information about used validation profile
	 * @param result  model of the result of the validation
	 */
	public ValidationInfo(final Profile profile, final Result result) {
		this.profile = profile;
		this.result = result;
	}

	/**
	 * @return Class which represents an information about a profile which used in this validation info.
	 */
	public Profile getProfile() {
		return profile;
	}

	/**
	 * @return Class which represents a result of validation.
	 */
	public Result getResult() {
		return result;
	}
}
