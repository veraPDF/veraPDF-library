/**
 * 
 */
package org.verapdf.pdfa.validation.profiles;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlAdapter;

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
    private final static VariableImpl DEFAULT = new VariableImpl();
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
        if (this.name == null) {
            if (other.getName() != null)
                return false;
        } else if (!this.name.equals(other.getName()))
            return false;
        if (this.value == null) {
            if (other.getValue() != null)
                return false;
        } else if (!this.value.equals(other.getValue()))
            return false;
        if (this.defaultValue == null) {
            if (other.getDefaultValue() != null)
                return false;
        } else if (!this.defaultValue.equals(other.getDefaultValue()))
            return false;
        if (this.object == null) {
            if (other.getObject() != null)
                return false;
        } else if (!this.object.equals(other.getObject()))
            return false;
        return true;
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
