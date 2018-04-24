package org.verapdf.processor.reports.multithread.writer;

import org.verapdf.processor.FormatOption;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class ReportWriter {
	private static final Logger LOGGER = Logger.getLogger(ReportWriter.class.getCanonicalName());

	protected OutputStream os;


	protected ReportWriter(OutputStream os) {
		this.os = os;
	}

	public static ReportWriter newInstance(OutputStream os, FormatOption outputFormat) {
		try {
			switch (outputFormat) {
				case TEXT:
					return new TextReportWriter(os);
				case MRR:
					return new MrrReportWriter(os);
				case XML:
					return new XmlReportWriter(os);
				default:
					return null;
			}
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Can't create new ReportWriter instance", e);
			return null;
		}
	}

	public abstract void write(File reportFile);

	public abstract void startDocument();

	public abstract void endDocument();

	protected void merge(File report, OutputStream destination) {
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

	protected static void deleteFile(File file) {
		if (!file.delete()) {
			file.deleteOnExit();
		}
	}

	public void closeOutputStream() {
		try {
			os.flush();
			os.close();
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, "Can't close output stream", e);
		}
	}
}
