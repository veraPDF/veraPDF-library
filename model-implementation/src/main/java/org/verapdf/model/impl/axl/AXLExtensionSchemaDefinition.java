package org.verapdf.model.impl.axl;

import com.adobe.xmp.impl.VeraPDFXMPNode;
import com.adobe.xmp.impl.XMPSchemaRegistryImpl;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.tools.xmp.ValidatorsContainer;
import org.verapdf.model.tools.xmp.validators.SimpleTypeValidator;
import org.verapdf.model.tools.xmp.validators.URITypeValidator;
import org.verapdf.model.xmplayer.ExtensionSchemaDefinition;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Maksim Bezrukov
 */
public class AXLExtensionSchemaDefinition extends AXLExtensionSchemaObject implements ExtensionSchemaDefinition {

    public static final String EXTENSION_SCHEMA_DEFINITION = "ExtensionSchemaDefinition";

    public static final String EXTENSION_SCHEMA_PROPERTIES = "ExtensionSchemaProperties";
    public static final String EXTENSION_SCHEMA_VALUE_TYPES = "ExtensionSchemaValueTypes";

    public AXLExtensionSchemaDefinition(VeraPDFXMPNode xmpNode, ValidatorsContainer containerForPDFA_1, ValidatorsContainer containerForPDFA_2_3) {
        super(EXTENSION_SCHEMA_DEFINITION, xmpNode, containerForPDFA_1, containerForPDFA_2_3);
    }

    /**
     * @param link name of the link
     * @return List of all objects with link name
     */
    @Override
    public List<? extends Object> getLinkedObjects(String link) {
        switch (link) {
            case EXTENSION_SCHEMA_PROPERTIES:
                return this.getExtensionSchemaProperties();
            case EXTENSION_SCHEMA_VALUE_TYPES:
                return this.getExtensionSchemaValueType();
            default:
                return super.getLinkedObjects(link);
        }
    }

    private List<AXLExtensionSchemaValueType> getExtensionSchemaValueType() {
        List<AXLExtensionSchemaValueType> res = new ArrayList<>();
        if (this.xmpNode == null) {
            return res;
        }
        for (VeraPDFXMPNode child : this.xmpNode.getChildren()) {
            if (XMPSchemaRegistryImpl.NS_PDFA_SCHEMA.equals(child.getNamespaceURI()) && "valueType".equals(child.getName())) {
                if (child.getOptions().isArray()) {
                    for (VeraPDFXMPNode node : child.getChildren()) {
                        res.add(new AXLExtensionSchemaValueType(node, containerForPDFA_1, containerForPDFA_2_3));
                    }
                }
                break;
            }
        }
        return res;
    }

