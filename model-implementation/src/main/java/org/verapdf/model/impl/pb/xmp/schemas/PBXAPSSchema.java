package org.verapdf.model.impl.pb.xmp.schemas;

import org.apache.xmpbox.schema.XMPSchema;
import org.verapdf.model.xmplayer.XAPSSchema;

/**
 * Current class is representation of XAPSSchema interface from abstract model based on xmpbox from pdfbox.
 *
 * @author Maksim Bezrukov
 */
public class PBXAPSSchema extends PBXMPPredefinedSchema implements XAPSSchema {

    private static final String XAPSSCHEMA = "XAPSSchema";

    /**
     * Constructs new object
     *
     * @param xmpSchema - object from xmpbox represented this schema
     */
    public PBXAPSSchema(XMPSchema xmpSchema) {
        super(xmpSchema);
        setType(XAPSSCHEMA);
    }

}
