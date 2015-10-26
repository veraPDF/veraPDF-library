/**
 * 
 */
package org.verapdf.pdfa.reporting;

import java.util.Set;

import org.verapdf.pdfa.flavours.PDFAFlavour;

/**
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *
 */
public class ValidationReports {
    private ValidationReports() {
        // Disable default constructor
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
    public static ValidationResult resultFromValues(final PDFAFlavour flavour,
            final Set<TestAssertion> assertions, final boolean isCompliant) {
        return ValidationResultImpl
                .fromValues(flavour, assertions, isCompliant);
    }
}
