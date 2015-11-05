package org.verapdf.report;

import org.verapdf.pdfa.results.TestAssertion;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Set;

/**
 * @author Maksim Bezrukov
 */
@XmlRootElement(name = "result")
public class Result {

	@XmlElement
	private boolean compliant;
	@XmlElement
	private String statement;
//	@XmlElement
//	private Summary summary;
//	@XmlElement
//	private Details details;

	private Result(boolean compliant, String statement, Summary summary, Details details) {
		this.compliant = compliant;
		this.statement = statement;
//		this.summary = summary;
//		this.details = details;
	}

	Result() {
		this(false, "", new Summary(), new Details());
	}

	static Result fromValues(Set<TestAssertion> assertions, boolean isCompliant) {
		boolean compliant = isCompliant;
		String statement = isCompliant ? "PDF file is compliant with Validation Profile requirements" :
				"PDF file is not compliant with Validation Profile requirements";

		return new Result(compliant, statement, null, null);
	}
}
