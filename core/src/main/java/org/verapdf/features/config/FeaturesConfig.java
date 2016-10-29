package org.verapdf.features.config;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.EnumSet;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import org.verapdf.features.FeatureObjectType;

/**
 * @author Maksim Bezrukov
 */
@XmlRootElement(name = "featuresConfig")
public final class FeaturesConfig {
	private static final FeaturesConfig DEFAULT = new FeaturesConfig(EnumSet.of(FeatureObjectType.INFORMATION_DICTIONARY));
	@XmlElementWrapper(name="enabledFeatures")
	@XmlElement(name="feature")
	private final EnumSet<FeatureObjectType> enabledFeatures;

	private FeaturesConfig() {
		this(EnumSet.noneOf(FeatureObjectType.class));
	}
	private FeaturesConfig(EnumSet<FeatureObjectType> enabledFeatures) {
		super();
		this.enabledFeatures = EnumSet.copyOf(enabledFeatures);
	}

	public boolean isFeatureEnabled(FeatureObjectType type) {
		return this.enabledFeatures.contains(type);
	}

	public boolean isAnyFeatureEnabled(EnumSet<FeatureObjectType> types) {
		for (FeatureObjectType type : types) {
			if (this.enabledFeatures.contains(type)) {
				return true;
			}
		}
		return false;
	}

	public EnumSet<FeatureObjectType> getEnabledFeatures() {
		return this.enabledFeatures;
	}

	public static FeaturesConfig defaultInstance() {
		return DEFAULT;
	}
	
	public static FeaturesConfig fromFeatureSet(EnumSet<FeatureObjectType> enabledFeatures) {
		return new FeaturesConfig(enabledFeatures);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.enabledFeatures == null) ? 0 : this.enabledFeatures.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FeaturesConfig other = (FeaturesConfig) obj;
		if (this.enabledFeatures == null) {
			if (other.enabledFeatures != null)
				return false;
		} else if (!this.enabledFeatures.equals(other.enabledFeatures))
			return false;
		return true;
	}

	/**
	 *	Converts PluginsCollectionConfig to XML,
	 *	@see javax.xml.bind.JAXB for more details
	 */
	public static String toXml(final FeaturesConfig toConvert, Boolean prettyXml)
			throws JAXBException, IOException {
		String retVal = "";
		try (StringWriter writer = new StringWriter()) {
			toXml(toConvert, writer, prettyXml);
			retVal = writer.toString();
			return retVal;
		}
	}

	/**
	 *	Converts XML file to PluginsCollectionConfig,
	 *	@see javax.xml.bind.JAXB for more details
	 */
	public static FeaturesConfig fromXml(final String toConvert)
			throws JAXBException {
		try (StringReader reader = new StringReader(toConvert)) {
			return fromXml(reader);
		}
	}

	/**
	 *	Converts PluginsCollectionConfig to XML,
	 *	@see javax.xml.bind.JAXB for more details
	 */
	public static void toXml(final FeaturesConfig toConvert,
							 final OutputStream stream, Boolean prettyXml) throws JAXBException {
		Marshaller varMarshaller = getMarshaller(prettyXml);
		varMarshaller.marshal(toConvert, stream);
	}

	/**
	 *	Converts XML file to PluginsCollectionConfig,
	 *	@see javax.xml.bind.JAXB for more details
	 */
	public static FeaturesConfig fromXml(final InputStream toConvert)
			throws JAXBException {
		Unmarshaller stringUnmarshaller = getUnmarshaller();
		return (FeaturesConfig) stringUnmarshaller.unmarshal(toConvert);
	}

	static void toXml(final FeaturesConfig toConvert, final Writer writer,
					  Boolean prettyXml) throws JAXBException {
		Marshaller varMarshaller = getMarshaller(prettyXml);
		varMarshaller.marshal(toConvert, writer);
	}

	static FeaturesConfig fromXml(final Reader toConvert)
			throws JAXBException {
		Unmarshaller stringUnmarshaller = getUnmarshaller();
		return (FeaturesConfig) stringUnmarshaller.unmarshal(toConvert);
	}

	private static Unmarshaller getUnmarshaller() throws JAXBException {
		JAXBContext context = JAXBContext
				.newInstance(FeaturesConfig.class);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		return unmarshaller;
	}

	private static Marshaller getMarshaller(Boolean setPretty)
			throws JAXBException {
		JAXBContext context = JAXBContext
				.newInstance(FeaturesConfig.class);
		Marshaller marshaller = context.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, setPretty);
		return marshaller;
	}
}
