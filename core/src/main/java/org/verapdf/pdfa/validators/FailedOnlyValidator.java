package org.verapdf.pdfa.validators;

import org.verapdf.pdfa.results.Location;
import org.verapdf.pdfa.results.TestAssertion;
import org.verapdf.pdfa.results.TestAssertion.Status;
import org.verapdf.pdfa.results.ValidationResults;
import org.verapdf.pdfa.validation.Rule;
import org.verapdf.pdfa.validation.ValidationProfile;

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
