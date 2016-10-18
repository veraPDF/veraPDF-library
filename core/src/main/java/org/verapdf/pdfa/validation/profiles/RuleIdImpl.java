/**
 * 
 */
package org.verapdf.pdfa.validation.profiles;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlAdapter;

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

    static RuleIdImpl defaultInstance() {
        return RuleIdImpl.DEFAULT;
    }

    static RuleIdImpl fromValues(final Specification specifcation,
            final String clause, final int testNumber) {
        return new RuleIdImpl(specifcation, clause, testNumber);
    }

    static RuleIdImpl fromRuleId(final RuleId toConvert) {
        return fromValues(toConvert.getSpecification(), toConvert.getClause(),
                toConvert.getTestNumber());
    }

    static String toXml(final RuleId toConvert) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(RuleIdImpl.class);
        Marshaller varMarshaller = context.createMarshaller();
        varMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,
                Boolean.TRUE);
        StringWriter writer = new StringWriter();
        varMarshaller.marshal(toConvert, writer);
        return writer.toString();
    }

    static RuleIdImpl fromXml(final String toConvert) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(RuleIdImpl.class);
        Unmarshaller stringUnmarshaller = context.createUnmarshaller();
        StringReader reader = new StringReader(toConvert);
        return (RuleIdImpl) stringUnmarshaller.unmarshal(reader);
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
