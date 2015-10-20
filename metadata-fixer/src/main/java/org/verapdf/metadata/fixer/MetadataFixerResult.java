package org.verapdf.metadata.fixer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Evgeniy Muravitskiy
 */
// TODO : remove me and use source implementation
public class MetadataFixerResult implements Iterable<String> {

	private RepairStatus status = RepairStatus.NO_ACTION;
	private final List<String> appliedFixes = new ArrayList<>();

	public MetadataFixerResult() {

	}

	public MetadataFixerResult(RepairStatus status) {
		if (status == null) {
			throw new IllegalArgumentException("Repair status must be not null");
		}
		this.status = status;
	}

	public RepairStatus getStatus() {
		return this.status;
	}

	public void setStatus(RepairStatus status) {
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

	public enum RepairStatus {

		FAILED("Metadata repair was attempted but failed"),

		NO_ACTION("No action was taken because the file is already valid"),

		NOT_REPAIRABLE("The fixer could not determine any action that could repair the PDF/A"),

		SUCCESSFUL("Metadata repair was carried out successfully");

		private final String value;

		RepairStatus(String value) {
			this.value = value;
		}

		public String getDescription() {
			return this.value;
		}

	}
}
