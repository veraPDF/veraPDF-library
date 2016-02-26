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
    private final boolean isClosedChoiceCheck;
    private final boolean isPDFA1Validation = true;
    private final SchemasDefinition mainPackageSchemasDefinition;
    private SchemasDefinition currentSchemasDefinitionPDFA_1;
    private SchemasDefinition currentSchemasDefinitionPDFA_2_3;

    public AXLXMPProperty(VeraPDFXMPNode xmpNode, boolean isMainMetadata, boolean isClosedChoiceCheck, SchemasDefinition mainPackageSchemasDefinition, SchemasDefinition currentSchemasDefinitionPDFA_1, SchemasDefinition currentSchemasDefinitionPDFA_2_3) {
        super(XMP_PROPERTY_TYPE);
        this.xmpNode = xmpNode;
        this.isMainMetadata = isMainMetadata;
        this.isClosedChoiceCheck = isClosedChoiceCheck;
        this.mainPackageSchemasDefinition = mainPackageSchemasDefinition;
        this.currentSchemasDefinitionPDFA_1 = currentSchemasDefinitionPDFA_1;
        this.currentSchemasDefinitionPDFA_2_3 = currentSchemasDefinitionPDFA_2_3;
        this.contextDependent = true;
    }

    @Override
    public String getID() {
        return this.xmpNode.getNamespaceURI() + " - " + this.xmpNode.getPrefix() + ":" + this.xmpNode.getName();
    }

    @Override
    public Boolean getisPredefinedInXMP2004() {
        return SchemasDefinitionCreator.getPredefinedSchemaDefinitionForPDFA_1(this.isClosedChoiceCheck).isDefinedProperty(this.xmpNode);
    }

    @Override
    public Boolean getisPredefinedInXMP2005() {
        return SchemasDefinitionCreator.getPredefinedSchemaDefinitionForPDFA_2_3(this.isClosedChoiceCheck).isDefinedProperty(this.xmpNode);
    }

    @Override
    public Boolean getisDefinedInCurrentPackage() {
        if (isPDFA1Validation) {
            return this.currentSchemasDefinitionPDFA_1.isDefinedProperty(this.xmpNode);
        } else {
            return this.currentSchemasDefinitionPDFA_2_3.isDefinedProperty(this.xmpNode);
        }
    }

    @Override
    public Boolean getisDefinedInMainPackage() {
        return this.isMainMetadata ? this.currentSchemasDefinitionPDFA_2_3.isDefinedProperty(this.xmpNode) :
                this.mainPackageSchemasDefinition.isDefinedProperty(this.xmpNode);
    }

    @Override
    public Boolean getisValueTypeCorrect() {
        if (isPDFA1Validation) {
            return isValueTypeCorrectForPDFA_1();
        } else {
            return isValueTypeCorrectForPDFA_2_3();
        }
    }

    private Boolean isValueTypeCorrectForPDFA_1() {
        if (SchemasDefinitionCreator.getPredefinedSchemaDefinitionForPDFA_1(this.isClosedChoiceCheck).isDefinedProperty(this.xmpNode)) {
            return SchemasDefinitionCreator.getPredefinedSchemaDefinitionForPDFA_1(this.isClosedChoiceCheck).isCorrespondsDefinedType(this.xmpNode);
        } else {
            return this.currentSchemasDefinitionPDFA_1.isCorrespondsDefinedType(this.xmpNode);
        }
    }

    private Boolean isValueTypeCorrectForPDFA_2_3() {
        if (SchemasDefinitionCreator.getPredefinedSchemaDefinitionForPDFA_2_3(this.isClosedChoiceCheck).isDefinedProperty(this.xmpNode)) {
            return SchemasDefinitionCreator.getPredefinedSchemaDefinitionForPDFA_2_3(this.isClosedChoiceCheck).isCorrespondsDefinedType(this.xmpNode);
        } else if (this.currentSchemasDefinitionPDFA_2_3.isDefinedProperty(this.xmpNode)) {
            return this.currentSchemasDefinitionPDFA_2_3.isCorrespondsDefinedType(this.xmpNode);
        } else {
            return this.mainPackageSchemasDefinition.isCorrespondsDefinedType(this.xmpNode);
        }
    }
}
