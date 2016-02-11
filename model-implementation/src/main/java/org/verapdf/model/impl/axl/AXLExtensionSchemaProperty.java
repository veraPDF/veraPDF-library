package org.verapdf.model.impl.axl;

import com.adobe.xmp.impl.VeraPDFXMPNode;
import com.adobe.xmp.impl.XMPSchemaRegistryImpl;
import org.verapdf.model.tools.xmp.ValidatorsContainer;
import org.verapdf.model.tools.xmp.validators.SimpleTypeValidator;
import org.verapdf.model.xmplayer.ExtensionSchemaProperty;

/**
 * @author Maksim Bezrukov
 */
public class AXLExtensionSchemaProperty extends AXLExtensionSchemaObject implements ExtensionSchemaProperty {

    public static final String EXTENSION_SCHEMA_PROPERTY = "ExtensionSchemaProperty";
    private static final String CATEGORY = "category";
    private static final String DESCRIPTION = "description";
    private static final String NAME = "name";
    private static final String VALUE_TYPE = "valueType";

    private final boolean isPDFA1Validation = true;

    public AXLExtensionSchemaProperty(VeraPDFXMPNode xmpNode, ValidatorsContainer containerForPDFA_1, ValidatorsContainer containerForPDFA_2_3) {
        super(EXTENSION_SCHEMA_PROPERTY, xmpNode, containerForPDFA_1, containerForPDFA_2_3);
    }

    @Override
    public Boolean getcontainsUndefinedFields() {
        boolean undef = false;
        for (VeraPDFXMPNode child : this.xmpNode.getChildren()) {
            if (!undef && XMPSchemaRegistryImpl.NS_PDFA_PROPERTY.equals(child.getNamespaceURI())) {
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
            if (XMPSchemaRegistryImpl.NS_PDFA_PROPERTY.equals(child.getNamespaceURI()) && CATEGORY.equals(child.getName())) {
                return child.getValue();
            }
        }
        return null;
    }

    @Override
    public Boolean getisCategoryValidText() {
        for (VeraPDFXMPNode child : this.xmpNode.getChildren()) {
            if (XMPSchemaRegistryImpl.NS_PDFA_PROPERTY.equals(child.getNamespaceURI()) && CATEGORY.equals(child.getName())) {
                return Boolean.valueOf(SimpleTypeValidator.fromValue(SimpleTypeValidator.SimpleTypeEnum.TEXT).isCorresponding(child));
            }
        }
        return Boolean.TRUE;
    }

    @Override
    public Boolean getisDescriptionValidText() {
        for (VeraPDFXMPNode child : this.xmpNode.getChildren()) {
            if (XMPSchemaRegistryImpl.NS_PDFA_PROPERTY.equals(child.getNamespaceURI()) && DESCRIPTION.equals(child.getName())) {
                return Boolean.valueOf(SimpleTypeValidator.fromValue(SimpleTypeValidator.SimpleTypeEnum.TEXT).isCorresponding(child));
            }
        }
        return Boolean.TRUE;
    }

    @Override
    public Boolean getisNameValidText() {
        for (VeraPDFXMPNode child : this.xmpNode.getChildren()) {
            if (XMPSchemaRegistryImpl.NS_PDFA_PROPERTY.equals(child.getNamespaceURI()) && NAME.equals(child.getName())) {
                return Boolean.valueOf(SimpleTypeValidator.fromValue(SimpleTypeValidator.SimpleTypeEnum.TEXT).isCorresponding(child));
            }
        }
        return Boolean.TRUE;
    }

    @Override
    public Boolean getisValueTypeValidText() {
        for (VeraPDFXMPNode child : this.xmpNode.getChildren()) {
            if (XMPSchemaRegistryImpl.NS_PDFA_PROPERTY.equals(child.getNamespaceURI()) && VALUE_TYPE.equals(child.getName())) {
                return Boolean.valueOf(SimpleTypeValidator.fromValue(SimpleTypeValidator.SimpleTypeEnum.TEXT).isCorresponding(child));
            }
        }
        return Boolean.TRUE;
    }

    @Override
    public Boolean getisValueTypeDefined() {
        if (isPDFA1Validation) {
            return isValueTypeValidForPDFA_1();
        } else {
            return isValueTypeValidForPDFA_2_3();
        }
    }

    private Boolean isValueTypeValidForPDFA_1() {
        for (VeraPDFXMPNode child : this.xmpNode.getChildren()) {
            if (XMPSchemaRegistryImpl.NS_PDFA_PROPERTY.equals(child.getNamespaceURI()) && VALUE_TYPE.equals(child.getName())) {
                return containerForPDFA_1.isKnownType(child.getValue());
            }
        }
        return Boolean.TRUE;
    }

    private Boolean isValueTypeValidForPDFA_2_3() {
        for (VeraPDFXMPNode child : this.xmpNode.getChildren()) {
            if (XMPSchemaRegistryImpl.NS_PDFA_PROPERTY.equals(child.getNamespaceURI()) && VALUE_TYPE.equals(child.getName())) {
                return containerForPDFA_2_3.isKnownType(child.getValue());
            }
        }
        return Boolean.TRUE;
    }

    @Override
    public String getcategoryPrefix() {
        for (VeraPDFXMPNode child : this.xmpNode.getChildren()) {
            if (XMPSchemaRegistryImpl.NS_PDFA_PROPERTY.equals(child.getNamespaceURI()) && CATEGORY.equals(child.getName())) {
                return child.getPrefix();
            }
        }
        return null;
    }

    @Override
    public String getdescriptionPrefix() {
        for (VeraPDFXMPNode child : this.xmpNode.getChildren()) {
            if (XMPSchemaRegistryImpl.NS_PDFA_PROPERTY.equals(child.getNamespaceURI()) && DESCRIPTION.equals(child.getName())) {
                return child.getPrefix();
            }
        }
        return null;
    }

    @Override
    public String getnamePrefix() {
        for (VeraPDFXMPNode child : this.xmpNode.getChildren()) {
            if (XMPSchemaRegistryImpl.NS_PDFA_PROPERTY.equals(child.getNamespaceURI()) && NAME.equals(child.getName())) {
                return child.getPrefix();
            }
        }
        return null;
    }

    @Override
    public String getvalueTypePrefix() {
        for (VeraPDFXMPNode child : this.xmpNode.getChildren()) {
            if (XMPSchemaRegistryImpl.NS_PDFA_PROPERTY.equals(child.getNamespaceURI()) && VALUE_TYPE.equals(child.getName())) {
                return child.getPrefix();
            }
        }
        return null;
    }
}