    private List<AXLExtensionSchemaProperty> getExtensionSchemaProperties() {
        List<AXLExtensionSchemaProperty> res = new ArrayList<>();
        if (this.xmpNode == null) {
            return res;
        }
        for (VeraPDFXMPNode child : this.xmpNode.getChildren()) {
            if (XMPSchemaRegistryImpl.NS_PDFA_SCHEMA.equals(child.getNamespaceURI()) && "property".equals(child.getName())) {
                if (child.getOptions().isArray()) {
                    for (VeraPDFXMPNode node : child.getChildren()) {
                        res.add(new AXLExtensionSchemaProperty(node, containerForPDFA_1, containerForPDFA_2_3));
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
        boolean isNamespacePresent = false;
        boolean isPrefixPresent = false;
        boolean isPropertyPresent = false;
        boolean isSchemaPresent = false;
        boolean isValueTypePresent = false;
        for (VeraPDFXMPNode child : this.xmpNode.getChildren()) {
            if (XMPSchemaRegistryImpl.NS_PDFA_SCHEMA.equals(child.getNamespaceURI())) {
                switch (child.getName()) {
                    case "namespaceURI":
                        if (isNamespacePresent) {
                            isValid = false;
                        } else {
                            isNamespacePresent = true;
                        }
                        break;
                    case "prefix":
                        if (isPrefixPresent) {
                            isValid = false;
                        } else {
                            isPrefixPresent = true;
                        }
                        break;
                    case "property":
                        if (isPropertyPresent) {
                            isValid = false;
                        } else {
                            isPropertyPresent = true;
                        }
                        break;
                    case "schema":
                        if (isSchemaPresent) {
                            isValid = false;
                        } else {
                            isSchemaPresent = true;
                        }
                        break;
                    case "valueType":
                        if (isValueTypePresent) {
                            isValid = false;
                        } else {
                            isValueTypePresent = true;
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
    public Boolean getisNamespaceURIValid() {
        for (VeraPDFXMPNode child : this.xmpNode.getChildren()) {
            if (XMPSchemaRegistryImpl.NS_PDFA_SCHEMA.equals(child.getNamespaceURI()) && "namespaceURI".equals(child.getName())) {
                return Boolean.valueOf(new URITypeValidator().isCorresponding(child));
            }
        }
        return Boolean.TRUE;
    }

    @Override
    public Boolean getisPrefixValid() {
        for (VeraPDFXMPNode child : this.xmpNode.getChildren()) {
            if (XMPSchemaRegistryImpl.NS_PDFA_SCHEMA.equals(child.getNamespaceURI()) && "prefix".equals(child.getName())) {
                return Boolean.valueOf(SimpleTypeValidator.fromValue(SimpleTypeValidator.SimpleTypeEnum.TEXT).isCorresponding(child));
            }
        }
        return Boolean.TRUE;
    }

    @Override
    public Boolean getisPropertyValid() {
        for (VeraPDFXMPNode child : this.xmpNode.getChildren()) {
            if (XMPSchemaRegistryImpl.NS_PDFA_SCHEMA.equals(child.getNamespaceURI()) && "property".equals(child.getName())) {
                return Boolean.valueOf(child.getOptions().isArrayOrdered());
            }
        }
        return Boolean.TRUE;
    }

    @Override
    public Boolean getisSchemaValid() {
        for (VeraPDFXMPNode child : this.xmpNode.getChildren()) {
            if (XMPSchemaRegistryImpl.NS_PDFA_SCHEMA.equals(child.getNamespaceURI()) && "schema".equals(child.getName())) {
                return Boolean.valueOf(SimpleTypeValidator.fromValue(SimpleTypeValidator.SimpleTypeEnum.TEXT).isCorresponding(child));
            }
        }
        return Boolean.TRUE;
    }

    @Override
    public Boolean getisValueTypeValid() {
        for (VeraPDFXMPNode child : this.xmpNode.getChildren()) {
            if (XMPSchemaRegistryImpl.NS_PDFA_SCHEMA.equals(child.getNamespaceURI()) && "valueType".equals(child.getName())) {
                return Boolean.valueOf(child.getOptions().isArrayOrdered());
            }
        }
        return Boolean.TRUE;
    }

    @Override
    public String getnamespaceURIPrefix() {
        for (VeraPDFXMPNode child : this.xmpNode.getChildren()) {
            if (XMPSchemaRegistryImpl.NS_PDFA_SCHEMA.equals(child.getNamespaceURI()) && "namespaceURI".equals(child.getName())) {
                return child.getPrefix();
            }
        }
        return null;
    }

    @Override
    public String getprefixPrefix() {
        for (VeraPDFXMPNode child : this.xmpNode.getChildren()) {
            if (XMPSchemaRegistryImpl.NS_PDFA_SCHEMA.equals(child.getNamespaceURI()) && "prefix".equals(child.getName())) {
                return child.getPrefix();
            }
        }
        return null;
    }

    @Override
    public String getpropertyPrefix() {
        for (VeraPDFXMPNode child : this.xmpNode.getChildren()) {
            if (XMPSchemaRegistryImpl.NS_PDFA_SCHEMA.equals(child.getNamespaceURI()) && "property".equals(child.getName())) {
                return child.getPrefix();
            }
        }
        return null;
    }

    @Override
    public String getschemaPrefix() {
        for (VeraPDFXMPNode child : this.xmpNode.getChildren()) {
            if (XMPSchemaRegistryImpl.NS_PDFA_SCHEMA.equals(child.getNamespaceURI()) && "schema".equals(child.getName())) {
                return child.getPrefix();
            }
        }
        return null;
    }

    @Override
    public String getvalueTypePrefix() {
        for (VeraPDFXMPNode child : this.xmpNode.getChildren()) {
            if (XMPSchemaRegistryImpl.NS_PDFA_SCHEMA.equals(child.getNamespaceURI()) && "valueType".equals(child.getName())) {
                return child.getPrefix();
            }
        }
        return null;
    }
}
