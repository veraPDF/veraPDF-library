package org.verapdf.model.impl.axl;

import com.adobe.xmp.impl.VeraPDFXMPNode;
import org.verapdf.model.xmplayer.ExtensionSchemaField;

import java.util.List;

/**
 * @author Maksim Bezrukov
 */
public class AXLExtensionSchemaField extends AXLExtensionSchemaObject implements ExtensionSchemaField {

    public static final String EXTENSION_SCHEMA_FIELD = "ExtensionSchemaField";

    private final List<String> currentValueTypes;
    private final List<String> mainPackageValueTypes;

    public AXLExtensionSchemaField(VeraPDFXMPNode xmpNode, List<String> currentValueTypes, List<String> mainPackageValueTypes) {
        super(EXTENSION_SCHEMA_FIELD, xmpNode);
        this.currentValueTypes = currentValueTypes;
        this.mainPackageValueTypes = mainPackageValueTypes;
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
