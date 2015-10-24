/**
 * 
 */
package org.verapdf.pdfa.reporting;

import java.util.Collections;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import org.verapdf.pdfa.ValidationResult;
import org.verapdf.pdfa.flavours.PDFAFlavour;

/**
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *
 */
@XmlRootElement(name = "validationResult")
final class ValidationResultImpl implements ValidationResult {
    private final static ValidationResultImpl DEFAULT = new ValidationResultImpl();
    @XmlAttribute
    private final PDFAFlavour flavour;
    @XmlElementWrapper
    @XmlElement(name = "assertion")
    private final List<TestAssertion> assertions;
    @XmlAttribute
    private final boolean isCompliant;

    private ValidationResultImpl() {
        this(PDFAFlavour.NO_FLAVOUR, Collections.EMPTY_LIST, false);
    }

    private ValidationResultImpl(final PDFAFlavour flavour,
            final List<TestAssertion> assertions, final boolean isCompliant) {
        super();
        this.flavour = flavour;
        this.assertions = assertions;
        this.isCompliant = isCompliant;
    }

    /**
     * { @inheritDoc }
     */
    @Override
    public boolean isCompliant() {
        return this.isCompliant;
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
    public List<TestAssertion> getTestAssertions() {
        return this.assertions;
    }

    /**
     * { @inheritDoc }
     */
    @Override
    public final int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((this.assertions == null) ? 0 : this.assertions.hashCode());
        result = prime * result
                + ((this.flavour == null) ? 0 : this.flavour.hashCode());
        result = prime * result + (this.isCompliant ? 1231 : 1237);
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
        if (!(obj instanceof ValidationResult))
            return false;
        ValidationResult other = (ValidationResult) obj;
        if (this.assertions == null) {
            if (other.getTestAssertions() != null)
                return false;
        } else if (!this.assertions.equals(other.getTestAssertions()))
            return false;
        if (this.flavour != other.getPDFAFlavour())
            return false;
        if (this.isCompliant != other.isCompliant())
            return false;
        return true;
    }

    /**
     * { @inheritDoc }
     */
    @Override
    public String toString() {
        return "ValidationResultImpl [flavour=" + this.flavour
                + ", assertions=" + this.assertions + ", isCompliant="
                + this.isCompliant + "]";
    }

    static ValidationResultImpl defaultInstance() {
        return DEFAULT;
    }

    static ValidationResultImpl fromValues(final PDFAFlavour flavour,
            final List<TestAssertion> assertions, final boolean isCompliant) {
        return new ValidationResultImpl(flavour, assertions, isCompliant);
    }
}
