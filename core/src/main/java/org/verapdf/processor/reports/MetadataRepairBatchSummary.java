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
@XmlRootElement(name = "metadataRepairReports")
public final class MetadataRepairBatchSummary extends AbstractBatchJobSummary {
	private static final MetadataRepairBatchSummary DEFAULT = new MetadataRepairBatchSummary();

	private MetadataRepairBatchSummary() {
		this(0, 0);
	}

	/**
	 * @param totalJobs
	 * @param failedJobs
	 */
	private MetadataRepairBatchSummary(final int totalJobs, final int failedJobs) {
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

	public static MetadataRepairBatchSummary defaultInstance() {
		return DEFAULT;
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

	public static MetadataRepairBatchSummary fromValues(final int totalJobs, final int failedJobs) {
		if (totalJobs < 0)
			throw new IllegalArgumentException("Argument totalJobs must be >= 0"); //$NON-NLS-1$
		if (failedJobs < 0)
			throw new IllegalArgumentException("Argument failedJobs must be >= 0"); //$NON-NLS-1$
		if (failedJobs > totalJobs)
			throw new IllegalArgumentException("Argument failedJobs can not be > totalJobs"); //$NON-NLS-1$
		return new MetadataRepairBatchSummary(totalJobs, failedJobs);
	}
}
