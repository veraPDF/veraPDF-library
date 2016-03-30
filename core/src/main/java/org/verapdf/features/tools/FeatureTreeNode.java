package org.verapdf.features.tools;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.verapdf.core.FeatureParsingException;

/**
 * Feature Tree Node for Feature Reporter
 *
 * @author Maksim Bezrukov
 */
public final class FeatureTreeNode {

	private String name;
	private String value;
	private boolean isMetadataNode = false;
	private FeatureTreeNode parent;
	private Map<String, String> attributes = new HashMap<>();
	private List<FeatureTreeNode> children = new ArrayList<>();

	private FeatureTreeNode(final String name) throws FeatureParsingException {
		this(name, null);
	}

	private FeatureTreeNode(String name, FeatureTreeNode parent)
			throws FeatureParsingException {
		this.name = name;
		this.parent = parent;
		if (parent != null) {
			parent.addChild(this);
		}
	}

	/**
	 * @param name the name of the node
	 * @return a new FeatureTreeNode with no parent
	 * @throws FeatureParsingException when
	 */
	public static FeatureTreeNode createRootNode(String name)
			throws FeatureParsingException {
		return new FeatureTreeNode(name);
	}

	/**
	 * Creates new Feature Tree Node
	 *
	 * @param name   name of the node
	 * @param parent parent of the node
	 * @throws FeatureParsingException occurs when parent of the new node has String value
	 */
	public static FeatureTreeNode createChildNode(String name,
												  FeatureTreeNode parent) throws FeatureParsingException {
		return new FeatureTreeNode(name, parent);
	}

	/**
	 * @param name the name of the node
	 * @return a new FeatureTreeNode with no parent
	 * @throws FeatureParsingException when
	 */
	public static FeatureTreeNode createRootMetadataNode(String name)
			throws FeatureParsingException {
		FeatureTreeNode node = new FeatureTreeNode(name);
		node.isMetadataNode = true;
		return node;
	}

	/**
	 * Creates new Feature Tree Node
	 *
	 * @param name   name of the node
	 * @param parent parent of the node
	 * @throws FeatureParsingException occurs when parent of the new node has String value
	 */
	public static FeatureTreeNode createChildMetadataNode(String name,
														  FeatureTreeNode parent) throws FeatureParsingException {
		FeatureTreeNode node = new FeatureTreeNode(name, parent);
		node.isMetadataNode = true;
		return node;
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
	 * @param child new child node for the current node
	 * @throws FeatureParsingException occurs when child adds to node with value or if the node is a metadata node
	 */
	public void addChild(FeatureTreeNode child)
			throws FeatureParsingException {
		if (this.isMetadataNode) {
			throw new FeatureParsingException(
					"You can not add a child for metadata nodes. Node name "
							+ this.name + ", value: " + this.value + ".");
		}

		if (child != null) {
			if (this.value == null) {
				this.children.add(child);
				child.parent = this;
			} else {
				throw new FeatureParsingException(
						"You can not add a child for nodes with defined values. Node name "
								+ this.name + ", value: " + this.value + ".");
			}
		}
	}

	/**
	 * Add value to the node
	 *
	 * @param value value
	 * @throws FeatureParsingException occurs when value adds to the node with childrens or if the object is a metadata
	 *                                 node and the value is not a hex string
	 */
	public void setValue(String value) throws FeatureParsingException {
		if (this.isMetadataNode && value != null && !value.matches("\\p{XDigit}*")) {
			throw new FeatureParsingException("A value for metadata node should be a hex String.");
		}

		if (value == null || this.children.isEmpty()) {
			this.value = value;
		} else {
			throw new FeatureParsingException(
					"You can not add value for nodes with childrens. Node name "
							+ this.name + ".");
		}
	}

	public boolean isMetadataNode() {
		return this.isMetadataNode;
	}

	/**
	 * @return Map object with keys equals to attributes names and values for
	 * them equals to attributes values
	 */
	public Map<String, String> getAttributes() {
		return Collections.unmodifiableMap(this.attributes);
	}

	/**
	 * Added attribute for the node
	 *
	 * @param attributeName  name of the attribute
	 * @param attributeValue value of the attribute
	 */
	public void setAttribute(String attributeName, String attributeValue) {
		this.attributes.put(attributeName, attributeValue);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((this.attributes == null) ? 0 : this.attributes.hashCode());
		result = prime * result
				+ ((this.children == null) ? 0 : this.children.hashCode());
		result = prime * result
				+ ((this.name == null) ? 0 : this.name.hashCode());
		result = prime * result
				+ ((this.value == null) ? 0 : this.value.hashCode());
		result = prime * result + (this.isMetadataNode ? 1 : 0);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FeatureTreeNode other = (FeatureTreeNode) obj;
		if (this.isMetadataNode != other.isMetadataNode) {
			return false;
		} else if (this.attributes == null) {
			if (other.attributes != null)
				return false;
		} else if (!this.attributes.equals(other.attributes))
			return false;
		if (this.children == null) {
			if (other.children != null)
				return false;
		} else if (!isChildrenMatch(this, other))
			return false;
		if (this.name == null) {
			if (other.name != null)
				return false;
		} else if (!this.name.equals(other.name))
			return false;
		if (this.value == null) {
			if (other.value != null)
				return false;
		} else {
			if (!this.value.equals(other.value))
				return false;
		}
		return true;
	}


	/**
	 * { @inheritDoc }
	 */
	@Override
	public String toString() {
		return "FeatureTreeNode [name=" + this.name + ", value=" + this.value
				+ ", isMetadataNode=" + this.isMetadataNode + ", parent=" + this.parent
				+ ", attributes=" + this.attributes + "]";
	}

	private static boolean isChildrenMatch(FeatureTreeNode aThis, FeatureTreeNode other) {
		return aThis.children == other.children || ((aThis.children == null) == (other.children == null))
				&& aThis.children.size() == other.children.size() && aThis.children.containsAll(other.children);
	}
}
