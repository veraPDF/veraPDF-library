package org.verapdf.report;

import org.verapdf.pdfa.MetadataFixerResult;
import org.verapdf.pdfa.results.TestAssertion;
import org.verapdf.pdfa.results.ValidationResult;
import org.verapdf.pdfa.validation.RuleId;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.verapdf.pdfa.MetadataFixerResult.RepairStatus.ID_REMOVED;
import static org.verapdf.pdfa.MetadataFixerResult.RepairStatus.SUCCESS;
import static org.verapdf.pdfa.results.TestAssertion.Status.FAILED;
import static org.verapdf.pdfa.results.TestAssertion.Status.PASSED;

/**
 * @author Maksim Bezrukov
 */
@XmlRootElement(name = "result")
public class Result {

	@XmlElement
	private final boolean compliant;
	@XmlElement
	private final String statement;
	@XmlElement
	private final Summary summary;
	@XmlElement
	private final Details details;

	private Result(boolean compliant, String statement, Summary summary, Details details) {
		this.compliant = compliant;
		this.statement = statement;
		this.summary = summary;
		this.details = details;
	}

	private Result() {
		this(false, "", Summary.fromValues(0, 0, 0, 0, null, 0), Details.fromValue(new HashSet<Rule>()));
	}

	static Result fromValues(ValidationResult info, MetadataFixerResult fixerResult) {

		Set<TestAssertion> assertions = info.getTestAssertions();
		boolean compliant = info.isCompliant();
		String statement = compliant ? "PDF file is compliant with Validation Profile requirements" :
				"PDF file is not compliant with Validation Profile requirements";

		Set<Rule> rules = getRules(assertions);

		String fixerResultStatus = null;
		Integer completedFixes = null;

		if (fixerResult != null) {
			MetadataFixerResult.RepairStatus repairStatus = fixerResult.getRepairStatus();
			fixerResultStatus = repairStatus.getName();
			if (SUCCESS == repairStatus
					|| ID_REMOVED == repairStatus) {
				completedFixes = fixerResult.getAppliedFixes().size();
			}
		}

		return new Result(
				compliant,
				statement,
				Summary.fromValues(0, 0, 0, 0, fixerResultStatus, completedFixes),
				Details.fromValue(rules));
	}

	private static Set<Rule> getRules(Set<TestAssertion> assertions) {
		Map<String, Rule> rulesMap = new HashMap<>();


		for (TestAssertion assertion : assertions) {
			RuleId id = assertion.getRuleId();
			String ruleId = id.getSpecification().getId() + id.getClause() + String.valueOf(id.getTestNumber());

			Rule rule = rulesMap.get(ruleId);
			if (rule == null) {
				rule = Rule.fromValues(id, assertion.getMessage(), PASSED, 0);
				rulesMap.put(ruleId, rule);
			}
			rule.addCheck(Check.fromValue(assertion));
			if (FAILED == assertion.getStatus()) {
				rule.setStatus(FAILED);
			}
		}

		Set<Rule> res = new HashSet<>(rulesMap.values());

		return res;
	}
}
