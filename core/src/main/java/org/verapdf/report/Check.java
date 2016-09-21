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
	private final String status;
	@XmlElement
	private final String context;

	private Check(TestAssertion.Status status, String context) {
		this.status = status.toString();
		this.context = context;
	}

	private Check() {
		this(TestAssertion.Status.PASSED, "");
	}

	static Check fromValue(TestAssertion assertion) {
		if (assertion == null) {
			throw new IllegalArgumentException("Argument assertion con not be null");
		}
		return new Check(assertion.getStatus(), assertion.getLocation().getContext());
	}
}
