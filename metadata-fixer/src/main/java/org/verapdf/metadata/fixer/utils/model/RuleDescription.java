package org.verapdf.metadata.fixer.utils.model;

/**
 * @author Evgeniy Muravitskiy
 */
public class RuleDescription {

	private final String objectType;
	private final String test;

	public RuleDescription(String test, String objectType) {
		this.test = test;
		this.objectType = objectType;
	}

	public String getObjectType() {
		return objectType;
	}

	public String getTest() {
		return test;
	}

	public boolean compareTo(String objectType, String test) {
		return this.objectType.equals(objectType) && this.test.equals(test);
	}
}
