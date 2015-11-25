package org.verapdf.report;

import static org.verapdf.pdfa.MetadataFixerResult.RepairStatus.ID_REMOVED;
import static org.verapdf.pdfa.MetadataFixerResult.RepairStatus.SUCCESS;
import static org.verapdf.pdfa.results.TestAssertion.Status.FAILED;
import static org.verapdf.pdfa.results.TestAssertion.Status.PASSED;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import org.verapdf.pdfa.MetadataFixerResult;
import org.verapdf.pdfa.results.TestAssertion;
import org.verapdf.pdfa.results.ValidationResult;
import org.verapdf.pdfa.results.ValidationResults;
import org.verapdf.pdfa.validation.RuleId;

/**
 * @author Maksim Bezrukov
 */
@XmlRootElement(name = "result")
public class ValidationReport {
    private final static String STATEMENT_PREFIX = "PDF file is ";
    private final static String NOT_INSERT = "not ";
    private final static String STATEMENT_SUFFIX = "compliant with Validation Profile requirements.";
    private final static String COMPLIANT_STATEMENT = STATEMENT_PREFIX + STATEMENT_SUFFIX;
    private final static String NONCOMPLIANT_STATEMENT = STATEMENT_PREFIX + NOT_INSERT + STATEMENT_SUFFIX;

	@XmlElement
	private final boolean compliant;
	@XmlElement
	private final String statement;
	@XmlElement
	private final ValidationSummary summary;
    @XmlElementWrapper
    @XmlElement(name = "rule")
    private final Set<RuleSummary> rules;
//  @XmlElementWrapper
//  @XmlElement(name = "warning")
//  private final Set<Warning> warnings;

	private ValidationReport(boolean compliant, String statement, ValidationSummary summary, Set<RuleSummary> rules) {
		this.compliant = compliant;
		this.statement = statement;
		this.summary = summary;
		this.rules = new HashSet<>(rules);
	}

	private ValidationReport() {
		this(false, "", ValidationSummary.fromValues(ValidationResults.defaultResult(), null, 0), new HashSet<RuleSummary>());
	}

	static ValidationReport fromValues(ValidationResult info, MetadataFixerResult fixerResult) {

		Set<TestAssertion> assertions = info.getTestAssertions();
		String statement = getStatement(info.isCompliant());

		Set<RuleSummary> rules = getRules(assertions);

		String fixerResultStatus = "";
		int completedFixes = 0;

		if (fixerResult != null) {
			MetadataFixerResult.RepairStatus repairStatus = fixerResult.getRepairStatus();
			fixerResultStatus = repairStatus.getName();
			if (SUCCESS == repairStatus
					|| ID_REMOVED == repairStatus) {
				completedFixes = fixerResult.getAppliedFixes().size();
			}
		}

		return new ValidationReport(
		        info.isCompliant(),
				statement,
				ValidationSummary.fromValues(info, fixerResultStatus, completedFixes),
				rules);
	}

	private static Set<RuleSummary> getRules(Set<TestAssertion> assertions) {
		Map<String, RuleSummary> rulesMap = new HashMap<>();


		for (TestAssertion assertion : assertions) {
			RuleId id = assertion.getRuleId();
			String ruleId = id.getSpecification().getId() + id.getClause() + String.valueOf(id.getTestNumber());

			RuleSummary rule = rulesMap.get(ruleId);
			if (rule == null) {
				rule = RuleSummary.fromValues(id, assertion.getMessage(), PASSED, 0);
				rulesMap.put(ruleId, rule);
			}
			rule.addCheck(Check.fromValue(assertion));
			if (FAILED == assertion.getStatus()) {
				rule.setStatus(FAILED);
			}
		}

		Set<RuleSummary> res = new HashSet<>(rulesMap.values());

		return res;
	}
	
	private static String getStatement(boolean status) {
	    return (status) ? COMPLIANT_STATEMENT : NONCOMPLIANT_STATEMENT; 
	}
}
