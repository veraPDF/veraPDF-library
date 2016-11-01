package org.verapdf.processor;

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
import javax.xml.bind.annotation.XmlRootElement;

import org.verapdf.features.FeatureExtractorConfig;
import org.verapdf.features.FeatureFactory;
import org.verapdf.metadata.fixer.FixerFactory;
import org.verapdf.metadata.fixer.MetadataFixerConfig;
import org.verapdf.pdfa.validation.profiles.Profiles;
import org.verapdf.pdfa.validation.profiles.ValidationProfile;
import org.verapdf.pdfa.validation.validators.ValidatorConfig;
import org.verapdf.pdfa.validation.validators.ValidatorFactory;

/**
 * @author Maksim Bezrukov
 */

@XmlRootElement(name = "processorConfig")
final class ProcessorConfigImpl implements ProcessorConfig {
	private static final ProcessorConfig defaultInstance = new ProcessorConfigImpl();
	@XmlElement
	private final EnumSet<TaskType> tasks;
	@XmlElement
	private final ValidatorConfig validatorConfig;
	@XmlElement
	private final FeatureExtractorConfig featureConfig;
	@XmlElement
	private final MetadataFixerConfig fixerConfig;
	@XmlElement
	private final ValidationProfile customProfile;

	private ProcessorConfigImpl() {
		this(ValidatorFactory.defaultConfig(), FeatureFactory.defaultConfig(), FixerFactory.defaultConfig(),
				EnumSet.noneOf(TaskType.class));
	}

	private ProcessorConfigImpl(final ValidatorConfig config, final FeatureExtractorConfig featureConfig,
			final MetadataFixerConfig fixerConfig, final EnumSet<TaskType> tasks) {
		this(config, featureConfig, fixerConfig, tasks, Profiles.defaultProfile());
	}

	private ProcessorConfigImpl(final ValidatorConfig config, final FeatureExtractorConfig featureConfig,
			final MetadataFixerConfig fixerConfig, final EnumSet<TaskType> tasks, ValidationProfile customProfile) {
		super();
		this.tasks = EnumSet.copyOf(tasks);
		this.validatorConfig = config;
		this.featureConfig = featureConfig;
		this.fixerConfig = fixerConfig;
		this.customProfile = customProfile;
	}

	public boolean isFixMetadata() {
		return (this.tasks.contains(TaskType.FIX_METADATA));
	}

	@Override
	public ValidatorConfig getValidatorConfig() {
		return this.validatorConfig;
	}

	@Override
	public FeatureExtractorConfig getFeatureConfig() {
		return this.featureConfig;
	}

	@Override
	public MetadataFixerConfig getFixerConfig() {
		return this.fixerConfig;
	}

	@Override
	public ValidationProfile getCustomProfile() {
		return this.customProfile;
	}

	@Override
	public EnumSet<TaskType> getTasks() {
		return EnumSet.copyOf(this.tasks);
	}

	@Override
	public boolean hasTask(TaskType toCheck) {
		return this.tasks.contains(toCheck);
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.featureConfig == null) ? 0 : this.featureConfig.hashCode());
		result = prime * result + ((this.fixerConfig == null) ? 0 : this.fixerConfig.hashCode());
		result = prime * result + ((this.tasks == null) ? 0 : this.tasks.hashCode());
		result = prime * result + ((this.validatorConfig == null) ? 0 : this.validatorConfig.hashCode());
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
		if (!(obj instanceof ProcessorConfigImpl)) {
			return false;
		}
		ProcessorConfigImpl other = (ProcessorConfigImpl) obj;
		if (this.featureConfig == null) {
			if (other.featureConfig != null) {
				return false;
			}
		} else if (!this.featureConfig.equals(other.featureConfig)) {
			return false;
		}
		if (this.fixerConfig == null) {
			if (other.fixerConfig != null) {
				return false;
			}
		} else if (!this.fixerConfig.equals(other.fixerConfig)) {
			return false;
		}
		if (this.tasks == null) {
			if (other.tasks != null) {
				return false;
			}
		} else if (!this.tasks.equals(other.tasks)) {
			return false;
		}
		if (this.validatorConfig == null) {
			if (other.validatorConfig != null) {
				return false;
			}
		} else if (!this.validatorConfig.equals(other.validatorConfig)) {
			return false;
		}
		return true;
	}

	static ProcessorConfig defaultInstance() {
		return defaultInstance;
	}

	static ProcessorConfig fromValues(final ValidatorConfig config, final FeatureExtractorConfig featureConfig,
			final MetadataFixerConfig fixerConfig, final EnumSet<TaskType> tasks) {
		return new ProcessorConfigImpl(config, featureConfig, fixerConfig, tasks);
	}

	static ProcessorConfig fromValues(final ValidatorConfig config, final FeatureExtractorConfig featureConfig,
			final MetadataFixerConfig fixerConfig, final EnumSet<TaskType> tasks, final ValidationProfile profile) {
		return new ProcessorConfigImpl(config, featureConfig, fixerConfig, tasks, profile);
	}

	/**
	 * Converts Config to XML,
	 *
	 * @see javax.xml.bind.JAXB for more details
	 */
	static String toXml(final ProcessorConfig toConvert, Boolean prettyXml) throws JAXBException, IOException {
		String retVal = "";
		try (StringWriter writer = new StringWriter()) {
			toXml(toConvert, writer, prettyXml);
			retVal = writer.toString();
			return retVal;
		}
	}

	/**
	 * Converts XML file to Config,
	 *
	 * @see javax.xml.bind.JAXB for more details
	 */
	static ProcessorConfig fromXml(final String toConvert) throws JAXBException {
		try (StringReader reader = new StringReader(toConvert)) {
			return fromXml(reader);
		}
	}

	/**
	 * Converts Config to XML,
	 *
	 * @see javax.xml.bind.JAXB for more details
	 */
	static void toXml(final ProcessorConfig toConvert, final OutputStream stream, Boolean prettyXml)
			throws JAXBException {
		Marshaller varMarshaller = getMarshaller(prettyXml);
		varMarshaller.marshal(toConvert, stream);
	}

	/**
	 * Converts XML file to Config,
	 *
	 * @see javax.xml.bind.JAXB for more details
	 */
	static ProcessorConfig fromXml(final InputStream toConvert) throws JAXBException {
		Unmarshaller stringUnmarshaller = getUnmarshaller();
		return (ProcessorConfig) stringUnmarshaller.unmarshal(toConvert);
	}

	static void toXml(final ProcessorConfig toConvert, final Writer writer, Boolean prettyXml) throws JAXBException {
		Marshaller varMarshaller = getMarshaller(prettyXml);
		varMarshaller.marshal(toConvert, writer);
	}

	static ProcessorConfig fromXml(final Reader toConvert) throws JAXBException {
		Unmarshaller stringUnmarshaller = getUnmarshaller();
		return (ProcessorConfig) stringUnmarshaller.unmarshal(toConvert);
	}

	private static Unmarshaller getUnmarshaller() throws JAXBException {
		JAXBContext context = JAXBContext.newInstance(ProcessorConfigImpl.class);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		return unmarshaller;
	}

	private static Marshaller getMarshaller(Boolean setPretty) throws JAXBException {
		JAXBContext context = JAXBContext.newInstance(ProcessorConfigImpl.class);
		Marshaller marshaller = context.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, setPretty);
		return marshaller;
	}
}
