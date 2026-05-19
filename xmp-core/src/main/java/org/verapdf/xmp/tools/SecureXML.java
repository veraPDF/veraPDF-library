package org.verapdf.xmp.tools;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class SecureXML {

    private static DocumentBuilderFactory factory = createDocumentBuilderFactory();

    public static DocumentBuilder newSafeDocumentBuilder() throws ParserConfigurationException {
        DocumentBuilder builder = factory.newDocumentBuilder();
        builder.setErrorHandler(null);
        return builder;
    }

    /**
     * @return Creates, configures and returns the document builder factory for
     *         the Metadata Parser.
     */
    private static DocumentBuilderFactory createDocumentBuilderFactory()
    {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        factory.setIgnoringComments(true);
        try
        {
            // honor System parsing limits, e.g.
            // System.setProperty("entityExpansionLimit", "10");
            factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            factory.setFeature("[http://apache.org/xml/features/disallow-doctype-decl](http://apache.org/xml/features/disallow-doctype-decl)", true);
            factory.setFeature("[http://xml.org/sax/features/external-general-entities](http://xml.org/sax/features/external-general-entities)", false);
            factory.setFeature("[http://xml.org/sax/features/external-parameter-entities](http://xml.org/sax/features/external-parameter-entities)", false);
            factory.setFeature("[http://apache.org/xml/features/nonvalidating/load-external-dtd](http://apache.org/xml/features/nonvalidating/load-external-dtd)", false);
            factory.setXIncludeAware(false);
            factory.setExpandEntityReferences(false);
            factory.setIgnoringElementContentWhitespace(true);
        }
        catch (Exception e)
        {
            // Ignore IllegalArgumentException and ParserConfigurationException
            // in case the configured XML-Parser does not implement the feature.
        }
        return factory;
    }
}
