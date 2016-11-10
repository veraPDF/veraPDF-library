/**
 * 
 */
package org.verapdf.processor.reports;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.verapdf.pdfa.results.TestAssertion;
import org.verapdf.pdfa.results.TestAssertion.Status;
import org.verapdf.pdfa.results.ValidationResult;
import org.verapdf.pdfa.validation.profiles.Profiles;
import org.verapdf.pdfa.validation.profiles.Rule;
import org.verapdf.pdfa.validation.profiles.RuleId;
import org.verapdf.pdfa.validation.profiles.ValidationProfile;

/**
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *         <a href="https://github.com/carlwilson">carlwilson AT github</a>
 * @version 0.1 Created 10 Nov 2016:08:34:32
 */
final class ValidationDetailsImpl implements ValidationDetails {
	private final static ValidationDetailsImpl defaultInstance = new ValidationDetailsImpl();
	@XmlAttribute
	private final int passedRules;
	@XmlAttribute
	private final int failedRules;
	@XmlAttribute
	private final int passedChecks;
	@XmlAttribute
	private final int failedChecks;
	@XmlElement(name = "rule")
	private final Set<RuleSummary> ruleSummaries;

	private ValidationDetailsImpl() {
		this(0, 0, 0, 0, Collections.<RuleSummary>emptySet());
	}
	
	private ValidationDetailsImpl(final int passedRules, final int failedRules, final int passedChecks,
			final int failedChecks, final Set<RuleSummary> ruleSummaries) {
		this.passedRules = passedRules;
		this.failedRules = failedRules;
		this.passedChecks = passedChecks;
		this.failedChecks = failedChecks;
		this.ruleSummaries = new HashSet<>(ruleSummaries);
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

	static ValidationDetails fromValues(final ValidationResult result, boolean logPassedChecks,
			final int maxFailedChecks) {
		ValidationProfile profile = Profiles.getVeraProfileDirectory()
				.getValidationProfileByFlavour(result.getPDFAFlavour());
		Map<RuleId, Set<TestAssertion>> assertionMap = mapAssertionsByRule(result.getTestAssertions());
		Set<RuleSummary> ruleSummaries = new HashSet<>();
		int passedRules = 0;
		int passedChecks = 0;
		int failedRules = 0;
		int failedChecks = 0;
		for (Rule rule : profile.getRules()) {
			RuleSummary summary = RuleSummaryImpl.uncheckedInstance(rule.getRuleId(), rule.getDescription(),
					rule.getObject(), rule.getTest());
			if (assertionMap.containsKey(rule.getRuleId())) {
				summary = RuleSummaryImpl.fromValues(rule.getRuleId(), rule.getDescription(), rule.getObject(),
						rule.getTest(), assertionMap.get(rule.getRuleId()), logPassedChecks,
						maxFailedChecks);
			}
			passedChecks += summary.getPassedChecks();
			failedChecks += summary.getFailedChecks();
			if (summary.getRuleStatus() == Status.PASSED) {
				passedRules++;
				if (logPassedChecks)
					ruleSummaries.add(summary);
			} else {
				failedRules++;
				ruleSummaries.add(summary);
			}
		}

		return new ValidationDetailsImpl(passedRules, failedRules, passedChecks, failedChecks, ruleSummaries);
	}

	private static Map<RuleId, Set<TestAssertion>> mapAssertionsByRule(final Set<TestAssertion> assertions) {
		Map<RuleId, Set<TestAssertion>> assertionMap = new HashMap<>();
		for (TestAssertion assertion : assertions) {
			if (assertionMap.containsKey(assertion.getRuleId())) {
				assertionMap.get(assertion.getRuleId()).add(assertion);
			} else {
				Set<TestAssertion> assertionSet = new HashSet<>();
				assertionSet.add(assertion);
				assertionMap.put(assertion.getRuleId(), assertionSet);
			}
		}

		return assertionMap;
	}
}
