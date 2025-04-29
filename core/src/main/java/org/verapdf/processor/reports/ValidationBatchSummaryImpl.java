/**
 * This file is part of veraPDF Library core, a module of the veraPDF project.
 * Copyright (c) 2015-2025, veraPDF Consortium <info@verapdf.org>
 * All rights reserved.
 *
 * veraPDF Library core is free software: you can redistribute it and/or modify
 * it under the terms of either:
 *
 * The GNU General public license GPLv3+.
 * You should have received a copy of the GNU General Public License
 * along with veraPDF Library core as the LICENSE.GPL file in the root of the source
 * tree.  If not, see http://www.gnu.org/licenses/ or
 * https://www.gnu.org/licenses/gpl-3.0.en.html.
 *
 * The Mozilla Public License MPLv2+.
 * You should have received a copy of the Mozilla Public License along with
 * veraPDF Library core as the LICENSE.MPL file in the root of the source tree.
 * If a copy of the MPL was not distributed with this file, you can obtain one at
 * http://mozilla.org/MPL/2.0/.
 */
package org.verapdf.processor.reports;

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.adapters.XmlAdapter;

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
