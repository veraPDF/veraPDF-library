package org.verapdf.model.tools.xmp;

import com.adobe.xmp.XMPConst;
import com.adobe.xmp.impl.VeraPDFXMPNode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @author Maksim Bezrukov
 */
public class ValidatorsContainerCreator {

    private static ValidatorsContainer PREDEFINED_CONTAINER_WITHOUT_CLOSED_CHOICE_FOR_PDFA_1 = null;
    private static ValidatorsContainer PREDEFINED_CONTAINER_WITHOUT_CLOSED_CHOICE_FOR_PDFA_2_3 = null;
    private static ValidatorsContainer PREDEFINED_CONTAINER_WITH_CLOSED_CHOICE_FOR_PDFA_1 = null;
    private static ValidatorsContainer PREDEFINED_CONTAINER_WITH_CLOSED_CHOICE_FOR_PDFA_2_3 = null;

    static ValidatorsContainer getPredefinedContainerForPDFA_1(boolean isClosedFieldsCheck) {
        if (isClosedFieldsCheck) {
            if (PREDEFINED_CONTAINER_WITH_CLOSED_CHOICE_FOR_PDFA_1 == null) {
                PREDEFINED_CONTAINER_WITH_CLOSED_CHOICE_FOR_PDFA_1 = createValidatorsContainerPredefinedForPDFA_1(true);
            }
            return PREDEFINED_CONTAINER_WITH_CLOSED_CHOICE_FOR_PDFA_1;
        }
        if (PREDEFINED_CONTAINER_WITHOUT_CLOSED_CHOICE_FOR_PDFA_1 == null) {
            PREDEFINED_CONTAINER_WITHOUT_CLOSED_CHOICE_FOR_PDFA_1 = createValidatorsContainerPredefinedForPDFA_1(false);
        }
        return PREDEFINED_CONTAINER_WITHOUT_CLOSED_CHOICE_FOR_PDFA_1;
    }

    static ValidatorsContainer getPredefinedContainerForPDFA_2_3(boolean isClosedFieldsCheck) {
        if (isClosedFieldsCheck) {
            if (PREDEFINED_CONTAINER_WITH_CLOSED_CHOICE_FOR_PDFA_2_3 == null) {
                PREDEFINED_CONTAINER_WITH_CLOSED_CHOICE_FOR_PDFA_2_3 = createValidatorsContainerPredefinedForPDFA_2_3(true);
            }
            return PREDEFINED_CONTAINER_WITH_CLOSED_CHOICE_FOR_PDFA_2_3;
        }
        if (PREDEFINED_CONTAINER_WITHOUT_CLOSED_CHOICE_FOR_PDFA_2_3 == null) {
            PREDEFINED_CONTAINER_WITHOUT_CLOSED_CHOICE_FOR_PDFA_2_3 = createValidatorsContainerPredefinedForPDFA_2_3(false);
        }
        return PREDEFINED_CONTAINER_WITHOUT_CLOSED_CHOICE_FOR_PDFA_2_3;
    }

    private static ValidatorsContainer createValidatorsContainerPredefinedForPDFA_1(boolean isClosedFieldsCheck) {
        ValidatorsContainer container = createBasicValidatorsContainer(isClosedFieldsCheck);
        container.registerSimpleValidator(XMPConstants.GPS_COORDINATE, Pattern.compile("^\\d{2},\\d{2}[,\\.]\\d{2}[NSEW]$"));
        container.registerSimpleValidator(XMPConstants.LOCALE, Pattern.compile("^([a-zA-Z]{1,8})((-[a-zA-Z]{1,8})*)$"));
        return container;
    }

