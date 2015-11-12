/**
 * 
 */
package org.verapdf.pdfa.validation;

import java.util.Set;

/**
 * Interface that encapsulates the behaviour of a directory for validation
 * {@link Rule}s
 * 
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *
 */
public interface RuleDirectory {
    /**
     * Retrieve the full set of Rules held in this validation directory.
     * 
     * @return the full set of validation {@link Rule}s.
     */
    public Set<Rule> getValidationRules();

    /**
     * Retrieve an individual Rule by {@link RuleId}.
     * 
     * @param id
     *            the {@link RuleId} of the {@link Rule} to get
     * @return the {@link Rule} with {@link RuleId} == id
     */
    public Rule getRuleById(final RuleId id);

    /**
     * Retrieve the full set of RuleIds held in the directory.
     * 
     * @return the full set of {@link RuleId}s registered in the directory
     */
    public Set<RuleId> getRuleIds();
}
