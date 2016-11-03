/**
 * 
 */
package org.verapdf.processor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.verapdf.ReleaseDetails;
import org.verapdf.component.ComponentDetails;
import org.verapdf.core.XmlSerialiser;

/**
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *         <a href="https://github.com/carlwilson">carlwilson AT github</a>
 * @version 0.1 Created 2 Nov 2016:11:29:39
 */

public class StreamingProcessorImpl implements StreamingProcessor {
	private final static int indentSize = 2;
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
	private final ItemProcessor processor;
	private int indent = 0;
	private XMLStreamWriter writer;

	private StreamingProcessorImpl(ItemProcessor processor) {
		this.processor = processor;
	}

	/**
	 * @see org.verapdf.processor.Processor#getConfig()
	 */
	@Override
	public ProcessorConfig getConfig() {
		return this.processor.getConfig();
	}

	/**
	 * @see org.verapdf.processor.Processor#getDependencies()
	 */
	@Override
	public Collection<ReleaseDetails> getDependencies() {
		return this.processor.getDependencies();
	}

	/**
	 * @see org.verapdf.component.Component#getDetails()
	 */
	@Override
	public ComponentDetails getDetails() {
		return this.processor.getDetails();
	}

	@Override
	public BatchSummary processDirectory(File toProcess, OutputStream dest, boolean recurse)
			throws XMLStreamException, JAXBException {
		Writer osw = new OutputStreamWriter(dest);
		return processDirectory(toProcess, osw, recurse);
	}

	@Override
	public BatchSummary processDirectory(File toProcess, Writer dest, boolean recurse)
			throws XMLStreamException, JAXBException {
		this.writer = outputFactory.createXMLStreamWriter(dest);
		startDoc(this.writer);
		this.processDirectory(toProcess, recurse);
		endDoc(this.writer);
		try {
			this.writer.close();
		} catch (XMLStreamException excep) {
			logger.log(Level.INFO, strmClose, excep);
		}
		return null;
	}

	/**
	 * @see org.verapdf.processor.StreamingProcessor#process(java.util.List,
	 *      java.io.OutputStream)
	 */
	@Override
	public BatchSummary process(List<? extends File> toProcess, OutputStream dest) throws XMLStreamException, JAXBException {
		Writer osw = new OutputStreamWriter(dest);
		return this.process(toProcess, osw);
	}

	@Override
	public BatchSummary process(List<? extends File> toProcess, Writer dest) throws XMLStreamException, JAXBException {
		this.writer = outputFactory.createXMLStreamWriter(dest);
		startDoc(this.writer);
		this.processItems(toProcess);
		endDoc(this.writer);
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

	private void processDirectory(File root, boolean recurse) throws XMLStreamException, JAXBException {
		if (root == null || !root.isDirectory() || !root.canRead()) {
			logger.log(Level.SEVERE, badItemMessage(root, true));
		} else {
			indentElement(jobs);
			processDir(root, recurse);
			outdentElement();
		}
	}

	private void processDir(File root, boolean recurse) throws XMLStreamException, JAXBException {
		for (File item : root.listFiles()) {
			if (item.isDirectory() && !item.isHidden() && item.canRead()) {
				processDir(item, recurse);
			} else {
				processItem(item);
			}
		}
	}

	private void processItems(List<? extends File> items) throws XMLStreamException, JAXBException {
		indentElement(jobs);
		for (File item : items) {
			if (item == null || !item.isFile() || !item.canRead()) {
				logger.log(Level.SEVERE, badItemMessage(item, false));
			} else {
				processItem(item);
			}
		}
		outdentElement();
	}

	private void processItem(File item) throws XMLStreamException, JAXBException {
		try {
			indentElement(job);
			newLine(this.writer, this.indent());
			streamResult(this.processor.process(item), this.writer);
		} catch (FileNotFoundException excep) {
			// Should really never happen after defensive file check in process
			// items
			logger.log(Level.SEVERE, badItemMessage(item, false), excep);
		}
		newLine(this.writer, this.outdent());
		outdentElement();
	}

	private static void streamResult(ProcessorResult result, XMLStreamWriter writer) throws JAXBException {
		XmlSerialiser.toXml(result.getValidationResult(), writer, true, true);
	}

	private static String badItemMessage(File item, boolean isDir) {
		String itemType = isDir ? "directory" : "file";
		if (item == null)
			return "Null " + itemType + " item passed for processing.";
		final String rootMessage = "Couldn't process: " + item.getAbsolutePath() + " is not a ";
		final String messageTrail = (item.canRead()) ? itemType + "." : "readable " + itemType + ".";
		return rootMessage + messageTrail;
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

	private int indent() {
		return this.indent = this.indent + indentSize;
	}

	private int outdent() {
		if (this.indent >= indentSize)
			return this.indent = this.indent - indentSize;
		return this.indent = 0;
	}
}
