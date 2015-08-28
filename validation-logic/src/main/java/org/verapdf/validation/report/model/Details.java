package org.verapdf.validation.report.model;

import java.util.Collections;
import java.util.List;

/**
 * Structure of the details about the performed checks.
 *
 * @author Maksim Bezrukov
 */
public class Details {

    private final List<Rule> rules;
    private final List<String> warnings;
	private final int rulesChecksCount;

    /**
     * Creates Details model
     * @param rules			   list of checked rules
     * @param warnings  	   list of warnings
	 * @param rulesChecksCount count of all checks
	 */
    public Details(final List<Rule> rules, final List<String> warnings, int rulesChecksCount) {
        this.rules = Collections.unmodifiableList(rules);
        this.warnings = Collections.unmodifiableList(warnings);
		this.rulesChecksCount = rulesChecksCount;
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

	/**
	 * @return count of all checks
	 */
	public int getRulesChecksCount() {
		return rulesChecksCount;
	}
}
