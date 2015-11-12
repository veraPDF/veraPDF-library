package org.verapdf.report;

import org.apache.log4j.Logger;
import org.verapdf.features.FeaturesObjectTypesEnum;
import org.verapdf.features.tools.ErrorsHelper;
import org.verapdf.features.tools.FeatureTreeNode;
import org.verapdf.features.tools.FeaturesCollection;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAnyAttribute;
import javax.xml.bind.annotation.XmlMixed;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.namespace.QName;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.*;

/**
 * @author Maksim Bezrukov
 */
@XmlRootElement
public class FeaturesNode {

	private static final Logger LOGGER = Logger.getLogger(FeaturesNode.class);

	private static final int XD7FF = 0xD7FF;
	private static final int XE000 = 0xE000;
	private static final int XFFFD = 0xFFFD;
	private static final int X10000 = 0x10000;
	private static final int X10FFFF = 0x10FFFF;
	private static final int SP = 0x20;
	private static final int HT = 0x9;
	private static final int LF = 0xA;
	private static final int CR = 0xD;

	@XmlAnyAttribute
	private final Map<QName, Object> attributes;
	@XmlMixed
	private final List<Object> children;

	private FeaturesNode(Map<QName, Object> attributes, List<Object> children) {
		this.attributes = attributes;
		this.children = children;
	}

	private FeaturesNode() {
		this(null, null);
	}

	static FeaturesNode fromValues(FeaturesCollection collection,
								   FeaturesObjectTypesEnum type) {
		List<FeatureTreeNode> children = collection.getFeatureTreesForType(type);
		List<Object> qChildren = new ArrayList<>();
		if (children != null) {
			for (FeatureTreeNode entry : children) {
				qChildren.add(new JAXBElement<>(new QName(entry.getName()),
						FeaturesNode.class, FeaturesNode.fromValues(entry, collection)));
			}
		}

		List<String> errors = collection.getErrorsForType(type);
		Map<QName, Object> attr = new HashMap<>();
		if (errors != null && !errors.isEmpty()) {
			StringBuilder builder = new StringBuilder();
			builder.append(errors.get(0));
			for (int i = 1; i < errors.size(); ++i) {
				builder.append(", ").append(errors.get(i));
			}
			attr.put(new QName(ErrorsHelper.ERRORID), builder.toString());
		}
		return new FeaturesNode(attr, qChildren);
	}

	static FeaturesNode fromValues(FeatureTreeNode node, FeaturesCollection collection) {
		if (node == null) {
			throw new IllegalArgumentException("Argument node cannot be null");
		}

		Map<QName, Object> qAttributes = new HashMap<>();
		for (Map.Entry<String, String> entry : node.getAttributes().entrySet()) {
			qAttributes.put(new QName(entry.getKey()),
					replaceInvalidCharacters(entry.getValue()));
		}

		List<Object> qChildren = new ArrayList<>();
		if (node.getValue() != null) {
			if (node.isMetadataNode()) {
				try {
					JAXBElement<FeaturesNode> metadata = FeaturesNode.fromXmp(node);
					qChildren.add(metadata);
				} catch (SAXException | IOException
						| ParserConfigurationException e) {
					LOGGER.error(e);
					String errorId = ErrorsHelper.addErrorIntoCollection(collection, null, e.getMessage());
					qAttributes.put(new QName(ErrorsHelper.ERRORID), errorId);
				}
			} else {
				qChildren.add(replaceInvalidCharacters(node.getValue()));
			}

		}
		if (!node.getChildren().isEmpty()) {
			for (FeatureTreeNode entry : node.getChildren()) {
				qChildren.add(new JAXBElement<>(new QName(entry.getName()),
						FeaturesNode.class, FeaturesNode.fromValues(entry, collection)));
			}
		}
		return new FeaturesNode(qAttributes, qChildren);
	}

	private static String replaceInvalidCharacters(String source) {
		try (Formatter formatter = new Formatter()) {

			for (int i = 0; i < source.length(); ++i) {
				char curChar = source.charAt(i);
				if ('#' == curChar) {
					formatter.format("#x%06X",
							Integer.valueOf("#".codePointAt(0)));
				} else {
					int codePoint = source.codePointAt(i);
					if (Character.isHighSurrogate(curChar)) {
						++i;
					}

					if (codePoint == HT || codePoint == LF || codePoint == CR
							|| (codePoint >= SP && codePoint <= XD7FF)
							|| (codePoint >= XE000 && codePoint <= XFFFD)
							|| (codePoint >= X10000 && codePoint <= X10FFFF)) {
						formatter.format("%c", Character.valueOf(curChar));
						if (Character.isHighSurrogate(curChar)
								&& i < source.length()) {
							formatter.format("%c",
									Character.valueOf(source.charAt(i)));
						}
					} else {
						formatter.format("#x%06X", Integer.valueOf(codePoint));
					}
				}
			}

			return formatter.toString();
		}
	}

	public static JAXBElement<FeaturesNode> fromXmp(final FeatureTreeNode xmpNode) throws SAXException, IOException, ParserConfigurationException {
		Node node = XmpHandler.parseMetadataRootElement(xmpNode);
		if (node == null) {
			return null;
		}
		FeaturesNode fromXmp = nodeFromXmlElement(node);
		return new JAXBElement<>(new QName(node.getNodeName()), FeaturesNode.class, fromXmp);
	}

	public static FeaturesNode nodeFromXmlElement(final Node node) {
		Map<QName, Object> atts = new HashMap<>();
		NamedNodeMap nnm = node.getAttributes();
		if (nnm != null) {
			for (int index = 0; index < nnm.getLength(); index++) {
				atts.put(new QName(nnm.item(index).getNodeName()), nnm.item(index).getNodeValue());
			}
		}
		List<Object> children = new ArrayList<>();
		NodeList nodeList = node.getChildNodes();
		for (int index = 0; index < nodeList.getLength(); index++) {
			Node item = nodeList.item(index);
			String nodeValue = item.getNodeValue();
			if (nodeValue == null) {
				children.add(new JAXBElement<>(new QName(item.getNodeName()), FeaturesNode.class,
						nodeFromXmlElement(item)));
			} else if (!nodeValue.trim().isEmpty()) {
				children.add(nodeValue);
			}
		}
		return new FeaturesNode(atts, children);
	}

}
