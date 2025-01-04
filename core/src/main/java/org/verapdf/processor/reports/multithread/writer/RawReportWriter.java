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

import org.verapdf.processor.reports.ResultStructure;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RawReportWriter extends AbstractXmlReportWriter {
	private static final Logger LOGGER = Logger.getLogger(RawReportWriter.class.getCanonicalName());

	private static final String RAW_RESULTS_TAG = "rawResults";
	private static final String ITEM_TAG = "item";
	private static final String VALIDATION_RESULT_TAG = "arlingtonResult";
	private static final String PROCESSOR_CONFIG_TAG = "processorConfig";
	private static final String FEATURES_REPORT_TAG = "featuresReport";
	private static final String FIXER_RESULT_TAG = "fixerResult";

	protected RawReportWriter(PrintWriter outputStreamWriter, PrintWriter errorStreamWriter) throws XMLStreamException, ParserConfigurationException, SAXException {
		super(outputStreamWriter, errorStreamWriter);
	}

	@Override
	public synchronized void write(ResultStructure result) {
		try {
			File reportFile = result.getReportFile();
			if (isFirstReport) {
				writer.writeStartElement(RAW_RESULTS_TAG);
				printFirstReport(reportFile);
				isFirstReport = false;
			} else {
				printReport(reportFile);
			}

			deleteTemp(result);

		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Can't printTag element", e);
		}
	}

	private void printReport(File reportFile) throws SAXException, IOException {
		super.printTag(reportFile, ITEM_TAG, false);
		super.printTag(reportFile, VALIDATION_RESULT_TAG, true);
		super.printTag(reportFile, FEATURES_REPORT_TAG, false);
		super.printTag(reportFile, FIXER_RESULT_TAG, false);
	}

	@Override
	public void printFirstReport(File report) throws SAXException, IOException {
		printTag(report, PROCESSOR_CONFIG_TAG, false);
		printReport(report);
	}
}
