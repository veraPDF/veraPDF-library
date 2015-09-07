package org.verapdf.model.impl.pb.xmp.schemas;

import org.verapdf.model.impl.pb.xmp.PBXMPObject;
import org.verapdf.model.xmplayer.XMPSchema;

/**
 * Current class is representation of XMPSchema interface from
 * abstract model based on xmpbox from pdfbox.
 *
 * @author Maksim Bezrukov
 */
public class PBXMPSchema extends PBXMPObject implements XMPSchema {

	private org.apache.xmpbox.schema.XMPSchema xmpSchema;

	/**
	 * Constructs new object
	 *
	 * @param xmpSchema object from xmpbox represented this schema
	 * @param type      type of current object
	 */
	public PBXMPSchema(org.apache.xmpbox.schema.XMPSchema xmpSchema,
					   final String type) {
		super(type);
		this.xmpSchema = xmpSchema;
	}

	/**
	 * @return uri of the namespace
	 */
	@Override
	public String geturi() {
		return this.xmpSchema.getNamespace();
	}

	/**
	 * @return prefix of the namespace
	 */
	@Override
	public String getprefix() {
		return this.xmpSchema.getPrefix();
	}

	protected org.apache.xmpbox.schema.XMPSchema getXmpSchema() {
		return this.xmpSchema;
	}

}
