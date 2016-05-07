package org.verapdf.model.tools.xmp;

import com.adobe.xmp.XMPConst;
import com.adobe.xmp.impl.VeraPDFXMPNode;

import java.util.List;
import java.util.regex.Pattern;

/**
 * @author Maksim Bezrukov
 */
public class SchemasDefinitionCreator {

    public static final SchemasDefinition EMPTY_SCHEMAS_DEFINITION = new SchemasDefinition();
    private static SchemasDefinition PREDEFINED_SCHEMA_DEFINITION_WITHOUT_CLOSED_CHOICE_FOR_PDFA_1 = null;
    private static SchemasDefinition PREDEFINED_SCHEMA_DEFINITION_WITHOUT_CLOSED_CHOICE_FOR_PDFA_2_3 = null;
    private static SchemasDefinition PREDEFINED_SCHEMA_DEFINITION_WITH_CLOSED_CHOICE_FOR_PDFA_1 = null;
    private static SchemasDefinition PREDEFINED_SCHEMA_DEFINITION_WITH_CLOSED_CHOICE_FOR_PDFA_2_3 = null;

    /**
     * Returns predefined schemas definition for PDF/A-1
     * @param isClosedFieldsCheck true for check the value of the closed choice
     * @return created schemas definition
     */
    public static SchemasDefinition getPredefinedSchemaDefinitionForPDFA_1(boolean isClosedFieldsCheck) {
        if (isClosedFieldsCheck) {
            if (PREDEFINED_SCHEMA_DEFINITION_WITH_CLOSED_CHOICE_FOR_PDFA_1 == null) {
                PREDEFINED_SCHEMA_DEFINITION_WITH_CLOSED_CHOICE_FOR_PDFA_1 = createPredefinedPDFA_1SchemasDefinition(true);
            }
            return PREDEFINED_SCHEMA_DEFINITION_WITH_CLOSED_CHOICE_FOR_PDFA_1;
        }
        if (PREDEFINED_SCHEMA_DEFINITION_WITHOUT_CLOSED_CHOICE_FOR_PDFA_1 == null) {
            PREDEFINED_SCHEMA_DEFINITION_WITHOUT_CLOSED_CHOICE_FOR_PDFA_1 = createPredefinedPDFA_1SchemasDefinition(false);
        }
        return PREDEFINED_SCHEMA_DEFINITION_WITHOUT_CLOSED_CHOICE_FOR_PDFA_1;
    }

    /**
     * Returns predefined schemas definition for PDF/A-2 or PDF/A-3
     * @param isClosedFieldsCheck true for check the value of the closed choice
     * @return created schemas definition
     */
    public static SchemasDefinition getPredefinedSchemaDefinitionForPDFA_2_3(boolean isClosedFieldsCheck) {
        if (isClosedFieldsCheck) {
            if (PREDEFINED_SCHEMA_DEFINITION_WITH_CLOSED_CHOICE_FOR_PDFA_2_3 == null) {
                PREDEFINED_SCHEMA_DEFINITION_WITH_CLOSED_CHOICE_FOR_PDFA_2_3 = createPredefinedPDFA_2_3SchemasDefinition(true);
            }
            return PREDEFINED_SCHEMA_DEFINITION_WITH_CLOSED_CHOICE_FOR_PDFA_2_3;
        }
        if (PREDEFINED_SCHEMA_DEFINITION_WITHOUT_CLOSED_CHOICE_FOR_PDFA_2_3 == null) {
            PREDEFINED_SCHEMA_DEFINITION_WITHOUT_CLOSED_CHOICE_FOR_PDFA_2_3 = createPredefinedPDFA_2_3SchemasDefinition(false);
        }
        return PREDEFINED_SCHEMA_DEFINITION_WITHOUT_CLOSED_CHOICE_FOR_PDFA_2_3;
    }

    /**
     * Creates schemas definition object valid for PDF/A-1
     *
     * @param schemas extension schemas container node
     * @return created Schemas Definition object
     */
    public static SchemasDefinition createExtendedSchemasDefinitionForPDFA_1(VeraPDFXMPNode schemas, boolean isClosedFieldsCheck) {
        return createExtendedSchemasDefinition(schemas, true, isClosedFieldsCheck);
    }

    /**
     * Creates schemas definition object valid for PDF/A-2 or for PDF/A-3
     *
     * @param schemas extension schemas container node
     * @return created Schemas Definition object
     */
    public static SchemasDefinition createExtendedSchemasDefinitionForPDFA_2_3(VeraPDFXMPNode schemas, boolean isClosedFieldsCheck) {
        return createExtendedSchemasDefinition(schemas, false, isClosedFieldsCheck);
    }

