package org.verapdf.processor;

/**
 * @author Evgeniy Muravitskiy
 */
public enum TaskType {
	NONE("NONE"), PARSE("parsing", "PDF Parsing"), VALIDATE("validation", "PDF/A Validation"), EXTRACT_FEATURES(
			"features", "Feature Extraction"), FIX_METADATA("metadata", "Metadata Repair");

	private final String value;
	private final String fullName;

	TaskType(final String value) {
		this(value, value);
	}

	TaskType(final String value, final String fullName) {
		this.value = value;
		this.fullName = fullName;
	}

	public static TaskType fromString(final String toParse) {
		for (TaskType processingType : TaskType.values()) {
			if (processingType.toString().equalsIgnoreCase(toParse))
				return processingType;
		}
		throw new IllegalArgumentException("String can't be parsed into ProcessingType");
	}

	public String getValue() {
		return this.value;
	}

	public String fullName() {
		return this.fullName;
	}

	@Override
	public String toString() {
		return this.value;
	}

}
