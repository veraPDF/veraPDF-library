package org.verapdf.report;

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
import java.util.List;
import java.util.Map;

/**
 * Generating XML structure of file for features report
 *
 * @author Maksim Bezrukov
 */
public final class XMLFeaturesReport {

    private static final char FFFE = (char) 65534;
    private static final char FFFF = (char) 65535;
    private static final char SP = (char) 32;
    private static final char HT = (char) 9;
    private static final char LF = (char) 10;
    private static final char CR = (char) 13;

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

                if (isValidXMLString(metadataNode.getValue())) {
                    metadata.appendChild(doc.createTextNode(metadataNode.getValue()));
                    metadata.setAttribute(ErrorsHelper.ERRORID, ErrorsHelper.METADATAPARSER_ID);
                    ErrorsHelper.addErrorIntoCollection(collection, ErrorsHelper.METADATAPARSER_ID, ErrorsHelper.METADATAPARSER_MESSAGE);
                } else {
                    addInvalidCharactersError(metadata, collection);
                }
            }

            return metadata;
        } else {
            return makeNode(metadataNode, collection, doc);
        }

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
            if (isValidXMLString(attr.getValue())) {
                root.setAttribute(attr.getKey(), attr.getValue());
            } else {
                addInvalidCharactersError(root, collection);
            }
        }

        if (node.getValue() != null) {
            if (isValidXMLString(node.getValue())) {
                root.appendChild(doc.createTextNode(node.getValue()));
            } else {
                addInvalidCharactersError(root, collection);
            }
        } else if (node.getChildren() != null) {
            for (FeatureTreeNode child : node.getChildren()) {
                root.appendChild(makeNode(child, collection, doc));
            }
        }

        return root;
    }

    private static boolean isValidXMLString(String str) {
        boolean res = true;

        for (char c : str.toCharArray()) {
            if ((c == FFFE) || (c == FFFF) || ((c < SP) && (c != HT && c != LF && c != CR))) {
                res = false;
                break;
            }
        }

        return res;
    }

    private static void addInvalidCharactersError(Element element, FeaturesCollection collection) {
        element.setAttribute(ErrorsHelper.ERRORID, ErrorsHelper.XMLINVALIDCHARACTERS_ID);
        ErrorsHelper.addErrorIntoCollection(collection, ErrorsHelper.XMLINVALIDCHARACTERS_ID,
                ErrorsHelper.XMLINVALIDCHARACTERS_MESSAGE);
    }
}
