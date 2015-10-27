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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlAdapter;

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
    private final RuleIdImpl id;
    @XmlAttribute
    private final String object;
    @XmlElement
    private final String description;
    @XmlElement
    private final String test;
    @XmlElementWrapper
    @XmlElement(name = "reference")
    private final List<Reference> references = new ArrayList<>();

    private RuleImpl() {
        this(RuleIdImpl.defaultInstance(), "object", "description", "test",
                Collections.EMPTY_LIST);
    }

    private RuleImpl(final RuleIdImpl id, final String object,
            final String description, final String test,
            final List<Reference> references) {
        super();
        this.id = id;
        this.object = object;
        this.description = description;
        this.test = test;
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
     * @return the object
     */
    @Override
    public String getObject() {
        return this.object;
    }

    /**
     * @return the description
     */
    @Override
    public String getDescription() {
        return this.description;
    }

    /**
     * @return the test
     */
    @Override
    public String getTest() {
        return this.test;
    }

    /**
     * @return the references
     */
    @Override
    public List<Reference> getReferences() {
        return Collections.unmodifiableList(this.references);
    }

    /**
     * { @inheritDoc }
     */
    @Override
    public final int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime
                * result
                + ((this.description == null) ? 0 : this.description.hashCode());
        result = prime * result + ((this.id == null) ? 0 : this.id.hashCode());
        result = prime * result
                + ((this.object == null) ? 0 : this.object.hashCode());
        result = prime * result
                + ((this.references == null) ? 0 : this.references.hashCode());
        result = prime * result
                + ((this.test == null) ? 0 : this.test.hashCode());
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
        if (!(obj instanceof Rule))
            return false;
        Rule other = (Rule) obj;
        if (this.description == null) {
            if (other.getDescription() != null)
                return false;
        } else if (!this.description.equals(other.getDescription()))
            return false;
        if (this.id == null) {
            if (other.getRuleId() != null)
                return false;
        } else if (!this.id.equals(other.getRuleId()))
            return false;
        if (this.object == null) {
            if (other.getObject() != null)
                return false;
        } else if (!this.object.equals(other.getObject()))
            return false;
        if (this.references == null) {
            if (other.getReferences() != null)
                return false;
        } else if (!this.references.equals(other.getReferences()))
            return false;
        if (this.test == null) {
            if (other.getTest() != null)
                return false;
        } else if (!this.test.equals(other.getTest()))
            return false;
        return true;
    }

    /**
     * { @inheritDoc }
     */
    @Override
    public String toString() {
        return "Rule [id=" + this.id + ", object=" + this.object
                + ", description=" + this.description + ", test=" + this.test
                + ", references=" + this.references + "]";
    }

    static RuleImpl defaultInstance() {
        return RuleImpl.DEFAULT;
    }

    static RuleImpl fromValues(final RuleIdImpl id, final String object,
            final String description, final String test,
            final List<Reference> references) {
        return new RuleImpl(id, object, description, test, references);
    }

    static RuleImpl fromRule(final Rule toConvert) {
        return RuleImpl.fromValues(
                RuleIdImpl.fromRuleId(toConvert.getRuleId()),
                toConvert.getObject(), toConvert.getDescription(),
                toConvert.getTest(), toConvert.getReferences());
    }

    static String toXml(final Rule toConvert, Boolean prettyXml)
            throws JAXBException, IOException {
        String retVal = "";
        try (StringWriter writer = new StringWriter()) {
            toXml(toConvert, writer, prettyXml);
            retVal = writer.toString();
            return retVal;
        }
    }

    static RuleImpl fromXml(final String toConvert) throws JAXBException {
        try (StringReader reader = new StringReader(toConvert)) {
            return fromXml(reader);
        }
    }

    static void toXml(final Rule toConvert, OutputStream stream,
            Boolean prettyXml) throws JAXBException {
        Marshaller varMarshaller = getMarshaller(prettyXml);
        varMarshaller.marshal(toConvert, stream);
    }

    static RuleImpl fromXml(final InputStream toConvert) throws JAXBException {
        Unmarshaller stringUnmarshaller = getUnmarshaller();
        return (RuleImpl) stringUnmarshaller.unmarshal(toConvert);
    }

    static void toXml(final Rule toConvert, Writer writer, Boolean prettyXml)
            throws JAXBException {
        Marshaller varMarshaller = getMarshaller(prettyXml);
        varMarshaller.marshal(toConvert, writer);
    }

    static RuleImpl fromXml(final Reader toConvert) throws JAXBException {
        Unmarshaller stringUnmarshaller = getUnmarshaller();
        return (RuleImpl) stringUnmarshaller.unmarshal(toConvert);
    }

    static class Adapter extends XmlAdapter<RuleImpl, Rule> {
        @Override
        public Rule unmarshal(RuleImpl ruleImpl) {
            return ruleImpl;
        }

        @Override
        public RuleImpl marshal(Rule rule) {
            return (RuleImpl) rule;
        }
    }

    private static Unmarshaller getUnmarshaller() throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(RuleImpl.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        return unmarshaller;
    }

    private static Marshaller getMarshaller(Boolean setPretty)
            throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(RuleImpl.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, setPretty);
        return marshaller;
    }

}
