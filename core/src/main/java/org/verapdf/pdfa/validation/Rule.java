/**
 * 
 */
package org.verapdf.pdfa.validation;

import java.util.List;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * Encapsulates a PDF/A Validation Rule including the String property
 * {@link Rule#getTest()} which is the logical expression that is evaluated when
 * applying the test for this rule.
 * 
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *
 */
@XmlJavaTypeAdapter(RuleImpl.Adapter.class)
public interface Rule {
    
    /**
     * @return the RuleID instance that uniquely identifies this rule
     */
    public RuleId getRuleId();

    /**
     * @return the String name of the PDF Object type to which the Rule applies
     */
    public String getObject();

    /**
     * @return a String name for the Rule
     */
    public String getName();

    /**
     * @return a textual description of the Rule
     */
    public String getDescription();

    /**
     * @return the logical expression that is evaluated when asserting the test
     *         for this rule.
     */
    public String getTest();

    /**
     * @return a List of {@link Reference}s to the specification clause(s) from
     *         which the rule is derived.
     */
    public List<Reference> getReferences();
}
