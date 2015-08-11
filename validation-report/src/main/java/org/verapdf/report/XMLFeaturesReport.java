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

            pdfFeatures.appendChild(makeList("embeddedFiles", collection.getFeatureTreesForType(FeaturesObjectTypesEnum.EMBEDDED_FILE), collection, doc));

            pdfFeatures.appendChild(makeList("iccProfiles", collection.getFeatureTreesForType(FeaturesObjectTypesEnum.ICCPROFILE), collection, doc));

            pdfFeatures.appendChild(makeList("outputIntents", collection.getFeatureTreesForType(FeaturesObjectTypesEnum.OUTPUTINTENT), collection, doc));

            parseElements(FeaturesObjectTypesEnum.OUTLINES, collection, pdfFeatures, doc);

            pdfFeatures.appendChild(makeList("annotations", collection.getFeatureTreesForType(FeaturesObjectTypesEnum.ANNOTATION), collection, doc));

            pdfFeatures.appendChild(makeList("pages", collection.getFeatureTreesForType(FeaturesObjectTypesEnum.PAGE), collection, doc));

            pdfFeatures.appendChild(makeList("errors", collection.getFeatureTreesForType(FeaturesObjectTypesEnum.ERROR), collection, doc));
        }

        return pdfFeatures;
    }

    private static void parseElements(FeaturesObjectTypesEnum type, FeaturesCollection collection, Element root, Document doc) {
        if (collection.getFeatureTreesForType(type) != null) {
            for (FeatureTreeNode rootNode : collection.getFeatureTreesForType(type)) {
                if (rootNode != null) {
                    root.appendChild(makeNode(rootNode, collection, doc));
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
        return makeNode(metadataNode, collection, doc);

    }

    private static Element makeList(String listName, List<FeatureTreeNode> list, FeaturesCollection collection, Document doc) {
        Element listElement = doc.createElement(listName);

        if (list != null) {
            for (FeatureTreeNode node : list) {
                if (node != null) {
                    listElement.appendChild(makeNode(node, collection, doc));
                }
            }
        }

        return listElement;
    }

    private static Element makeNode(FeatureTreeNode node, FeaturesCollection collection, Document doc) {
        Element root = doc.createElement(node.getName());

        for (Map.Entry<String, String> attr : node.getAttributes().entrySet()) {
            root.setAttribute(attr.getKey(), replaceInvalidCharacters(attr.getValue()));
        }

        if (node.getValue() != null) {
            root.appendChild(doc.createTextNode(replaceInvalidCharacters(node.getValue())));
        } else if (node.getChildren() != null) {
            for (FeatureTreeNode child : node.getChildren()) {
                root.appendChild(makeNode(child, collection, doc));
            }
        }

        return root;
    }

    private static String replaceInvalidCharacters(String source) {
        try (Formatter formatter = new Formatter()) {

            StringBuilder builder = new StringBuilder();

            for (int i = 0; i < source.length(); ++i) {
                char curChar = source.charAt(i);
                if ('#' == curChar) {
                    builder.append("#x000023");
                } else {
                    int codePoint = source.codePointAt(i);
                    if (Character.isHighSurrogate(curChar)) {
                        ++i;
                    }

                    if (codePoint == HT || codePoint == LF || codePoint == CR ||
                            (codePoint >= SP && codePoint <= XD7FF) ||
                            (codePoint >= XE000 && codePoint <= XFFFD) ||
                            (codePoint >= X10000 && codePoint <= X10FFFF)) {
                        builder.append(curChar);
                        if (Character.isHighSurrogate(curChar) && i < source.length()) {
                            builder.append(source.charAt(i));
                        }
                    } else {
                        builder.append(formatter.format("#x%06X", codePoint));
                    }
                }
            }

            return builder.toString();
        }
    }
}
