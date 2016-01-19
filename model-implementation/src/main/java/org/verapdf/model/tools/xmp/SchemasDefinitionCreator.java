package org.verapdf.model.tools.xmp;

import com.adobe.xmp.impl.VeraPDFXMPNode;
import com.adobe.xmp.impl.XMPSchemaRegistryImpl;

import java.util.List;
import java.util.regex.Pattern;

/**
 * @author Maksim Bezrukov
 */
public class SchemasDefinitionCreator {

    public static final SchemasDefinition EMPTY_SCHEMAS_DEFINITION = new SchemasDefinition();
    public static final SchemasDefinition PREDEFINED_SCHEMA_DEFINITION_FOR_PDFA_1 = createPredefinedPDFA_1SchemasDefinition();
    public static final SchemasDefinition PREDEFINED_SCHEMA_DEFINITION_FOR_PDFA_2_3 = createPredefinedPDFA_2_3SchemasDefinition();

    /**
     * Creates schemas definition object valid for PDF/A-1
     *
     * @param schemas extension schemas container node
     * @return created Schemas Definition object
     */
    public static SchemasDefinition createExtendedSchemasDefinitionForPDFA_1(VeraPDFXMPNode schemas) {
        return createExtendedSchemasDefinition(schemas, true);
    }

    /**
     * Creates schemas definition object valid for PDF/A-2 or for PDF/A-3
     *
     * @param schemas extension schemas container node
     * @return created Schemas Definition object
     */
    public static SchemasDefinition createExtendedSchemasDefinitionForPDFA_2_3(VeraPDFXMPNode schemas) {
        return createExtendedSchemasDefinition(schemas, false);
    }

