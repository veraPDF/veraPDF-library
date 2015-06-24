package org.verapdf.model.impl.pb.xmp;

import org.apache.xmpbox.schema.*;
import org.verapdf.model.impl.pb.xmp.schemas.*;
import org.verapdf.model.tools.XMPHelper;

/**
 * Wraps xmpbox schema object into PBXMPSchema object based on it's NSURI
 * Created by bezrukov on 6/22/15.
 *
 * @author Maksim Bezrukov
 */
public class PBSchemaFactory {

    /**
     * Creates schema object based on it's NSURI
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
            case XMPHelper.NSPDFAFIELD:
                resultSchema = new PBPDFAFieldSchema(schema);
                break;
            case XMPHelper.NSPDFAPROPERTY:
                resultSchema = new PBPDFAPropertySchema(schema);
                break;
            case XMPHelper.NSPDFASCHEMA:
                resultSchema = new PBPDFASchemaSchema(schema);
                break;
            case XMPHelper.NSPDFATYPE:
                resultSchema = new PBPDFATypeSchema(schema);
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
            case XMPHelper.NSIPTC4XMPCORE:
                resultSchema = new PBIptc4xmpCoreSchema(schema);
                break;
            case XMPHelper.NSPDFE:
                resultSchema = new PBPDFESchema(schema);
                break;
            case XMPHelper.NSPDFX:
                resultSchema = new PBPDFXSchema(schema);
                break;
            case XMPHelper.NSPDFXID:
                resultSchema = new PBPDFXIdSchema(schema);
                break;
            case XMPHelper.NSXMPDM:
                resultSchema = new PBXMPDMSchema(schema);
                break;
            case XMPHelper.NSXAPS:
                resultSchema = new PBXAPSSchema(schema);
                break;
            case XMPHelper.NSXAPG:
                resultSchema = new PBXAPGSchema(schema);
                break;
            case XMPHelper.NSSTFNT:
                resultSchema = new PBStFntSchema(schema);
                break;
            default:
                if (XMPHelper.isPredifinedSchema(schema.getNamespace())) {
                    resultSchema = new PBXMPSchema(schema);
                } else {
                    resultSchema = new PBXMPCustomSchema(schema);
                }
        }

        return resultSchema;
    }
}
