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
	private static final String xslExt = ".xls"; //$NON-NLS-1$
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
