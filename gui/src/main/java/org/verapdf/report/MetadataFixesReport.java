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
	private final int completedMetadataFixes;
	@XmlElement(name = "appliedFix")
	private final List<String> appliedFixes;

	private MetadataFixesReport(String status, int completedMetadataFixes, List<String> appliedFixes) {
		this.status = status;
		this.completedMetadataFixes = completedMetadataFixes;
		this.appliedFixes = appliedFixes;
	}

	private MetadataFixesReport() {
		this("", 0, null);
	}

	static MetadataFixesReport fromValues(final MetadataFixerResult fixerResult) {
		if (fixerResult == null) {
			return null;
		}
		String status = fixerResult.getRepairStatus().toString();
		List<String> fixes = new ArrayList<>(fixerResult.getAppliedFixes());
		int completedFixes = fixes.size();

		return new MetadataFixesReport(status, completedFixes, fixes);
	}
}
