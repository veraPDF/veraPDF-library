package org.verapdf.processor.config;

/**
 * @author Evgeniy Muravitskiy
 */
public enum ProcessingType {

	VALIDATION(Constants.VALIDATION, Constants.TEXT_VALIDATION),

	FEATURES(Constants.FEATURES, Constants.TEXT_FEATURES),

	VALIDATION_AND_FEATURES(Constants.VALIDATION_AND_FEATURES, Constants.TEXT_VALIDATION_AND_FEATURES);

	private final String value;
	private final String text;

	ProcessingType(String value, String text) {
		this.value = value;
		this.text = text;
	}

	public static ProcessingType getType(boolean isValidation, boolean isFeatures) {
		if (isValidation) {
			if (isFeatures) {
				return VALIDATION_AND_FEATURES;
			}
			return VALIDATION;
		} else if (isFeatures) {
			return FEATURES;
		} else {
			throw new IllegalArgumentException("Processing type should contain at least one process");
		}
	}

	public boolean isValidating() {
		return this == VALIDATION || this == VALIDATION_AND_FEATURES;
	}

	public boolean isFeatures() {
		return this == FEATURES || this == VALIDATION_AND_FEATURES;
	}

	public static ProcessingType fromString(final String toParse) {
		for (ProcessingType processingType : ProcessingType.values()) {
			if (processingType.toString().equalsIgnoreCase(toParse))
				return processingType;
		}
		throw new IllegalArgumentException("String can't be parsed into ProcessingType");
	}

	public String toText() {
		return this.text;
	}

	@Override
	public String toString() {
		return this.value;
	}

	public static class Constants {
		public static final String TEXT_VALIDATION_AND_FEATURES = "Validation & Features";
		public static final String TEXT_VALIDATION = "Validation";
		public static final String TEXT_FEATURES = "Features";
		public static final String VALIDATION_AND_FEATURES = "validationAndFeatures";
		public static final String VALIDATION = "validation";
		public static final String FEATURES = "features";
	}

}
