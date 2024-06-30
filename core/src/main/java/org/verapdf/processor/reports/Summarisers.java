/**
 * 
 */
package org.verapdf.processor.reports;

import org.verapdf.pdfa.results.ValidationResult;
import org.verapdf.processor.ProcessorResult;
import org.verapdf.processor.TaskResult;
import org.verapdf.processor.TaskType;

/**
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *         <a href="https://github.com/carlwilson">carlwilson AT github</a>
 * @version 0.1 Created 18 Apr 2017:22:21:39
 */

public final class Summarisers {
	private Summarisers() {
		assert (false);
	}

	public static ValidationBatchSummary defaultValidationSummary() {
		return ValidationBatchSummaryImpl.DEFAULT;
	}

	public static ValidationBatchSummary validationSummaryFromValues(final int compliant, final int nonCompliant,
			final int failedJobs) {
		if (compliant < 0)
			throw new IllegalArgumentException("Argument compliant must be >= 0"); //$NON-NLS-1$
		if (nonCompliant < 0)
			throw new IllegalArgumentException("Argument nonCompliant must be >= 0"); //$NON-NLS-1$
		if (failedJobs < 0)
			throw new IllegalArgumentException("Argument failedJobs must be >= 0"); //$NON-NLS-1$
		return ValidationBatchSummaryImpl.fromValues(compliant, nonCompliant, failedJobs);
	}

	public static class ValidationSummaryBuilder extends AbstractSummaryBuilder {
		private int compliant = 0;
		private int nonCompliant = 0;

		public ValidationSummaryBuilder() {
			super(TaskType.VALIDATE);
		}

		public ValidationSummaryBuilder addResult(final ProcessorResult result) {
			super.processResult(result);
			TaskResult taskResult = result.getResultForTask(TaskType.VALIDATE);
			if (taskResult != null && taskResult.isExecuted() && taskResult.isSuccess()) {
				boolean isCompliant = true;
				for (ValidationResult res : result.getValidationResults()) {
					if (!res.isCompliant()) {
						isCompliant = false;
						break;
					}
				}
				if (isCompliant)
					this.compliant++;
				else
					this.nonCompliant++;
			}
			return this;
		}

		public ValidationBatchSummary build() {
			return ValidationBatchSummaryImpl.fromValues(this.compliant, this.nonCompliant, this.failed);
		}
	}

	public static class FeatureSummaryBuilder extends AbstractSummaryBuilder {
		public FeatureSummaryBuilder() {
			super(TaskType.EXTRACT_FEATURES);
		}

		public FeatureSummaryBuilder addResult(final ProcessorResult result) {
			super.processResult(result);
			return this;
		}

		public FeaturesBatchSummary build() {
			return FeaturesBatchSummary.fromValues(this.total, this.failed);
		}
	}

	public static class RepairSummaryBuilder extends AbstractSummaryBuilder {
		public RepairSummaryBuilder() {
			super(TaskType.FIX_METADATA);
		}

		public RepairSummaryBuilder addResult(final ProcessorResult result) {
			super.processResult(result);
			return this;
		}

		public MetadataRepairBatchSummary build() {
			return MetadataRepairBatchSummary.fromValues(this.total, this.failed);
		}
	}

	private abstract static class AbstractSummaryBuilder {
		protected int total = 0;
		protected int failed = 0;
		private final TaskType taskType;

		protected AbstractSummaryBuilder(final TaskType taskType) {
			this.taskType = taskType;
		}

		protected void processResult(final ProcessorResult result) {
			this.total++;
			if (!result.isPdf() || result.isEncryptedPdf()) {
				this.failed++;
			} else {
				TaskResult taskResult = result.getResultForTask(this.taskType);
				if (taskResult == null || !taskResult.isExecuted() || !taskResult.isSuccess()) {
					this.failed++;
				}
			}
		}
	}
}
