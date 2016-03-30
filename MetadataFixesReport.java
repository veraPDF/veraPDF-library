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
        this("", Integer.valueOf(0), null, null);
    }

	static MetadataFixesReport fromValues(final MetadataFixerResult fixerResult) {
		if (fixerResult == null) {
			return null;
		}
        MetadataFixerResult.RepairStatus repairStatus = fixerResult.getRepairStatus();
        Integer completedFixes = null;
        List<String> fixes = null;
        List<String> errorMessages = null;
        List<String> appliedFixes = fixerResult.getAppliedFixes();
        switch (repairStatus) {
            case SUCCESS:
            case ID_REMOVED:
                completedFixes = Integer.valueOf(appliedFixes.size());
                fixes = new ArrayList<>(appliedFixes);
                break;
            case FIX_ERROR:
                errorMessages = new ArrayList<>(appliedFixes);
                break;
            default:
				break;
        }
        String status = repairStatus.toString();
        return new MetadataFixesReport(status, completedFixes, fixes, errorMessages);
    }
}
