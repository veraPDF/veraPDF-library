package org.verapdf.validation.profile.model;

import java.util.List;

/**
 * Structure of the reference in a rule.
 *
 * @author Maksim Bezrukov
 */
public class Reference {
    private String specification;
    private String clause;
    private List<Reference> references;

    /**
     * Creates reference model of a rule.
     *
     * @param specification - specification of a rule
     * @param clause        - clause of a rule
     * @param references    - references from this reference
     */
    public Reference(String specification, String clause, List<Reference> references) {
        this.specification = specification;
        this.clause = clause;
        this.references = references;
    }

    /**
     * @return Text in tag "specification".
     */
    public String getSpecification() {
        return specification;
    }

    /**
     * @return Text in tag "clause".
     */
    public String getClause() {
        return clause;
    }

    /**
     * @return List of references used in this reference
     */
    public List<Reference> getReferences() {
        return references;
    }
}
