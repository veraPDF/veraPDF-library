/**
 * 
 */
package org.verapdf.pdfa.validators;

import org.verapdf.pdfa.PDFAValidator;
import org.verapdf.pdfa.flavours.PDFAFlavour;
import org.verapdf.pdfa.validation.Profiles;
import org.verapdf.pdfa.validation.ValidationProfile;

/**
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *
 */
public final class Validators {
    private Validators() {

    }

    /**
     * Generates validation info for objects with root {@code root} and
     * validation profile structure {@code validationProfile}
     * <p/>
     * This method doesn't need to parse validation profile (it works faster
     * than those ones, which parses profile).
     * 
     * @param flavour
     *            the PDF/A Flavour
     * @param logSuccess
     * @return
     *
     */
    public static PDFAValidator createValidator(PDFAFlavour flavour,
            boolean logSuccess) {
        if (flavour == null)
            throw new IllegalArgumentException(
                    "Parameter (PDFAFlavour flavour) cannot be null.");
        return createValidator(Profiles.getVeraProfileDirectory()
                .getValidationProfileByFlavour(flavour), logSuccess);
    }

    /**
     * Generates validation info for objects with root {@code root} and
     * validation profile structure {@code validationProfile}
     * <p/>
     * This method doesn't need to parse validation profile (it works faster
     * than those ones, which parses profile).
     * 
     * @param profile
     * @return validation info structure
     */
    public static PDFAValidator createValidator(final ValidationProfile profile) {
        return createValidator(profile, false);
    }

    /**
     * Generates validation info for objects with root {@code root} and
     * validation profile structure {@code validationProfile}
     * <p/>
     * This method doesn't need to parse validation profile (it works faster
     * than those ones, which parses profile).
     * 
     * @param profile
     * @param logSuccess
     * @return validation info structure
     */
    public static PDFAValidator createValidator(
            final ValidationProfile profile, boolean logSuccess) {
        if (profile == null)
            throw new IllegalArgumentException(
                    "Parameter (ValidationProfile profile) cannot be null.");
        return new BaseValidator(profile, logSuccess);
    }

    /**
     * Generates validation info for objects with root {@code root} and
     * validation profile structure {@code validationProfile}
     * <p/>
     * This method doesn't need to parse validation profile (it works faster
     * than those ones, which parses profile).
     * 
     * @param profile
     * @param logSuccess
     * @param maxFailures
     * @return validation info structure
     */
    public static PDFAValidator createValidator(
            final ValidationProfile profile, final int maxFailures) {
        return createValidator(profile, false, maxFailures);
    }

    /**
     * Generates validation info for objects with root {@code root} and
     * validation profile structure {@code validationProfile}
     * <p/>
     * This method doesn't need to parse validation profile (it works faster
     * than those ones, which parses profile).
     * 
     * @param profile
     * @param logSuccess
     * @param maxFailures
     * @return validation info structure
     */
    public static PDFAValidator createValidator(
            final ValidationProfile profile, boolean logSuccess,
            final int maxFailures) {
        if (profile == null)
            throw new IllegalArgumentException(
                    "Parameter (ValidationProfile profile) cannot be null.");
        if (maxFailures > 0)
            return new FastFailValidator(profile, logSuccess, maxFailures);
        return createValidator(profile, logSuccess);
    }
}
