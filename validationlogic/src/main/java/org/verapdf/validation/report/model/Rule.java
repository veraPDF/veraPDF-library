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
    private String attr_id;
    private String status;
    private int attr_checks;

    private List<Check> checks;

    public Rule(String attr_id, List<Check> checks) {
        this.attr_id = attr_id;

        String status = "passed";

        for (Check check : checks){
            if (check.getAttr_status().equals("failed")){
                status = "failed";
            }
        }

        this.status = status;

        this.attr_checks = checks.size();
        this.checks = checks;
    }

    /**
     * @return id of the rule
     */
    public String getAttr_id() {
        return attr_id;
    }

    /**
     * @return status (passed/failed) of the rule
     */
    public String getStatus() {
        return status;
    }

    /**
     * @return number of checks for this rule
     */
    public int getAttr_checks() {
        return attr_checks;
    }

    /**
     * @return list of checks structure
     */
    public List<Check> getChecks() {
        return checks;
    }
}
