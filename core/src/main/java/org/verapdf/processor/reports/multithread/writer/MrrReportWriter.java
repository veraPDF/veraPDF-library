package org.verapdf.processor.reports.multithread.writer;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MrrReportWriter extends AbstractXmlReportWriter {
	private static final Logger LOGGER = Logger.getLogger(MrrReportWriter.class.getCanonicalName());
	private final String REPORT_TAG = "report";
	private final String BUILD_INFORMATION_TAG = "buildInformation";
	private final String JOBS_TAG = "jobs";
	private final String JOB_TAG = "job";

	MrrReportWriter(OutputStream os) throws XMLStreamException, ParserConfigurationException, SAXException {
		super(os);
	}

	@Override
	public void write(File reportFile) {
		try {
			if (this.isFirstReport) {
				this.writer.writeStartElement(this.REPORT_TAG);
				printFirstReport(reportFile);
				this.isFirstReport = false;
			} else {
				super.printTag(reportFile, this.JOB_TAG, Boolean.TRUE);
			}

			deleteFile(reportFile);

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
			this.writer.writeEndElement();
		} catch (XMLStreamException e) {
			LOGGER.log(Level.SEVERE, "Can't write end element", e);
		}
	}

	@Override
	public void printFirstReport(File report) throws SAXException, IOException, XMLStreamException {
		printTag(report, this.BUILD_INFORMATION_TAG, Boolean.FALSE);
		this.writer.writeStartElement(this.JOBS_TAG);
		printTag(report, this.JOB_TAG, Boolean.TRUE);
	}
}
