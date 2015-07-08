package org.verapdf.features.tools;

import org.verapdf.exceptions.featurereport.FeaturesTreeNodeException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Feature Tree Node for Feature Reporter
 *
 * @author Maksim Bezrukov
 */
public final class FeatureTreeNode {

    private String name;
    private String value;
    private FeatureTreeNode parent;
    private FeatureTreeNode root;
    private Map<String, String> attributes;
    private List<FeatureTreeNode> children;

    private FeatureTreeNode(String name, FeatureTreeNode parent) throws FeaturesTreeNodeException {
        this.name = name;
        this.parent = parent;
        if (parent == null) {
            this.root = this;
        } else {
            this.root = parent.getRoot();
            parent.addChild(this);
        }
        attributes = new HashMap<>();
    }

    private FeatureTreeNode(String name, String value, FeatureTreeNode parent) throws FeaturesTreeNodeException {
        this.name = name;
        this.value = value;
        this.parent = parent;
        if (parent == null) {
            this.root = this;
        } else {
            this.root = parent.getRoot();
            parent.addChild(this);
        }
        attributes = new HashMap<>();
    }

    /**
     * Creates new Feature Tree Node
     *
     * @param name   - name of the node
     * @param parent - parent of the node
     * @throws FeaturesTreeNodeException - occurs when parent of the new node has String value
     */
    public static FeatureTreeNode newInstance(String name, FeatureTreeNode parent) throws FeaturesTreeNodeException {
        return new FeatureTreeNode(name, parent);
    }

    /**
     * Constructs node with string value
     *
     * @param name   - name of the node
     * @param value  - value of the node
     * @param parent - parend of the node
     * @throws FeaturesTreeNodeException - occurs when parent of the new node has String value
     */
    public static FeatureTreeNode newInstance(String name, String value, FeatureTreeNode parent) throws FeaturesTreeNodeException {
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
     * @return root node for a tree in which this node situated
     */
    public FeatureTreeNode getRoot() {
        return root;
    }

    /**
     * @return list of all children nodes for this node
     */
    public List<FeatureTreeNode> getChildren() {
        return children;
    }

    /**
     * @return true if there is no childrens for this node
     */
    public boolean isLeaf() {
        return children == null;
    }

    /**
     * Add a child to the node
     *
     * @param child - new child node for the current node
     * @throws FeaturesTreeNodeException - occurs when child adds to node with value
     */
    public void addChild(FeatureTreeNode child) throws FeaturesTreeNodeException {
        if (child != null) {
            if (value == null) {
                if (children == null) {
                    children = new ArrayList<>();
                }
                children.add(child);
            } else {
                throw new FeaturesTreeNodeException("You can not add childrens for nodes with defined values. Node name " + name + ", value: " + value + ".");
            }
        }
    }

    /**
     * Add value to the node
     *
     * @param value - String value
     * @throws FeaturesTreeNodeException - occurs when value adds to the node with childrens
     */
    public void setValue(String value) throws FeaturesTreeNodeException {
        if (children == null) {
            this.value = value;
        } else {
            throw new FeaturesTreeNodeException("You can not add value for nodes with childrens. Node name " + name + ".");
        }
    }

    /**
     * @return Map object with keys equals to attributes names and values for them equals to attributes values
     */
    public Map<String, String> getAttributes() {
        return attributes;
    }

    /**
     * Added attribute for the node
     *
     * @param name  - name of the attribute
     * @param value - value of the attribute
     */
    public void addAttribute(String name, String value) {
        attributes.put(name, value);
    }
}
