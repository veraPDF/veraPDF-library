/**
 * 
 */
package org.verapdf.pdfa.validation;

import org.verapdf.pdfa.flavours.PDFAFlavour.Part;

/**
 * A Reference is used to correlate a validation {@link Rule} to a specific
 * clause in a PDF/A Specification Part.
 * 
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 */
public interface Reference {
    /**
     * @return a {@link Part} that identifies the PDF/A Specification referred
     *         to.
     */
    public Part getSpecification();

    /**
     * @return a String identifier for the Specification clause referred to, e.g. 6.1.2
     */
    public String getClause();
}
