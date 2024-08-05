/**
 * This file is part of veraPDF Library core, a module of the veraPDF project.
 * Copyright (c) 2015-2024, veraPDF Consortium <info@verapdf.org>
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
import java.util.Objects;

/**
 * JAXB serialisable implementation of {@link Reference} with safe methods for
 * equals and hashCode plus useful conversion methods.
 * 
 * Not meant for public consumption, hidden behind the {@link Reference}
 * interface.
 * 
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 */
@XmlRootElement(name = "reference")
final class ReferenceImpl implements Reference {
    private static final ReferenceImpl DEFAULT = new ReferenceImpl();
    @XmlAttribute
    private final String specification;
    @XmlAttribute
    private final String clause;
    
    private ReferenceImpl() {
        this("specification", "clause");
    }

    private ReferenceImpl(final String specification, final String clause) {
        super();
        this.specification = specification;
        this.clause = clause;
    }

    /**
     * @return the specification
     */
    @Override
    public String getSpecification() {
        return this.specification;
    }

    /**
     * @return the clause
     */
    @Override
    public String getClause() {
        return this.clause;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
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
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public final boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof Reference))
            return false;
        Reference other = (Reference) obj;
        if (!Objects.equals(this.getClause(), other.getClause())) {
            return false;
        }
        return Objects.equals(this.getSpecification(), other.getSpecification());
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public final String toString() {
        return "Reference [specification=" + this.specification + ", clause="
                + this.clause + "]";
    }

    static Reference defaultInstance() {
        return ReferenceImpl.DEFAULT;
    }

    static Reference fromValues(final String specification,
            final String clause) {
        return new ReferenceImpl(specification, clause);
    }

    static class Adapter extends XmlAdapter<ReferenceImpl, Reference> {
        @Override
        public Reference unmarshal(ReferenceImpl refImpl) { return refImpl; }
        @Override
        public ReferenceImpl marshal(Reference ref) { return (ReferenceImpl)ref; }
      }
    }
