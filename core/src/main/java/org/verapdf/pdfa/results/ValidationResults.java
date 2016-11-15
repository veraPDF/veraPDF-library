/**
 * 
 */
package org.verapdf.pdfa.results;

import java.util.Set;

import org.verapdf.pdfa.flavours.PDFAFlavour;
import org.verapdf.pdfa.results.TestAssertion.Status;
import org.verapdf.pdfa.validation.profiles.ProfileDetails;
import org.verapdf.pdfa.validation.profiles.RuleId;

/**
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 */
public class ValidationResults {
	private static final String NOT_NULL_MESSAGE = " cannot be null.";
	private static final String FLAVOUR_NOT_NULL_MESSAGE = "Flavour " + NOT_NULL_MESSAGE;
	private static final String ASSERTIONS_NOT_NULL_MESSAGE = "Assertions " + NOT_NULL_MESSAGE;

	private ValidationResults() {
		throw new AssertionError("Should never enter ValidationResults().");
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
			throw new NullPointerException("toStrip can not be null.");
		return ValidationResultImpl.stripPassedTests(toStrip);
	}
}
