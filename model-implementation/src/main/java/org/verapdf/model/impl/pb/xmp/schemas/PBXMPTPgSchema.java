package org.verapdf.model.impl.pb.xmp.schemas;

import org.apache.xmpbox.schema.XMPageTextSchema;
import org.verapdf.model.xmplayer.XMPTPgSchema;

/**
 * Current class is representation of XMPTPgSchema interface from
 * abstract model based on xmpbox from pdfbox.
 *
 * @author Maksim Bezrukov
 */
public class PBXMPTPgSchema extends PBXMPPredefinedSchema implements XMPTPgSchema {

	public static final String XMP_TPG_SCHEMA_TYPE = "XMPTPgSchema";

	private static final String COLORANTS = "Colorants";
	private static final String FONTS = "Fonts";
	private static final String PLATE_NAMES = "PlateNames";

	/**
	 * Constructs new object
	 *
	 * @param xmpSchema - object from xmpbox represented this schema
	 */
	public PBXMPTPgSchema(XMPageTextSchema xmpSchema) {
		super(xmpSchema, XMP_TPG_SCHEMA_TYPE);
	}

	/**
	 * @return property Colorants of the schema
	 */
	@Override
	public String getcolorants() {
		return this.getProperty(COLORANTS);
	}

	/**
	 * @return property Fonts of the schema
	 */
	@Override
	public String getfonts() {
		return this.getProperty(FONTS);
	}

	/**
	 * @return property PlateNames of the schema
	 */
	@Override
	public String getplateNames() {
		return this.getProperty(PLATE_NAMES);
	}

}
