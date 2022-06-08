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

import org.verapdf.xmp.XMPConst;
import org.verapdf.xmp.impl.VeraPDFXMPNode;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.tools.xmp.ValidatorsContainer;
import org.verapdf.model.tools.xmp.validators.SimpleTypeValidator;
import org.verapdf.model.tools.xmp.validators.URITypeValidator;
import org.verapdf.model.xmplayer.ExtensionSchemaValueType;
import org.verapdf.pdfa.flavours.PDFAFlavour;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Maksim Bezrukov
 */
public class AXLExtensionSchemaValueType extends AXLExtensionSchemaObject implements ExtensionSchemaValueType {

    public static final String EXTENSION_SCHEMA_VALUE_TYPE = "ExtensionSchemaValueType";

    public static final String EXTENSION_SCHEMA_FIELDS = "ExtensionSchemaFields";

    private static final String NAMESPACE_URI = "namespaceURI";
    private static final String PREFIX = "prefix";
    private static final String FIELD = "field";
    private static final String DESCRIPTION = "description";
    private static final String TYPE = "type";

    public AXLExtensionSchemaValueType(VeraPDFXMPNode xmpNode, ValidatorsContainer containerForPDFA_1, ValidatorsContainer containerForPDFA_2_3, PDFAFlavour flavour) {
        super(EXTENSION_SCHEMA_VALUE_TYPE, xmpNode, containerForPDFA_1, containerForPDFA_2_3, flavour);
    }

    /**
     * @param link name of the link
     * @return List of all objects with link name
     */
    @Override
    public List<? extends Object> getLinkedObjects(String link) {
        switch (link) {
            case EXTENSION_SCHEMA_FIELDS:
                return this.getExtensionSchemaFields();
            default:
                return super.getLinkedObjects(link);
        }
    }

    private List<AXLExtensionSchemaField> getExtensionSchemaFields() {
        if (this.xmpNode != null) {
            List<AXLExtensionSchemaField> res = new ArrayList<>();
            for (VeraPDFXMPNode child : this.xmpNode.getChildren()) {
                if (XMPConst.NS_PDFA_TYPE.equals(child.getNamespaceURI()) && FIELD.equals(child.getName())) {
                    if (child.getOptions().isArray()) {
                        for (VeraPDFXMPNode node : child.getChildren()) {
                            res.add(new AXLExtensionSchemaField(node, this.containerForPDFA_1, this.containerForPDFA_2_3, this.flavour));
                        }
                    }
                    break;
                }
            }
            return Collections.unmodifiableList(res);
        }
        return Collections.emptyList();
    }

