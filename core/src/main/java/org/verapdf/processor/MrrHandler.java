/**
 * 
 */
package org.verapdf.processor;

import org.verapdf.ReleaseDetails;
import org.verapdf.core.VeraPDFException;
import org.verapdf.core.XmlSerialiser;
import org.verapdf.pdfa.results.MetadataFixerResult;
import org.verapdf.pdfa.results.ValidationResult;
import org.verapdf.processor.reports.*;
import org.verapdf.report.FeaturesReport;

import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;
import java.io.Writer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *         <a href="https://github.com/carlwilson">carlwilson AT github</a>
 * @version 0.1 Created 9 Nov 2016:06:43:57
 */

final class MrrHandler extends AbstractXmlHandler {
	private final static Logger logger = Logger.getLogger(MrrHandler.class.getCanonicalName());
    private final static String STATEMENT_PREFIX = "PDF file is ";
    private final static String NOT_INSERT = "not ";
    private final static String STATEMENT_SUFFIX = "compliant with Validation Profile requirements.";
    private final static String COMPLIANT_STATEMENT = STATEMENT_PREFIX
            + STATEMENT_SUFFIX;
    private final static String NONCOMPLIANT_STATEMENT = STATEMENT_PREFIX
            + NOT_INSERT + STATEMENT_SUFFIX;
	private final static String report = "report";
	private final static String job = "job";
	private final static String jobs = job + "s";

	private final static String buildInformation = "buildInformation";

	private final boolean logSuccess;
	private final int maxFailedChecks;
	/**
	 * @param indentSize
	 * @param dest
	 * @throws VeraPDFException
	 */
	private MrrHandler(Writer dest) throws VeraPDFException {
		this(dest, 2);
	}

	/**
	 * @param indentSize
	 * @param dest
	 * @throws VeraPDFException
	 */
	private MrrHandler(Writer dest, int indentSize) throws VeraPDFException {
		this(dest, indentSize, 100, false);
	}
	
	private MrrHandler(Writer dest, int indentSize, int maxFailedChecks, boolean logSuccess) throws VeraPDFException {
		super(dest, indentSize);
		this.maxFailedChecks = maxFailedChecks;
		this.logSuccess = logSuccess;
	}

	/**
	 * @see org.verapdf.processor.BatchProcessingHandler#handleBatchStart()
	 */
	@Override
	public void handleBatchStart(ProcessorConfig config) throws VeraPDFException {
		try {
			startDoc(this.writer);
			indentElement(report);
			addReleaseDetails();
			indentElement(jobs);
		} catch (XMLStreamException excep) {
			throw wrapStreamException(excep);
		}
	}

	private void addReleaseDetails() throws XMLStreamException, VeraPDFException {
		indentElement(buildInformation);
		for (ReleaseDetails details : ReleaseDetails.getDetails()) {
			try {
				XmlSerialiser.toXml(details, this.writer, true, true);
			} catch (JAXBException excep) {
				logger.log(Level.WARNING, String.format(unmarshalErrMessage, "releaseDetails"), excep);
				throw wrapMarshallException(excep, "releaseDetails");
			}
		}
		outdentElement();
	}

	@Override
	void resultStart(ProcessorResult result) throws VeraPDFException {
		try {
			indentElement(job);
			XmlSerialiser.toXml(result.getProcessedItem(), this.writer, true, true);
		} catch (XMLStreamException excep) {
			throw wrapStreamException(excep);
		} catch (JAXBException excep) {
			logger.log(Level.WARNING, String.format(unmarshalErrMessage, "itemDetails"), excep);
			throw wrapMarshallException(excep, "itemDetails");
		}
	}

	@Override
	void parsingSuccess(TaskResult taskResult) {
		// Even here we're not handling parsing success event
	}

	@Override
	void parsingFailure(TaskResult taskResult) throws VeraPDFException {
		try {
			XmlSerialiser.toXml(taskResult, this.writer, true, true);
		} catch (JAXBException excep) {
			logger.log(Level.WARNING, String.format(unmarshalErrMessage, "taskResult"), excep);
			throw wrapMarshallException(excep, "taskResult");
		}
	}

	@Override
	void pdfEncrypted(TaskResult taskResult) throws VeraPDFException {
		try {
			XmlSerialiser.toXml(taskResult, this.writer, true, true);
		} catch (JAXBException excep) {
			logger.log(Level.WARNING, String.format(unmarshalErrMessage, "taskResult"), excep);
			throw wrapMarshallException(excep, "taskResult");
		}
	}

