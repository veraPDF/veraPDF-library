package org.verapdf.processor.reports.enums;

public enum JobEndStatus {
	NORMAL("normal"),
	TIMEOUT("timeout"),
	CANCELLED("cancelled");

	private final String value;

	JobEndStatus(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	@Override
	public String toString() {
		return this.value;
	}
}
