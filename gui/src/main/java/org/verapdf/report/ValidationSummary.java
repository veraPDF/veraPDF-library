package org.verapdf.report;

import java.util.HashSet;
import java.util.Set;

import javax.xml.bind.annotation.XmlAttribute;

import org.verapdf.pdfa.results.TestAssertion;
import org.verapdf.pdfa.results.TestAssertion.Status;
import org.verapdf.pdfa.results.ValidationResult;
import org.verapdf.pdfa.validation.RuleId;

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

    private ValidationSummary(final int passedRules, final int failedRules, final int passedChecks,
            final int failedChecks, final String metadataFixesStatus,
            final int completedMetadataFixes) {
        this.passedRules = passedRules;
        this.failedRules = failedRules;
        this.passedChecks = passedChecks;
        this.failedChecks = failedChecks;
        this.metadataFixesStatus = metadataFixesStatus;
        this.completedMetadataFixes = completedMetadataFixes;
    }

    private ValidationSummary() {
        this(0, 0, 0, 0, "", 0);
    }

    static ValidationSummary fromValues(final ValidationResult result,
            final String metadataFixesStatus, final int completedMetadataFixes) {
        int passedChecks = 0;
        int failedChecks = 0;
        Set<RuleId> passedRules = new HashSet<>();
        Set<RuleId> failedRules = new HashSet<>();
        for (TestAssertion assertion : result.getTestAssertions()) {
            if (assertion.getStatus() == Status.PASSED) {
                passedChecks++;
                passedRules.add(assertion.getRuleId());
            } else {
                failedChecks++;
                failedRules.add(assertion.getRuleId());
            }
        }

        return new ValidationSummary(passedRules.size(), failedRules.size(),
                passedChecks, failedChecks, metadataFixesStatus,
                completedMetadataFixes);
    }
}
