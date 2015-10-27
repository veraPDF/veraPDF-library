package org.verapdf.model.factory.xmp;

import org.apache.log4j.Logger;
import org.apache.xmpbox.schema.PDFAIdentificationSchema;
import org.apache.xmpbox.schema.XMPSchema;
import org.verapdf.model.impl.pb.xmp.schemas.PBPDFAIdSchema;
import org.verapdf.model.impl.pb.xmp.schemas.PBXMPSchema;
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
			default:
				resultSchema = new PBXMPSchema(schema);
		}

		return resultSchema;
	}

	private static void schemaExceptionMessage(String className, String namespace) {
		LOGGER.fatal("Founded not " + className + "schema with namespace " + namespace);
	}

}
