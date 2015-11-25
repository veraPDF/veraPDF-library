package org.verapdf.report;

import org.verapdf.pdfa.results.TestAssertion;
import org.verapdf.pdfa.validation.RuleId;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Maksim Bezrukov
 */
@XmlRootElement
public class RuleSummary {

    @XmlAttribute
    private final String specification;
    @XmlAttribute
    private final String clause;
    @XmlAttribute
    private final String testNumber;
    @XmlAttribute
    private TestAssertion.Status status;
    @XmlAttribute(name = "checks")
    private final int totalChecks;
    @XmlElement
    private final String description;
    @XmlElementWrapper
    @XmlElement(name = "check")
    private final Set<Check> checks;

    private RuleSummary(final String specification, final String clause,
            final String testNumber, final TestAssertion.Status status,
            final int totalChecks, final String description,
            final Set<Check> checks) {
        this.specification = specification;
        this.clause = clause;
        this.testNumber = testNumber;
        this.status = status;
        this.totalChecks = totalChecks;
        this.description = description;
        this.checks = new HashSet<>(checks);
    }

    private RuleSummary() {
        this("", "", "", TestAssertion.Status.PASSED, 0, "",
                new HashSet<Check>());
    }

    static RuleSummary fromValues(RuleId id, String description,
            TestAssertion.Status status, int totalChecks) {
        if (id == null) {
            throw new IllegalArgumentException("Argument id con not be null");
        }
        if (status == null) {
            throw new IllegalArgumentException(
                    "Argument status con not be null");
        }
        if (description == null) {
            throw new IllegalArgumentException(
                    "Argument description con not be null");
        }

        return new RuleSummary(id.getSpecification().getId(), id.getClause(),
                String.valueOf(id.getTestNumber()), status, totalChecks,
                description, new HashSet<Check>());
    }

    boolean addCheck(Check check) {
        if (check == null) {
            throw new IllegalArgumentException("Argument check con not be null");
        }
        return this.checks.add(check);
    }

    void setStatus(TestAssertion.Status status) {
        this.status = status;
    }
}
