/**
 *
 */
package org.verapdf.pdfa.validation;

import org.verapdf.pdfa.flavours.PDFAFlavour;

import javax.xml.bind.*;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.transform.Result;
import javax.xml.transform.stream.StreamResult;

import java.io.*;
import java.util.*;

/**
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 */
@XmlRootElement(namespace = "http://www.verapdf.org/ValidationProfile", name = "profile")
final class ValidationProfileImpl implements ValidationProfile {
    private final static Map<PDFAFlavour, Map<String, Set<Rule>>> OBJECT_RULE_MAP = new HashMap<>();
    private final static Map<PDFAFlavour, Map<String, Set<Variable>>> OBJECT_VARIABLE_MAP = new HashMap<>();
    private final static Map<RuleId, Rule> RULE_LOOKUP = new HashMap<>();
    private final static ValidationProfileImpl DEFAULT = new ValidationProfileImpl();

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
                "hash", Collections.<Rule> emptySet(), Collections.<Variable> emptySet());
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
                                  final SortedSet<Rule> rules, final Set<Variable> variables) {
        super();
        this.flavour = flavour;
        this.details = details;
        this.hash = hash;
        this.rules = rules;
        this.variables = new HashSet<>(variables);
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
        if (OBJECT_RULE_MAP.get(this.flavour) == null) {
            OBJECT_RULE_MAP.put(this.flavour, createObjectRuleMap(this.rules));
        }
        Set<Rule> objRules = OBJECT_RULE_MAP.get(this.flavour).get(objectName);
        return (objRules == null) ? Collections.<Rule> emptySet() : Collections
                .unmodifiableSet(objRules);
    }

    /**
     * { @inheritDoc }
     */
    @Override
    public Set<Variable> getVariablesByObject(String objectName) {
        if (OBJECT_VARIABLE_MAP.get(this.flavour) == null) {
            OBJECT_VARIABLE_MAP.put(this.flavour,
                    createObjectVariableMap(this.variables));
        }
        Set<Variable> objRules = OBJECT_VARIABLE_MAP.get(this.flavour).get(
                objectName);
        return (objRules == null) ? Collections.<Variable> emptySet() : Collections
                .unmodifiableSet(objRules);
    }

    /**
     * { @inheritDoc }
     */
    @Override
    public Rule getRuleByRuleId(RuleId id) {
        return RULE_LOOKUP.get(id);
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
        if (this.hash == null) {
            if (other.getHexSha1Digest() != null)
                return false;
        } else if (!this.hash.equals(other.getHexSha1Digest()))
            return false;
        if (this.details == null) {
            if (other.getDetails() != null)
                return false;
        } else if (!this.details.equals(other.getDetails()))
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
        return "ValidationProfile [flavour=" + this.flavour + ", details="
                + this.details + ", hash=" + this.hash + ", rules="
                + this.rules + ", variables=" + this.variables + "]";
    }

    static ValidationProfileImpl defaultInstance() {
        return ValidationProfileImpl.DEFAULT;
    }

    static ValidationProfileImpl fromValues(final PDFAFlavour flavour,
            final ProfileDetails details, final String hash,
            final Set<Rule> rules, final Set<Variable> variables) {
        return new ValidationProfileImpl(flavour, details, hash, rules,
                variables);
    }

    static ValidationProfileImpl fromSortedValues(final PDFAFlavour flavour,
                                                  final ProfileDetails details, final String hash,
                                                  final SortedSet<Rule> rules, final Set<Variable> variables) {
        return new ValidationProfileImpl(flavour, details, hash, rules,
                variables);
    }

    static ValidationProfileImpl fromValidationProfile(
            ValidationProfile toConvert) {
        return fromValues(toConvert.getPDFAFlavour(), toConvert.getDetails(),
                toConvert.getHexSha1Digest(), toConvert.getRules(),
                toConvert.getVariables());
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

    static void toXml(final ValidationProfile toConvert, final Writer writer,
            Boolean prettyXml) throws JAXBException {
        Marshaller varMarshaller = getMarshaller(prettyXml);
        varMarshaller.marshal(toConvert, writer);
    }

    static ValidationProfileImpl fromXml(final Reader toConvert)
            throws JAXBException {
        Unmarshaller stringUnmarshaller = getUnmarshaller();
        return (ValidationProfileImpl) stringUnmarshaller.unmarshal(toConvert);
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

    private static Map<String, Set<Rule>> createObjectRuleMap(
            final Set<Rule> rules) {
        Map<String, Set<Rule>> rulesByObject = new HashMap<>();
        for (Rule rule : rules) {
            RULE_LOOKUP.put(rule.getRuleId(), rule);
            if (!rulesByObject.containsKey(rule.getObject())) {
                rulesByObject.put(rule.getObject(), new HashSet<Rule>());
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
                variablesByObject
                        .put(rule.getObject(), new HashSet<Variable>());
            }
            variablesByObject.get(rule.getObject()).add(rule);
        }
        return variablesByObject;
    }

    static String getSchema() throws JAXBException, IOException {
        JAXBContext context = JAXBContext
                .newInstance(ValidationProfileImpl.class);
        final StringWriter writer = new StringWriter();
        context.generateSchema(new WriterSchemaOutputResolver(writer));
        return writer.toString();
    }

    private static class WriterSchemaOutputResolver extends SchemaOutputResolver {
        private final Writer out;
        /**
         * @param out a Writer for the generated schema
         *
         */
        public WriterSchemaOutputResolver(final Writer out) {
            super();
            this.out = out;
        }

        /**
         * { @inheritDoc }
         */
        @Override
        public Result createOutput(String namespaceUri, String suggestedFileName)
                throws IOException {
            final StreamResult result = new StreamResult(this.out);
            result.setSystemId("no-id");
            return result;
        }

    }
}