    private static ValidatorsContainer createValidatorsContainerPredefinedForPDFA_2_3(boolean isClosedFieldsCheck) {
        ValidatorsContainer container = createBasicValidatorsContainer(isClosedFieldsCheck);
        container.registerSimpleValidator(XMPConstants.GPS_COORDINATE, Pattern.compile("^\\d{1,3},\\d{1,2}(,\\d{1,2}|\\.\\d+)[NSEW]$"));
        container.registerSimpleValidator(XMPConstants.LOCALE, Pattern.compile("^([a-zA-Z]{1,8})((-[a-zA-Z0-9]{1,8})*)$"));
        registerStructureTypeWithRestrictedSimpleFieldsForContainer(
                XMPConstants.COLORANT,
                isClosedFieldsCheck,
                XMPConstants.getColorantWithoutRestrictedFieldStructure(),
                XMPConstants.getColorantRestrictedFieldStructure(),
                container);
        registerStructureTypeForContainer(XMPConstants.FONT, XMPConstants.getFontStructure(), container);
        registerStructureTypeForContainer(XMPConstants.BEAT_SPLICE_STRETCH, XMPConstants.getBeatSpliceStretchStructure(), container);
        registerStructureTypeWithRestrictedSimpleFieldsForContainer(
                XMPConstants.MARKER,
                isClosedFieldsCheck,
                XMPConstants.getMarkerWithoutRestrictedFieldStructure(),
                XMPConstants.getMarkerRestrictedFieldStructure(),
                container);
        registerStructureTypeForContainer(XMPConstants.MEDIA, XMPConstants.getMediaStructure(), container);
        registerStructureTypeWithRestrictedSimpleFieldsForContainer(
                XMPConstants.PROJECT_LINK,
                isClosedFieldsCheck,
                XMPConstants.getProjectLinkWithoutRestrictedFieldStructure(),
                XMPConstants.getProjectLinkRestrictedFieldStructure(),
                container);
        registerStructureTypeWithRestrictedSimpleFieldsForContainer(
                XMPConstants.RESAMPLE_STRETCH,
                isClosedFieldsCheck,
                XMPConstants.getResampleStretchWithoutRestrictedFieldStructure(),
                XMPConstants.getResampleStretchRestrictedFieldStructure(),
                container);
        registerStructureTypeForContainer(XMPConstants.TIME, XMPConstants.getTimeStructure(), container);
        registerStructureTypeWithRestrictedSimpleFieldsForContainer(
                XMPConstants.TIMECODE,
                isClosedFieldsCheck,
                XMPConstants.getTimecodeWithoutRestrictedFieldStructure(),
                XMPConstants.getTimecodeRestrictedFieldStructure(),
                container);
        registerStructureTypeWithRestrictedSimpleFieldsForContainer(
                XMPConstants.TIME_SCALE_STRETCH,
                isClosedFieldsCheck,
                XMPConstants.getTimeScaleStretchWithoutRestrictedFieldStructure(),
                XMPConstants.getTimeScaleStretchRestrictedFieldStructure(),
                container);
        return container;
    }

    private static ValidatorsContainer createBasicValidatorsContainer(boolean isClosedFieldsCheck) {
        ValidatorsContainer container = new ValidatorsContainer();
        registerStructureTypeForContainer(XMPConstants.DIMENSIONS, XMPConstants.getDimensionsStructure(), container);
        registerStructureTypeWithRestrictedSimpleFieldsForContainer(
                XMPConstants.THUMBNAIL,
                isClosedFieldsCheck,
                XMPConstants.getThumbnailWithoutRestrictedFieldStructure(),
                XMPConstants.getThumbnailRestrictedFieldStructure(),
                container);
        registerStructureTypeForContainer(XMPConstants.RESOURCE_EVENT, XMPConstants.getResourceEventStructure(), container);
        registerStructureTypeForContainer(XMPConstants.RESOURCE_REF, XMPConstants.getResourceRefStructure(), container);
        registerStructureTypeForContainer(XMPConstants.VERSION, XMPConstants.getVersionStructure(), container);
        registerStructureTypeForContainer(XMPConstants.JOB, XMPConstants.getJobStructure(), container);
        registerStructureTypeWithRestrictedSimpleFieldsForContainer(
                XMPConstants.FLASH,
                isClosedFieldsCheck,
                XMPConstants.getFlashWithoutRestrictedFieldStructure(),
                XMPConstants.getFlashRestrictedFieldStructure(),
                container);
        registerStructureTypeForContainer(XMPConstants.OECF_SFR, XMPConstants.getOecfSfrStructure(), container);
        registerStructureTypeForContainer(XMPConstants.CFA_PATTERN, XMPConstants.getCfaPatternStructure(), container);
        registerStructureTypeForContainer(XMPConstants.DEVICE_SETTINGS, XMPConstants.getDeviceSettingsStructure(), container);
        return container;
    }

    static ValidatorsContainer createExtendedValidatorsContainerForPDFA_1(VeraPDFXMPNode extensionContainer, boolean isClosedFieldsCheck) {
        ValidatorsContainer container = createValidatorsContainerPredefinedForPDFA_1(isClosedFieldsCheck);
        return createExtendedValidatorsContainer(extensionContainer, container);
    }

