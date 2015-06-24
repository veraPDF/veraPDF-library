package org.verapdf.model.impl.pb.xmp.schemas;

import org.verapdf.model.impl.pb.xmp.PBXMPObject;
import org.verapdf.model.xmplayer.XMPSchema;

/**
 * Current class is representation of XMPSchema interface from abstract model based on xmpbox from pdfbox.
 *
 * @author Maksim Bezrukov
 */
public class PBXMPSchema extends PBXMPObject implements XMPSchema {

    private static final String XMPSCHEMA = "XMPSchema";

    protected org.apache.xmpbox.schema.XMPSchema xmpSchema;

    /**
     * Constructs new object
     *
     * @param xmpSchema - object from xmpbox represented this schema
     */
    public PBXMPSchema(org.apache.xmpbox.schema.XMPSchema xmpSchema) {
        setType(XMPSCHEMA);
        this.xmpSchema = xmpSchema;
    }

    /**
     * @return uri of the namespace
     */
    @Override
    public String geturi() {
        return xmpSchema.getNamespace();
    }

    /**
     * @return prefix of the namespace
     */
    @Override
    public String getprefix() {
        return xmpSchema.getPrefix();
    }

}
