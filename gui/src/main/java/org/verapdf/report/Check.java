package org.verapdf.report;

import org.verapdf.pdfa.results.TestAssertion;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Maksim Bezrukov
 */
@XmlRootElement
public class Check {

	@XmlAttribute
	private final TestAssertion.Status status;
	@XmlElement
	private final String context;

	private Check(TestAssertion.Status status, String context) {
		this.status = status;
		this.context = context;
	}

	private Check() {
		this(TestAssertion.Status.PASSED, "");
	}

	TestAssertion.Status getStatus() {
		return this.status;
	}

	static Check fromValue(TestAssertion assertion) {
		if (assertion == null) {
			throw new IllegalArgumentException("Argument assertion con not be null");
		}
		TestAssertion.Status status = assertion.getStatus();
		String context = assertion.getLocation().getContext();

		return new Check(status, context);
	}
}
