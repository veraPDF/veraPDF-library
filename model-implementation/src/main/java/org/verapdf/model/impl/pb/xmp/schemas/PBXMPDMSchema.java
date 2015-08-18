package org.verapdf.model.impl.pb.xmp.schemas;

import org.apache.xmpbox.schema.XMPSchema;
import org.verapdf.model.xmplayer.XMPDMSchema;

/**
 * Current class is representation of XMPDMSchema interface from abstract model based on xmpbox from pdfbox.
 *
 * @author Maksim Bezrukov
 */
public class PBXMPDMSchema extends PBXMPPredefinedSchema implements XMPDMSchema {

    private static final String XMP_DM_SCHEMA = "XMPDMSchema";

    /**
     * Constructs new object
     *
     * @param xmpSchema - object from xmpbox represented this schema
     */
    public PBXMPDMSchema(XMPSchema xmpSchema) {
        super(xmpSchema, XMP_DM_SCHEMA);
    }

}
