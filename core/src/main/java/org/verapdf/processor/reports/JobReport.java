/**
 * 
 */
package org.verapdf.processor.reports;

import org.verapdf.component.AuditDuration;
import org.verapdf.report.FeaturesReport;
import org.verapdf.report.ItemDetails;
import org.verapdf.report.MetadataFixesReport;
import org.verapdf.report.ValidationReport;

/**
 * @author  <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *          <a href="https://github.com/carlwilson">carlwilson AT github</a>
 *
 * @version 0.1
 * 
 * Created 9 Nov 2016:07:28:52
 */

public interface JobReport {
	public AuditDuration getDuration();
	public ItemDetails getItemDetails();
	public ValidationReport getValidationReport();
	public FeaturesReport getFeaturesReport();
	public MetadataFixesReport getFixerReport();
}
