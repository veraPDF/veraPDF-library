package org.verapdf.metadata.fixer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * @author Evgeniy Muravitskiy
 */
@XmlRootElement(name="fixerConfig")
final class FixerConfigImpl implements MetadataFixerConfig {
	private final static MetadataFixerConfig defaultInstance = new FixerConfigImpl();
	@XmlAttribute
	private final boolean fixId;
	@XmlAttribute
	private final String fixesPrefix;
	
	private FixerConfigImpl() {
		this("veraFixMd_", true);
	}

	private FixerConfigImpl(final String fixesPrefix, boolean fixId) {
		super();
		this.fixId = fixId;
		this.fixesPrefix = fixesPrefix;
	}

	/**
	 * @return the fixId
	 */
	@Override
	public boolean isFixId() {
		return this.fixId;
	}

	/**
	 * @return the fixesPrefix
	 */
	@Override
	public String getFixesPrefix() {
		return this.fixesPrefix;
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (this.fixId ? 1231 : 1237);
		result = prime * result + ((this.fixesPrefix == null) ? 0 : this.fixesPrefix.hashCode());
		return result;
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof FixerConfigImpl)) {
			return false;
		}
		FixerConfigImpl other = (FixerConfigImpl) obj;
		if (this.fixId != other.fixId) {
			return false;
		}
		if (this.fixesPrefix == null) {
			if (other.fixesPrefix != null) {
				return false;
			}
		} else if (!this.fixesPrefix.equals(other.fixesPrefix)) {
			return false;
		}
		return true;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "FixerConfigImpl [fixId=" + this.fixId + ", fixesPrefix=" + this.fixesPrefix + "]";
	}

	static MetadataFixerConfig defaultInstance() {
		return defaultInstance;
	}
	
	static MetadataFixerConfig fromValues(final String fixesPrefix, boolean fixId) {
		return new FixerConfigImpl(fixesPrefix, fixId);
	}

    static String toXml(final MetadataFixerConfig toConvert, Boolean prettyXml)
            throws JAXBException, IOException {
        String retVal = "";
        try (StringWriter writer = new StringWriter()) {
            toXml(toConvert, writer, prettyXml);
            retVal = writer.toString();
            return retVal;
        }
    }

    static void toXml(final MetadataFixerConfig toConvert,
            final OutputStream stream, Boolean prettyXml) throws JAXBException {
        Marshaller varMarshaller = getMarshaller(prettyXml);
        varMarshaller.marshal(toConvert, stream);
    }

    static FixerConfigImpl fromXml(final InputStream toConvert)
            throws JAXBException {
        Unmarshaller stringUnmarshaller = getUnmarshaller();
        return (FixerConfigImpl) stringUnmarshaller.unmarshal(toConvert);
    }

    static void toXml(final MetadataFixerConfig toConvert, final Writer writer,
            Boolean prettyXml) throws JAXBException {
        Marshaller varMarshaller = getMarshaller(prettyXml);
        varMarshaller.marshal(toConvert, writer);
    }

    static FixerConfigImpl fromXml(final Reader toConvert)
            throws JAXBException {
        Unmarshaller stringUnmarshaller = getUnmarshaller();
        return (FixerConfigImpl) stringUnmarshaller.unmarshal(toConvert);
    }

    static FixerConfigImpl fromXml(final String toConvert)
            throws JAXBException {
        try (StringReader reader = new StringReader(toConvert)) {
            return fromXml(reader);
        }
    }

    static class Adapter extends
            XmlAdapter<FixerConfigImpl, MetadataFixerConfig> {
        @Override
        public MetadataFixerConfig unmarshal(
        		FixerConfigImpl fixerConfigImpl) {
            return fixerConfigImpl;
        }

        @Override
        public FixerConfigImpl marshal(MetadataFixerConfig fixerConfig) {
            return (FixerConfigImpl) fixerConfig;
        }
    }

    private static Unmarshaller getUnmarshaller() throws JAXBException {
        JAXBContext context = JAXBContext
                .newInstance(FixerConfigImpl.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        return unmarshaller;
    }

    private static Marshaller getMarshaller(Boolean setPretty)
            throws JAXBException {
        JAXBContext context = JAXBContext
                .newInstance(FixerConfigImpl.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, setPretty);
        return marshaller;
    }
}
