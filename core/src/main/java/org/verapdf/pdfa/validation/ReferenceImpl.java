/**
 * 
 */
package org.verapdf.pdfa.validation;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAttribute;
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
@XmlRootElement(name = "reference")
final class ReferenceImpl implements Reference {
    private final static ReferenceImpl DEFAULT = new ReferenceImpl();
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
        if (this.clause == null) {
            if (other.getClause() != null)
                return false;
        } else if (!this.clause.equals(other.getClause()))
            return false;
        if (this.specification == null) {
            if (other.getSpecification() != null)
                return false;
        } else if (!this.specification.equals(other.getSpecification()))
            return false;
        return true;
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

    static ReferenceImpl defaultInstance() {
        return ReferenceImpl.DEFAULT;
    }

    static ReferenceImpl fromValues(final String specification,
            final String clause) {
        return new ReferenceImpl(specification, clause);
    }

    static ReferenceImpl fromReference(final Reference toConvert) {
        return new ReferenceImpl(toConvert.getSpecification(), toConvert.getClause());
    }

    static String toXml(final Reference toConvert) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(ReferenceImpl.class);
        Marshaller varMarshaller = context.createMarshaller();
        varMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        StringWriter writer = new StringWriter();
        varMarshaller.marshal(toConvert, writer);
        return writer.toString();
    }
    
    static ReferenceImpl fromXml(final String toConvert) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(ReferenceImpl.class);
        Unmarshaller stringUnmarshaller = context.createUnmarshaller();
        StringReader reader = new StringReader(toConvert);
        return (ReferenceImpl) stringUnmarshaller.unmarshal(reader);
    }

    static class Adapter extends XmlAdapter<ReferenceImpl, Reference> {
        @Override
        public Reference unmarshal(ReferenceImpl refImpl) { return refImpl; }
        @Override
        public ReferenceImpl marshal(Reference ref) { return (ReferenceImpl)ref; }
      }
    }
