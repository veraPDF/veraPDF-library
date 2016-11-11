/**
 * 
 */
package org.verapdf.processor.reports;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *         <a href="https://github.com/carlwilson">carlwilson AT github</a>
 * @version 0.1 Created 9 Nov 2016:07:43:07
 */
@XmlJavaTypeAdapter(ValidationReportImpl.Adapter.class)
public interface ValidationReport {
	/**
	 * @return the name of the
	 *         {@link org.verapdf.pdfa.validation.profiles.ValidationProfile}
	 *         used to validate
	 */
	public String getProfileName();

	/**
	 * @return the {@link ValidationDetails} for the validation task
	 */
	public ValidationDetails getDetails();

	/**
	 * @return the validation statement
	 */
	public String getStatement();

	/**
	 * @return true if the PDF was compliant with the profile
	 */
	public boolean isCompliant();
}
