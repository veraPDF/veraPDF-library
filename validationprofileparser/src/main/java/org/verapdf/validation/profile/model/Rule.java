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
    private String attr_id;
    private String attr_object;

    private String description;
    private String test;
    private RuleError ruleError;
    private boolean isHasError;
    private Reference reference;
    private List<Fix> fix;

    public Rule(String attr_id, String attr_object, String description, RuleError ruleError, boolean isHasError, String test, Reference reference, List<Fix> fix) {
        this.attr_id = attr_id;
        this.attr_object = attr_object;
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
    public String getAttr_id() {
        return attr_id;
    }

    /**
     * @return Text provided by attribute "object".
     */
    public String getAttr_object() {
        return attr_object;
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
