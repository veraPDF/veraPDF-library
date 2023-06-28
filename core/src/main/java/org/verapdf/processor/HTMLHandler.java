/**
 * This file is part of veraPDF Library core, a module of the veraPDF project.
 * Copyright (c) 2015, veraPDF Consortium <info@verapdf.org>
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
/**
 * 
 */
package org.verapdf.processor;

import org.verapdf.core.VeraPDFException;
import org.verapdf.processor.reports.BatchSummary;
import org.verapdf.report.HTMLReport;

import javax.xml.transform.TransformerException;
import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * @author Maxim Plushchov
 */
final class HTMLHandler extends MrrHandler {

	private final File file;
	private final PrintWriter reportStreamWriter;
	private final String wikiPath;

	private HTMLHandler(PrintWriter reportStreamWriter, File file, String wikiPath) throws VeraPDFException, IOException {
		super(new PrintWriter(new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8)), false);
		this.reportStreamWriter = reportStreamWriter;
		this.file = file;
		this.wikiPath = wikiPath;
	}

	static BatchProcessingHandler newInstance(final PrintWriter reportStreamWriter, final String wikiPath) throws VeraPDFException {
		try {
			File file = File.createTempFile("veraPDF","xmlReport");
			return new HTMLHandler(reportStreamWriter, file, wikiPath);
		} catch (IOException exception) {
			throw new VeraPDFException(exception.getMessage(), exception);
		}
	}

	@Override
	public void handleBatchEnd(BatchSummary summary) throws VeraPDFException {
		super.handleBatchEnd(summary);
		try (InputStream xmlStream = new FileInputStream(file)) {
			HTMLReport.writeHTMLReport(xmlStream, reportStreamWriter, summary.isMultiJob(), wikiPath, true);
		} catch (IOException | TransformerException e) {
			throw new VeraPDFException(e.getMessage(), e);
		}
		file.deleteOnExit();
		this.reportStreamWriter.flush();
		this.reportStreamWriter.close();
	}

}
