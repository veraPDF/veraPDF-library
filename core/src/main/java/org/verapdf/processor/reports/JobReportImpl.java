/**
 * 
 */
package org.verapdf.processor.reports;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

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
 * Created 10 Nov 2016:07:34:53
 */
@XmlRootElement(name="job")
final class JobReportImpl implements JobReport {
	@XmlElement
	public final AuditDuration duration;
	@XmlElement
	public final ItemDetails itemDetails;
	@XmlElement
	public final ValidationReport validationReport;
	@XmlElement
	public final MetadataFixesReport fixerReport;
	@XmlElement
	public final FeaturesReport featuresReport;

	private JobReportImpl(final AuditDuration duration, final ItemDetails itemDetails, final ValidationReport validationReport,
			final MetadataFixesReport fixerReport, final FeaturesReport featuresReport) {
		super();
		this.duration = duration;
		this.itemDetails = itemDetails;
		this.validationReport = validationReport;
		this.fixerReport = fixerReport;
		this.featuresReport = featuresReport;
	}

	
	/**
	 * @return the duration
	 */
	@Override
	public AuditDuration getDuration() {
		return this.duration;
	}


	/**
	 * @return the itemDetails
	 */
	@Override
	public ItemDetails getItemDetails() {
		return this.itemDetails;
	}


	/**
	 * @return the validationReport
	 */
	@Override
	public ValidationReport getValidationReport() {
		return this.validationReport;
	}


	/**
	 * @return the fixerReport
	 */
	@Override
	public MetadataFixesReport getFixerReport() {
		return this.fixerReport;
	}


	/**
	 * @return the featuresReport
	 */
	@Override
	public FeaturesReport getFeaturesReport() {
		return this.featuresReport;
	}


	static JobReport fromValues(final AuditDuration duration, final ItemDetails itemDetails, final ValidationReport validationReport,
			final MetadataFixesReport fixerReport, final FeaturesReport featuresReport) {
		return new JobReportImpl(duration, itemDetails, validationReport, fixerReport, featuresReport);
	}
}
