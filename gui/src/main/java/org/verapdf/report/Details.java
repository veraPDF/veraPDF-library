package org.verapdf.report;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Maksim Bezrukov
 */
@XmlRootElement
public class Details {

	@XmlElementWrapper
	@XmlElement(name = "rule")
	private final Set<Rule> rules;
//	@XmlElementWrapper
//	@XmlElement(name = "warning")
//	private final Set<Warning> warnings;

	private Details(Set<Rule> rules) {
		this.rules = Collections.unmodifiableSet(rules);
	}

	private Details() {
		this(new HashSet<Rule>());
	}

	static Details fromValue(Set<Rule> rules) {
		if (rules == null) {
			throw new IllegalArgumentException("Argument rules con not be null");
		}
		return new Details(rules);
	}
}