    @Override
    public Boolean getcontainsUndefinedFields() {
        boolean undef = false;
        for (VeraPDFXMPNode child : this.xmpNode.getChildren()) {
            if (!undef && XMPConst.NS_PDFA_TYPE.equals(child.getNamespaceURI())) {
                switch (child.getName()) {
                    case NAMESPACE_URI:
                    case PREFIX:
                    case FIELD:
                    case DESCRIPTION:
                    case TYPE:
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
    public String getundefinedFields() {
        List<String> undefinedFields = new LinkedList<>();
        for (VeraPDFXMPNode child : this.xmpNode.getChildren()) {
            if (XMPConst.NS_PDFA_TYPE.equals(child.getNamespaceURI())) {
                switch (child.getName()) {
                    case NAMESPACE_URI:
                    case PREFIX:
                    case FIELD:
                    case DESCRIPTION:
                    case TYPE:
                        break;
                    default:
                        undefinedFields.add(child.getNamespaceURI() + ":" + child.getName());
                }
            } else {
                undefinedFields.add(child.getNamespaceURI() + ":" + child.getName());
            }
        }
        return String.join(",", undefinedFields);
    }

    @Override
    public Boolean getisDescriptionValidText() {
        for (VeraPDFXMPNode child : this.xmpNode.getChildren()) {
            if (XMPConst.NS_PDFA_TYPE.equals(child.getNamespaceURI()) && DESCRIPTION.equals(child.getName())) {
                return Boolean.valueOf(SimpleTypeValidator.fromValue(SimpleTypeValidator.SimpleTypeEnum.TEXT).isCorresponding(child));
            }
        }
        return Boolean.TRUE;
    }

    @Override
    public Boolean getisFieldValidSeq() {
        for (VeraPDFXMPNode child : this.xmpNode.getChildren()) {
            if (XMPConst.NS_PDFA_TYPE.equals(child.getNamespaceURI()) && FIELD.equals(child.getName())) {
                return Boolean.valueOf(child.getOptions().isArrayOrdered());
            }
        }
        return Boolean.TRUE;
    }

    @Override
    public Boolean getisNamespaceURIValidURI() {
        for (VeraPDFXMPNode child : this.xmpNode.getChildren()) {
            if (XMPConst.NS_PDFA_TYPE.equals(child.getNamespaceURI()) && NAMESPACE_URI.equals(child.getName())) {
                return Boolean.valueOf(new URITypeValidator().isCorresponding(child));
            }
        }
        return Boolean.TRUE;
    }

    @Override
    public Boolean getisPrefixValidText() {
        for (VeraPDFXMPNode child : this.xmpNode.getChildren()) {
            if (XMPConst.NS_PDFA_TYPE.equals(child.getNamespaceURI()) && PREFIX.equals(child.getName())) {
                return Boolean.valueOf(SimpleTypeValidator.fromValue(SimpleTypeValidator.SimpleTypeEnum.TEXT).isCorresponding(child));
            }
        }
        return Boolean.TRUE;
    }

    @Override
    public Boolean getisTypeValidText() {
        for (VeraPDFXMPNode child : this.xmpNode.getChildren()) {
            if (XMPConst.NS_PDFA_TYPE.equals(child.getNamespaceURI()) && TYPE.equals(child.getName())) {
                return Boolean.valueOf(SimpleTypeValidator.fromValue(SimpleTypeValidator.SimpleTypeEnum.TEXT).isCorresponding(child));
            }
        }
        return Boolean.TRUE;
    }

    @Override
    public String getdescriptionPrefix() {
        for (VeraPDFXMPNode child : this.xmpNode.getChildren()) {
            if (XMPConst.NS_PDFA_TYPE.equals(child.getNamespaceURI()) && DESCRIPTION.equals(child.getName())) {
                return child.getPrefix();
            }
        }
        return null;
    }

    @Override
    public String getfieldPrefix() {
        for (VeraPDFXMPNode child : this.xmpNode.getChildren()) {
            if (XMPConst.NS_PDFA_TYPE.equals(child.getNamespaceURI()) && FIELD.equals(child.getName())) {
                return child.getPrefix();
            }
        }
        return null;
    }

    @Override
    public String getnamespaceURIPrefix() {
        for (VeraPDFXMPNode child : this.xmpNode.getChildren()) {
            if (XMPConst.NS_PDFA_TYPE.equals(child.getNamespaceURI()) && NAMESPACE_URI.equals(child.getName())) {
                return child.getPrefix();
            }
        }
        return null;
    }

    @Override
    public String getprefixPrefix() {
        for (VeraPDFXMPNode child : this.xmpNode.getChildren()) {
            if (XMPConst.NS_PDFA_TYPE.equals(child.getNamespaceURI()) && PREFIX.equals(child.getName())) {
                return child.getPrefix();
            }
        }
        return null;
    }

    @Override
    public String gettypePrefix() {
        for (VeraPDFXMPNode child : this.xmpNode.getChildren()) {
            if (XMPConst.NS_PDFA_TYPE.equals(child.getNamespaceURI()) && TYPE.equals(child.getName())) {
                return child.getPrefix();
            }
        }
        return null;
    }
}
