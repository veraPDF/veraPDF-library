/**
 * 
 */
package org.verapdf.pdfa.validation;

import java.util.List;

/**
 * Encapsulates a PDF/A Validation Rule including the String property
 * {@link Rule#getTest()} which is the logical expression that is evaluated when
 * applying the test for this rule.
 * 
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *
 */
public interface Rule {
    /**
     * @return a String ID for the Rule, usually a combination of specification
     *         clause and test number.
     */
    public String getId();

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
