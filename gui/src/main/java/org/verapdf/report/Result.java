package org.verapdf.report;

import org.verapdf.pdfa.results.TestAssertion;
import org.verapdf.pdfa.validation.RuleId;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Maksim Bezrukov
 */
@XmlRootElement(name = "result")
public class Result {

	@XmlElement
	private final boolean compliant;
	@XmlElement
	private final String statement;
//	@XmlElement
//	private final Summary summary;
@XmlElement
private final Details details;

	private Result(boolean compliant, String statement, Summary summary, Details details) {
		this.compliant = compliant;
		this.statement = statement;
//		this.summary = summary;
		this.details = details;
	}

	Result() {
		this(false, "", Summary.fromValues(0, 0, 0, 0, "", 0), Details.fromValue(new HashSet<Rule>()));
	}

	static Result fromValues(Set<TestAssertion> assertions, boolean isCompliant) {
		if (assertions == null) {
			throw new IllegalArgumentException("Argument assertions con not be null");
		}

		boolean compliant = isCompliant;
		String statement = isCompliant ? "PDF file is compliant with Validation Profile requirements" :
				"PDF file is not compliant with Validation Profile requirements";

		Set<Rule> rules = getRules(assertions);

		return new Result(
				compliant,
				statement,
				Summary.fromValues(0, 0, 0, 0, "", 0),
				Details.fromValue(rules));
	}

	private static Set<Rule> getRules(Set<TestAssertion> assertions) {
		Map<String, Rule> rulesMap = new HashMap<>();


		for (TestAssertion assertion : assertions) {
			RuleId id = assertion.getRuleId();
			String ruleId = id.getSpecfication().getId() + id.getClause() + String.valueOf(id.getTestNumber());

			Rule rule = rulesMap.get(ruleId);
			if (rule == null) {
				rule = Rule.fromValues(id, assertion.getMessage(), TestAssertion.Status.PASSED, 0);
				rulesMap.put(ruleId, rule);
			}
			rule.addCheck(Check.fromValue(assertion));
			if (TestAssertion.Status.FAILED.equals(assertion.getStatus())) {
				rule.setStatus(TestAssertion.Status.FAILED);
			}
		}

		Set<Rule> res = new HashSet<>(rulesMap.values());

		return res;
	}
}
