package org.verapdf.processor.reports.multithread.writer;

import javanet.staxutils.IndentingXMLStreamWriter;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class AbstractXmlReportWriter extends ReportWriter {
	private static final Logger LOGGER = Logger.getLogger(AbstractXmlReportWriter.class.getCanonicalName());

	protected final XMLStreamWriter writer;

	protected SAXParser saxParser;
	protected ReportParserEventHandler reportHandler;
	protected boolean isFirstReport;

	protected AbstractXmlReportWriter(OutputStream os) throws XMLStreamException, ParserConfigurationException, SAXException {
		super(os);
		this.saxParser = SAXParserFactory.newInstance().newSAXParser();
		this.writer = new IndentingXMLStreamWriter(XMLOutputFactory.newFactory().createXMLStreamWriter(os));
		this.reportHandler = new ReportParserEventHandler(this.writer);
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
		this.reportHandler.setIsAddReportToSummary(isAddReportToSummary.booleanValue());
		this.saxParser.parse(report, this.reportHandler);
	}
}
