package org.verapdf.report;

import org.verapdf.validation.report.model.ValidationInfo;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Formatter;
import java.util.GregorianCalendar;


/**
 * Generating XML structure of file for general report
 *
 * @author Maksim Bezrukov
 */
public final class XMLReport {

    private static final long MS_IN_HOUR = 3600000;
    private static final long MS_IN_MIN = 60000;
    private static final long MS_IN_SEC = 1000;

    private XMLReport() {

    }

    private static String getProcessingTimeAsString(long processTime) {
        long processingTime = processTime;

        StringBuffer buffer = new StringBuffer();

        long hours = processingTime / MS_IN_HOUR;
        processingTime %= MS_IN_HOUR;

        long mins = processingTime / MS_IN_MIN;
        processingTime %= MS_IN_MIN;

        long sec = processingTime / MS_IN_SEC;
        processingTime %= MS_IN_SEC;

        long ms = processingTime;

        buffer.append(new Formatter().format("%02d", hours));
        buffer.append(":");
        buffer.append(new Formatter().format("%02d", mins));
        buffer.append(":");
        buffer.append(new Formatter().format("%02d", sec));
        buffer.append(".");
        buffer.append(new Formatter().format("%03d", ms));

        return buffer.toString();
    }

    /**
     * Creates tree of xml tags for general report
     *
     * @param info - validation info model to be writed
     * @param doc  - document used for writing xml in further
     * @return root element of the xml structure
     * @throws DatatypeConfigurationException - indicates a serious configurating error
     */
    public static Element makeXMLTree(ValidationInfo info, Document doc, long processingTimeInMS) throws DatatypeConfigurationException {

        Element report = doc.createElement("report");

        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        XMLGregorianCalendar now = DatatypeFactory.newInstance().newXMLGregorianCalendar(gregorianCalendar);

        report.setAttribute("creationDateTime", now.toXMLFormat());
        report.setAttribute("processingTime", getProcessingTimeAsString(processingTimeInMS));

        // TODO: Change next two lines to the normal generating of dom tree for documentInfo and processingInfo
        report.appendChild(doc.createElement("documentInfo"));
        report.appendChild(doc.createElement("processingInfo"));

        report.appendChild(XMLValidationReport.makeXMLTree(info, doc));

        return report;
    }

    private static Document generateDOMDocument(ValidationInfo info, long processingTimeInMS) throws ParserConfigurationException, DatatypeConfigurationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        DocumentBuilder builder = factory.newDocumentBuilder();

        Document doc = builder.newDocument();

        doc.appendChild(makeXMLTree(info, doc, processingTimeInMS));

        return doc;

    }

    private static void transform(Document doc, StreamResult outputTarget) throws TransformerException {
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.METHOD, "xml");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
        transformer.transform(new DOMSource(doc), outputTarget);
    }

    /**
     * Write the resulting report into xml formatted report.
     *
     * @param info - validation info model to be writed
     * @param path - the path for output the resulting document. Path have to ends with file name with extension.
     * @throws ParserConfigurationException         - if a DocumentBuilder cannot be created which satisfies the configuration requested.
     * @throws TransformerFactoryConfigurationError - thrown in case of service configuration error or if the implementation is not available or cannot be instantiated or when it is not possible to create a Transformer instance.
     * @throws TransformerException                 - if an unrecoverable error occurs during the course of the transformation or
     * @throws FileNotFoundException                - if the file with path {@code path} exists but is a directory rather than a regular file, does not exist but cannot be created, or cannot be opened for any other reason
     * @throws DatatypeConfigurationException       - indicates a serious configurating error
     */
    public static void writeXMLReport(ValidationInfo info, String path, long processingTimeInMS) throws ParserConfigurationException, TransformerFactoryConfigurationError, TransformerException, FileNotFoundException, DatatypeConfigurationException {

        Document doc = generateDOMDocument(info, processingTimeInMS);

        try (FileOutputStream fos = new FileOutputStream(path)) {
            transform(doc, new StreamResult(fos));
        } catch (IOException excep) {
            // TODO: Review this exception handling
            // Thrown on failure to close fos so do nothing
        }
    }

    /**
     * Write the resulting report into xml formatted report.
     *
     * @param info - validation info model to be writed
     * @return {@code String} representation of the resulting xml report
     * @throws ParserConfigurationException         - if a DocumentBuilder cannot be created which satisfies the configuration requested.
     * @throws TransformerFactoryConfigurationError - thrown in case of service configuration error or if the implementation is not available or cannot be instantiated or when it is not possible to create a Transformer instance.
     * @throws TransformerException                 - if an unrecoverable error occurs during the course of the transformation or
     * @throws FileNotFoundException                - if the file with path {@code path} exists but is a directory rather than a regular file, does not exist but cannot be created, or cannot be opened for any other reason
     * @throws DatatypeConfigurationException       - indicates a serious configurating error
     */
    public static String getXMLReportAsString(ValidationInfo info, long processingTimeInMS) throws ParserConfigurationException, TransformerFactoryConfigurationError, TransformerException, FileNotFoundException, DatatypeConfigurationException {

        Document doc = generateDOMDocument(info, processingTimeInMS);

        StringWriter writer = new StringWriter();
        transform(doc, new StreamResult(writer));
        return writer.getBuffer().toString();
    }

}
