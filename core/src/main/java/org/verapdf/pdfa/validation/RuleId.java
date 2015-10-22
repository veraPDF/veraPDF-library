/**
 * 
 */
package org.verapdf.pdfa.validation;

import org.verapdf.pdfa.flavours.PDFAFlavour.Part;

/**
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *
 */
public interface RuleId {
    /**
     * @return the 
     */
    public Part getSpecfication();
    /**
     * @return the specification clause String identifier.
     */
    public String getClause();
    
    /**
     * @return the test number for this particular rule under its specification clause.
     */
    public int getTestNumber();

}
