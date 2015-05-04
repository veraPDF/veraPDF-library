package org.verapdf.validation.profile.model;

/**
 * Structure of the reference in a rule.
 * Created by bezrukov on 4/24/15.
 *
 * @author Maksim Bezrukov
 * @version 1.0
 * @see Rule
 */
public class Reference {
    private String specification;
    private String clause;

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
