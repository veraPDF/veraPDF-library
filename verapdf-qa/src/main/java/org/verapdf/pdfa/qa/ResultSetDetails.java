/**
 * 
 */
package org.verapdf.pdfa.qa;

import java.util.Date;

/**
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *
 */
public interface ResultSetDetails {
    /**
     * @return the date that the result set was created
     */
    public Date getDateCreated();

    /**
     * @return the version number of the veraPDF-library used to create the result set
     */
    public String getLibraryVersion();
    
    /**
     * @return the build date for the veraPDF-library used to create the result set
     */
    public Date getLibraryBuildDate();
}
