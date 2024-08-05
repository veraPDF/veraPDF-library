/**
 * This file is part of veraPDF Library core, a module of the veraPDF project.
 * Copyright (c) 2015-2024, veraPDF Consortium <info@verapdf.org>
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

import org.verapdf.processor.reports.ResultStructure;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MrrReportWriter extends AbstractXmlReportWriter {
	private static final Logger LOGGER = Logger.getLogger(MrrReportWriter.class.getCanonicalName());
	private static final String REPORT_TAG = "report";
	private static final String BUILD_INFORMATION_TAG = "buildInformation";
	private static final String JOBS_TAG = "jobs";
	private static final String JOB_TAG = "job";

	MrrReportWriter(PrintWriter outputStreamWriter, PrintWriter errorStreamWriter) throws XMLStreamException, ParserConfigurationException, SAXException {
		super(outputStreamWriter, errorStreamWriter);
	}

	@Override
	public void write(ResultStructure result) {
		try {
			File reportFile = result.getReportFile();
			if (isFirstReport) {
				writer.writeStartElement(REPORT_TAG);
				printFirstReport(reportFile);
				isFirstReport = false;
			} else {
				super.printTag(reportFile, JOB_TAG, true);
			}

			deleteTemp(result);

		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Can't write element", e);
		}
	}

	@Override
	public void endDocument() {
		writeEndElement();
		super.endDocument();
	}

	public void writeEndElement() {
		try {
			writer.writeEndElement();
		} catch (XMLStreamException e) {
			LOGGER.log(Level.SEVERE, "Can't write end element", e);
		}
	}

	@Override
	public void printFirstReport(File report) throws SAXException, IOException, XMLStreamException {
		printTag(report, BUILD_INFORMATION_TAG, false);
		writer.writeStartElement(JOBS_TAG);
		printTag(report, JOB_TAG, true);
	}
}
