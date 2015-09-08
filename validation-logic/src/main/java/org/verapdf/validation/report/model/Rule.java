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
	private final Status status;
	private final List<Check> checks;

	/**
	 * Creates rule model for the validation report
	 *
	 * @param attrID id of the rule
	 * @param checks list of performed checks of this rule
	 */
	public Rule(final String attrID, final List<Check> checks) {
		this.ID = attrID;

		Status ruleStatus = Status.PASSED;

		if (checks != null) {
			for (Check check : checks) {
				if (check != null && Status.FAILED == check.getStatus()) {
					ruleStatus = Status.FAILED;
				}
			}
			this.checks = Collections.unmodifiableList(checks);
		} else {
			this.checks = Collections.emptyList();
		}

		this.status = ruleStatus;

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

	/**
	 * @return number of checks for this rule
	 */
	public int getCheckCount() {
		return this.checks.size();
	}

	/**
	 * @return list of checks structure
	 */
	public List<Check> getChecks() {
		return this.checks;
	}

}
