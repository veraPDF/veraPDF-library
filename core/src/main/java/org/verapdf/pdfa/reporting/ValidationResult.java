/**
 * 
 */
package org.verapdf.pdfa.reporting;

import java.util.List;

import org.verapdf.pdfa.flavours.PDFAFlavour;

/**
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *
 */
public interface ValidationResult {
    /**
     * @return true if the PDF/A document complies with the PDF/A specification
     *         / flavour it was validated against.
     */
    public boolean isCompliant();

    /**
     * @return the {@link PDFAFlavour} that identifies the PDF/A specification
     *         part and conformance level enforced by the Validator that
     *         produced this result.
     */
    public PDFAFlavour getPDFAFlavour();

    /**
     * @return the list of {@link TestAssertion}s made during PDF/A validation
     */
    public List<TestAssertion> getTestAssertions();
}
