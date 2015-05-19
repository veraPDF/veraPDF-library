package org.verapdf.validation.report.model;

/**
 * Structure of the check of the rule.
 * Created by bezrukov on 5/4/15.
 *
 * @author Maksim Bezrukov
 * @version 1.0
 */
public class Check {
    private String attrStatus;

    private CheckLocation location;
    private CheckError error;
    private boolean isHasError;

    public Check(String attrStatus, CheckLocation location, CheckError error, boolean isHasError) {
        this.attrStatus = attrStatus;
        this.location = location;
        this.error = error;
        this.isHasError = isHasError;
    }

    /**
     * @return status (passed/failed) of the check
     */
    public String getAttrStatus() {
        return attrStatus;
    }

    /**
     * @return location structure of the check
     */
    public CheckLocation getLocation() {
        return location;
    }

    /**
     * @return an error/warning message
     */
    public CheckError getError() {
        return error;
    }

    /**
     * Get what {@code error} (if {@code this} check has it) represents: an error or a warning.
     * @return true if {@code error} represents an error, and false if {@code error} represents a warning (or null).
     */
    public boolean isHasError() {
        return isHasError;
    }
}
