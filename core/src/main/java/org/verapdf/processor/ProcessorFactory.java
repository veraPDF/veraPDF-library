/**
 * 
 */
package org.verapdf.processor;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.EnumSet;

import javax.xml.bind.JAXBException;

import org.verapdf.core.XmlSerialiser;
import org.verapdf.features.FeatureExtractorConfig;
import org.verapdf.metadata.fixer.MetadataFixerConfig;
import org.verapdf.pdfa.validation.validators.ValidatorConfig;

/**
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *         <a href="https://github.com/carlwilson">carlwilson AT github</a>
 * @version 0.1 Created 30 Oct 2016:22:18:23
 */

public final class ProcessorFactory {
	private ProcessorFactory() {

	}

	public static ProcessorConfig defaultConfig() {
		return ProcessorConfigImpl.defaultInstance();
	}

	public static ProcessorConfig fromValues(final ValidatorConfig config, final FeatureExtractorConfig featureConfig,
			final MetadataFixerConfig fixerConfig, final EnumSet<TaskType> tasks) {
		return ProcessorConfigImpl.fromValues(config, featureConfig, fixerConfig, tasks);
	}

	public static void configToXml(final ProcessorConfig toConvert, final OutputStream stream, boolean format)
			throws JAXBException {
		XmlSerialiser.toXml(toConvert, stream, format, false);
	}

	public static ProcessorConfig configFromXml(final InputStream source) throws JAXBException {
		return XmlSerialiser.typeFromXml(ProcessorConfigImpl.class, source);
	}

	public static final ItemProcessor createProcessor(final ProcessorConfig config) {
		return ProcessorImpl.newProcessor(config);
	}

	public static final StreamingProcessor createStreamingProcessor(final ProcessorConfig config) {
		return StreamingProcessorImpl.newInstance(createProcessor(config));
	}

	public static void resultToXml(final ProcessorResult toConvert, final OutputStream stream, boolean prettyXml)
			throws JAXBException {
		XmlSerialiser.toXml(toConvert, stream, prettyXml, false);
	}

	public static ProcessorResult resultFromXml(final InputStream source) throws JAXBException {
		return XmlSerialiser.typeFromXml(ProcessorResultImpl.class, source);
	}

	public static void taskResultToXml(final TaskResult toConvert, final OutputStream dest, boolean prettyXml)
			throws JAXBException {
		XmlSerialiser.toXml(toConvert, dest, prettyXml, true);
	}

	public static TaskResult taskResultfromXml(final InputStream source) throws JAXBException {
		return XmlSerialiser.typeFromXml(TaskResultImpl.class, source);
	}

}
