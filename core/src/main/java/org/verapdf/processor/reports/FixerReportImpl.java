/**
 * 
 */
package org.verapdf.processor.reports;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import org.verapdf.pdfa.results.MetadataFixerResult;

/**
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *         <a href="https://github.com/carlwilson">carlwilson AT github</a>
 * @version 0.1 Created 9 Nov 2016:07:50:43
 */
@XmlRootElement(name = "metadataRepairReport")
final class FixerReportImpl implements MetadataFixerReport {
	@XmlAttribute
	private final String status;
	@XmlAttribute
	private final int fixCount;
	@XmlElementWrapper(name="fixes")
	@XmlElement(name="fix")
	private final List<String> fixes;
	@XmlElementWrapper(name="errors")
	@XmlElement(name="error")
	private final List<String> errors;

	private FixerReportImpl(final String status, final int fixCount, final List<String> fixes,
			final List<String> errors) {
		super();
		this.status = status;
		this.fixCount = fixCount;
		this.fixes = Collections.unmodifiableList(fixes);
		this.errors = Collections.unmodifiableList(errors);
	}
	
	/**
	 * @return the status
	 */
	@Override
	public String getStatus() {
		return this.status;
	}


	/**
	 * @return the fixCount
	 */
	@Override
	public int getFixCount() {
		return this.fixCount;
	}


	/**
	 * @return the fixes
	 */
	@Override
	public List<String> getFixes() {
		return this.fixes;
	}


	/**
	 * @return the errors
	 */
	@Override
	public List<String> getErrors() {
		return this.errors;
	}


	static MetadataFixerReport fromValues(final String status, final int fixCount, final List<String> fixes,
			final List<String> errors) {
		return new FixerReportImpl(status, fixCount, fixes, errors);
	}
	
	static MetadataFixerReport fromValues(final MetadataFixerResult fixerResult) {
        int fixCount = 0;
        List<String> fixes = new ArrayList<>();
        List<String> errors = new ArrayList<>();
        switch (fixerResult.getRepairStatus()) {
            case SUCCESS:
            case ID_REMOVED:
            	fixCount = fixerResult.getAppliedFixes().size();
                fixes = new ArrayList<>(fixerResult.getAppliedFixes());
                break;
            case FIX_ERROR:
            	errors = new ArrayList<>(fixerResult.getAppliedFixes());
                break;
            default:
				break;
        }
        String status = fixerResult.getRepairStatus().toString();
		return new FixerReportImpl(status, fixCount, fixes, errors);
    }

}
