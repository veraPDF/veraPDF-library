/**
 *
 */
package org.verapdf.pdfa.validation;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Collections;
import java.util.Date;
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

import org.verapdf.pdfa.ValidationProfile;
import org.verapdf.pdfa.flavours.PDFAFlavour;

/**
 * @author cfw
 *
 */
@XmlRootElement(namespace = "http://www.verapdf.org/ValidationProfile", name = "profile")
final class ValidationProfileImpl implements ValidationProfile {
    private final static ValidationProfileImpl DEFAULT = new ValidationProfileImpl();
    @XmlAttribute
    private final PDFAFlavour flavour;
    @XmlElement
    private final String name;
    @XmlElement
    private final String description;
    @XmlAttribute
    private final String creator;
    @XmlAttribute
    private final Date created;
    @XmlElement
    private final String hash;
    @XmlElementWrapper
    @XmlElement(name = "rule")
    private final Set<Rule> rules;
    @XmlElementWrapper
    @XmlElement(name = "variable")
    private final Set<Variable> variables;

    private ValidationProfileImpl() {
        this(PDFAFlavour.NO_FLAVOUR, "name", "description", "creator",
                new Date(0L), "hash", Collections.EMPTY_SET,
                Collections.EMPTY_SET);
    }

    private ValidationProfileImpl(final PDFAFlavour flavour, final String name,
            final String description, final String creator, final Date created,
            final String hash, final Set<Rule> rules,
            final Set<Variable> variables) {
        super();
        this.flavour = flavour;
        this.name = name;
        this.description = description;
        this.creator = creator;
        this.created = created;
        this.hash = hash;
        this.rules = new HashSet<>(rules);
        this.variables = new HashSet<>(variables);
    }

    /**
     * { @inheritDoc }
     */
    @Override
    public String getName() {
        return this.name;
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
    public String getCreator() {
        return this.creator;
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
    public Date getDateCreated() {
        return this.created;
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
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((this.created == null) ? 0 : this.created.hashCode());
        result = prime * result
                + ((this.creator == null) ? 0 : this.creator.hashCode());
        result = prime
                * result
                + ((this.description == null) ? 0 : this.description.hashCode());
        result = prime * result
                + ((this.flavour == null) ? 0 : this.flavour.hashCode());
        result = prime * result
                + ((this.hash == null) ? 0 : this.hash.hashCode());
        result = prime * result
                + ((this.name == null) ? 0 : this.name.hashCode());
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
        if (this.created == null) {
            if (other.getDateCreated() != null)
                return false;
        } else if (!this.created.equals(other.getDateCreated()))
            return false;
        if (this.creator == null) {
            if (other.getCreator() != null)
                return false;
        } else if (!this.creator.equals(other.getCreator()))
            return false;
        if (this.description == null) {
            if (other.getDescription() != null)
                return false;
        } else if (!this.description.equals(other.getDescription()))
            return false;
        if (this.flavour != other.getPDFAFlavour())
            return false;
        if (this.hash == null) {
            if (other.getHexSha1Digest() != null)
                return false;
        } else if (!this.hash.equals(other.getHexSha1Digest()))
            return false;
        if (this.name == null) {
            if (other.getName() != null)
                return false;
        } else if (!this.name.equals(other.getName()))
            return false;
        if (this.rules == null) {
            if (other.getRules() != null)
                return false;
        } else if (!this.rules.equals(other.getRules()))
            return false;
        if (this.variables == null) {
            if (other.getVariables() != null)
                return false;
        } else if (!this.variables.equals(other.getVariables()))
            return false;
        return true;
    }

    /**
     * { @inheritDoc }
     */
    @Override
    public String toString() {
        return "ValidationProfile [flavour=" + this.flavour + ", name="
                + this.name + ", description=" + this.description
                + ", creator=" + this.creator + ", created=" + this.created
                + ", hash=" + this.hash + ", rules=" + this.rules
                + ", variables=" + this.variables + "]";
    }

    static ValidationProfileImpl defaultInstance() {
        return ValidationProfileImpl.DEFAULT;
    }

    static ValidationProfileImpl fromValues(final PDFAFlavour flavour,
            final String name, final String description, final String creator,
            final Date created, final String hash, final Set<Rule> rules,
            final Set<Variable> variables) {
        return new ValidationProfileImpl(flavour, name, description, creator,
                created, hash, rules, variables);
    }

    static ValidationProfileImpl fromValidationProfile(
            ValidationProfile toConvert) {
        return fromValues(toConvert.getPDFAFlavour(), toConvert.getName(),
                toConvert.getDescription(), toConvert.getCreator(),
                toConvert.getDateCreated(), toConvert.getHexSha1Digest(),
                toConvert.getRules(), toConvert.getVariables());
    }

    static String toXml(final ValidationProfile toConvert, Boolean prettyXml)
            throws JAXBException, IOException {
        String retVal = "";
        try (StringWriter writer = new StringWriter()) {
            toXml(toConvert, writer, prettyXml);
            retVal = writer.toString();
            return retVal;
        }
    }

    static ValidationProfileImpl fromXml(final String toConvert)
            throws JAXBException {
        try (StringReader reader = new StringReader(toConvert)) {
            return fromXml(reader);
        }
    }

    static void toXml(final ValidationProfile toConvert,
            final OutputStream stream, Boolean prettyXml) throws JAXBException {
        Marshaller varMarshaller = getMarshaller(prettyXml);
        varMarshaller.marshal(toConvert, stream);
    }

    static ValidationProfileImpl fromXml(final InputStream toConvert)
            throws JAXBException {
        Unmarshaller stringUnmarshaller = getUnmarshaller();
        return (ValidationProfileImpl) stringUnmarshaller.unmarshal(toConvert);
    }

    static void toXml(final ValidationProfile toConvert, final Writer writer, Boolean prettyXml)
            throws JAXBException {
        Marshaller varMarshaller = getMarshaller(prettyXml);
        varMarshaller.marshal(toConvert, writer);
    }

    static ValidationProfileImpl fromXml(final Reader toConvert)
            throws JAXBException {
        Unmarshaller stringUnmarshaller = getUnmarshaller();
        return (ValidationProfileImpl) stringUnmarshaller.unmarshal(toConvert);
    }

    private static Unmarshaller getUnmarshaller() throws JAXBException {
        JAXBContext context = JAXBContext
                .newInstance(ValidationProfileImpl.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        return unmarshaller;
    }

    private static Marshaller getMarshaller(Boolean setPretty)
            throws JAXBException {
        JAXBContext context = JAXBContext
                .newInstance(ValidationProfileImpl.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, setPretty);
        return marshaller;
    }
}
