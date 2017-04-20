/**
 * 
 */
package org.verapdf.processor.reports;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *         <a href="https://github.com/carlwilson">carlwilson AT github</a>
 * @version 0.1 Created 18 Apr 2017:21:43:38
 */
@XmlRootElement(name = "featureReports")
public final class FeaturesBatchSummary extends AbstractBatchJobSummary {
	private static final FeaturesBatchSummary DEFAULT = new FeaturesBatchSummary();

	private FeaturesBatchSummary() {
		this(0, 0);
	}

	/**
	 * @param totalJobs
	 * @param failedJobs
	 */
	private FeaturesBatchSummary(final int totalJobs, final int failedJobs) {
		super(totalJobs, failedJobs);
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
		result = prime * result + this.failedJobs;
		result = prime * result + this.totalJobs;
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
		if (!(obj instanceof AbstractBatchJobSummary)) {
			return false;
		}
		AbstractBatchJobSummary other = (AbstractBatchJobSummary) obj;
		if (this.failedJobs != other.failedJobs) {
			return false;
		}
		if (this.totalJobs != other.totalJobs) {
			return false;
		}
		return true;
	}

	public static FeaturesBatchSummary defaultInstance() {
		return DEFAULT;
	}

	public static FeaturesBatchSummary fromValues(final int totalJobs, final int failedJobs) {
		if (totalJobs < 0)
			throw new IllegalArgumentException("Argument totalJobs must be >= 0"); //$NON-NLS-1$
		if (failedJobs < 0)
			throw new IllegalArgumentException("Argument failedJobs must be >= 0"); //$NON-NLS-1$
		if (failedJobs > totalJobs)
			throw new IllegalArgumentException("Argument failedJobs can not be > totalJobs"); //$NON-NLS-1$
		return new FeaturesBatchSummary(totalJobs, failedJobs);
	}
}
