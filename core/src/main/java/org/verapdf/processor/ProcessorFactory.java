/**
 * 
 */
package org.verapdf.processor;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.EnumSet;

import javax.xml.bind.JAXBException;

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
	
    public static void configToXml(final ProcessorConfig toConvert,
            final OutputStream stream, Boolean prettyXml) throws JAXBException {
    	ProcessorConfigImpl.toXml(toConvert, stream, prettyXml);
    }

    public static ProcessorConfig configFromXml(final InputStream toConvert)
            throws JAXBException {
    	return ProcessorConfigImpl.fromXml(toConvert);
    }

	public static final VeraProcessor createProcessor(final ProcessorConfig config) {
		return ProcessorImpl.newProcessor(config);
	}
	
    public static void resultToXml(final ProcessorResult toConvert,
            final OutputStream stream, Boolean prettyXml) throws JAXBException {
    	ProcessorResultImpl.toXml(toConvert, stream, prettyXml);
    }

    public static ProcessorResult resultFromXml(final InputStream toConvert)
            throws JAXBException {
    	return ProcessorResultImpl.fromXml(toConvert);
    }

    public static void taskResultToXml(final TaskResult toConvert,
            final OutputStream stream, Boolean prettyXml) throws JAXBException {
    	TaskResultImpl.toXml(toConvert, stream, prettyXml);
    }

    public static TaskResult taskResultfromXml(final InputStream toConvert)
            throws JAXBException {
    	return TaskResultImpl.fromXml(toConvert);
    }

}
