package org.verapdf.processor.reports.multithread.writer;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.verapdf.processor.FormatOption;
import org.verapdf.processor.reports.ResultStructure;

public abstract class ReportWriter {
	private static final Logger LOGGER = Logger.getLogger(ReportWriter.class.getCanonicalName());

	protected PrintWriter outputStreamWriter;

	private PrintWriter errorStreamWriter;

	protected ReportWriter(PrintWriter outputStreamWriter, PrintWriter errorStreamWriter) {
		this.outputStreamWriter = outputStreamWriter;
		this.errorStreamWriter = errorStreamWriter;
	}

	public static ReportWriter newInstance(OutputStream os, FormatOption outputFormat, OutputStream errorStream) {
		PrintWriter outputStreamWriter = new PrintWriter(new OutputStreamWriter(os, StandardCharsets.UTF_8));
		PrintWriter errorStreamWriter = new PrintWriter(new OutputStreamWriter(errorStream, StandardCharsets.UTF_8));
		try {
			switch (outputFormat) {
				case TEXT:
					return new TextReportWriter(outputStreamWriter, errorStreamWriter);
				case MRR:
				case XML:
					return new MrrReportWriter(outputStreamWriter, errorStreamWriter);
				case RAW:
					return new RawReportWriter(outputStreamWriter, errorStreamWriter);
				case JSON:
					return new JsonReportWriter(outputStreamWriter, errorStreamWriter);
				default:
					return null;
			}
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Can't create new ReportWriter instance", e);
			return null;
		}
	}

	public abstract void write(ResultStructure result);

	public abstract void startDocument();

	public abstract void endDocument();

	protected void merge(File report, PrintWriter destination) {
		try (FileReader fis = new FileReader(report)) {
			int read;
			while ((read = fis.read()) != -1) {
				destination.write(read);
			}
			destination.flush();
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Can't read from report file", e);
		}
	}

	protected void deleteTemp(ResultStructure result) {
		deleteFile(result.getReportFile());
	}

	private void deleteFile(File file) {
		if (!file.delete()) {
			file.deleteOnExit();
		}
	}

	public void closeOutputStream() {
		outputStreamWriter.flush();
		outputStreamWriter.close();
	}
}
