package org.verapdf.report;

import org.verapdf.exceptions.featurereport.FeatureValueException;
import org.verapdf.features.FeaturesObjectTypesEnum;
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
     * @throws FeatureValueException - occurs when there is errors in parsing metadata
     */
    public static Element makeXMLTree(FeaturesCollection collection, Document doc) throws FeatureValueException {

        Element pdfFeatures = doc.createElement("pdfFeatures");

        if (collection != null) {

            for (FeatureTreeNode infoDict : collection.getFeatureTreesForType(FeaturesObjectTypesEnum.INFORMATION_DICTIONARY)) {
                if (infoDict != null) {
                    pdfFeatures.appendChild(makeNode(infoDict, doc));
                }
            }

            for (FeatureTreeNode metadataNode : collection.getFeatureTreesForType(FeaturesObjectTypesEnum.METADATA)) {
                if (metadataNode != null) {
                    pdfFeatures.appendChild(parseMetadata(metadataNode, doc));
                }
            }

            pdfFeatures.appendChild(makePages(collection.getFeatureTreesForType(FeaturesObjectTypesEnum.PAGE), doc));
        }

        return pdfFeatures;
    }

    private static Element parseMetadata(FeatureTreeNode metadataNode, Document doc) throws FeatureValueException {
        Element metadata = doc.createElement("metadata");

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document metadataDocument = builder.parse(new InputSource(new StringReader(metadataNode.getValue())));
            Node pack = doc.importNode(metadataDocument.getDocumentElement(), true);
            pack.normalize();
            metadata.appendChild(pack);
        } catch (ParserConfigurationException e) {
            throw new FeatureValueException("A DocumentBuilder cannot be created for parse metadata which satisfies the configuration requested.", e);
        } catch (SAXException e) {
            throw new FeatureValueException("Error occurs in metadata parsing.", e);
        } catch (IOException e) {
            throw new FeatureValueException("IO error occurs in metadata parsing.", e);
        }
        return metadata;
    }

    private static Element makePages(List<FeatureTreeNode> list, Document doc) {
        Element pages = doc.createElement("pages");

        if (list != null) {
            for (FeatureTreeNode node : list) {
                if (node != null) {
                    pages.appendChild(makeNode(node, doc));
                }
            }
        }

        return pages;
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
