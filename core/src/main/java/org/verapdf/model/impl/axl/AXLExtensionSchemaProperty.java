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
package org.verapdf.model.impl.axl;

import org.verapdf.model.tools.xmp.ValidatorsContainer;
import org.verapdf.model.tools.xmp.validators.SimpleTypeValidator;
import org.verapdf.model.xmplayer.ExtensionSchemaProperty;
import org.verapdf.pdfa.flavours.PDFAFlavour;

import com.adobe.xmp.XMPConst;
import com.adobe.xmp.impl.VeraPDFXMPNode;

/**
 * @author Maksim Bezrukov
 */
public class AXLExtensionSchemaProperty extends AXLExtensionSchemaObject implements ExtensionSchemaProperty {

    public static final String EXTENSION_SCHEMA_PROPERTY = "ExtensionSchemaProperty";
    private static final String CATEGORY = "category";
    private static final String DESCRIPTION = "description";
    private static final String NAME = "name";
    private static final String VALUE_TYPE = "valueType";

    public AXLExtensionSchemaProperty(VeraPDFXMPNode xmpNode, ValidatorsContainer containerForPDFA_1, ValidatorsContainer containerForPDFA_2_3, PDFAFlavour flavour) {
        super(EXTENSION_SCHEMA_PROPERTY, xmpNode, containerForPDFA_1, containerForPDFA_2_3, flavour);
    }

    @Override
    public Boolean getcontainsUndefinedFields() {
        boolean undef = false;
        for (VeraPDFXMPNode child : this.xmpNode.getChildren()) {
            if (!undef && XMPConst.NS_PDFA_PROPERTY.equals(child.getNamespaceURI())) {
                switch (child.getName()) {
                    case CATEGORY:
                    case DESCRIPTION:
                    case NAME:
                    case VALUE_TYPE:
                        break;
                    default:
                        undef = true;
                }
            } else {
                undef = true;
                break;
            }
        }
        return Boolean.valueOf(undef);
    }

    @Override
    public String getcategory() {
        for (VeraPDFXMPNode child : this.xmpNode.getChildren()) {
            if (XMPConst.NS_PDFA_PROPERTY.equals(child.getNamespaceURI()) && CATEGORY.equals(child.getName())) {
                return child.getValue();
            }
        }
        return null;
    }

    @Override
    public Boolean getisCategoryValidText() {
        for (VeraPDFXMPNode child : this.xmpNode.getChildren()) {
            if (XMPConst.NS_PDFA_PROPERTY.equals(child.getNamespaceURI()) && CATEGORY.equals(child.getName())) {
                return Boolean.valueOf(SimpleTypeValidator.fromValue(SimpleTypeValidator.SimpleTypeEnum.TEXT).isCorresponding(child));
            }
        }
        return Boolean.TRUE;
    }

    @Override
    public Boolean getisDescriptionValidText() {
        for (VeraPDFXMPNode child : this.xmpNode.getChildren()) {
            if (XMPConst.NS_PDFA_PROPERTY.equals(child.getNamespaceURI()) && DESCRIPTION.equals(child.getName())) {
                return Boolean.valueOf(SimpleTypeValidator.fromValue(SimpleTypeValidator.SimpleTypeEnum.TEXT).isCorresponding(child));
            }
        }
        return Boolean.TRUE;
    }

    @Override
    public Boolean getisNameValidText() {
        for (VeraPDFXMPNode child : this.xmpNode.getChildren()) {
            if (XMPConst.NS_PDFA_PROPERTY.equals(child.getNamespaceURI()) && NAME.equals(child.getName())) {
                return Boolean.valueOf(SimpleTypeValidator.fromValue(SimpleTypeValidator.SimpleTypeEnum.TEXT).isCorresponding(child));
            }
        }
        return Boolean.TRUE;
    }

    @Override
    public Boolean getisValueTypeValidText() {
        for (VeraPDFXMPNode child : this.xmpNode.getChildren()) {
            if (XMPConst.NS_PDFA_PROPERTY.equals(child.getNamespaceURI()) && VALUE_TYPE.equals(child.getName())) {
                return Boolean.valueOf(SimpleTypeValidator.fromValue(SimpleTypeValidator.SimpleTypeEnum.TEXT).isCorresponding(child));
            }
        }
        return Boolean.TRUE;
    }

    @Override
    public Boolean getisValueTypeDefined() {
        if (this.flavour != null && this.flavour.getPart() != null && this.flavour.getPart().getPartNumber() == 1) {
            return isValueTypeValidForPDFA_1();
        }
        return isValueTypeValidForPDFA_2_3();
    }

    private Boolean isValueTypeValidForPDFA_1() {
        for (VeraPDFXMPNode child : this.xmpNode.getChildren()) {
            if (XMPConst.NS_PDFA_PROPERTY.equals(child.getNamespaceURI()) && VALUE_TYPE.equals(child.getName())) {
                return Boolean.valueOf(this.containerForPDFA_1.isKnownType(child.getValue()));
            }
        }
        return Boolean.TRUE;
    }

    private Boolean isValueTypeValidForPDFA_2_3() {
        for (VeraPDFXMPNode child : this.xmpNode.getChildren()) {
            if (XMPConst.NS_PDFA_PROPERTY.equals(child.getNamespaceURI()) && VALUE_TYPE.equals(child.getName())) {
                return Boolean.valueOf(this.containerForPDFA_2_3.isKnownType(child.getValue()));
            }
        }
        return Boolean.TRUE;
    }

    @Override
    public String getcategoryPrefix() {
        for (VeraPDFXMPNode child : this.xmpNode.getChildren()) {
            if (XMPConst.NS_PDFA_PROPERTY.equals(child.getNamespaceURI()) && CATEGORY.equals(child.getName())) {
                return child.getPrefix();
            }
        }
        return null;
    }

    @Override
    public String getdescriptionPrefix() {
        for (VeraPDFXMPNode child : this.xmpNode.getChildren()) {
            if (XMPConst.NS_PDFA_PROPERTY.equals(child.getNamespaceURI()) && DESCRIPTION.equals(child.getName())) {
                return child.getPrefix();
            }
        }
        return null;
    }

    @Override
    public String getnamePrefix() {
        for (VeraPDFXMPNode child : this.xmpNode.getChildren()) {
            if (XMPConst.NS_PDFA_PROPERTY.equals(child.getNamespaceURI()) && NAME.equals(child.getName())) {
                return child.getPrefix();
            }
        }
        return null;
    }

    @Override
    public String getvalueTypePrefix() {
        for (VeraPDFXMPNode child : this.xmpNode.getChildren()) {
            if (XMPConst.NS_PDFA_PROPERTY.equals(child.getNamespaceURI()) && VALUE_TYPE.equals(child.getName())) {
                return child.getPrefix();
            }
        }
        return null;
    }
}
