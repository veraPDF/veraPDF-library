package org.verapdf.processor.reports;

import java.io.File;

public class ResultStructure {

	private final File reportFile;

	public ResultStructure(File reportFile) {
		this.reportFile = reportFile;
	}

	public File getReportFile() {
		return reportFile;
	}
	
}
