/**
 * This file is part of veraPDF Library core, a module of the veraPDF project.
 * Copyright (c) 2015-2025, veraPDF Consortium <info@verapdf.org>
 * All rights reserved.
 *
 * veraPDF Library core is free software: you can redistribute it and/or modify
 * it under the terms of either:
 *
 * The GNU General public license GPLv3+.
 * You should have received a copy of the GNU General Public License
 * along with veraPDF Library core as the LICENSE.GPL file in the root of the source
 * tree.  If not, see http://www.gnu.org/licenses/ or
 * https://www.gnu.org/licenses/gpl-3.0.en.html.
 *
 * The Mozilla Public License MPLv2+.
 * You should have received a copy of the Mozilla Public License along with
 * veraPDF Library core as the LICENSE.MPL file in the root of the source tree.
 * If a copy of the MPL was not distributed with this file, you can obtain one at
 * http://mozilla.org/MPL/2.0/.
 */
/**
 *
 */
package org.verapdf.pdfa.validation.profiles;

import java.util.*;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.verapdf.pdfa.flavours.PDFAFlavour;

/**
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 */
@XmlRootElement(namespace = "http://www.verapdf.org/ValidationProfile", name = "profile")
final class ValidationProfileImpl implements ValidationProfile {
    private Map<String, Set<Rule>> objectRuleMap;
    private Map<String, Set<Variable>> objectVariableMap;
    private final Map<RuleId, Rule> ruleLookup = new HashMap<>();
    private static final ValidationProfileImpl DEFAULT = new ValidationProfileImpl();
    private final Object lock = new Object();

    @XmlAttribute
    private final PDFAFlavour flavour;
    @XmlElement
    private final ProfileDetails details;
    @XmlElement
    private final String hash;
    @XmlElementWrapper
    @XmlElement(name = "rule")
    private final Set<Rule> rules;
    @XmlElementWrapper
    @XmlElement(name = "variable")
    private final Set<Variable> variables;

    private ValidationProfileImpl() {
        this(PDFAFlavour.NO_FLAVOUR, ProfileDetailsImpl.defaultInstance(),
                "hash", Collections.emptySet(), Collections.emptySet());
    }

    private ValidationProfileImpl(final PDFAFlavour flavour,
            final ProfileDetails details, final String hash,
            final Set<Rule> rules, final Set<Variable> variables) {
        super();
        this.flavour = flavour;
        this.details = details;
        this.hash = hash;
        this.rules = new HashSet<>(rules);
        this.variables = new HashSet<>(variables);
    }

    private ValidationProfileImpl(final PDFAFlavour flavour,
                                  final ProfileDetails details, final String hash,
                                  final SortedSet<Rule> rules, final SortedSet<Variable> variables) {
        super();
        this.flavour = flavour;
        this.details = details;
        this.hash = hash;
        this.rules = rules;
        this.variables = variables;
    }

    /**
     * { @inheritDoc }
     */
    @Override
    public PDFAFlavour getPDFAFlavour() {
        return this.flavour;
    }

    /**
     * { @inheritDoc }
     */
    @Override
    public ProfileDetails getDetails() {
        return this.details;
    }

    /**
     * { @inheritDoc }
     */
    @Override
    public String getHexSha1Digest() {
        return this.hash;
    }

    /**
     * { @inheritDoc }
     */
    @Override
    public Set<Rule> getRules() {
        return Collections.unmodifiableSet(this.rules);
    }

    /**
     * { @inheritDoc }
     */
    @Override
    public Set<Variable> getVariables() {
        return Collections.unmodifiableSet(this.variables);
    }

    /**
     * { @inheritDoc }
     */
    @Override
    public Set<Rule> getRulesByObject(final String objectName) {
        synchronized (this.lock) {
            if (this.objectRuleMap == null) {
                this.objectRuleMap = createObjectRuleMap(this.rules);
            }
        }
        Set<Rule> objRules = this.objectRuleMap.get(objectName);
        return objRules == null ? Collections.emptySet() : Collections.unmodifiableSet(objRules);
    }

