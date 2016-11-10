/**
 * 
 */
package org.verapdf.processor.reports;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *         <a href="https://github.com/carlwilson">carlwilson AT github</a>
 * @version 0.1 Created 10 Nov 2016:08:18:17
 */
@XmlRootElement(name = "validationReport")
final class ValidationReportImpl implements ValidationReport {
	private static final ValidationReportImpl defaultInstance = new ValidationReportImpl();
	@XmlElement
	private final ValidationDetails details;
	@XmlAttribute
	private final String profileName;
	@XmlAttribute
	private final String statement;
	@XmlAttribute
	private final boolean isCompliant;

	private ValidationReportImpl() {
		this(ValidationDetailsImpl.defaultInstance(), "Unknown Profile", "Statement", false);
	}

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

	static class Adapter extends XmlAdapter<ValidationReportImpl, ValidationReport> {
		@Override
		public ValidationReport unmarshal(ValidationReportImpl report) {
			return report;
		}

		@Override
		public ValidationReportImpl marshal(ValidationReport report) {
			return (ValidationReportImpl) report;
		}
	}

	static final ValidationReport defaultInstance() {
		return defaultInstance;
	}

	static final ValidationReport fromValues(final ValidationDetails details, final String profileName,
			final String statement, final boolean isCompliant) {
		return new ValidationReportImpl(details, profileName, statement, isCompliant);
	}
}
