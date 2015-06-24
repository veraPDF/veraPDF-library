package org.verapdf.validation.profile.model;

/**
 * Structure of the reference in a rule.
 *
 * @author Maksim Bezrukov
 */
public class Reference {
    private String specification;
    private String clause;

    /**
     * Creates reference model of a rule.
     *
     * @param specification - specification of a rule
     * @param clause        - clause of a rule
     */
    public Reference(String specification, String clause) {
        this.specification = specification;
        this.clause = clause;
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
}
