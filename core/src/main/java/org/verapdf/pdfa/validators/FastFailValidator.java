/**
 * 
 */
package org.verapdf.pdfa.validators;

import org.verapdf.pdfa.validation.Rule;
import org.verapdf.pdfa.validation.ValidationProfile;

/**
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *
 */
class FastFailValidator extends BaseValidator {
    private final int maxFailedTests;
    protected int failureCount = 0;

    /**
     * @param profile
     * @param logPassedTests
     */
    protected FastFailValidator(final ValidationProfile profile,
            final boolean logPassedTests) {
        this(profile, logPassedTests, 0);
    }

    /**
     * @param profile
     * @param logPassedTests
     */
    protected FastFailValidator(final ValidationProfile profile,
            final boolean logPassedTests, final int maxFailedTests) {
        super(profile, logPassedTests);
        this.maxFailedTests = maxFailedTests;
    }

    @Override
    protected void processAssertionResult(final boolean assertionResult,
            final String locationContext, final Rule rule) {
        if (!assertionResult) {
            this.failureCount++;
            if ((this.maxFailedTests > 0) && (this.failureCount >= this.maxFailedTests)) {
                this.abortProcessing = true;
            }
        }
        super.processAssertionResult(assertionResult, locationContext, rule);
    }

}
