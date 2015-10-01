package org.verapdf.metadata.fixer.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Evgeniy Muravitskiy
 */
public class FixReport {

	private ValidationStatus status = ValidationStatus.VALID;

	private List<String> appliedFixes = new ArrayList<>();

	public void addFix(String fix) {
		this.appliedFixes.add(fix);
	}

	public List<String> getAppliedFixes() {
		return this.appliedFixes;
	}

	public ValidationStatus getStatus() {
		return status;
	}

	public void setStatus(ValidationStatus status) {
		this.status = status;
	}
}
