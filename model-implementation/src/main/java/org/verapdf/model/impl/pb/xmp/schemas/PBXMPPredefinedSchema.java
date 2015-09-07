package org.verapdf.model.impl.pb.xmp.schemas;

import org.apache.xmpbox.schema.XMPSchema;
import org.apache.xmpbox.type.AbstractField;
import org.verapdf.model.xmplayer.XMPPredefinedSchema;

/**
 * Current class is representation of XMPPredefinedSchema
 * interface from abstract model based on xmpbox from pdfbox.
 *
 * @author Maksim Bezrukov
 */
public class PBXMPPredefinedSchema extends PBXMPSchema
		implements XMPPredefinedSchema {

	public static final String XMP_PREDEFINED_SCHEMA_TYPE =
			"XMPPredefinedSchema";

	protected static final String AUTHOR = "Author";
	protected static final String TITLE = "Title";
	protected static final String COPYRIGHT = "Copyright";

	/**
	 * Constructs new object
	 *
	 * @param xmpSchema object from xmpbox represented
	 *                  this schema
	 */
	public PBXMPPredefinedSchema(XMPSchema xmpSchema) {
		super(xmpSchema, XMP_PREDEFINED_SCHEMA_TYPE);
	}

	/**
	 * Constructs new object
	 *
	 * @param xmpSchema object from xmpbox represented
	 *                  this schema
	 * @param type      type of current object
	 */
	public PBXMPPredefinedSchema(XMPSchema xmpSchema,
								 final String type) {
		super(xmpSchema, type);
	}

	/**
	 * @return String that contains all unknown properties
	 * for predefined schema (XMP 2005)
	 */
	@Override
	public String getunknownProperties() {
		// TODO: implement this
		return null;
	}

	/**
	 * @return String that contains all wrong value type
	 * properties for predefined schema
	 */
	@Override
	public String getwrongValueTypeProperties() {
		// TODO: implement this
		return null;
	}

	protected String getProperty(String property) {
		AbstractField xmpProperty = this.getXmpSchema()
				.getProperty(property);
		return xmpProperty == null ? null : xmpProperty
				.toString();
	}

}
