package org.verapdf.pdfa.results;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
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
 * @author Evgeniy Muravitskiy
 */
@XmlRootElement(name="fixerResult")
public final class MetadataFixerResultImpl implements MetadataFixerResult {
	@XmlAttribute
    private final RepairStatus status;
	@XmlElementWrapper
	@XmlElement(name="fix")
    private final List<String> appliedFixes;

    private MetadataFixerResultImpl() {
        this(RepairStatus.NO_ACTION, new ArrayList<String>());
    }

    private MetadataFixerResultImpl(final RepairStatus status,
            final List<String> fixes) {
        super();
        this.status = status;
        this.appliedFixes = new ArrayList<>(fixes);
    }

    @Override
    public RepairStatus getRepairStatus() {
        return this.status;
    }

    @Override
    public List<String> getAppliedFixes() {
        return Collections.unmodifiableList(this.appliedFixes);
    }

    @Override
    public Iterator<String> iterator() {
        return this.appliedFixes.iterator();
    }

    /**
     * @param status
     * @param fixes
     * @return
     */
    public static MetadataFixerResult fromValues(final RepairStatus status,
            final List<String> fixes) {
        return new MetadataFixerResultImpl(status, fixes);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MetadataFixerResultImpl)) return false;

        MetadataFixerResultImpl strings = (MetadataFixerResultImpl) o;

        if (status != strings.status) return false;
        return appliedFixes != null ? appliedFixes.equals(strings.appliedFixes) : strings.appliedFixes == null;

    }

    @Override
    public int hashCode() {
        int result = status != null ? status.hashCode() : 0;
        result = 31 * result + (appliedFixes != null ? appliedFixes.hashCode() : 0);
        return result;
    }

    /**
     * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
     *
     */
    @SuppressWarnings("hiding")
    public static class Builder {
        private RepairStatus status = RepairStatus.NO_ACTION;
        private final List<String> fixes = new ArrayList<>();

        /**
         * @param status the {@link org.verapdf.pdfa.results.MetadataFixerResult.RepairStatus} to set for the Builder.
         * @return the Builder instance.
         */
        public Builder status(final RepairStatus status) {
            this.status = status;
            return this;
        }

        /**
         * @return the current status
         */
        public RepairStatus getStatus() {
            return this.status;
        }

        /**
         * @param fix a fix to add for the builder
         * @return the Builder instance
         */
        public Builder addFix(final String fix) {
            this.fixes.add(fix);
            return this;
        }

        /**
         * @return a {@link MetadataFixerResult} instance built from the values
         */
        public MetadataFixerResult build() {
            return MetadataFixerResultImpl.fromValues(this.status, this.fixes);
        }
    }

    static String toXml(final MetadataFixerResult toConvert, Boolean prettyXml)
            throws JAXBException, IOException {
        String retVal = "";
        try (StringWriter writer = new StringWriter()) {
            toXml(toConvert, writer, prettyXml);
            retVal = writer.toString();
            return retVal;
        }
    }

    static void toXml(final MetadataFixerResult toConvert,
            final OutputStream stream, Boolean prettyXml) throws JAXBException {
        Marshaller varMarshaller = getMarshaller(prettyXml);
        varMarshaller.marshal(toConvert, stream);
    }

    static MetadataFixerResult fromXml(final InputStream toConvert)
            throws JAXBException {
        Unmarshaller stringUnmarshaller = getUnmarshaller();
        return (MetadataFixerResultImpl) stringUnmarshaller.unmarshal(toConvert);
    }

    static void toXml(final MetadataFixerResult toConvert, final Writer writer,
            Boolean prettyXml) throws JAXBException {
        Marshaller varMarshaller = getMarshaller(prettyXml);
        varMarshaller.marshal(toConvert, writer);
    }

    static MetadataFixerResult fromXml(final Reader toConvert)
            throws JAXBException {
        Unmarshaller stringUnmarshaller = getUnmarshaller();
        return (MetadataFixerResultImpl) stringUnmarshaller.unmarshal(toConvert);
    }

    static MetadataFixerResult fromXml(final String toConvert)
            throws JAXBException {
        try (StringReader reader = new StringReader(toConvert)) {
            return fromXml(reader);
        }
    }

    static class Adapter extends
            XmlAdapter<MetadataFixerResultImpl, MetadataFixerResult> {
        @Override
        public MetadataFixerResult unmarshal(
        		MetadataFixerResultImpl mdfResult) {
            return mdfResult;
        }

        @Override
        public MetadataFixerResultImpl marshal(MetadataFixerResult procResult) {
            return (MetadataFixerResultImpl) procResult;
        }
    }

    private static Unmarshaller getUnmarshaller() throws JAXBException {
        JAXBContext context = JAXBContext
                .newInstance(MetadataFixerResultImpl.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        return unmarshaller;
    }

    private static Marshaller getMarshaller(Boolean setPretty)
            throws JAXBException {
        JAXBContext context = JAXBContext
                .newInstance(MetadataFixerResultImpl.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, setPretty);
        return marshaller;
    }
}
