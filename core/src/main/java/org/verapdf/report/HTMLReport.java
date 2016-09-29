package org.verapdf.report;

import javax.xml.transform.TransformerException;
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
     *
     * @throws TransformerException
     *             if an unrecoverable error occurs during the course of the
     *             transformation
     * @throws IOException
     *             file system exceptions
     * @throws JAXBException
     */
    public static void writeHTMLReport(InputStream source,
            OutputStream destination, String wikiPath, boolean isFullHTML) throws TransformerException{
        Map<String, String> arguments = new HashMap<>();
        arguments.put("wikiPath", wikiPath);
        arguments.put("isFullHTML", Boolean.toString(isFullHTML));

        XsltTransformer.transform(source, HTMLReport.class.getClassLoader().getResourceAsStream(
                "org/verapdf/report/HTMLReportStylesheet.xsl"), destination, arguments);
    }

}
