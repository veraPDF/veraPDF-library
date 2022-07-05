package org.verapdf.processor.reports.multithread.writer;

import org.verapdf.processor.reports.ResultStructure;

import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JsonReportWriter extends ReportWriter {

	private static final Logger LOGGER = Logger.getLogger(JsonReportWriter.class.getCanonicalName());

	protected boolean isFirstReport;

	protected JsonReportWriter(OutputStream os, OutputStream errorStream) {
		super(os, errorStream);
	}

	public void write(ResultStructure result) {
		if (isFirstReport) {
			isFirstReport = false;
		} else {
			try {
				os.write(",".getBytes());
			} catch (IOException e) {
				LOGGER.log(Level.SEVERE, "Can't write", e);
			}
		}
		merge(result.getReportFile(), os);
		deleteTemp(result);
	}

	public void startDocument() {
		try {
			os.write("{\"reports\":[".getBytes());
			isFirstReport = true;
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, "Can't write start document", e);
		}
	}

	public void endDocument() {
		try {
			os.write("]}".getBytes());
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, "Can't write end document", e);
		}
	}

}
