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

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.adapters.XmlAdapter;

import org.verapdf.pdfa.flavours.PDFAFlavour.Specification;

/**
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *
 */
@XmlRootElement(name = "ruleId")
final class RuleIdImpl implements RuleId {
    private static final RuleIdImpl DEFAULT = new RuleIdImpl();
    @XmlAttribute
    private final Specification specification;
    @XmlAttribute
    private final String clause;
    @XmlAttribute
    private final int testNumber;

    private RuleIdImpl() {
        this(Specification.NO_STANDARD, "clause", 0);
    }

    private RuleIdImpl(final Specification specification, final String clause,
            final int testNumber) {
        super();
        this.specification = specification;
        this.clause = clause;
        this.testNumber = testNumber;
    }

    /**
     * { @inheritDoc }
     */
    @Override
    public Specification getSpecification() {
        return this.specification;
    }

    /**
     * { @inheritDoc }
     */
    @Override
    public String getClause() {
        return this.clause;
    }

    /**
     * { @inheritDoc }
     */
    @Override
    public int getTestNumber() {
        return this.testNumber;
    }

    /**
     * { @inheritDoc }
     */
    @Override
    public final int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((this.clause == null) ? 0 : this.clause.hashCode());
        result = prime
                * result
                + ((this.specification == null) ? 0 : this.specification
                        .hashCode());
        result = prime * result + this.testNumber;
        return result;
    }

    /**
     * { @inheritDoc }
     */
    @Override
    public final boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof RuleId))
            return false;
        RuleId other = (RuleId) obj;
        if (this.clause == null) {
            if (other.getClause() != null)
                return false;
        } else if (!this.clause.equals(other.getClause()))
            return false;
        if (this.specification != other.getSpecification())
            return false;
        return this.testNumber == other.getTestNumber();
    }

    /**
     * { @inheritDoc }
     */
    @Override
    public final String toString() {
        return "RuleId [specification=" + this.specification.toString()
                + ", clause=" + this.clause + ", testNumber=" + this.testNumber
                + "]";
    }

    static RuleId defaultInstance() {
        return RuleIdImpl.DEFAULT;
    }

    static RuleId fromValues(final Specification specifcation,
            final String clause, final int testNumber) {
        return new RuleIdImpl(specifcation, clause, testNumber);
    }

    static RuleId fromRuleId(final RuleId toConvert) {
        return fromValues(toConvert.getSpecification(), toConvert.getClause(),
                toConvert.getTestNumber());
    }

    static class Adapter extends XmlAdapter<RuleIdImpl, RuleId> {
        @Override
        public RuleId unmarshal(RuleIdImpl ruleIdImpl) {
            return ruleIdImpl;
        }

        @Override
        public RuleIdImpl marshal(RuleId ruleId) {
            return (RuleIdImpl) ruleId;
        }
    }
}
