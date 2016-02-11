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

    private static final String NAMESPACE_URI = "namespaceURI";
    private static final String PREFIX = "prefix";
    private static final String FIELD = "field";
    private static final String DESCRIPTION = "description";
    private static final String TYPE = "type";

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
            if (XMPSchemaRegistryImpl.NS_PDFA_TYPE.equals(child.getNamespaceURI()) && FIELD.equals(child.getName())) {
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
    public Boolean getcontainsUndefinedFields() {
        boolean undef = false;
        for (VeraPDFXMPNode child : this.xmpNode.getChildren()) {
            if (!undef && XMPSchemaRegistryImpl.NS_PDFA_TYPE.equals(child.getNamespaceURI())) {
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
    public Boolean getisDescriptionValidText() {
        for (VeraPDFXMPNode child : this.xmpNode.getChildren()) {
            if (XMPSchemaRegistryImpl.NS_PDFA_TYPE.equals(child.getNamespaceURI()) && DESCRIPTION.equals(child.getName())) {
                return Boolean.valueOf(SimpleTypeValidator.fromValue(SimpleTypeValidator.SimpleTypeEnum.TEXT).isCorresponding(child));
            }
        }
        return Boolean.TRUE;
    }

    @Override
    public Boolean getisFieldValidSeq() {
        for (VeraPDFXMPNode child : this.xmpNode.getChildren()) {
            if (XMPSchemaRegistryImpl.NS_PDFA_TYPE.equals(child.getNamespaceURI()) && FIELD.equals(child.getName())) {
                return Boolean.valueOf(child.getOptions().isArrayOrdered());
            }
        }
        return Boolean.TRUE;
    }

    @Override
    public Boolean getisNamespaceURIValidURI() {
        for (VeraPDFXMPNode child : this.xmpNode.getChildren()) {
            if (XMPSchemaRegistryImpl.NS_PDFA_TYPE.equals(child.getNamespaceURI()) && NAMESPACE_URI.equals(child.getName())) {
                return Boolean.valueOf(new URITypeValidator().isCorresponding(child));
            }
        }
        return Boolean.TRUE;
    }

    @Override
    public Boolean getisPrefixValidText() {
        for (VeraPDFXMPNode child : this.xmpNode.getChildren()) {
            if (XMPSchemaRegistryImpl.NS_PDFA_TYPE.equals(child.getNamespaceURI()) && PREFIX.equals(child.getName())) {
                return Boolean.valueOf(SimpleTypeValidator.fromValue(SimpleTypeValidator.SimpleTypeEnum.TEXT).isCorresponding(child));
            }
        }
        return Boolean.TRUE;
    }

    @Override
    public Boolean getisTypeValidText() {
        for (VeraPDFXMPNode child : this.xmpNode.getChildren()) {
            if (XMPSchemaRegistryImpl.NS_PDFA_TYPE.equals(child.getNamespaceURI()) && TYPE.equals(child.getName())) {
                return Boolean.valueOf(SimpleTypeValidator.fromValue(SimpleTypeValidator.SimpleTypeEnum.TEXT).isCorresponding(child));
            }
        }
        return Boolean.TRUE;
    }

    @Override
    public String getdescriptionPrefix() {
        for (VeraPDFXMPNode child : this.xmpNode.getChildren()) {
            if (XMPSchemaRegistryImpl.NS_PDFA_TYPE.equals(child.getNamespaceURI()) && DESCRIPTION.equals(child.getName())) {
                return child.getPrefix();
            }
        }
        return null;
    }

    @Override
    public String getfieldPrefix() {
        for (VeraPDFXMPNode child : this.xmpNode.getChildren()) {
            if (XMPSchemaRegistryImpl.NS_PDFA_TYPE.equals(child.getNamespaceURI()) && FIELD.equals(child.getName())) {
                return child.getPrefix();
            }
        }
        return null;
    }

    @Override
    public String getnamespaceURIPrefix() {
        for (VeraPDFXMPNode child : this.xmpNode.getChildren()) {
            if (XMPSchemaRegistryImpl.NS_PDFA_TYPE.equals(child.getNamespaceURI()) && NAMESPACE_URI.equals(child.getName())) {
                return child.getPrefix();
            }
        }
        return null;
    }

    @Override
    public String getprefixPrefix() {
        for (VeraPDFXMPNode child : this.xmpNode.getChildren()) {
            if (XMPSchemaRegistryImpl.NS_PDFA_TYPE.equals(child.getNamespaceURI()) && PREFIX.equals(child.getName())) {
                return child.getPrefix();
            }
        }
        return null;
    }

    @Override
    public String gettypePrefix() {
        for (VeraPDFXMPNode child : this.xmpNode.getChildren()) {
            if (XMPSchemaRegistryImpl.NS_PDFA_TYPE.equals(child.getNamespaceURI()) && TYPE.equals(child.getName())) {
                return child.getPrefix();
            }
        }
        return null;
    }
}
