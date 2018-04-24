package org.verapdf.processor.reports.multithread;

import org.verapdf.processor.reports.ResultStructure;

public interface MultiThreadProcessingHandler {
	void startReport();

	void fillReport(ResultStructure result);

	void endReport();
}
