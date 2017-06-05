/**
 * This file is part of veraPDF Library core, a module of the veraPDF project.
 * Copyright (c) 2015, veraPDF Consortium <info@verapdf.org>
 * All rights reserved.
 *
 * veraPDF Library core is free software: you can redistribute it and/or modify
 * it under the terms of either:
 *
 * The GNU General public license GPLv3+.
 * You should have received a copy of the GNU General Public License
 * along with veraPDF Library core as the LICENSE.GPL file in the root of the source
 * tree.  If not, see http://www.gnu.org/licenses/ or
 * https://www.gnu.org/licenses/gpl-3.0.en.html.
 *
 * The Mozilla Public License MPLv2+.
 * You should have received a copy of the Mozilla Public License along with
 * veraPDF Library core as the LICENSE.MPL file in the root of the source tree.
 * If a copy of the MPL was not distributed with this file, you can obtain one at
 * http://mozilla.org/MPL/2.0/.
 */
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
            this.arrayValidators.put(entr.getType(), ArrayTypeValidator.fromValues(entr, this));
        }
        this.validators = new HashMap<>();
        this.validators.put(XMPConstants.DATE, new DateTypeValidator());
        this.validators.put(XMPConstants.LANG_ALT, new LangAltValidator());
        this.validators.put(XMPConstants.URI, new URITypeValidator());
        this.validators.put(XMPConstants.URL, new URLTypeValidator());
        this.validators.put(XMPConstants.XPATH, new XPathTypeValidator());
        for (SimpleTypeValidator.SimpleTypeEnum entr : SimpleTypeValidator.SimpleTypeEnum.values()) {
            this.validators.put(entr.getType(), SimpleTypeValidator.fromValue(entr));
        }
    }

    boolean registerSimpleValidator(String typeName, Pattern pattern) {
        if (pattern == null) {
            throw new IllegalArgumentException("Argument pattern can not be null");
        }
        return registerSimpleValidator(typeName, SimpleTypeValidator.fromValue(pattern));
    }

    boolean registerSimpleValidator(String typeName, SimpleTypeValidator simpleTypeValidator) {
        if (typeName == null) {
            throw new IllegalArgumentException("Argument typeName can not be null");
        }
        if (simpleTypeValidator == null) {
            throw new IllegalArgumentException("Argument pattern can not be null");
        }

        String type = getSimplifiedType(typeName);
        if (this.validators.containsKey(type)) {
            return false;
        }

        this.validators.put(type, simpleTypeValidator);
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
        if (this.validators.containsKey(type)) {
            return false;
        }

        this.validators.put(type, StructuredTypeValidator.fromValues(typeNamespaceURI, childrenTypes, this));
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
        if (this.validators.containsKey(type)) {
            return false;
        }

        this.validators.put(type, StructuredTypeWithRestrictedFieldsValidator.fromValues(typeNamespaceURI, childrenTypes, childrenRestrictedTypes, this));
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
                return this.arrayValidators.get(entr.getType()).isCorresponding(node, type.substring(prefix.length()));
            }
        }
        return this.validators.containsKey(type) && this.validators.get(type).isCorresponding(node);
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
        return this.validators.containsKey(type);
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
