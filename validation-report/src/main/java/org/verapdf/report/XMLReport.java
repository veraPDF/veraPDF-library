package org.verapdf.report;

import org.apache.log4j.Logger;
import org.verapdf.features.tools.FeaturesCollection;
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
    private static final Logger LOGGER = Logger.getLogger(XMLReport.class);

    private static final long MS_IN_SEC = 1000L;
    private static final int SEC_IN_MIN = 60;
    private static final long MS_IN_MIN = SEC_IN_MIN * MS_IN_SEC;
    private static final int MIN_IN_HOUR = 60;
    private static final long MS_IN_HOUR = MS_IN_MIN * MIN_IN_HOUR;

    private XMLReport() {

    }

    private static String getProcessingTimeAsString(long processTime) {
        long processingTime = processTime;

        Long hours = Long.valueOf(processingTime / MS_IN_HOUR);
        processingTime %= MS_IN_HOUR;

        Long mins = Long.valueOf(processingTime / MS_IN_MIN);
        processingTime %= MS_IN_MIN;

        Long sec = Long.valueOf(processingTime / MS_IN_SEC);
        processingTime %= MS_IN_SEC;

        Long ms = Long.valueOf(processingTime);

        String res;

        try (Formatter formatter = new Formatter()) {
            formatter.format("%02d:", hours);
            formatter.format("%02d:", mins);
            formatter.format("%02d.", sec);
            formatter.format("%03d", ms);
            res = formatter.toString();
        }

        return res;
    }

    /**
     * Creates tree of xml tags for general report
     *
     * @param info
     *            validation info model to be writed
     * @param collection
     *            features collection to be writed
     * @param doc
     *            document used for writing xml in further
     * @param processingTimeInMS
     *            processing time of validation in ms
     * @return root element of the xml structure
     * @throws DatatypeConfigurationException
     *             indicates a serious configurating error
     */
    public static Element makeXMLTree(ValidationInfo info, FeaturesCollection collection, Document doc, long processingTimeInMS) throws DatatypeConfigurationException {

        Element report = doc.createElement("report");

        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        XMLGregorianCalendar now = DatatypeFactory.newInstance().newXMLGregorianCalendar(gregorianCalendar);

        report.setAttribute("creationDateTime", now.toXMLFormat());
        report.setAttribute("processingTime", getProcessingTimeAsString(processingTimeInMS));

        // TODO: Change next two lines to the normal generating of dom tree for documentInfo and processingInfo
        report.appendChild(doc.createElement("documentInfo"));
        report.appendChild(doc.createElement("processingInfo"));

        if (info != null) {
            report.appendChild(XMLValidationReport.makeXMLTree(info, doc));
        }

        if (collection != null) {
            report.appendChild(XMLFeaturesReport.makeXMLTree(collection, doc));
        }

        return report;
    }

    private static Document generateDOMDocument(ValidationInfo info, FeaturesCollection collection, long processingTimeInMS) throws ParserConfigurationException, DatatypeConfigurationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        DocumentBuilder builder = factory.newDocumentBuilder();

        Document doc = builder.newDocument();

        doc.appendChild(makeXMLTree(info, collection, doc, processingTimeInMS));

        return doc;

    }

    private static void transform(Document doc, StreamResult outputTarget) throws TransformerException {
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.METHOD, "xml");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
        transformer.transform(new DOMSource(doc), outputTarget);
    }

    /**
     * Write the resulting report without features into xml formatted report.
     *
     * @param info
     *            validation info model to be writed
     * @param path
     *            the path for output the resulting document. Path have to ends
     *            with file name with extension.
     * @param processingTimeInMS
     *            processing time of validation in ms
     * @throws ParserConfigurationException
     *             if a DocumentBuilder cannot be created which satisfies the
     *             configuration requested.
     * @throws TransformerFactoryConfigurationError
     *             thrown in case of service configuration error or if the
     *             implementation is not available or cannot be instantiated or
     *             when it is not possible to create a Transformer instance.
     * @throws TransformerException
     *             if an unrecoverable error occurs during the course of the
     *             transformation or
     * @throws DatatypeConfigurationException
     *             indicates a serious configurating error
     */
    public static void writeXMLReport(ValidationInfo info, String path, long processingTimeInMS) throws ParserConfigurationException, TransformerFactoryConfigurationError, TransformerException, DatatypeConfigurationException {

        writeXMLReport(info, null, path, processingTimeInMS);
    }

    /**
     * Write the resulting report into xml formatted report.
     *
     * @param info
     *            validation info model to be writed
     * @param collection
     *            features collection to be writed
     * @param path
     *            the path for output the resulting document. Path have to ends
     *            with file name with extension.
     * @param processingTimeInMS
     *            processing time of validation in ms
     * @throws ParserConfigurationException
     *             if a DocumentBuilder cannot be created which satisfies the
     *             configuration requested.
     * @throws TransformerFactoryConfigurationError
     *             thrown in case of service configuration error or if the
     *             implementation is not available or cannot be instantiated or
     *             when it is not possible to create a Transformer instance.
     * @throws TransformerException
     *             if an unrecoverable error occurs during the course of the
     *             transformation or
     * @throws DatatypeConfigurationException
     *             indicates a serious configuration error
     */
    public static void writeXMLReport(ValidationInfo info, FeaturesCollection collection, String path, long processingTimeInMS) throws ParserConfigurationException, TransformerFactoryConfigurationError, TransformerException, DatatypeConfigurationException {

        Document doc = generateDOMDocument(info, collection, processingTimeInMS);

        try (FileOutputStream fos = new FileOutputStream(path)) {
            transform(doc, new StreamResult(fos));
        } catch (IOException excep) {
            LOGGER.debug("Failed to close FileOutputStream fos.", excep);
            // TODO: Review this exception handling
            // Thrown on failure to close fos so do nothing
        }
    }

    /**
     * Write the resulting report into xml formatted report.
     *
     * @param info
     *            validation info model to be writed
     * @param processingTimeInMS
     *            processing time of validation in ms
     * @return {@code String} representation of the resulting xml report
     * @throws ParserConfigurationException
     *             if a DocumentBuilder cannot be created which satisfies the
     *             configuration requested.
     * @throws TransformerFactoryConfigurationError
     *             thrown in case of service configuration error or if the
     *             implementation is not available or cannot be instantiated or
     *             when it is not possible to create a Transformer instance.
     * @throws TransformerException
     *             if an unrecoverable error occurs during the course of the
     *             transformation or
     * @throws DatatypeConfigurationException
     *             indicates a serious configurating error
     */
    public static String getXMLReportAsString(ValidationInfo info, long processingTimeInMS) throws ParserConfigurationException, TransformerFactoryConfigurationError, TransformerException, DatatypeConfigurationException {

        return getXMLReportAsString(info, null, processingTimeInMS);
    }

    /**
     * Write the resulting report into xml formatted report.
     *
     * @param info
     *            validation info model to be writed
     * @param collection
     *            features collection to be writed
     * @param processingTimeInMS
     *            processing time of validation in ms
     * @return {@code String} representation of the resulting xml report
     * @throws ParserConfigurationException
     *             if a DocumentBuilder cannot be created which satisfies the
     *             configuration requested.
     * @throws TransformerFactoryConfigurationError
     *             thrown in case of service configuration error or if the
     *             implementation is not available or cannot be instantiated or
     *             when it is not possible to create a Transformer instance.
     * @throws TransformerException
     *             if an unrecoverable error occurs during the course of the
     *             transformation or
     * @throws DatatypeConfigurationException
     *             indicates a serious configurating error
     */
    public static String getXMLReportAsString(ValidationInfo info, FeaturesCollection collection, long processingTimeInMS) throws ParserConfigurationException, TransformerFactoryConfigurationError, TransformerException, DatatypeConfigurationException {

        Document doc = generateDOMDocument(info, collection, processingTimeInMS);

        StringWriter writer = new StringWriter();
        transform(doc, new StreamResult(writer));
        return writer.getBuffer().toString();
    }

}
