/**
 * 
 */
package org.verapdf.pdfa.validation;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.verapdf.pdfa.flavours.PDFAFlavour.Specification;

/**
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *
 */
@XmlJavaTypeAdapter(RuleIdImpl.Adapter.class)
public interface RuleId {
    /**
     * @return the 
     */
    public Specification getSpecfication();
    /**
     * @return the specification clause String identifier.
     */
    public String getClause();
    
    /**
     * @return the test number for this particular rule under its specification clause.
     */
    public int getTestNumber();

}
