package org.verapdf.validation.report.model;

import org.verapdf.validation.report.model.Check.Status;

import java.util.Collections;
import java.util.List;

/**
 * Structure of the rule check result.
 *
 * @author Maksim Bezrukov
 */
public class Rule {

	private final String ID;
	private Status status;
	private List<Check> checks;
	private int checksCount;
	private int failedChecksCount;

	/**
	 * Creates rule model for the validation report
	 *
	 * @param attrID id of the rule
	 * @param checks list of performed checks of this rule
	 */
	public Rule(final String attrID, final List<Check> checks) {
		this.ID = attrID;
		this.checks = checks;
		this.checksCount = checks.size();
	}

	/**
	 * @return id of the rule
	 */
	public String getID() {
		return this.ID;
	}

	/**
	 * @return actual status (passed/failed) of the rule
	 */
	public Status getStatus() {
		return this.status;
	}

	public void setStatus() {
		this.checks = Collections.unmodifiableList(checks);
		this.status = this.failedChecksCount > 0 ? Status.FAILED : Status.PASSED;
	}

	/**
	 * @return list of checks structure
	 */
	public List<Check> getChecks() {
		return this.checks;
	}

	/**
	 * Increment count of performed checks.  Count of
	 * performed checks and count of checks in
	 * {@link Rule#checks} can be different
	 */
	public void incChecksCount() {
		this.checksCount++;
	}

	public int getFailedChecksCount() {
		return this.failedChecksCount;
	}

	public void incFailedChecksCount() {
		this.failedChecksCount++;
	}

	public int getPassedChecksCount() {
		return this.checksCount - this.failedChecksCount;
	}

	/**
	 * Add another one performed check for current rule
	 * @param check another one check
	 */
	public void add(Check check) {
		this.checks.add(check);
		incChecksCount(check);
	}

	public void incChecksCount(Check check) {
		incChecksCount();
		if (check.getStatus() == Status.FAILED) {
			incFailedChecksCount();
		}
	}

}
