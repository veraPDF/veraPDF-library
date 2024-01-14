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
