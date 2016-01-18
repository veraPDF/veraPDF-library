package org.verapdf.model.tools.xmp;

import com.adobe.xmp.impl.VeraPDFXMPNode;
import org.verapdf.model.tools.xmp.validators.*;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @author Maksim Bezrukov
 */
public class ValidatorsContainer {

    private Map<String, TypeValidator> validators;
    private Map<String, ArrayTypeValidator> arrayValidators;

    /**
     * Creates new validators container and initialize all array types and simple types
     */
    ValidatorsContainer() {
        this.arrayValidators = new HashMap<>();
        for (ArrayTypeValidator.ArrayTypeEnum entr : ArrayTypeValidator.ArrayTypeEnum.values()) {
            arrayValidators.put(entr.getType(), ArrayTypeValidator.fromValues(entr, this));
        }
        this.validators = new HashMap<>();
        validators.put(XMPConstants.DATE, new DateTypeValidator());
        validators.put(XMPConstants.LANG_ALT, new LangAltValidator());
        validators.put(XMPConstants.URI, new URITypeValidator());
        validators.put(XMPConstants.URL, new URLTypeValidator());
        validators.put(XMPConstants.XPATH, new XPathTypeValidator());
        for (SimpleTypeValidator.SimpleTypeEnum entr : SimpleTypeValidator.SimpleTypeEnum.values()) {
            validators.put(entr.getType(), SimpleTypeValidator.fromValue(entr));
        }
    }

    boolean registerValidator(String typeName, String typeNamespaceURI, Map<String, String> childrenTypes) {
        if (typeName == null) {
            throw new IllegalArgumentException("Argument typeName can not be null");
        }
        if (typeNamespaceURI == null) {
            throw new IllegalArgumentException("Argument typeNamespaceURI can not be null");
        }
        if (childrenTypes == null || childrenTypes.isEmpty()) {
            throw new IllegalArgumentException("Argument childrenTypes can not be null or empty");
        }
        if (validators.containsKey(typeName)) {
            return false;
        }

        validators.put(typeName, StructuredTypeValidator.fromValues(typeNamespaceURI, childrenTypes, this));
        return true;
    }

    boolean registerClosedChoiceValidator(String typeName, String typeNamespaceURI, Map<String, String> childrenTypes, Map<String, Pattern> childrenClosedTypes) {
        if (typeName == null) {
            throw new IllegalArgumentException("Argument typeName can not be null");
        }
        if (typeNamespaceURI == null) {
            throw new IllegalArgumentException("Argument typeNamespaceURI can not be null");
        }
        if (childrenTypes == null) {
            throw new IllegalArgumentException("Argument childrenTypes can not be null or empty");
        }
        if (childrenClosedTypes == null) {
            throw new IllegalArgumentException("Argument childrenClosedTypes can not be null or empty");
        }
        if (validators.containsKey(typeName)) {
            return false;
        }

        validators.put(typeName, StructuredTypeWithClosedChoiceValidator.fromValues(typeNamespaceURI, childrenTypes, childrenClosedTypes, this));
        return true;
    }

    /**
     * Validates the given node to corresponding the given type
     *
     * @param node     node for validating
     * @param typeName type for corresponding
     * @return true if the given type is registred and the given node corresponds to it
     */
    public boolean validate(VeraPDFXMPNode node, String typeName) {
        for (ArrayTypeValidator.ArrayTypeEnum entr : ArrayTypeValidator.ArrayTypeEnum.values()) {
            String prefix = entr.getType() + " ";
            if (typeName.startsWith(prefix)) {
                return arrayValidators.get(entr.getType()).isCorresponding(node, typeName.substring(prefix.length()));
            }
        }
        return validators.containsKey(typeName) && validators.get(typeName).isCorresponding(node);
    }

    public boolean isKnownType(String typeName) {
        if (typeName == null) {
            throw new IllegalArgumentException("Argument typeName can not be null");
        }
        boolean needCheck = true;
        String type = typeName;
        while (needCheck) {
            needCheck = false;
            for (ArrayTypeValidator.ArrayTypeEnum entr : ArrayTypeValidator.ArrayTypeEnum.values()) {
                String prefix = entr.getType() + " ";
                if (type.startsWith(prefix)) {
                    type = type.substring(prefix.length());
                    needCheck = true;
                    break;
                }
            }
        }
        return validators.containsKey(type);
    }
}
