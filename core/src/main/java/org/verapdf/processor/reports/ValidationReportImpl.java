/**
 * 
 */
package org.verapdf.processor.reports;

/**
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *         <a href="https://github.com/carlwilson">carlwilson AT github</a>
 * @version 0.1 Created 10 Nov 2016:08:18:17
 */

final class ValidationReportImpl implements ValidationReport {
	private final ValidationDetails details;
	private final String profileName;
	private final String statement;
	private final boolean isCompliant;

	private ValidationReportImpl(final ValidationDetails details, final String profileName, final String statement,
			final boolean isCompliant) {
		super();
		this.details = details;
		this.profileName = profileName;
		this.statement = statement;
		this.isCompliant = isCompliant;
	}

	/**
	 * @return the details
	 */
	@Override
	public ValidationDetails getDetails() {
		return this.details;
	}

	/**
	 * @return the profileName
	 */
	@Override
	public String getProfileName() {
		return this.profileName;
	}

	/**
	 * @return the statement
	 */
	@Override
	public String getStatement() {
		return this.statement;
	}

	/**
	 * @return the isCompliant
	 */
	@Override
	public boolean isCompliant() {
		return this.isCompliant;
	}

	static ValidationReport fromValues(final ValidationDetails details, final String profileName,
			final String statement, final boolean isCompliant) {
		return new ValidationReportImpl(details, profileName, statement, isCompliant);
	}
}
