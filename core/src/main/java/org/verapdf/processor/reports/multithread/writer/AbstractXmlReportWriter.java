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
package org.verapdf.processor.reports.multithread.writer;

import javanet.staxutils.IndentingXMLStreamWriter;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class AbstractXmlReportWriter extends ReportWriter {
	private static final Logger LOGGER = Logger.getLogger(AbstractXmlReportWriter.class.getCanonicalName());

	protected final javax.xml.stream.XMLStreamWriter writer;

	protected SAXParser saxParser;
	protected ReportParserEventHandler reportHandler;
	protected boolean isFirstReport;

	protected AbstractXmlReportWriter(PrintWriter outputStreamWriter, PrintWriter errorStreamWriter)
			throws XMLStreamException, ParserConfigurationException, SAXException {
		super(outputStreamWriter, errorStreamWriter);
		this.saxParser = SAXParserFactory.newInstance().newSAXParser();
		IndentingXMLStreamWriter writer = new IndentingXMLStreamWriter(XMLOutputFactory.newFactory()
				.createXMLStreamWriter(outputStreamWriter));
		this.writer = writer;
		this.reportHandler = new ReportParserEventHandler(writer);
	}

	protected abstract void printFirstReport(File report) throws SAXException, IOException, XMLStreamException;

	@Override
	public void startDocument() {
		try {
			this.isFirstReport = true;
            this.writer.writeStartDocument();
		} catch (XMLStreamException e) {
			LOGGER.log(Level.SEVERE, "Can't write start document", e);
		}
	}

	@Override
	public void endDocument() {
		try {
			this.reportHandler.printSummary();
			this.writer.writeEndElement();
			this.writer.writeEndDocument();
			this.writer.flush();

		} catch (XMLStreamException e) {
			e.printStackTrace();
		}
	}

	protected void printTag(File report, String tag, Boolean isAddReportToSummary) throws SAXException, IOException {
		this.reportHandler.setElement(tag);
		this.reportHandler.setIsAddReportToSummary(isAddReportToSummary);
		this.saxParser.parse(report, this.reportHandler);
	}
}
