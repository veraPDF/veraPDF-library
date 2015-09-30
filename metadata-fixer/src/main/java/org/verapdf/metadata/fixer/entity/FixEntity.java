package org.verapdf.metadata.fixer.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Evgeniy Muravitskiy
 */
public class FixEntity {

	private ValidationStatus status;

	private List<String> fixes = new ArrayList<>();

	public FixEntity() {
		status = ValidationStatus.VALID;
	}

	public void addFix(String fix) {
		this.fixes.add(fix);
	}

	public List<String> getFixes() {
		return this.fixes;
	}

	public ValidationStatus getStatus() {
		return status;
	}

	public void setStatus(ValidationStatus status) {
		this.status = status;
	}
}
