package org.verapdf.processor.reports;

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlValue;

public abstract class AbstractBatchJobSummary implements BatchJobSummary {
	@XmlValue
	protected final int totalJobs;
	@XmlAttribute
	protected final int failedJobs;

	protected AbstractBatchJobSummary() {
		this(0, 0);
	}

	protected AbstractBatchJobSummary(final int totalJobs, final int failedJobs) {
		super();
		assert (totalJobs >= 0 && failedJobs >= 0 && totalJobs >= failedJobs);
		this.failedJobs = failedJobs;
		this.totalJobs = totalJobs;
	}
	
	@Override
	public int getSuccessfulJobCount() {
		return this.totalJobs - this.failedJobs;
	}
}
