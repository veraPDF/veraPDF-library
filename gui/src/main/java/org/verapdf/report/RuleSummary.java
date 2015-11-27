package org.verapdf.report;

import org.verapdf.pdfa.results.TestAssertion;
import org.verapdf.pdfa.results.TestAssertion.Status;
import org.verapdf.pdfa.validation.Profiles;
import org.verapdf.pdfa.validation.RuleId;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Maksim Bezrukov
 */
@XmlRootElement
public class RuleSummary {
    private final Status ruleStatus;
    @XmlAttribute
    private final String specification;
    @XmlAttribute
    private final String clause;
    @XmlAttribute
    private final int testNumber;
    @XmlAttribute
    private String status;
    @XmlAttribute
    private final int passedChecks;
    @XmlAttribute
    private final int failedChecks;
    @XmlElement
    private final String description;
    @XmlElement(name = "check")
    private final Set<Check> checks;

    private RuleSummary(final RuleId ruleId, final Status status,
            final int passedChecks, final int failedChecks,
            final String description, final Set<Check> checks) {
        this.specification = ruleId.getSpecification().getId();
        this.clause = ruleId.getClause();
        this.testNumber = ruleId.getTestNumber();
        this.ruleStatus = status;
        this.status = status.toString();
        this.passedChecks = passedChecks;
        this.failedChecks = failedChecks;
        this.description = (status != Status.PASSED) ? description : null;
        this.checks = ((checks != null) && !checks.isEmpty()) ? new HashSet<>(checks) : null;
    }

    private RuleSummary(final RuleId ruleId, final Status status,
            final String description) {
        this(ruleId, status, 0, 0, description, Collections.<Check> emptySet());
    }

    private RuleSummary() {
        this(Profiles.defaultRuleId(), Status.UNKNOWN, "description");
    }
    
    /**
     * @return the {@link Status} of the rule
     */
    public Status getRuleStatus() {
        return this.ruleStatus;
    }
    
    /**
     * @return the number of passed checks for the rule
     */
    public int getPassedChecks() {
        return this.passedChecks;
    }

    /**
     * @return the number of failed checks for the rule
     */
    public int getFailedChecks() {
        return this.failedChecks;
    }

    static RuleSummary fromValues(final RuleId id, final String description,
                                  Set<TestAssertion> assertions, boolean logPassedChecks, int maxNumberOfDisplayedFailedChecks) {
        if (id == null) {
            throw new NullPointerException("Argument id can not be null");
        }
        if (description == null) {
            throw new NullPointerException(
                    "Argument description can not be null");
        }
        if (assertions == null) {
            throw new NullPointerException(
                    "Argument assertions can not be null");
        }
        Set<Check> checks = new HashSet<>();
        Status status = Status.PASSED;
        int passedChecks = 0;
        int failedChecks = 0;
        for (TestAssertion assertion : assertions) {
            if (assertion.getStatus() == Status.PASSED) {
                passedChecks++;
                if (logPassedChecks)
                    checks.add(Check.fromValue(assertion));
            } else {
                status = assertion.getStatus();
                failedChecks++;
                if ((maxNumberOfDisplayedFailedChecks == -1) || (failedChecks <= maxNumberOfDisplayedFailedChecks)) {
                    checks.add(Check.fromValue(assertion));
                }
            }
        }
        return new RuleSummary(id, status, passedChecks, failedChecks,
                description, checks);
    }

    static RuleSummary uncheckedInstance(final RuleId id,
            final String description) {
        if (id == null) {
            throw new NullPointerException("Argument id can not be null");
        }
        if (description == null) {
            throw new NullPointerException(
                    "Argument description can not be null");
        }
        return new RuleSummary(id, Status.PASSED, description);
    }
}
