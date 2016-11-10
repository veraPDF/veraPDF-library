/**
 * 
 */
package org.verapdf.processor.reports;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

import org.verapdf.pdfa.results.TestAssertion;

/**
 * @author  <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *          <a href="https://github.com/carlwilson">carlwilson AT github</a>
 *
 * @version 0.1
 * 
 * Created 10 Nov 2016:08:51:41
 */

final class CheckImpl implements Check {
	@XmlAttribute
	private final String status;
	@XmlElement
	private final String context;

	private CheckImpl(TestAssertion.Status status, String context) {
		this.status = status.toString();
		this.context = context;
	}

	private CheckImpl() {
		this(TestAssertion.Status.PASSED, "");
	}
	
	/**
	 * @return the status
	 */
	@Override
	public String getStatus() {
		return this.status;
	}

	/**
	 * @return the context
	 */
	@Override
	public String getContext() {
		return this.context;
	}

	static Check fromValue(TestAssertion assertion) {
		if (assertion == null) {
			throw new IllegalArgumentException("Argument assertion con not be null");
		}
		return new CheckImpl(assertion.getStatus(), assertion.getLocation().getContext());
	}
}
