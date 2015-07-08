package org.verapdf.model.factory.xmp;

import org.apache.xmpbox.schema.*;
import org.verapdf.model.impl.pb.xmp.schemas.*;
import org.verapdf.model.tools.XMPHelper;

/**
 * Wraps xmpbox schema object into PBXMPSchema object based on it's NSURI
 *
 * @author Maksim Bezrukov
 */
public final class PBSchemaFactory {

    private PBSchemaFactory() {
    }

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
                if (schema instanceof PDFAIdentificationSchema) {
                    resultSchema = new PBPDFAIdSchema((PDFAIdentificationSchema) schema);
                } else {
                    throw getSchemaException(schema, "PDFAIdentificationSchema");
                }
                break;
            case XMPHelper.NSPDFAEXTENSION:
                if (schema instanceof PDFAExtensionSchema) {
                    resultSchema = new PBPDFAExtensionSchema((PDFAExtensionSchema) schema);
                } else {
                    throw getSchemaException(schema, "PDFAExtensionSchema");
                }
                break;
            case XMPHelper.NSXMPBASIC:
                if (schema instanceof XMPBasicSchema) {
                    resultSchema = new PBXMPBasicSchema((XMPBasicSchema) schema);
                } else {
                    throw getSchemaException(schema, "XMPBasicSchema");
                }
                break;
            case XMPHelper.NSXMPRIGHTS:
                if (schema instanceof XMPRightsManagementSchema) {
                    resultSchema = new PBXMPRightsSchema((XMPRightsManagementSchema) schema);
                } else {
                    throw getSchemaException(schema, "XMPRightsManagementSchema");
                }
                break;
            case XMPHelper.NSXMPMM:
                if (schema instanceof XMPMediaManagementSchema) {
                    resultSchema = new PBXMPMMSchema((XMPMediaManagementSchema) schema);
                } else {
                    throw getSchemaException(schema, "XMPMediaManagementSchema");
                }
                break;
            case XMPHelper.NSXMPTPG:
                if (schema instanceof XMPageTextSchema) {
                    resultSchema = new PBXMPTPgSchema((XMPageTextSchema) schema);
                } else {
                    throw getSchemaException(schema, "XMPageTextSchema");
                }
                break;
            case XMPHelper.NSPDF:
                if (schema instanceof  AdobePDFSchema) {
                    resultSchema = new PBPDFSchema((AdobePDFSchema) schema);
                } else {
                    throw getSchemaException(schema, "AdobePDFSchema");
                }
                break;
            case XMPHelper.NSPHOTOSHOP:
                if (schema instanceof PhotoshopSchema) {
                    resultSchema = new PBPhotoshopSchema((PhotoshopSchema) schema);
                } else {
                    throw getSchemaException(schema, "PhotoshopSchema");
                }
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

    private static ClassCastException getSchemaException(XMPSchema schema, String className) {
        return new ClassCastException("Schema with namespace " + schema.getNamespace() + "must be " + className + "class. Real class: " + schema.getClass().getName() + ".");
    }
}
