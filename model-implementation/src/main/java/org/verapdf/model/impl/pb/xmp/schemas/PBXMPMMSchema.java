package org.verapdf.model.impl.pb.xmp.schemas;

import org.apache.xmpbox.schema.XMPMediaManagementSchema;
import org.verapdf.model.xmplayer.XMPMMSchema;

/**
 * Current class is representation of XMPMMSchema interface from
 * abstract model based on xmpbox from pdfbox.
 *
 * @author Maksim Bezrukov
 */
public class PBXMPMMSchema extends PBXMPPredefinedSchema implements XMPMMSchema {

	public static final String XMP_MM_SCHEMA_TYPE = "XMPMMSchema";

	private static final String MANIFEST = "Manifest";

	/**
	 * Constructs new object
	 *
	 * @param xmpSchema object from xmpbox represented this schema
	 */
	public PBXMPMMSchema(XMPMediaManagementSchema xmpSchema) {
		super(xmpSchema, XMP_MM_SCHEMA_TYPE);
	}

	/**
	 * @return property Manifest of the schema
	 */
	@Override
	public String getmanifest() {
		return this.getProperty(MANIFEST);
	}

}
