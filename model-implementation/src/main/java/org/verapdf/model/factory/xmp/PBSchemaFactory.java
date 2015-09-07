package org.verapdf.model.factory.xmp;

import org.apache.log4j.Logger;
import org.apache.xmpbox.schema.*;
import org.verapdf.model.impl.pb.xmp.schemas.*;
import org.verapdf.model.tools.XMPHelper;

/**
 * Wraps xmpbox schema object into PBXMPSchema object based on it's NSURI
 *
 * @author Maksim Bezrukov
 */
public final class PBSchemaFactory {

	private static final Logger LOGGER = Logger
			.getLogger(PBSchemaFactory.class);

	private PBSchemaFactory() {
		// disable default constructor
	}

	/**
	 * Creates schema object based on it's NSURI
	 *
	 * @param schema xmpbox schema
	 * @return PBXMPSchema wrapper of the xmpbox's schema
	 */
	public static PBXMPSchema createSchema(XMPSchema schema) {
		if (schema == null) {
			return null;
		}

		PBXMPSchema resultSchema = null;

		switch (schema.getNamespace()) {
			case XMPHelper.NSPDFAID:
				if (schema instanceof PDFAIdentificationSchema) {
					resultSchema = new PBPDFAIdSchema((PDFAIdentificationSchema) schema);
				} else {
					schemaExceptionMessage("PDFAIdentificationSchema", XMPHelper.NSPDFAID);
				}
				break;
			case XMPHelper.NSPDFAEXTENSION:
				if (schema instanceof PDFAExtensionSchema) {
					resultSchema = new PBPDFAExtensionSchema((PDFAExtensionSchema) schema);
				} else {
					schemaExceptionMessage("PDFAExtensionSchema", XMPHelper.NSPDFAEXTENSION);
				}
				break;
			case XMPHelper.NSXMPBASIC:
				if (schema instanceof XMPBasicSchema) {
					resultSchema = new PBXMPBasicSchema((XMPBasicSchema) schema);
				} else {
					schemaExceptionMessage("XMPBasicSchema", XMPHelper.NSXMPBASIC);
				}
				break;
			case XMPHelper.NSXMPRIGHTS:
				if (schema instanceof XMPRightsManagementSchema) {
					resultSchema = new PBXMPRightsSchema((XMPRightsManagementSchema) schema);
				} else {
					schemaExceptionMessage("XMPRightsManagementSchema", XMPHelper.NSXMPRIGHTS);
				}
				break;
			case XMPHelper.NSXMPMM:
				if (schema instanceof XMPMediaManagementSchema) {
					resultSchema = new PBXMPMMSchema((XMPMediaManagementSchema) schema);
				} else {
					schemaExceptionMessage("XMPMediaManagementSchema", XMPHelper.NSXMPMM);
				}
				break;
			case XMPHelper.NSXMPTPG:
				if (schema instanceof XMPageTextSchema) {
					resultSchema = new PBXMPTPgSchema((XMPageTextSchema) schema);
				} else {
					schemaExceptionMessage("XMPageTextSchema", XMPHelper.NSXMPTPG);
				}
				break;
			case XMPHelper.NSPDF:
				if (schema instanceof AdobePDFSchema) {
					resultSchema = new PBPDFSchema((AdobePDFSchema) schema);
				} else {
					schemaExceptionMessage("AdobePDFSchema", XMPHelper.NSPDF);
				}
				break;
			case XMPHelper.NSPHOTOSHOP:
				if (schema instanceof PhotoshopSchema) {
					resultSchema = new PBPhotoshopSchema((PhotoshopSchema) schema);
				} else {
					schemaExceptionMessage("PhotoshopSchema", XMPHelper.NSPHOTOSHOP);
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

	private static void schemaExceptionMessage(String className, String namespace) {
		LOGGER.fatal("Founded not " + className + "schema with namespace " + namespace);
	}

}
