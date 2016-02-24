package org.verapdf.model.tools.xmp;

import com.adobe.xmp.impl.VeraPDFXMPNode;

import javax.xml.namespace.QName;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Maksim Bezrukov
 */
public class SchemasDefinition {

    private Map<QName, String> properties = new HashMap<>();
    private ValidatorsContainer validator;

    protected SchemasDefinition() {
        this(null);
    }

    protected SchemasDefinition(ValidatorsContainer validator) {
        this.validator = validator;
    }

    public boolean isDefinedProperty(VeraPDFXMPNode node) {
        QName name = new QName(node.getNamespaceURI(), node.getName());
        return isDefinedProperty(name);
    }

    protected boolean isDefinedProperty(QName name) {
        return properties.containsKey(name);
    }

    /**
     * Checks the node type
     *
     * @param node node for check
     * @return true if the node type corresponds to defined one, false if it is not,
     * null if the node is not defined or value type is not defined
     */
    public Boolean isCorrespondsDefinedType(VeraPDFXMPNode node) {
        if (validator == null) {
            return null;
        }
        String type = getType(node);
        return type == null ? null : validator.validate(node, type);
    }

    public ValidatorsContainer getValidatorsContainer() {
        return this.validator;
    }

    /**
     * Registers property with known value type
     *
     * @param namespaceURI property namespace uri for registration
     * @param propertyName property name for registration
     * @param type         property type for registration
     * @return true if property was registered successfully
     */
    protected boolean registerProperty(String namespaceURI, String propertyName, String type) {
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

        QName name = new QName(namespaceURI, propertyName);
        if (properties.containsKey(name)) {
            return false;
        } else {
            properties.put(name, type);
            return true;
        }
    }

    private String getType(VeraPDFXMPNode node) {
        QName name = new QName(node.getNamespaceURI(), node.getName());
        return properties.get(name);
    }
}
