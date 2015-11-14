package org.verapdf.pdfa.validators;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.NativeJavaObject;
import org.mozilla.javascript.Script;
import org.mozilla.javascript.ScriptableObject;
import org.verapdf.core.ValidationException;
import org.verapdf.model.baselayer.Object;
import org.verapdf.pdfa.ValidationModelParser;
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
public final class FailedOnlyValidator extends AbstractValidator {
    private int passedTests = 0;

    FailedOnlyValidator(final ValidationProfile profile) {
        super(profile);
    }

    @Override
    protected void initialise() {
        super.initialise();
        this.passedTests = 0;
    }

    @Override
    protected void processAssertionResult(final boolean assertionResult,
            final String locationContext, final Rule rule) {
        Status assertionStatus = (assertionResult) ? Status.PASSED
                : Status.FAILED;

        Location location = ValidationResults.locationFromValues(this.rootType,
                locationContext);
        TestAssertion assertion = ValidationResults.assertionFromValues(
                rule.getRuleId(), assertionStatus, rule.getDescription(),
                location);
        if (assertionResult) {
            this.passedTests++;
        } else {
            this.results.add(assertion);
        }
    }
}
