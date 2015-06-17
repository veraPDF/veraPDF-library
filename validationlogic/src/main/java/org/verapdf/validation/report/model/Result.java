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
        int warnings = (details == null || details.getWarnings() == null) ? 0 : details.getWarnings().size();

        if (details != null) {
            for (Rule rule : details.getRules()) {
                if (rule != null) {
                    if ("passed".equals(rule.getAttrStatus())) {
                        ++passedRules;
                    } else {
                        compliantCheck = false;
                        ++failedRules;
                    }

                    for (Check check : rule.getChecks()) {
                        if (check != null) {
                            if ("passed".equals(check.getAttrStatus())) {
                                ++passedChecks;
                            } else {
                                ++failedChecks;
                            }
                        }
                    }
                }
            }
        }

        this.compliant = compliantCheck;
        this.statement = compliantCheck ? "PDF file is compliant with Validation Profile requirements" : "PDF file is not compliant with Validation Profile requirements";
        this.summary = new Summary(passedRules, failedRules, passedChecks, failedChecks, completedMetadataFixes, failedMetadataFixes, warnings);
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
