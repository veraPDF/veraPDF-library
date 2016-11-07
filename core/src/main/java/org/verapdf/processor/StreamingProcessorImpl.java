/**
 * 
 */
package org.verapdf.processor;

import java.io.File;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.verapdf.core.VeraPDFException;
import org.verapdf.core.XmlSerialiser;

/**
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *         <a href="https://github.com/carlwilson">carlwilson AT github</a>
 * @version 0.1 Created 2 Nov 2016:11:29:39
 */

public final class StreamingProcessorImpl extends AbstractStreamingProcessor {
	private final static String streamingErrMessage = "Exception when streaming results";
	private final static String encoding = "job";
	private final static String xmlVersion = "1.0";
	private final static String report = "report";
	private final static String job = "job";
	private final static String jobs = job + "s";
	private final static String lineSepProp = "line.separator";
	private final static String newline = System.getProperty(lineSepProp);
	private final static String strmClose = "Exception closing result stream";
	private static final Logger logger = Logger.getLogger(StreamingProcessorImpl.class.getCanonicalName());
	private static XMLOutputFactory outputFactory = XMLOutputFactory.newFactory();
	private XMLStreamWriter writer;

	private StreamingProcessorImpl(ItemProcessor processor) {
		super(processor);
	}

	@Override
	public BatchSummary processDirectory(File toProcess, OutputStream dest, boolean recurse) throws VeraPDFException {
		Writer osw = new OutputStreamWriter(dest);
		return processDirectory(toProcess, osw, recurse);
	}

	@Override
	public BatchSummary processDirectory(File toProcess, Writer dest, boolean recurse) throws VeraPDFException {
		try {
			this.writer = outputFactory.createXMLStreamWriter(dest);
			startDoc(this.writer);
			indentElement(jobs);
			this.processDirectory(toProcess, recurse);
			outdentElement();
			endDoc(this.writer);
		} catch (XMLStreamException excep) {
			logger.log(Level.WARNING, streamingErrMessage, excep);
			throw new VeraPDFException(streamingErrMessage, excep);
		}
		try {
			this.writer.close();
		} catch (XMLStreamException excep) {
			logger.log(Level.INFO, strmClose, excep);
		}
		return null;
	}

	@Override
	public BatchSummary process(List<? extends File> toProcess, Writer dest) throws VeraPDFException {
		try {
			this.writer = outputFactory.createXMLStreamWriter(dest);
			startDoc(this.writer);
			indentElement(jobs);
			this.processItems(toProcess);
			outdentElement();
			endDoc(this.writer);
		} catch (XMLStreamException excep) {
			logger.log(Level.WARNING, streamingErrMessage, excep);
			throw new VeraPDFException(streamingErrMessage, excep);
		}
		try {
			this.writer.close();
		} catch (XMLStreamException excep) {
			logger.log(Level.INFO, strmClose, excep);
		}
		return null;
	}

	static StreamingProcessor newInstance(ItemProcessor processor) {
		return new StreamingProcessorImpl(processor);
	}

	@Override
	protected void streamResult(ProcessorResult result) throws VeraPDFException {
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

	private static void startDoc(XMLStreamWriter writer) throws XMLStreamException {
		writer.writeStartDocument(encoding, xmlVersion);
		newLine(writer);
		writer.writeStartElement(report);
	}

	private static void endDoc(XMLStreamWriter writer) throws XMLStreamException {
		writer.writeEndElement();
		newLine(writer);
		writer.writeEndDocument();
	}

	private static void newLine(XMLStreamWriter writer) throws XMLStreamException {
		writer.writeCharacters(newline);
	}

	private static void newLine(XMLStreamWriter writer, int indent) throws XMLStreamException {
		newLine(writer);
		writer.writeCharacters(new String(new char[indent]).replace('\0', ' '));
	}

	private void indentElement(String eleName) throws XMLStreamException {
		newLine(this.writer, this.indent());
		this.writer.writeStartElement(eleName);
	}

	private void outdentElement() throws XMLStreamException {
		this.writer.writeEndElement();
		newLine(this.writer, this.outdent());
	}
}
