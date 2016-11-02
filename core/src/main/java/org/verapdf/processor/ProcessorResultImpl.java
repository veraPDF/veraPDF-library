package org.verapdf.processor;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Collection;
import java.util.EnumMap;
import java.util.EnumSet;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.verapdf.features.FeatureExtractionResult;
import org.verapdf.metadata.fixer.FixerFactory;
import org.verapdf.pdfa.results.MetadataFixerResult;
import org.verapdf.pdfa.results.ValidationResult;
import org.verapdf.pdfa.results.ValidationResults;
import org.verapdf.report.FeaturesReport;
import org.verapdf.report.ItemDetails;

/**
 * Instance of this class contains result of
 * {@link org.verapdf.processor.ProcessorImpl#validate(InputStream, ItemDetails, Config, OutputStream)}
 * work.
 *
 * @author Sergey Shemyakov
 */
@XmlRootElement(name="processorResult")
class ProcessorResultImpl implements ProcessorResult {
	private final static ProcessorResult defaultInstance = new ProcessorResultImpl();
	@XmlAttribute
	private final boolean isValidPdf;
	@XmlAttribute
	private final boolean isEncryptedPdf;
	private final EnumMap<TaskType, TaskResult> taskResults;
	@XmlElement
	private final ValidationResult validationResult;
	private final FeatureExtractionResult featuresResult;
	@XmlElement
	private final MetadataFixerResult fixerResult;

	private ProcessorResultImpl() {
		this(false, false);
	}

	private ProcessorResultImpl(boolean isValidPdf, boolean isEncrypted) {
		this(isValidPdf, isEncrypted, new EnumMap<TaskType, TaskResult>(TaskType.class),
				ValidationResults.defaultResult(), new FeatureExtractionResult(),
				FixerFactory.defaultResult());
	}

	private ProcessorResultImpl(final EnumMap<TaskType, TaskResult> results, final ValidationResult validationResult,
			final FeatureExtractionResult featuresResult, final MetadataFixerResult fixerResult) {
		this(true, true, results, validationResult, featuresResult, fixerResult);
	}

	private ProcessorResultImpl(boolean isValidPdf, boolean isEncrypted, final EnumMap<TaskType, TaskResult> results,
			final ValidationResult validationResult, final FeatureExtractionResult featuresResult,
			final MetadataFixerResult fixerResult) {
		super();
		this.isValidPdf = isValidPdf;
		this.isEncryptedPdf = isEncrypted;
		this.taskResults = results;
		this.validationResult = validationResult;
		this.featuresResult = featuresResult;
		this.fixerResult = fixerResult;
	}

	/**
	 * @return the results
	 */
	@Override
	public EnumMap<TaskType, TaskResult> getResults() {
		return this.taskResults;
	}

	@Override
	@XmlElementWrapper(name="taskResult")
	@XmlElement(name="taskResult")
	public Collection<TaskResult> getResultSet() {
		return this.taskResults.values();
	}

	@Override
	public ItemDetails getProcessedItem() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EnumSet<TaskType> getTaskTypes() {
		return EnumSet.copyOf(this.taskResults.keySet());
	}

	static ProcessorResult defaultInstance() {
		return defaultInstance;
	}

	static ProcessorResult fromValues(final EnumMap<TaskType, TaskResult> results,
			final ValidationResult validationResult, final FeatureExtractionResult featuresResult,
			final MetadataFixerResult fixerResult) {
		return new ProcessorResultImpl(true, true, results, validationResult, featuresResult, fixerResult);
	}

	static ProcessorResult invalidPdfResult() {
		return new ProcessorResultImpl();
	}

	static ProcessorResult encryptedResult() {
		return new ProcessorResultImpl(true, false);
	}

	@Override
	public ValidationResult getValidationResult() {
		return this.validationResult;
	}

	@Override
	@XmlElement
	public FeaturesReport getFeaturesReport() {
		return FeaturesReport.fromValues(this.featuresResult);
	}

	@Override
	public MetadataFixerResult getFixerResult() {
		return this.fixerResult;
	}

	@Override
	public TaskResult getResultForTask(TaskType taskType) {
		return this.taskResults.get(taskType);
	}

	@Override
	public boolean isValidPdf() {
		return this.isValidPdf;
	}

	@Override
	public boolean isEncryptedPdf() {
		return this.isEncryptedPdf;
	}

    static String toXml(final ProcessorResult toConvert, Boolean prettyXml)
            throws JAXBException, IOException {
        String retVal = "";
        try (StringWriter writer = new StringWriter()) {
            toXml(toConvert, writer, prettyXml);
            retVal = writer.toString();
            return retVal;
        }
    }

    static void toXml(final ProcessorResult toConvert,
            final OutputStream stream, Boolean prettyXml) throws JAXBException {
        Marshaller varMarshaller = getMarshaller(prettyXml);
        varMarshaller.marshal(toConvert, stream);
    }

    static ProcessorResult fromXml(final InputStream toConvert)
            throws JAXBException {
        Unmarshaller stringUnmarshaller = getUnmarshaller();
        return (ProcessorResultImpl) stringUnmarshaller.unmarshal(toConvert);
    }

    static void toXml(final ProcessorResult toConvert, final Writer writer,
            Boolean prettyXml) throws JAXBException {
        Marshaller varMarshaller = getMarshaller(prettyXml);
        varMarshaller.marshal(toConvert, writer);
    }

    static ProcessorResult fromXml(final Reader toConvert)
            throws JAXBException {
        Unmarshaller stringUnmarshaller = getUnmarshaller();
        return (ProcessorResultImpl) stringUnmarshaller.unmarshal(toConvert);
    }

    static ProcessorResult fromXml(final String toConvert)
            throws JAXBException {
        try (StringReader reader = new StringReader(toConvert)) {
            return fromXml(reader);
        }
    }

    static class Adapter extends
            XmlAdapter<ProcessorResultImpl, ProcessorResult> {
        @Override
        public ProcessorResult unmarshal(
        		ProcessorResultImpl procResultImpl) {
            return procResultImpl;
        }

        @Override
        public ProcessorResultImpl marshal(ProcessorResult procResult) {
            return (ProcessorResultImpl) procResult;
        }
    }

    private static Unmarshaller getUnmarshaller() throws JAXBException {
        JAXBContext context = JAXBContext
                .newInstance(ProcessorResultImpl.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        return unmarshaller;
    }

    private static Marshaller getMarshaller(Boolean setPretty)
            throws JAXBException {
        JAXBContext context = JAXBContext
                .newInstance(ProcessorResultImpl.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, setPretty);
        return marshaller;
    }
}
