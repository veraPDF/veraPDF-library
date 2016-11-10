/**
 * 
 */
package org.verapdf.processor;

import java.io.PrintWriter;
import java.io.Writer;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.verapdf.core.VeraPDFException;

/**
 * @author  <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *          <a href="https://github.com/carlwilson">carlwilson AT github</a>
 *
 * @version 0.1
 * 
 * Created 09 Nov 2016:00:32:49
 */

public abstract class AbstractXmlHandler implements BatchProcessingHandler {
	private static final Logger logger = Logger.getLogger(AbstractXmlHandler.class.getCanonicalName());
	protected final static String strmExcpMessTmpl = "XmlStreamException caught when %s output writer.";
	private final static String encoding = "utf-8";
	private final static String xmlVersion = "1.0";
	private final static String lineSepProp = "line.separator";
	private final static String newline = System.getProperty(lineSepProp);
	private static XMLOutputFactory outputFactory = XMLOutputFactory.newFactory();

	protected XMLStreamWriter writer;
	private final int indentSize;
	private int indent = 0;
	
	public AbstractXmlHandler() throws VeraPDFException {
		this(new PrintWriter(System.out));
	}

	public AbstractXmlHandler(final Writer dest) throws VeraPDFException {
		this(dest, 2);
	}
	/**
	 * 
	 */
	public AbstractXmlHandler(final Writer dest, final int indentSize) throws VeraPDFException {
		super();
		this.indentSize = indentSize;
		try {
			this.writer = outputFactory.createXMLStreamWriter(dest);
		} catch (XMLStreamException excep) {
			throw wrapStreamException(excep, "initialising");
		}
	}

	protected int indent() {
		return this.indent = this.indent + indentSize;
	}

	protected int outdent() {
		if (this.indent >= indentSize)
			return this.indent = this.indent - indentSize;
		return this.indent = 0;
	}

	protected static void startDoc(XMLStreamWriter writer) throws XMLStreamException {
		writer.writeStartDocument(encoding, xmlVersion);
	}

	protected static void endDoc(XMLStreamWriter writer) throws XMLStreamException {
		writer.writeEndDocument();
	}

	protected void indentElement(String eleName) throws XMLStreamException {
		newLine(this.writer, this.indent());
		this.writer.writeStartElement(eleName);
	}

	protected void outdentElement() throws XMLStreamException {
		this.writer.writeEndElement();
		newLine(this.writer, this.outdent());
	}

	protected static void newLine(XMLStreamWriter writer) throws XMLStreamException {
		writer.writeCharacters(newline);
	}

	protected static void newLine(XMLStreamWriter writer, int indent) throws XMLStreamException {
		newLine(writer);
		writer.writeCharacters(new String(new char[indent]).replace('\0', ' '));
	}

	protected static VeraPDFException wrapStreamException(final XMLStreamException excep) {
		return wrapStreamException(excep, "writing to");
	}

	protected static VeraPDFException wrapStreamException(final XMLStreamException excep, final String verbPart) {
		return new VeraPDFException(String.format(strmExcpMessTmpl, verbPart), excep);
	}
	
	@Override
	public void close() {
		try {
			this.writer.close();
		} catch (XMLStreamException excep) {
			logger.log(Level.INFO, String.format(strmExcpMessTmpl, "closing"), excep);
		}
	}
}
