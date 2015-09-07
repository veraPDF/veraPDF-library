package org.verapdf.model.impl.pb.xmp.schemas;

import org.apache.xmpbox.schema.XMPBasicSchema;

/**
 * Current class is representation of XMPBasicSchema interface from
 * abstract model based on xmpbox from pdfbox.
 *
 * @author Maksim Bezrukov
 */
public class PBXMPBasicSchema extends PBXMPPredefinedSchema
		implements org.verapdf.model.xmplayer.XMPBasicSchema {

	public static final String XMP_BASIC_SCHEMA_TYPE = "XMPBasicSchema";

	private static final String DESCRIPTION = "Description";

	/**
	 * Constructs new object
	 *
	 * @param xmpSchema object from xmpbox represented this schema
	 */
	public PBXMPBasicSchema(org.apache.xmpbox.schema.XMPBasicSchema xmpSchema) {
		super(xmpSchema, XMP_BASIC_SCHEMA_TYPE);
	}

	/**
	 * @return property Author of the schema
	 */
	@Override
	public String getauthor() {
		return this.getProperty(AUTHOR);
	}

	/**
	 * @return property Description of the schema
	 */
	@Override
	public String getdescription() {
		return this.getProperty(DESCRIPTION);
	}

	/**
	 * @return property Label of the schema
	 */
	@Override
	public String getlabel() {
		return ((XMPBasicSchema) this.getXmpSchema()).getLabel();
	}

	/**
	 * @return property Rating of the schema
	 */
	@Override
	public Long getrating() {
		Integer rating = ((XMPBasicSchema) this.getXmpSchema()).getRating();
		return rating == null ? null : Long.valueOf(rating.longValue());
	}

	/**
	 * @return property Title of the schema
	 */
	@Override
	public String gettitle() {
		return this.getProperty(TITLE);
	}
}
