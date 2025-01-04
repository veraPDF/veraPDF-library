/**
 * This file is part of veraPDF Library core, a module of the veraPDF project.
 * Copyright (c) 2015-2025, veraPDF Consortium <info@verapdf.org>
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

import org.verapdf.xmp.impl.VeraPDFXMPNode;

import javax.xml.namespace.QName;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Maksim Bezrukov
 */
public class SchemasDefinition {

    private final Map<QName, String> properties = new HashMap<>();
    private final ValidatorsContainer validator;

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
        return this.properties.containsKey(name);
    }

    /**
     * Checks the node type
     *
     * @param node node for check
     * @return true if the node type corresponds to defined one, false if it is not,
     * null if the node is not defined or value type is not defined
     */
    public Boolean isCorrespondsDefinedType(VeraPDFXMPNode node) {
        if (this.validator == null) {
            return null;
        }
        String type = getType(node);
        return type == null ? null : this.validator.validate(node, type);
    }

    public String getDefinedType(VeraPDFXMPNode node) {
        if (this.validator == null) {
            return null;
        }
        return getType(node);
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

        if (!this.validator.isKnownType(type)) {
            return false;
        }

        QName name = new QName(namespaceURI, propertyName);
        if (this.properties.containsKey(name)) {
            return false;
        }
        this.properties.put(name, type);
        return true;
    }

    private String getType(VeraPDFXMPNode node) {
        QName name = new QName(node.getNamespaceURI(), node.getName());
        return this.properties.get(name);
    }
}
