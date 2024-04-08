/**
 * This file is part of veraPDF Library core, a module of the veraPDF project.
 * Copyright (c) 2015, veraPDF Consortium <info@verapdf.org>
 * All rights reserved.
 *
 * veraPDF Library core is free software: you can redistribute it and/or modify
 * it under the terms of either:
 *
 * The GNU General public license GPLv3+.
 * You should have received a copy of the GNU General Public License
 * along with veraPDF Library core as the LICENSE.GPL file in the root of the source
 * tree.  If not, see http://www.gnu.org/licenses/ or
 * https://www.gnu.org/licenses/gpl-3.0.en.html.
 *
 * The Mozilla Public License MPLv2+.
 * You should have received a copy of the Mozilla Public License along with
 * veraPDF Library core as the LICENSE.MPL file in the root of the source tree.
 * If a copy of the MPL was not distributed with this file, you can obtain one at
 * http://mozilla.org/MPL/2.0/.
 */
package org.verapdf.report;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAnyAttribute;
import javax.xml.bind.annotation.XmlMixed;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.namespace.QName;
import javax.xml.parsers.ParserConfigurationException;

import org.verapdf.features.FeatureExtractionResult;
import org.verapdf.features.FeatureObjectType;
import org.verapdf.features.tools.ErrorsHelper;
import org.verapdf.features.tools.FeatureTreeNode;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * @author Maksim Bezrukov
 */
@XmlRootElement
public class FeaturesNode {

	private static final Logger LOGGER = Logger.getLogger(FeaturesNode.class.getName());

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

	static FeaturesNode fromValues(FeatureExtractionResult collection,
			FeatureObjectType... types) {
		List<Object> qChildren = new ArrayList<>();
		StringBuilder builder = new StringBuilder();
		for (FeatureObjectType type : types) {
			List<FeatureTreeNode> children = collection.getFeatureTreesForType(type);
			if (children != null) {
				for (FeatureTreeNode entry : children) {
					qChildren.add(new JAXBElement<>(new QName(entry.getName()),
							FeaturesNode.class, FeaturesNode.fromValues(entry, collection)));
				}
			}

			List<String> errors = collection.getErrorsForType(type);
			if (errors != null && !errors.isEmpty()) {
				int i = 0;
				if (builder.toString().isEmpty()) {
					builder.append(errors.get(0));
					i = 1;
				}
				while (i < errors.size()) {
					builder.append(", ").append(errors.get(i)); //$NON-NLS-1$
					++i;
				}
			}
		}
		Map<QName, Object> attr = new HashMap<>();
		if (!builder.toString().isEmpty()) {
			attr.put(new QName(ErrorsHelper.ERRORID), builder.toString());
		}

		if (qChildren.isEmpty() && attr.isEmpty()) {
			return null;
		}
        return new FeaturesNode(attr, qChildren);
	}

	static FeaturesNode fromValues(FeatureTreeNode node, FeatureExtractionResult collection) {
		if (node == null) {
			throw new IllegalArgumentException("Argument node cannot be null"); //$NON-NLS-1$
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
					LOGGER.log(Level.INFO, e.getMessage(), e);
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
		if (qAttributes.isEmpty() && qChildren.isEmpty()) {
			return null;
		}
        return new FeaturesNode(qAttributes, qChildren);
	}

	private static String replaceInvalidCharacters(String source) {
		try (Formatter formatter = new Formatter()) {

			for (int i = 0; i < source.length(); ++i) {
				char curChar = source.charAt(i);
				if ('#' == curChar) {
					formatter.format("#x%06X", //$NON-NLS-1$
                            "#".codePointAt(0)); //$NON-NLS-1$
				} else {
					int codePoint = source.codePointAt(i);
					if (Character.isHighSurrogate(curChar)) {
						++i;
					}

					if (codePoint == HT || codePoint == LF || codePoint == CR
							|| (codePoint >= SP && codePoint <= XD7FF)
							|| (codePoint >= XE000 && codePoint <= XFFFD)
							|| (codePoint >= X10000 && codePoint <= X10FFFF)) {
						formatter.format("%c", curChar); //$NON-NLS-1$
						if (Character.isHighSurrogate(curChar)
								&& i < source.length()) {
							formatter.format("%c", //$NON-NLS-1$
                                    source.charAt(i));
						}
					} else {
						formatter.format("#x%06X", codePoint); //$NON-NLS-1$
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

	public Map<QName, Object> getAttributes() {
		return attributes;
	}

	public List<Object> getChildren() {
		return children;
	}
}
