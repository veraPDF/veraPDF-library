package org.verapdf.xmp.impl;

import org.verapdf.xmp.XMPConst;
import org.verapdf.xmp.XMPException;
import org.verapdf.xmp.options.PropertyOptions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class VeraPDFExtensionSchemaDefinition {

    private static final String PROPERTY = "property";
    public static final String NAMESPACE_URI = "namespaceURI";
    private static final String PREFIX = "prefix";
    private static final String SCHEMA = "schema";
    private static final String PDFA_SCHEMA_PREFIX = "pdfaSchema";

    private final VeraPDFXMPNode xmpNode;

    public VeraPDFExtensionSchemaDefinition(VeraPDFXMPNode xmpNode) {
        this.xmpNode = xmpNode;
    }

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

    public void addExtensionSchemaProperty(VeraPDFExtensionSchemaProperty propertyDefinitionXMPNode) throws XMPException {
        if (this.xmpNode != null) {
            getPropertiesNode().getOriginalNode().addChild(propertyDefinitionXMPNode.getXmpNode());
        }
    }

    public String getNamespaceURI() {
        for (VeraPDFXMPNode child : this.xmpNode.getChildren()) {
            if (XMPConst.NS_PDFA_SCHEMA.equals(child.getNamespaceURI()) && NAMESPACE_URI.equals(child.getName())) {
                return child.getValue();
            }
        }
        return null;
    }

    public String getPrefix() {
        for (VeraPDFXMPNode child : this.xmpNode.getChildren()) {
            if (XMPConst.NS_PDFA_SCHEMA.equals(child.getNamespaceURI()) && PREFIX.equals(child.getName())) {
                return child.getValue();
            }
        }
        return null;
    }

    public XMPNode getXmpNode() {
        return xmpNode.getOriginalNode();
    }

    public static VeraPDFExtensionSchemaDefinition createExtensionSchemaDefinitionNode(String schema, String namespaceURI, String prefix) throws XMPException {
        XMPNode node = new XMPNode(XMPConst.ARRAY_ITEM_NAME,"", new PropertyOptions(PropertyOptions.STRUCT), "rdf");
        node.addChild(new XMPNode(PDFA_SCHEMA_PREFIX + ":" + SCHEMA, schema,
                new PropertyOptions(PropertyOptions.NO_OPTIONS), PDFA_SCHEMA_PREFIX));
        node.addChild(new XMPNode(PDFA_SCHEMA_PREFIX + ":" + NAMESPACE_URI, namespaceURI,
                new PropertyOptions(PropertyOptions.NO_OPTIONS), PDFA_SCHEMA_PREFIX));
        node.addChild(new XMPNode(PDFA_SCHEMA_PREFIX + ":" + PREFIX, prefix,
                new PropertyOptions(PropertyOptions.NO_OPTIONS), PDFA_SCHEMA_PREFIX));
        node.addChild(new XMPNode(PDFA_SCHEMA_PREFIX + ":" + PROPERTY,"",
                new PropertyOptions(PropertyOptions.ARRAY + PropertyOptions.ARRAY_ORDERED), PDFA_SCHEMA_PREFIX));
        return new VeraPDFExtensionSchemaDefinition(VeraPDFXMPNode.fromXMPNode(node));
    }
}
