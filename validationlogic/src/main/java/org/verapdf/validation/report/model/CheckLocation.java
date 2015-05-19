package org.verapdf.validation.report.model;

/**
 * Structure of the location of a check.
 * Created by bezrukov on 5/4/15.
 *
 * @author Maksim Bezrukov
 * @version 1.0
 */
public class CheckLocation {
    private String attrLevel;
    private String context;

    public CheckLocation(String attrLevel, String context) {
        this.attrLevel = attrLevel;
        this.context = context;
    }

    /**
     * @return the level of the check location
     */
    public String getAttrLevel() {
        return attrLevel;
    }

    /**
     * @return list of edges' names which used for come to the checked object
     */
    public String getContext() {
        return context;
    }
}
