package org.verapdf.validation.profile.model;

import java.util.Collections;
import java.util.List;

/**
 * Structure of the rule in a validation profile.
 *
 * @author Maksim Bezrukov
 */
public final class Rule {
	private final String attrID;
	private final String attrObject;
	private final String description;
	private final String test;
	private final RuleError ruleError;
	private final Reference reference;
	private final List<Fix> fixes;

	/**
	 * Creates new rule model.
	 *
	 * @param attrID      id of the rule, can NOT be null
	 * @param attrObject  name of the object to which this rule applied
	 * @param description description of the rule
	 * @param ruleError   rule error
	 * @param test        test of the rule as JavaScript context
	 * @param reference   reference of the rule
	 * @param fixes       list of fixes for this rule
	 * @throws IllegalArgumentException if the supplied rule ID is null
	 */
	public Rule(String attrID, String attrObject, String description,
				RuleError ruleError, String test,
				Reference reference, List<Fix> fixes) {
		if (attrID == null)
			throw new IllegalArgumentException(
					"Rule ID attrID can not be null.");
		this.attrID = attrID;
		this.attrObject = attrObject;
		this.description = description;
		this.ruleError = ruleError;
		this.test = test;
		this.reference = reference;
		if (fixes == null) {
			this.fixes = Collections.emptyList();
		} else {
			this.fixes = Collections.unmodifiableList(fixes);
		}
	}

	/**
	 * @return Text provided by attribute "id".
	 */
	public String getAttrID() {
		return this.attrID;
	}

	/**
	 * @return Text provided by attribute "object".
	 */
	public String getAttrObject() {
		return this.attrObject;
	}

	/**
	 * @return Text in tag "description".
	 */
	public String getDescription() {
		return this.description;
	}

	/**
	 * @return Text in tag "test".
	 */
	public String getTest() {
		return this.test;
	}

	/**
	 * @return Class which represents an error/warning in this rule.
	 */
	public RuleError getRuleError() {
		return this.ruleError;
	}

	/**
	 * @return Class which represents a reference in this rule.
	 */
	public Reference getReference() {
		return this.reference;
	}

	/**
	 * @return List of classes which represents fixes in this rule.
	 */
	public List<Fix> getFixes() {
		return this.fixes;
	}

	/**
	 * { @inheritDoc }
	 */
	@Override
	public String toString() {
		return "Rule [attrID=" + this.attrID + ", attrObject="
				+ this.attrObject + ", description=" + this.description
				+ ", test=" + this.test + ", ruleError=" + this.ruleError
				+ ", reference=" + this.reference + ", fixes=" + this.fixes
				+ "]";
	}

	/**
	 * { @inheritDoc }
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + this.attrID.hashCode();
		result = prime * result
				+ ((this.attrObject == null) ? 0 : this.attrObject.hashCode());
		result = prime
				* result
				+ ((this.description == null) ? 0 : this.description.hashCode());
		result = prime * result + this.fixes.hashCode();
		result = prime * result
				+ ((this.reference == null) ? 0 : this.reference.hashCode());
		result = prime * result
				+ ((this.ruleError == null) ? 0 : this.ruleError.hashCode());
		result = prime * result
				+ ((this.test == null) ? 0 : this.test.hashCode());
		return result;
	}

	/**
	 * { @inheritDoc }
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		} else if (obj == null) {
			return false;
		} else if (getClass() != obj.getClass()) {
			return false;
		}

		Rule other = (Rule) obj;
		if (!this.attrID.equals(other.attrID))
			return false;
		if (this.attrObject == null) {
			if (other.attrObject != null) {
				return false;
			}
		} else if (!this.attrObject.equals(other.attrObject)) {
			return false;
		}
		if (this.description == null) {
			if (other.description != null) {
				return false;
			}
		} else if (!this.description.equals(other.description)) {
			return false;
		}
		if (this.test == null) {
			if (other.test != null)
				return false;
		} else if (!this.test.equals(other.test))
			return false;
		if (!isEquals(other)) {
			return false;
		}
		if (this.reference == null) {
			if (other.reference != null) {
				return false;
			}
		} else if (!this.reference.equals(other.reference)) {
			return false;
		}
		if (this.ruleError == null) {
			if (other.ruleError != null) {
				return false;
			}
		} else if (!this.ruleError.equals(other.ruleError)) {
			return false;
		}
		return true;
	}

	private boolean isEquals(Rule other) {
		return this.fixes.size() == other.fixes.size() &&
				this.fixes.containsAll(other.fixes);
	}
}