	@Override
	void validationSuccess(TaskResult taskResult, ValidationResult result) throws VeraPDFException {
		ValidationDetails details = Reports.fromValues(result, this.logSuccess, this.maxFailedChecks);
		ValidationReport valRep = Reports.createValidationReport(details, "LOGIC TO DO", getStatement(result.isCompliant()), result.isCompliant());
		try {
			XmlSerialiser.toXml(valRep, this.writer, true, true);
		} catch (JAXBException excep) {
			logger.log(Level.WARNING, String.format(unmarshalErrMessage, "validationReport"), excep);
			throw wrapMarshallException(excep, "validationReport");
		}
	}

	@Override
	void validationFailure(TaskResult taskResult) throws VeraPDFException {
		try {
			XmlSerialiser.toXml(taskResult, this.writer, true, true);
		} catch (JAXBException excep) {
			logger.log(Level.WARNING, String.format(unmarshalErrMessage, "taskResult"), excep);
			throw wrapMarshallException(excep, "taskResult");
		}
	}

	@Override
	void featureSuccess(TaskResult taskResult, FeaturesReport featuresReport) throws VeraPDFException {
		try {
			XmlSerialiser.toXml(featuresReport, this.writer, true, true);
		} catch (JAXBException excep) {
			logger.log(Level.WARNING, String.format(unmarshalErrMessage, "featuresReport"), excep);
			throw wrapMarshallException(excep, "featuresReport");
		}
	}

	@Override
	void featureFailure(TaskResult taskResult) throws VeraPDFException {
		try {
			XmlSerialiser.toXml(taskResult, this.writer, true, true);
		} catch (JAXBException excep) {
			logger.log(Level.WARNING, String.format(unmarshalErrMessage, "taskResult"), excep);
			throw wrapMarshallException(excep, "taskResult");
		}
	}

	@Override
	void fixerSuccess(TaskResult taskResult, MetadataFixerResult fixerResult) throws VeraPDFException {
		MetadataFixerReport mfRep = Reports.fromValues(fixerResult);
		try {
			XmlSerialiser.toXml(mfRep, this.writer, true, true);
		} catch (JAXBException excep) {
			logger.log(Level.WARNING, String.format(unmarshalErrMessage, "fixerReport"), excep);
			throw wrapMarshallException(excep, "fixerReport");
		}
	}

	@Override
	void fixerFailure(TaskResult taskResult) throws VeraPDFException {
		try {
			XmlSerialiser.toXml(taskResult, this.writer, true, true);
		} catch (JAXBException excep) {
			logger.log(Level.WARNING, String.format(unmarshalErrMessage, "taskResult"), excep);
			throw wrapMarshallException(excep, "taskResult");
		}
	}

	@Override
	void resultEnd(ProcessorResult result) throws VeraPDFException {
		try {
			// End job element
			outdentElement();
		} catch (XMLStreamException excep) {
			throw wrapStreamException(excep);
		}
	}

	/**
	 * @see org.verapdf.processor.BatchProcessingHandler#handleBatchEnd(org.verapdf.processor.BatchSummary)
	 */
	@Override
	public void handleBatchEnd(BatchSummary summary) throws VeraPDFException {
		try {
			// closes jobs element
			outdentElement();
			// closes report element
			outdentElement();
			endDoc(this.writer);
		} catch (XMLStreamException excep) {
			throw wrapStreamException(excep);
		}
		try {
			this.writer.close();
		} catch (XMLStreamException excep) {
			logger.log(Level.INFO, String.format(strmExcpMessTmpl, "closing"), excep);
		}
	}

	protected static VeraPDFException wrapStreamException(final JAXBException excep, final String typePart) {
		return new VeraPDFException(String.format(unmarshalErrMessage, typePart), excep);
	}

	static BatchProcessingHandler newInstance(final Writer dest) throws VeraPDFException {
		return new MrrHandler(dest);
	}

	static BatchProcessingHandler newInstance(final Writer dest, final int indent) throws VeraPDFException {
		return new MrrHandler(dest, indent);
	}
	
    private static String getStatement(boolean status) {
        return status ? COMPLIANT_STATEMENT : NONCOMPLIANT_STATEMENT;
    }

}
