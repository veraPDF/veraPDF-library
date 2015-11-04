package org.verapdf.report;

import java.io.OutputStream;
import java.util.GregorianCalendar;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.verapdf.features.tools.FeaturesCollection;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Generating XML structure of file for general report
 *
 * @author Maksim Bezrukov
 */
public final class XMLReport {
    private XMLReport() {

    }

    /**
     * Creates tree of xml tags for general report
     *
     * @param collection
     *            features collection to be writed
     * @param doc
     *            document used for writing xml in further
     * @return root element of the xml structure
     * @throws DatatypeConfigurationException
     *             indicates a serious configurating error
     */
    public static Element makeXMLTree(FeaturesCollection collection,
            Document doc) throws DatatypeConfigurationException {

        Element report = doc.createElement("report");
        report.setAttribute("xmlns",
                "http://www.verapdf.org/MachineReadableReport");

        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        XMLGregorianCalendar now = DatatypeFactory.newInstance()
                .newXMLGregorianCalendar(gregorianCalendar);

        report.setAttribute("creationDateTime", now.toXMLFormat());

        if (collection != null) {
            report.appendChild(XMLFeaturesReport.makeXMLTree(collection, doc));
        }

        return report;
    }

    private static Document generateDOMDocument(FeaturesCollection collection)
            throws ParserConfigurationException, DatatypeConfigurationException {

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        DocumentBuilder builder = factory.newDocumentBuilder();

        Document doc = builder.newDocument();

        doc.appendChild(makeXMLTree(collection, doc));

        return doc;

    }

    private static void transform(Document doc, StreamResult outputTarget)
            throws TransformerException {
        Transformer transformer = TransformerFactory.newInstance()
                .newTransformer();
        transformer.setOutputProperty(OutputKeys.METHOD, "xml");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.setOutputProperty(
                "{http://xml.apache.org/xslt}indent-amount", "4");
        transformer.transform(new DOMSource(doc), outputTarget);
    }

    /**
     * Write the resulting report into xml formatted report.
     *
     * @param collection
     *            features collection to be writed with file name with
     *            extension.
     * @param destination
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
    public static void writeXMLReport(FeaturesCollection collection,
            OutputStream destination) throws ParserConfigurationException,
            TransformerFactoryConfigurationError, TransformerException,
            DatatypeConfigurationException {

        Document doc = generateDOMDocument(collection);

        transform(doc, new StreamResult(destination));
    }

}
