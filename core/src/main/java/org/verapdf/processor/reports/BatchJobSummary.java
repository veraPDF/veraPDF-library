/**
 * 
 */
package org.verapdf.processor.reports;

/**
 * @author  <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *          <a href="https://github.com/carlwilson">carlwilson AT github</a>
 *
 * @version 0.1
 * 
 * Created 18 Apr 2017:18:24:01
 */

public interface BatchJobSummary {
	/**
	 * @return the number of jobs that failed to execute due to an exception.
	 */
	int getFailedJobCount();

	/**
	 * @return the total number of validation jobs in the batch.
	 */
	int getTotalJobCount();
	
	int getSuccessfulJobCount();
}
