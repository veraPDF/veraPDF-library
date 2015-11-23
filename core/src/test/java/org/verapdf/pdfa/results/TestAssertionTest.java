/**
 * 
 */
package org.verapdf.pdfa.results;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import nl.jqno.equalsverifier.EqualsVerifier;

import org.junit.Test;
import org.verapdf.pdfa.results.TestAssertion.Status;
import org.verapdf.pdfa.validation.Profiles;

/**
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *
 */
@SuppressWarnings("static-method")
public class TestAssertionTest {
    private static final String DEFAULT_ASSERTION_STRING = "TestAssertion [ruleId="
            + Profiles.defaultRuleId()
            + ", status="
            + Status.FAILED
            + ", message="
            + "message"
            + ", location="
            + ValidationResults.defaultLocation() + "]";

    /**
     * Test method for
     * {@link org.verapdf.pdfa.results.TestAssertionImpl#hashCode()}.
     */
    @Test
    public final void testHashCodeAndEquals() {
        EqualsVerifier.forClass(TestAssertionImpl.class).verify();
    }

    /**
     * Test method for
     * {@link org.verapdf.pdfa.results.TestAssertionImpl#toString()}.
     */
    @Test
    public final void testToString() {
        assertTrue(ValidationResults.defaultAssertion().toString()
                .equals(DEFAULT_ASSERTION_STRING));
    }

    /**
     * Test method for
     * {@link org.verapdf.pdfa.results.TestAssertionImpl#defaultInstance()}.
     */
    @Test
    public final void testDefaultInstance() {
        TestAssertion defaultAssertion = ValidationResults.defaultAssertion();
        assertTrue(defaultAssertion
                .equals(ValidationResults.defaultAssertion()));
        assertTrue(defaultAssertion == ValidationResults.defaultAssertion());
    }

    /**
     * Test method for
     * {@link org.verapdf.pdfa.results.TestAssertionImpl#fromValues(int, org.verapdf.pdfa.validation.RuleId, org.verapdf.pdfa.results.TestAssertion.Status, java.lang.String, org.verapdf.pdfa.results.Location)}
     * .
     */
    @Test
    public final void testFromValues() {
        TestAssertion assertionFromVals = ValidationResults
                .assertionFromValues(0, Profiles.defaultRuleId(),
                        Status.FAILED, "message",
                        ValidationResults.defaultLocation());
        assertTrue(assertionFromVals.equals(ValidationResults
                .defaultAssertion()));
        assertFalse(assertionFromVals == ValidationResults.defaultAssertion());
    }

}
