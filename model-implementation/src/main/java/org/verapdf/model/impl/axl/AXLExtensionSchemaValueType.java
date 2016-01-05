package org.verapdf.model.impl.axl;

import com.adobe.xmp.impl.VeraPDFXMPNode;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.xmplayer.ExtensionSchemaValueType;

import java.util.List;

/**
 * @author Maksim Bezrukov
 */
public class AXLExtensionSchemaValueType extends AXLExtensionSchemaObject implements ExtensionSchemaValueType {

    public static final String EXTENSION_SCHEMA_VALUE_TYPE = "ExtensionSchemaValueType";

    public static final String EXTENSION_SCHEMA_FIELDS = "ExtensionSchemaFields";

    private final List<String> currentValueTypes;
    private final List<String> mainPackageValueTypes;

    public AXLExtensionSchemaValueType(VeraPDFXMPNode xmpNode, List<String> currentValueTypes, List<String> mainPackageValueTypes) {
        super(EXTENSION_SCHEMA_VALUE_TYPE, xmpNode);
        this.currentValueTypes = currentValueTypes;
        this.mainPackageValueTypes = mainPackageValueTypes;
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
        //TODO: Implement
        return null;
    }

    @Override
    public Boolean getisValueTypeCorrect() {
        //TODO: Implement
        return null;
    }

    @Override
    public Boolean getisDescriptionValid() {
        //TODO: Implement
        return null;
    }

    @Override
    public Boolean getisFieldValid() {
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
    public Boolean getisTypeValid() {
        //TODO: Implement
        return null;
    }

    @Override
    public String getdescriptionPrefix() {
        //TODO: Implement
        return null;
    }

    @Override
    public String getfieldPrefix() {
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
    public String gettypePrefix() {
        //TODO: Implement
        return null;
    }
}
