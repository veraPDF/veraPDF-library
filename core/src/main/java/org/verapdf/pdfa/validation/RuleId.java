/**
 * 
 */
package org.verapdf.pdfa.validation;

import org.verapdf.pdfa.flavours.PDFAFlavour.Specification;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * RuleIds are used to identify the individual {@link Rule}s that make up a
 * {@link ValidationProfile}. A RuleId instance can be traced to the PDF/A
 * specification and clause that was the motivation behind it. A RuleId
 * comprises:
 * <ul>
 * <li>a {@link Specification} instance that identifies the particular PDF/A
 * specification referenced.</li>
 * <li>a String clause identifier, for the PDF/A specifications clauses are
 * identified by a sequence of period separated integers, e.g 6.1.3, that
 * reflect the heading identifiers used in the specification document.</li>
 * <li>an int test number, many specification clauses are further sub-divided
 * into a set of tests.</li>
 * </ul>
 * 
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *
 */
@XmlJavaTypeAdapter(RuleIdImpl.Adapter.class)
public interface RuleId {
    /**
     * @return the
     */
    public Specification getSpecification();
    /**
     * @return the specification clause String identifier.
     */
    public String getClause();

    /**
     * @return the test number for this particular rule under its specification
     *         clause.
     */
    public int getTestNumber();

}
