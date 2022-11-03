package org.verapdf.processor.reports.multithread.writer;


import org.verapdf.processor.reports.ResultStructure;

import java.io.PrintWriter;

public class TextReportWriter extends ReportWriter {

	protected TextReportWriter(PrintWriter outputStreamWriter, PrintWriter errorStreamWriter) {
		super(outputStreamWriter, errorStreamWriter);
	}

	@Override
	public void write(ResultStructure result) {
		merge(result.getReportFile(), outputStreamWriter);
		deleteTemp(result);
	}

	@Override
	public void startDocument() {
		//NOP
	}

	@Override
	public void endDocument() {
		//NOP
	}
}
