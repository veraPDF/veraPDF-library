/**
 * This file is part of veraPDF Library core, a module of the veraPDF project.
 * Copyright (c) 2015, veraPDF Consortium <info@verapdf.org>
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

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * JAXB serialisable implementation of {@link Reference} with safe methods for
 * equals and hashCode plus useful conversion methods.
 *
 * Not meant for public consumption, hidden behind the {@link Reference}
 * interface.
 *
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 */
@XmlRootElement(name = "rule")
final class RuleImpl implements Rule {
    private final static RuleImpl DEFAULT = new RuleImpl();
    @XmlElement
    private final RuleId id;
    @XmlAttribute
    private final String object;
    @XmlAttribute
    private final Boolean deferred;
    @XmlElement
    private final String description;
    @XmlElement
    private final String test;
    @XmlElement
    private final ErrorDetails error;
    @XmlElementWrapper
    @XmlElement(name = "reference")
    private final List<Reference> references = new ArrayList<>();

    private RuleImpl() {
        this(RuleIdImpl.defaultInstance(), "object", null, "description", "test",
                ErrorDetailsImpl.defaultInstance(), Collections.<Reference> emptyList());
    }

    private RuleImpl(final RuleId id, final String object, final Boolean deferred,
            final String description, final String test,
            final ErrorDetails error, final List<Reference> references) {
        super();
        this.id = id;
        this.object = object;
        this.deferred = deferred;
        this.description = description;
        this.test = test;
        this.error = error;
        this.references.addAll(references);
    }

    /**
     * { @inheritDoc }
     */
    @Override
    public RuleId getRuleId() {
        return this.id;
    }

    /**
     * { @inheritDoc }
     */
    @Override
    public String getObject() {
        return this.object;
    }

    /**
     * { @inheritDoc }
     */
    @Override
    public Boolean getDeferred() {
        return this.deferred;
    }

    /**
     * { @inheritDoc }
     */
    @Override
    public String getDescription() {
        return this.description;
    }

    /**
     * { @inheritDoc }
     */
    @Override
    public String getTest() {
        return this.test;
    }

    /**
     * { @inheritDoc }
     */
    @Override
    public ErrorDetails getError() {
        return this.error;
    }

    /**
     * { @inheritDoc }
     */
    @Override
    public List<Reference> getReferences() {
        return Collections.unmodifiableList(this.references);
    }

    /**
     * { @inheritDoc }
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RuleImpl)) return false;

        RuleImpl rule = (RuleImpl) o;

        if (id != null ? !id.equals(rule.id) : rule.id != null) return false;
        if (object != null ? !object.equals(rule.object) : rule.object != null) return false;
        if (deferred != null ? !deferred.equals(rule.deferred) : rule.deferred != null) return false;
        if (description != null ? !description.equals(rule.description) : rule.description != null) return false;
        if (test != null ? !test.equals(rule.test) : rule.test != null) return false;
        if (error != null ? !error.equals(rule.error) : rule.error != null) return false;
        return references != null ? references.equals(rule.references) : rule.references == null;

    }

    /**
     * { @inheritDoc }
     */
    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (object != null ? object.hashCode() : 0);
        result = 31 * result + (deferred != null ? deferred.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (test != null ? test.hashCode() : 0);
        result = 31 * result + (error != null ? error.hashCode() : 0);
        result = 31 * result + (references != null ? references.hashCode() : 0);
        return result;
    }

    /**
     * { @inheritDoc }
     */
    @Override
    public String toString() {
        return "Rule [id=" + this.id + ", object=" + this.object + ", deferred=" + this.deferred
                + ", description=" + this.description + ", test=" + this.test
                + ", error=" + this.error + ", references=" + this.references
                + "]";
    }

    static RuleImpl defaultInstance() {
        return RuleImpl.DEFAULT;
    }

    static RuleImpl fromValues(final RuleId id, final String object, final Boolean deferred,
            final String description, final String test,
            final ErrorDetails error, final List<Reference> references) {
        return new RuleImpl(RuleIdImpl.fromRuleId(id), object, deferred, description, test, error, references);
    }

    static RuleImpl fromRule(final Rule toConvert) {
        return RuleImpl.fromValues(
                RuleIdImpl.fromRuleId(toConvert.getRuleId()),
                toConvert.getObject(), toConvert.getDeferred(), toConvert.getDescription(),
                toConvert.getTest(), toConvert.getError(),
                toConvert.getReferences());
    }

    public static String getStringWithoutProfilesTabulation(String string) {
        if (string == null || string.isEmpty()) {
            return string;
        }
        return string.replaceAll("[\\s\u00A0\u2007\u202F]+", " ");
    }

    static class Adapter extends XmlAdapter<RuleImpl, Rule> {
        @Override
        public Rule unmarshal(RuleImpl ruleImpl) {
            return new RuleImpl(ruleImpl.getRuleId(), ruleImpl.getObject(), ruleImpl.getDeferred(),
                    RuleImpl.getStringWithoutProfilesTabulation(ruleImpl.getDescription()),
                    RuleImpl.getStringWithoutProfilesTabulation(ruleImpl.getTest()),
                    ruleImpl.getError(), ruleImpl.getReferences());
        }

        @Override
        public RuleImpl marshal(Rule rule) {
            return (RuleImpl) rule;
        }
    }
}
