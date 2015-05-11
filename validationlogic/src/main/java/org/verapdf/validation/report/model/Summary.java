package org.verapdf.validation.report.model;

/**
 * Structure of the summary of a result.
 * Created by bezrukov on 5/4/15.
 *
 * @author Maksim Bezrukov
 * @version 1.0
 */
public class Summary {
    private int attr_passedRules;
    private int attr_failedRules;
    private int attr_passedChecks;
    private int attr_failedChecks;
    private int attr_completedMetadataFixes;
    private int attr_failedMetadataFixes;
    private int attr_warnings;

    public Summary(int attr_passedRules, int attr_failedRules, int attr_passedChecks, int attr_failedChecks, int attr_completedMetadataFixes, int attr_failedMetadataFixes, int attr_warnings) {
        this.attr_passedRules = attr_passedRules;
        this.attr_failedRules = attr_failedRules;
        this.attr_passedChecks = attr_passedChecks;
        this.attr_failedChecks = attr_failedChecks;
        this.attr_completedMetadataFixes = attr_completedMetadataFixes;
        this.attr_failedMetadataFixes = attr_failedMetadataFixes;
        this.attr_warnings = attr_warnings;
    }

    /**
     * @return the number of passed rules.
     */
    public int getAttr_passedRules() {
        return attr_passedRules;
    }

    /**
     * @return the number of failed rules.
     */
    public int getAttr_failedRules() {
        return attr_failedRules;
    }

    /**
     * @return the number of passed checks.
     */
    public int getAttr_passedChecks() {
        return attr_passedChecks;
    }

    /**
     * @return the number of failed checks.
     */
    public int getAttr_failedChecks() {
        return attr_failedChecks;
    }

    /**
     * @return the number of completed metadata fixes.
     */
    public int getAttr_completedMetadataFixes() {
        return attr_completedMetadataFixes;
    }

    /**
     * @return the number of failed metadata fixes.
     */
    public int getAttr_failedMetadataFixes() {
        return attr_failedMetadataFixes;
    }

    /**
     * @return the number of warnings.
     */
    public int getAttr_warnings() {
        return attr_warnings;
    }
}
