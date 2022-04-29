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

/**
 * @author Maxim Plushchov
 */
final class HTMLHandler extends MrrHandler {

	private final File file;
	private final OutputStream reportStream;
	private final String wikiPath;

	private HTMLHandler(OutputStream reportStream, File file, String wikiPath) throws VeraPDFException, IOException {
		super(new PrintWriter(new FileOutputStream(file)), false);
		this.reportStream = reportStream;
		this.file = file;
		this.wikiPath = wikiPath;
	}

	static BatchProcessingHandler newInstance(final OutputStream reportStream, final String wikiPath) throws VeraPDFException {
		try {
			File file = File.createTempFile("veraPDF","xmlReport");
			return new HTMLHandler(reportStream, file, wikiPath);
		} catch (IOException exception) {
			throw new VeraPDFException(exception.getMessage(), exception);
		}
	}

	@Override
	public void handleBatchEnd(BatchSummary summary) throws VeraPDFException {
		super.handleBatchEnd(summary);
		try (InputStream xmlStream = new FileInputStream(file)) {
			HTMLReport.writeHTMLReport(xmlStream, reportStream, summary.isMultiJob(), wikiPath, true);
		} catch (IOException | TransformerException e) {
			throw new VeraPDFException(e.getMessage(), e);
		}
		file.deleteOnExit();
	}

}
