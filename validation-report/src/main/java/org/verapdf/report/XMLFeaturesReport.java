package org.verapdf.report;

import org.apache.log4j.Logger;
import org.verapdf.features.FeaturesObjectTypesEnum;
import org.verapdf.features.tools.ErrorsHelper;
import org.verapdf.features.tools.FeatureTreeNode;
import org.verapdf.features.tools.FeaturesCollection;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;
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

    private static final byte[] utf8MetadataPrefix = {0x3C, 0x3F, 0x78, 0x70, 0x61, 0x63, 0x6B,
            0x65, 0x74, 0x20, 0x62, 0x65, 0x67, 0x69, 0x6E, 0x3D};
    private static final byte[] utf16BEMetadataPrefix = {0x00, 0x3C, 0x00, 0x3F, 0x00, 0x78, 0x00, 0x70, 0x00, 0x61, 0x00,
            0x63, 0x00, 0x6B, 0x00, 0x65, 0x00, 0x74, 0x00, 0x20, 0x00, 0x62, 0x00, 0x65, 0x00, 0x67, 0x00, 0x69, 0x00, 0x6E, 0x00, 0x3D};
    private static final byte[] utf16LEMetadataPrefix = {0x3C, 0x00, 0x3F, 0x00, 0x78, 0x00, 0x70, 0x00, 0x61, 0x00,
            0x63, 0x00, 0x6B, 0x00, 0x65, 0x00, 0x74, 0x00, 0x20, 0x00, 0x62, 0x00, 0x65, 0x00, 0x67, 0x00, 0x69, 0x00, 0x6E, 0x00, 0x3D, 0x00};
    private static final byte[] utf32BEMetadataPrefix = {0x00, 0x00, 0x00, 0x3C, 0x00, 0x00, 0x00, 0x3F, 0x00, 0x00, 0x00, 0x78,
            0x00, 0x00, 0x00, 0x70, 0x00, 0x00, 0x00, 0x61, 0x00, 0x00, 0x00, 0x63, 0x00, 0x00, 0x00, 0x6B,
            0x00, 0x00, 0x00, 0x65, 0x00, 0x00, 0x00, 0x74, 0x00, 0x00, 0x00, 0x20, 0x00, 0x00, 0x00, 0x62,
            0x00, 0x00, 0x00, 0x65, 0x00, 0x00, 0x00, 0x67, 0x00, 0x00, 0x00, 0x69, 0x00, 0x00, 0x00, 0x6E, 0x00, 0x00, 0x00, 0x3D};
    private static final byte[] utf32LEMetadataPrefix = {0x3C, 0x00, 0x00, 0x00, 0x3F, 0x00, 0x00, 0x00, 0x78,
            0x00, 0x00, 0x00, 0x70, 0x00, 0x00, 0x00, 0x61, 0x00, 0x00, 0x00, 0x63, 0x00, 0x00, 0x00, 0x6B,
            0x00, 0x00, 0x00, 0x65, 0x00, 0x00, 0x00, 0x74, 0x00, 0x00, 0x00, 0x20, 0x00, 0x00, 0x00, 0x62,
            0x00, 0x00, 0x00, 0x65, 0x00, 0x00, 0x00, 0x67, 0x00, 0x00, 0x00, 0x69, 0x00, 0x00, 0x00, 0x6E, 0x00, 0x00, 0x00, 0x3D, 0x00, 0x00, 0x00};
    private static final byte[] utf8SingleQuote = {0x27};
    private static final byte[] utf8DoubleQuote = {0x22};
    private static final byte[] utf16BESingleQuote = {0x00, 0x27};
    private static final byte[] utf16BEDoubleQuote = {0x00, 0x22};
    private static final byte[] utf16LESingleQuote = {0x27, 0x00};
    private static final byte[] utf16LEDoubleQuote = {0x22, 0x00};
    private static final byte[] utf32BESingleQuote = {0x00, 0x00, 0x00, 0x27};
    private static final byte[] utf32BEDoubleQuote = {0x00, 0x00, 0x00, 0x22};
    private static final byte[] utf32LESingleQuote = {0x27, 0x00, 0x00, 0x00};
    private static final byte[] utf32LEDoubleQuote = {0x22, 0x00, 0x00, 0x00};

    private static final byte[] utf8Begin = {-0x11, -0x45, -0x41};
    private static final byte[] utf16BEBegin = {-0x02, -0x01};
    private static final byte[] utf16LEBegin = {-0x01, -0x02};
    private static final byte[] utf32BEBegin = {0x00, 0x00, -0x02, -0x01};
    private static final byte[] utf32LEBegin = {-0x01, -0x02, 0x00, 0x00};

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
            makeList("failed", collection.getFeatureTreesForType(FeaturesObjectTypesEnum.FAILED_XOBJECT), xobjects, collection, doc);
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
                    root.appendChild(makeNode(rootNode, collection, doc));
                }
            }
        }
    }

    private static void makeList(String listName, List<FeatureTreeNode> list, Element parent, FeaturesCollection collection, Document doc) {

        if (list != null && !list.isEmpty()) {
            Element listElement = doc.createElement(listName);
            for (FeatureTreeNode node : list) {
                if (node != null) {
                    listElement.appendChild(makeNode(node, collection, doc));
                }
            }
            parent.appendChild(listElement);
        }
    }

    private static Element makeNode(FeatureTreeNode node, FeaturesCollection collection, Document doc) {
        if ("metadata".equalsIgnoreCase(node.getName())) {
            return parseMetadata(node, collection, doc);
        } else {
            Element root = doc.createElement(node.getName());
            for (Map.Entry<String, String> attr : node.getAttributes().entrySet()) {
                root.setAttribute(attr.getKey(), replaceInvalidCharacters(attr.getValue()));
            }

            if (node.getValue() != null) {
                root.appendChild(doc.createTextNode(replaceInvalidCharacters(node.getValue().toString())));
            } else if (node.getChildren() != null) {
                for (FeatureTreeNode child : node.getChildren()) {
                    root.appendChild(makeNode(child, collection, doc));
                }
            }
            return root;
        }
    }

    private static Element parseMetadata(FeatureTreeNode metadataNode, FeaturesCollection collection, Document doc) {

        if (metadataNode.getAttributes().get(ErrorsHelper.ERRORID) == null) {
            Element metadata = doc.createElement(metadataNode.getName());
            for (Map.Entry<String, String> attr : metadataNode.getAttributes().entrySet()) {
                metadata.setAttribute(attr.getKey(), replaceInvalidCharacters(attr.getValue()));
            }
            try {
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                factory.setNamespaceAware(true);
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document metadataDocument = builder.parse(new ByteArrayInputStream((byte[]) metadataNode.getValue()));
                Node pack = doc.importNode(metadataDocument.getDocumentElement(), true);
                pack.normalize();
                metadata.appendChild(pack);
            } catch (ParserConfigurationException | SAXException | IOException e) {
                LOGGER.debug("Caught exception and checking XML String.", e);
                metadata.setAttribute(ErrorsHelper.ERRORID, ErrorsHelper.METADATAPARSER_ID);
                ErrorsHelper.addErrorIntoCollection(collection, ErrorsHelper.METADATAPARSER_ID, ErrorsHelper.METADATAPARSER_MESSAGE);
            }

            return metadata;
        }
        return makeNode(metadataNode, collection, doc);

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

    private static String getEncodingWithBegin(byte[] bStream, int beginOffset) {
        if (beginOffset >= 0) {
            byte[] suffix = Arrays.copyOfRange(bStream, beginOffset, bStream.length);
            if (startsWith(suffix, utf32BEBegin)) {
                return "UTF-32BE";
            } else if (startsWith(suffix, utf32LEBegin)) {
                return "UTF-32LE";
            } else if (startsWith(suffix, utf8Begin)) {
                return "UTF-8";
            } else if (startsWith(suffix, utf16BEBegin)) {
                return "UTF-16BE";
            } else if (startsWith(suffix, utf16LEBegin)) {
                return "UTF-16LE";
            }
        }
        return "UTF-8";
    }

    private static String getStringFromMeta(byte[] stream) {
        int beginOffset = -1;
        int packageBeginOffset = 0;
        if (stream != null) {
            for (int i = 0; i < stream.length; ++i) {
                if (stream[i] == 0x3C) {
                    beginOffset = getBegOffsetFromCutted(Arrays.copyOfRange(stream, i, stream.length));
                    if (beginOffset >= 0) {
                        packageBeginOffset = i;
                        beginOffset += i;
                        break;
                    }
                }
            }
            return new String(Arrays.copyOfRange(stream, packageBeginOffset, stream.length), Charset.forName(getEncodingWithBegin(stream, beginOffset)));
        }

        return null;
    }

    private static int getBegOffsetFromCutted(byte[] bStream) {
        int beginOffset = -1;
        if ((bStream.length >= utf32LEMetadataPrefix.length + utf32LEDoubleQuote.length + utf32LEBegin.length) &&
                startsWith(bStream, utf32LEMetadataPrefix)) {
            byte[] quote = Arrays.copyOfRange(bStream, utf32LEMetadataPrefix.length, utf32LEMetadataPrefix.length + utf32LEDoubleQuote.length);
            if (startsWith(quote, utf32LESingleQuote) || startsWith(quote, utf32LEDoubleQuote)) {
                beginOffset = utf32LEMetadataPrefix.length + utf32LEDoubleQuote.length;
            }
        } else if ((bStream.length >= utf32BEMetadataPrefix.length + utf32BEDoubleQuote.length + utf32BEBegin.length) &&
                startsWith(bStream, utf32BEMetadataPrefix)) {
            byte[] quote = Arrays.copyOfRange(bStream, utf32BEMetadataPrefix.length, utf32BEMetadataPrefix.length + utf32BEDoubleQuote.length);
            if (startsWith(quote, utf32BESingleQuote) || startsWith(quote, utf32BEDoubleQuote)) {
                beginOffset = utf32BEMetadataPrefix.length + utf32BEDoubleQuote.length;
            }
        } else if ((bStream.length >= utf16LEMetadataPrefix.length + utf16LEDoubleQuote.length + utf16LEBegin.length) &&
                startsWith(bStream, utf16LEMetadataPrefix)) {
            byte[] quote = Arrays.copyOfRange(bStream, utf16LEMetadataPrefix.length, utf16LEMetadataPrefix.length + utf16LEDoubleQuote.length);
            if (startsWith(quote, utf16LESingleQuote) || startsWith(quote, utf16LEDoubleQuote)) {
                beginOffset = utf16LEMetadataPrefix.length + utf16LEDoubleQuote.length;
            }
        } else if ((bStream.length >= utf16BEMetadataPrefix.length + utf16BEDoubleQuote.length + utf16BEBegin.length) &&
                startsWith(bStream, utf16BEMetadataPrefix)) {
            byte[] quote = Arrays.copyOfRange(bStream, utf16BEMetadataPrefix.length, utf16BEMetadataPrefix.length + utf16BEDoubleQuote.length);
            if (startsWith(quote, utf16BESingleQuote) || startsWith(quote, utf16BEDoubleQuote)) {
                beginOffset = utf16BEMetadataPrefix.length + utf16BEDoubleQuote.length;
            }
        } else if ((bStream.length >= utf8MetadataPrefix.length + utf8DoubleQuote.length + utf8Begin.length) &&
                startsWith(bStream, utf8MetadataPrefix)) {
            byte[] quote = Arrays.copyOfRange(bStream, utf8MetadataPrefix.length, utf8MetadataPrefix.length + utf8DoubleQuote.length);
            if (startsWith(quote, utf8SingleQuote) || startsWith(quote, utf8DoubleQuote)) {
                beginOffset = utf8MetadataPrefix.length + utf8DoubleQuote.length;
            }
        }
        return beginOffset;
    }

    private static boolean startsWith(byte[] source, byte[] match) {
        if (match.length > source.length) {
            return false;
        }
        for (int i = 0; i < match.length; i++) {
            if (source[i] != match[i]) {
                return false;
            }
        }
        return true;
    }
}
