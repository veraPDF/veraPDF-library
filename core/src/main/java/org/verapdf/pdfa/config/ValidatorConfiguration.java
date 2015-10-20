/**
 * 
 */
package org.verapdf.pdfa.config;

import org.verapdf.pdfa.PDFAValidator;

/**
 * This interface defines the configuration settings applicable to a
 * {@link PDFAValidator} instance.
 * 
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 */
public interface ValidatorConfiguration {
    /**
     * @return true if the configuration requests that a {@link PDFAValidator}
     *         records passed checks as well as failed checks
     */
    public boolean doRecordPassedChecks();

    /**
     * @return an int value that is the limit of failed checks the
     *         {@link PDFAValidator} instance should detect before aborting the
     *         validation process.
     */
    public int getFailedChecksLimit();
}
