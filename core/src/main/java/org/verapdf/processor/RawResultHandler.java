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
import org.verapdf.processor.reports.BatchSummary;

/**
 * @author  <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *          <a href="https://github.com/carlwilson">carlwilson AT github</a>
 *
 * @version 0.1
 * 
 * Created 9 Nov 2016:00:44:27
 */

class RawResultHandler extends AbstractXmlHandler {
	private static final Logger logger = Logger.getLogger(RawResultHandler.class.getCanonicalName());
	private final static String unmarshalErrMessage = "Unmarshalling exception when streaming %s.";

	/**
	 * @param indentSize
	 * @param dest
	 * @throws VeraPDFException
	 */
	public RawResultHandler() throws VeraPDFException {
		super();
	}

	/**
	 * @param indentSize
	 * @param dest
	 * @throws VeraPDFException
	 */
	public RawResultHandler(Writer dest) throws VeraPDFException {
		super(dest);
	}

	/**
	 * @param indentSize
	 * @param dest
	 * @throws VeraPDFException
	 */
	public RawResultHandler(Writer dest, int indentSize) throws VeraPDFException {
		super(dest, indentSize);
	}

	/**
	 * @see org.verapdf.processor.BatchProcessingHandler#handleBatchStart()
	 */
	@Override
	public void handleBatchStart() {
		// Do nothing, we're just going to output the raw processor result each time
	}

	/**
	 * @see org.verapdf.processor.BatchProcessingHandler#handleResult(org.verapdf.processor.ProcessorResult)
	 */
	@Override
	public void handleResult(ProcessorResult result) throws VeraPDFException {
		try {
			XmlSerialiser.toXml(result, this.writer, true, false);
		} catch (JAXBException excep) {
			logger.log(Level.WARNING, String.format(unmarshalErrMessage, "result"), excep);
			throw wrapStreamException(excep, "result");
		}
	}

	/**
	 * @see org.verapdf.processor.BatchProcessingHandler#handleBatchEnd(org.verapdf.processor.BatchSummary)
	 */
	@Override
	public void handleBatchEnd(BatchSummary summary) throws VeraPDFException {
		try {
			XmlSerialiser.toXml(summary, this.writer, true, false);
		} catch (JAXBException excep) {
			logger.log(Level.WARNING, String.format(unmarshalErrMessage, "summary"), excep);
			throw wrapStreamException(excep, "summary");
		}
	}

	protected static VeraPDFException wrapStreamException(final JAXBException excep, final String typePart) {
		return new VeraPDFException(String.format(unmarshalErrMessage, typePart), excep);
	}
	
}
