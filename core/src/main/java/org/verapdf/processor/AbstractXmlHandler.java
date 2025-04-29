/**
 * This file is part of veraPDF Library core, a module of the veraPDF project.
 * Copyright (c) 2015-2025, veraPDF Consortium <info@verapdf.org>
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
import org.verapdf.core.XmlSerialiser;

import javanet.staxutils.IndentingXMLStreamWriter;

import jakarta.xml.bind.JAXBException;
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
	protected static final String strmExcpMessTmpl = "XmlStreamException caught when %s output writer."; //$NON-NLS-1$
	protected static final String unmarshalErrMessage = "Unmarshalling exception when streaming %s."; //$NON-NLS-1$
	protected static final String writingMessage = "writing"; //$NON-NLS-1$
	private static final String encoding = "utf-8"; //$NON-NLS-1$
	private static final String xmlVersion = "1.0"; //$NON-NLS-1$
	private static final String lineSepProp = "line.separator"; //$NON-NLS-1$
	private static final String newline = System.getProperty(lineSepProp);
	private static final XMLOutputFactory outputFactory = XMLOutputFactory.newFactory();

	protected XMLStreamWriter writer;

	protected AbstractXmlHandler(final Writer dest) throws VeraPDFException {
		super();
		try {
			this.writer = new IndentingXMLStreamWriter(outputFactory.createXMLStreamWriter(dest));
		} catch (XMLStreamException excep) {
			throw wrapStreamException(excep, "initialising"); //$NON-NLS-1$
		}
	}

	protected <T> void serializeElement(T obj, String eleName, boolean format, boolean fragment)
			throws VeraPDFException {
		try {
			XmlSerialiser.toXml(obj, this.writer, format, fragment);
			this.writer.flush();
		} catch (JAXBException excep) {
			logger.log(Level.WARNING, String.format(unmarshalErrMessage, eleName), excep);
			throw wrapMarshallException(excep, eleName);
		} catch (XMLStreamException excep) {
			logger.log(Level.WARNING, String.format(strmExcpMessTmpl, writingMessage), excep);
			throw wrapStreamException(excep, eleName);
		}
	}

	@Override
	public void close() {
		try {
			this.writer.flush();
			this.writer.close();
		} catch (XMLStreamException excep) {
			logger.log(Level.INFO, String.format(strmExcpMessTmpl, "closing"), excep); //$NON-NLS-1$
		}
	}

	protected static void startDoc(XMLStreamWriter writer) throws XMLStreamException {
		writer.writeStartDocument(encoding, xmlVersion);
	}

	protected static void endDoc(XMLStreamWriter writer) throws XMLStreamException {
		newLine(writer);
		writer.writeEndDocument();
	}

	protected void addAttribute(String name, String value) throws XMLStreamException {
		this.writer.writeAttribute(name, value);
	}

	protected static void newLine(XMLStreamWriter writer) throws XMLStreamException {
		writer.writeCharacters(newline);
	}

	protected static VeraPDFException wrapStreamException(final XMLStreamException excep) {
		return wrapStreamException(excep, "writing to"); //$NON-NLS-1$
	}

	protected static VeraPDFException wrapStreamException(final XMLStreamException excep, final String verbPart) {
		return new VeraPDFException(String.format(strmExcpMessTmpl, verbPart), excep);
	}

	protected static final VeraPDFException wrapMarshallException(final JAXBException excep, final String typePart) {
		return new VeraPDFException(String.format(unmarshalErrMessage, typePart), excep);
	}

}
