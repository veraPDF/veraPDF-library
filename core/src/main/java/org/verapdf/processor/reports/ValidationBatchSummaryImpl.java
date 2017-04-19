package org.verapdf.processor.reports;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlAdapter;

@XmlRootElement(name = "validationReports")
final class ValidationBatchSummaryImpl extends AbstractBatchJobSummary implements ValidationBatchSummary {
	static final ValidationBatchSummary DEFAULT = new ValidationBatchSummaryImpl();
	@XmlAttribute
	private final int compliant;
	@XmlAttribute
	private final int nonCompliant;

	private ValidationBatchSummaryImpl() {
		this(0, 0, 0);
	}

	private ValidationBatchSummaryImpl(final int compliant, final int nonCompliant, final int failedJobs) {
		super(compliant + nonCompliant + failedJobs, failedJobs);
		assert (compliant >= 0 && nonCompliant >= 0);
		this.compliant = compliant;
		this.nonCompliant = nonCompliant;
	}

	@Override
	public int getCompliantPdfaCount() {
		return this.compliant;
	}

	@Override
	public int getNonCompliantPdfaCount() {
		return this.nonCompliant;
	}

	@Override
	public int getFailedJobCount() {
		return this.failedJobs;
	}

	@Override
	public int getTotalJobCount() {
		return this.totalJobs;
	}


	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + this.totalJobs;
		result = prime * result + this.failedJobs;
		result = prime * result + this.compliant;
		result = prime * result + this.nonCompliant;
		return result;
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof ValidationBatchSummaryImpl)) {
			return false;
		}
		ValidationBatchSummaryImpl other = (ValidationBatchSummaryImpl) obj;
		if (this.totalJobs != other.totalJobs) {
			return false;
		}
		if (this.failedJobs != other.failedJobs) {
			return false;
		}
		if (this.compliant != other.compliant) {
			return false;
		}
		if (this.nonCompliant != other.nonCompliant) {
			return false;
		}
		return true;
	}

	static ValidationBatchSummary defaultInstance() {
		return ValidationBatchSummaryImpl.DEFAULT;
	}

	static ValidationBatchSummary fromValues(final int compliant, final int nonCompliant, final int failedJobs) {
		return new ValidationBatchSummaryImpl(compliant, nonCompliant, failedJobs);
	}

	static class Adapter extends XmlAdapter<ValidationBatchSummaryImpl, ValidationBatchSummary> {
		@Override
		public ValidationBatchSummary unmarshal(ValidationBatchSummaryImpl summary) {
			return summary;
		}

		@Override
		public ValidationBatchSummaryImpl marshal(ValidationBatchSummary summary) {
			return (ValidationBatchSummaryImpl) summary;
		}
	}
}
