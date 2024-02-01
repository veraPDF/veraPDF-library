package org.verapdf.xmp.impl;

import org.verapdf.xmp.XMPConst;
import org.verapdf.xmp.XMPException;
import org.verapdf.xmp.options.PropertyOptions;

public class VeraPDFExtensionSchemaProperty {

    private static final String NAME = "name";
    private static final String VALUE_TYPE = "valueType";
    private static final String CATEGORY = "category";
    private static final String DESCRIPTION = "description";
    private static final String PDFA_PROPERTY_PREFIX = "pdfaProperty";

    private final VeraPDFXMPNode xmpNode;

    public VeraPDFExtensionSchemaProperty(VeraPDFXMPNode xmpNode) {
        this.xmpNode = xmpNode;
    }

    public String getName() {
        for (VeraPDFXMPNode child : this.xmpNode.getChildren()) {
            if (XMPConst.NS_PDFA_PROPERTY.equals(child.getNamespaceURI()) && NAME.equals(child.getName())) {
                return child.getValue();
            }
        }
        return null;
    }

    public XMPNode getXmpNode() {
        return xmpNode.getOriginalNode();
    }

    public static VeraPDFExtensionSchemaProperty createPropertyDefinitionNode(String name, String valueType, String category, String description) throws XMPException {
        XMPNode node = new XMPNode(XMPConst.ARRAY_ITEM_NAME,"", new PropertyOptions(PropertyOptions.STRUCT), "rdf");
        node.addChild(new XMPNode(PDFA_PROPERTY_PREFIX + ":" + NAME, name, 
                new PropertyOptions(PropertyOptions.NO_OPTIONS), PDFA_PROPERTY_PREFIX));
        node.addChild(new XMPNode(PDFA_PROPERTY_PREFIX + ":" + VALUE_TYPE, valueType, 
                new PropertyOptions(PropertyOptions.NO_OPTIONS), PDFA_PROPERTY_PREFIX));
        node.addChild(new XMPNode(PDFA_PROPERTY_PREFIX + ":" + CATEGORY, category, 
                new PropertyOptions(PropertyOptions.NO_OPTIONS), PDFA_PROPERTY_PREFIX));
        node.addChild(new XMPNode(PDFA_PROPERTY_PREFIX + ":" + DESCRIPTION, description, 
                new PropertyOptions(PropertyOptions.NO_OPTIONS), PDFA_PROPERTY_PREFIX));
        return new VeraPDFExtensionSchemaProperty(VeraPDFXMPNode.fromXMPNode(node));
    }
}
