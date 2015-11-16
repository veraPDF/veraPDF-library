/**
 * 
 */
package org.verapdf.pdfa.validators;

import org.verapdf.core.ValidationException;
import org.verapdf.pdfa.PDFAValidator;
import org.verapdf.pdfa.flavours.PDFAFlavour;
import org.verapdf.pdfa.validation.Profiles;
import org.verapdf.pdfa.validation.ValidationProfile;

/**
 * @author cfw
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
     *
     * @param root
     *            the root object for validation
     * @return validation info structure
     * @throws ValidationException
     *             when a problem occurs validating the PDF
     */
    public static PDFAValidator validate(PDFAFlavour flavour, boolean logSuccess) {
        if (flavour == null)
            throw new IllegalArgumentException(
                    "Parameter (PDFAFlavour flavour) cannot be null.");
        return validate(Profiles.getVeraProfileDirectory()
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
     *
     * @param root
     *            the root object for validation
     * @param logSuccess
     * @return validation info structure
     * @throws ValidationException
     *             when a problem occurs validating the PDF
     */
    public static PDFAValidator validate(final ValidationProfile profile,
            boolean logSuccess) {
        if (profile == null)
            throw new IllegalArgumentException(
                    "Parameter (ValidationProfile profile) cannot be null.");
        return (logSuccess) ? new SimpleValidator(profile)
                : new FailedOnlyValidator(profile);
    }

}
