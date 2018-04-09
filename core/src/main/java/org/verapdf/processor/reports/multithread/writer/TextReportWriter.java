package org.verapdf.processor.reports.multithread.writer;


import org.verapdf.processor.reports.ResultStructure;

import java.io.OutputStream;

public class TextReportWriter extends ReportWriter {

	protected TextReportWriter(OutputStream os, OutputStream errorStream) {
		super(os, errorStream);
	}

	@Override
	public void write(ResultStructure result) {
		merge(result.getReportFile(), os);
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
