package org.verapdf.model.tools.xmp;

import com.adobe.xmp.impl.VeraPDFXMPNode;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Maksim Bezrukov
 */
public class ValidatorsContainerCreator {

    public static ValidatorsContainer createValidatorsContainerPredefinedForPDFA_1() {
        ValidatorsContainer container = new ValidatorsContainer();
        registerStructureTypeForContainer(XMPConstants.DIMENSIONS, XMPConstants.DIMENSIONS_STRUCTURE, container);
        registerStructureTypeForContainer(XMPConstants.THUMBNAIL, XMPConstants.THUMBNAIL_STRUCTURE, container);
        registerStructureTypeForContainer(XMPConstants.RESOURCE_EVENT, XMPConstants.RESOURCE_EVENT_STRUCTURE, container);
        registerStructureTypeForContainer(XMPConstants.RESOURCE_REF, XMPConstants.RESOURCE_REF_STRUCTURE, container);
        registerStructureTypeForContainer(XMPConstants.VERSION, XMPConstants.VERSION_STRUCTURE, container);
        registerStructureTypeForContainer(XMPConstants.JOB, XMPConstants.JOB_STRUCTURE, container);
        registerStructureTypeForContainer(XMPConstants.FLASH, XMPConstants.FLASH_STRUCTURE, container);
        registerStructureTypeForContainer(XMPConstants.OECF_SFR, XMPConstants.OECF_SFR_STRUCTURE, container);
        registerStructureTypeForContainer(XMPConstants.CFA_PATTERN, XMPConstants.CFA_PATTERN_STRUCTURE, container);
        registerStructureTypeForContainer(XMPConstants.DEVICE_SETTINGS, XMPConstants.DEVICE_SETTINGS_STRUCTURE, container);
        return container;
    }

    public static ValidatorsContainer createValidatorsContainerPredefinedForPDFA_2_3() {
        ValidatorsContainer container = createValidatorsContainerPredefinedForPDFA_1();
        registerStructureTypeForContainer(XMPConstants.COLORANT, XMPConstants.COLORANT_STRUCTURE, container);
        registerStructureTypeForContainer(XMPConstants.FONT, XMPConstants.FONT_STRUCTURE, container);
        registerStructureTypeForContainer(XMPConstants.BEAT_SPLICE_STRETCH, XMPConstants.BEAT_SPLICE_STRETCH_STRUCTURE, container);
        registerStructureTypeForContainer(XMPConstants.MARKER, XMPConstants.MARKER_STRUCTURE, container);
        registerStructureTypeForContainer(XMPConstants.MEDIA, XMPConstants.MEDIA_STRUCTURE, container);
        registerStructureTypeForContainer(XMPConstants.PROJECT_LINK, XMPConstants.PROJECT_LINK_STRUCTURE, container);
        registerStructureTypeForContainer(XMPConstants.RESAMPLE_STRETCH, XMPConstants.RESAMPLE_STRETCH_STRUCTURE, container);
        registerStructureTypeForContainer(XMPConstants.TIME, XMPConstants.TIME_STRUCTURE, container);
        registerStructureTypeForContainer(XMPConstants.TIMECODE, XMPConstants.TIMECODE_STRUCTURE, container);
        registerStructureTypeForContainer(XMPConstants.TIME_SCALE_STRETCH, XMPConstants.TIME_SCALE_STRETCH_STRUCTURE, container);
        return container;
    }

    public static ValidatorsContainer createExtendedValidatorsContainerForPDFA_1(VeraPDFXMPNode extensionContainer) {
        ValidatorsContainer container = createValidatorsContainerPredefinedForPDFA_1();
        //TODO: implement
        return container;
    }

    public static ValidatorsContainer createExtendedValidatorsContainerForPDFA_2_3(VeraPDFXMPNode extensionContainer, ValidatorsContainer mainSchemaContainer) {
        ValidatorsContainer container = mainSchemaContainer == null ? createValidatorsContainerPredefinedForPDFA_2_3() : new ValidatorsContainer(mainSchemaContainer);
        //TODO: implement
        return container;
    }

    private static void registerStructureTypeForContainer(String structureType, String[] structure, ValidatorsContainer container) {
        Map<String, String> res = new HashMap<>();
        for (int i = 1; i < structure.length; i += 2) {
            res.put(structure[i], structure[i + 1]);
        }
        container.registerStructureValidator(structureType, structure[0], res);
    }
}
