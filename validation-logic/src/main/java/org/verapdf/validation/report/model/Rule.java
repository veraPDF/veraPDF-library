package org.verapdf.validation.report.model;

import java.util.Collections;
import java.util.List;

/**
 * Structure of the rule check result.
 *
 * @author Maksim Bezrukov
 */
public class Rule {
    private static final String PASSED = "passed";
    private static final String FAILED = "failed";
    private final String attrID;
    private final String attrStatus;
    private final List<Check> checks;

    /**
     * Creates rule model for the validation report
     *
     * @param attrID - id of the rule
     * @param checks - list of performed checks of this rule
     */
    public Rule(final String attrID, final List<Check> checks) {
        this.attrID = attrID;

        String status = PASSED;

        if (checks != null) {
            for (Check check : checks) {
                if (check != null && FAILED.equals(check.getAttrStatus())) {
                    status = FAILED;
                }
            }
        }

        this.attrStatus = status;

        if (checks == null) {
            this.checks = Collections.emptyList();
        } else {
            this.checks =  Collections.unmodifiableList(checks);
        }
    }

    /**
     * @return id of the rule
     */
    public String getAttrID() {
        return attrID;
    }

    /**
     * @return actual status (passed/failed) of the rule
     */
    public String getAttrStatus() {
        return attrStatus;
    }

    /**
     * @return number of checks for this rule
     */
    public int getAttrChecks() {
        return this.checks.size();
    }

    /**
     * @return list of checks structure
     */
    public List<Check> getChecks() {
        return checks;
    }
}