    static ValidatorsContainer createExtendedValidatorsContainerForPDFA_2_3(VeraPDFXMPNode extensionContainer, boolean isClosedFieldsCheck) {
        ValidatorsContainer container = createValidatorsContainerPredefinedForPDFA_2_3(isClosedFieldsCheck);
        return createExtendedValidatorsContainer(extensionContainer, container);
    }

    private static ValidatorsContainer createExtendedValidatorsContainer(VeraPDFXMPNode schemasDefinitions, ValidatorsContainer container) {
        List<VeraPDFXMPNode> schemas = schemasDefinitions.getChildren();
        for (VeraPDFXMPNode node : schemas) {
            registerAllTypesFromExtensionSchemaNode(node, container);
        }
        return container;
    }

    private static void registerAllTypesFromExtensionSchemaNode(VeraPDFXMPNode schema, ValidatorsContainer container) {
        List<VeraPDFXMPNode> schemaChildren = schema.getChildren();
        for (int i = schemaChildren.size() - 1; i >= 0; --i) {
            VeraPDFXMPNode child = schemaChildren.get(i);
            if (XMPConst.NS_PDFA_SCHEMA.equals(child.getNamespaceURI()) && "valueType".equals(child.getName())) {
                if (child.getOptions().isArray()) {
                    registerAllTypesFromValueTypeArrayNode(child, container);
                }
                break;
            }
        }
    }

    private static void registerAllTypesFromValueTypeArrayNode(VeraPDFXMPNode valueTypes, ValidatorsContainer container) {
        List<VeraPDFXMPNode> children = valueTypes.getChildren();
        for (VeraPDFXMPNode node : children) {
            registerTypeNode(node, container);
        }
    }

    private static void registerTypeNode(VeraPDFXMPNode valueType, ValidatorsContainer container) {
        String name = null;
        String namespace = null;
        Map<String, String> fields = null;

        for (VeraPDFXMPNode child : valueType.getChildren()) {
            if (XMPConst.NS_PDFA_TYPE.equals(child.getNamespaceURI())) {
                switch (child.getName()) {
                    case "type":
                        name = child.getValue();
                        break;
                    case "namespaceURI":
                        namespace = child.getValue();
                        break;
                    case "field":
                        if (child.getOptions().isArray()) {
                            fields = getStructureMapFromFieldsNode(child);
                        }
                        break;
                    default:
                    	break;
                }
            }
        }

        if (name != null && namespace != null && fields != null && !fields.isEmpty()) {
            container.registerStructuredValidator(name, namespace, fields);
        }
    }

    private static Map<String, String> getStructureMapFromFieldsNode(VeraPDFXMPNode node) {
        Map<String, String> res = new HashMap<>();

        List<VeraPDFXMPNode> fields = node.getChildren();
        for (VeraPDFXMPNode field : fields) {
            String name = null;
            String valueType = null;

            for (VeraPDFXMPNode child : field.getChildren()) {
                if (XMPConst.NS_PDFA_FIELD.equals(child.getNamespaceURI())) {
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
                res.put(name, valueType);
            }
        }

        return res;
    }

    private static void registerStructureTypeForContainer(String structureType, String[] structure, ValidatorsContainer container) {
        Map<String, String> res = new HashMap<>();
        for (int i = 1; i < structure.length; i += 2) {
            res.put(structure[i], structure[i + 1]);
        }
        container.registerStructuredValidator(structureType, structure[0], res);
    }

    private static void registerStructureTypeWithRestrictedSimpleFieldsForContainer(String structureType, boolean isClosedFieldsCheck, String[] structure, String[] closedStructure, ValidatorsContainer container) {
        Map<String, String> res = new HashMap<>();
        for (int i = 1; i < structure.length; i += 2) {
            res.put(structure[i], structure[i + 1]);
        }
        if (isClosedFieldsCheck) {
            Map<String, Pattern> closedRes = new HashMap<>();
            for (int i = 0; i < closedStructure.length; i += 3) {
                closedRes.put(closedStructure[i], Pattern.compile(closedStructure[i + 2]));
            }
            container.registerStructuredWithRestrictedFieldsValidator(structureType, structure[0], res, closedRes);
        } else {
            for (int i = 0; i < closedStructure.length; i += 3) {
                res.put(closedStructure[i], closedStructure[i + 1]);
            }
            container.registerStructuredValidator(structureType, structure[0], res);
        }
    }
}
