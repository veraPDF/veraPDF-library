package org.verapdf.processor.config;

/**
 * @author Evgeniy Muravitskiy
 */
public enum ProcessingType {

	VALIDATING_AND_FEATURES(Constants.VALIDATING_AND_FEATURES, Constants.TEXT_VALIDATING_AND_FEATURES),

	VALIDATING(Constants.VALIDATING, Constants.TEXT_VALIDATING),

	FEATURES(Constants.FEATURES, Constants.TEXT_FEATURES);

	private final String value;
	private final String text;

	ProcessingType(String value, String text) {
		this.value = value;
		this.text = text;
	}

	public boolean isValidating() {
		return this == VALIDATING || this == VALIDATING_AND_FEATURES;
	}

	public boolean isFeatures() {
		return this == FEATURES || this == VALIDATING_AND_FEATURES;
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
		public static final String TEXT_VALIDATING_AND_FEATURES = "Validation & Features";
		public static final String TEXT_VALIDATING = "Validation";
		public static final String TEXT_FEATURES = "Features";
		public static final String VALIDATING_AND_FEATURES = "validationAndFeatures";
		public static final String VALIDATING = "validation";
		public static final String FEATURES = "features";
	}

}