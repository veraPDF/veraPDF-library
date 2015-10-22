/**
 * 
 */
package org.verapdf.pdfa.reporting;

/**
 * Used to record the location of specific test assertions during the validation
 * process. TODO: A better definition of this class and the terms it
 * encapsulates (Level and Context).
 * 
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 */
public interface Location {
    /**
     * @return the level of the location within the PDF Document
     */
    public String getLevel();

    /**
     * @return the Location's context within the PDF Document
     */
    public String getContext();
}
