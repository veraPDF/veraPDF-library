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
