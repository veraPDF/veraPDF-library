package org.verapdf.model.impl.pb.xmp.schemas;

import org.apache.xmpbox.schema.XMPSchema;
import org.verapdf.model.xmplayer.XMPPredefinedSchema;

/**
 * Current class is representation of XMPPredefinedSchema interface from abstract model based on xmpbox from pdfbox.
 *
 * @author Maksim Bezrukov
 */
public class PBXMPPredefinedSchema extends PBXMPSchema implements XMPPredefinedSchema {

    public static final String XMP_PREDEFINED_SCHEMA = "XMPPredefinedSchema";

    /**
     * Constructs new object
     *
     * @param xmpSchema - object from xmpbox represented this schema
     */
    public PBXMPPredefinedSchema(XMPSchema xmpSchema) {
        super(xmpSchema, XMP_PREDEFINED_SCHEMA);
    }

	/**
	 * Constructs new object
	 *
	 * @param xmpSchema - object from xmpbox represented this schema
	 * @param type - type of current object
	 */
	public PBXMPPredefinedSchema(XMPSchema xmpSchema, final String type) {
		super(xmpSchema, type);
	}

    /**
     * @return String that contains all unknown properties for predefined schema (XMP 2005)
     */
    @Override
    public String getunknownProperties() {
        // TODO: implement this
        return null;
    }

    /**
     * @return String that contains all wrong value type properties for predefined schema
     */
    @Override
    public String getwrongValueTypeProperties() {
        // TODO: implement this
        return null;
    }
}
