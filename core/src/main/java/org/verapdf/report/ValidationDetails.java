package org.verapdf.report;

import org.verapdf.pdfa.results.TestAssertion;
import org.verapdf.pdfa.results.TestAssertion.Status;
import org.verapdf.pdfa.validation.profiles.Profiles;
import org.verapdf.pdfa.validation.profiles.Rule;
import org.verapdf.pdfa.validation.profiles.RuleId;
import org.verapdf.pdfa.validation.profiles.ValidationProfile;
import org.verapdf.pdfa.results.ValidationResult;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Maksim Bezrukov
 */
public class ValidationDetails {

    @XmlAttribute
    private final int passedRules;
    @XmlAttribute
    private final int failedRules;
    @XmlAttribute
    private final int passedChecks;
    @XmlAttribute
    private final int failedChecks;
    @XmlElement(name = "rule")
    private final Set<RuleSummary> ruleSummaries;

    private ValidationDetails(final int passedRules, final int failedRules,
                              final int passedChecks, final int failedChecks,
                              final Set<RuleSummary> ruleSummaries) {
        this.passedRules = passedRules;
        this.failedRules = failedRules;
        this.passedChecks = passedChecks;
        this.failedChecks = failedChecks;
        this.ruleSummaries = new HashSet<>(ruleSummaries);
    }

    private ValidationDetails() {
        this(0, 0, 0, 0, new HashSet<RuleSummary>());
    }

    public int getPassedChecks() {
    	return this.passedChecks;
    }
    
    public int getFailedChecks() {
    	return this.failedChecks;
    }
    
    static ValidationDetails fromValues(final ValidationResult result, boolean logPassedChecks,
                                        final int maxNumberOfDisplayedFailedChecks) {
    	ValidationProfile profile = Profiles.getVeraProfileDirectory().getValidationProfileByFlavour(result.getPDFAFlavour());
        Map<RuleId, Set<TestAssertion>> assertionMap = mapAssertionsByRule(result
                .getTestAssertions());
        Set<RuleSummary> ruleSummaries = new HashSet<>();
        int passedRules = 0;
        int passedChecks = 0;
        int failedRules = 0;
        int failedChecks = 0;
        for (Rule rule : profile.getRules()) {
            RuleSummary summary = RuleSummary.uncheckedInstance(
                    rule.getRuleId(), rule.getDescription(), rule.getObject(), rule.getTest());
            if (assertionMap.containsKey(rule.getRuleId())) {
                summary = RuleSummary.fromValues(rule.getRuleId(),
                        rule.getDescription(), rule.getObject(), rule.getTest(),
                        assertionMap.get(rule.getRuleId()), logPassedChecks,
                        maxNumberOfDisplayedFailedChecks);
            }
            passedChecks += summary.getPassedChecks();
            failedChecks += summary.getFailedChecks();
            if (summary.getRuleStatus() == Status.PASSED) {
                passedRules++;
                if (logPassedChecks)
                    ruleSummaries.add(summary);
            } else {
                failedRules++;
                ruleSummaries.add(summary);
            }
        }

        return new ValidationDetails(passedRules, failedRules, passedChecks,
                failedChecks, ruleSummaries);
    }

    private static Map<RuleId, Set<TestAssertion>> mapAssertionsByRule(
            final Set<TestAssertion> assertions) {
        Map<RuleId, Set<TestAssertion>> assertionMap = new HashMap<>();
        for (TestAssertion assertion : assertions) {
            if (assertionMap.containsKey(assertion.getRuleId())) {
                assertionMap.get(assertion.getRuleId()).add(assertion);
            } else {
                Set<TestAssertion> assertionSet = new HashSet<>();
                assertionSet.add(assertion);
                assertionMap.put(assertion.getRuleId(), assertionSet);
            }
        }

        return assertionMap;
    }
}
