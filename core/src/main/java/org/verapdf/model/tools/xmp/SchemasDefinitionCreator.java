/**
 * This file is part of veraPDF Library core, a module of the veraPDF project.
 * Copyright (c) 2015-2025, veraPDF Consortium <info@verapdf.org>
 * All rights reserved.
 *
 * veraPDF Library core is free software: you can redistribute it and/or modify
 * it under the terms of either:
 *
 * The GNU General public license GPLv3+.
 * You should have received a copy of the GNU General Public License
 * along with veraPDF Library core as the LICENSE.GPL file in the root of the source
 * tree.  If not, see http://www.gnu.org/licenses/ or
 * https://www.gnu.org/licenses/gpl-3.0.en.html.
 *
 * The Mozilla Public License MPLv2+.
 * You should have received a copy of the Mozilla Public License along with
 * veraPDF Library core as the LICENSE.MPL file in the root of the source tree.
 * If a copy of the MPL was not distributed with this file, you can obtain one at
 * http://mozilla.org/MPL/2.0/.
 */
package org.verapdf.model.tools.xmp;

import org.verapdf.xmp.XMPConst;
import org.verapdf.xmp.impl.VeraPDFXMPNode;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @author Maksim Bezrukov
 */
public class SchemasDefinitionCreator {

    public static final Map<String, SchemasDefinition> EMPTY_SCHEMAS_DEFINITION = Collections.emptyMap();
    public static final SchemasDefinition EMPTY_SCHEMA_DEFINITION = new SchemasDefinition();
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
    public static Map<String, SchemasDefinition> createExtendedSchemasDefinitionForPDFA_1(VeraPDFXMPNode schemas,
                                                                                          boolean isClosedFieldsCheck) {
        return createExtendedSchemasDefinition(Collections.emptyMap(), schemas, true, isClosedFieldsCheck);
    }

    /**
     * Creates schemas definition object valid for PDF/A-2 or for PDF/A-3
     *
     * @param schemas extension schemas container node
     * @return created Schemas Definition object
     */
    public static Map<String, SchemasDefinition> createExtendedSchemasDefinitionForPDFA_2_3(VeraPDFXMPNode schemas,
                                                                                            boolean isClosedFieldsCheck) {
        return extendSchemasDefinitionForPDFA_2_3(Collections.emptyMap(), schemas, isClosedFieldsCheck);
    }

    /**
     * Extends already created extended schemas definitions object valid for PDF/A-2 or for PDF/A-3 without properties
     * This method doesn't require PDF/A-1 analog because extended schemas extensions aren't allowed in PDF/A-1
     *
     * @param extendedSchemas extended schemas for extension
     * @param schemas extension schemas container node
     * @return created Schemas Definition object
     */
    public static Map<String, SchemasDefinition> extendSchemasDefinitionForPDFA_2_3(Map<String, SchemasDefinition> extendedSchemas,
                                                                                    VeraPDFXMPNode schemas,
                                                                                    boolean isClosedFieldsCheck) {
        if (extendedSchemas == null) {
            throw new IllegalArgumentException("Nothing to extend");
        }
        return createExtendedSchemasDefinition(extendedSchemas, schemas, false, isClosedFieldsCheck);
    }

    private static Map<String, SchemasDefinition> createExtendedSchemasDefinition(Map<String, SchemasDefinition> extendedSchema,
                                                                                  VeraPDFXMPNode schemas,
                                                                                  boolean isPDFA_1,
                                                                                  boolean isClosedFieldsCheck) {
        if (schemas == null) {
            return EMPTY_SCHEMAS_DEFINITION;
        }
        if (!(XMPConst.NS_PDFA_EXTENSION.equals(schemas.getNamespaceURI()) && "schemas".equals(schemas.getName()) && schemas.getOptions().isArray())) {
            return EMPTY_SCHEMAS_DEFINITION;
        }
        Map<String, ValidatorsContainer> valueType = new HashMap<>(extendedSchema.size());
        for (Map.Entry<String, SchemasDefinition> entry : extendedSchema.entrySet()) {
            valueType.put(entry.getKey(), entry.getValue().getValidatorsContainer());
        }
        Map<String, SchemasDefinition> res = new HashMap<>();
        List<VeraPDFXMPNode> schemasNodes = schemas.getChildren();
        for (VeraPDFXMPNode node : schemasNodes) {
            registerAllPropertiesFromExtensionSchemaNode(node, res, valueType, isPDFA_1, isClosedFieldsCheck);
        }
        return res;
    }

    private static void registerAllPropertiesFromExtensionSchemaNode(VeraPDFXMPNode schema,
                                                                     Map<String, SchemasDefinition> schemasMap,
                                                                     Map<String, ValidatorsContainer> oldValidators,
                                                                     boolean isPDFA_1,
                                                                     boolean isClosedFieldsCheck) {
        List<VeraPDFXMPNode> schemaChildren = schema.getChildren();
        VeraPDFXMPNode propertyNode = null;
        VeraPDFXMPNode valueTypeNode = null;
        String namespaceURI = null;
        for (VeraPDFXMPNode child : schemaChildren) {
            if (XMPConst.NS_PDFA_SCHEMA.equals(child.getNamespaceURI())) {
                switch (child.getName()) {
                    case "property":
                        if (child.getOptions().isArray()) {
                            propertyNode = child;
                        }
                        break;
                    case "valueType":
                        if (child.getOptions().isArray()) {
                            valueTypeNode = child;
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
            ValidatorsContainer validatorsContainer = oldValidators.get(namespaceURI);
            ValidatorsContainer currentContainer;
            if (validatorsContainer == null) {
                currentContainer = isPDFA_1 ? ValidatorsContainerCreator.createValidatorsContainerPredefinedForPDFA_1(isClosedFieldsCheck) :
                        ValidatorsContainerCreator.createValidatorsContainerPredefinedForPDFA_2_3(isClosedFieldsCheck);
            } else {
                currentContainer = new ValidatorsContainer(validatorsContainer);
            }
            ValidatorsContainerCreator.extendValidatorsContainer(currentContainer, valueTypeNode);
            SchemasDefinition schemasDefinition = new SchemasDefinition(currentContainer);
            registerAllPropertiesFromPropertyArrayNode(namespaceURI, propertyNode, schemasDefinition);
            schemasMap.put(namespaceURI, schemasDefinition);
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
