/**
 * This file is part of veraPDF Library core, a module of the veraPDF project.
 * Copyright (c) 2015, veraPDF Consortium <info@verapdf.org>
 * All rights reserved.
 *
 * veraPDF Library core is free software: you can redistribute it and/or modify
 * it under the terms of either:
 *
 * The GNU General public license GPLv3+.
 * You should have received a copy of the GNU General Public License
 * along with veraPDF Library core as the LICENSE.GPL file in the root of the source
 * tree.  If not, see http://www.gnu.org/licenses/ or
 * https://www.gnu.org/licenses/gpl-3.0.en.html.
 *
 * The Mozilla Public License MPLv2+.
 * You should have received a copy of the Mozilla Public License along with
 * veraPDF Library core as the LICENSE.MPL file in the root of the source tree.
 * If a copy of the MPL was not distributed with this file, you can obtain one at
 * http://mozilla.org/MPL/2.0/.
 */
/**
 * 
 */
package org.verapdf.processor;

import org.verapdf.core.VeraPDFException;

import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.Writer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *         <a href="https://github.com/carlwilson">carlwilson AT github</a>
 * @version 0.1 Created 09 Nov 2016:00:32:49
 */

public abstract class AbstractXmlHandler extends AbstractBatchHandler {
	private static final Logger logger = Logger.getLogger(AbstractXmlHandler.class.getCanonicalName());
	protected final static String strmExcpMessTmpl = "XmlStreamException caught when %s output writer.";
	protected final static String unmarshalErrMessage = "Unmarshalling exception when streaming %s.";
	private final static String encoding = "utf-8";
	private final static String xmlVersion = "1.0";
	private final static String lineSepProp = "line.separator";
	private final static String newline = System.getProperty(lineSepProp);
	private static XMLOutputFactory outputFactory = XMLOutputFactory.newFactory();

	protected XMLStreamWriter writer;
	private final int indentSize;
	private int indent = 0;

	protected AbstractXmlHandler(final Writer dest) throws VeraPDFException {
		this(dest, 2);
	}

	/**
	 * 
	 */
	protected AbstractXmlHandler(final Writer dest, final int indentSize) throws VeraPDFException {
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

	protected void addAttribute(String name, String value) throws XMLStreamException {
		this.writer.writeAttribute(name, value);
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

	protected static final VeraPDFException wrapMarshallException(final JAXBException excep, final String typePart) {
		return new VeraPDFException(String.format(unmarshalErrMessage, typePart), excep);
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
