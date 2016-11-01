package org.verapdf.processor;

/**
 * @author Evgeniy Muravitskiy
 */
public enum TaskType {
	PARSE("parsing"),
	VALIDATE("validation"),
	EXTRACT_FEATURES("features"),
	FIX_METADATA("metadata");

	private final String value;
	

	TaskType(String value) {
		this.value = value;
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
	@Override
	public String toString() {
		return this.value;
	}

}
