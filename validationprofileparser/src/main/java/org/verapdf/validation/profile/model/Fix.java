package org.verapdf.validation.profile.model;

/**
 * Structure of the fix in a rule.
 * Created by bezrukov on 4/24/15.
 *
 * @author Maksim Bezrukov
 * @version 1.0
 * @see Rule
 */
public class Fix {
    private String attrID;

    private String description;
    private FixInfo info;
    private FixError error;

    public Fix(String attrID, String description, FixInfo info, FixError error) {
        this.attrID = attrID;
        this.description = description;
        this.info = info;
        this.error = error;
    }

    /**
     * @return Text provided by attribute "id".
     */
    public String getAttrID() {
        return attrID;
    }

    /**
     * @return Text in tag "description".
     */
    public String getDescription() {
        return description;
    }

    /**
     * @return Class which represents an info in this fix.
     */
    public FixInfo getInfo() {
        return info;
    }

    /**
     * @return Class which represents an error in this fix.
     */
    public FixError getError() {
        return error;
    }
}
