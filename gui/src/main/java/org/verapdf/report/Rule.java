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
public class Rule {

	@XmlAttribute
	private final String specification;
	@XmlAttribute
	private final String clause;
	@XmlAttribute
	private final String testNumber;
	@XmlAttribute
	private TestAssertion.Status status;
//	@XmlAttribute(name = "checks")
//	private final int checksNumber;

	@XmlElementWrapper
	@XmlElement(name = "check")
	private final Set<Check> checks;

	public Rule(String specification, String clause, String testNumber, TestAssertion.Status status, int checksNumber, Set<Check> checks) {
		this.specification = specification;
		this.clause = clause;
		this.testNumber = testNumber;
		this.status = status;
//		this.checksNumber = checksNumber;
		this.checks = checks;
	}

	private Rule() {
		this("", "", "", TestAssertion.Status.PASSED, 0, new HashSet<Check>());
	}

	static Rule fromValues(RuleId id, TestAssertion.Status status, int checksNumber) {
		if (id == null) {
			throw new IllegalArgumentException("Argument id con not be null");
		}
		if (status == null) {
			throw new IllegalArgumentException("Argument status con not be null");
		}

		return new Rule(
				id.getSpecfication().getId(),
				id.getClause(),
				String.valueOf(id.getTestNumber()),
				status,
				checksNumber,
				new HashSet<Check>());
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
