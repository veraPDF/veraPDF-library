package org.verapdf.model.impl.pb.xmp.schemas;

import org.verapdf.model.impl.pb.xmp.PBXMPObject;
import org.verapdf.model.xmplayer.XMPSchema;

/**
 * Current class is representation of XMPSchema interface from abstract model based on xmpbox from pdfbox.
 * Created by bezrukov on 6/19/15.
 *
 * @author Maksim Bezrukov
 */
public class PBXMPSchema extends PBXMPObject implements XMPSchema {

    protected org.apache.xmpbox.schema.XMPSchema xmpSchema;

    /**
     * Constructs new object
     * @param xmpSchema - object from xmpbox represented this schema
     */
    public PBXMPSchema(org.apache.xmpbox.schema.XMPSchema xmpSchema) {
        setType("XMPSchema");
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
