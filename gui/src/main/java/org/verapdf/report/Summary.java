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
public class Summary {

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

    private Summary(int passedRules, int failedRules, int passedChecks,
            int failedChecks, String metadataFixesStatus,
            int completedMetadataFixes) {
        this.passedRules = passedRules;
        this.failedRules = failedRules;
        this.passedChecks = passedChecks;
        this.failedChecks = failedChecks;
        this.metadataFixesStatus = metadataFixesStatus;
        this.completedMetadataFixes = completedMetadataFixes;
    }

    private Summary() {
        this(0, 0, 0, 0, null, 0);
    }

    static Summary fromValues(ValidationResult result,
            String metadataFixesStatus, int completedMetadataFixes) {
        int passedChecks = 0;
        ;
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

        return new Summary(passedRules.size(), failedRules.size(),
                passedChecks, failedChecks, metadataFixesStatus,
                completedMetadataFixes);
    }
}
