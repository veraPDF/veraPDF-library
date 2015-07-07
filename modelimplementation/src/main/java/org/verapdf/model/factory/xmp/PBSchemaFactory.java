package org.verapdf.model.factory.xmp;

import org.apache.xmpbox.schema.*;
import org.verapdf.model.impl.pb.xmp.schemas.*;
import org.verapdf.model.tools.XMPHelper;

/**
 * Wraps xmpbox schema object into PBXMPSchema object based on it's NSURI
 *
 * @author Maksim Bezrukov
 */
public class PBSchemaFactory {

    /**
     * Creates schema object based on it's NSURI
     *
     * @param schema - xmpbox schema
     * @return PBXMPSchema wrapper of the xmpbox's schema
     */
    public static PBXMPSchema createSchema(XMPSchema schema) {
        PBXMPSchema resultSchema;

        switch (schema.getNamespace()) {
            case XMPHelper.NSPDFAID:
                resultSchema = new PBPDFAIdSchema((PDFAIdentificationSchema) schema);
                break;
            case XMPHelper.NSPDFAEXTENSION:
                resultSchema = new PBPDFAExtensionSchema((PDFAExtensionSchema) schema);
                break;
            case XMPHelper.NSXMPBASIC:
                resultSchema = new PBXMPBasicSchema((XMPBasicSchema) schema);
                break;
            case XMPHelper.NSXMPRIGHTS:
                resultSchema = new PBXMPRightsSchema((XMPRightsManagementSchema) schema);
                break;
            case XMPHelper.NSXMPMM:
                resultSchema = new PBXMPMMSchema((XMPMediaManagementSchema) schema);
                break;
            case XMPHelper.NSXMPTPG:
                resultSchema = new PBXMPTPgSchema((XMPageTextSchema) schema);
                break;
            case XMPHelper.NSPDF:
                resultSchema = new PBPDFSchema((AdobePDFSchema) schema);
                break;
            case XMPHelper.NSPHOTOSHOP:
                resultSchema = new PBPhotoshopSchema((PhotoshopSchema) schema);
                break;
            case XMPHelper.NSCRS:
                resultSchema = new PBCRSSchema(schema);
                break;
            case XMPHelper.NSAUX:
                resultSchema = new PBAUXSchema(schema);
                break;
            case XMPHelper.NSXMPDM:
                resultSchema = new PBXMPDMSchema(schema);
                break;
            case XMPHelper.NSXAPS:
                resultSchema = new PBXAPSSchema(schema);
                break;
            default:
                if (XMPHelper.isPredifinedSchema(schema.getNamespace())) {
                    resultSchema = new PBXMPPredefinedSchema(schema);
                } else {
                    resultSchema = new PBXMPCustomSchema(schema);
                }
        }

        return resultSchema;
    }
}
