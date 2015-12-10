package org.verapdf.report;

import org.verapdf.pdfa.results.MetadataFixerResult;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Maksim Bezrukov
 */
public class MetadataFixesReport {

	@XmlAttribute
	private final String status;
	@XmlAttribute
    private final Integer completedMetadataFixes;
    @XmlElement(name = "appliedFix")
	private final List<String> appliedFixes;
    @XmlElement(name = "errorMessage")
    private final List<String> errorMessages;

    private MetadataFixesReport(String status, Integer completedMetadataFixes, List<String> appliedFixes, List<String> errorMessages) {
        this.status = status;
		this.completedMetadataFixes = completedMetadataFixes;
		this.appliedFixes = appliedFixes;
        this.errorMessages = errorMessages;
    }

	private MetadataFixesReport() {
        this("", 0, null, null);
    }

	static MetadataFixesReport fromValues(final MetadataFixerResult fixerResult) {
		if (fixerResult == null) {
			return null;
		}
		String status = fixerResult.getRepairStatus().toString();
        Integer completedFixes = null;
        List<String> fixes = null;
        List<String> errorMessages = null;
        switch (fixerResult.getRepairStatus()) {
            case SUCCESS:
            case ID_REMOVED:
                completedFixes = fixes.size();
                fixes = new ArrayList<>(fixerResult.getAppliedFixes());
                break;
            case FIX_ERROR:
                errorMessages = new ArrayList<>(fixerResult.getAppliedFixes());
                break;
        }

        return new MetadataFixesReport(status, completedFixes, fixes, errorMessages);
    }
}
