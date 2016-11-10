/**
 * 
 */
package org.verapdf.processor.reports;

import org.verapdf.component.Components;

/**
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *         <a href="https://github.com/carlwilson">carlwilson AT github</a>
 * @version 0.1 Created 10 Nov 2016:08:12:36
 */

public class Reports {
	private Reports() {
		// TODO Auto-generated constructor stub
	}

	public static final BatchSummary createBatchSummary(final Components.Timer timer, final int jobs,
			final int failedJobs) {
		return BatchSummaryImpl.fromValues(timer.stop(), jobs, failedJobs);
	}
}
