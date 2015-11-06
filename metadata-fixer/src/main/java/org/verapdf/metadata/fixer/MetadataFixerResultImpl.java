package org.verapdf.metadata.fixer;

import org.verapdf.pdfa.MetadataFixerResult;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Evgeniy Muravitskiy
 */
public class MetadataFixerResultImpl implements MetadataFixerResult {

	private RepairStatus status;
	private final List<String> appliedFixes = new ArrayList<>();

	public MetadataFixerResultImpl() {
		this.status = RepairStatus.NO_ACTION;
	}

	public RepairStatus getRepairStatus() {
		return this.status;
	}

	public void setRepairStatus(RepairStatus status) {
		this.status = status;
	}

	public List<String> getAppliedFixes() {
		return this.appliedFixes;
	}

	public void addAppliedFix(String fix) {
		this.appliedFixes.add(fix);
	}

	@Override
	public Iterator<String> iterator() {
		return this.appliedFixes.iterator();
	}

}
