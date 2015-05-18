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

    public Rule(String attrID, List<Check> checks) {
        this.attrID = attrID;

        String status = "passed";

        for (Check check : checks){
            if (check.getAttrStatus().equals("failed")){
                status = "failed";
            }
        }

        this.attrStatus = status;

        this.attrChecks = checks.size();
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
