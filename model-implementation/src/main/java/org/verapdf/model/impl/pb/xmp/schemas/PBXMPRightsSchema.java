package org.verapdf.model.impl.pb.xmp.schemas;

import org.apache.xmpbox.schema.XMPRightsManagementSchema;
import org.verapdf.model.xmplayer.XMPRightsSchema;

/**
 * Current class is representation of XMPRightsSchema interface from
 * abstract model based on xmpbox from pdfbox.
 *
 * @author Maksim Bezrukov
 */
public class PBXMPRightsSchema extends PBXMPPredefinedSchema implements XMPRightsSchema {

	public static final String XMP_RIGHTS_SCHEMA_TYPE = "XMPRightsSchema";

	/**
	 * Constructs new object
	 *
	 * @param xmpSchema object from xmpbox represented this schema
	 */
	public PBXMPRightsSchema(XMPRightsManagementSchema xmpSchema) {
		super(xmpSchema, XMP_RIGHTS_SCHEMA_TYPE);
	}

	/**
	 * @return property Copyright of the schema
	 */
	@Override
	public String getcopyright() {
		return this.getProperty(COPYRIGHT);
	}
}
