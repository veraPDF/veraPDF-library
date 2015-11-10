package org.verapdf.report;

import javax.xml.bind.annotation.XmlAttribute;

/**
 * @author Maksim Bezrukov
 */
public class Summary {

//	@XmlAttribute
//	private final int passedRules;
//	@XmlAttribute
//	private final int failedRules;
//	@XmlAttribute
//	private final int passedChecks;
//	@XmlAttribute
//	private final int failedChecks;
@XmlAttribute
private final String metadataFixesStatus;
	@XmlAttribute
	private final Integer completedMetadataFixes;

	private Summary(int passedRules, int failedRules, int passedChecks, int failedChecks, String metadataFixesStatus, Integer completedMetadataFixes) {
//		this.passedRules = passedRules;
//		this.failedRules = failedRules;
//		this.passedChecks = passedChecks;
//		this.failedChecks = failedChecks;
		this.metadataFixesStatus = metadataFixesStatus;
		this.completedMetadataFixes = completedMetadataFixes;
	}

	private Summary() {
		this(0, 0, 0, 0, null, 0);
	}

	static Summary fromValues(int passedRules, int failedRules, int passedChecks, int failedChecks, String metadataFixesStatus, Integer completedMetadataFixes) {
		return new Summary(passedRules, failedRules, passedChecks, failedChecks, metadataFixesStatus, completedMetadataFixes);
	}
}
