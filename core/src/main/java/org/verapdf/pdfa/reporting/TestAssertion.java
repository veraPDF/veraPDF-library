/**
 * 
 */
package org.verapdf.pdfa.reporting;

/**
 * A TestAssertion records the result of performing a validation test on a
 * particular document property, or set of properties.
 * 
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 */
public interface TestAssertion {
    /**
     * @return the String id for the Rule
     */
    public String getRuleId();

    /**
     * @return the {@link Status} that indicates the result of this test
     *         assertion
     */
    public Status getStatus();

    /**
     * @return any message that accompanies this assertion, usually an error
     *         message. Returns an empty string if there is no message.
     */
    public String getMessage();

    /**
     * @return the {@link Location} within the PDF document where the test was
     *         asserted.
     */
    public Location getLocation();

    /**
     * Enum that indicates the result of a particular test assertion, i.e.
     * whether the test passed or failed
     * 
     * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
     */
    public enum Status {
        /**
         * Indicates that a test assertion passed
         */
        PASSED,
        /**
         * Indicates a test failure
         */
        FAILED;
    }
}
