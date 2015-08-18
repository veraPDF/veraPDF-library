package org.verapdf.report;

import org.apache.log4j.Logger;
import org.verapdf.features.FeaturesObjectTypesEnum;
import org.verapdf.features.tools.ErrorsHelper;
import org.verapdf.features.tools.FeatureTreeNode;
import org.verapdf.features.tools.FeaturesCollection;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;
import java.util.Formatter;
import java.util.List;
import java.util.Map;

/**
 * Generating XML structure of file for features report
 *
 * @author Maksim Bezrukov
 */
public final class XMLFeaturesReport {
    private static final Logger LOGGER = Logger.getLogger(XMLFeaturesReport.class);

    private static final int XD7FF = 0xD7FF;
    private static final int XE000 = 0xE000;
    private static final int XFFFD = 0xFFFD;
    private static final int X10000 = 0x10000;
    private static final int X10FFFF = 0x10FFFF;
    private static final int SP = 0x20;
    private static final int HT = 0x9;
    private static final int LF = 0xA;
    private static final int CR = 0xD;

    private XMLFeaturesReport() {

    }

    /**
     * Creates tree of xml tags for features report
     *
     * @param collection - features collection to be written
     * @param doc        - document used for writing xml in further
     * @return root element of the xml structure
     */
    public static Element makeXMLTree(FeaturesCollection collection, Document doc) {

        Element pdfFeatures = doc.createElement("pdfFeatures");

        if (collection != null) {

            parseElements(FeaturesObjectTypesEnum.INFORMATION_DICTIONARY, collection, pdfFeatures, doc);

            if (collection.getFeatureTreesForType(FeaturesObjectTypesEnum.METADATA) != null) {
                for (FeatureTreeNode metadataNode : collection.getFeatureTreesForType(FeaturesObjectTypesEnum.METADATA)) {
                    if (metadataNode != null) {
                        pdfFeatures.appendChild(parseMetadata(metadataNode, collection, doc));
                    }
                }
            }

            parseElements(FeaturesObjectTypesEnum.DOCUMENT_SECURITY, collection, pdfFeatures, doc);

            parseElements(FeaturesObjectTypesEnum.LOW_LEVEL_INFO, collection, pdfFeatures, doc);

            makeList("embeddedFiles", collection.getFeatureTreesForType(FeaturesObjectTypesEnum.EMBEDDED_FILE), pdfFeatures, collection, doc);

            makeList("iccProfiles", collection.getFeatureTreesForType(FeaturesObjectTypesEnum.ICCPROFILE), pdfFeatures, collection, doc);

            makeList("outputIntents", collection.getFeatureTreesForType(FeaturesObjectTypesEnum.OUTPUTINTENT), pdfFeatures, collection, doc);

            parseElements(FeaturesObjectTypesEnum.OUTLINES, collection, pdfFeatures, doc);

            makeList("annotations", collection.getFeatureTreesForType(FeaturesObjectTypesEnum.ANNOTATION), pdfFeatures, collection, doc);

            makeList("pages", collection.getFeatureTreesForType(FeaturesObjectTypesEnum.PAGE), pdfFeatures, collection, doc);

            Element resources = doc.createElement("resources");
            makeList("graphicsStates", collection.getFeatureTreesForType(FeaturesObjectTypesEnum.EXT_G_STATE), resources, collection, doc);
            makeList("colorSpaces", collection.getFeatureTreesForType(FeaturesObjectTypesEnum.COLORSPACE), resources, collection, doc);
            makeList("patterns", collection.getFeatureTreesForType(FeaturesObjectTypesEnum.PATTERN), resources, collection, doc);
            makeList("shadings", collection.getFeatureTreesForType(FeaturesObjectTypesEnum.SHADING), resources, collection, doc);
            Element xobjects = doc.createElement("xobjects");
            makeList("images", collection.getFeatureTreesForType(FeaturesObjectTypesEnum.IMAGE_XOBJECT), xobjects, collection, doc);
            makeList("forms", collection.getFeatureTreesForType(FeaturesObjectTypesEnum.FORM_XOBJECT), xobjects, collection, doc);
            if (xobjects.getChildNodes().getLength() > 0) {
                resources.appendChild(xobjects);
            }
            makeList("fonts", collection.getFeatureTreesForType(FeaturesObjectTypesEnum.FONT), resources, collection, doc);
            makeList("procSets", collection.getFeatureTreesForType(FeaturesObjectTypesEnum.PROCSET), resources, collection, doc);
            makeList("propertiesDicts", collection.getFeatureTreesForType(FeaturesObjectTypesEnum.PROPERTIES), resources, collection, doc);

            if (resources.getChildNodes().getLength() > 0) {
                pdfFeatures.appendChild(resources);
            }

            makeList("errors", collection.getFeatureTreesForType(FeaturesObjectTypesEnum.ERROR), pdfFeatures, collection, doc);
        }

        return pdfFeatures;
    }

