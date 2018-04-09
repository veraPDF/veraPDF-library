package org.verapdf.processor.reports.multithread.writer;

import javanet.staxutils.IndentingXMLStreamWriter;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class AbstractXmlReportWriter extends ReportWriter {
	private static final Logger LOGGER = Logger.getLogger(AbstractXmlReportWriter.class.getCanonicalName());

	protected final javax.xml.stream.XMLStreamWriter writer;

	protected SAXParser saxParser;
	protected ReportParserEventHandler reportHandler;
	protected boolean isFirstReport;

	protected AbstractXmlReportWriter(OutputStream os, OutputStream errorStream) throws XMLStreamException, ParserConfigurationException, SAXException {
		super(os, errorStream);
		this.saxParser = SAXParserFactory.newInstance().newSAXParser();
		IndentingXMLStreamWriter writer = new IndentingXMLStreamWriter(XMLOutputFactory.newFactory().createXMLStreamWriter(os));
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
