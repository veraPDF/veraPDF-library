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

    boolean registerSimpleValidator(String typeName, Pattern pattern) {
        if (typeName == null) {
            throw new IllegalArgumentException("Argument typeName can not be null");
        }
        if (pattern == null) {
            throw new IllegalArgumentException("Argument pattern can not be null");
        }

        String type = getSimplifiedType(typeName);
        if (validators.containsKey(type)) {
            return false;
        }

        validators.put(type, SimpleTypeValidator.fromValue(pattern));
        return true;
    }

    boolean registerStructuredValidator(String typeName, String typeNamespaceURI, Map<String, String> childrenTypes) {
        if (typeName == null) {
            throw new IllegalArgumentException("Argument typeName can not be null");
        }
        if (typeNamespaceURI == null) {
            throw new IllegalArgumentException("Argument typeNamespaceURI can not be null");
        }
        if (childrenTypes == null || childrenTypes.isEmpty()) {
            throw new IllegalArgumentException("Argument childrenTypes can not be null or empty");
        }

        String type = getSimplifiedType(typeName);
        if (validators.containsKey(type)) {
            return false;
        }

        validators.put(type, StructuredTypeValidator.fromValues(typeNamespaceURI, childrenTypes, this));
        return true;
    }

    boolean registerStructuredWithRestrictedFieldsValidator(String typeName, String typeNamespaceURI, Map<String, String> childrenTypes, Map<String, Pattern> childrenRestrictedTypes) {
        if (typeName == null) {
            throw new IllegalArgumentException("Argument typeName can not be null");
        }
        if (typeNamespaceURI == null) {
            throw new IllegalArgumentException("Argument typeNamespaceURI can not be null");
        }
        if (childrenTypes == null) {
            throw new IllegalArgumentException("Argument childrenTypes can not be null or empty");
        }
        if (childrenRestrictedTypes == null) {
            throw new IllegalArgumentException("Argument childrenClosedTypes can not be null or empty");
        }
        String type = getSimplifiedType(typeName);
        if (validators.containsKey(type)) {
            return false;
        }

        validators.put(type, StructuredTypeWithRestrictedFieldsValidator.fromValues(typeNamespaceURI, childrenTypes, childrenRestrictedTypes, this));
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
        String type = getSimplifiedType(typeName);
        for (ArrayTypeValidator.ArrayTypeEnum entr : ArrayTypeValidator.ArrayTypeEnum.values()) {
            String prefix = entr.getType() + " ";
            if (type.startsWith(prefix)) {
                return arrayValidators.get(entr.getType()).isCorresponding(node, type.substring(prefix.length()));
            }
        }
        return validators.containsKey(type) && validators.get(type).isCorresponding(node);
    }

    public boolean isKnownType(String typeName) {
        if (typeName == null) {
            throw new IllegalArgumentException("Argument typeName can not be null");
        }
        boolean needCheck = true;
        String type = getSimplifiedType(typeName);

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

    private static String getSimplifiedType(String type) {
        String res = type.toLowerCase().replaceAll("(open |closed )?(choice |choice$)(of )?", "").trim();
        if (res.isEmpty()) {
            res = XMPConstants.TEXT;
        } else if (res.endsWith(XMPConstants.LANG_ALT)) {
            return res;
        } else {
            for (ArrayTypeValidator.ArrayTypeEnum entr : ArrayTypeValidator.ArrayTypeEnum.values()) {
                String prefix = entr.getType();
                if (res.endsWith(prefix)) {
                    res += " " + XMPConstants.TEXT;
                    break;
                }
            }
        }

        return res;
    }
}