    private static SchemasDefinition createExtendedSchemasDefinition(VeraPDFXMPNode schemas, boolean isPDFA_1, boolean isClosedFieldsCheck) {
        if (schemas == null) {
            return EMPTY_SCHEMAS_DEFINITION;
        }
        if (!(XMPConst.NS_PDFA_EXTENSION.equals(schemas.getNamespaceURI()) && "schemas".equals(schemas.getName()) && schemas.getOptions().isArray())) {
            return EMPTY_SCHEMAS_DEFINITION;
        }
        ValidatorsContainer typeContainer = isPDFA_1 ? ValidatorsContainerCreator.createExtendedValidatorsContainerForPDFA_1(schemas, isClosedFieldsCheck) :
                ValidatorsContainerCreator.createExtendedValidatorsContainerForPDFA_2_3(schemas, isClosedFieldsCheck);
        SchemasDefinition res = new SchemasDefinition(typeContainer);
        List<VeraPDFXMPNode> schemasNodes = schemas.getChildren();
        for (VeraPDFXMPNode node : schemasNodes) {
            registerAllPropertiesFromExtensionSchemaNode(node, res);
        }
        return res;
    }

    private static void registerAllPropertiesFromExtensionSchemaNode(VeraPDFXMPNode schema, SchemasDefinition schemasDefinition) {
        List<VeraPDFXMPNode> schemaChildren = schema.getChildren();
        VeraPDFXMPNode propertyNode = null;
        String namespaceURI = null;
        for (VeraPDFXMPNode child : schemaChildren) {
            if (XMPConst.NS_PDFA_SCHEMA.equals(child.getNamespaceURI())) {
                switch (child.getName()) {
                    case "property":
                        if (child.getOptions().isArray()) {
                            propertyNode = child;
                        }
                        break;
                    case "namespaceURI":
                        namespaceURI = child.getValue();
                        break;
                    default:
                    	break;
                }
            }
        }
        if (namespaceURI != null && propertyNode != null) {
            registerAllPropertiesFromPropertyArrayNode(namespaceURI, propertyNode, schemasDefinition);
        }
    }

    private static void registerAllPropertiesFromPropertyArrayNode(String namespace, VeraPDFXMPNode properties, SchemasDefinition schemasDefinition) {
        List<VeraPDFXMPNode> children = properties.getChildren();
        for (VeraPDFXMPNode node : children) {
            registerPropertyNode(namespace, node, schemasDefinition);
        }
    }

    private static void registerPropertyNode(String namespace, VeraPDFXMPNode property, SchemasDefinition schemasDefinition) {
        String name = null;
        String valueType = null;

        for (VeraPDFXMPNode child : property.getChildren()) {
            if (XMPConst.NS_PDFA_PROPERTY.equals(child.getNamespaceURI())) {
                switch (child.getName()) {
                    case "name":
                        name = child.getValue();
                        break;
                    case "valueType":
                        valueType = child.getValue();
                        break;
                    default:
                    	break;
                }
            }
        }
        if (name != null && valueType != null) {
            schemasDefinition.registerProperty(namespace, name, valueType);
        }
    }

    private static PredefinedSchemasDefinition createPredefinedPDFA_1SchemasDefinition(boolean isClosedFieldsCheck) {
        PredefinedSchemasDefinition schemas = createBasicSchemasDefinition(
                ValidatorsContainerCreator.getPredefinedContainerForPDFA_1(isClosedFieldsCheck),
                isClosedFieldsCheck
        );
        registerStructureTypeForSchema(XMPConstants.getPhotoshopDiffer1(), schemas);
        registerStructureTypeForSchema(XMPConstants.getExifWithoutRestrictedFieldDiffer1(), schemas);
        registerRestrictedSimpleFieldForSchema(XMPConstants.getPdfaIdentificationRestrictedFieldDiffer1(), schemas, isClosedFieldsCheck);
        registerRestrictedSimpleFieldForSchema(XMPConstants.getExifRestrictedFieldDiffer1(), schemas, isClosedFieldsCheck);
        return schemas;
    }

    private static PredefinedSchemasDefinition createPredefinedPDFA_2_3SchemasDefinition(boolean isClosedFieldsCheck) {
        PredefinedSchemasDefinition schemas = createBasicSchemasDefinition(
                ValidatorsContainerCreator.getPredefinedContainerForPDFA_2_3(isClosedFieldsCheck),
                isClosedFieldsCheck
        );
        registerStructureTypeForSchema(XMPConstants.getPdfaIdentificationSpecified23(), schemas);
        registerStructureTypeForSchema(XMPConstants.getXmpBasicSpecified23(), schemas);
        registerStructureTypeForSchema(XMPConstants.getXmpPagedTextSpecified23(), schemas);
        registerStructureTypeForSchema(XMPConstants.getXmpDynamicMediaWithoutRestrictedFieldSpecified23(), schemas);
        registerStructureTypeForSchema(XMPConstants.getPhotoshopDiffer23(), schemas);
        registerStructureTypeForSchema(XMPConstants.getCameraRawWithoutRestrictedFieldSpecified23(), schemas);
        registerStructureTypeForSchema(XMPConstants.getExifWithoutRestrictedFieldDiffer23(), schemas);
        registerStructureTypeForSchema(XMPConstants.getAuxSpecified23(), schemas);
        registerRestrictedSimpleFieldForSchema(XMPConstants.getPdfaIdentificationRestrictedFieldDiffer23(), schemas, isClosedFieldsCheck);
        registerRestrictedSimpleFieldForSchema(XMPConstants.getXmpDynamicMediaRestrictedFieldSpecified23(), schemas, isClosedFieldsCheck);
        registerRestrictedSimpleFieldForSchema(XMPConstants.getCameraRawRestrictedFieldSpecified23(), schemas, isClosedFieldsCheck);
        registerRestrictedSimpleFieldForSchema(XMPConstants.getExifRestrictedFieldDiffer23(), schemas, isClosedFieldsCheck);
        String[] cameraRawSeqOfPointsSpecified23 = XMPConstants.getCameraRawSeqOfPointsSpecified23();
        registerRestrictedSeqTextFieldForSchema(
                cameraRawSeqOfPointsSpecified23[0],
                cameraRawSeqOfPointsSpecified23[1],
                cameraRawSeqOfPointsSpecified23[2],
                XMPConstants.SEQ + " " + XMPConstants.TEXT,
                schemas,
                isClosedFieldsCheck
        );
        return schemas;
    }

