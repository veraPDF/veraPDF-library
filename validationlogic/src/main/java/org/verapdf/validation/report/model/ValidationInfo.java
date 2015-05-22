package org.verapdf.validation.report.model;

/**
 * Structure of the validation info part of the report.
 * Created by bezrukov on 5/4/15.
 *
 * @author Maksim Bezrukov
 * @version 1.0
 */
public class ValidationInfo {
    private Profile profile;
    private Result result;

    /**
     * Creates model of validation report
     * @param profile - model of an information about used validation profile
     * @param result - model of the result of the validation
     */
    public ValidationInfo(Profile profile, Result result) {
        this.profile = profile;
        this.result = result;
    }

    /**
     * @return Class which represents an information about a profile which used in this validation info.
     */
    public Profile getProfile() {
        return profile;
    }

    /**
     * @return Class which represents a result of validation.
     */
    public Result getResult() {
        return result;
    }
}
