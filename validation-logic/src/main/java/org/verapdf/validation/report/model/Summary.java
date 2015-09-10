package org.verapdf.validation.report.model;

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
	private final int attrCompletedMetadataFixes;
	private final int attrFailedMetadataFixes;
	private final int attrWarnings;

	/**
	 * Creates summary model for validation report
	 *
	 * @param attrPassedRules            number of passed rules
	 * @param attrFailedRules            number of failed rules
	 * @param attrPassedChecks           number of passed checks
	 * @param attrFailedChecks           number of failed checks
	 * @param attrCompletedMetadataFixes number of completed metadata fixes
	 * @param attrFailedMetadataFixes    number of failed metadata fixes
	 * @param attrWarnings               number of warnngs
	 */
	public Summary(int attrPassedRules, int attrFailedRules, int attrPassedChecks, int attrFailedChecks, int attrCompletedMetadataFixes, int attrFailedMetadataFixes, int attrWarnings) {
		this.attrPassedRules = attrPassedRules;
		this.attrFailedRules = attrFailedRules;
		this.attrPassedChecks = attrPassedChecks;
		this.attrFailedChecks = attrFailedChecks;
		this.attrCompletedMetadataFixes = attrCompletedMetadataFixes;
		this.attrFailedMetadataFixes = attrFailedMetadataFixes;
		this.attrWarnings = attrWarnings;
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
	 * @return the number of failed metadata fixes.
	 */
	public int getAttrFailedMetadataFixes() {
		return attrFailedMetadataFixes;
	}

	/**
	 * @return the number of warnings.
	 */
	public int getAttrWarnings() {
		return attrWarnings;
	}
}
