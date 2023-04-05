/**
 * 
 */
package org.verapdf.processor.reports;

import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * Holds the count of validation jobs and their statuses for a batch job
 * summary.
 * 
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *         <a href="https://github.com/carlwilson">carlwilson AT github</a>
 * @version 0.1 Created 18 Apr 2017:16:53:46
 */
@XmlJavaTypeAdapter(ValidationBatchSummaryImpl.Adapter.class)
public interface ValidationBatchSummary extends BatchJobSummary {
	/**
	 * @return the number of compliant PDF/As in the batch, that is the number
	 *         of files that passed PDF/A validation.
	 */
	int getCompliantPdfaCount();

	/**
	 * @return the number of non-compliant PDF/As in the batch, that is the
	 *         number of files that failed PDF/A validation.
	 */
	int getNonCompliantPdfaCount();
}
