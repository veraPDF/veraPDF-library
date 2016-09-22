/**
 * 
 */
package org.verapdf.pdfa.validators;

import org.verapdf.pdfa.BatchValidator;
import org.verapdf.pdfa.PDFAValidator;
import org.verapdf.pdfa.flavours.PDFAFlavour;
import org.verapdf.pdfa.validation.Profiles;
import org.verapdf.pdfa.validation.ValidationProfile;

/**
 * Static utility class that fills in for a factory for {@link PDFAValidator}s.
 * 
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *
 */
public final class Validators {
    private Validators() {

    }

    /**
     * Creates a new {@link PDFAValidator} instance that uses one of the
     * {@link ValidationProfile}s packaged as a core library resource. While
     * these profiles are not guaranteed to be up to date, they are available
     * when offline.
     * 
     * A {@link ProfileDirectory} populated with the pre-loaded profiles can be
     * obtained by calling {@link Profiles#getVeraProfileDirectory()}.
     * 
     * @param flavour
     *            the {@link PDFAFlavour} that's associated with the
     *            {@code ValidationProfile} to used to initialise the
     *            {@code PDFAValidator}.
     * @param logPassedChecks
     *            {@code boolean} used to configure logging of passed tests by
     *            the {@code PDFAValidator}. Pass {@code true} to log passed
     *            tests, {@code false} to only log tests that don't pass.
     * @return a {@link PDFAValidator} instance initialised from the passed
     *         parameters
     *
     */
    public static PDFAValidator createValidator(final PDFAFlavour flavour,
            final boolean logPassedChecks) {
        if (flavour == null)
            throw new IllegalArgumentException(
                    "Parameter (PDFAFlavour flavour) cannot be null.");
        return createValidator(Profiles.getVeraProfileDirectory()
                .getValidationProfileByFlavour(flavour), logPassedChecks);
    }

    /**
     * Creates a new {@link PDFAValidator} instance that uses one of the
     * {@link ValidationProfile}s packaged as a core library resource, see
     * {@link Validators#createValidator(PDFAFlavour, boolean)}.
     * 
     * @param flavour
     *            the {@link PDFAFlavour} that's associated with the
     *            {@code ValidationProfile} to used to initialise the
     *            {@code PDFAValidator}.
     * @param logPassedChecks
     *            {@code boolean} used to configure logging of passed tests by
     *            the {@code PDFAValidator}. Pass {@code true} to log passed
     *            tests, {@code false} to only log tests that don't pass.
     * @param maxFailures
     *            an {@code int} value that configures the {@code PDFAValidator}
     *            to abort validation after {@code maxFailures} failed tests. If
     *            {@code maxFailures} is less than 1 then the
     *            {@code PDFAValidator} will complete the full validation
     *            process.
     * @return a {@link PDFAValidator} instance initialised from the passed
     *         parameters
     *
     */
    public static PDFAValidator createValidator(final PDFAFlavour flavour,
            final boolean logPassedChecks, final int maxFailures) {
        if (flavour == null)
            throw new IllegalArgumentException(
                    "Parameter (PDFAFlavour flavour) cannot be null.");
        return createValidator(Profiles.getVeraProfileDirectory()
                .getValidationProfileByFlavour(flavour), logPassedChecks, maxFailures);
    }

    /**
     * Creates a new {@link PDFAValidator} initialised with the passed profile
     * and configured NOT to log passed checks.
     * 
     * @param profile
     *            the {@link ValidationProfile} to be enforced by the returned
     *            {@code PDFAValidator}.
     * @return a {@link PDFAValidator} instance initialised from the passed
     *         parameters
     */
    public static PDFAValidator createValidator(final ValidationProfile profile) {
        return createValidator(profile, false);
    }

    /**
     * Creates a new {@link PDFAValidator} initialised with the passed profile
     * and chosen passed test logging.
     * 
     * @param profile
     *            the {@link ValidationProfile} to be enforced by the returned
     *            {@code PDFAValidator}.
     * @param logPassedChecks
     *            {@code boolean} used to configure logging of passed tests by
     *            the {@code PDFAValidator}. Pass {@code true} to log passed
     *            tests, {@code false} to only log tests that don't pass.
     * @return a {@link PDFAValidator} instance initialised from the passed
     *         parameters
     */
    public static PDFAValidator createValidator(
            final ValidationProfile profile, final boolean logPassedChecks) {
        if (profile == null)
            throw new IllegalArgumentException(
                    "Parameter (ValidationProfile profile) cannot be null.");
        return new BaseValidator(profile, logPassedChecks);
    }

    /**
     * Creates a new {@link PDFAValidator} initialised with the passed profile,
     * requested fast failing behaviour and configured NOT to log passed checks.
     * 
     * @param profile
     *            the {@link ValidationProfile} to be enforced by the returned
     *            {@code PDFAValidator}.
     * @param maxFailures
     *            an {@code int} value that configures the {@code PDFAValidator}
     *            to abort validation after {@code maxFailures} failed tests. If
     *            {@code maxFailures} is less than 1 then the
     *            {@code PDFAValidator} will complete the full validation
     *            process.
     * @return a {@link PDFAValidator} instance initialised from the passed
     *         parameters
     */
    public static PDFAValidator createValidator(
            final ValidationProfile profile, final int maxFailures) {
        return createValidator(profile, false, maxFailures);
    }

    /**
     * Creates a new {@link PDFAValidator} initialised with the passed profile,
     * chosen passed test logging and requested fast failing behaviour.
     * 
     * @param profile
     *            the {@link ValidationProfile} to be enforced by the returned
     *            {@code PDFAValidator}.
     * @param logPassedChecks
     *            {@code boolean} used to configure logging of passed tests by
     *            the {@code PDFAValidator}. Pass {@code true} to log passed
     *            tests, {@code false} to only log tests that don't pass.
     * @param maxFailures
     *            an {@code int} value that configures the {@code PDFAValidator}
     *            to abort validation after {@code maxFailures} failed tests. If
     *            {@code maxFailures} is less than 1 then the
     *            {@code PDFAValidator} will complete the full validation
     *            process.
     * @return a {@link PDFAValidator} instance initialised from the passed
     *         parameters
     */
    public static PDFAValidator createValidator(
            final ValidationProfile profile, final boolean logPassedChecks,
            final int maxFailures) {
        if (profile == null)
            throw new IllegalArgumentException(
                    "Parameter (ValidationProfile profile) cannot be null.");
        if (maxFailures > 0)
            return new FastFailValidator(profile, logPassedChecks, maxFailures);
        return createValidator(profile, logPassedChecks);
    }
}
