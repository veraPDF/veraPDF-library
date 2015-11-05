package org.verapdf.gui.tools;

/**
 * @author Evgeniy Muravitskiy
 */
public enum ProcessingType {

	VALIDATING("Validation"),

	FEATURES("Features"),

	VALIDATING_AND_FEATURES("Validation & Features");

	private final String value;

	ProcessingType(String value) {
		this.value = value;
	}

	public boolean isValidating() {
		return this == VALIDATING || this == VALIDATING_AND_FEATURES;
	}

	public boolean isFeatures() {
		return this == FEATURES || this == VALIDATING_AND_FEATURES;
	}

	@Override
	public String toString() {
		return this.value;
	}

}
