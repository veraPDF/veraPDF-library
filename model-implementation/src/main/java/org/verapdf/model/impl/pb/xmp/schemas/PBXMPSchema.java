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

	public static final String XMPSCHEMA_TYPE = "XMPSchema";

	private final String namespace;
	private final String prefix;

	/**
	 * Constructs new object
	 *
	 * @param xmpSchema object from xmpbox represented this schema
	 */
	public PBXMPSchema(org.apache.xmpbox.schema.XMPSchema xmpSchema) {
		this(xmpSchema, XMPSCHEMA_TYPE);
	}

	protected PBXMPSchema(org.apache.xmpbox.schema.XMPSchema xmpSchema,
					   final String type) {
		super(type);
		this.namespace = xmpSchema.getNamespace();
		this.prefix = xmpSchema.getPrefix();
	}

	/**
	 * @return uri of the namespace
	 */
	@Override
	public String geturi() {
		return this.namespace;
	}

	/**
	 * @return prefix of the namespace
	 */
	@Override
	public String getprefix() {
		return this.prefix;
	}

}
