package org.verapdf.processor.reports.multithread;

import org.verapdf.processor.reports.ResultStructure;
import org.verapdf.processor.reports.multithread.writer.ReportWriter;

public class MultiThreadProcessingHandlerImpl implements MultiThreadProcessingHandler {
	private final ReportWriter reportWriter;

	public MultiThreadProcessingHandlerImpl(ReportWriter reportWriter) {
		this.reportWriter = reportWriter;
	}

	@Override
	public void startReport() {
		reportWriter.startDocument();
	}

	@Override
	public void fillReport(ResultStructure result) {
		reportWriter.write(result);
	}

	@Override
	public void endReport() {
		reportWriter.endDocument();
		reportWriter.closeOutputStream();
	}
}
