/**
 * 
 */
package org.verapdf.pdfa.results;

import org.verapdf.pdfa.flavours.PDFAFlavour;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.Set;

/**
 * Created as the result of validating a PDF/A document against a
 * {@link org.verapdf.pdfa.validation.ValidationProfile}. The class encapsulates
 * the following information:
 * <ul>
 * <li>a boolean flag indicating whether the PDF/A document validated complied
 * with ValidationProfile.</li>
 * <li>the {@link PDFAFlavour} of the profile used to validate the PDF/A
 * document.</li>
 * <li>a Set of {@link TestAssertion}s representing the results of performing
 * individual tests.</li>
 * </ul>
 * A particular {@link ValidationResult} instance is not guaranteed to carry all
 * of the information generated during validation. While the
 * <code>isCompliant()</code> value MUST reflect the result of validation,
 * individual validator implementations are not obliged to record all, or any
 * individual {@link TestAssertion}s. Such implementations may choose to only
 * record failed tests, or may be configured to abort validation after a so many
 * failed tests.
 *
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *
 */
@XmlJavaTypeAdapter(ValidationResultImpl.Adapter.class)
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
     * @return the total number of valdiation checks performed 
     */
    public int getTotalAssertions();

    /**
     * @return the list of {@link TestAssertion}s made during PDF/A validation
     */
    public Set<TestAssertion> getTestAssertions();
}
