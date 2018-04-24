package org.verapdf.processor.reports.multithread;

import java.io.File;

public interface MultiThreadProcessingHandler {
	void startReport();

	void fillReport(File reportFile);

	void endReport();
}
