package org.verapdf.processor.reports.multithread.writer;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

public class XmlReportWriter extends AbstractXmlReportWriter {
	private static final Logger LOGGER = Logger.getLogger(XmlReportWriter.class.getCanonicalName());

	private static final String RAW_RESULTS_TAG = "rawResults";
	private static final String ITEM_TAG = "item";
	private static final String VALIDATION_RESULT_TAG = "validationResult";
	private static final String PROCESSOR_CONFIG_TAG = "processorConfig";
	private static final String FEATURES_REPORT_TAG = "featuresReport";
	private static final String FIXER_RESULT_TAG = "fixerResult";

	protected XmlReportWriter(OutputStream os) throws XMLStreamException, ParserConfigurationException, SAXException {
		super(os);
	}

	@Override
	public synchronized void write(File reportFile) {
		try {
			if (this.isFirstReport) {
				this.writer.writeStartElement(XmlReportWriter.RAW_RESULTS_TAG);
				printFirstReport(reportFile);
				this.isFirstReport = false;
			} else {
				printReport(reportFile);
			}

			deleteFile(reportFile);

		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Can't printTag element", e);
		}
	}

	private void printReport(File reportFile) throws SAXException, IOException {
		super.printTag(reportFile, XmlReportWriter.ITEM_TAG, Boolean.FALSE);
		super.printTag(reportFile, XmlReportWriter.VALIDATION_RESULT_TAG, Boolean.TRUE);
		super.printTag(reportFile, XmlReportWriter.FEATURES_REPORT_TAG, Boolean.FALSE);
		super.printTag(reportFile, XmlReportWriter.FIXER_RESULT_TAG, Boolean.FALSE);
	}

	@Override
	public void printFirstReport(File report) throws SAXException, IOException {
		printTag(report, XmlReportWriter.PROCESSOR_CONFIG_TAG, Boolean.FALSE);
		printReport(report);
	}
}
