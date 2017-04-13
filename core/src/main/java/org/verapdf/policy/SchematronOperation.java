package org.verapdf.policy;

import org.verapdf.features.objects.Feature;

/**
 * Enum with operations on elements of Feature.FeatureType that can be used in
 * policy configuration file.
 *
 * @author Sergey Shemyakov
 */
public enum SchematronOperation {

	// Any type
	PRESENT(false, "is present"),
	NOT_PRESENT(false, "is not present"),

	//Numbers and strings
	IS_EQUAL(true, "equals to"),
	NOT_EQUAL(true, "not equals to"),

	// Boolean
	IS_TRUE(false, "is true"),
	IS_FALSE(false, "is false"),

	// Number
	IS_GREATER(true, "is greater than"),
	IS_GREATER_OR_EQUAL(true, "is greater or equal to"),
	IS_LESS(true, "is less than"),
	IS_LESS_OR_EQUAL(true, "is less or equal to"),

	// String
	STARTS_WITH(true, "starts with"),
	ENDS_WITH(true, "ends with"),
	CONTAINS(true, "contains substring");

	private boolean hasArguments;
	private String description;

	SchematronOperation(boolean hasArguments, String description) {
		this.hasArguments = hasArguments;
		this.description = description;
	}

	public boolean hasArguments() {
		return hasArguments;
	}

	public String getDescription() {
		return description;
	}

	public AssertionInformation getAssertionInfo(Feature feature, String argument) {
		if (feature == null) {
			throw new IllegalArgumentException("Feature argument can not be null");
		}
		if (this.hasArguments() && argument == null) {
			throw new IllegalArgumentException("Argument variable can not be null for operation " + this.description);
		}
		String test;
		switch (this) {
			case PRESENT:
				test = "count(" + feature.getFeatureXPath() + ") > 0";
				break;
			case NOT_PRESENT:
				test = "not(count(" + feature.getFeatureXPath() + ") > 0)";
				break;
			case IS_EQUAL:
				if (feature.getFeatureType() == Feature.FeatureType.NUMBER) {
					test = "number(" + feature.getFeatureXPath() + ") = " + argument;
				} else {
					test = feature.getFeatureXPath() + " = '" + argument + "'";
				}
				break;
			case NOT_EQUAL:
				if (feature.getFeatureType() == Feature.FeatureType.NUMBER) {
					test = "not(number(" + feature.getFeatureXPath() + ") = " + argument + ")";
				} else {
					test = "not(" + feature.getFeatureXPath() + " = '" + argument + "')";
				}
				break;
			case IS_TRUE:
				test = "boolean(" + feature.getFeatureXPath() + ")";
				break;
			case IS_FALSE:
				test = "not(boolean(" + feature.getFeatureXPath() + "))";
				break;
			case IS_GREATER:
				test = "number(" + feature.getFeatureXPath() + ") > " + argument;
				break;
			case IS_GREATER_OR_EQUAL:
				test = "number(" + feature.getFeatureXPath() + ") >= " + argument;
				break;
			case IS_LESS:
				test = "number(" + feature.getFeatureXPath() + ") < " + argument;
				break;
			case IS_LESS_OR_EQUAL:
				test = "number(" + feature.getFeatureXPath() + ") <= " + argument;
				break;
			case CONTAINS:
				test = "contains(" + feature.getFeatureXPath() + ",'" + argument + "')";
				break;
			case STARTS_WITH:
				test = "starts-with(" + feature.getFeatureXPath() + ",'" + argument + "')";
				break;
			case ENDS_WITH:
				test = "ends-with(" + feature.getFeatureXPath() + ",'" + argument + "')";
				break;
			default:
				throw new IllegalStateException("Unsupported operation type");
		}
		String desc = "Failed check: " + feature.getFeatureName() + " " + this.getDescription();
		if (argument != null) {
			desc += " " + argument;
		}
		return new AssertionInformation(test, desc);
	}

	public static class AssertionInformation {
		private String testAssertion;
		private String assertionDescription;

		private AssertionInformation(String testAssertion, String assertionDescription) {
			this.testAssertion = testAssertion;
			this.assertionDescription = assertionDescription;
		}

		public String getTestAssertion() {
			return testAssertion;
		}

		public String getAssertionDescription() {
			return assertionDescription;
		}
	}
}
