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
 * Created 9 Nov 2016:07:43:07
 */

public interface ValidationReport {
	public String getProfileName();
	public ValidationDetails getDetails();
	public String getStatement();
	public boolean isCompliant();
}
