/**
 * This file is part of veraPDF Library core, a module of the veraPDF project.
 * Copyright (c) 2015-2025, veraPDF Consortium <info@verapdf.org>
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
package org.verapdf.features.tools;

import org.verapdf.core.FeatureParsingException;

import java.util.*;

/**
 * Feature Tree Node for Feature Reporter
 *
 * @author Maksim Bezrukov
 */
public final class FeatureTreeNode {

	private final String name;
	private final boolean isMetadataNode;
	private String value;
	private final Map<String, String> attributes = new HashMap<>();
	private final List<FeatureTreeNode> children = new ArrayList<>();

	private FeatureTreeNode(String name) {
		this(name, false);
	}

	private FeatureTreeNode(String name, boolean isMetaNode) {
		this.name = name;
		this.isMetadataNode = isMetaNode;
	}

	/**
	 * @return name of the node
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * @return value of the node
	 */
	public String getValue() {
		return this.value;
	}

	/**
	 * @return list of all children nodes for this node
	 */
	public List<FeatureTreeNode> getChildren() {
		return Collections.unmodifiableList(this.children);
	}

	/**
	 * Add a child to the node
	 *
	 * @param nodeName
	 *            new child node for the current node
	 * @throws FeatureParsingException
	 *             occurs when child adds to node with value or if the node is a
	 *             metadata node
	 */
	public FeatureTreeNode addChild(String nodeName) throws FeatureParsingException {
		FeatureTreeNode node = new FeatureTreeNode(nodeName);
		return this.addChild(node);
	}

	/**
	 * Add a child to the node
	 *
	 * @param nodeName
	 *            new child node for the current node
	 * @throws FeatureParsingException
	 *             occurs when child adds to node with value or if the node is a
	 *             metadata node
	 */
	public FeatureTreeNode addMetadataChild(String nodeName) throws FeatureParsingException {
		FeatureTreeNode node = new FeatureTreeNode(nodeName, true);
		return this.addChild(node);
	}

	public FeatureTreeNode addChild(FeatureTreeNode node) throws FeatureParsingException {
		if (node == null)
			throw new IllegalArgumentException("Arg node cannot be null.");
		if (this.isMetadataNode) {
			throw new FeatureParsingException("You can not add a child for metadata nodes. Node name " + this.name
					+ ", value: " + this.value + '.');
		}
		if (this.value == null) {
			this.children.add(node);
		} else {
			throw new FeatureParsingException("You can not add a child for nodes with defined values. Node name "
					+ this.name + ", value: " + this.value + '.');
		}
		return node;
	}

	/**
	 * Add value to the node
	 *
	 * @param value
	 *            value
	 * @throws FeatureParsingException
	 *             occurs when value adds to the node with children or if the
	 *             object is a metadata node and the value is not a hex string
	 */
	public void setValue(String value) throws FeatureParsingException {
		if (this.isMetadataNode && value != null && !value.matches("\\p{XDigit}*")) {
			throw new FeatureParsingException("A value for metadata node should be a hex String.");
		}

		if (value == null || this.children.isEmpty()) {
			this.value = value;
		} else {
			throw new FeatureParsingException(
					"You can not add value for nodes with children. Node name " + this.name + '.');
		}
	}

	public boolean isMetadataNode() {
		return this.isMetadataNode;
	}

	/**
	 * @return Map object with keys equals to attributes names and values for
	 *         them equals to attributes values
	 */
	public Map<String, String> getAttributes() {
		return Collections.unmodifiableMap(this.attributes);
	}

	/**
	 * Added attribute for the node
	 *
	 * @param attributeName
	 *            name of the attribute
	 * @param attributeValue
	 *            value of the attribute
	 */
	public void setAttribute(String attributeName, String attributeValue) {
		this.attributes.put(attributeName, attributeValue);
	}

	public void setAttributes(Map<String, String> toSet) {
		this.attributes.putAll(toSet);
	}
	
	public static final FeatureTreeNode createRootNode(final String name) {
		return new FeatureTreeNode(name);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.attributes == null) ? 0 : this.attributes.hashCode());
		int childrenHashCode = 0;
		for (FeatureTreeNode child : this.children) {
			childrenHashCode += child.hashCode();
		}
		result = prime * result + childrenHashCode;
		result = prime * result + ((this.name == null) ? 0 : this.name.hashCode());
		result = prime * result + ((this.value == null) ? 0 : this.value.hashCode());
		result = prime * result + (this.isMetadataNode ? 1 : 0);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		FeatureTreeNode other = (FeatureTreeNode) obj;
		if (this.isMetadataNode != other.isMetadataNode) {
			return false;
		}
		if (!Objects.equals(this.attributes, other.attributes)) {
			return false;
		}
		if (!isChildrenMatch(this, other)) {
			return false;
		}
		if (!Objects.equals(this.name, other.name)) {
			return false;
		}
		return Objects.equals(this.value, other.value);
	}

	/**
	 * { @inheritDoc }
	 */
	@Override
	public String toString() {
		return "FeatureTreeNode [name=" + this.name + ", value=" + this.value + ", isMetadataNode="
				+ this.isMetadataNode + ", " + ", attributes=" + this.attributes + ']';
	}

	private static boolean isChildrenMatch(FeatureTreeNode aThis, FeatureTreeNode other) {
		return aThis.children == other.children || (aThis.children.size() == other.children.size() && 
				aThis.children.containsAll(other.children));
	}
}
