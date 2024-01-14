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

import org.verapdf.pdfa.results.TestAssertion;
import org.verapdf.pdfa.results.TestAssertion.Status;
import org.verapdf.pdfa.results.ValidationResult;
import org.verapdf.pdfa.validation.profiles.Rule;
import org.verapdf.pdfa.validation.profiles.RuleId;
import org.verapdf.pdfa.validation.profiles.ValidationProfile;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.util.*;

/**
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *         <a href="https://github.com/carlwilson">carlwilson AT github</a>
 * @version 0.1 Created 10 Nov 2016:08:34:32
 */
final class ValidationDetailsImpl implements ValidationDetails {
	private static final ValidationDetailsImpl defaultInstance = new ValidationDetailsImpl();
	private final int passedRules;
	private final int failedRules;
	private final int passedChecks;
	@XmlAttribute(name="deviations")
	private final int failedChecks;

	private final Set<String> tags;

	@XmlElement(name = "rule")
	private final Set<RuleSummary> ruleSummaries;

	private ValidationDetailsImpl() {
		this(0, 0, 0, 0, Collections.emptySet(), null);
	}
	
	private ValidationDetailsImpl(final int passedRules, final int failedRules, final int passedChecks,
			final int failedChecks, final Set<RuleSummary> ruleSummaries, final Set<String> tags) {
		this.passedRules = passedRules;
		this.failedRules = failedRules;
		this.passedChecks = passedChecks;
		this.failedChecks = failedChecks;
		this.ruleSummaries = new HashSet<>(ruleSummaries);
		this.tags = tags != null && !tags.isEmpty() ? tags : null;
	}

	/**
	 * @return the passedRules
	 */
	@Override
	public int getPassedRules() {
		return this.passedRules;
	}

	/**
	 * @return the failedRules
	 */
	@Override
	public int getFailedRules() {
		return this.failedRules;
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
	 * @return the ruleSummaries
	 */
	@Override
	public Set<RuleSummary> getRuleSummaries() {
		return this.ruleSummaries;
	}

	@Override
	public Set<String> getTags() {
		return this.tags;
	}

	static class Adapter extends XmlAdapter<ValidationDetailsImpl, ValidationDetails> {
		@Override
		public ValidationDetails unmarshal(ValidationDetailsImpl details) {
			return details;
		}

		@Override
		public ValidationDetailsImpl marshal(ValidationDetails details) {
			return (ValidationDetailsImpl) details;
		}
	}

	static final ValidationDetails defaultInstance() {
		return defaultInstance;
	}

	static ValidationDetails fromValues(final ValidationResult result, boolean logPassedChecks) {
		ValidationProfile profile = result.getValidationProfile();
		Map<RuleId, List<TestAssertion>> assertionMap = mapAssertionsByRule(result.getTestAssertions());
		Set<RuleSummary> ruleSummaries = new HashSet<>();
		Map<RuleId, Integer> failedChecksMap = result.getFailedChecks();
		Set<String> tags = new HashSet<>();
		int passedRules = 0;
		int failedRules = 0;
		int failedChecks = 0;
		for (Rule rule : profile.getRules()) {
			RuleSummary summary = assertionMap.containsKey(rule.getRuleId()) ?
				RuleSummaryImpl.fromValues(rule.getRuleId(), rule.getDescription(), rule.getObject(), rule.getTest(),
					assertionMap.get(rule.getRuleId()), logPassedChecks, failedChecksMap.get(rule.getRuleId()), rule.getTags()) :
				RuleSummaryImpl.uncheckedInstance(rule.getRuleId(), rule.getDescription(), rule.getObject(), rule.getTest(), rule.getTags());
			failedChecks += summary.getFailedChecks();
			Set<String> summaryTags = summary.getTags();
			if (summary.getRuleStatus() == Status.PASSED) {
				passedRules++;
				if (logPassedChecks) {
					ruleSummaries.add(summary);
					if (summaryTags != null) {
						tags.addAll(summaryTags);
					}
				}
			} else {
				failedRules++;
				ruleSummaries.add(summary);
				if (summaryTags != null) {
					tags.addAll(summaryTags);
				}
			}
		}
		int passedChecks = result.getTotalAssertions() - failedChecks;
		return new ValidationDetailsImpl(passedRules, failedRules, passedChecks, failedChecks, ruleSummaries, tags);
	}

	private static Map<RuleId, List<TestAssertion>> mapAssertionsByRule(final List<TestAssertion> assertions) {
		Map<RuleId, List<TestAssertion>> assertionMap = new HashMap<>();
		for (TestAssertion assertion : assertions) {
			if (assertionMap.containsKey(assertion.getRuleId())) {
				assertionMap.get(assertion.getRuleId()).add(assertion);
			} else {
				List<TestAssertion> assertionSet = new ArrayList<>();
				assertionSet.add(assertion);
				assertionMap.put(assertion.getRuleId(), assertionSet);
			}
		}

		return assertionMap;
	}
}
