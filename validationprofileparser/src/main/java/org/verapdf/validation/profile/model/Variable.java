package org.verapdf.validation.profile.model;

import java.util.List;

/**
 * Structure of the variable in a validation profile.
 * Created by bezrukov on 6/17/15.
 *
 * @author Maksim Bezrukov
 * @version 1.0
 * @see ValidationProfile
 */
public class Variable {
    private String attrName;
    private String attrObject;

    private String defaultValue;
    private String value;

    /**
     * Creates variable model.
     * @param attrName
     * @param attrObject
     * @param defaultValue
     * @param value
     */
    public Variable(String attrName, String attrObject, String defaultValue, String value) {
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
