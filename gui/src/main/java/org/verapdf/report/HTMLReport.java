package org.verapdf.report;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.bind.JAXBException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

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
            OutputStream destination) throws TransformerException, IOException,
            JAXBException {

        TransformerFactory factory = TransformerFactory.newInstance();

        Transformer transformer = factory.newTransformer(new StreamSource(
                HTMLReport.class.getClassLoader().getResourceAsStream(
                        "HTMLReportStylesheet.xsl")));

        transformer.transform(new StreamSource(source), new StreamResult(
                destination));
    }

}
