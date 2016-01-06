package org.verapdf.model.impl.axl;

import com.adobe.xmp.impl.VeraPDFXMPNode;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.xmplayer.ExtensionSchemaDefinition;

import java.util.List;

/**
 * @author Maksim Bezrukov
 */
public class AXLExtensionSchemaDefinition extends AXLExtensionSchemaObject implements ExtensionSchemaDefinition {

    public static final String EXTENSION_SCHEMA_DEFINITION = "ExtensionSchemaDefinition";

    public static final String EXTENSION_SCHEMA_PROPERTIES = "ExtensionSchemaProperties";
    public static final String EXTENSION_SCHEMA_VALUE_TYPES = "ExtensionSchemaValueTypes";

    public AXLExtensionSchemaDefinition(VeraPDFXMPNode xmpNode) {
        super(EXTENSION_SCHEMA_DEFINITION, xmpNode);
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
        //TODO: Implement
        return null;
    }

    private List<AXLExtensionSchemaProperty> getExtensionSchemaProperties() {
        //TODO: Implement
        return null;
    }

    @Override
    public Boolean getisValueTypeCorrect() {
        //TODO: Implement
        return null;
    }

    @Override
    public Boolean getisNamespaceURIValid() {
        //TODO: Implement
        return null;
    }

    @Override
    public Boolean getisPrefixValid() {
        //TODO: Implement
        return null;
    }

    @Override
    public Boolean getisPropertyValid() {
        //TODO: Implement
        return null;
    }

    @Override
    public Boolean getisSchemaValid() {
        //TODO: Implement
        return null;
    }

    @Override
    public Boolean getisValueTypeValid() {
        //TODO: Implement
        return null;
    }

    @Override
    public String getnamespaceURIPrefix() {
        //TODO: Implement
        return null;
    }

    @Override
    public String getprefixPrefix() {
        //TODO: Implement
        return null;
    }

    @Override
    public String getpropertyPrefix() {
        //TODO: Implement
        return null;
    }

    @Override
    public String getschemaPrefix() {
        //TODO: Implement
        return null;
    }

    @Override
    public String getvalueTypePrefix() {
        //TODO: Implement
        return null;
    }
}
