package org.verapdf.validation.profile.model;

import java.util.Collections;
import java.util.List;

/**
 * Structure of the reference in a rule.
 *
 * @author Maksim Bezrukov
 */
public class Reference {
    private final String specification;
    private final String clause;
    private final List<Reference> references;

    /**
     * Creates reference model of a rule.
     *
     * @param specification - specification of a rule
     * @param clause        - clause of a rule
     * @param references    - references from this reference
     */
    public Reference(final String specification, final String clause, final List<Reference> references) {
        this.specification = specification;
        this.clause = clause;
        this.references = Collections.unmodifiableList(references);
    }

    /**
     * @return Text in tag "specification".
     */
    public String getSpecification() {
        return this.specification;
    }

    /**
     * @return Text in tag "clause".
     */
    public String getClause() {
        return this.clause;
    }

    /**
     * @return List of references used in this reference
     */
    public List<Reference> getReferences() {
        return this.references;
    }
}
