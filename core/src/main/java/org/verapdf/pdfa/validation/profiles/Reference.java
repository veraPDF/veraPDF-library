/**
 * 
 */
package org.verapdf.pdfa.validation.profiles;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * A Reference is used to correlate a validation {@link Rule} to a specific
 * clause in a PDF/A Specification Part.
 * 
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 */
@XmlJavaTypeAdapter(ReferenceImpl.Adapter.class)
public interface Reference {
    /**
     * @return a {@link Specification} that identifies the PDF/A Specification
     *         referred to.
     */
    public String getSpecification();

    /**
     * @return a String identifier for the Specification clause referred to,
     *         e.g. 6.1.2
     */
    public String getClause();
}
