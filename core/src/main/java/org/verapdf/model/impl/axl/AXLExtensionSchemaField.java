package org.verapdf.model.impl.axl;

import org.verapdf.model.tools.xmp.ValidatorsContainer;
import org.verapdf.model.tools.xmp.validators.SimpleTypeValidator;
import org.verapdf.model.xmplayer.ExtensionSchemaField;
import org.verapdf.pdfa.flavours.PDFAFlavour;

import com.adobe.xmp.XMPConst;
import com.adobe.xmp.impl.VeraPDFXMPNode;

/**
 * @author Maksim Bezrukov
 */
public class AXLExtensionSchemaField extends AXLExtensionSchemaObject implements ExtensionSchemaField {

    public static final String EXTENSION_SCHEMA_FIELD = "ExtensionSchemaField";
    private static final String DESCRIPTION = "description";
    private static final String NAME = "name";
    private static final String VALUE_TYPE = "valueType";

    public AXLExtensionSchemaField(VeraPDFXMPNode xmpNode, ValidatorsContainer containerForPDFA_1, ValidatorsContainer containerForPDFA_2_3, PDFAFlavour flavour) {
        super(EXTENSION_SCHEMA_FIELD, xmpNode, containerForPDFA_1, containerForPDFA_2_3, flavour);
    }

    @Override
    public Boolean getcontainsUndefinedFields() {
        boolean undef = false;
        for (VeraPDFXMPNode child : this.xmpNode.getChildren()) {
            if (!undef && XMPConst.NS_PDFA_FIELD.equals(child.getNamespaceURI())) {
                switch (child.getName()) {
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
    public Boolean getisDescriptionValidText() {
        for (VeraPDFXMPNode child : this.xmpNode.getChildren()) {
            if (XMPConst.NS_PDFA_FIELD.equals(child.getNamespaceURI()) && DESCRIPTION.equals(child.getName())) {
                return Boolean.valueOf(SimpleTypeValidator.fromValue(SimpleTypeValidator.SimpleTypeEnum.TEXT).isCorresponding(child));
            }
        }
        return Boolean.TRUE;
    }

    @Override
    public Boolean getisNameValidText() {
        for (VeraPDFXMPNode child : this.xmpNode.getChildren()) {
            if (XMPConst.NS_PDFA_FIELD.equals(child.getNamespaceURI()) && NAME.equals(child.getName())) {
                return Boolean.valueOf(SimpleTypeValidator.fromValue(SimpleTypeValidator.SimpleTypeEnum.TEXT).isCorresponding(child));
            }
        }
        return Boolean.TRUE;
    }

    @Override
    public Boolean getisValueTypeValidText() {
        for (VeraPDFXMPNode child : this.xmpNode.getChildren()) {
            if (XMPConst.NS_PDFA_FIELD.equals(child.getNamespaceURI()) && VALUE_TYPE.equals(child.getName())) {
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
            if (XMPConst.NS_PDFA_FIELD.equals(child.getNamespaceURI()) && VALUE_TYPE.equals(child.getName())) {
                return Boolean.valueOf(this.containerForPDFA_1.isKnownType(child.getValue()));
            }
        }
        return Boolean.TRUE;
    }

    private Boolean isValueTypeValidForPDFA_2_3() {
        for (VeraPDFXMPNode child : this.xmpNode.getChildren()) {
            if (XMPConst.NS_PDFA_FIELD.equals(child.getNamespaceURI()) && VALUE_TYPE.equals(child.getName())) {
                return Boolean.valueOf(this.containerForPDFA_2_3.isKnownType(child.getValue()));
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
}
