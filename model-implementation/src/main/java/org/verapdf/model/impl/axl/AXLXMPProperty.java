package org.verapdf.model.impl.axl;

import com.adobe.xmp.impl.VeraPDFXMPNode;
import org.verapdf.model.tools.xmp.SchemasDefinition;
import org.verapdf.model.tools.xmp.SchemasDefinitionCreator;
import org.verapdf.model.xmplayer.XMPProperty;

/**
 * @author Maksim Bezrukov
 */
public class AXLXMPProperty extends AXLXMPObject implements XMPProperty {

    public static final String XMP_PROPERTY_TYPE = "XMPProperty";

    private final VeraPDFXMPNode xmpNode;
    private final boolean isMainMetadata;
    private final SchemasDefinition mainPackageSchemasDefinition;
    private SchemasDefinition currentSchemasDefinitionPDFA_1;
    private SchemasDefinition currentSchemasDefinitionPDFA_2_3;

    public AXLXMPProperty(VeraPDFXMPNode xmpNode, boolean isMainMetadata, SchemasDefinition mainPackageSchemasDefinition, SchemasDefinition currentSchemasDefinitionPDFA_1, SchemasDefinition currentSchemasDefinitionPDFA_2_3) {
        super(XMP_PROPERTY_TYPE);
        this.xmpNode = xmpNode;
        this.isMainMetadata = isMainMetadata;
        this.mainPackageSchemasDefinition = mainPackageSchemasDefinition;
        this.currentSchemasDefinitionPDFA_1 = currentSchemasDefinitionPDFA_1;
        this.currentSchemasDefinitionPDFA_2_3 = currentSchemasDefinitionPDFA_2_3;
    }

    @Override
    public Boolean getisPredefinedForPDFA_1() {
        return SchemasDefinitionCreator.PREDEFINED_SCHEMA_DEFINITION_FOR_PDFA_1.isDefinedProperty(this.xmpNode);
    }

    @Override
    public Boolean getisPredefinedForPDFA_2_3() {
        return SchemasDefinitionCreator.PREDEFINED_SCHEMA_DEFINITION_FOR_PDFA_2_3.isDefinedProperty(this.xmpNode);
    }

    @Override
    public Boolean getisDefinedInCurrentPackageForPDFA_1() {
        return this.currentSchemasDefinitionPDFA_1.isDefinedProperty(this.xmpNode);
    }

    @Override
    public Boolean getisDefinedInCurrentPackageForPDFA_2_3() {
        return this.currentSchemasDefinitionPDFA_2_3.isDefinedProperty(this.xmpNode);
    }

    @Override
    public Boolean getisDefinedInMainPackage() {
        return this.isMainMetadata ? this.currentSchemasDefinitionPDFA_2_3.isDefinedProperty(this.xmpNode) :
                this.mainPackageSchemasDefinition.isDefinedProperty(this.xmpNode);
    }

    @Override
    public Boolean getisValueTypeCorrectForPDFA_1() {
        if (SchemasDefinitionCreator.PREDEFINED_SCHEMA_DEFINITION_FOR_PDFA_1.isDefinedProperty(this.xmpNode)) {
            return SchemasDefinitionCreator.PREDEFINED_SCHEMA_DEFINITION_FOR_PDFA_1.isCorrespondsDefinedType(this.xmpNode);
        } else {
            return this.currentSchemasDefinitionPDFA_1.isCorrespondsDefinedType(this.xmpNode);
        }
    }

    @Override
    public Boolean getisValueTypeCorrectForPDFA_2_3() {
        if (SchemasDefinitionCreator.PREDEFINED_SCHEMA_DEFINITION_FOR_PDFA_2_3.isDefinedProperty(this.xmpNode)) {
            return SchemasDefinitionCreator.PREDEFINED_SCHEMA_DEFINITION_FOR_PDFA_2_3.isCorrespondsDefinedType(this.xmpNode);
        } else if (this.currentSchemasDefinitionPDFA_2_3.isDefinedProperty(this.xmpNode)) {
            return this.currentSchemasDefinitionPDFA_2_3.isCorrespondsDefinedType(this.xmpNode);
        } else {
            return this.mainPackageSchemasDefinition.isCorrespondsDefinedType(this.xmpNode);
        }
    }
}
