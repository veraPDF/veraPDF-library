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
package org.verapdf.model.impl.axl;

import org.verapdf.containers.StaticCoreContainers;
import org.verapdf.pdfa.flavours.PDFFlavours;
import org.verapdf.xmp.XMPConst;
import org.verapdf.xmp.impl.VeraPDFXMPNode;
import org.verapdf.model.tools.xmp.ValidatorsContainer;
import org.verapdf.model.tools.xmp.validators.SimpleTypeValidator;
import org.verapdf.model.xmplayer.ExtensionSchemaField;
import org.verapdf.pdfa.flavours.PDFAFlavour;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Maksim Bezrukov
 */
public class AXLExtensionSchemaField extends AXLExtensionSchemaObject implements ExtensionSchemaField {

    public static final String EXTENSION_SCHEMA_FIELD = "ExtensionSchemaField";
    private static final String DESCRIPTION = "description";
    private static final String NAME = "name";
    private static final String VALUE_TYPE = "valueType";
    private static final Set<String> validChildNames = new HashSet<>();

    public AXLExtensionSchemaField(VeraPDFXMPNode xmpNode, ValidatorsContainer containerForPDFA_1, ValidatorsContainer containerForPDFA_2_3) {
        super(EXTENSION_SCHEMA_FIELD, xmpNode, containerForPDFA_1, containerForPDFA_2_3);
    }

    @Override
    public Boolean getisDescriptionValidText() {
        for (VeraPDFXMPNode child : this.xmpNode.getChildren()) {
            if (XMPConst.NS_PDFA_FIELD.equals(child.getNamespaceURI()) && DESCRIPTION.equals(child.getName())) {
                return SimpleTypeValidator.fromValue(SimpleTypeValidator.SimpleTypeEnum.TEXT).isCorresponding(child);
            }
        }
        return Boolean.TRUE;
    }

    @Override
    public Boolean getisNameValidText() {
        for (VeraPDFXMPNode child : this.xmpNode.getChildren()) {
            if (XMPConst.NS_PDFA_FIELD.equals(child.getNamespaceURI()) && NAME.equals(child.getName())) {
                return SimpleTypeValidator.fromValue(SimpleTypeValidator.SimpleTypeEnum.TEXT).isCorresponding(child);
            }
        }
        return Boolean.TRUE;
    }

    @Override
    public Boolean getisValueTypeValidText() {
        for (VeraPDFXMPNode child : this.xmpNode.getChildren()) {
            if (XMPConst.NS_PDFA_FIELD.equals(child.getNamespaceURI()) && VALUE_TYPE.equals(child.getName())) {
                return SimpleTypeValidator.fromValue(SimpleTypeValidator.SimpleTypeEnum.TEXT).isCorresponding(child);
            }
        }
        return Boolean.TRUE;
    }

    @Override
    public Boolean getisValueTypeDefined() {
        if (PDFFlavours.isFlavourPart(StaticCoreContainers.getFlavour(), PDFAFlavour.Specification.ISO_19005_1)) {
            return isValueTypeValidForPDFA_1();
        }
        return isValueTypeValidForPDFA_2_3();
    }

    private Boolean isValueTypeValidForPDFA_1() {
        for (VeraPDFXMPNode child : this.xmpNode.getChildren()) {
            if (XMPConst.NS_PDFA_FIELD.equals(child.getNamespaceURI()) && VALUE_TYPE.equals(child.getName())) {
                return this.containerForPDFA_1.isKnownType(child.getValue());
            }
        }
        return Boolean.TRUE;
    }

    private Boolean isValueTypeValidForPDFA_2_3() {
        for (VeraPDFXMPNode child : this.xmpNode.getChildren()) {
            if (XMPConst.NS_PDFA_FIELD.equals(child.getNamespaceURI()) && VALUE_TYPE.equals(child.getName())) {
                return this.containerForPDFA_2_3.isKnownType(child.getValue());
            }
        }
        return Boolean.TRUE;
    }

    @Override
    public String getdescriptionPrefix() {
        for (VeraPDFXMPNode child : this.xmpNode.getChildren()) {
            if (XMPConst.NS_PDFA_FIELD.equals(child.getNamespaceURI()) && DESCRIPTION.equals(child.getName())) {
                return child.getPrefix();
            }
        }
        return null;
    }

    @Override
    public String getnamePrefix() {
        for (VeraPDFXMPNode child : this.xmpNode.getChildren()) {
            if (XMPConst.NS_PDFA_FIELD.equals(child.getNamespaceURI()) && NAME.equals(child.getName())) {
                return child.getPrefix();
            }
        }
        return null;
    }

    @Override
    public String getvalueTypePrefix() {
        for (VeraPDFXMPNode child : this.xmpNode.getChildren()) {
            if (XMPConst.NS_PDFA_FIELD.equals(child.getNamespaceURI()) && VALUE_TYPE.equals(child.getName())) {
                return child.getPrefix();
            }
        }
        return null;
    }

    @Override
    protected String getValidNamespaceURI() {
        return XMPConst.NS_PDFA_FIELD;
    }

    @Override
    protected Set<String> getValidChildNames() {
        return validChildNames;
    }

    static {
        validChildNames.add(DESCRIPTION);
        validChildNames.add(NAME);
        validChildNames.add(VALUE_TYPE);
    }

}
