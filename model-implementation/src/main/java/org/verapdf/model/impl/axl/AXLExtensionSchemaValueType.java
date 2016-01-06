package org.verapdf.model.impl.axl;

import com.adobe.xmp.impl.VeraPDFXMPNode;
import com.adobe.xmp.impl.XMPSchemaRegistryImpl;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.tools.xmp.ValidatorsContainer;
import org.verapdf.model.tools.xmp.validators.SimpleTypeValidator;
import org.verapdf.model.tools.xmp.validators.URITypeValidator;
import org.verapdf.model.xmplayer.ExtensionSchemaValueType;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Maksim Bezrukov
 */
public class AXLExtensionSchemaValueType extends AXLExtensionSchemaObject implements ExtensionSchemaValueType {

    public static final String EXTENSION_SCHEMA_VALUE_TYPE = "ExtensionSchemaValueType";

    public static final String EXTENSION_SCHEMA_FIELDS = "ExtensionSchemaFields";

    public AXLExtensionSchemaValueType(VeraPDFXMPNode xmpNode, ValidatorsContainer containerForPDFA_1, ValidatorsContainer containerForPDFA_2_3) {
        super(EXTENSION_SCHEMA_VALUE_TYPE, xmpNode, containerForPDFA_1, containerForPDFA_2_3);
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
        List<AXLExtensionSchemaField> res = new ArrayList<>();
        if (this.xmpNode == null) {
            return res;
        }
        for (VeraPDFXMPNode child : this.xmpNode.getChildren()) {
            if (XMPSchemaRegistryImpl.NS_PDFA_TYPE.equals(child.getNamespaceURI()) && "field".equals(child.getName())) {
                if (child.getOptions().isArray()) {
                    for (VeraPDFXMPNode node : child.getChildren()) {
                        res.add(new AXLExtensionSchemaField(node, containerForPDFA_1, containerForPDFA_2_3));
                    }
                }
                break;
            }
        }
        return res;
    }

    @Override
    public Boolean getisValueTypeCorrect() {
        boolean isValid = true;
        boolean isDescriptionPresent = false;
        boolean isFieldPresent = false;
        boolean isNamespaceURIPresent = false;
        boolean isPrefixPresent = false;
        boolean isTypePresent = false;
        for (VeraPDFXMPNode child : this.xmpNode.getChildren()) {
            if (XMPSchemaRegistryImpl.NS_PDFA_TYPE.equals(child.getNamespaceURI())) {
                switch (child.getName()) {
                    case "namespaceURI":
                        if (isNamespaceURIPresent) {
                            isValid = false;
                        } else {
                            isNamespaceURIPresent = true;
                        }
                        break;
                    case "prefix":
                        if (isPrefixPresent) {
                            isValid = false;
                        } else {
                            isPrefixPresent = true;
                        }
                        break;
                    case "field":
                        if (isFieldPresent) {
                            isValid = false;
                        } else {
                            isFieldPresent = true;
                        }
                        break;
                    case "description":
                        if (isDescriptionPresent) {
                            isValid = false;
                        } else {
                            isDescriptionPresent = true;
                        }
                        break;
                    case "type":
                        if (isTypePresent) {
                            isValid = false;
                        } else {
                            isTypePresent = true;
                        }
                        break;
                    default:
                        isValid = false;
                }
            } else {
                isValid = false;
            }
        }
        return Boolean.valueOf(isValid);
    }

    @Override
    public Boolean getisDescriptionValid() {
        for (VeraPDFXMPNode child : this.xmpNode.getChildren()) {
            if (XMPSchemaRegistryImpl.NS_PDFA_TYPE.equals(child.getNamespaceURI()) && "description".equals(child.getName())) {
                return Boolean.valueOf(SimpleTypeValidator.fromValue(SimpleTypeValidator.SimpleTypeEnum.TEXT).isCorresponding(child));
            }
        }
        return Boolean.TRUE;
    }

    @Override
    public Boolean getisFieldValid() {
        for (VeraPDFXMPNode child : this.xmpNode.getChildren()) {
            if (XMPSchemaRegistryImpl.NS_PDFA_TYPE.equals(child.getNamespaceURI()) && "field".equals(child.getName())) {
                return Boolean.valueOf(child.getOptions().isArrayOrdered());
            }
        }
        return Boolean.TRUE;
    }

    @Override
    public Boolean getisNamespaceURIValid() {
        for (VeraPDFXMPNode child : this.xmpNode.getChildren()) {
            if (XMPSchemaRegistryImpl.NS_PDFA_TYPE.equals(child.getNamespaceURI()) && "namespaceURI".equals(child.getName())) {
                return Boolean.valueOf(new URITypeValidator().isCorresponding(child));
            }
        }
        return Boolean.TRUE;
    }

    @Override
    public Boolean getisPrefixValid() {
        for (VeraPDFXMPNode child : this.xmpNode.getChildren()) {
            if (XMPSchemaRegistryImpl.NS_PDFA_TYPE.equals(child.getNamespaceURI()) && "prefix".equals(child.getName())) {
                return Boolean.valueOf(SimpleTypeValidator.fromValue(SimpleTypeValidator.SimpleTypeEnum.TEXT).isCorresponding(child));
            }
        }
        return Boolean.TRUE;
    }

    @Override
    public Boolean getisTypeValid() {
        for (VeraPDFXMPNode child : this.xmpNode.getChildren()) {
            if (XMPSchemaRegistryImpl.NS_PDFA_TYPE.equals(child.getNamespaceURI()) && "type".equals(child.getName())) {
                return Boolean.valueOf(SimpleTypeValidator.fromValue(SimpleTypeValidator.SimpleTypeEnum.TEXT).isCorresponding(child));
            }
        }
        return Boolean.TRUE;
    }

    @Override
    public String getdescriptionPrefix() {
        for (VeraPDFXMPNode child : this.xmpNode.getChildren()) {
            if (XMPSchemaRegistryImpl.NS_PDFA_TYPE.equals(child.getNamespaceURI()) && "description".equals(child.getName())) {
                return child.getPrefix();
            }
        }
        return null;
    }

    @Override
    public String getfieldPrefix() {
        for (VeraPDFXMPNode child : this.xmpNode.getChildren()) {
            if (XMPSchemaRegistryImpl.NS_PDFA_TYPE.equals(child.getNamespaceURI()) && "field".equals(child.getName())) {
                return child.getPrefix();
            }
        }
        return null;
    }

    @Override
    public String getnamespaceURIPrefix() {
        for (VeraPDFXMPNode child : this.xmpNode.getChildren()) {
            if (XMPSchemaRegistryImpl.NS_PDFA_TYPE.equals(child.getNamespaceURI()) && "namespaceURI".equals(child.getName())) {
                return child.getPrefix();
            }
        }
        return null;
    }

    @Override
    public String getprefixPrefix() {
        for (VeraPDFXMPNode child : this.xmpNode.getChildren()) {
            if (XMPSchemaRegistryImpl.NS_PDFA_TYPE.equals(child.getNamespaceURI()) && "prefix".equals(child.getName())) {
                return child.getPrefix();
            }
        }
        return null;
    }

    @Override
    public String gettypePrefix() {
        for (VeraPDFXMPNode child : this.xmpNode.getChildren()) {
            if (XMPSchemaRegistryImpl.NS_PDFA_TYPE.equals(child.getNamespaceURI()) && "type".equals(child.getName())) {
                return child.getPrefix();
            }
        }
        return null;
    }
}
