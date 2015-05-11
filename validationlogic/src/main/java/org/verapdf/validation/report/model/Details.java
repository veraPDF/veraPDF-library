package org.verapdf.validation.report.model;

import java.util.List;

/**
 * Structure of the details about the performed checks.
 * Created by bezrukov on 5/4/15.
 *
 * @author Maksim Bezrukov
 * @version 1.0
 */
public class Details {
    private List<Rule> rules;
    private List<String> warnings;

    public Details(List<Rule> rules, List<String> warnings) {
        this.rules = rules;
        this.warnings = warnings;
    }

    /**
     * @return list of all checked rulles
     */
    public List<Rule> getRules() {
        return rules;
    }

    /**
     * @return list of all warnings
     */
    public List<String> getWarnings() {
        return warnings;
    }
}
