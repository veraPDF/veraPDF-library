package org.verapdf.validation.profile.model;

/**
 * Structure of the variable in a validation profile.
 *
 * @author Maksim Bezrukov
 */
public class Variable {
	private final String attrName;
	private final String attrObject;
	private final String defaultValue;
	private final String value;

	/**
	 * Creates variable model.
	 *
	 * @param attrName
	 * @param attrObject
	 * @param defaultValue
	 * @param value
	 */
	public Variable(final String attrName, final String attrObject, final String defaultValue, final String value) {
		this.attrName = attrName;
		this.attrObject = attrObject;
		this.defaultValue = defaultValue;
		this.value = value;
	}

	public String getAttrName() {
		return attrName;
	}

	public String getAttrObject() {
		return attrObject;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public String getValue() {
		return value;
	}
}
