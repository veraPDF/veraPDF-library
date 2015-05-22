package org.verapdf.validation.report.model;

/**
 * Structure of the result of the validation.
 * Created by bezrukov on 5/4/15.
 *
 * @author Maksim Bezrukov
 * @version 1.0
 */
public class Result {
    private boolean compliant;
    private String statement;
    private Summary summary;
    private Details details;

    /**
     * Creates result model by given details
     * @param details - details model of the validation
     */
    public Result(Details details) {
        boolean compliantCheck = true;
        int passedRules = 0;
        int failedRules = 0;
        int passedChecks = 0;
        int failedChecks = 0;
        int completedMetadataFixes = 0;
        int failedMetadataFixes = 0;
        int warnings = details.getWarnings().size();

        for(Rule rule : details.getRules()){
            if (rule.getAttrStatus().equals("passed")){
                ++passedRules;
            }
            else {
                compliantCheck = false;
                ++failedRules;
            }
            for(Check check : rule.getChecks()){
                if (check.getAttrStatus().equals("passed")){
                    ++passedChecks;
                }
                else {
                    ++failedChecks;
                }
            }
        }

        Summary newSummary = new Summary(passedRules, failedRules, passedChecks, failedChecks, completedMetadataFixes, failedMetadataFixes, warnings);

        this.compliant = compliantCheck;
        this.statement = compliantCheck ? "Validation is successful! Everything is correct!" : "Validation failed. Something is wrong.";
        this.summary = newSummary;
        this.details = details;
    }

    /**
     * @return The resolution if the PDF Document is compliant.
     */
    public boolean isCompliant() {
        return compliant;
    }

    /**
     * @return Textual statement indicating validation result.
     */
    public String getStatement() {
        return statement;
    }

    /**
     * @return Structure of the brief summary of all the performed checks and fixes.
     */
    public Summary getSummary() {
        return summary;
    }

    /**
     * @return Structure of the details about performed checks of the validation rules.
     */
    public Details getDetails() {
        return details;
    }
}
