package org.verapdf.processor.reports.multithread.writer;

import org.verapdf.processor.reports.ResultStructure;

import java.io.PrintWriter;
import java.util.logging.Logger;

public class JsonReportWriter extends ReportWriter {

	private static final Logger LOGGER = Logger.getLogger(JsonReportWriter.class.getCanonicalName());

	protected boolean isFirstReport;

	protected JsonReportWriter(PrintWriter outputStreamWriter, PrintWriter errorStreamWriter) {
		super(outputStreamWriter, errorStreamWriter);
	}

	@Override
	public void write(ResultStructure result) {
		if (isFirstReport) {
			isFirstReport = false;
		} else {
			outputStreamWriter.write(",");
		}
		merge(result.getReportFile(), outputStreamWriter);
		deleteTemp(result);
	}

	public void startDocument() {
		outputStreamWriter.write("{\"reports\":[");
		isFirstReport = true;
	}

	public void endDocument() {
		outputStreamWriter.write("]}");
	}

}
