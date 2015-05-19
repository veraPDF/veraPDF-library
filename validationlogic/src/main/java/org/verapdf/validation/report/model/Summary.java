package org.verapdf.validation.report.model;

/**
 * Structure of the summary of a result.
 * Created by bezrukov on 5/4/15.
 *
 * @author Maksim Bezrukov
 * @version 1.0
 */
public class Summary {
    private int attrPassedRules;
    private int attrFailedRules;
    private int attrPassedChecks;
    private int attrFailedChecks;
    private int attrCompletedMetadataFixes;
    private int attrFailedMetadataFixes;
    private int attrWarnings;

    public Summary(int attrPassedRules, int attrFailedRules, int attrPassedChecks, int attrFailedChecks, int attrCompletedMetadataFixes, int attrFailedMetadataFixes, int attrWarnings) {
        this.attrPassedRules = attrPassedRules;
        this.attrFailedRules = attrFailedRules;
        this.attrPassedChecks = attrPassedChecks;
        this.attrFailedChecks = attrFailedChecks;
        this.attrCompletedMetadataFixes = attrCompletedMetadataFixes;
        this.attrFailedMetadataFixes = attrFailedMetadataFixes;
        this.attrWarnings = attrWarnings;
    }

    /**
     * @return the number of passed rules.
     */
    public int getAttrPassedRules() {
        return attrPassedRules;
    }

    /**
     * @return the number of failed rules.
     */
    public int getAttrFailedRules() {
        return attrFailedRules;
    }

    /**
     * @return the number of passed checks.
     */
    public int getAttrPassedChecks() {
        return attrPassedChecks;
    }

    /**
     * @return the number of failed checks.
     */
    public int getAttrFailedChecks() {
        return attrFailedChecks;
    }

    /**
     * @return the number of completed metadata fixes.
     */
    public int getAttrCompletedMetadataFixes() {
        return attrCompletedMetadataFixes;
    }

    /**
     * @return the number of failed metadata fixes.
     */
    public int getAttrFailedMetadataFixes() {
        return attrFailedMetadataFixes;
    }

    /**
     * @return the number of warnings.
     */
    public int getAttrWarnings() {
        return attrWarnings;
    }
}
