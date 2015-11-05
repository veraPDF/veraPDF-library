/**
 * 
 */
package org.verapdf.pdfa.validation;

import java.util.Set;

/**
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *
 */
public interface RuleDirectory {
    /**
     * @return the full set of validation {@link Rule}s.
     */
    public Set<Rule> getValidationRules();

    /**
     * @param id the {@link RuleId} of the {@link Rule} to get
     * @return the {@link Rule} with {@link RuleId} == id
     */
    public Rule getRuleById(final RuleId id);

    /**
     * @return the full set of {@link RuleId}s registered in the directory
     */
    public Set<RuleId> getRuleIds();
}