    private static void parseElements(FeaturesObjectTypesEnum type, FeaturesCollection collection, Element root, Document doc) {
        if (collection.getFeatureTreesForType(type) != null) {
            for (FeatureTreeNode rootNode : collection.getFeatureTreesForType(type)) {
                if (rootNode != null) {
                    root.appendChild(makeNode(rootNode, doc));
                }
            }
        }
    }

    private static Element parseMetadata(FeatureTreeNode metadataNode, FeaturesCollection collection, Document doc) {

        if (metadataNode.getAttributes() == null || metadataNode.getAttributes().get(ErrorsHelper.ERRORID) == null) {
            Element metadata = doc.createElement("metadata");
            try {
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                factory.setNamespaceAware(true);
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document metadataDocument = builder.parse(new InputSource(new StringReader(metadataNode.getValue())));
                Node pack = doc.importNode(metadataDocument.getDocumentElement(), true);
                pack.normalize();
                metadata.appendChild(pack);
            } catch (ParserConfigurationException | SAXException | IOException e) {
                LOGGER.debug("Caught exception and checking XML String.", e);
                metadata.appendChild(doc.createTextNode(replaceInvalidCharacters(metadataNode.getValue())));
                metadata.setAttribute(ErrorsHelper.ERRORID, ErrorsHelper.METADATAPARSER_ID);
                ErrorsHelper.addErrorIntoCollection(collection, ErrorsHelper.METADATAPARSER_ID, ErrorsHelper.METADATAPARSER_MESSAGE);
            }

            return metadata;
        }
        return makeNode(metadataNode, doc);

    }

    private static void makeList(String listName, List<FeatureTreeNode> list, Element parent, FeaturesCollection collection, Document doc) {

        if (list != null && !list.isEmpty()) {
            Element listElement = doc.createElement(listName);
            for (FeatureTreeNode node : list) {
                if (node != null) {
                    listElement.appendChild(makeNode(node, doc));
                }
            }
            parent.appendChild(listElement);
        }
    }

    private static Element makeNode(FeatureTreeNode node, Document doc) {
        Element root = doc.createElement(node.getName());

        for (Map.Entry<String, String> attr : node.getAttributes().entrySet()) {
            root.setAttribute(attr.getKey(), replaceInvalidCharacters(attr.getValue()));
        }

        if (node.getValue() != null) {
            root.appendChild(doc.createTextNode(replaceInvalidCharacters(node.getValue())));
        } else if (node.getChildren() != null) {
            for (FeatureTreeNode child : node.getChildren()) {
                root.appendChild(makeNode(child, doc));
            }
        }

        return root;
    }

    private static String replaceInvalidCharacters(String source) {
        try (Formatter formatter = new Formatter()) {

            for (int i = 0; i < source.length(); ++i) {
                char curChar = source.charAt(i);
                if ('#' == curChar) {
                    formatter.format("#x%06X", "#".codePointAt(0));
                } else {
                    int codePoint = source.codePointAt(i);
                    if (Character.isHighSurrogate(curChar)) {
                        ++i;
                    }

                    if (codePoint == HT || codePoint == LF || codePoint == CR ||
                            (codePoint >= SP && codePoint <= XD7FF) ||
                            (codePoint >= XE000 && codePoint <= XFFFD) ||
                            (codePoint >= X10000 && codePoint <= X10FFFF)) {
                        formatter.format("%c", curChar);
                        if (Character.isHighSurrogate(curChar) && i < source.length()) {
                            formatter.format("%c", source.charAt(i));
                        }
                    } else {
                        formatter.format("#x%06X", codePoint);
                    }
                }
            }

            return formatter.toString();
        }
    }
}
