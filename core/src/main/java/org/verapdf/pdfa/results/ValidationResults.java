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
package org.verapdf.pdfa.results;

import java.util.Set;

import javax.xml.bind.JAXBException;

import org.verapdf.core.XmlSerialiser;
import org.verapdf.pdfa.flavours.PDFAFlavour;
import org.verapdf.pdfa.results.TestAssertion.Status;
import org.verapdf.pdfa.validation.profiles.ProfileDetails;
import org.verapdf.pdfa.validation.profiles.RuleId;

/**
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 */
public class ValidationResults {
	private static final String NOT_NULL_MESSAGE = " cannot be null."; //$NON-NLS-1$
	private static final String FLAVOUR_NOT_NULL_MESSAGE = "Flavour " + NOT_NULL_MESSAGE; //$NON-NLS-1$
	private static final String ASSERTIONS_NOT_NULL_MESSAGE = "Assertions " + NOT_NULL_MESSAGE; //$NON-NLS-1$

	private ValidationResults() {
		throw new AssertionError("Should never enter ValidationResults()."); //$NON-NLS-1$
	}

	/**
	 * @param flavour
	 *            a {@link PDFAFlavour} instance indicating the validation type
	 *            performed
	 * @param assertions
	 *            the Set of TestAssertions reported by during validation
	 * @param isCompliant
	 *            a boolean that indicating whether the validated PDF/A data was
	 *            compliant with the indicated flavour
	 * @return a new ValidationResult instance populated from the values
	 */
	public static ValidationResult resultFromValues(final PDFAFlavour flavour, final ProfileDetails profileDetails, final Set<TestAssertion> assertions,
			final boolean isCompliant) {
		if (flavour == null)
			throw new NullPointerException(FLAVOUR_NOT_NULL_MESSAGE);
		if (assertions == null)
			throw new NullPointerException(ASSERTIONS_NOT_NULL_MESSAGE);
		return ValidationResultImpl.fromValues(flavour, profileDetails, assertions, isCompliant, assertions.size());
	}

	/**
	 * @param flavour
	 *            a {@link PDFAFlavour} instance indicating the validation type
	 *            performed
	 * @param assertions
	 *            the Set of TestAssertions reported by during validation
	 * @param isCompliant
	 *            a boolean that indicating whether the validated PDF/A data was
	 *            compliant with the indicated flavour
	 * @param totalAssertions
	 * @return a new ValidationResult instance populated from the values
	 */
	public static ValidationResult resultFromValues(final PDFAFlavour flavour, final ProfileDetails profileDetails, final Set<TestAssertion> assertions,
			final boolean isCompliant, final int totalAssertions) {
		if (flavour == null)
			throw new NullPointerException(FLAVOUR_NOT_NULL_MESSAGE);
		if (assertions == null)
			throw new NullPointerException(ASSERTIONS_NOT_NULL_MESSAGE);
		return ValidationResultImpl.fromValues(flavour, profileDetails, assertions, isCompliant, totalAssertions);
	}

	/**
	 * @param flavour
	 *            a {@link PDFAFlavour} instance indicating the validation type
	 *            performed
	 * @param assertions
	 *            the Set of TestAssertions reported by during validation
	 * @return a new ValidationResult instance populated from the values
	 */
	public static ValidationResult resultFromValues(final PDFAFlavour flavour, final ProfileDetails profileDetails, final Set<TestAssertion> assertions) {
		if (flavour == null)
			throw new NullPointerException(FLAVOUR_NOT_NULL_MESSAGE);
		if (assertions == null)
			throw new NullPointerException(ASSERTIONS_NOT_NULL_MESSAGE);
		boolean isCompliant = true;
		for (TestAssertion assertion : assertions) {
			if (assertion.getStatus() == Status.FAILED) {
				isCompliant = false;
				break;
			}
		}
		return resultFromValues(flavour, profileDetails, assertions, isCompliant);
	}

	/**
	 * @param xmlSource XML representation of a {@link ValidationResult} to deserialise
	 * @return a new ValidationResult instance deserialised from the passed String
	 * @throws JAXBException when the passed String is not a valid XML representation
	 */
	public static ValidationResult resultFromXmlString(final String xmlSource) throws JAXBException {
		return XmlSerialiser.typeFromXml(ValidationResultImpl.class, xmlSource);
	}

	/**
	 * Returns an immutable default instance of a ValidationResult. This is a
	 * static single instance, i.e.
	 * <code>ValidationResults.defaultResult() == ValidationResults.defaultResult()</code>
	 * is always true.
	 *
	 * @return the default ValidationResult instance
	 */
	public static ValidationResult defaultResult() {
		return ValidationResultImpl.defaultInstance();
	}

	/**
	 * Creates an immutable TestAssertion instance from the passed parameter
	 * values.
	 * 
	 * @param ordinal
	 *            the integer ordinal for the instance
	 * @param ruleId
	 *            the {@link RuleId} value for
	 *            {@link org.verapdf.pdfa.validation.Rule} the assertion refers
	 *            to.
	 * @param status
	 *            the {@link Status} of the assertion.
	 * @param message
	 *            any String message to be associated with the assertion.
	 * @param location
	 *            a {@link Location} instance indicating the location within the
	 *            PDF document where the test was performed.
	 * @return an immutable TestAssertion instance initialised using the passed
	 *         values
	 */
	public static TestAssertion assertionFromValues(final int ordinal, final RuleId ruleId, final Status status,
			final String message, final Location location) {
		return TestAssertionImpl.fromValues(ordinal, ruleId, status, message, location);
	}

	/**
	 * Returns an immutable default instance of a TestAssertion. This is a
	 * static single instance, i.e.
	 * <code>ValidationResults.defaultAssertion() == ValidationResults.defaultAssertion()</code>
	 * is always true.
	 *
	 * @return the default TestAssertion instance
	 */
	public static TestAssertion defaultAssertion() {
		return TestAssertionImpl.defaultInstance();
	}

	/**
	 * TODO: Better explanation of level and context. Creates an immutable
	 * {@link Location} instance.
	 * 
	 * @param level
	 *            the Locations level, represented as a String
	 * @param context
	 *            the Locations context, represented as a String
	 * @return and immutable Location instance initialised with the passed
	 *         values.
	 */
	public static Location locationFromValues(final String level, final String context) {
		return LocationImpl.fromValues(level, context);
	}

	/**
	 * Returns an immutable default instance of a Location. This is a static
	 * single instance, i.e.
	 * <code>ValidationResults.defaultLocation() == ValidationResults.defaultLocation()</code>
	 * is always true.
	 *
	 * @return the default Location instance
	 */
	public static Location defaultLocation() {
		return LocationImpl.defaultInstance();
	}

	/**
	 * Strips any {@link TestAssertion}s where
	 * {@code assertion.getStatus() == TestAssertion.Status.PASSED} from
	 * {@code toStrip} and returns a new {@link ValidationResult} without the
	 * passed assertions.
	 * 
	 * @param toStrip
	 *            a {@code ValidationResult} to clone without passed
	 *            {@code TestAssertion}s
	 * @return a ValidationResult instance identical to {@code toStrip}, but
	 *         without passed {@code TestAssertion}s.
	 */
	public static ValidationResult stripPassedTests(ValidationResult toStrip) {
		if (toStrip == null)
			throw new NullPointerException("toStrip can not be null."); //$NON-NLS-1$
		return ValidationResultImpl.stripPassedTests(toStrip);
	}
}
