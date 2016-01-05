package org.verapdf.model.impl.axl;

import com.adobe.xmp.impl.VeraPDFXMPNode;
import org.verapdf.model.xmplayer.ExtensionSchemaProperty;

import java.util.List;

/**
 * @author Maksim Bezrukov
 */
public class AXLExtensionSchemaProperty extends AXLExtensionSchemaObject implements ExtensionSchemaProperty {

    public static final String EXTENSION_SCHEMA_PROPERTY = "ExtensionSchemaProperty";

    private final List<String> currentValueTypes;
    private final List<String> mainPackageValueTypes;

    public AXLExtensionSchemaProperty(VeraPDFXMPNode xmpNode, List<String> currentValueTypes, List<String> mainPackageValueTypes) {
        super(EXTENSION_SCHEMA_PROPERTY, xmpNode);
        this.currentValueTypes = currentValueTypes;
        this.mainPackageValueTypes = mainPackageValueTypes;
    }

    @Override
    public Boolean getisValueTypeCorrect() {
        //TODO: Implement
        return null;
    }

    @Override
    public Boolean getisCategoryValid() {
        //TODO: Implement
        return null;
    }

    @Override
    public Boolean getisDescriptionValid() {
        //TODO: Implement
        return null;
    }

    @Override
    public Boolean getisNameValid() {
        //TODO: Implement
        return null;
    }

    @Override
    public Boolean getisValueTypeValidForPDFA_1() {
        //TODO: Implement
        return null;
    }

    @Override
    public Boolean getisValueTypeValidForPDFA_2_3() {
        //TODO: Implement
        return null;
    }


    @Override
    public String getcategoryPrefix() {
        //TODO: Implement
        return null;
    }

    @Override
    public String getdescriptionPrefix() {
        //TODO: Implement
        return null;
    }

    @Override
    public String getnamePrefix() {
        //TODO: Implement
        return null;
    }

    @Override
    public String getvalueTypePrefix() {
        //TODO: Implement
        return null;
    }
}
