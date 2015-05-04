package org.verapdf.validation.report.model;

/**
 * Structure of the location of a check.
 * Created by bezrukov on 5/4/15.
 *
 * @author Maksim Bezrukov
 * @version 1.0
 */
public class CheckLocation {
    private String attr_level;

    public CheckLocation(String attr_level) {
        this.attr_level = attr_level;
    }

    /**
     * @return the level of the check location
     */
    public String getAttr_level() {
        return attr_level;
    }
}
