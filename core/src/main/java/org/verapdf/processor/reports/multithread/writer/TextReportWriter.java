package org.verapdf.processor.reports.multithread.writer;


import java.io.File;
import java.io.OutputStream;

public class TextReportWriter extends ReportWriter {

	protected TextReportWriter(OutputStream os) {
		super(os);
	}

	@Override
	public void write(File reportFile) {
		merge(reportFile, os);
		deleteFile(reportFile);
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
