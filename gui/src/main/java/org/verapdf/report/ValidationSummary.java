package org.verapdf.report;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

import org.verapdf.pdfa.MetadataFixerResult;
import org.verapdf.pdfa.MetadataFixerResult.RepairStatus;
import org.verapdf.pdfa.results.TestAssertion;
import org.verapdf.pdfa.results.TestAssertion.Status;
import org.verapdf.pdfa.results.ValidationResult;
import org.verapdf.pdfa.validation.Rule;
import org.verapdf.pdfa.validation.RuleId;
import org.verapdf.pdfa.validation.ValidationProfile;

/**
 * @author Maksim Bezrukov
 */
public class ValidationSummary {

    @XmlAttribute
    private final int passedRules;
    @XmlAttribute
    private final int failedRules;
    @XmlAttribute
    private final int passedChecks;
    @XmlAttribute
    private final int failedChecks;
    @XmlAttribute
    private final String metadataFixesStatus;
    @XmlAttribute
    private final int completedMetadataFixes;
    @XmlElement(name = "rule")
    private final Set<RuleSummary> ruleSummaries;

    private ValidationSummary(final int passedRules, final int failedRules,
            final int passedChecks, final int failedChecks,
            final Set<RuleSummary> ruleSummaries,
            final String metadataFixesStatus, final int completedMetadataFixes) {
        this.passedRules = passedRules;
        this.failedRules = failedRules;
        this.passedChecks = passedChecks;
        this.failedChecks = failedChecks;
        this.ruleSummaries = new HashSet<>(ruleSummaries);
        this.metadataFixesStatus = metadataFixesStatus;
        this.completedMetadataFixes = completedMetadataFixes;
    }

    private ValidationSummary() {
        this(0, 0, 0, 0, new HashSet<RuleSummary>(), "", 0);
    }

    static ValidationSummary fromValues(final ValidationProfile profile,
            final ValidationResult result, boolean logPassedChecks,
            final MetadataFixerResult metadataResult) {
        return fromValues(profile, result, logPassedChecks, metadataResult
                .getRepairStatus().toString(), metadataResult.getAppliedFixes()
                .size());
    }

    static ValidationSummary fromValues(final ValidationProfile profile,
            final ValidationResult result, boolean logPassedChecks) {
        return fromValues(profile, result, logPassedChecks,
                RepairStatus.NO_ACTION.toString(), 0);
    }

    static ValidationSummary fromValues(final ValidationProfile profile,
            final ValidationResult result, boolean logPassedChecks,
            final String repairStatus, final int fixes) {

        Map<RuleId, Set<TestAssertion>> assertionMap = mapAssertionsByRule(result
                .getTestAssertions());
        Set<RuleSummary> ruleSummaries = new HashSet<>();
        int passedRules = 0;
        int passedChecks = 0;
        int failedRules = 0;
        int failedChecks = 0;
        for (Rule rule : profile.getRules()) {
            RuleSummary summary = RuleSummary.uncheckedInstance(
                    rule.getRuleId(), rule.getDescription());
            if (assertionMap.containsKey(rule.getRuleId())) {
                summary = RuleSummary.fromValues(rule.getRuleId(),
                        rule.getDescription(),
                        assertionMap.get(rule.getRuleId()), logPassedChecks);
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

        return new ValidationSummary(passedRules, failedRules, passedChecks,
                failedChecks, ruleSummaries, repairStatus, fixes);
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
