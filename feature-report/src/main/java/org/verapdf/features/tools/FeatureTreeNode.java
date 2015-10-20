package org.verapdf.features.tools;

import org.verapdf.exceptions.featurereport.FeaturesTreeNodeException;

import java.util.*;

/**
 * Feature Tree Node for Feature Reporter
 *
 * @author Maksim Bezrukov
 */
public final class FeatureTreeNode {

	private String name;
	private String value;
	private FeatureTreeNode parent;
	private Map<String, String> attributes = new HashMap<>();
	private List<FeatureTreeNode> children;

	private FeatureTreeNode(final String name) throws FeaturesTreeNodeException {
		this(name, null, null);
	}

	private FeatureTreeNode(final String name, final String value) throws FeaturesTreeNodeException {
		this(name, value, null);
	}

	private FeatureTreeNode(String name, FeatureTreeNode parent)
			throws FeaturesTreeNodeException {
		this(name, null, parent);
	}

	private FeatureTreeNode(String name, String value, FeatureTreeNode parent)
			throws FeaturesTreeNodeException {
		this.name = name;
		this.value = value;
		this.parent = parent;
		if (parent != null) {
			parent.addChild(this);
		}
	}

	/**
	 * @param name the name of the node
	 * @return a new FeatureTreeNode with no parent
	 * @throws FeaturesTreeNodeException when
	 */
	public static FeatureTreeNode newRootInstance(String name)
			throws FeaturesTreeNodeException {
		return new FeatureTreeNode(name);
	}

	/**
	 * @param name the name of the node
	 * @return a new FeatureTreeNode with no parent
	 * @throws FeaturesTreeNodeException when
	 */
	public static FeatureTreeNode newRootInstanceWIthValue(
			final String name, final String value)
			throws FeaturesTreeNodeException {
		return new FeatureTreeNode(name, value);
	}

	/**
	 * Creates new Feature Tree Node
	 *
	 * @param name   name of the node
	 * @param parent parent of the node
	 * @throws FeaturesTreeNodeException occurs when parent of the new node has String value
	 */
	public static FeatureTreeNode newChildInstance(String name,
												   FeatureTreeNode parent) throws FeaturesTreeNodeException {
		return new FeatureTreeNode(name, parent);
	}

	/**
	 * Constructs node with string value
	 *
	 * @param name   name of the node
	 * @param value  value of the node
	 * @param parent parend of the node
	 * @return a new feature
	 * @throws FeaturesTreeNodeException occurs when parent of the new node has String value
	 */
	public static FeatureTreeNode newChildInstanceWithValue(String name,
															String value, FeatureTreeNode parent)
			throws FeaturesTreeNodeException {
		return new FeatureTreeNode(name, value, parent);
	}

	/**
	 * @return name of the node
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return value of the node
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @return parent node for the curent node
	 */
	public FeatureTreeNode getParent() {
		return parent;
	}

	/**
	 * @return list of all children nodes for this node
	 */
	public List<FeatureTreeNode> getChildren() {
		return children == null ? null : Collections.unmodifiableList(children);
	}

	/**
	 * Add a child to the node
	 *
	 * @param child new child node for the current node
	 * @throws FeaturesTreeNodeException occurs when child adds to node with value
	 */
	public void addChild(FeatureTreeNode child)
			throws FeaturesTreeNodeException {
		if (child != null) {
			if (value == null) {
				if (children == null) {
					children = new ArrayList<>();
				}
				children.add(child);
				child.parent = this;
			} else {
				throw new FeaturesTreeNodeException(
						"You can not add childrens for nodes with defined values. Node name "
								+ name + ", value: " + value + ".");
			}
		}
	}

	/**
	 * Add value to the node
	 *
	 * @param value value
	 * @throws FeaturesTreeNodeException occurs when value adds to the node with childrens
	 */
	public void setValue(String value) throws FeaturesTreeNodeException {
		if (children == null) {
			this.value = value;
		} else {
			throw new FeaturesTreeNodeException(
					"You can not add value for nodes with childrens. Node name "
							+ name + ".");
		}
	}

	/**
	 * @return Map object with keys equals to attributes names and values for
	 * them equals to attributes values
	 */
	public Map<String, String> getAttributes() {
		return Collections.unmodifiableMap(attributes);
	}

	/**
	 * Added attribute for the node
	 *
	 * @param attributeName  name of the attribute
	 * @param attributeValue value of the attribute
	 */
	public void addAttribute(String attributeName, String attributeValue) {
		attributes.put(attributeName, attributeValue);
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
		if (this.attributes == null) {
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

	private static boolean isChildrenMatch(FeatureTreeNode aThis, FeatureTreeNode other) {
		return aThis.children == other.children || !(aThis.children == null ^ other.children == null)
				&& aThis.children.size() == other.children.size() && aThis.children.containsAll(other.children);
	}
}
