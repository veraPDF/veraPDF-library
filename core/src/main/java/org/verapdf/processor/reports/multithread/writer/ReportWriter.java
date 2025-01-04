/**
 * This file is part of veraPDF Library core, a module of the veraPDF project.
 * Copyright (c) 2015-2025, veraPDF Consortium <info@verapdf.org>
 * All rights reserved.
 *
 * veraPDF Library core is free software: you can redistribute it and/or modify
 * it under the terms of either:
 *
 * The GNU General public license GPLv3+.
 * You should have received a copy of the GNU General Public License
 * along with veraPDF Library core as the LICENSE.GPL file in the root of the source
 * tree.  If not, see http://www.gnu.org/licenses/ or
 * https://www.gnu.org/licenses/gpl-3.0.en.html.
 *
 * The Mozilla Public License MPLv2+.
 * You should have received a copy of the Mozilla Public License along with
 * veraPDF Library core as the LICENSE.MPL file in the root of the source tree.
 * If a copy of the MPL was not distributed with this file, you can obtain one at
 * http://mozilla.org/MPL/2.0/.
 */
package org.verapdf.processor.reports.multithread.writer;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.verapdf.processor.FormatOption;
import org.verapdf.processor.reports.ResultStructure;

public abstract class ReportWriter {
	private static final Logger LOGGER = Logger.getLogger(ReportWriter.class.getCanonicalName());

	protected final PrintWriter outputStreamWriter;

	private final PrintWriter errorStreamWriter;

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
