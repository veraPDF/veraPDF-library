package org.verapdf.validation.report.model;

import java.util.List;

/**
 * Structure of the details about the performed checks.
 *
 * @author Maksim Bezrukov
 */
public class Details {
    private List<Rule> rules;
    private List<String> warnings;

    /**
     * Creates Details model
     *
     * @param rules    list of checked rules
     * @param warnings - list of warnings
     */
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