    /**
     * { @inheritDoc }
     */
    @Override
    public Set<Variable> getVariablesByObject(String objectName) {
        synchronized (this.lock) {
            if (this.objectVariableMap == null) {
                this.objectVariableMap = createObjectVariableMap(this.variables);
            }
        }
        Set<Variable> objRules = this.objectVariableMap.get(objectName);
        return objRules == null ? Collections.emptySet() : Collections.unmodifiableSet(objRules);
    }

    /**
     * { @inheritDoc }
     */
    @Override
    public Rule getRuleByRuleId(RuleId id) {
        synchronized (this.lock) {
            if (this.ruleLookup.isEmpty()) {
                this.objectRuleMap = createObjectRuleMap(this.rules);
            }
        }
        return this.ruleLookup.get(id);
    }

    @Override
    public SortedSet<String> getTags() {
        SortedSet<String> tags = new TreeSet<>();
        for (Rule rule : rules) {
            tags.addAll(rule.getTagsSet());
        }
        return tags;
    }

    /**
     * { @inheritDoc }
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((this.details == null) ? 0 : this.details.hashCode());
        result = prime * result
                + ((this.flavour == null) ? 0 : this.flavour.hashCode());
        result = prime * result
                + ((this.hash == null) ? 0 : this.hash.hashCode());
        result = prime * result
                + ((this.rules == null) ? 0 : this.rules.hashCode());
        result = prime * result
                + ((this.variables == null) ? 0 : this.variables.hashCode());
        return result;
    }

    /**
     * { @inheritDoc }
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof ValidationProfile))
            return false;
        ValidationProfile other = (ValidationProfile) obj;
        if (this.flavour != other.getPDFAFlavour())
            return false;
        if (!Objects.equals(this.getHexSha1Digest(), other.getHexSha1Digest())) {
            return false;
        }
        if (!Objects.equals(this.getDetails(), other.getDetails())) {
            return false;
        }
        if (!Objects.equals(this.getRules(), other.getRules())) {
            return false;
        }
        return Objects.equals(this.getVariables(), other.getVariables());
    }

    /**
     * { @inheritDoc }
     */
    @Override
    public String toString() {
        return "ValidationProfile [flavour=" + this.flavour + ", details="
                + this.details + ", hash=" + this.hash + ", rules="
                + this.rules + ", variables=" + this.variables + "]";
    }

    static ValidationProfile defaultInstance() {
        return ValidationProfileImpl.DEFAULT;
    }

    static ValidationProfile fromValues(final PDFAFlavour flavour,
            final ProfileDetails details, final String hash,
            final Set<Rule> rules, final Set<Variable> variables) {
        return new ValidationProfileImpl(flavour, details, hash, rules,
                variables);
    }

    static ValidationProfile fromSortedValues(final PDFAFlavour flavour,
                                                  final ProfileDetails details, final String hash,
                                                  final SortedSet<Rule> rules, final SortedSet<Variable> variables) {
        return new ValidationProfileImpl(flavour, details, hash, rules,
                variables);
    }

    static class Adapter extends
            XmlAdapter<ValidationProfileImpl, ValidationProfile> {
        @Override
        public ValidationProfileImpl unmarshal(ValidationProfileImpl profileImpl) {
            return profileImpl;
        }

        @Override
        public ValidationProfileImpl marshal(ValidationProfile profile) {
            return (ValidationProfileImpl) profile;
        }
    }

    private Map<String, Set<Rule>> createObjectRuleMap(
            final Set<Rule> rulesToSet) {
        this.ruleLookup.clear();
        Map<String, Set<Rule>> rulesByObject = new HashMap<>();
        for (Rule rule : rulesToSet) {
            this.ruleLookup.put(rule.getRuleId(), rule);
            if (!rulesByObject.containsKey(rule.getObject())) {
                rulesByObject.put(rule.getObject(), new HashSet<>());
            }
            rulesByObject.get(rule.getObject()).add(rule);
        }
        return rulesByObject;
    }

    private static Map<String, Set<Variable>> createObjectVariableMap(
            final Set<Variable> variables) {
        Map<String, Set<Variable>> variablesByObject = new HashMap<>();
        for (Variable rule : variables) {
            if (!variablesByObject.containsKey(rule.getObject())) {
                variablesByObject.put(rule.getObject(), new HashSet<>());
            }
            variablesByObject.get(rule.getObject()).add(rule);
        }
        return variablesByObject;
    }
}
