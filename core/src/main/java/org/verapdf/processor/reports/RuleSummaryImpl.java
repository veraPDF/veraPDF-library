/**
 * This file is part of veraPDF Library core, a module of the veraPDF project.
 * Copyright (c) 2015, veraPDF Consortium <info@verapdf.org>
 * All rights reserved.
 *
 * veraPDF Library core is free software: you can redistribute it and/or modify
 * it under the terms of either:
 *
 * The GNU General public license GPLv3+.
 * You should have received a copy of the GNU General Public License
 * along with veraPDF Library core as the LICENSE.GPL file in the root of the source
 * tree.  If not, see http://www.gnu.org/licenses/ or
 * https://www.gnu.org/licenses/gpl-3.0.en.html.
 *
 * The Mozilla Public License MPLv2+.
 * You should have received a copy of the Mozilla Public License along with
 * veraPDF Library core as the LICENSE.MPL file in the root of the source tree.
 * If a copy of the MPL was not distributed with this file, you can obtain one at
 * http://mozilla.org/MPL/2.0/.
 */
/**
 * 
 */
package org.verapdf.processor.reports;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.verapdf.pdfa.results.TestAssertion;
import org.verapdf.pdfa.results.TestAssertion.Status;
import org.verapdf.pdfa.validation.profiles.Profiles;
import org.verapdf.pdfa.validation.profiles.RuleId;

/**
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *         <a href="https://github.com/carlwilson">carlwilson AT github</a>
 * @version 0.1 Created 10 Nov 2016:08:45:52
 */
final class RuleSummaryImpl implements RuleSummary {
	private final Status ruleStatus;
	@XmlAttribute
	private final String specification;
	@XmlAttribute
	private final String clause;
	@XmlAttribute
	private final int testNumber;
	@XmlAttribute
	private final String status;
	@XmlAttribute
	private final int passedChecks;
	@XmlAttribute
	private final int failedChecks;
	@XmlElement
	private final String description;
	@XmlElement
	private final String object;
	@XmlElement
	private final String test;
	@XmlElement(name = "check")
	private final Set<Check> checks;

	private RuleSummaryImpl(final RuleId ruleId, final Status status, final int passedChecks, final int failedChecks,
			final String description, final String object, final String test, final Set<Check> checks) {
		this.specification = ruleId.getSpecification().getId();
		this.clause = ruleId.getClause();
		this.testNumber = ruleId.getTestNumber();
		this.ruleStatus = status;
		this.status = status.toString();
		this.passedChecks = passedChecks;
		this.failedChecks = failedChecks;
		this.description = description;
		this.object = object;
		this.test = test;
		this.checks = ((checks != null) && !checks.isEmpty()) ? new HashSet<>(checks) : null;
	}

	private RuleSummaryImpl(final RuleId ruleId, final Status status, final String description, final String object,
			final String test) {
		this(ruleId, status, 0, 0, description, object, test, Collections.<Check>emptySet());
	}

	private RuleSummaryImpl() {
		this(Profiles.defaultRuleId(), Status.UNKNOWN, "", "", "");
	}

	/**
	 * @return the status
	 */
	@Override
	public String getStatus() {
		return this.status;
	}


	/**
	 * @return the status
	 */
	@Override
	public Status getRuleStatus() {
		return this.ruleStatus;
	}

	/**
	 * @return the specification
	 */
	@Override
	public String getSpecification() {
		return this.specification;
	}

	/**
	 * @return the clause
	 */
	@Override
	public String getClause() {
		return this.clause;
	}

	/**
	 * @return the testNumber
	 */
	@Override
	public int getTestNumber() {
		return this.testNumber;
	}

	/**
	 * @return the passedChecks
	 */
	@Override
	public int getPassedChecks() {
		return this.passedChecks;
	}

	/**
	 * @return the failedChecks
	 */
	@Override
	public int getFailedChecks() {
		return this.failedChecks;
	}

	/**
	 * @return the description
	 */
	@Override
	public String getDescription() {
		return this.description;
	}

	/**
	 * @return the object
	 */
	@Override
	public String getObject() {
		return this.object;
	}

	/**
	 * @return the test
	 */
	@Override
	public String getTest() {
		return this.test;
	}

	/**
	 * @return the checks
	 */
	@Override
	public Set<Check> getChecks() {
		return this.checks;
	}

	static class Adapter extends XmlAdapter<RuleSummaryImpl, RuleSummary> {
		@Override
		public RuleSummary unmarshal(RuleSummaryImpl summary) {
			return summary;
		}

		@Override
		public RuleSummaryImpl marshal(RuleSummary summary) {
			return (RuleSummaryImpl) summary;
		}
	}

	static final RuleSummary fromValues(final RuleId id, final String description, final String object, final String test,
			Set<TestAssertion> assertions, boolean logPassedChecks, int maxNumberOfDisplayedFailedChecks) {
		if (id == null) {
			throw new NullPointerException("Argument id can not be null");
		}
		if (description == null) {
			throw new NullPointerException("Argument description can not be null");
		}
		if (assertions == null) {
			throw new NullPointerException("Argument assertions can not be null");
		}
		Set<Check> checks = new HashSet<>();
		Status status = Status.PASSED;
		int passedChecks = 0;
		int failedChecks = 0;
		for (TestAssertion assertion : assertions) {
			if (assertion.getStatus() == Status.PASSED) {
				passedChecks++;
				if (logPassedChecks)
					checks.add(CheckImpl.fromValue(assertion));
			} else {
				status = assertion.getStatus();
				failedChecks++;
				if ((maxNumberOfDisplayedFailedChecks == -1) || (failedChecks <= maxNumberOfDisplayedFailedChecks)) {
					checks.add(CheckImpl.fromValue(assertion));
				}
			}
		}
		return new RuleSummaryImpl(id, status, passedChecks, failedChecks, description, object, test, checks);
	}

	static final RuleSummary uncheckedInstance(final RuleId id, final String description, final String object,
			final String test) {
		if (id == null) {
			throw new NullPointerException("Argument id can not be null");
		}
		if (description == null) {
			throw new NullPointerException("Argument description can not be null");
		}
		return new RuleSummaryImpl(id, Status.PASSED, description, object, test);
	}

}
