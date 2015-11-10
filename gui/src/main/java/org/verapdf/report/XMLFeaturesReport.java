package org.verapdf.report;

import org.apache.log4j.Logger;
import org.verapdf.features.FeaturesObjectTypesEnum;
import org.verapdf.features.FeaturesReporter;
import org.verapdf.features.tools.ErrorsHelper;
import org.verapdf.features.tools.FeatureTreeNode;
import org.verapdf.features.tools.FeaturesCollection;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.bind.DatatypeConverter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
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

	private static final byte[] UTF8_METADATA_PREFIX_SQ = {0x3C, 0x3F, 0x78, 0x70, 0x61, 0x63, 0x6B,
			0x65, 0x74, 0x20, 0x62, 0x65, 0x67, 0x69, 0x6E, 0x3D, 0x27, -0x11, -0x45, -0x41, 0x27};
	private static final byte[] UTF8_METADATA_PREFIX_DQ = {0x3C, 0x3F, 0x78, 0x70, 0x61, 0x63, 0x6B,
			0x65, 0x74, 0x20, 0x62, 0x65, 0x67, 0x69, 0x6E, 0x3D, 0x22, -0x11, -0x45, -0x41, 0x22};
	private static final byte[] UTF8_METADATA_PREFIX_SQ_EMPTY = {0x3C, 0x3F, 0x78, 0x70, 0x61, 0x63, 0x6B,
			0x65, 0x74, 0x20, 0x62, 0x65, 0x67, 0x69, 0x6E, 0x3D, 0x27, 0x27};
	private static final byte[] UTF8_METADATA_PREFIX_DQ_EMPTY = {0x3C, 0x3F, 0x78, 0x70, 0x61, 0x63, 0x6B,
			0x65, 0x74, 0x20, 0x62, 0x65, 0x67, 0x69, 0x6E, 0x3D, 0x22, 0x22};
	private static final byte[] UTF16BE_METADATA_PREFIX_SQ = {0x00, 0x3C, 0x00, 0x3F, 0x00, 0x78, 0x00, 0x70, 0x00, 0x61, 0x00,
			0x63, 0x00, 0x6B, 0x00, 0x65, 0x00, 0x74, 0x00, 0x20, 0x00, 0x62, 0x00, 0x65, 0x00, 0x67, 0x00, 0x69, 0x00, 0x6E, 0x00, 0x3D,
			0x00, 0x27, -0x02, -0x01, 0x00, 0x27};
	private static final byte[] UTF16BE_METADATA_PREFIX_DQ = {0x00, 0x3C, 0x00, 0x3F, 0x00, 0x78, 0x00, 0x70, 0x00, 0x61, 0x00,
			0x63, 0x00, 0x6B, 0x00, 0x65, 0x00, 0x74, 0x00, 0x20, 0x00, 0x62, 0x00, 0x65, 0x00, 0x67, 0x00, 0x69, 0x00, 0x6E, 0x00, 0x3D,
			0x00, 0x22, -0x02, -0x01, 0x00, 0x22};
	private static final byte[] UTF16LE_METADATA_PREFIX_SQ = {0x3C, 0x00, 0x3F, 0x00, 0x78, 0x00, 0x70, 0x00, 0x61, 0x00,
			0x63, 0x00, 0x6B, 0x00, 0x65, 0x00, 0x74, 0x00, 0x20, 0x00, 0x62, 0x00, 0x65, 0x00, 0x67, 0x00, 0x69, 0x00, 0x6E, 0x00, 0x3D, 0x00,
			0x27, 0x00, -0x01, -0x02, 0x27, 0x00};
	private static final byte[] UTF16LE_METADATA_PREFIX_DQ = {0x3C, 0x00, 0x3F, 0x00, 0x78, 0x00, 0x70, 0x00, 0x61, 0x00,
			0x63, 0x00, 0x6B, 0x00, 0x65, 0x00, 0x74, 0x00, 0x20, 0x00, 0x62, 0x00, 0x65, 0x00, 0x67, 0x00, 0x69, 0x00, 0x6E, 0x00, 0x3D, 0x00,
			0x22, 0x00, -0x01, -0x02, 0x22, 0x00};
	private static final byte[] UTF32BE_METADATA_PREFIX_SQ = {0x00, 0x00, 0x00, 0x3C, 0x00, 0x00, 0x00, 0x3F, 0x00, 0x00, 0x00, 0x78,
			0x00, 0x00, 0x00, 0x70, 0x00, 0x00, 0x00, 0x61, 0x00, 0x00, 0x00, 0x63, 0x00, 0x00, 0x00, 0x6B,
			0x00, 0x00, 0x00, 0x65, 0x00, 0x00, 0x00, 0x74, 0x00, 0x00, 0x00, 0x20, 0x00, 0x00, 0x00, 0x62,
			0x00, 0x00, 0x00, 0x65, 0x00, 0x00, 0x00, 0x67, 0x00, 0x00, 0x00, 0x69, 0x00, 0x00, 0x00, 0x6E, 0x00, 0x00, 0x00, 0x3D,
			0x00, 0x00, 0x00, 0x27, 0x00, 0x00, -0x02, -0x01, 0x00, 0x00, 0x00, 0x27};
	private static final byte[] UTF32BE_METADATA_PREFIX_DQ = {0x00, 0x00, 0x00, 0x3C, 0x00, 0x00, 0x00, 0x3F, 0x00, 0x00, 0x00, 0x78,
			0x00, 0x00, 0x00, 0x70, 0x00, 0x00, 0x00, 0x61, 0x00, 0x00, 0x00, 0x63, 0x00, 0x00, 0x00, 0x6B,
			0x00, 0x00, 0x00, 0x65, 0x00, 0x00, 0x00, 0x74, 0x00, 0x00, 0x00, 0x20, 0x00, 0x00, 0x00, 0x62,
			0x00, 0x00, 0x00, 0x65, 0x00, 0x00, 0x00, 0x67, 0x00, 0x00, 0x00, 0x69, 0x00, 0x00, 0x00, 0x6E, 0x00, 0x00, 0x00, 0x3D,
			0x00, 0x00, 0x00, 0x22, 0x00, 0x00, -0x02, -0x01, 0x00, 0x00, 0x00, 0x22};
	private static final byte[] UTF32LE_METADATA_PREFIX_SQ = {0x3C, 0x00, 0x00, 0x00, 0x3F, 0x00, 0x00, 0x00, 0x78,
			0x00, 0x00, 0x00, 0x70, 0x00, 0x00, 0x00, 0x61, 0x00, 0x00, 0x00, 0x63, 0x00, 0x00, 0x00, 0x6B,
			0x00, 0x00, 0x00, 0x65, 0x00, 0x00, 0x00, 0x74, 0x00, 0x00, 0x00, 0x20, 0x00, 0x00, 0x00, 0x62,
			0x00, 0x00, 0x00, 0x65, 0x00, 0x00, 0x00, 0x67, 0x00, 0x00, 0x00, 0x69, 0x00, 0x00, 0x00, 0x6E, 0x00, 0x00, 0x00, 0x3D, 0x00, 0x00, 0x00,
			0x27, 0x00, 0x00, 0x00, -0x01, -0x02, 0x00, 0x00, 0x27, 0x00, 0x00, 0x00};
	private static final byte[] UTF32LE_METADATA_PREFIX_DQ = {0x3C, 0x00, 0x00, 0x00, 0x3F, 0x00, 0x00, 0x00, 0x78,
			0x00, 0x00, 0x00, 0x70, 0x00, 0x00, 0x00, 0x61, 0x00, 0x00, 0x00, 0x63, 0x00, 0x00, 0x00, 0x6B,
			0x00, 0x00, 0x00, 0x65, 0x00, 0x00, 0x00, 0x74, 0x00, 0x00, 0x00, 0x20, 0x00, 0x00, 0x00, 0x62,
			0x00, 0x00, 0x00, 0x65, 0x00, 0x00, 0x00, 0x67, 0x00, 0x00, 0x00, 0x69, 0x00, 0x00, 0x00, 0x6E, 0x00, 0x00, 0x00, 0x3D, 0x00, 0x00, 0x00,
			0x22, 0x00, 0x00, 0x00, -0x01, -0x02, 0x00, 0x00, 0x22, 0x00, 0x00, 0x00};

	private XMLFeaturesReport() {

	}

	/**
	 * Creates tree of xml tags for features report
	 *
	 * @param collection features collection to be written
	 * @param doc        document used for writing xml in further
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

			makeList("embeddedFiles", FeaturesObjectTypesEnum.EMBEDDED_FILE, pdfFeatures, collection, doc);

			makeList("iccProfiles", FeaturesObjectTypesEnum.ICCPROFILE, pdfFeatures, collection, doc);

			makeList("outputIntents", FeaturesObjectTypesEnum.OUTPUTINTENT, pdfFeatures, collection, doc);

			parseElements(FeaturesObjectTypesEnum.OUTLINES, collection, pdfFeatures, doc);

			makeList("annotations", FeaturesObjectTypesEnum.ANNOTATION, pdfFeatures, collection, doc);

			makeList("pages", FeaturesObjectTypesEnum.PAGE, pdfFeatures, collection, doc);

			Element resources = doc.createElement("resources");
			makeList("graphicsStates", FeaturesObjectTypesEnum.EXT_G_STATE, resources, collection, doc);
			makeList("colorSpaces", FeaturesObjectTypesEnum.COLORSPACE, resources, collection, doc);
			makeList("patterns", FeaturesObjectTypesEnum.PATTERN, resources, collection, doc);
			makeList("shadings", FeaturesObjectTypesEnum.SHADING, resources, collection, doc);
			Element xobjects = doc.createElement("xobjects");
			makeList("images", FeaturesObjectTypesEnum.IMAGE_XOBJECT, xobjects, collection, doc);
			makeList("forms", FeaturesObjectTypesEnum.FORM_XOBJECT, xobjects, collection, doc);
			makeList("postscripts", FeaturesObjectTypesEnum.POSTSCRIPT_XOBJECT, xobjects, collection, doc);
			makeList("failed", FeaturesObjectTypesEnum.FAILED_XOBJECT, xobjects, collection, doc);
			if (xobjects.getChildNodes().getLength() > 0) {
				resources.appendChild(xobjects);
			}
			makeList("fonts", FeaturesObjectTypesEnum.FONT, resources, collection, doc);
			makeList("propertiesDicts", FeaturesObjectTypesEnum.PROPERTIES, resources, collection, doc);

			if (resources.getChildNodes().getLength() > 0) {
				pdfFeatures.appendChild(resources);
			}

			makeList("errors", FeaturesObjectTypesEnum.ERROR, pdfFeatures, collection, doc);
		}

		return pdfFeatures;
	}

	/**
	 * Takes a list of FeatureTreeNode elements from the collection of specified type and for each of them creates
	 * a child xml node to the root element. The created child xml node represents obtained feature tree.
	 */
	private static void parseElements(FeaturesObjectTypesEnum type,
									  FeaturesCollection collection,
									  Element root, Document doc) {
		for (FeatureTreeNode rootNode : collection.getFeatureTreesForType(type)) {
			if (rootNode != null) {
				root.appendChild(makeNode(rootNode, collection, doc, false));
			}
		}
	}

	/**
	 *
	 */
	private static void makeList(String listName, FeaturesObjectTypesEnum type,
								 Element parent, FeaturesCollection collection,
								 Document doc) {
		List<FeatureTreeNode> list = collection.getFeatureTreesForType(type);
		List<String> errors = collection.getErrorsForType(type);
		if (!list.isEmpty() || !errors.isEmpty()) {
			Element listElement = doc.createElement(listName);
			for (FeatureTreeNode node : list) {
				if (node != null) {
					listElement.appendChild(makeNode(node, collection, doc, false));
				}
			}
			parent.appendChild(listElement);

			if (!errors.isEmpty()) {
				StringBuilder builder = new StringBuilder();
				builder.append(errors.get(0));
				for (int i = 1; i < errors.size(); ++i) {
					builder.append(", ").append(errors.get(i));
				}
				listElement.setAttribute(ErrorsHelper.ERRORID, builder.toString());
			}
		}
	}

	private static Element makeNode(FeatureTreeNode node,
									FeaturesCollection collection, Document doc, boolean isCustom) {
		if (!isCustom && "metadata".equalsIgnoreCase(node.getName())) {
			return parseMetadata(node, collection, doc);
		} else {
			Element root = doc.createElement(node.getName());
			for (Map.Entry<String, String> attr : node.getAttributes().entrySet()) {
				root.setAttribute(attr.getKey(),
						replaceInvalidCharacters(attr.getValue()));
			}
			if (node.getValue() != null) {
				root.appendChild(doc.createTextNode(
						replaceInvalidCharacters(node.getValue())));
			} else if (node.getChildren() != null) {
				boolean isCustomChildren = isCustom || FeaturesReporter.CUSTOM_FEATURES_ROOT_NODE_NAME.equals(node.getName());
				for (FeatureTreeNode child : node.getChildren()) {
					root.appendChild(makeNode(child, collection, doc, isCustomChildren));
				}
			}
			return root;
		}
	}

	private static Element parseMetadata(FeatureTreeNode metadataNode,
										 FeaturesCollection collection, Document doc) {
		Element metadata = doc.createElement(metadataNode.getName());

		if (metadataNode.getAttributes().get(ErrorsHelper.ERRORID) == null) {
			for (Map.Entry<String, String> attr : metadataNode.getAttributes().entrySet()) {
				metadata.setAttribute(attr.getKey(), replaceInvalidCharacters(attr.getValue()));
			}
			if (metadataNode.getValue() != null) {
				InputSource is = getInputSourceWithEncoding(DatatypeConverter.parseHexBinary(metadataNode.getValue()));
				if (is != null) {
					try {
						DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
						factory.setNamespaceAware(true);
						DocumentBuilder builder = factory.newDocumentBuilder();
						Document metadataDocument = builder.parse(is);
						Node pack = doc.importNode(metadataDocument.getDocumentElement(), true);
						pack.normalize();
						metadata.appendChild(pack);
					} catch (ParserConfigurationException | SAXException | IOException e) {
						LOGGER.debug("Caught exception while parsing metadata byte array.", e);
						parseMetadataError(collection, metadata, e.getMessage());
					}
				} else {
					LOGGER.debug("Metadata stream does not contains valid prefix.");
					parseMetadataError(collection, metadata, "Metadata stream does not contains valid prefix.");
				}
			}
		} else {
			for (Map.Entry<String, String> attr : metadataNode.getAttributes().entrySet()) {
				metadata.setAttribute(attr.getKey(),
						replaceInvalidCharacters(attr.getValue()));
			}
		}

		return metadata;
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

	private static void parseMetadataError(FeaturesCollection collection, Element metadata, String message) {
		String id = ErrorsHelper.addErrorIntoCollection(collection, null,
				message);
		metadata.setAttribute(ErrorsHelper.ERRORID, id);
	}

	private static InputSource getInputSourceWithEncoding(byte[] array) {
		if (array != null) {
			for (int i = 0; i < array.length; ++i) {
				String encoding = getEncodingWithBegin(array, i);
				if (encoding != null) {
					ByteArrayInputStream is = new ByteArrayInputStream(
							Arrays.copyOfRange(array, i, array.length));
					InputSource source = new InputSource(is);
					source.setEncoding(encoding);
					return source;
				}
			}
		}

		return null;
	}

	private static String getEncodingWithBegin(byte[] bStream, int beginOffset) {
		if (beginOffset >= 0) {
			if (matchesFrom(bStream, beginOffset, UTF32BE_METADATA_PREFIX_DQ) ||
					matchesFrom(bStream, beginOffset, UTF32BE_METADATA_PREFIX_SQ)) {
				return "UTF-32BE";
			} else if (matchesFrom(bStream, beginOffset, UTF32LE_METADATA_PREFIX_DQ) ||
					matchesFrom(bStream, beginOffset, UTF32LE_METADATA_PREFIX_SQ)) {
				return "UTF-32LE";
			} else if (matchesFrom(bStream, beginOffset, UTF16BE_METADATA_PREFIX_DQ) ||
					matchesFrom(bStream, beginOffset, UTF16BE_METADATA_PREFIX_SQ)) {
				return "UTF-16BE";
			} else if (matchesFrom(bStream, beginOffset, UTF16LE_METADATA_PREFIX_DQ) ||
					matchesFrom(bStream, beginOffset, UTF16LE_METADATA_PREFIX_SQ)) {
				return "UTF-16LE";
			} else if (matchesFrom(bStream, beginOffset, UTF8_METADATA_PREFIX_DQ) ||
					matchesFrom(bStream, beginOffset, UTF8_METADATA_PREFIX_DQ_EMPTY) ||
					matchesFrom(bStream, beginOffset, UTF8_METADATA_PREFIX_SQ) ||
					matchesFrom(bStream, beginOffset, UTF8_METADATA_PREFIX_SQ_EMPTY)) {
				return "UTF-8";
			}
		}
		return null;
	}

	private static boolean matchesFrom(byte[] source, int from, byte[] match) {
		if (match.length > source.length - from) {
			return false;
		}
		for (int i = 0; i < match.length; i++) {
			if (source[i + from] != match[i]) {
				return false;
			}
		}
		return true;
	}
}
