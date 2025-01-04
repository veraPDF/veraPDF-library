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

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.util.Objects;

/**
 * JAXB serialisable implementation of {@link Variable} with safe methods for
 * equals and hashCode plus useful conversion methods.
 * 
 * Not meant for public consumption, hidden behind the {@link Variable}
 * interface.
 * 
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 */
@XmlRootElement(name = "variable")
final class VariableImpl implements Variable {
    private static final VariableImpl DEFAULT = new VariableImpl();
    @XmlAttribute
    private final String name;
    @XmlAttribute
    private final String object;
    @XmlElement
    private final String defaultValue;
    @XmlElement
    private final String value;

    private VariableImpl() {
        this("name", "object", "defaultValue", "value");
    }

    private VariableImpl(final String name, final String object,
            final String defaultValue, String value) {
        super();
        this.name = name;
        this.object = object;
        this.defaultValue = defaultValue;
        this.value = value;
    }

    /**
     * @return the name
     */
    @Override
    public String getName() {
        return this.name;
    }

    /**
     * @return the object
     */
    @Override
    public String getObject() {
        return this.object;
    }

    /**
     * @return the defaultValue
     */
    @Override
    public String getDefaultValue() {
        return this.defaultValue;
    }

    /**
     * @return the value
     */
    @Override
    public String getValue() {
        return this.value;
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
        result = prime
                * result
                + ((this.defaultValue == null) ? 0 : this.defaultValue
                        .hashCode());
        result = prime * result
                + ((this.name == null) ? 0 : this.name.hashCode());
        result = prime * result
                + ((this.object == null) ? 0 : this.object.hashCode());
        result = prime * result
                + ((this.value == null) ? 0 : this.value.hashCode());
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
        if (!(obj instanceof Variable))
            return false;
        Variable other = (Variable) obj;
        if (!Objects.equals(this.getName(), other.getName())) {
            return false;
        }
        if (!Objects.equals(this.getValue(), other.getValue())) {
            return false;
        }
        if (!Objects.equals(this.getDefaultValue(), other.getDefaultValue())) {
            return false;
        }
        return Objects.equals(this.getObject(), other.getObject());
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "Variable [name=" + this.name + ", object=" + this.object
                + ", defaultValue=" + this.defaultValue + ", value="
                + this.value + "]";
    }

    static VariableImpl defaultInstance() {
        return VariableImpl.DEFAULT;
    }

    static VariableImpl fromValues(final String name, final String object,
            final String defaultValue, final String value) {
        return new VariableImpl(name, object, defaultValue, value);
    }

    static VariableImpl fromVariable(final Variable toConvert) {
        return new VariableImpl(toConvert.getName(), toConvert.getObject(),
                toConvert.getDefaultValue(), toConvert.getValue());
    }

    static class Adapter extends XmlAdapter<VariableImpl, Variable> {
        @Override
        public Variable unmarshal(VariableImpl variableImpl) {
            return variableImpl;
        }

        @Override
        public VariableImpl marshal(Variable variable) {
            return (VariableImpl) variable;
        }
    }
}
