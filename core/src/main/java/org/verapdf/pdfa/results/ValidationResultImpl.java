/**
 * This file is part of veraPDF Library core, a module of the veraPDF project.
 * Copyright (c) 2015-2024, veraPDF Consortium <info@verapdf.org>
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
package org.verapdf.pdfa.results;

import org.verapdf.pdfa.flavours.PDFAFlavour;
import org.verapdf.pdfa.results.TestAssertion.Status;
import org.verapdf.pdfa.validation.profiles.ProfileDetails;
import org.verapdf.pdfa.validation.profiles.Profiles;
import org.verapdf.pdfa.validation.profiles.RuleId;
import org.verapdf.pdfa.validation.profiles.ValidationProfile;
import org.verapdf.processor.reports.enums.JobEndStatus;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.util.*;

/**
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 */
@XmlRootElement(name = "validationResult")
final class ValidationResultImpl implements ValidationResult {
	private static final ValidationResultImpl DEFAULT = new ValidationResultImpl();
	@XmlAttribute
	private final PDFAFlavour flavour;
	@XmlElement
	private final ProfileDetails profileDetails;
	@XmlAttribute
	private final int totalAssertions;
	@XmlElementWrapper
	@XmlElement(name = "assertion")
	private final List<TestAssertion> assertions;
	@XmlAttribute
	private final boolean isCompliant;
	@XmlAttribute
	private final JobEndStatus jobEndStatus;

	private HashMap<RuleId, Integer> failedChecks = null;

	private final ValidationProfile validationProfile;

	private ValidationResultImpl() {
		this(Profiles.defaultProfile(), Collections.emptyList(),
		     false, JobEndStatus.NORMAL);
	}

	private ValidationResultImpl(final ValidationProfile validationProfile,
			final List<TestAssertion> assertions, final boolean isCompliant, JobEndStatus endStatus) {
		this(validationProfile, assertions, isCompliant, assertions.size(), endStatus);
	}

	private ValidationResultImpl(final ValidationProfile validationProfile,
			final List<TestAssertion> assertions, final boolean isCompliant, int totalAssertions,
			                     final JobEndStatus endStatus) {
		super();
		this.flavour = validationProfile.getPDFAFlavour();
		this.assertions = new ArrayList<>(assertions);
		this.isCompliant = isCompliant;
		this.totalAssertions = totalAssertions;
		this.profileDetails = validationProfile.getDetails();
		this.validationProfile = validationProfile;
		this.jobEndStatus = endStatus;
	}

	private ValidationResultImpl(final ValidationProfile validationProfile, final List<TestAssertion> assertions,
	                             final HashMap<RuleId, Integer> failedChecks, final boolean isCompliant,
	                             int totalAssertions, final JobEndStatus endStatus) {
		this(validationProfile, assertions, isCompliant, totalAssertions, endStatus);
		this.failedChecks = failedChecks;
	}

	/**
	 * { @inheritDoc }
	 */
	@Override
	public boolean isCompliant() {
		return this.isCompliant;
	}

	/**
	 * { @inheritDoc }
	 */
	@Override
	public PDFAFlavour getPDFAFlavour() {
		return this.flavour;
	}

	/**
	 * { @inheritDoc }
	 */
	@Override
	public ProfileDetails getProfileDetails() {
		return this.profileDetails;
	}

	/**
	 * { @inheritDoc }
	 */
	@Override
	public int getTotalAssertions() {
		return this.totalAssertions;
	}

	/**
	 * { @inheritDoc }
	 */
	@Override
	public List<TestAssertion> getTestAssertions() {
		return Collections.unmodifiableList(this.assertions);
	}

	/**
	 * { @inheritDoc }
	 */
	@Override
	public ValidationProfile getValidationProfile() {
		return this.validationProfile;
	}

	@Override
	public JobEndStatus getJobEndStatus() {
		return this.jobEndStatus;
	}

	@Override
	public HashMap<RuleId, Integer> getFailedChecks() {
		return this.failedChecks;
	}

	/**
	 * { @inheritDoc }
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.assertions == null) ? 0 : this.assertions.hashCode());
		result = prime * result + ((this.flavour == null) ? 0 : this.flavour.hashCode());
		result = prime * result + (this.isCompliant ? 1231 : 1237);
		result = prime * result + ((this.jobEndStatus == null) ? 0 : this.jobEndStatus.hashCode());
		result = prime * result + this.totalAssertions;
		return result;
	}

	/**
	 * { @inheritDoc }
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ValidationResult other = (ValidationResult) obj;
		if (!Objects.equals(this.assertions, other.getTestAssertions())) {
			return false;
		}
		if (this.flavour != other.getPDFAFlavour())
			return false;
		if (this.isCompliant != other.isCompliant())
			return false;
		if (this.jobEndStatus != other.getJobEndStatus())
			return false;
		return this.totalAssertions == other.getTotalAssertions();
	}

	/**
	 * { @inheritDoc }
	 */
	@Override
	public String toString() {
		return "ValidationResult [flavour=" + this.flavour + ", totalAssertions=" + this.totalAssertions //$NON-NLS-1$ //$NON-NLS-2$
				+ ", assertions=" + this.assertions + ", isCompliant=" + this.isCompliant + "]"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}

	static ValidationResultImpl defaultInstance() {
		return DEFAULT;
	}

	static ValidationResultImpl fromValues(final ValidationProfile validationProfile, final List<TestAssertion> assertions,
	                                       final HashMap<RuleId, Integer> failedChecks, final boolean isCompliant,
	                                       final int totalChecks, final JobEndStatus endStatus) {
		return new ValidationResultImpl(validationProfile, assertions, failedChecks, isCompliant, totalChecks, endStatus);
	}

	static ValidationResultImpl fromValues(final ValidationProfile validationProfile, final List<TestAssertion> assertions,
	                                       final boolean isCompliant, final int totalChecks, JobEndStatus endStatus) {
		return new ValidationResultImpl(validationProfile, assertions, isCompliant, totalChecks, endStatus);
	}

	static ValidationResultImpl fromValidationResult(ValidationResult toConvert) {
		List<TestAssertion> assertions = toConvert.getTestAssertions();
		return fromValues(toConvert.getValidationProfile(), assertions,
				toConvert.isCompliant(), toConvert.getTotalAssertions(), toConvert.getJobEndStatus());
	}

	static ValidationResultImpl stripPassedTests(ValidationResult toStrip) {
		List<TestAssertion> assertions = toStrip.getTestAssertions();
		return fromValues(toStrip.getValidationProfile(), stripPassedTests(assertions),
				toStrip.isCompliant(), toStrip.getTotalAssertions(), toStrip.getJobEndStatus());
	}

	static class Adapter extends XmlAdapter<ValidationResultImpl, ValidationResult> {
		@Override
		public ValidationResult unmarshal(ValidationResultImpl validationResultImpl) {
			return validationResultImpl;
		}

		@Override
		public ValidationResultImpl marshal(ValidationResult validationResult) {
			return (ValidationResultImpl) validationResult;
		}
	}

	static List<TestAssertion> stripPassedTests(final List<TestAssertion> toStrip) {
		List<TestAssertion> strippedList = new ArrayList<>();
		for (TestAssertion test : toStrip) {
			if (test.getStatus() != Status.PASSED)
				strippedList.add(test);
		}
		return strippedList;
	}
}
