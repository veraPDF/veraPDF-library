/**
 * 
 */
package org.verapdf.pdfa.results;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlAdapter;

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
    @XmlAttribute
    private final int totalAssertions;
    @XmlElementWrapper
    @XmlElement(name = "assertion")
    private final Set<TestAssertion> assertions;
    @XmlAttribute
    private final boolean isCompliant;

    private ValidationResultImpl() {
        this(PDFAFlavour.NO_FLAVOUR, Collections.<TestAssertion> emptySet(),
                false);
    }

    private ValidationResultImpl(final PDFAFlavour flavour,
            final Set<TestAssertion> assertions, final boolean isCompliant) {
        this(flavour, assertions, isCompliant, assertions.size());
    }

    private ValidationResultImpl(final PDFAFlavour flavour,
            final Set<TestAssertion> assertions, final boolean isCompliant,
            int totalAssertions) {
        super();
        this.flavour = flavour;
        this.assertions = new HashSet<>(assertions);
        this.isCompliant = isCompliant;
        this.totalAssertions = totalAssertions;
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
    public int getTotalAssertions() {
        return this.totalAssertions;
    }

    /**
     * { @inheritDoc }
     */
    @Override
    public Set<TestAssertion> getTestAssertions() {
        return Collections.unmodifiableSet(this.assertions);
    }

    /**
     * { @inheritDoc }
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((this.assertions == null) ? 0 : this.assertions.hashCode());
        result = prime * result + ((this.flavour == null) ? 0 : this.flavour.hashCode());
        result = prime * result + (this.isCompliant ? 1231 : 1237);
        result = prime * result + this.totalAssertions;
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
        if (getClass() != obj.getClass())
            return false;
        if (!(obj instanceof ValidationResult))
            return false;
        ValidationResult other = (ValidationResult) obj;
        if (this.assertions == null) {
            if (other.getTestAssertions() != null)
                return false;
        } else if (other.getTestAssertions() == null)
            return false;
        else if (!this.assertions.equals(other.getTestAssertions()))
            return false;
        if (this.flavour != other.getPDFAFlavour())
            return false;
        if (this.isCompliant != other.isCompliant())
            return false;
        if (this.totalAssertions != other.getTotalAssertions())
            return false;
        return true;
    }

    /**
     * { @inheritDoc }
     */
    @Override
    public String toString() {
        return "ValidationResult [flavour=" + this.flavour + ", totalAssertions="
                + this.totalAssertions + ", assertions=" + this.assertions + ", isCompliant="
                + this.isCompliant + "]";
    }

    static ValidationResultImpl defaultInstance() {
        return DEFAULT;
    }

    static ValidationResultImpl fromValues(final PDFAFlavour flavour,
            final Set<TestAssertion> assertions, final boolean isCompliant, final int totalChecks) {
        return new ValidationResultImpl(flavour, assertions, isCompliant, totalChecks);
    }

    static ValidationResultImpl fromValidationResult(ValidationResult toConvert) {
        return fromValues(toConvert.getPDFAFlavour(),
                toConvert.getTestAssertions(), toConvert.isCompliant(), toConvert.getTotalAssertions());
    }

    static String toXml(final ValidationResult toConvert, Boolean prettyXml)
            throws JAXBException, IOException {
        String retVal = "";
        try (StringWriter writer = new StringWriter()) {
            toXml(toConvert, writer, prettyXml);
            retVal = writer.toString();
            return retVal;
        }
    }

    static void toXml(final ValidationResult toConvert,
            final OutputStream stream, Boolean prettyXml) throws JAXBException {
        Marshaller varMarshaller = getMarshaller(prettyXml);
        varMarshaller.marshal(toConvert, stream);
    }

    static ValidationResultImpl fromXml(final InputStream toConvert)
            throws JAXBException {
        Unmarshaller stringUnmarshaller = getUnmarshaller();
        return (ValidationResultImpl) stringUnmarshaller.unmarshal(toConvert);
    }

    static void toXml(final ValidationResult toConvert, final Writer writer,
            Boolean prettyXml) throws JAXBException {
        Marshaller varMarshaller = getMarshaller(prettyXml);
        varMarshaller.marshal(toConvert, writer);
    }

    static ValidationResultImpl fromXml(final Reader toConvert)
            throws JAXBException {
        Unmarshaller stringUnmarshaller = getUnmarshaller();
        return (ValidationResultImpl) stringUnmarshaller.unmarshal(toConvert);
    }

    static ValidationResultImpl fromXml(final String toConvert)
            throws JAXBException {
        try (StringReader reader = new StringReader(toConvert)) {
            return fromXml(reader);
        }
    }

    static class Adapter extends
            XmlAdapter<ValidationResultImpl, ValidationResult> {
        @Override
        public ValidationResult unmarshal(
                ValidationResultImpl validationResultImpl) {
            return validationResultImpl;
        }

        @Override
        public ValidationResultImpl marshal(ValidationResult validationResult) {
            return (ValidationResultImpl) validationResult;
        }
    }

    private static Unmarshaller getUnmarshaller() throws JAXBException {
        JAXBContext context = JAXBContext
                .newInstance(ValidationResultImpl.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        return unmarshaller;
    }

    private static Marshaller getMarshaller(Boolean setPretty)
            throws JAXBException {
        JAXBContext context = JAXBContext
                .newInstance(ValidationResultImpl.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, setPretty);
        return marshaller;
    }
}
