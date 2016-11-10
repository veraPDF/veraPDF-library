/**
 * 
 */
package org.verapdf.processor;

import java.io.Writer;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.bind.JAXBException;

import org.verapdf.core.VeraPDFException;
import org.verapdf.core.XmlSerialiser;
import org.verapdf.pdfa.results.MetadataFixerResult;
import org.verapdf.pdfa.results.ValidationResult;
import org.verapdf.processor.reports.BatchSummary;
import org.verapdf.report.FeaturesReport;

/**
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *         <a href="https://github.com/carlwilson">carlwilson AT github</a>
 * @version 0.1 Created 9 Nov 2016:00:44:27
 */

final class RawResultHandler extends AbstractXmlHandler {
	private static final Logger logger = Logger.getLogger(RawResultHandler.class.getCanonicalName());
	private final boolean format;
	private final boolean fragment;
	/**
	 * @param indentSize
	 * @param dest
	 * @throws VeraPDFException
	 */
	private RawResultHandler(final Writer dest) throws VeraPDFException {
		super(dest);
		this.format = true;
		this.fragment = false;
	}

	/**
	 * @param indentSize
	 * @param dest
	 * @throws VeraPDFException
	 */
	private RawResultHandler(final Writer dest, final int indentSize, final boolean format, final boolean fragment) throws VeraPDFException {
		super(dest, indentSize);
		this.format = format;
		this.fragment = fragment;
	}

	/**
	 * @see org.verapdf.processor.BatchProcessingHandler#handleBatchStart()
	 */
	@Override
	public void handleBatchStart(ProcessorConfig config) throws VeraPDFException {
		try {
			XmlSerialiser.toXml(config, this.writer, this.format, this.fragment);
		} catch (JAXBException excep) {
			logger.log(Level.WARNING, String.format(unmarshalErrMessage, "config"), excep);
			throw wrapMarshallException(excep, "config");
		}
	}

	@Override
	void resultStart(ProcessorResult result) throws VeraPDFException {
		try {
			XmlSerialiser.toXml(result.getProcessedItem(), this.writer, this.format, this.fragment);
		} catch (JAXBException excep) {
			logger.log(Level.WARNING, String.format(unmarshalErrMessage, "item"), excep);
			throw wrapMarshallException(excep, "item");
		}
	}

	@Override
	void parsingSuccess(TaskResult taskResult) {
		// No action for successful parsing
	}

	@Override
	void parsingFailure(TaskResult taskResult) throws VeraPDFException {
		outputTask(taskResult);
	}

	@Override
	void pdfEncrypted(TaskResult taskResult) throws VeraPDFException {
		outputTask(taskResult);
	}

	@Override
	void validationSuccess(TaskResult taskResult, ValidationResult validationResult)
			throws VeraPDFException {
		try {
			XmlSerialiser.toXml(validationResult, this.writer, this.format, this.fragment);
		} catch (JAXBException excep) {
			logger.log(Level.WARNING, String.format(unmarshalErrMessage, "validationResult"), excep);
			throw wrapMarshallException(excep, "validationResult");
		}
	}

	@Override
	void validationFailure(TaskResult taskResult) throws VeraPDFException {
		outputTask(taskResult);
	}

	@Override
	void featureSuccess(TaskResult taskResult, FeaturesReport featuresReport)
			throws VeraPDFException {
		try {
			XmlSerialiser.toXml(featuresReport, this.writer, this.format, this.fragment);
		} catch (JAXBException excep) {
			logger.log(Level.WARNING, String.format(unmarshalErrMessage, "featuresReport"), excep);
			throw wrapMarshallException(excep, "featuresReport");
		}
	}

	@Override
	void featureFailure(TaskResult taskResult) throws VeraPDFException {
		outputTask(taskResult);
	}

	@Override
	void fixerSuccess(TaskResult taskResult, MetadataFixerResult fixerResult)
			throws VeraPDFException {
		try {
			XmlSerialiser.toXml(fixerResult, this.writer, this.format, this.fragment);
		} catch (JAXBException excep) {
			logger.log(Level.WARNING, String.format(unmarshalErrMessage, "fixerResult"), excep);
			throw wrapMarshallException(excep, "fixerResult");
		}
	}

	@Override
	void fixerFailure(TaskResult taskResult) throws VeraPDFException {
		outputTask(taskResult);
	}


	@Override
	void resultEnd(ProcessorResult result) {
		// Nothing to do here
	}
	/**
	 * @see org.verapdf.processor.BatchProcessingHandler#handleBatchEnd(org.verapdf.processor.BatchSummary)
	 */
	@Override
	public void handleBatchEnd(BatchSummary summary) throws VeraPDFException {
		try {
			XmlSerialiser.toXml(summary, this.writer, this.format, this.fragment);
		} catch (JAXBException excep) {
			logger.log(Level.WARNING, String.format(unmarshalErrMessage, "summary"), excep);
			throw wrapMarshallException(excep, "summary");
		}
	}

	private void outputTask(TaskResult taskResult) throws VeraPDFException {
		try {
			XmlSerialiser.toXml(taskResult, this.writer, this.format, this.fragment);
		} catch (JAXBException excep) {
			logger.log(Level.WARNING, String.format(unmarshalErrMessage, "taskResult"), excep);
			throw wrapMarshallException(excep, "taskResult");
		}
	}

	static final BatchProcessingHandler newInstance(final Writer dest) throws VeraPDFException {
		return new RawResultHandler(dest);
	}

	static final BatchProcessingHandler newInstance(final Writer dest, final int indent) throws VeraPDFException {
		return new RawResultHandler(dest, indent, true, false);
	}

	static final BatchProcessingHandler newInstance(final Writer dest, final int indent, final boolean format) throws VeraPDFException {
		return new RawResultHandler(dest, indent, format, false);
	}

	static final BatchProcessingHandler newInstance(final Writer dest, final int indent, final boolean format, final boolean fragment) throws VeraPDFException {
		return new RawResultHandler(dest, indent, format, fragment);
	}
}
