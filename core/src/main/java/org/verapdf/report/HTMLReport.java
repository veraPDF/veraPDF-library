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
package org.verapdf.report;

import javax.xml.transform.TransformerException;

import org.verapdf.processor.reports.BatchSummary;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Generating HTML validation report
 *
 * @author Maksim Bezrukov
 */
public final class HTMLReport {
	private static final String resourceRoot = "org/verapdf/report/"; //$NON-NLS-1$
	private static final String xslExt = ".xsl"; //$NON-NLS-1$
	private static final String detailedReport = resourceRoot + "DetailedHtmlReport" + xslExt; //$NON-NLS-1$
	private static final String summaryReport = resourceRoot + "SummaryHtmlReport" + xslExt; //$NON-NLS-1$

	private HTMLReport() {
	}

	/**
	 * Creates html validation report
	 * 
	 * @param source
	 *            an {@link InputStream} instance that is the source Machine
	 *            Readable Report.
	 * @param destination
	 *            an {@link OutputStream} to write the HTML report to.
	 * @throws TransformerException
	 *             if an unrecoverable error occurs during the course of the
	 *             transformation
	 * @throws IOException
	 *             file system exceptions
	 * @throws JAXBException
	 */
	public static void writeHTMLReport(InputStream source, OutputStream destination, BatchSummary summary, String wikiPath,
			boolean isFullHTML) throws TransformerException {
		String reportPath = (summary.getJobs() > 1) ? summaryReport : detailedReport;
		Map<String, String> arguments = new HashMap<>();
		arguments.put("wikiPath", wikiPath); //$NON-NLS-1$
		arguments.put("isFullHTML", Boolean.toString(isFullHTML)); //$NON-NLS-1$
		XsltTransformer.transform(source, HTMLReport.class.getClassLoader().getResourceAsStream(reportPath),
				destination, arguments);
	}

}
