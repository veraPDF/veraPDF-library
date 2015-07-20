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

    private XMLFeaturesReport() {

    }

    /**
     * Creates tree of xml tags for features report
     *
     * @param collection - features collection to be written
     * @param doc  - document used for writing xml in further
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

            parseElements(FeaturesObjectTypesEnum.LOW_LEVEL_INFO, collection, pdfFeatures, doc);

            if (collection.getFeatureTreesForType(FeaturesObjectTypesEnum.OUTPUTINTENT) != null) {
                pdfFeatures.appendChild(makeList("outputIntents", collection.getFeatureTreesForType(FeaturesObjectTypesEnum.OUTPUTINTENT), doc));
            }

            parseElements(FeaturesObjectTypesEnum.OUTLINES, collection, pdfFeatures, doc);

            if (collection.getFeatureTreesForType(FeaturesObjectTypesEnum.PAGE) != null) {
                pdfFeatures.appendChild(makeList("pages", collection.getFeatureTreesForType(FeaturesObjectTypesEnum.PAGE), doc));
            }

            if (collection.getFeatureTreesForType(FeaturesObjectTypesEnum.ERROR) != null) {
                pdfFeatures.appendChild(makeList("errors", collection.getFeatureTreesForType(FeaturesObjectTypesEnum.ERROR), doc));
            }
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
                metadata.appendChild(doc.createTextNode(removeZeros(metadataNode.getValue())));
                metadata.setAttribute(ErrorsHelper.ERRORID, ErrorsHelper.METADATAPARSER_ID);

                ErrorsHelper.addErrorIntoCollection(collection, ErrorsHelper.METADATAPARSER_ID, ErrorsHelper.METADATAPARSER_MESSAGE);
            }

            return metadata;
        } else {
            return makeNode(metadataNode, doc);
        }

    }

    private static Element makeList(String listName, List<FeatureTreeNode> list, Document doc) {
        Element listElement = doc.createElement(listName);

        if (list != null) {
            for (FeatureTreeNode node : list) {
                if (node != null) {
                    listElement.appendChild(makeNode(node, doc));
                }
            }
        }

        return listElement;
    }

    private static Element makeNode(FeatureTreeNode node, Document doc) {
        Element root = doc.createElement(node.getName());

        for (Map.Entry<String, String> attr : node.getAttributes().entrySet()) {
            root.setAttribute(attr.getKey(), removeZeros(attr.getValue()));
        }

        if (node.isLeaf() && node.getValue() != null) {
            root.appendChild(doc.createTextNode(removeZeros(node.getValue())));
        } else if (node.getChildren() != null) {
            for (FeatureTreeNode child : node.getChildren()) {
                root.appendChild(makeNode(child, doc));
            }
        }

        return root;
    }

    private static String removeZeros(String str) {
        return str.replaceAll(String.valueOf((char) 0), "");
    }
}
