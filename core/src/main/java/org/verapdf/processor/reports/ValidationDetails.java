/**
 * 
 */
package org.verapdf.processor.reports;

import java.util.Set;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *         <a href="https://github.com/carlwilson">carlwilson AT github</a>
 * @version 0.1 Created 10 Nov 2016:08:19:46
 */
@XmlJavaTypeAdapter(ValidationDetailsImpl.Adapter.class)
public interface ValidationDetails {
	/**
	 * @return the number of rules for which all checks passed during validation
	 */
	public int getPassedRules();

	/**
	 * @return the number of rules for which at least one check failed during
	 *         validation
	 */
	public int getFailedRules();

	/**
	 * @return the number of successful checks during validation
	 */
	public int getPassedChecks();

	/**
	 * @return the number of failed checks during validation
	 */
	public int getFailedChecks();

	/**
	 * @return the {@link Set} of {@link RuleSummary} for rules used during
	 *         validation
	 */
	public Set<RuleSummary> getRuleSummaries();
}
