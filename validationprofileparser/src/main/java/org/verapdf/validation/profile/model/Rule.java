package org.verapdf.validation.profile.model;

import java.util.List;

/**
 * Structure of the rule in a validation profile.
 * Created by bezrukov on 4/24/15.
 *
 * @author Maksim Bezrukov
 * @version 1.0
 * @see ValidationProfile
 */
public class Rule {
    private String attrID;
    private String attrObject;

    private String description;
    private String test;
    private RuleError ruleError;
    private boolean isHasError;
    private Reference reference;
    private List<Fix> fix;

    /**
     * Creates new rule model.
     * @param attrID - id of the rule
     * @param attrObject - name of the object to which this rule applied
     * @param description - description of the rule
     * @param ruleError - rule error
     * @param isHasError - is the rule error of type error or warning
     * @param test - test of the rule as JavaScript context
     * @param reference - reference of the rule
     * @param fix - list of fixes for this rule
     */
    public Rule(String attrID, String attrObject, String description, RuleError ruleError, boolean isHasError, String test, Reference reference, List<Fix> fix) {
        this.attrID = attrID;
        this.attrObject = attrObject;
        this.description = description;
        this.ruleError = ruleError;
        this.isHasError = isHasError;
        this.test = test;
        this.reference = reference;
        this.fix = fix;
    }

    /**
     * @return Text provided by attribute "id".
     */
    public String getAttrID() {
        return attrID;
    }

    /**
     * @return Text provided by attribute "object".
     */
    public String getAttrObject() {
        return attrObject;
    }

    /**
     * @return Text in tag "description".
     */
    public String getDescription() {
        return description;
    }

    /**
     * @return Text in tag "test".
     */
    public String getTest() {
        return test;
    }

    /**
     * @return Class which represents an error/warning in this rule.
     */
    public RuleError getRuleError() {
        return ruleError;
    }

    /**
     * Get what {@code ruleError} (if {@code this} rule has it) represents: an error or a warning.
     * @return true if {@code ruleError} represents an error, and false if {@code ruleError} represents a warning (or null).
     */
    public boolean isHasError() {
        return isHasError;
    }

    /**
     * @return Class which represents a reference in this rule.
     */
    public Reference getReference() {
        return reference;
    }

    /**
     * @return List of classes which represents fixes in this rule.
     */
    public List<Fix> getFix() {
        return fix;
    }
}
