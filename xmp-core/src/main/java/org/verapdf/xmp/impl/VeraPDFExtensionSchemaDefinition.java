package org.verapdf.xmp.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.verapdf.xmp.XMPConst;
import org.verapdf.xmp.XMPException;
import org.verapdf.xmp.options.PropertyOptions;

public class VeraPDFExtensionSchemaDefinition {

    private static final String PROPERTY = "property";
    private static final String NAMESPACE_URI = "namespaceURI";
    private static final String PREFIX = "prefix";
    private static final String SCHEMA = "schema";
    private static final String PDFA_SCHEMA_PREFIX = "pdfaSchema";

    private final VeraPDFXMPNode xmpNode;

    /**
     * VeraPDF extension schema.
     *
     * @param xmpNode a metadata node
     */
    public VeraPDFExtensionSchemaDefinition(VeraPDFXMPNode xmpNode) {
        this.xmpNode = xmpNode;
    }

    /**
     * Gets schema properties.
     *
     * @return schema properties
     */
    public List<VeraPDFExtensionSchemaProperty> getExtensionSchemaProperties() {
        if (this.xmpNode != null) {
            List<VeraPDFExtensionSchemaProperty> res = new ArrayList<>();
            for (VeraPDFXMPNode child : this.xmpNode.getChildren()) {
                if (XMPConst.NS_PDFA_SCHEMA.equals(child.getNamespaceURI()) && PROPERTY.equals(child.getName())) {
                    if (child.getOptions().isArray()) {
                        for (VeraPDFXMPNode node : child.getChildren()) {
                            res.add(new VeraPDFExtensionSchemaProperty(node));
                        }
                    }
                    break;
                }
            }
            return res;
        }
        return Collections.emptyList();
    }

    /**
     * Gets property of xmp node.
     *
     * @return xmp node property value
     */
    public VeraPDFXMPNode getPropertiesNode() {
        if (this.xmpNode != null) {
            for (VeraPDFXMPNode child : this.xmpNode.getChildren()) {
                if (XMPConst.NS_PDFA_SCHEMA.equals(child.getNamespaceURI()) && PROPERTY.equals(child.getName())) {
                    return child;
                }
            }
        }
        return null;
    }

    /**
     * Adds extension schema property.
     *
     * @param propertyDefinitionXMPNode a property of xmp node
     *
     * @throws XMPException exceptions from the metadata processing
     */
    public void addExtensionSchemaProperty(VeraPDFExtensionSchemaProperty propertyDefinitionXMPNode)
            throws XMPException {
        if (this.xmpNode != null) {
            getPropertiesNode().getOriginalNode().addChild(propertyDefinitionXMPNode.getXmpNode());
        }
    }

    /**
     * Gets namespaces URI of xmp node.
     *
     * @return xmp node namespace value
     */
    public String getNamespaceURI() {
        for (VeraPDFXMPNode child : this.xmpNode.getChildren()) {
            if (XMPConst.NS_PDFA_SCHEMA.equals(child.getNamespaceURI()) && NAMESPACE_URI.equals(child.getName())) {
                return child.getValue();
            }
        }
        return null;
    }

    /**
     * Gets prefix of xmp node.
     *
     * @return xmp node prefix value
     */
    public String getPrefix() {
        for (VeraPDFXMPNode child : this.xmpNode.getChildren()) {
            if (XMPConst.NS_PDFA_SCHEMA.equals(child.getNamespaceURI()) && PREFIX.equals(child.getName())) {
                return child.getValue();
            }
        }
        return null;
    }

    /**
     * Gets xmp node.
     *
     * @return original xmp node from metadata
     */
    public XMPNode getXmpNode() {
        return xmpNode.getOriginalNode();
    }

    /**
     * Creates schema definition node.
     *
     * @param schema       a xmp schema definition
     * @param namespaceURI a namespace URI of the node
     * @param prefix       a prefix of the node
     *
     * @return xmp extension schema
     *
     * @throws XMPException exceptions from the metadata processing
     */
    public static VeraPDFExtensionSchemaDefinition createExtensionSchemaDefinitionNode(String schema,
            String namespaceURI, String prefix) throws XMPException {
        XMPNode node = new XMPNode(XMPConst.ARRAY_ITEM_NAME, "", new PropertyOptions(PropertyOptions.STRUCT), "rdf");
        node.addChild(new XMPNode(PDFA_SCHEMA_PREFIX + ":" + SCHEMA, schema,
                new PropertyOptions(PropertyOptions.NO_OPTIONS), PDFA_SCHEMA_PREFIX));
        node.addChild(new XMPNode(PDFA_SCHEMA_PREFIX + ":" + NAMESPACE_URI, namespaceURI,
                new PropertyOptions(PropertyOptions.NO_OPTIONS), PDFA_SCHEMA_PREFIX));
        node.addChild(new XMPNode(PDFA_SCHEMA_PREFIX + ":" + PREFIX, prefix,
                new PropertyOptions(PropertyOptions.NO_OPTIONS), PDFA_SCHEMA_PREFIX));
        node.addChild(new XMPNode(PDFA_SCHEMA_PREFIX + ":" + PROPERTY, "",
                new PropertyOptions(PropertyOptions.ARRAY + PropertyOptions.ARRAY_ORDERED), PDFA_SCHEMA_PREFIX));
        return new VeraPDFExtensionSchemaDefinition(VeraPDFXMPNode.fromXMPNode(node));
    }
}
