package org.verapdf.xmp.impl;

import org.verapdf.xmp.XMPMetaFactory;
import org.verapdf.xmp.options.PropertyOptions;
import org.verapdf.xmp.XMPConst;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Maksim Bezrukov
 */
public class VeraPDFXMPNode {

    /**
     * original <code>XMPNode</code> object from metadata
     */
    private XMPNode originalNode;
    /**
     * namespace URI of the node
     */
    private String namespaceURI;
    /**
     * prefix of the node
     */
    private String prefix;
    /**
     * name of the node, contains different information depending of the node kind
     */
    private String name;
    /**
     * value of the node, contains different information depending of the node kind
     */
    private String value;
    /**
     * list of child nodes, lazy initialized
     */
    private List<VeraPDFXMPNode> children;
    /**
     * options describing the kind of the node
     */
    private PropertyOptions options;

    /**
     * Creates an <code>VeraPDFXMPNode</code> with initial values.
     *
     * @param prefix  the prefix of the node
     * @param name    the name of the node
     * @param value   the value of the node
     * @param children list of children for the node
     * @param options the options of the node
     */
    private VeraPDFXMPNode(String prefix, String namespaceURI, String name, String value, List<VeraPDFXMPNode> children, PropertyOptions options, XMPNode originalNode) {
        this.prefix = prefix;
        this.namespaceURI = namespaceURI;
        this.name = name;
        this.value = value;
        this.children = children;
        this.options = options;
        this.originalNode = originalNode;
    }

    static VeraPDFXMPNode fromXMPNode(XMPNode original) {
        if (original == null) {
            return null;
        }
        String prefix = original.getOriginalPrefix();
        String originalName = original.getName();
        int prefixEndIndex = originalName.indexOf(":");
        String name = originalName.substring(prefixEndIndex + 1, originalName.length());
        String value = original.getValue();
        PropertyOptions options = original.getOptions();
        List originalChildren = original.getUnmodifiableChildren();
        List<VeraPDFXMPNode> children = new ArrayList<>(originalChildren.size());
        for (Object child : originalChildren) {
            children.add(fromXMPNode((XMPNode) child));
        }
        String namespaceURI = XMPMetaFactory.getSchemaRegistry().getNamespaceURI(originalName.substring(0, Math.max(prefixEndIndex, 0)));
        return new VeraPDFXMPNode(prefix, namespaceURI, name, value, children, options, original);
    }

    public String getPrefix() {
        return prefix;
    }

    public String getNamespaceURI() {
        return namespaceURI;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public boolean isLanguageAlternative() {
        if (children.isEmpty()) {
            return false;
        }
        for(VeraPDFXMPNode node : children) {
            List elems = node.getQualifier();
            boolean hasLanguageNode = false;
            for(Object elem : elems) {
                if (((XMPNode)elem).isLanguageNode()) {
                    hasLanguageNode = true;
                }
            }
            if (!hasLanguageNode) {
                return false;
            }
        }
        return true;
    }

    public String getLanguageAlternative() {
        if (children.isEmpty()) {
            return null;
        }
        String firstValue = null;
        for (VeraPDFXMPNode node : children) {
            List elems = node.getQualifier();
            for (java.lang.Object elem : elems) {
                if (((XMPNode)elem).isLanguageNode()) {
                    if (XMPConst.X_DEFAULT.equals(((XMPNode)elem).getValue())) {
                        return node.getValue();
                    }
                    if (firstValue == null) {
                        firstValue = node.getValue();
                    }
                }
            }
        }
        return firstValue;
    }

    public List getQualifier(){
        return originalNode.getQualifier();
    }

    public List<VeraPDFXMPNode> getChildren() {
        return children;
    }

    public PropertyOptions getOptions() {
        return options;
    }
}
