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

import javax.xml.namespace.QName;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @author Maksim Bezrukov
 */
public class PredefinedSchemasDefinition extends SchemasDefinition {

    private Map<QName, Pattern> restrictedSimpleField = new HashMap<>();
    private Map<QName, Pattern> restrictedSeqText = new HashMap<>();
    private Map<QName, String[][]> closedSeqChoice = new HashMap<>();

    protected PredefinedSchemasDefinition() {
    }

    protected PredefinedSchemasDefinition(ValidatorsContainer validator) {
        super(validator);
    }

    @Override
    protected boolean isDefinedProperty(QName name) {
        return this.restrictedSimpleField.containsKey(name) ||
                this.restrictedSeqText.containsKey(name) ||
                this.closedSeqChoice.containsKey(name) ||
                super.isDefinedProperty(name);
    }

    @Override
    public Boolean isCorrespondsDefinedType(VeraPDFXMPNode node) {
        if (node == null) {
            throw new IllegalArgumentException("Argument node can not be null");
        }

        QName name = new QName(node.getNamespaceURI(), node.getName());
        if (this.restrictedSimpleField.containsKey(name)) {
            return isCorrespondsClosedSimpleChoice(node, this.restrictedSimpleField.get(name));
        } else if (this.restrictedSeqText.containsKey(name)) {
            return isCorrespondsRestrictedSeqText(node, this.restrictedSeqText.get(name));
        } else if (this.closedSeqChoice.containsKey(name)) {
            return isCorrespondsClosedSeqChoice(node, this.closedSeqChoice.get(name));
        } else {
            return super.isCorrespondsDefinedType(node);
        }
    }

    private static Boolean isCorrespondsClosedSimpleChoice(VeraPDFXMPNode node, Pattern p) {
        return Boolean.valueOf(node.getOptions().isSimple() && p.matcher(node.getValue()).matches());
    }

    private static Boolean isCorrespondsRestrictedSeqText(VeraPDFXMPNode node, Pattern p) {
        if (!node.getOptions().isArrayOrdered()) {
            return Boolean.FALSE;
        }
        for (VeraPDFXMPNode child : node.getChildren()) {
            if (!(child.getOptions().isSimple() && p.matcher(child.getValue()).matches())) {
                return Boolean.FALSE;
            }
        }
        return Boolean.TRUE;
    }

    private static Boolean isCorrespondsClosedSeqChoice(VeraPDFXMPNode node, String[][] choices) {
        if (!node.getOptions().isArrayOrdered()) {
            return Boolean.FALSE;
        }
        List<VeraPDFXMPNode> children = node.getChildren();
        for (String[] choice : choices) {
            if (isEqualValues(children, choice)) {
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }

    private static boolean isEqualValues(List<VeraPDFXMPNode> nodes, String[] values) {
        if (nodes.size() != values.length) {
            return false;
        }
        for (int i = 0; i < values.length; ++i) {
            if (!values[i].equals(nodes.get(i).getValue())) {
                return false;
            }
        }
        return true;
    }

    protected boolean registerRestrictedSimpleFieldProperty(String namespaceURI, String propertyName, Pattern pattern) {
        return registerRestrictedPropertyIntoMap(this.restrictedSimpleField, namespaceURI, propertyName, pattern);
    }

    protected boolean registerRestrictedSeqTextProperty(String namespaceURI, String propertyName, Pattern pattern) {
        return registerRestrictedPropertyIntoMap(this.restrictedSeqText, namespaceURI, propertyName, pattern);
    }

    protected boolean registerSeqChoiceProperty(String namespaceURI, String propertyName, String[][] choices) {
        return registerRestrictedPropertyIntoMap(this.closedSeqChoice, namespaceURI, propertyName, choices);
    }

    private <T> boolean registerRestrictedPropertyIntoMap(Map<QName, T> map, String namespaceURI, String propertyName, T value) {
        if (namespaceURI == null) {
            throw new IllegalArgumentException("Argument namespaceURI can not be null");
        }
        if (propertyName == null) {
            throw new IllegalArgumentException("Argument property name can not be null");
        }
        if (value == null) {
            throw new IllegalArgumentException("Argument value can not be null");
        }

        QName name = new QName(namespaceURI, propertyName);
        if (isDefinedProperty(name)) {
            return false;
        }
        map.put(name, value);
        return true;
    }
}
