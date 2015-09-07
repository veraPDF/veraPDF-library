package org.verapdf.model.impl.pb.xmp.schemas;

import org.apache.xmpbox.schema.XMPSchema;
import org.verapdf.model.xmplayer.CRSSchema;

/**
 * Current class is representation of CRSSchema interface from abstract model based on xmpbox from pdfbox.
 *
 * @author Maksim Bezrukov
 */
public class PBCRSSchema extends PBXMPPredefinedSchema implements CRSSchema {

	public static final String CRS_SCHEMA = "CRSSchema";

	/**
	 * Constructs new object
	 *
	 * @param xmpSchema object from xmpbox represented this schema
	 */
	public PBCRSSchema(XMPSchema xmpSchema) {
		super(xmpSchema, CRS_SCHEMA);
	}

}
