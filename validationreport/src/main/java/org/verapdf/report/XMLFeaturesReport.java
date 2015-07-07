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
public class XMLFeaturesReport {

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

            if (collection.getFeatureTreesForType(FeaturesObjectTypesEnum.INFORMATION_DICTIONARY) != null) {
                for (FeatureTreeNode infoDict : collection.getFeatureTreesForType(FeaturesObjectTypesEnum.INFORMATION_DICTIONARY)) {
                    if (infoDict != null) {
                        pdfFeatures.appendChild(makeNode(infoDict, doc));
                    }
                }
            }

            if (collection.getFeatureTreesForType(FeaturesObjectTypesEnum.METADATA) != null) {
                for (FeatureTreeNode metadataNode : collection.getFeatureTreesForType(FeaturesObjectTypesEnum.METADATA)) {
                    if (metadataNode != null) {
                        pdfFeatures.appendChild(parseMetadata(metadataNode, collection, doc));
                    }
                }
            }

            if (collection.getFeatureTreesForType(FeaturesObjectTypesEnum.OUTPUTINTENT) != null) {
                pdfFeatures.appendChild(makeList("outputIntents", collection.getFeatureTreesForType(FeaturesObjectTypesEnum.OUTPUTINTENT), doc));
            }

            if (collection.getFeatureTreesForType(FeaturesObjectTypesEnum.OUTLINES) != null) {
                for (FeatureTreeNode outline : collection.getFeatureTreesForType(FeaturesObjectTypesEnum.OUTLINES)) {
                    if (outline != null) {
                        pdfFeatures.appendChild(makeNode(outline, doc));
                    }
                }
            }

            if (collection.getFeatureTreesForType(FeaturesObjectTypesEnum.PAGE) != null) {
                pdfFeatures.appendChild(makeList("pages", collection.getFeatureTreesForType(FeaturesObjectTypesEnum.PAGE), doc));
            }

            if (collection.getFeatureTreesForType(FeaturesObjectTypesEnum.ERROR) != null) {
                pdfFeatures.appendChild(makeList("errors", collection.getFeatureTreesForType(FeaturesObjectTypesEnum.ERROR), doc));
            }
        }

        return pdfFeatures;
    }

    private static Element parseMetadata(FeatureTreeNode metadataNode, FeaturesCollection collection, Document doc) {

        if (metadataNode.getAttributes().get(ErrorsHelper.ERRORID) == null) {
            Element metadata = doc.createElement("metadata");
            try {
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document metadataDocument = builder.parse(new InputSource(new StringReader(metadataNode.getValue())));
                Node pack = doc.importNode(metadataDocument.getDocumentElement(), true);
                pack.normalize();
                metadata.appendChild(pack);
            } catch (ParserConfigurationException | SAXException | IOException e) {
                metadata.appendChild(doc.createTextNode(metadataNode.getValue()));
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
            root.setAttribute(attr.getKey(), attr.getValue());
        }

        if (node.isLeaf() && node.getValue() != null) {
            root.appendChild(doc.createTextNode(node.getValue()));
        } else if (node.getChildren() != null) {
            for (FeatureTreeNode child : node.getChildren()) {
                root.appendChild(makeNode(child, doc));
            }
        }

        return root;
    }
}
