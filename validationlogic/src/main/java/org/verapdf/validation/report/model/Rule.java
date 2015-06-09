package org.verapdf.validation.report.model;

import java.util.List;

/**
 * Structure of the rule check result.
 * Created by bezrukov on 5/4/15.
 *
 * @author Maksim Bezrukov
 * @version 1.0
 */
public class Rule {
    private String attrID;
    private String attrStatus;
    private int attrChecks;

    private List<Check> checks;

    /**
     * Creates rule model for the validation report
     * @param attrID - id of the rule
     * @param checks - list of performed checks of this rule
     */
    public Rule(String attrID, List<Check> checks) {
        this.attrID = attrID;

        String status = "passed";

        if (checks != null) {
            for (Check check : checks) {
                if (check != null && check.getAttrStatus().equals("failed")) {
                    status = "failed";
                }
            }
        }

        this.attrStatus = status;

        this.attrChecks = checks == null ? 0 : checks.size();
        this.checks = checks;
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
        return attrChecks;
    }

    /**
     * @return list of checks structure
     */
    public List<Check> getChecks() {
        return checks;
    }
}
