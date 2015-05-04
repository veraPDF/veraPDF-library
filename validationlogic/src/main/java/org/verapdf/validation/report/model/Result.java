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

    public Result(boolean compliant, String statement, Summary summary, Details details) {
        this.compliant = compliant;
        this.statement = statement;
        this.summary = summary;
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
