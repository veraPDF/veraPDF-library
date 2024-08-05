/**
 * This file is part of veraPDF Library core, a module of the veraPDF project.
 * Copyright (c) 2015-2024, veraPDF Consortium <info@verapdf.org>
 * All rights reserved.
 *
 * veraPDF Library core is free software: you can redistribute it and/or modify
 * it under the terms of either:
 *
 * The GNU General public license GPLv3+.
 * You should have received a copy of the GNU General Public License
 * along with veraPDF Library core as the LICENSE.GPL file in the root of the source
 * tree.  If not, see http://www.gnu.org/licenses/ or
 * https://www.gnu.org/licenses/gpl-3.0.en.html.
 *
 * The Mozilla Public License MPLv2+.
 * You should have received a copy of the Mozilla Public License along with
 * veraPDF Library core as the LICENSE.MPL file in the root of the source tree.
 * If a copy of the MPL was not distributed with this file, you can obtain one at
 * http://mozilla.org/MPL/2.0/.
 */
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
//	ENDS_WITH(true, "ends with"),
	CONTAINS(true, "contains substring");

	private final boolean hasArguments;
	private final String description;

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
//			case ENDS_WITH:
//				test = "ends-with(" + feature.getFeatureXPath() + ",'" + argument + "')";
//				break;
			default:
				throw new IllegalStateException("Unsupported operation type");
		}
		String desc = "Failed check: " + feature.getFeatureName() + " " + this.getDescription();
		if (this.hasArguments) {
			desc += " " + argument;
		}
		return new AssertionInformation(test, desc);
	}

	public static class AssertionInformation {
		private final String testAssertion;
		private final String assertionDescription;

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
