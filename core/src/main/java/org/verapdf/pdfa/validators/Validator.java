package org.verapdf.pdfa.validators;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.NativeJavaObject;
import org.mozilla.javascript.Script;
import org.mozilla.javascript.ScriptableObject;
import org.verapdf.core.ValidationException;
import org.verapdf.model.baselayer.Object;
import org.verapdf.pdfa.flavours.PDFAFlavour;
import org.verapdf.pdfa.results.Location;
import org.verapdf.pdfa.results.TestAssertion;
import org.verapdf.pdfa.results.TestAssertion.Status;
import org.verapdf.pdfa.results.ValidationResult;
import org.verapdf.pdfa.results.ValidationResults;
import org.verapdf.pdfa.validation.Profiles;
import org.verapdf.pdfa.validation.Rule;
import org.verapdf.pdfa.validation.ValidationProfile;

import java.util.*;

/**
 * Validation logic
 *
 * @author Maksim Bezrukov
 */
public final class Validator extends AbstractValidator {
    private Validator(final ValidationProfile profile) {
        super(profile);
    }

    @Override
    protected void processAssertionResult(final boolean assertionResult, final String locationContext, final Rule rule) {
        Status assertionStatus = (assertionResult) ? Status.PASSED
                : Status.FAILED;

        Location location = ValidationResults.locationFromValues(this.rootType,
                locationContext);
        TestAssertion assertion = ValidationResults.assertionFromValues(
                rule.getRuleId(), assertionStatus,
                rule.getDescription(), location);
            this.results.add(assertion);
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
    public static ValidationResult validate(PDFAFlavour flavour, Object root, boolean logSuccess)
            throws ValidationException {
        if (flavour == null)
            throw new IllegalArgumentException(
                    "Parameter (PDFAFlavour flavour) cannot be null.");
        return validate(Profiles.getVeraProfileDirectory().getValidationProfileByFlavour(flavour), root, logSuccess);
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
    public static ValidationResult validate(final ValidationProfile profile, final Object root, boolean logSuccess)
            throws ValidationException {
        if (root == null)
            throw new IllegalArgumentException(
                    "Parameter (Object root) cannot be null.");
        if (profile == null)
            throw new IllegalArgumentException(
                    "Parameter (ValidationProfile profile) cannot be null.");
        Validator validator = new Validator(profile);
        return validator.validate(root);
    }

}