    private static SchemasDefinition createExtendedSchemasDefinition(VeraPDFXMPNode schemas, boolean isPDFA_1) {
        if (schemas == null) {
            return EMPTY_SCHEMAS_DEFINITION;
        }
        if (!(XMPSchemaRegistryImpl.NS_PDFA_EXTENSION.equals(schemas.getNamespaceURI()) && "schemas".equals(schemas.getName()) && schemas.getOptions().isArray())) {
            return EMPTY_SCHEMAS_DEFINITION;
        }
        ValidatorsContainer typeContainer = isPDFA_1 ? ValidatorsContainerCreator.createExtendedValidatorsContainerForPDFA_1(schemas) :
                ValidatorsContainerCreator.createExtendedValidatorsContainerForPDFA_2_3(schemas);
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
            if (XMPSchemaRegistryImpl.NS_PDFA_SCHEMA.equals(child.getNamespaceURI())) {
                switch (child.getName()) {
                    case "property":
                        if (child.getOptions().isArray()) {
                            propertyNode = child;
                        }
                        break;
                    case "namespaceURI":
                        namespaceURI = child.getValue();
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
            if (XMPSchemaRegistryImpl.NS_PDFA_PROPERTY.equals(child.getNamespaceURI())) {
                switch (child.getName()) {
                    case "name":
                        name = child.getValue();
                        break;
                    case "valueType":
                        valueType = child.getValue();
                        break;
                }
            }
        }
        if (name != null && valueType != null) {
            schemasDefinition.registerProperty(namespace, name, valueType);
        }
    }

    private static PredefinedSchemasDefinition createPredefinedPDFA_1SchemasDefinition() {
        PredefinedSchemasDefinition schemas = createBasicSchemasDefinition(ValidatorsContainerCreator.PREDEFINED_CONTAINER_FOR_PDFA_1);
        registerRestrictedSimpleFieldForSchema(XMPConstants.PDFA_IDENTIFICATION_RESTRICTED_FIELD_DIFFER_1, schemas);
        registerStructureTypeForSchema(XMPConstants.PHOTOSHOP_DIFFER_1, schemas);
        registerStructureTypeForSchema(XMPConstants.EXIF_WITHOUT_RESTRICTED_FIELD_DIFFER_1, schemas);
        registerRestrictedSimpleFieldForSchema(XMPConstants.EXIF_RESTRICTED_FIELD_DIFFER_1, schemas);
        return schemas;
    }

    private static PredefinedSchemasDefinition createPredefinedPDFA_2_3SchemasDefinition() {
        PredefinedSchemasDefinition schemas = createBasicSchemasDefinition(ValidatorsContainerCreator.PREDEFINED_CONTAINER_FOR_PDFA_2_3);
        registerRestrictedSimpleFieldForSchema(XMPConstants.PDFA_IDENTIFICATION_RESTRICTED_FIELD_DIFFER_2_3, schemas);
        registerStructureTypeForSchema(XMPConstants.PDFA_IDENTIFICATION_SPECIFIED_2_3, schemas);
        registerStructureTypeForSchema(XMPConstants.XMP_BASIC_SPECIFIED_2_3, schemas);
        registerStructureTypeForSchema(XMPConstants.XMP_PAGED_TEXT_SPECIFIED_2_3, schemas);
        registerStructureTypeForSchema(XMPConstants.XMP_DYNAMIC_MEDIA_WITHOUT_RESTRICTED_FIELD_SPECIFIED_2_3, schemas);
        registerRestrictedSimpleFieldForSchema(XMPConstants.XMP_DYNAMIC_MEDIA_RESTRICTED_FIELD_SPECIFIED_2_3, schemas);
        registerStructureTypeForSchema(XMPConstants.PHOTOSHOP_DIFFER_2_3, schemas);
        registerStructureTypeForSchema(XMPConstants.CAMERA_RAW_WITHOUT_RESTRICTED_FIELD_SPECIFIED_2_3, schemas);
        registerRestrictedSimpleFieldForSchema(XMPConstants.CAMERA_RAW_RESTRICTED_FIELD_SPECIFIED_2_3, schemas);
        schemas.registerRestrictedSeqTextProperty(
                XMPConstants.CAMERA_RAW_SEQ_OF_POINTS_SPECIFIED_2_3[0],
                XMPConstants.CAMERA_RAW_SEQ_OF_POINTS_SPECIFIED_2_3[1],
                Pattern.compile(XMPConstants.CAMERA_RAW_SEQ_OF_POINTS_SPECIFIED_2_3[2])
        );
        registerStructureTypeForSchema(XMPConstants.EXIF_WITHOUT_RESTRICTED_FIELD_DIFFER_2_3, schemas);
        registerRestrictedSimpleFieldForSchema(XMPConstants.EXIF_RESTRICTED_FIELD_DIFFER_2_3, schemas);
        registerStructureTypeForSchema(XMPConstants.AUX_SPECIFIED_2_3, schemas);
        return schemas;
    }

    private static PredefinedSchemasDefinition createBasicSchemasDefinition(ValidatorsContainer typeContainer) {
        PredefinedSchemasDefinition schemas = new PredefinedSchemasDefinition(typeContainer);
        registerStructureTypeForSchema(XMPConstants.PDFA_IDENTIFICATION_COMMON, schemas);
        registerStructureTypeForSchema(XMPConstants.DUBLIN_CORE_COMMON, schemas);
        registerStructureTypeForSchema(XMPConstants.XMP_BASIC_COMMON, schemas);
        registerStructureTypeForSchema(XMPConstants.XMP_RIGHTS_COMMON, schemas);
        registerStructureTypeForSchema(XMPConstants.XMP_MEDIA_MANAGEMENT_COMMON, schemas);
        registerStructureTypeForSchema(XMPConstants.XMP_BASIC_JOB_COMMON, schemas);
        registerStructureTypeForSchema(XMPConstants.XMP_PAGED_TEXT_COMMON, schemas);
        registerStructureTypeForSchema(XMPConstants.ADOBE_PDF_COMMON, schemas);
        registerStructureTypeForSchema(XMPConstants.PHOTOSHOP_COMMON, schemas);
        registerStructureTypeForSchema(XMPConstants.TIFF_WITHOUT_RESTRICTED_FIELD_COMMON, schemas);
        registerRestrictedSimpleFieldForSchema(XMPConstants.TIFF_RESTRICTED_FIELD_COMMON, schemas);
        schemas.registerSeqChoiceProperty(
                XMPSchemaRegistryImpl.NS_TIFF,
                "YCbCrSubSampling",
                XMPConstants.TIFF_YCBCRSUBSAMPLING_SEQ_CHOICE_COMMON);
        registerStructureTypeForSchema(XMPConstants.EXIF_WITHOUT_RESTRICTED_FIELD_COMMON, schemas);
        registerRestrictedSimpleFieldForSchema(XMPConstants.EXIF_RESTRICTED_FIELD_COMMON, schemas);
        schemas.registerSeqChoiceProperty(
                XMPSchemaRegistryImpl.NS_EXIF,
                "ComponentsConfiguration",
                XMPConstants.EXIF_COMPONENTS_CONFIGURATION_CLOSED_SEQ_CHOICE_COMMON);
        return schemas;
    }

    private static void registerStructureTypeForSchema(String[] structure, SchemasDefinition schema) {
        for (int i = 1; i < structure.length; i += 2) {
            schema.registerProperty(structure[0], structure[i], structure[i + 1]);
        }
    }

    private static void registerRestrictedSimpleFieldForSchema(String[] structure, PredefinedSchemasDefinition schema) {
        for (int i = 1; i < structure.length; i += 2) {
            schema.registerRestrictedSimpleFieldProperty(structure[0], structure[i], Pattern.compile(structure[i + 1]));
        }
    }
}
