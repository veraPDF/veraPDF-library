package org.verapdf.xmp.impl;

import org.verapdf.xmp.XMPException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class VeraPDFExtensionSchemasContainer {
    
    public static final String PDFA_SCHEMA_PREFIX = "pdfaSchema";
    public static final String PDFA_PROPERTY_PREFIX = "pdfaProperty";
    public static final String NAMESPACE_URI = "namespaceURI";

    private final VeraPDFXMPNode veraPDFXMPNode;

    public VeraPDFExtensionSchemasContainer(VeraPDFXMPNode veraPDFXMPNode) {
        this.veraPDFXMPNode = veraPDFXMPNode;
    }
    
    public void addExtensionSchemaDefinition(VeraPDFExtensionSchemaDefinition veraPDFExtensionSchemaDefinition) throws XMPException {
        veraPDFXMPNode.getOriginalNode().addChild(veraPDFExtensionSchemaDefinition.getXmpNode());
    }
    
    public VeraPDFExtensionSchemaProperty getPropertyDefinition(String namespaceURI, String prefix, String name) {
        VeraPDFExtensionSchemaDefinition definition = getExtensionSchemaDefinitionXMPNode(namespaceURI, prefix);
        if (definition != null) {
            for (VeraPDFExtensionSchemaProperty propertyDefinitionXMPNode : definition.getExtensionSchemaProperties()) {
                if (Objects.equals(propertyDefinitionXMPNode.getName(), name)) {
                    return propertyDefinitionXMPNode;
                }
            }
        }
        return null;
    }

    public VeraPDFExtensionSchemaDefinition getExtensionSchemaDefinitionXMPNode(String namespaceURI, String prefix) {
        for (VeraPDFExtensionSchemaDefinition definition : getExtensionSchemaDefinitions()) {
            if (Objects.equals(definition.getNamespaceURI(), namespaceURI) && Objects.equals(definition.getPrefix(), prefix)) {
                return definition;
            }
        }
        return null;
    }

    private List<VeraPDFExtensionSchemaDefinition> getExtensionSchemaDefinitions() {
        if (this.veraPDFXMPNode != null && this.veraPDFXMPNode.getOptions().isArray()) {
            List<VeraPDFExtensionSchemaDefinition> res = new ArrayList<>();
            for (VeraPDFXMPNode node : this.veraPDFXMPNode.getChildren()) {
                res.add(new VeraPDFExtensionSchemaDefinition(node));
            }
            return Collections.unmodifiableList(res);
        }
        return Collections.emptyList();
    }
}