    private static PredefinedSchemasDefinition createBasicSchemasDefinition(ValidatorsContainer typeContainer, boolean isClosedFieldsCheck) {
        PredefinedSchemasDefinition schemas = new PredefinedSchemasDefinition(typeContainer);
        registerStructureTypeForSchema(XMPConstants.getPdfaIdentificationCommon(), schemas);
        registerStructureTypeForSchema(XMPConstants.getDublinCoreCommon(), schemas);
        registerStructureTypeForSchema(XMPConstants.getXmpBasicCommon(), schemas);
        registerStructureTypeForSchema(XMPConstants.getXmpRightsCommon(), schemas);
        registerStructureTypeForSchema(XMPConstants.getXmpMediaManagementCommon(), schemas);
        registerStructureTypeForSchema(XMPConstants.getXmpBasicJobCommon(), schemas);
        registerStructureTypeForSchema(XMPConstants.getXmpPagedTextCommon(), schemas);
        registerStructureTypeForSchema(XMPConstants.getAdobePdfCommon(), schemas);
        registerStructureTypeForSchema(XMPConstants.getPhotoshopCommon(), schemas);
        registerStructureTypeForSchema(XMPConstants.getTiffWithoutRestrictedFieldCommon(), schemas);
        registerStructureTypeForSchema(XMPConstants.getExifWithoutRestrictedFieldCommon(), schemas);
        registerRestrictedSimpleFieldForSchema(XMPConstants.getTiffRestrictedFieldCommon(), schemas, isClosedFieldsCheck);
        registerRestrictedSimpleFieldForSchema(XMPConstants.getExifRestrictedFieldCommon(), schemas, isClosedFieldsCheck);
        registerSeqChoiceFieldForSchema(
                XMPConst.NS_TIFF,
                "YCbCrSubSampling",
                XMPConstants.getTiffYcbcrsubsamplingSeqChoiceCommon(),
                XMPConstants.SEQ + " " + XMPConstants.INTEGER,
                schemas,
                isClosedFieldsCheck
                );
        registerSeqChoiceFieldForSchema(
                XMPConst.NS_EXIF,
                "ComponentsConfiguration",
                XMPConstants.getExifComponentsConfigurationClosedSeqChoiceCommon(),
                XMPConstants.SEQ + " " + XMPConstants.INTEGER,
                schemas,
                isClosedFieldsCheck
        );
        return schemas;
    }

    private static void registerStructureTypeForSchema(String[] structure, SchemasDefinition schema) {
        for (int i = 1; i < structure.length; i += 2) {
            schema.registerProperty(structure[0], structure[i], structure[i + 1]);
        }
    }

    private static void registerRestrictedSimpleFieldForSchema(String[] structure, PredefinedSchemasDefinition schema, boolean isClosedFieldsCheck) {
        if (isClosedFieldsCheck) {
            for (int i = 1; i < structure.length; i += 3) {
                schema.registerRestrictedSimpleFieldProperty(structure[0], structure[i], Pattern.compile(structure[i + 2]));
            }
        } else {
            for (int i = 1; i < structure.length; i += 3) {
                schema.registerProperty(structure[0], structure[i], structure[i + 1]);
            }
        }
    }

    private static void registerSeqChoiceFieldForSchema(String namespaceURI, String propertyName, String[][] structure, String type, PredefinedSchemasDefinition schema, boolean isClosedFieldsCheck) {
        if (isClosedFieldsCheck) {
            schema.registerSeqChoiceProperty(namespaceURI, propertyName, structure);
        } else {
            schema.registerProperty(namespaceURI, propertyName, type);
        }
    }

    private static void registerRestrictedSeqTextFieldForSchema(String namespaceURI, String propertyName, String regex, String type, PredefinedSchemasDefinition schema, boolean isClosedFieldsCheck) {
        if (isClosedFieldsCheck) {
            schema.registerRestrictedSeqTextProperty(
                    namespaceURI,
                    propertyName,
                    Pattern.compile(regex)
            );
        } else {
            schema.registerProperty(namespaceURI, propertyName, type);
        }
    }
}
