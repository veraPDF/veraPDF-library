package org.verapdf.processor.reports.multithread;

import java.io.File;

import org.verapdf.processor.reports.multithread.writer.ReportWriter;

public class MultiThreadProcessingHandlerImpl implements MultiThreadProcessingHandler {
	private ReportWriter reportWriter;

	public MultiThreadProcessingHandlerImpl(ReportWriter reportWriter) {
		this.reportWriter = reportWriter;
	}

	@Override
	public void startReport() {
		reportWriter.startDocument();
	}

	@Override
	public void fillReport(File reportFile) {
		reportWriter.write(reportFile);
	}

	@Override
	public void endReport() {
		reportWriter.endDocument();
		reportWriter.closeOutputStream();
	}
}
