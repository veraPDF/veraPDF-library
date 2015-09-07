package org.verapdf.model.impl.pb.xmp.schemas;

import org.apache.xmpbox.schema.PhotoshopSchema;

/**
 * Current class is representation of PhotoshopSchema interface from
 * abstract model based on xmpbox from pdfbox.
 *
 * @author Maksim Bezrukov
 */
public class PBPhotoshopSchema extends PBXMPPredefinedSchema
		implements org.verapdf.model.xmplayer.PhotoshopSchema {

	public static final String PHOTOSHOP_SCHEMA_TYPE = "PhotoshopSchema";

	/**
	 * Constructs new object
	 *
	 * @param xmpSchema object from xmpbox represented this schema
	 */
	public PBPhotoshopSchema(PhotoshopSchema xmpSchema) {
		super(xmpSchema, PHOTOSHOP_SCHEMA_TYPE);
	}

	/**
	 * @return property Author of the schema
	 */
	@Override
	public String getauthor() {
		return this.getProperty(AUTHOR);
	}

	/**
	 * @return Copyright part of the schema
	 */
	@Override
	public String getcopyright() {
		return this.getProperty(COPYRIGHT);
	}

	/**
	 * @return History part of the schema
	 */
	@Override
	public String gethistory() {
		return ((PhotoshopSchema) this.getXmpSchema()).getHistory();
	}

	/**
	 * @return Title part of the schema
	 */
	@Override
	public String gettitle() {
		return this.getProperty(TITLE);
	}
}
