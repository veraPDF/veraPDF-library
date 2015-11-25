/**
 * 
 */
package org.verapdf.pdfa.validators;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.verapdf.core.ValidationException;
import org.verapdf.pdfa.PDFAValidator;
import org.verapdf.pdfa.ValidationModelParser;
import org.verapdf.pdfa.results.TestAssertion;
import org.verapdf.pdfa.results.TestAssertion.Status;
import org.verapdf.pdfa.results.ValidationResult;
import org.verapdf.pdfa.results.ValidationResults;
import org.verapdf.pdfa.validation.ValidationProfile;

/**
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *
 */
public class SummarisingValidator implements PDFAValidator {
    private final PDFAValidator validator;
    private final boolean logPassedTests;

    protected SummarisingValidator(final ValidationProfile profile) {
        this(profile, false);
    }

    protected SummarisingValidator(final ValidationProfile profile,
            final boolean logPassedTests) {
        super();
        this.validator = new BaseValidator(profile);
        this.logPassedTests = logPassedTests;
    }

    /**
     * { @inheritDoc }
     */
    @Override
    public ValidationProfile getProfile() {
        return this.validator.getProfile();
    }

    /**
     * { @inheritDoc }
     */
    @Override
    public ValidationResult validate(final ValidationModelParser toValidate)
            throws ValidationException, IOException {
        if (this.logPassedTests)
            return this.validator.validate(toValidate);
        ValidationResult result = this.validator.validate(toValidate);
        return ValidationResults.resultFromValues(result.getPDFAFlavour(),
                stripPassedTests(result.getTestAssertions()),
                result.isCompliant(), result.getTotalAssertions());
    }

    private static final Set<TestAssertion> stripPassedTests(
            final Set<TestAssertion> toStrip) {
        Set<TestAssertion> strippedSet = new HashSet<>();
        for (TestAssertion test : toStrip) {
            if (test.getStatus() != Status.PASSED)
                strippedSet.add(test);
        }
        return strippedSet;
    }
}
