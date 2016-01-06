package org.verapdf.model.tools.xmp;

import com.adobe.xmp.impl.VeraPDFXMPNode;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Maksim Bezrukov
 */
public class SchemasDefinition {

    private Map<String, Map<String, String>> properties = new HashMap<>();
    private ValidatorsContainer validator;

    public SchemasDefinition(ValidatorsContainer validator) {
        this.validator = validator;
    }

    public boolean isDefinedProperty(VeraPDFXMPNode node) {
        //TODO: implement this
        return false;
    }

    /**
     * Checks the node type
     *
     * @param node node for check
     * @return true if the node type corresponds to defined one, false if it is not,
     * null if the node is not defined
     */
    public Boolean isCorrespondsDefinedType(VeraPDFXMPNode node) {
        //TODO: implement this
        return null;
    }

    public ValidatorsContainer getValidatorsContainer() {
        return this.validator;
    }

    /**
     * Registers property with value type
     *
     * @param namespaceURI property namespace uri for registration
     * @param propertyName property name for registration
     * @param type         property type for registration
     * @return true if this type is known by schema and property was registered successfully
     */
    public boolean registerProperty(String namespaceURI, String propertyName, String type) {
        if (namespaceURI == null) {
            throw new IllegalArgumentException("Argument namespaceURI can not be null");
        }
        if (propertyName == null) {
            throw new IllegalArgumentException("Argument property name can not be null");
        }
        if (type == null) {
            throw new IllegalArgumentException("Argument type can not be null");
        }

        if (!validator.isKnownType(type)) {
            return false;
        }

        if (!properties.containsKey(namespaceURI)) {
            properties.put(namespaceURI, new HashMap<String, String>());
        }
        Map<String, String> schemaProperties = properties.get(namespaceURI);
        if (schemaProperties.containsKey(propertyName)) {
            return false;
        } else {
            schemaProperties.put(propertyName, type);
            return true;
        }
    }

    private String getType(VeraPDFXMPNode node) {
        String namespace = node.getNamespaceURI();
        String name = node.getName();
        Map<String, String> namespaceMap = properties.get(namespace);
        if (namespaceMap == null) {
            return null;
        } else {
            return namespaceMap.get(name);
        }
    }
}
