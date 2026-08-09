package org.verapdf.xmp.tools;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SecureXML {

    private static final Logger LOGGER = Logger.getLogger(SecureXML.class.getCanonicalName());

    private static DocumentBuilderFactory factory;

    public static DocumentBuilder newSafeDocumentBuilder() throws ParserConfigurationException {
        if (factory == null) {
            factory = createDocumentBuilderFactory();
        }
        DocumentBuilder builder = factory.newDocumentBuilder();
        builder.setErrorHandler(null);
        return builder;
    }

    /**
     * @return Creates, configures and returns the document builder factory for
     *         the Metadata Parser.
     */
    private static DocumentBuilderFactory createDocumentBuilderFactory() throws ParserConfigurationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            factory.setNamespaceAware(true);
            factory.setIgnoringComments(true);
            factory.setIgnoringElementContentWhitespace(true);
            factory.setExpandEntityReferences(false);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to set basic factory properties");
            throw new ParserConfigurationException("Basic factory configuration failed: " + e.getMessage());
        }
        boolean secureProcessingSet = false;
        try {
            factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            secureProcessingSet = true;
        } catch (ParserConfigurationException e) {
            LOGGER.log(Level.WARNING, "FEATURE_SECURE_PROCESSING is not supported; DoS risk may be higher");
        }
        try {
            factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            secureProcessingSet = true;
        } catch (ParserConfigurationException e) {
            LOGGER.log(Level.WARNING, "disallow-doctype-decl is not supported; relying on external entity disabling");
        }
        if (!secureProcessingSet) {
            LOGGER.log(Level.SEVERE, "FEATURE_SECURE_PROCESSING is disabled and disallow-doctype-decl is unsupported. "
                    + "Parser may be vulnerable to DTD expansion attacks (e.g., billion laughs). ");
            throw new ParserConfigurationException("Insufficient security features available for XML parsing – "
                    + "neither secure-processing nor DOCTYPE disallow is available");
        }
        try {
            factory.setFeature("http://xml.org/sax/features/external-general-entities", false);
            factory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
        } catch (ParserConfigurationException e) {
            try {
                factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
                factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
            } catch (Exception ex) {
                LOGGER.log(Level.SEVERE, "Could not disable external entities by any means. XXE risk remains!");
                throw new ParserConfigurationException("Unable to secure DocumentBuilderFactory against XXE");
            }
        }
        try {
            factory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
        } catch (ParserConfigurationException e) {
            LOGGER.log(Level.WARNING, "load-external-dtd is not supported");
        }
        try {
            factory.setXIncludeAware(false);
        } catch (UnsupportedOperationException e) {
            LOGGER.log(Level.WARNING, "XIncludeAware is not supported");
        }
        return factory;
    }
}
