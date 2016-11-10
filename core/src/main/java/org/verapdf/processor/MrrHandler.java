/**
 * 
 */
package org.verapdf.processor;

import java.io.Writer;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;

import org.verapdf.core.VeraPDFException;
import org.verapdf.core.XmlSerialiser;
import org.verapdf.processor.reports.BatchSummary;

/**
 * @author  <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *          <a href="https://github.com/carlwilson">carlwilson AT github</a>
 *
 * @version 0.1
 * 
 * Created 9 Nov 2016:06:43:57
 */

final class MrrHandler extends AbstractXmlHandler {
	private static final Logger logger = Logger.getLogger(MrrHandler.class.getCanonicalName());
	private final static String unmarshalErrMessage = "Unmarshalling exception when streaming %s.";
	private final static String streamingErrMessage = "Exception when streaming results";
	private final static String report = "report";
	private final static String job = "job";
	private final static String jobs = job + "s";

	/**
	 * @param indentSize
	 * @param dest
	 * @throws VeraPDFException
	 */
	public MrrHandler() throws VeraPDFException {
		super();
	}

	/**
	 * @param indentSize
	 * @param dest
	 * @throws VeraPDFException
	 */
	public MrrHandler(Writer dest) throws VeraPDFException {
		super(dest);
	}

	/**
	 * @param indentSize
	 * @param dest
	 * @throws VeraPDFException
	 */
	public MrrHandler(Writer dest, int indentSize) throws VeraPDFException {
		super(dest, indentSize);
	}

	/**
	 * @see org.verapdf.processor.BatchProcessingHandler#handleBatchStart()
	 */
	@Override
	public void handleBatchStart() throws VeraPDFException {
		try {
			startDoc(this.writer);
			indentElement(report);
			indentElement(jobs);
		} catch (XMLStreamException excep) {
			throw wrapStreamException(excep);
		}
	}

	/**
	 * @see org.verapdf.processor.BatchProcessingHandler#handleResult(org.verapdf.processor.ProcessorResult)
	 */
	@Override
	public void handleResult(ProcessorResult result) throws VeraPDFException {
		try {
			indentElement(job);
			XmlSerialiser.toXml(result.getProcessedItem(), this.writer, true, true);
			XmlSerialiser.toXml(result.getValidationResult(), this.writer, true, true);
			outdentElement();
		} catch (JAXBException | XMLStreamException excep) {
			logger.log(Level.WARNING, streamingErrMessage, excep);
			throw new VeraPDFException(streamingErrMessage, excep);
		}
	}

	/**
	 * @see org.verapdf.processor.BatchProcessingHandler#handleBatchEnd(org.verapdf.processor.BatchSummary)
	 */
	@Override
	public void handleBatchEnd(BatchSummary summary) throws VeraPDFException {
		try {
			outdentElement();
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
	
}
