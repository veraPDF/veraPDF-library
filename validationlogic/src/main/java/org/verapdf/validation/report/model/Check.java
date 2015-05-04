package org.verapdf.validation.report.model;

/**
 * Structure of the check of the rule.
 * Created by bezrukov on 5/4/15.
 *
 * @author Maksim Bezrukov
 * @version 1.0
 */
public class Check {
    private String attr_status;

    private CheckLocation location;

    public Check(String attr_status, CheckLocation location) {
        this.attr_status = attr_status;
        this.location = location;
    }

    /**
     * @return status (passed/failed) of the check
     */
    public String getAttr_status() {
        return attr_status;
    }

    /**
     * @return location structure of the check
     */
    public CheckLocation getLocation() {
        return location;
    }
}
