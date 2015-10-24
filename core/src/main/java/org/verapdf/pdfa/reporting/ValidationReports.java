/**
 * 
 */
package org.verapdf.pdfa.reporting;

import java.util.List;

import org.verapdf.pdfa.ValidationResult;
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
     * @param assertions
     * @param isCompliant
     * @return
     */
    public static ValidationResult resultFromValues(final PDFAFlavour flavour,
            final List<TestAssertion> assertions, final boolean isCompliant) {
        return ValidationResultImpl.fromValues(flavour, assertions, isCompliant);
    }
}
