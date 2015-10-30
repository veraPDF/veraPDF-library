package org.verapdf.validation.report.model;

import org.verapdf.pdfa.MetadataFixerResult;

/**
 * Structure of the summary of a result.
 *
 * @author Maksim Bezrukov
 */
public class Summary {
	private final int attrPassedRules;
	private final int attrFailedRules;
	private final int attrPassedChecks;
	private final int attrFailedChecks;
	private String attrMetadataFixerResult = "";
	private int attrCompletedMetadataFixes = -1;
	private final int attrWarnings;

	/**
	 * Creates summary model for validation report
	 *
	 * @param attrPassedRules            number of passed rules
	 * @param attrFailedRules            number of failed rules
	 * @param attrPassedChecks           number of passed checks
	 * @param attrFailedChecks           number of failed checks
	 * @param attrWarnings               number of warnngs
	 */
	public Summary(int attrPassedRules, int attrFailedRules, int attrPassedChecks, int attrFailedChecks, int attrWarnings) {
		this.attrPassedRules = attrPassedRules;
		this.attrFailedRules = attrFailedRules;
		this.attrPassedChecks = attrPassedChecks;
		this.attrFailedChecks = attrFailedChecks;
		this.attrWarnings = attrWarnings;
	}

	void setMetadataFixerResult(MetadataFixerResult res) {
		this.attrMetadataFixerResult = res.getRepairStatus().getReadableName();
		if (res.getRepairStatus().equals(MetadataFixerResult.RepairStatus.SUCCESS) ||
				res.getRepairStatus().equals(MetadataFixerResult.RepairStatus.ID_REMOVED)) {
			this.attrCompletedMetadataFixes = res.getAppliedFixes().size();
		}
	}

	/**
	 * @return the number of passed rules.
	 */
	public int getAttrPassedRules() {
		return attrPassedRules;
	}

	/**
	 * @return the number of failed rules.
	 */
	public int getAttrFailedRules() {
		return attrFailedRules;
	}

	/**
	 * @return the number of passed checks.
	 */
	public int getAttrPassedChecks() {
		return attrPassedChecks;
	}

	/**
	 * @return the number of failed checks.
	 */
	public int getAttrFailedChecks() {
		return attrFailedChecks;
	}

	/**
	 * @return the number of completed metadata fixes.
	 */
	public int getAttrCompletedMetadataFixes() {
		return attrCompletedMetadataFixes;
	}

	/**
	 * @return metadata fixer status or empty String if it is not set
	 */
	public String getAttrMetadataFixerResult() {
		return attrMetadataFixerResult;
	}

	/**
	 * @return the number of warnings.
	 */
	public int getAttrWarnings() {
		return attrWarnings;
	}
}
