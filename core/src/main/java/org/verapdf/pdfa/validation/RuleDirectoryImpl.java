/**
 * 
 */
package org.verapdf.pdfa.validation;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.verapdf.core.Directory;
import org.verapdf.core.MapBackedDirectory;

/**
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *
 */
class RuleDirectoryImpl implements RuleDirectory {
    private final Directory<RuleId, Rule> rules;

    private RuleDirectoryImpl(final Set<Rule> rules) {
        this(createRuleMapFromSet(rules));
    }

    private RuleDirectoryImpl(final Map<RuleId, Rule> rules) {
        super();
        this.rules = new MapBackedDirectory<>(rules);
    }

    /**
     * { @inheritDoc }
     */
    @Override
    public Set<Rule> getValidationRules() {
        return Collections
                .unmodifiableSet(new HashSet<>(this.rules.getItems()));
    }

    /**
     * { @inheritDoc }
     */
    @Override
    public Rule getRuleById(RuleId id) {
        return this.rules.getItem(id);
    }

    /**
     * { @inheritDoc }
     */
    @Override
    public Set<RuleId> getRuleIds() {
        return Collections.unmodifiableSet(this.rules.getKeys());
    }

    static Map<RuleId, Rule> createRuleMapFromSet(final Set<Rule> rules) {
        final Map<RuleId, Rule> retVal = new HashMap<>();
        for (Rule rule : rules) {
            retVal.put(rule.getRuleId(), rule);
        }
        return retVal;
    }
}
